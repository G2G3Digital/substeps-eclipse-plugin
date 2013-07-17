package com.technophobia.substeps.editor.message;

import org.eclipse.osgi.util.NLS;

public class SubstepsEditorMessages {

    private static final String BUNDLE_NAME = "com.technophobia.substeps.editor.message.SubstepsEditorMessages";

    public static String StepImplementationHoverModel_No_JavaDoc;
    public static String StepImplementationHoverModel_No_JavaDoc_Recommendation;

    static {
        NLS.initializeMessages(BUNDLE_NAME, SubstepsEditorMessages.class);
    }
}
