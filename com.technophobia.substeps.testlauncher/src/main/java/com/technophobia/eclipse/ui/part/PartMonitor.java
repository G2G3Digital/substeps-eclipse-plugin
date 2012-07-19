package com.technophobia.eclipse.ui.part;

import org.eclipse.ui.IPartListener2;

public interface PartMonitor extends IPartListener2 {

    boolean isPartVisible();
}
