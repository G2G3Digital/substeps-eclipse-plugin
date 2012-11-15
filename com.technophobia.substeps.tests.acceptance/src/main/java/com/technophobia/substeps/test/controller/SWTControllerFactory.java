package com.technophobia.substeps.test.controller;

public class SWTControllerFactory {

	public static <T extends SWTController> T createFor(
			final Class<T> constructorClass) {
		try {
			return constructorClass.newInstance();
		} catch (final Exception ex) {
			throw new RuntimeException("Could not create item "
					+ constructorClass.getName(), ex);
		}
	}
}
