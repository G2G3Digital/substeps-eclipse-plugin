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

import java.util.LinkedList;
import java.util.List;

import com.technophobia.eclipse.transformer.Callback1;
import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.supplier.Transformer;

public class PredicatedLinkedParentItemManager<T> implements LinkedParentItemManager<T> {

    private final List<T> linkItems;
    private final Supplier<T> rootItemSupplier;
    private final Transformer<T, Boolean> hasCompletedPredicate;
    private final Callback1<T> nodeProcessor;


    public PredicatedLinkedParentItemManager(final Supplier<T> rootItemSupplier, final Callback1<T> nodeProcessor,
            final Transformer<T, Boolean> hasCompletedPredicate) {
        this.rootItemSupplier = rootItemSupplier;
        this.nodeProcessor = nodeProcessor;
        this.hasCompletedPredicate = hasCompletedPredicate;
        this.linkItems = new LinkedList<T>();
    }


    @Override
    public T get() {
        return isEmpty() ? rootItemSupplier.get() : linkItems.get(linkItems.size() - 1);
    }


    @Override
    public boolean isEmpty() {
        return linkItems.isEmpty();
    }


    @Override
    public void reset() {
        this.linkItems.clear();
    }


    @Override
    public void addNode(final T node) {
        linkItems.add(node);
    }


    @Override
    public void processOutstandingChild() {
        final T lastItem = linkItems.get(linkItems.size() - 1);
        nodeProcessor.callback(lastItem);

        if (hasCompletedPredicate.from(lastItem).booleanValue()) {
            linkItems.remove(lastItem);
        }
    }

    // @Override
    // public void addIncompleteParent(final IncompleteParentItem parent) {
    // incompleteParentItems.add(parent);
    // }
    //
    //
    // @Override
    // public IncompleteParentItem popIfComplete() {
    // final IncompleteParentItem item =
    // incompleteParentItems.get(incompleteParentItems.size() - 1);
    // if (item.outstandingChildren <= 0) {
    // incompleteParentItems.remove(item);
    // }
    // return item;
    // }
    //
    //
    // @Override
    // public void processOutstandingChild() {
    // final IncompleteParentItem item =
    // incompleteParentItems.get(incompleteParentItems.size() - 1);
    // item.decrementOutstandingChildren();
    // }

}
