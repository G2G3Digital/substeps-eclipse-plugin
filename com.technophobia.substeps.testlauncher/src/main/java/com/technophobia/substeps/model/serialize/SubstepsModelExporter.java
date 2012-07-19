package com.technophobia.substeps.model.serialize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.xml.sax.InputSource;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;

public class SubstepsModelExporter {

    /**
     * Exports the given test run session.
     * 
     * @param testRunSession
     *            the test run session
     * @param file
     *            the destination
     * @throws CoreException
     *             if an error occurred
     */
    public static void exportTestRunSession(final SubstepsRunSession substepsRunSession, final File file)
            throws CoreException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            exportTestRunSession(substepsRunSession, out);

        } catch (final IOException e) {
            throwExportError(file, e);
        } catch (final TransformerConfigurationException e) {
            throwExportError(file, e);
        } catch (final TransformerException e) {
            throwExportError(file, e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e2) {
                    FeatureRunnerPlugin.log(e2);
                }
            }
        }
    }


    public static void exportTestRunSession(final SubstepsRunSession testRunSession, final OutputStream out)
            throws TransformerFactoryConfigurationError, TransformerException {

        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        final InputSource inputSource = new InputSource();
        final SAXSource source = new SAXSource(new SubstepsRunSessionSerializer(testRunSession), inputSource);
        final StreamResult result = new StreamResult(out);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
        /*
         * Bug in Xalan: Only indents if proprietary property
         * org.apache.xalan.templates.OutputProperties.S_KEY_INDENT_AMOUNT is
         * set.
         * 
         * Bug in Xalan as shipped with J2SE 5.0: Does not read the
         * indent-amount property at all >:-(.
         */
        try {
            transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2"); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (final IllegalArgumentException e) {
            // no indentation today...
        }
        transformer.transform(source, result);
    }


    private static void throwExportError(final File file, final Exception e) throws CoreException {
        throw new CoreException(new org.eclipse.core.runtime.Status(IStatus.ERROR, FeatureRunnerPlugin.PLUGIN_ID,
                MessageFormat.format(SubstepsFeatureMessages.Model_could_not_write, file.getAbsolutePath()), e));
    }
}
