package com.anugrahatwork.anukit;

import lombok.Getter;
import lombok.Setter;

/**
 * A lightweight mutable container for chaining transformations fluently.
 * <p>
 * Inspired by functional programming pipes and method chaining patterns.
 *
 * @param <T> the wrapped value type
 */
@Getter
@Setter
public class Pipe<T> {

    private T value;

    /**
     * Creates a new anukit.Pipe with the initial value.
     *
     * @param value the value to wrap
     */
    public Pipe(T value) {
        this.value = value;
    }

    /**
     * Applies a transformation to the value and returns the updated pipe.
     * <p>
     * Example:
     * <pre>
     *     anukit.Pipe.of("hello")
     *         .then(str -> str.toUpperCase())
     *         .then(str -> str + " world")
     *         .getValue(); // "HELLO world"
     * </pre>
     *
     * @param transformer a function to apply to the current value
     * @return the current anukit.Pipe with updated value
     */
    public Pipe<T> then(PipeTransformer<T> transformer) {
        this.value = transformer.apply(value);
        return this;
    }

    /**
     * Static factory method for cleaner instantiation.
     *
     * @param value the value to wrap
     * @param <T>   the type
     * @return a new anukit.Pipe instance
     */
    public static <T> Pipe<T> of(T value) {
        return new Pipe<>(value);
    }

    /**
     * Functional interface representing a transformation on the anukit.Pipe value.
     */
    @FunctionalInterface
    public interface PipeTransformer<T> {
        T apply(T value);
    }
}
