package com.technophobia.substeps.supplier;

public interface Transformer<From, To> {

    To to(From from);
}
