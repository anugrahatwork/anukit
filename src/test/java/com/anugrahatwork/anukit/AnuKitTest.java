package com.anugrahatwork.anukit;

import com.anugrahatwork.anukit.result.Result;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AnuKitTest {

    // ====== Core Functional Method Tests ======

    @Test
    void testTryWrap_success() {
        Result<String, Exception> result = AnuKit.tryWrap(() -> "hello");
        assertTrue(result.isOk());
        assertEquals("hello", result.getOk().orElse(null));
    }

    @Test
    void testTryWrap_failure() {
        Result<String, Exception> result = AnuKit.tryWrap(() -> {
            throw new IOException("fail!");
        });
        assertTrue(result.isErr());
        assertEquals("fail!", result.getErrorMessage());
    }

    @Test
    void testSafeMap_success() {
        int output = AnuKit.safeMap("42", -1, Integer::parseInt);
        assertEquals(42, output);
    }

    @Test
    void testSafeMap_failure() {
        int output = AnuKit.safeMap("oops", -1, Integer::parseInt);
        assertEquals(-1, output);
    }

    @Test
    void testWrapList_mapSafe_success() {
        List<String> data = Arrays.asList("1", "2", "3");

        List<Result<Integer, Exception>> results = AnuKit
                .wrapList(data)
                .mapSafe(Integer::parseInt)
                .collect(Collectors.toList());

        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(Result::isOk));
    }

    @Test
    void testWrapList_mapSafe_withError() {
        List<String> data = Arrays.asList("1", "x", "3");

        List<Result<Integer, Exception>> results = AnuKit
                .wrapList(data)
                .mapSafe(Integer::parseInt)
                .collect(Collectors.toList());

        assertEquals(3, results.size());
        assertTrue(results.get(1).isErr());
        assertTrue(results.get(0).isOk());
        assertTrue(results.get(2).isOk());
    }

    @Test
    void testRunAsync_success() throws ExecutionException, InterruptedException {
        String result = AnuKit.runAsync(() -> "async-ok").get();
        assertEquals("async-ok", result);
    }

    @Test
    void testTryWrapAsync_success() throws ExecutionException, InterruptedException {
        Result<String, Exception> result = AnuKit.tryWrapAsync(() -> "async-wrap-ok").get();
        assertTrue(result.isOk());
        assertEquals("async-wrap-ok", result.getOk().orElse(null));
    }

    @Test
    void testTryWrapAsync_failure() throws ExecutionException, InterruptedException {
        Result<?, Exception> result = AnuKit.tryWrapAsync(() -> {
            throw new IllegalStateException("crash");
        }).get();

        assertTrue(result.isErr());
        assertEquals("crash", result.getErrorMessage());
    }

    // ====== Functional Interface Tests ======

    @Test
    void testCheckedSupplier_interface() throws Exception {
        AnuKit.CheckedSupplier<String> supplier = () -> "checked!";
        assertEquals("checked!", supplier.get());
    }

    @Test
    void testSafeFunction_interface() throws Exception {
        AnuKit.SafeFunction<String, Integer> safeFunc = Integer::parseInt;
        assertEquals(123, safeFunc.apply("123"));
    }

    @Test
    void testTransformer_interface() {
        AnuKit.Transformer<String, Integer> transformer = String::length;
        assertEquals(3, transformer.transform("Anu"));
    }

    @Test
    void testModifier_interface() {
        AnuKit.Modifier<Integer> doubler = val -> val * 2;
        assertEquals(10, doubler.modify(5));
    }

    @Test
    void testPureSupplier_interface() {
        AtomicInteger counter = new AtomicInteger(0);
        AnuKit.PureSupplier<Integer> supplier = counter::incrementAndGet;
        assertEquals(1, supplier.get());
        assertEquals(2, supplier.get());
    }
}
