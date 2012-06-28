package com.technophobia.substeps.model.structure;

import com.technophobia.substeps.junit.ui.SubstepsRunSession;

public class SubstepsTestRootElement extends SubstepsTestParentElement {

    private final SubstepsRunSession session;


    public SubstepsTestRootElement(final SubstepsRunSession session) {
        super(null, "-1", session.getTestRunName(), 1); //$NON-NLS-1$
        this.session = session;
    }


    @Override
    public SubstepsTestRootElement getRoot() {
        return this;
    }


    @Override
    public SubstepsRunSession getSubstepsRunSession() {
        return session;
    }
}
