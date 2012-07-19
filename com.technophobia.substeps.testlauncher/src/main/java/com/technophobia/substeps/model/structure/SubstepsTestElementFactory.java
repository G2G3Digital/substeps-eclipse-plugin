package com.technophobia.substeps.model.structure;

import com.technophobia.eclipse.transformer.Supplier;

public interface SubstepsTestElementFactory {

    SubstepsTestElement createForTestEntryString(String testEntry,
            Supplier<SubstepsTestParentElement> parentElementSupplier);


    SubstepsTestElement createTestElement(SubstepsTestParentElement parent, String id, String testName, boolean suite,
            int testCount);
}
