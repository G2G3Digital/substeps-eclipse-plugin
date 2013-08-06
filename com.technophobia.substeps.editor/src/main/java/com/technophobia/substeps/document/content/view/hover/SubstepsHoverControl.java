package com.technophobia.substeps.document.content.view.hover;

import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.substeps.document.content.view.hover.model.HoverModel;

public class SubstepsHoverControl extends AbstractInformationControl {

    private Label header;
    private Label body;


    public SubstepsHoverControl(final Shell parent) {
        super(parent, "");
        create();
    }


    @Override
    public boolean hasContents() {
        return true;
    }


    @Override
    protected void createContent(final Composite parent) {
        final Group group = new Group(parent, SWT.NONE);
        group.setLayout(new GridLayout(1, true));

        header = new Label(group, SWT.NONE);
        final Font existingFont = header.getFont();
        final FontData existingFontData = existingFont.getFontData()[0];
        header.setFont(new Font(existingFont.getDevice(), new FontData(existingFontData.getName(), existingFontData
                .getHeight(), SWT.BOLD)));
        body = new Label(group, SWT.NONE);
    }


    @Override
    public void setInformation(final String information) {
        final HoverModel hoverModel = HoverModel.fromString(information);
        if (hoverModel != null) {
            header.setText(hoverModel.header());
            body.setText(hoverModel.body());

            setStatusText(hoverModel.location());
        }
    }
}
