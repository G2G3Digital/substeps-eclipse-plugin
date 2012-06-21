package com.technophobia.substeps.junit.ui.label;

import java.text.NumberFormat;

import org.eclipse.jdt.internal.junit.BasicElementLabels;
import org.eclipse.jdt.internal.junit.Messages;
import org.eclipse.jdt.internal.junit.model.TestCaseElement;
import org.eclipse.jdt.internal.junit.model.TestElement.Status;
import org.eclipse.jdt.internal.junit.model.TestSuiteElement;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestSuiteElement;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.ui.view.ViewLayout;
import com.technophobia.substeps.junit.ui.SubstepsIcon;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;

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

        final ITestElement testElement = (ITestElement) element;
        if (layoutMode.equals(ViewLayout.HIERARCHICAL)) {
            if (testElement.getParentContainer() instanceof ITestRunSession) {
                final String testKindDisplayName = testKindDisplayNameSupplier.get();
                if (testKindDisplayName != null) {
                    final String decorated = Messages.format(
                            JUnitMessages.TestSessionLabelProvider_testName_JUnitVersion, new Object[] { label,
                                    testKindDisplayName });
                    text = StyledCellLabelProvider.styleDecoratedString(decorated, StyledString.QUALIFIER_STYLER, text);
                }
            }

        } else {
            if (element instanceof ITestCaseElement) {
                final String className = BasicElementLabels.getJavaElementName(((ITestCaseElement) element)
                        .getTestClassName());
                final String decorated = Messages.format(
                        JUnitMessages.TestSessionLabelProvider_testMethodName_className, new Object[] { label,
                                className });
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
        return Messages.format(JUnitMessages.TestSessionLabelProvider_testName_elapsedTimeInSeconds, new String[] {
                string, formattedTime });
    }


    private String getSimpleLabel(final Object element) {
        if (element instanceof ITestCaseElement) {
            return BasicElementLabels.getJavaElementName(((ITestCaseElement) element).getTestMethodName());
        } else if (element instanceof ITestSuiteElement) {
            return BasicElementLabels.getJavaElementName(((ITestSuiteElement) element).getSuiteTypeName());
        }
        return null;
    }


    @Override
    public String getText(final Object element) {
        String label = getSimpleLabel(element);
        if (label == null) {
            return element.toString();
        }
        final ITestElement testElement = (ITestElement) element;
        if (layoutMode.equals(ViewLayout.HIERARCHICAL)) {
            if (testElement.getParentContainer() instanceof ITestRunSession) {
                final String testKindDisplayName = testKindDisplayNameSupplier.get();
                if (testKindDisplayName != null) {
                    label = Messages.format(JUnitMessages.TestSessionLabelProvider_testName_JUnitVersion, new Object[] {
                            label, testKindDisplayName });
                }
            }
        } else {
            if (element instanceof ITestCaseElement) {
                final String className = BasicElementLabels.getJavaElementName(((ITestCaseElement) element)
                        .getTestClassName());
                label = Messages.format(JUnitMessages.TestSessionLabelProvider_testMethodName_className, new Object[] {
                        label, className });
            }
        }
        return addElapsedTime(label, testElement.getElapsedTimeInSeconds());
    }


    @Override
    public Image getImage(final Object element) {
        if (element instanceof TestCaseElement) {
            final TestCaseElement testCaseElement = ((TestCaseElement) element);
            if (testCaseElement.isIgnored())
                return iconProvider.imageFor(SubstepsIcon.TestIgnored);

            final Status status = testCaseElement.getStatus();
            if (status.isNotRun())
                return iconProvider.imageFor(SubstepsIcon.Test);
            else if (status.isRunning())
                return iconProvider.imageFor(SubstepsIcon.TestRunning);
            else if (status.isError())
                return iconProvider.imageFor(SubstepsIcon.TestError);
            else if (status.isFailure())
                return iconProvider.imageFor(SubstepsIcon.TestFail);
            else if (status.isOK())
                return iconProvider.imageFor(SubstepsIcon.TestOk);
            else
                throw new IllegalStateException(element.toString());

        } else if (element instanceof TestSuiteElement) {
            final Status status = ((TestSuiteElement) element).getStatus();
            if (status.isNotRun())
                return iconProvider.imageFor(SubstepsIcon.Suite);
            else if (status.isRunning())
                return iconProvider.imageFor(SubstepsIcon.SuiteRunning);
            else if (status.isError())
                return iconProvider.imageFor(SubstepsIcon.SuiteError);
            else if (status.isFailure())
                return iconProvider.imageFor(SubstepsIcon.SuiteFail);
            else if (status.isOK())
                return iconProvider.imageFor(SubstepsIcon.SuiteOk);
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
