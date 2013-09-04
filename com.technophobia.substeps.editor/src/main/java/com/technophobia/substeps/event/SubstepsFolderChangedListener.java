package com.technophobia.substeps.event;

import org.eclipse.core.runtime.IPath;

public interface SubstepsFolderChangedListener {

    boolean isConfirmationRequired(IPath previousPath, IPath newPath);


    String confirmationMessage(IPath previousPath, IPath newPath);


    void onConfirmation(IPath previousPath, IPath newPath);
}
