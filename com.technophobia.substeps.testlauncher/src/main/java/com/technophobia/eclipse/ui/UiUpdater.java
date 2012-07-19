package com.technophobia.eclipse.ui;

public interface UiUpdater extends Resettable {

    void doUpdate();


    @Override
    public void reset();
}
