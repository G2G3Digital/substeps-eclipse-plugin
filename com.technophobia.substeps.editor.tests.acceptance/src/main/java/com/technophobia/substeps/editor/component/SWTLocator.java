package com.technophobia.substeps.editor.component;

public interface SWTLocator<Parent, Child> {

	Child locate(Parent parent);
}
