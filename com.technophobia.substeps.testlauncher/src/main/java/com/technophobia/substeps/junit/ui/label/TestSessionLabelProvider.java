package com.technophobia.substeps.junit.ui.label;

import java.text.MessageFormat;
import java.text.NumberFormat;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.technophobia.eclipse.ui.view.ViewLayout;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.junit.ui.SubstepsTestIcon;
import com.technophobia.substeps.model.structure.Status;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;
import com.technophobia.substeps.model.structure.SubstepsTestParentElement;
import com.technophobia.substeps.supplier.Supplier;

public class TestSessionLabelProvider extends LabelProvider implements IStyledLabelProvider {

    private final NumberFormat timeFormat;

    private boolean showTime;

    private final Supplier<String> testKindDisplayNameSupplier;

    private final SubstepsIconProvider iconProvider;

    private final ViewLayout layoutMode;


    public TestSessionLabelProvider(final Supplier<String> testKindDisplayNameSupplier,
            final SubstepsIconProvider iconProvider, final ViewLayout layoutMode) {

        this.testKindDisplayNameSupplier = testKindDisplayNameSupplier;
        this.iconProvider = iconProvider;
        this.layoutMode = layoutMode;
        this.showTime = true;

        timeFormat = NumberFormat.getNumberInstance();
        timeFormat.setGroupingUsed(true);
        timeFormat.setMinimumFractionDigits(3);
        timeFormat.setMaximumFractionDigits(3);
        timeFormat.setMinimumIntegerDigits(1);
    }


    @Override
    public StyledString getStyledText(final Object element) {
        final String label = getSimpleLabel(element);
        if (label == null) {
            return new StyledString(element.toString());
        }
        StyledString text = new StyledString(label);

        final SubstepsTestElement testElement = (SubstepsTestElement) element;
        if (layoutMode.equals(ViewLayout.HIERARCHICAL)) {
            if (testElement.getParentContainer() instanceof SubstepsRunSession) {
                final String testKindDisplayName = testKindDisplayNameSupplier.get();
                if (testKindDisplayName != null && testKindDisplayName.length() > 0) {
                    final String decorated = MessageFormat.format(
                            SubstepsFeatureMessages.TestSessionLabelProvider_testName_JUnitVersion, new Object[] {
                                    label, testKindDisplayName });
                    text = StyledCellLabelProvider.styleDecoratedString(decorated, StyledString.QUALIFIER_STYLER, text);
                }
            }

        } else {
            if (element instanceof SubstepsTestLeafElement) {
                final String decorated = MessageFormat.format(
                        SubstepsFeatureMessages.TestSessionLabelProvider_testMethodName_className, label);
                text = StyledCellLabelProvider.styleDecoratedString(decorated, StyledString.QUALIFIER_STYLER, text);
            }
        }
        return addElapsedTime(text, testElement.getElapsedTimeInSeconds());
    }


    private StyledString addElapsedTime(final StyledString styledString, final double time) {
        final String string = styledString.getString();
        final String decorated = addElapsedTime(string, time);
        return StyledCellLabelProvider.styleDecoratedString(decorated, StyledString.COUNTER_STYLER, styledString);
    }


    private String addElapsedTime(final String string, final double time) {
        if (!showTime || Double.isNaN(time)) {
            return string;
        }
        final String formattedTime = timeFormat.format(time);
        return MessageFormat.format(SubstepsFeatureMessages.TestSessionLabelProvider_testName_elapsedTimeInSeconds,
                new Object[] { string, formattedTime });
    }


    private String getSimpleLabel(final Object element) {
        if (element instanceof SubstepsTestLeafElement) {
            return ((SubstepsTestLeafElement) element).getTestName();
        } else if (element instanceof SubstepsTestParentElement) {
            return ((SubstepsTestParentElement) element).getTestName();
        }
        return null;
    }


    @Override
    public String getText(final Object element) {
        String label = getSimpleLabel(element);
        if (label == null) {
            return element.toString();
        }
        final SubstepsTestElement testElement = (SubstepsTestElement) element;
        if (layoutMode.equals(ViewLayout.HIERARCHICAL)) {
            if (testElement.getParentContainer() instanceof SubstepsRunSession) {
                final String testKindDisplayName = testKindDisplayNameSupplier.get();
                if (testKindDisplayName != null) {
                    label = MessageFormat.format(
                            SubstepsFeatureMessages.TestSessionLabelProvider_testName_JUnitVersion, new Object[] {
                                    label, testKindDisplayName });
                }
            }
        } else {
            if (element instanceof SubstepsTestLeafElement) {
                label = MessageFormat.format(SubstepsFeatureMessages.TestSessionLabelProvider_testMethodName_className,
                        label);
            }
        }
        return addElapsedTime(label, testElement.getElapsedTimeInSeconds());
    }


    @Override
    public Image getImage(final Object element) {
        if (element instanceof SubstepsTestLeafElement) {
            final SubstepsTestLeafElement testCaseElement = ((SubstepsTestLeafElement) element);
            if (testCaseElement.isIgnored())
                return iconProvider.imageFor(SubstepsTestIcon.TestIgnored);

            final Status status = testCaseElement.getStatus();
            if (status.isNotRun())
                return iconProvider.imageFor(SubstepsTestIcon.Test);
            else if (status.isRunning())
                return iconProvider.imageFor(SubstepsTestIcon.TestRunning);
            else if (status.isError())
                return iconProvider.imageFor(SubstepsTestIcon.TestError);
            else if (status.isFailure())
                return iconProvider.imageFor(SubstepsTestIcon.TestFail);
            else if (status.isOK())
                return iconProvider.imageFor(SubstepsTestIcon.TestOk);
            else
                throw new IllegalStateException(element.toString());

        } else if (element instanceof SubstepsTestParentElement) {
            final Status status = ((SubstepsTestParentElement) element).getStatus();
            if (status.isNotRun())
                return iconProvider.imageFor(SubstepsTestIcon.Suite);
            else if (status.isRunning())
                return iconProvider.imageFor(SubstepsTestIcon.SuiteRunning);
            else if (status.isError())
                return iconProvider.imageFor(SubstepsTestIcon.SuiteError);
            else if (status.isFailure())
                return iconProvider.imageFor(SubstepsTestIcon.SuiteFail);
            else if (status.isOK())
                return iconProvider.imageFor(SubstepsTestIcon.SuiteOk);
            else
                throw new IllegalStateException(element.toString());

        } else {
            throw new IllegalArgumentException(String.valueOf(element));
        }
    }


    public void setShowTime(final boolean showTime) {
        this.showTime = showTime;
        fireLabelProviderChanged(new LabelProviderChangedEvent(this));
    }

}
