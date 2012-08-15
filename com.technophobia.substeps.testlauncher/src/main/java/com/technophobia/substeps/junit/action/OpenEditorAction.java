package com.technophobia.substeps.junit.action;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameMatch;
import org.eclipse.jdt.core.search.TypeNameMatchRequestor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import com.technophobia.eclipse.ui.NotifyingUiUpdater;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.supplier.Supplier;

public abstract class OpenEditorAction extends Action {
    protected String className;
    private final boolean activate;
    private final Supplier<SubstepsRunSession> substepsRunSessionSupplier;
    private final NotifyingUiUpdater<String> infoMessageUpdater;
    private final Shell shell;


    protected OpenEditorAction(final Supplier<SubstepsRunSession> substepsRunSessionSupplier,
            final NotifyingUiUpdater<String> infoMessageUpdater, final Shell shell, final String testClassName) {
        this(substepsRunSessionSupplier, infoMessageUpdater, shell, testClassName, true);
    }


    public OpenEditorAction(final Supplier<SubstepsRunSession> substepsRunSessionSupplier,
            final NotifyingUiUpdater<String> infoMessageUpdater, final Shell shell, final String className,
            final boolean activate) {
        super(SubstepsFeatureMessages.OpenEditorAction_action_label);
        this.substepsRunSessionSupplier = substepsRunSessionSupplier;
        this.infoMessageUpdater = infoMessageUpdater;
        this.shell = shell;
        this.className = className;
        this.activate = activate;
    }


    /*
     * @see IAction#run()
     */
    @Override
    public void run() {
        IEditorPart editor = null;
        try {
            final IJavaElement element = findElement(getLaunchedProject(), className);
            if (element == null) {
                MessageDialog.openError(getShell(), SubstepsFeatureMessages.OpenEditorAction_error_cannotopen_title,
                        SubstepsFeatureMessages.OpenEditorAction_error_cannotopen_message);
                return;
            }
            editor = JavaUI.openInEditor(element, activate, false);
        } catch (final CoreException e) {
            ErrorDialog.openError(getShell(), SubstepsFeatureMessages.OpenEditorAction_error_dialog_title,
                    SubstepsFeatureMessages.OpenEditorAction_error_dialog_message, e.getStatus());
            return;
        }
        if (!(editor instanceof ITextEditor)) {
            infoMessageUpdater.notify(SubstepsFeatureMessages.OpenEditorAction_message_cannotopen);
            return;
        }
        reveal((ITextEditor) editor);
    }


    protected Shell getShell() {
        return shell;
    }


    /**
     * @return the Java project, or <code>null</code>
     */
    protected IJavaProject getLaunchedProject() {
        return substepsRunSessionSupplier.get().getLaunchedProject();
    }


    protected String getClassName() {
        return className;
    }


    protected abstract IJavaElement findElement(IJavaProject project, String testClassName) throws CoreException;


    protected abstract void reveal(ITextEditor editor);


    protected final IType findType(final IJavaProject project, final String testClassName) {
        final IType[] result = { null };
        final String dottedName = testClassName.replace('$', '.'); // for nested
        // classes...
        try {
            PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
                @Override
                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        if (project != null) {
                            result[0] = internalFindType(project, dottedName, new HashSet<IJavaProject>(), monitor);
                        }
                        if (result[0] == null) {
                            final int lastDot = dottedName.lastIndexOf('.');
                            final TypeNameMatchRequestor nameMatchRequestor = new TypeNameMatchRequestor() {
                                @Override
                                public void acceptTypeNameMatch(final TypeNameMatch match) {
                                    result[0] = match.getType();
                                }
                            };
                            new SearchEngine().searchAllTypeNames(lastDot >= 0 ? dottedName.substring(0, lastDot)
                                    .toCharArray() : null,
                                    SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE,
                                    (lastDot >= 0 ? dottedName.substring(lastDot + 1) : dottedName).toCharArray(),
                                    SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE,
                                    IJavaSearchConstants.TYPE, SearchEngine.createWorkspaceScope(), nameMatchRequestor,
                                    IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, monitor);
                        }
                    } catch (final JavaModelException e) {
                        throw new InvocationTargetException(e);
                    }
                }
            });
        } catch (final InvocationTargetException e) {
            FeatureRunnerPlugin.log(e);
        } catch (final InterruptedException e) {
            // user cancelled
        }
        return result[0];
    }


    private IType internalFindType(final IJavaProject project, final String testClassName,
            final Set<IJavaProject> visitedProjects, final IProgressMonitor monitor) throws JavaModelException {
        try {
            if (visitedProjects.contains(project))
                return null;
            monitor.beginTask("", 2); //$NON-NLS-1$
            IType type = project.findType(testClassName, new SubProgressMonitor(monitor, 1));
            if (type != null)
                return type;
            // fix for bug 87492: visit required projects explicitly to also
            // find not exported types
            visitedProjects.add(project);
            final IJavaModel javaModel = project.getJavaModel();
            final String[] requiredProjectNames = project.getRequiredProjectNames();
            final IProgressMonitor reqMonitor = new SubProgressMonitor(monitor, 1);
            reqMonitor.beginTask("", requiredProjectNames.length); //$NON-NLS-1$
            for (final String requiredProjectName : requiredProjectNames) {
                final IJavaProject requiredProject = javaModel.getJavaProject(requiredProjectName);
                if (requiredProject.exists()) {
                    type = internalFindType(requiredProject, testClassName, visitedProjects, new SubProgressMonitor(
                            reqMonitor, 1));
                    if (type != null)
                        return type;
                }
            }
            return null;
        } finally {
            monitor.done();
        }
    }
}