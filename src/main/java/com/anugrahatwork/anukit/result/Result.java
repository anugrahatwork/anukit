package com.anugrahatwork.anukit.result;

import lombok.Getter;

import java.util.Optional;

/**
 * A functional container that represents either a successful value ({@code Ok})
 * or an error ({@code Err}), or an uninitialized state ({@code None}).
 * <p>
 * This class is inspired by Rust’s {@code Result<T, E>} and is designed for
 * safer and expressive error handling in Java without using exceptions for control flow.
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error value (usually {@link Exception} or {@link String})
 */
public class Result<T, E> {

    private final T value;
    private final E error;

    /**
     * The current state of the {@code Result}.
     */
    @Getter
    private final State state;

    /**
     * Optional custom message (useful for debugging or context).
     */
    @Getter
    private String message;

    /**
     * Enum representing the state of the {@code Result}.
     */
    public enum State {
        /**
         * Indicates the result is a success and holds a value.
         */
        OK,

        /**
         * Indicates the result is a failure and holds an error.
         */
        ERR,

        /**
         * Indicates the result is uninitialized or empty.
         */
        NONE
    }

    // ====== Constructors ======

    private Result(T value, E error, State state) {
        this.value = value;
        this.error = error;
        this.state = state;
    }

    private Result(T value, E error, State state, String message) {
        this.value = value;
        this.error = error;
        this.state = state;
        this.message = message;
    }

    // ====== Static Constructors ======

    /**
     * Creates a success result.
     *
     * @param value the successful result
     * @return a {@code Result} representing success
     */
    public static <T, E> Result<T, E> ok(T value) {
        return new Result<>(value, null, State.OK);
    }

    /**
     * Creates a failure result with an error.
     *
     * @param error the error object
     * @return a {@code Result} representing error
     */
    public static <T, E> Result<T, E> err(E error) {
        return new Result<>(null, error, State.ERR);
    }

    /**
     * Creates a failure result with an error and a custom message.
     *
     * @param error   the error object
     * @param message additional context or description
     * @return a {@code Result} representing error
     */
    public static <T, E> Result<T, E> err(E error, String message) {
        return new Result<>(null, error, State.ERR, message);
    }

    /**
     * Creates an uninitialized {@code Result}.
     *
     * @return a {@code Result} in the {@code NONE} state
     */
    public static <T, E> Result<T, E> none() {
        return new Result<>(null, null, State.NONE);
    }

    // ====== State Checks ======

    /**
     * Returns {@code true} if this result is a success.
     */
    public boolean isOk() {
        return state == State.OK;
    }

    /**
     * Returns {@code true} if this result is a failure.
     */
    public boolean isErr() {
        return state == State.ERR;
    }

    /**
     * Returns {@code true} if this result is uninitialized.
     */
    public boolean isNone() {
        return state == State.NONE;
    }

    // ====== Accessors ======

    /**
     * Returns the success value as an {@link Optional}, or {@link Optional#empty()} if not successful.
     */
    public Optional<T> getOk() {
        return Optional.ofNullable(value);
    }

    /**
     * Returns the error value as an {@link Optional}, or {@link Optional#empty()} if not an error.
     */
    public Optional<E> getErr() {
        return Optional.ofNullable(error);
    }

    /**
     * Unwraps the value if present; throws a {@link RuntimeException} if in error or none state.
     *
     * @return the successful value
     * @throws RuntimeException if result is error or none
     */
    public T unwrapOrThrow() {
        if (isOk()) return value;
        if (isNone()) throw new IllegalStateException("result is uninitialized (NONE)");
        throw wrapToException(error);
    }

    /**
     * Unwraps the value with a custom message if an error is thrown.
     *
     * @param customMessage the message to use if exception is thrown
     * @return the successful value
     * @throws RuntimeException if result is error or none
     */
    public T unwrapOrThrow(String customMessage) {
        if (isOk()) return value;

        this.message = customMessage;

        if (isNone()) throw new IllegalStateException("result is uninitialized: " + customMessage);
        throw wrapToException(error, customMessage);
    }

    /**
     * Returns the error value, or throws if result is uninitialized.
     *
     * @return the error object
     * @throws IllegalStateException if result is none
     */
    public E getError() {
        if (isNone()) throw new IllegalStateException("result is uninitialized");
        return error;
    }

    /**
     * Returns a human-readable message from the error.
     * Supports {@link Throwable}, {@link String}, or custom types.
     */
    public String getErrorMessage() {
        if (isNone()) return "result is uninitialized";
        if (error instanceof Throwable) return ((Throwable) error).getMessage();
        return error != null ? error.toString() : null;
    }

    /**
     * Attaches a contextual message to the result (useful for tracing).
     *
     * @param message the message to attach
     * @return the current {@code Result} instance
     */
    public Result<T, E> intercept(String message) {
        this.message = message;
        return this;
    }

    // ====== Internal Utility ======

    private RuntimeException wrapToException(E err) {
        return wrapToException(err, this.message);
    }

    private RuntimeException wrapToException(E err, String msg) {
        if (err instanceof Throwable) {
            return new RuntimeException(msg != null ? msg : "Error occurred", (Throwable) err);
        }
        return new RuntimeException((msg != null ? msg + ": " : "") + err);
    }
}
