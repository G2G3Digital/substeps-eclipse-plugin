package com.technophobia.eclipse.transformer;

public interface Transformer<From, To> {

	To to(From from);
}
