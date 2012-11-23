/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.junit.ui.dialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.help.SubstepsHelpContextIds;
import com.technophobia.substeps.model.structure.FailureTrace;
import com.technophobia.substeps.model.structure.SubstepsTestElement;

public class CompareResultDialog extends TrayDialog {
    private static final String PREFIX_SUFFIX_PROPERTY = "org.eclipse.jdt.internal.junit.ui.CompareResultDialog.prefixSuffix"; //$NON-NLS-1$

    private static class CompareResultMergeViewer extends TextMergeViewer {
        private CompareResultMergeViewer(final Composite parent, final int style,
                final CompareConfiguration configuration) {
            super(parent, style, configuration);
        }


        @Override
        protected void createControls(final Composite composite) {
            super.createControls(composite);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, SubstepsHelpContextIds.RESULT_COMPARE_DIALOG);
        }


        @Override
        protected void configureTextViewer(final TextViewer textViewer) {
            if (textViewer instanceof SourceViewer) {
                final int[] prefixSuffixOffsets = (int[]) getCompareConfiguration().getProperty(PREFIX_SUFFIX_PROPERTY);
                ((SourceViewer) textViewer).configure(new CompareResultViewerConfiguration(prefixSuffixOffsets));
            }
        }
    }

    private static class CompareResultViewerConfiguration extends SourceViewerConfiguration {
        private static class SimpleDamagerRepairer implements IPresentationDamager, IPresentationRepairer {
            private IDocument document;
            private final int[] prefixSuffixOffsets2;


            public SimpleDamagerRepairer(final int[] prefixSuffixOffsets) {
                this.prefixSuffixOffsets2 = prefixSuffixOffsets;
            }


            @Override
            public void setDocument(final IDocument document) {
                this.document = document;
            }


            @Override
            public IRegion getDamageRegion(final ITypedRegion partition, final DocumentEvent event,
                    final boolean changed) {
                return new Region(0, document.getLength());
            }


            @Override
            public void createPresentation(final TextPresentation presentation, final ITypedRegion damage) {
                presentation.setDefaultStyleRange(new StyleRange(0, document.getLength(), null, null));
                final int prefix = prefixSuffixOffsets2[0];
                final int suffix = prefixSuffixOffsets2[1];
                final TextAttribute attr = new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_RED), null,
                        SWT.BOLD);
                presentation.addStyleRange(new StyleRange(prefix, document.getLength() - suffix - prefix, attr
                        .getForeground(), attr.getBackground(), attr.getStyle()));
            }
        }

        private final int[] prefixSuffixOffsets;


        public CompareResultViewerConfiguration(final int[] prefixSuffixOffsets) {
            this.prefixSuffixOffsets = prefixSuffixOffsets;
        }


        @Override
        public IPresentationReconciler getPresentationReconciler(final ISourceViewer sourceViewer) {
            final PresentationReconciler reconciler = new PresentationReconciler();
            final SimpleDamagerRepairer dr = new SimpleDamagerRepairer(prefixSuffixOffsets);
            reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
            reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
            return reconciler;
        }
    }

    private static class CompareElement implements ITypedElement, IEncodedStreamContentAccessor {
        private final String content;


        public CompareElement(final String content) {
            this.content = content;
        }


        @Override
        public String getName() {
            return "<no name>"; //$NON-NLS-1$
        }


        @Override
        public Image getImage() {
            return null;
        }


        @Override
        public String getType() {
            return "txt"; //$NON-NLS-1$
        }


        @Override
        public InputStream getContents() {
            try {
                return new ByteArrayInputStream(content.getBytes("UTF-8")); //$NON-NLS-1$
            } catch (final UnsupportedEncodingException e) {
                return new ByteArrayInputStream(content.getBytes());
            }
        }


        @Override
        public String getCharset() throws CoreException {
            return "UTF-8"; //$NON-NLS-1$
        }
    }

    private TextMergeViewer viewer;
    private String expected;
    private String actual;
    private String testName;

    /**
     * Lengths of common prefix and suffix. Note: this array is passed to the
     * DamagerRepairer and the lengths are updated on content change.
     */
    private final int[] prefixSuffix = new int[2];

    private CompareViewerPane compareViewerPane;


    public CompareResultDialog(final Shell parentShell, final SubstepsTestElement element) {
        super(parentShell);
        setShellStyle((getShellStyle() & ~SWT.APPLICATION_MODAL) | SWT.TOOL);
        setFailedTest(element);
    }


    @Override
    protected boolean isResizable() {
        return true;
    }


    private void setFailedTest(final SubstepsTestElement failedTest) {
        this.testName = failedTest.getTestName();

        final FailureTrace failureTrace = failedTest.getFailureTrace();
        this.expected = failureTrace.getExpected();
        this.actual = failureTrace.getActual();
        computePrefixSuffix();
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#getDialogBoundsSettings()
     */
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        final IDialogSettings dialogSettings = FeatureRunnerPlugin.instance().getDialogSettings();
        IDialogSettings section = dialogSettings.getSection(getClass().getName());
        if (section == null) {
            section = dialogSettings.addNewSection(getClass().getName());
        }
        return section;
    }


    private void computePrefixSuffix() {
        final int end = Math.min(expected.length(), actual.length());
        int i = 0;
        for (; i < end; i++)
            if (expected.charAt(i) != actual.charAt(i))
                break;
        prefixSuffix[0] = i;

        int j = expected.length() - 1;
        int k = actual.length() - 1;
        int l = 0;
        for (; k >= i && j >= i; k--, j--) {
            if (expected.charAt(j) != actual.charAt(k))
                break;
            l++;
        }
        prefixSuffix[1] = l;
    }


    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(SubstepsFeatureMessages.CompareResultDialog_title);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(newShell, SubstepsHelpContextIds.RESULT_COMPARE_DIALOG);
    }


    @Override
    protected void createButtonsForButtonBar(final Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, SubstepsFeatureMessages.CompareResultDialog_labelOK, true);
    }


    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite composite = (Composite) super.createDialogArea(parent);
        final GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);

        compareViewerPane = new CompareViewerPane(composite, SWT.BORDER | SWT.FLAT);
        final GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
        data.widthHint = convertWidthInCharsToPixels(120);
        data.heightHint = convertHeightInCharsToPixels(13);
        compareViewerPane.setLayoutData(data);

        final Control previewer = createPreviewer(compareViewerPane);
        compareViewerPane.setContent(previewer);
        final GridData gd = new GridData(GridData.FILL_BOTH);
        previewer.setLayoutData(gd);
        applyDialogFont(parent);
        return composite;
    }


    private Control createPreviewer(final Composite parent) {
        final CompareConfiguration compareConfiguration = new CompareConfiguration();
        compareConfiguration.setLeftLabel(SubstepsFeatureMessages.CompareResultDialog_expectedLabel);
        compareConfiguration.setLeftEditable(false);
        compareConfiguration.setRightLabel(SubstepsFeatureMessages.CompareResultDialog_actualLabel);
        compareConfiguration.setRightEditable(false);
        compareConfiguration.setProperty(CompareConfiguration.IGNORE_WHITESPACE, Boolean.FALSE);
        compareConfiguration.setProperty(PREFIX_SUFFIX_PROPERTY, prefixSuffix);

        viewer = new CompareResultMergeViewer(parent, SWT.NONE, compareConfiguration);
        setCompareViewerInput();

        final Control control = viewer.getControl();
        control.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(final DisposeEvent e) {
                compareConfiguration.dispose();
            }
        });
        return control;
    }


    private void setCompareViewerInput() {
        if (!viewer.getControl().isDisposed()) {
            viewer.setInput(new DiffNode(new CompareElement(expected), new CompareElement(actual)));
            compareViewerPane.setText(testName);
        }
    }


    public void setInput(final SubstepsTestElement failedTest) {
        setFailedTest(failedTest);
        setCompareViewerInput();
    }
}
