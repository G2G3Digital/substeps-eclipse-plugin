package com.technophobia.eclipse.ui.render;

import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.substeps.supplier.Supplier;

public abstract class NonDisposedUiUpdater implements UiUpdater {

    private final Supplier<Boolean> disposedChecker;


    public NonDisposedUiUpdater(final Supplier<Boolean> disposedChecker) {
        this.disposedChecker = disposedChecker;
    }


    @Override
    public final void doUpdate() {
        if (!disposedChecker.get().booleanValue()) {
            safeUpdate();
        }
    }


    protected abstract void safeUpdate();
}
