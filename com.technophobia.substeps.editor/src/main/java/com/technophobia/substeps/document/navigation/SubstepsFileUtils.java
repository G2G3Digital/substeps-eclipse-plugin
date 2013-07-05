package com.technophobia.substeps.document.navigation;

import static com.technophobia.substeps.FeatureEditorPlugin.instance;
import static com.technophobia.substeps.supplier.Transformers.transform;
import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.filefilter.FileFilterUtils.and;
import static org.apache.commons.io.filefilter.FileFilterUtils.nameFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.trueFileFilter;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.technophobia.substeps.supplier.Transformer;

/**
 * Can much of this be replaced with IJavaProject.findType()?
 */
public final class SubstepsFileUtils {

    private SubstepsFileUtils() {
    }


    private static final IOFileFilter exclusionFilter(final Set<IPath> exclusionSet) {
        IOFileFilter filter = trueFileFilter();
        for (IPath exclusion : exclusionSet) {
            // TODO is this correct?
            and(filter, notFileFilter(nameFileFilter(exclusion.lastSegment())));
        }
        return filter;
    }


    public static final List<IFile> searchForFile(final IProject project, final String filename) {
        return searchForFile(project, filename, new HashSet<IPath>());
    }


    public static final List<IFile> searchForFile(final IProject project, final String filename,
            final Set<IPath> exclusionSet) {
        final File file = new File(project.getLocationURI());
        final IOFileFilter directoryExclusion = exclusionFilter(exclusionSet);
        // Find the files.
        final Collection<File> listFiles = listFiles(file, nameFileFilter(filename), directoryExclusion);

        return transform(listFiles, new Transformer<File, IFile>() {
            @Override
            public IFile from(File from) {
                return project
                        .getFile(from.getAbsolutePath().substring(project.getLocationURI().getRawPath().length()));
            }
        });
    }
    
    
    /**
     * A set containing all of the output directories for a given project.
     * 
     * @param project
     * @return
     */
    public static final Set<IPath> outputPaths(final IJavaProject project) {
        final Set<IPath> exclusions = new HashSet<IPath>();
        try {
            for (final IClasspathEntry entry : project.getRawClasspath()) {
                if (IClasspathEntry.CPE_SOURCE == entry.getEntryKind()) {
                    if (entry.getOutputLocation() != null) {
                        exclusions.add(entry.getOutputLocation());
                    }
                }
            }
            // Add the default
            exclusions.add(project.getOutputLocation());
        } catch (final JavaModelException e) {
            final String error = "Unable to calculate output paths for IJavaProject:" + project;
            instance().error(error, e);
            throw new RuntimeException(e);
        }
        return exclusions;
    }
}
