package com.technophobia.eclipse.transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class SiteToJavaProjectTransformerTest {

    private Mockery context;

    private IWorkbenchSite site;
    private Transformer<IProject, IJavaProject> projectToJavaProjectTransformer;

    private Transformer<IWorkbenchSite, IJavaProject> siteToJavaProjectTransformer;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.site = context.mock(IWorkbenchSite.class);
        this.projectToJavaProjectTransformer = context.mock(Transformer.class);

        this.siteToJavaProjectTransformer = new SiteToJavaProjectTransformer(projectToJavaProjectTransformer);
    }


    @Test
    public void canGetJavaProjectBasedOnSelection() throws Exception {

        final IWorkbenchWindow workbenchWindow = context.mock(IWorkbenchWindow.class);
        final ISelectionService selectionService = context.mock(ISelectionService.class);
        final IStructuredSelection selection = context.mock(IStructuredSelection.class);
        final IResource resource = context.mock(IResource.class);
        final IProject project = context.mock(IProject.class);
        final IJavaProject javaProject = context.mock(IJavaProject.class);

        context.checking(new Expectations() {
            {
                oneOf(site).getWorkbenchWindow();
                will(returnValue(workbenchWindow));

                oneOf(workbenchWindow).getSelectionService();
                will(returnValue(selectionService));

                oneOf(selectionService).getSelection();
                will(returnValue(selection));

                oneOf(selection).getFirstElement();
                will(returnValue(resource));

                oneOf(resource).getProject();
                will(returnValue(project));

                oneOf(projectToJavaProjectTransformer).to(project);
                will(returnValue(javaProject));
            }
        });

        assertThat(siteToJavaProjectTransformer.to(site), is(javaProject));
    }


    @Test
    public void canGetJavaProjectBasedOnActiveEditor() {
        final IWorkbenchWindow workbenchWindow = context.mock(IWorkbenchWindow.class);
        final ISelectionService selectionService = context.mock(ISelectionService.class);
        final IWorkbenchPage page = context.mock(IWorkbenchPage.class);
        final IEditorPart editorPart = context.mock(IEditorPart.class);
        final IEditorInput editorInput = context.mock(IEditorInput.class);
        final IFile file = context.mock(IFile.class);
        final IProject project = context.mock(IProject.class);
        final IJavaProject javaProject = context.mock(IJavaProject.class);

        context.checking(new Expectations() {
            {
                oneOf(site).getWorkbenchWindow();
                will(returnValue(workbenchWindow));

                oneOf(workbenchWindow).getSelectionService();
                will(returnValue(selectionService));

                oneOf(selectionService).getSelection();
                will(returnValue(null));

                oneOf(site).getPage();
                will(returnValue(page));

                oneOf(page).getActiveEditor();
                will(returnValue(editorPart));

                oneOf(editorPart).getEditorInput();
                will(returnValue(editorInput));

                oneOf(editorInput).getAdapter(IFile.class);
                will(returnValue(file));

                oneOf(file).getProject();
                will(returnValue(project));

                oneOf(projectToJavaProjectTransformer).to(project);
                will(returnValue(javaProject));
            }
        });

        assertThat(siteToJavaProjectTransformer.to(site), is(javaProject));
    }


    @Test
    public void noSelectionsOrEditorsReturnsNullJavaProject() {
        final IWorkbenchWindow workbenchWindow = context.mock(IWorkbenchWindow.class);
        final ISelectionService selectionService = context.mock(ISelectionService.class);
        final IWorkbenchPage page = context.mock(IWorkbenchPage.class);

        context.checking(new Expectations() {
            {
                oneOf(site).getWorkbenchWindow();
                will(returnValue(workbenchWindow));

                oneOf(workbenchWindow).getSelectionService();
                will(returnValue(selectionService));

                oneOf(selectionService).getSelection();
                will(returnValue(null));

                oneOf(site).getPage();
                will(returnValue(page));

                oneOf(page).getActiveEditor();
                will(returnValue(null));
            }
        });

        assertThat(siteToJavaProjectTransformer.to(site), is(nullValue()));
    }
}
