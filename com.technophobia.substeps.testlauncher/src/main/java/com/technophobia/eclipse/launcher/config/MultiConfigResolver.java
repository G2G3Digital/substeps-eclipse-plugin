package com.technophobia.eclipse.launcher.config;

import java.util.Collection;

public interface MultiConfigResolver<T> {

    T resolver(Collection<T> allConfigs);
}
