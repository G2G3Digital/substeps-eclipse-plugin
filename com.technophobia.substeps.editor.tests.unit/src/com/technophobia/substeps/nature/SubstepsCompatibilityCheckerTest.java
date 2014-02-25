package com.technophobia.substeps.nature;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.preferences.SubstepsPreferences;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class SubstepsCompatibilityCheckerTest {

    private Mockery context;

    private Transformer<IProject, IPersistentPreferenceStore> projectToPreferenceLookup;
    private CompatibilityChecker<IProject> compatibilityChecker;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.projectToPreferenceLookup = context.mock(Transformer.class);

        this.compatibilityChecker = new SubstepsCompatibilityChecker(projectToPreferenceLookup);
    }


    @Test
    public void projectIsNotCompatibleIfItIsAlreadyASubstepsProject() throws Exception {
        final IProject project = context.mock(IProject.class);

        prepareState(project, true, false);

        assertThat(compatibilityChecker.isCompatible(project), is(false));
    }


    @Test
    public void projectIsNotCompatibleIfItHasAlreadyBeenProcessed() throws Exception {
        final IProject project = context.mock(IProject.class);

        prepareState(project, false, true);

        assertThat(compatibilityChecker.isCompatible(project), is(false));
    }


    @Test
    public void projectIsNotCompatibleIfDoesNotHaveRelevantFolders() throws Exception {
        final IProject project = context.mock(IProject.class);

        prepareState(project, false, false);

        assertThat(compatibilityChecker.isCompatible(project), is(false));
    }


    @Test
    public void projectIsCompatibleIfItHasFeatureFolder() throws Exception {
        final IProject project = context.mock(IProject.class);

        prepareState(project, false, false, "src/main/resources/features");

        assertThat(compatibilityChecker.isCompatible(project), is(true));
    }


    @Test
    public void projectIsCompatibleIfItHasSubstepsFolder() throws Exception {
        final IProject project = context.mock(IProject.class);

        prepareState(project, false, false, "src/test/resources/substeps");

        assertThat(compatibilityChecker.isCompatible(project), is(true));
    }


    @Test
    public void markingProjectAsCompatibilityCheckedWillUpdatePreferenceStore() throws Exception {
        final IProject project = context.mock(IProject.class);
        final IPersistentPreferenceStore preferenceStore = context.mock(IPersistentPreferenceStore.class);

        context.checking(new Expectations() {
            {
                oneOf(projectToPreferenceLookup).from(project);
                will(returnValue(preferenceStore));

                oneOf(preferenceStore).setValue(SubstepsPreferences.SUBSTEPS_COMPATIBILITY_CHECKED.key(), true);

                oneOf(preferenceStore).save();
            }
        });

        compatibilityChecker.markResourceAsCompatibilityChecked(project);
    }


    private void prepareState(final IProject project, final boolean isSubstepsProject, final boolean hasBeenProcessed,
            final String... folders) throws Exception {

        final IPersistentPreferenceStore preferenceStore = context.mock(IPersistentPreferenceStore.class);
        final IFolder existsFolder = context.mock(IFolder.class, "exists folder");
        final IFolder doesNotExistFolder = context.mock(IFolder.class, "does not exist folder");

        context.checking(new Expectations() {
            {
                oneOf(project).hasNature(SubstepsNature.NATURE_ID);
                will(returnValue(isSubstepsProject));

                oneOf(projectToPreferenceLookup).from(project);
                will(returnValue(preferenceStore));

                oneOf(preferenceStore).getBoolean(SubstepsPreferences.SUBSTEPS_COMPATIBILITY_CHECKED.key());
                will(returnValue(hasBeenProcessed));

                for (final String folder : folders) {
                    allowing(project).getFolder(folder);
                    will(returnValue(existsFolder));
                }
                allowing(project).getFolder(with(any(String.class)));
                will(returnValue(doesNotExistFolder));

                allowing(existsFolder).exists();
                will(returnValue(true));
                allowing(doesNotExistFolder).exists();
                will(returnValue(false));
            }
        });
    }
}
