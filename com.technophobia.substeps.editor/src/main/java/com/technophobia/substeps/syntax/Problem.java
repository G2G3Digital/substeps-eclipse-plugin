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
    private final int offset;


    public static Problem createError(final String description, final String line, final int lineNumber,
            final int offset) {
        return new Problem(IMarker.SEVERITY_ERROR, description, line, lineNumber, offset);
    }


    public static Problem createWarning(final String description, final String line, final int lineNumber,
            final int offset) {
        return new Problem(IMarker.SEVERITY_WARNING, description, line, lineNumber, offset);
    }


    private Problem(final int severity, final String description, final String offendingLine, final int lineNumber,
            final int offset) {
        this.severity = severity;
        this.description = description;
        this.offendingLine = offendingLine;
        this.lineNumber = lineNumber;
        this.offset = offset;
    }


    public boolean mark(final IResource resource) {
        try {
            final IMarker marker = resource.createMarker(IMarker.PROBLEM);
            marker.setAttribute(IMarker.SEVERITY, severity);
            marker.setAttribute(IMarker.MESSAGE, description);
            marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber + 1);
            marker.setAttribute(IMarker.CHAR_START, offset);
            marker.setAttribute(IMarker.CHAR_END, (offset + offendingLine.trim().length()));
            return true;
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error("Could not mark resource " + resource, ex);
            return false;
        }
    }
}
