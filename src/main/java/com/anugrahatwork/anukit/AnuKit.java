package com.anugrahatwork.anukit;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import com.anugrahatwork.anukit.result.Result;

/**
 * AnuKit – Functional-style helper utilities for exception-safe and asynchronous Java programming.
 */
public class AnuKit {

    // ========== FUNCTIONAL INTERFACES ==========

    /**
     * A simple supplier that returns a value without throwing exceptions.
     * Similar to {@link java.util.function.Supplier}.
     */
    @FunctionalInterface
    public interface PureSupplier<T> {
        T get();
    }

    /**
     * A supplier that can throw checked exceptions.
     */
    @FunctionalInterface
    public interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    /**
     * A function that takes input and returns a result, potentially throwing an exception.
     */
    @FunctionalInterface
    public interface SafeFunction<T, R> {
        R apply(T input) throws Exception;
    }

    /**
     * A simple transformer interface with no exception.
     */
    public interface Transformer<T, R> {
        R transform(T input);
    }

    /**
     * An interface for simple mutation-style operations.
     */
    public interface Modifier<T> {
        T modify(T input);
    }

    // ========== CORE METHODS ==========

    /**
     * Attempts to execute a checked supplier, wrapping the result in a {@link Result}.
     *
     * @param supplier function that may throw
     * @param <T>      type of successful result
     * @return {@link Result} of success or error
     */
    public static <T> Result<T, Exception> tryWrap(CheckedSupplier<T> supplier) {
        try {
            return Result.ok(supplier.get());
        } catch (Exception e) {
            return Result.err(e);
        }
    }

    /**
     * Executes the given function and returns a fallback value if an error occurs.
     *
     * @param input the input value
     * @param fallback the fallback value if error happens
     * @param function the function to apply
     * @return transformed value or fallback
     */
    public static <T, R> R safeMap(T input, R fallback, SafeFunction<T, R> function) {
        try {
            return function.apply(input);
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Wraps a list into a ResultStreamHolder for safe functional transformations.
     *
     * @param collection source list
     * @param <T>        element type
     * @return wrapped stream
     */
    public static <T> ResultStreamHolder<T> wrapList(List<T> collection) {
        return new ResultStreamHolder<>(collection.stream());
    }

    /**
     * Executes a checked supplier asynchronously, returning a {@link Result} wrapped in a {@link CompletableFuture}.
     */
    public static <T> CompletableFuture<Result<T, Exception>> tryWrapAsync(CheckedSupplier<T> supplier) {
        return runAsync(() -> tryWrap(supplier));
    }

    /**
     * Executes a supplier asynchronously using {@link CompletableFuture}.
     */
    public static <T> CompletableFuture<T> runAsync(PureSupplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier::get);
    }

    // ========== HELPER CLASSES ==========

    /**
     * Wrapper for streaming collections with safe functional transforms.
     */
    @AllArgsConstructor
    public static class ResultStreamHolder<T> {
        private final Stream<T> stream;

        /**
         * Applies a function to each stream item, wrapping results in {@link Result}.
         */
        public <R> Stream<Result<R, Exception>> mapSafe(SafeFunction<T, R> mapper) {
            return stream.map(item -> tryWrap(() -> mapper.apply(item)));
        }
    }
}
