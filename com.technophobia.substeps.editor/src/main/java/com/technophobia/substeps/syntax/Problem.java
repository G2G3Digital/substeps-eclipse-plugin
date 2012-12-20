/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
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
    private final int lineOffset;


    public static Problem createError(final String description, final String line, final int lineNumber,
            final int lineOffset) {
        return new Problem(IMarker.SEVERITY_ERROR, description, line, lineNumber, lineOffset);
    }


    public static Problem createWarning(final String description, final String line, final int lineNumber,
            final int lineOffset) {
        return new Problem(IMarker.SEVERITY_WARNING, description, line, lineNumber, lineOffset);
    }


    private Problem(final int severity, final String description, final String offendingLine, final int lineNumber,
            final int lineOffset) {
        this.severity = severity;
        this.description = description;
        this.offendingLine = offendingLine;
        this.lineNumber = lineNumber;
        this.lineOffset = lineOffset;
    }


    public boolean mark(final IResource resource) {
        try {
            final IMarker marker = resource.createMarker(IMarker.PROBLEM);
            marker.setAttribute(IMarker.SEVERITY, severity);
            marker.setAttribute(IMarker.MESSAGE, description);
            marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber + 1);

            marker.setAttribute(IMarker.CHAR_START, lineOffset);
            marker.setAttribute(IMarker.CHAR_END, (lineOffset + offendingLine.trim().length()));
            return true;
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error("Could not mark resource " + resource, ex);
            return false;
        }
    }
}
