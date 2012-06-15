package com.technophobia.eclipse.transformer;

import java.util.Collection;

public interface Locator<T, Context> {

    Collection<T> all(Context c);


    T one(Context c);
}
