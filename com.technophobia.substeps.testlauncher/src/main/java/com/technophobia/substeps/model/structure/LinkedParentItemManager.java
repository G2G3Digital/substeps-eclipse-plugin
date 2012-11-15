package com.technophobia.substeps.model.structure;

import com.technophobia.substeps.supplier.Supplier;

public interface LinkedParentItemManager<T> extends Supplier<T> {

    void reset();


    void addNode(T node);


    void processOutstandingChild();


    boolean isEmpty();
}
