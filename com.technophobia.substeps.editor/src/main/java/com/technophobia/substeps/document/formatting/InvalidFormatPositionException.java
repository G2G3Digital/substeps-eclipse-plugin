package com.technophobia.substeps.document.formatting;

public class InvalidFormatPositionException extends RuntimeException {

    private static final long serialVersionUID = 6608083371582181514L;


    public InvalidFormatPositionException() {
        super();
    }


    public InvalidFormatPositionException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public InvalidFormatPositionException(final String message) {
        super(message);
    }


    public InvalidFormatPositionException(final Throwable cause) {
        super(cause);
    }
}
