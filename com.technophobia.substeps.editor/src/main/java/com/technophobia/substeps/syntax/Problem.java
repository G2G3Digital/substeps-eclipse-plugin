package com.technophobia.substeps.syntax;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.technophobia.substeps.FeatureEditorPlugin;

public class Problem {

    private final int severity;
    private final String description;
    private final String offendingLine;
    private final int lineNumber;


    public static Problem createError(final String description, final String line, final int lineNumber) {
        return new Problem(IMarker.SEVERITY_ERROR, description, line, lineNumber);
    }


    public static Problem createWarning(final String description, final String line, final int lineNumber) {
        return new Problem(IMarker.SEVERITY_WARNING, description, line, lineNumber);
    }


    private Problem(final int severity, final String description, final String offendingLine, final int lineNumber) {
        this.severity = severity;
        this.description = description;
        this.offendingLine = offendingLine;
        this.lineNumber = lineNumber;
    }


    public boolean mark(final IResource resource) {
        try {
            final IMarker marker = resource.createMarker(IMarker.PROBLEM);
            marker.setAttribute(IMarker.SEVERITY, severity);
            marker.setAttribute(IMarker.MESSAGE, description);
            marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber + 1);
            marker.setAttribute(IMarker.CHAR_START, charStart(offendingLine));
            marker.setAttribute(IMarker.CHAR_END, charEnd(offendingLine));
            return true;
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error("Could not mark resource " + resource, ex);
            return false;
        }
    }


    private int charStart(final String line) {
        final String trimmed = line.trim();
        return Math.max(line.indexOf(trimmed), 0);
    }


    private int charEnd(final String line) {
        final String trimmed = line.trim();

        final int index = line.indexOf(trimmed);
        return index > -1 ? index + trimmed.length() : line.length();
    }
}
