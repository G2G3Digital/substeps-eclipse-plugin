package com.technophobia.eclipse.launcher.config;

import java.util.Collection;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.technophobia.substeps.supplier.Transformer;

public class DialogConfigSelector implements Transformer<Collection<ILaunchConfiguration>, ILaunchConfiguration> {

    private final Shell shell;

    private final String title;
    private final String message;


    public DialogConfigSelector(final Shell shell, final String title, final String message) {
        super();
        this.shell = shell;
        this.title = title;
        this.message = message;
    }


    @Override
    public ILaunchConfiguration from(final Collection<ILaunchConfiguration> configList) {
        final IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
        final ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
        dialog.setElements(configList.toArray());
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setMultipleSelection(false);
        final int result = dialog.open();
        if (result == Window.OK) {
            return (ILaunchConfiguration) dialog.getFirstResult();
        }
        return null;
    }

}
