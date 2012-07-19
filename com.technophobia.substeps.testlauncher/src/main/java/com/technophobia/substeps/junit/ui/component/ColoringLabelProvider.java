package com.technophobia.substeps.junit.ui.component;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DecoratingStyledCellLabelProvider;
import org.eclipse.jface.viewers.IDecorationContext;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

public class ColoringLabelProvider extends DecoratingStyledCellLabelProvider implements ILabelProvider {

    public static final Styler HIGHLIGHT_STYLE = StyledString.createColorRegistryStyler(null,
            ColoredViewersManager.HIGHLIGHT_BG_COLOR_NAME);
    public static final Styler HIGHLIGHT_WRITE_STYLE = StyledString.createColorRegistryStyler(null,
            ColoredViewersManager.HIGHLIGHT_WRITE_BG_COLOR_NAME);

    public static final Styler INHERITED_STYLER = StyledString.createColorRegistryStyler(
            ColoredViewersManager.INHERITED_COLOR_NAME, null);


    public ColoringLabelProvider(final IStyledLabelProvider labelProvider) {
        this(labelProvider, null, null);
    }


    public ColoringLabelProvider(final IStyledLabelProvider labelProvider, final ILabelDecorator decorator,
            final IDecorationContext decorationContext) {
        super(labelProvider, decorator, decorationContext);
    }


    @Override
    public void initialize(final ColumnViewer viewer, final ViewerColumn column) {
        ColoredViewersManager.install(this);
        setOwnerDrawEnabled(ColoredViewersManager.showColoredLabels());

        super.initialize(viewer, column);
    }


    @Override
    public void dispose() {
        super.dispose();
        ColoredViewersManager.uninstall(this);
    }


    public void update() {
        final ColumnViewer viewer = getViewer();

        if (viewer == null) {
            return;
        }

        boolean needsUpdate = false;

        final boolean showColoredLabels = ColoredViewersManager.showColoredLabels();
        if (showColoredLabels != isOwnerDrawEnabled()) {
            setOwnerDrawEnabled(showColoredLabels);
            needsUpdate = true;
        } else if (showColoredLabels) {
            needsUpdate = true;
        }
        if (needsUpdate) {
            fireLabelProviderChanged(new LabelProviderChangedEvent(this));
        }
    }


    @Override
    protected StyleRange prepareStyleRange(StyleRange styleRange, final boolean applyColors) {
        if (!applyColors && styleRange.background != null) {
            styleRange = super.prepareStyleRange(styleRange, applyColors);
            styleRange.borderStyle = SWT.BORDER_DOT;
            return styleRange;
        }
        return super.prepareStyleRange(styleRange, applyColors);
    }


    @Override
    public String getText(final Object element) {
        return getStyledText(element).getString();
    }

}
