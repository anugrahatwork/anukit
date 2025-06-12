package com.anugrahatwork.anukit.result.;

import lombok.Getter;

/**
 * A custom {@link RuntimeException} used by {@link Result} to propagate errors in a structured way.
 * <p>
 * This exception can wrap any error object (not just {@link Throwable}),
 * making it useful for propagating messages, codes, or rich error types.
 */
@Getter
public class PropagateErrorException extends RuntimeException {

    /**
     * The underlying error object. May be a {@link Throwable}, {@link String}, or any custom type.
     */
    private final Object error;

    /**
     * Constructs an exception from an error object.
     *
     * @param error the error object (message, exception, etc.)
     */
    public PropagateErrorException(Object error) {
        super(error != null ? error.toString() : "Unknown error");
        this.error = error;
    }

    /**
     * Constructs an exception with a custom message and error object.
     *
     * @param message human-readable message
     * @param error   the raw error object
     */
    public PropagateErrorException(String message, Object error) {
        super(message != null ? message : error != null ? error.toString() : "Unknown error");
        this.error = error;
    }

    /**
     * Constructs an exception with message, cause, and error object.
     *
     * @param message human-readable message
     * @param cause   underlying cause (optional)
     * @param error   the raw error object
     */
    public PropagateErrorException(String message, Throwable cause, Object error) {
        super(message != null ? message : error != null ? error.toString() : "Unknown error", cause);
        this.error = error;
    }
}