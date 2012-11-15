package com.technophobia.substeps.editor.outline.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.text.Position;

public class SubstepsRootElement extends AbstractModelElement {

    private final Collection<SubstepsDefinitionElement> substepsDefinitions;


    public SubstepsRootElement(final String text, final Position position) {
        super(text, position);
        this.substepsDefinitions = new ArrayList<SubstepsDefinitionElement>();
    }


    public void addSubstepsDefinition(final SubstepsDefinitionElement substepsDefinitionElement) {
        substepsDefinitionElement.setParent(this);
        this.substepsDefinitions.add(substepsDefinitionElement);
    }


    public Collection<SubstepsDefinitionElement> getSubstepsDefinitions() {
        return substepsDefinitions;
    }


    @Override
    public Collection<AbstractModelElement> getChildren() {
        return Collections.<AbstractModelElement> unmodifiableCollection(getSubstepsDefinitions());
    }
}
