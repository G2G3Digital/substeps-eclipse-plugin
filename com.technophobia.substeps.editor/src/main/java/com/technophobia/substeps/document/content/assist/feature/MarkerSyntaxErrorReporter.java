package com.technophobia.substeps.document.content.assist.feature;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerUtilities;

import com.technophobia.eclipse.transformer.FileToIFileTransformer;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.runner.syntax.SyntaxErrorReporter;
import com.technophobia.substeps.supplier.Supplier;

public class MarkerSyntaxErrorReporter implements SyntaxErrorReporter {

    private static final String FEATURE_ERROR_MARKER_ID = "com.technophobia.substeps.editor.featureerror";
    private static final String SUBSTEP_ERROR_MARKER_ID = "com.technophobia.substeps.editor.substeperror";

    private final Supplier<IProject> projectSupplier;


    public MarkerSyntaxErrorReporter(final Supplier<IProject> projectSupplier) {
        this.projectSupplier = projectSupplier;
    }


    @Override
    public void reportFeatureError(final File file, final int lineNumber, final String description)
            throws RuntimeException {
        addMarker(file, lineNumber, description, FEATURE_ERROR_MARKER_ID);
    }


    @Override
    public void reportFeatureError(final File file, final int lineNumber, final String description,
            final RuntimeException ex) throws RuntimeException {
        addMarker(file, lineNumber, description, FEATURE_ERROR_MARKER_ID);
    }


    @Override
    public void reportSubstepsError(final File file, final int lineNumber, final String description)
            throws RuntimeException {
        addMarker(file, lineNumber, description, SUBSTEP_ERROR_MARKER_ID);
    }


    @Override
    public void reportSubstepsError(final File file, final int lineNumber, final String description,
            final RuntimeException ex) throws RuntimeException {
        addMarker(file, lineNumber, description, SUBSTEP_ERROR_MARKER_ID);
    }


    private void addMarker(final File file, final int lineNumber, final String description, final String errorMarkerId) {
        projectSupplier.get().getLocation().toFile();
        final IFile f = new FileToIFileTransformer(projectSupplier.get()).from(file);

        @SuppressWarnings("rawtypes")
        final Map data = createMarkerData(lineNumber + 1, description, f);

        try {
            // MarkerUtilities.createMarker(f, data, errorMarkerId);
            final IMarker marker = f.createMarker(IMarker.PROBLEM);
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            marker.setAttribute(IMarker.MESSAGE, description);
            marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber + 1);
            // marker.setAttributes(data);
        } catch (final CoreException e) {
            FeatureEditorPlugin.error("Error createing marker for line " + lineNumber + " of " + file.getAbsolutePath()
                    + ": " + description);
        }
    }


    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    private Map createMarkerData(final int lineNumber, final String description, final IFile file) {
        final Map map = new HashMap();
        // final int columnNumber = 0;
        MarkerUtilities.setLineNumber(map, lineNumber);
        MarkerUtilities.setMessage(map, description);
        if (file != null) {
            map.put(IMarker.LOCATION, file.getFullPath().toString());
        }

        // final Integer charStart = null;
        // if (charStart != null)
        map.put(IMarker.CHAR_START, 0);

        // final Integer charEnd = null;
        // if (charEnd != null)
        map.put(IMarker.CHAR_END, 30);

        map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
        return map;
    }
}
