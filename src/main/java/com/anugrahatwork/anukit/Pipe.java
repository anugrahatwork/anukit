package com.anugrahatwork.anukit;

import com.anugrahatwork.anukit.result.Result;
import lombok.Getter;

/**
 * A fluent wrapper for safely applying transformations to a value,
 * capturing success and failure as a {@link Result}.
 * <p>
 * Inspired by functional programming pipes and method chaining patterns.
 *
 * @param <T> the type of the wrapped value
 */
@Getter
public class Pipe<T> {
    /**
     * The current {@link Result} representing the state of transformation.
     */
    private Result<T, Exception> result;

    /**
     * Private constructor for static factory.
     *
     * @param value the initial value
     */
    private Pipe(T value) {
        this.result = Result.ok(value);
    }

    /**
     * Constructs a Pipe from a {@link AnuKit.CheckedSupplier}, wrapping exceptions into a {@link Result}.
     *
     * @param supplier a supplier that may throw
     */
    public Pipe(AnuKit.CheckedSupplier<T> supplier) {
        this.result = AnuKit.tryWrap(supplier);
    }

    /**
     * Applies a transformation to the value if the current state is {@code ok()},
     * and updates the result.
     *
     * @param modifier a function to apply to the current value
     * @return this Pipe (for chaining)
     */
    public Pipe<T> then(AnuKit.Modifier<T> modifier) {
        if (result.isOk()) {
            this.result = AnuKit.tryWrap(() -> modifier.modify(this.result.unwrapOrThrow()));
        }
        return this;
    }

    /**
     * Applies a final transformation to the internal {@link Result}, producing a new {@link Result}.
     *
     * @param transformer function to transform the result
     * @param <R> return type
     * @return new transformed {@link Result}
     */
    public <R> Result<R, Exception> mapResult(AnuKit.Transformer<Result<T, Exception>, R> transformer) {
        return AnuKit.tryWrap(() -> transformer.transform(result));
    }

    /**
     * Creates a Pipe from a raw value.
     *
     * @param value the initial value
     * @param <T> the value type
     * @return a new Pipe instance
     */
    public static <T> Pipe<T> of(T value) {
        return new Pipe<>(value);
    }
}
