package com.technophobia.substeps.junit.launcher.tab;

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.widgets.Composite;

import com.technophobia.substeps.junit.launcher.model.SubstepsLaunchModel;

public interface TabComponent {

    void create(Composite comp);


    void initializeFrom(SubstepsLaunchModel model);


    void saveTo(SubstepsLaunchModel model);


    void setDefaultOn(SubstepsLaunchModel model, IResource currentResource);


    void validate(Collection<String> errorMessageList);


    void enableControls();


    void disableControls();


    void addDependentTabComponent(TabComponent component);
}
