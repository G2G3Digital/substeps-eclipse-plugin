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
package com.technophobia.substeps.model.structure;

import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.supplier.Transformer;

public class DefaultSubstepsTestElementFactory implements SubstepsTestElementFactory {

    private final Transformer<String, String>[] testNameTransformers;


    public DefaultSubstepsTestElementFactory(final Transformer<String, String>... testNameTransformers) {
        this.testNameTransformers = testNameTransformers;
    }


    @Override
    public SubstepsTestElement createForTestEntryString(final String testEntry,
            final Supplier<SubstepsTestParentElement> parentElementSupplier) {
        final int index0 = testEntry.indexOf(',');
        final String id = testEntry.substring(0, index0);

        final StringBuffer testNameBuffer = new StringBuffer(100);
        final int index1 = scanTestName(testEntry, index0 + 1, testNameBuffer);
        final String testName = transform(testNameBuffer.toString().trim());

        final int index2 = testEntry.indexOf(',', index1 + 1);
        final boolean isSuite = testEntry.substring(index1 + 1, index2).equals("true"); //$NON-NLS-1$

        final int testCount = Integer.parseInt(testEntry.substring(index2 + 1));

        return createTestElement(parentElementSupplier.get(), id, testName, isSuite, testCount);
    }


    private String transform(final String originalString) {
        String currentString = originalString;
        for (final Transformer<String, String> transformer : testNameTransformers) {
            currentString = transformer.from(currentString);
        }
        return currentString;
    }


    @Override
    public SubstepsTestElement createTestElement(final SubstepsTestParentElement parent, final String id,
            final String testName, final boolean suite, final int testCount) {
        if (suite) {
            return new DefaultSubstepsTestParentElement(parent, id, testName, testCount);
        }
        return new SubstepsTestLeafElement(parent, id, testName);
    }


    /**
     * Append the test name from <code>s</code> to <code>testName</code>.
     * 
     * @param s
     *            the string to scan
     * @param start
     *            the offset of the first character in <code>s</code>
     * @param testName
     *            the result
     * 
     * @return the index of the next ','
     */
    private int scanTestName(final String s, final int start, final StringBuffer testName) {
        boolean inQuote = false;
        int i = start;
        for (; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (c == '\\' && !inQuote) {
                inQuote = true;
                continue;
            } else if (inQuote) {
                inQuote = false;
                testName.append(c);
            } else if (c == ',')
                break;
            else
                testName.append(c);
        }
        return i;
    }
}
