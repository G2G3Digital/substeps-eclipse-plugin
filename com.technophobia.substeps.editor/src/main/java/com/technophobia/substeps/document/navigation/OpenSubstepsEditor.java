package com.technophobia.substeps.document.navigation;

import static com.technophobia.substeps.FeatureEditorPlugin.instance;
import static com.technophobia.substeps.document.navigation.SubstepsFileUtils.outputPaths;
import static com.technophobia.substeps.document.navigation.SubstepsFileUtils.searchForFile;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.editor.SubstepsEditor;
import com.technophobia.substeps.model.ParentStep;

/**
 * @author rpopple
 * 
 */
public class OpenSubstepsEditor {

    private static final String SUBSTEPS_EDITOR_KEY = "com.technophobia.substeps.editor.substepsEditor";

    public static void open(final IWorkbenchPage page, final IJavaProject project, final ParentStep parentStep) {
        final IFile substepsIFile = substepIFile(project, parentStep.getParent().getSource());
        final int lineNumber = parentStep.getSourceLineNumber();

        instance().info(
                "parentStep.getSourceLineNumber: " + lineNumber + " for line: " + parentStep.getParent().getLine());

        final IEditorInput input = new FileEditorInput(substepsIFile);
        try {
            final IEditorPart substepsEditor = page.openEditor(input, SUBSTEPS_EDITOR_KEY);
            selectLineInEditor(lineNumber, substepsEditor);
        } catch (final PartInitException e) {
            FeatureEditorPlugin.instance().error("exception opening substep", e);
        }
    }


    private static URI findOutputUri(IJavaProject project, IClasspathEntry entry) {
        assert (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE);
        try {
            // The default output and the specific out will be relative if they
            // exist. As an example we might have:
            // "/weddriver-substeps-example/target/classes"
            final IPath defaultOutputPath = project.getOutputLocation();
            final IPath specificOutputPath = entry.getOutputLocation();
            final IPath outputPath = specificOutputPath != null ? specificOutputPath : defaultOutputPath;
            // Project path is relative too - so for example we might have:
            // "/webdriver-substeps-example"
            final IPath projectPath = project.getProject().getFullPath();
            // Find out how may segments we match on, starting from the
            // beginning. So in the example given so far we have a single
            // matching segment at the start.
            final int matchSegs = outputPath.matchingFirstSegments(projectPath);
            // Remove the matching segments, so in the given example we have:
            // "/target/classes"
            final IPath removeFirstSegments = outputPath.removeFirstSegments(matchSegs);
            // Now we should be able to get hold of the output IFile directory.
            final IFile outputFile = project.getProject().getFile(removeFirstSegments);
            // Now that we have an output IFile directory we can get hold of the
            // absolute URI.For example:
            // "file:/home/auser/Substeps/Code/example-substeps-project/target/classes"
            return outputFile.getLocationURI();
        } catch (JavaModelException e) {
            final String error = "Unable to find the URI for IClasspathEntry:" + entry;
            instance().error(error, e);
            throw new RuntimeException(e);
        }
    }


    private static IFile substepIFile(final IJavaProject project, final File substepDefinitionFile) {
        try {
            final String substepOutputFile = substepDefinitionFile.getAbsolutePath();
            for (final IClasspathEntry entry : project.getRawClasspath()) {
                if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                    // We have an absolute path. For example:
                    // "file:/home/auser/Substeps/Code/example-substeps-project/target/test-classes"
                    final URI outputUri = findOutputUri(project, entry);
                    if (substepOutputFile.startsWith(outputUri.getPath())) {
                        // Make a relative path by removing the outputUri
                        // location from substepDefinitionFile absolute path. So
                        // for example here we might have:
                        // "/substeps/self-test.substeps"
                        final String relativePath = substepOutputFile.substring(outputUri.getPath().length());
                        // Get the source folder for example we have here:
                        // "entry.getPath(): /webdriver-substeps-example/src/test/java"
                        // And from this we get the source folder:
                        // "entry.getPath().removeFirstSegments(1): /src/test/java"
                        final IFolder sourceIFolder = project.getProject().getFolder(
                                entry.getPath().removeFirstSegments(1));
                        final IFile substepsIFile = sourceIFolder.getFile(relativePath);
                        if (substepsIFile.exists()) {
                            return substepsIFile;
                        }
                    }
                }
            }
            final List<IFile> matches = searchForFile(project.getProject(), substepDefinitionFile.getName(),
                    outputPaths(project));
            return matches.isEmpty() ? null : matches.get(0);
        } catch (final JavaModelException e) {
            final String error = "Unable to find the substeps IFile for File:" + substepDefinitionFile;
            instance().error(error, e);
            throw new RuntimeException(e);
        }

    }


    private static void selectLineInEditor(final int lineNumber, final IEditorPart newEditor) {
        final ISelectionProvider newSelectionProvider = newEditor.getEditorSite().getSelectionProvider();

        final SubstepsEditor newStepEditor = (SubstepsEditor) newEditor.getAdapter(SubstepsEditor.class);

        final IDocument newDocument = newStepEditor.getCurrentDocument();

        try {
            // this is zero based
            final IRegion lineInformation = newDocument.getLineInformation(lineNumber - 1);

            final int newOffset = lineInformation.getOffset();

            final int selectionLength = lineInformation.getLength();

            newSelectionProvider.setSelection(new TextSelection(newDocument, newOffset, selectionLength));

        } catch (final BadLocationException e) {
            FeatureEditorPlugin.instance().error("BadLocationException@ " + lineNumber, e);
        }
    }

}
