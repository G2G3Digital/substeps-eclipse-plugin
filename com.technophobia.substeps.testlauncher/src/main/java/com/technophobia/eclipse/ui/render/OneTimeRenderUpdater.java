package com.technophobia.eclipse.ui.render;

import com.technophobia.eclipse.ui.NotifyingUiUpdater;
import com.technophobia.eclipse.ui.Renderer;

public class OneTimeRenderUpdater<T> implements NotifyingUiUpdater<T> {

    private T item;
    private final Renderer<T> renderer;


    public OneTimeRenderUpdater(final Renderer<T> renderer) {
        this.renderer = renderer;
    }


    @Override
    public void notify(final T t) {
        this.item = t;
    }


    @Override
    public void doUpdate() {
        if (item != null) {
            renderer.render(item);
            item = null;
        }
    }


    @Override
    public void reset() {
        item = null;
    }


    @Override
    public T currentValue() {
        return item;
    }

}
