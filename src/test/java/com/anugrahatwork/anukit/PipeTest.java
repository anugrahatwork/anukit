package com.anugrahatwork.anukit;

import com.anugrahatwork.anukit.result.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PipeTest {

    @Test
    void testOf_withInitialValue() {
        Pipe<String> pipe = Pipe.of("start");
        assertTrue(pipe.getResult().isOk());
        assertEquals("start", pipe.getResult().unwrapOrThrow());
    }

    @Test
    void testConstructor_withCheckedSupplier_success() {
        Pipe<Integer> pipe = new Pipe<>(() -> 42);
        Result<Integer, Exception> result = pipe.getResult();

        assertTrue(result.isOk());
        assertEquals(42, result.unwrapOrThrow());
    }

    @Test
    void testConstructor_withCheckedSupplier_failure() {
        Pipe<Integer> pipe = new Pipe<>(() -> {
            throw new IllegalStateException("fail");
        });

        Result<Integer, Exception> result = pipe.getResult();
        assertTrue(result.isErr());
        assertEquals("fail", result.getErrorMessage());
    }

    @Test
    void testThen_chainSuccessfulTransforms() {
        Pipe<String> pipe = Pipe.of("anu")
                .then(String::toUpperCase)
                .then(s -> s + "-KIT");

        Result<String, Exception> result = pipe.getResult();
        assertTrue(result.isOk());
        assertEquals("ANU-KIT", result.unwrapOrThrow());
    }

    @Test
    void testThen_doesNotApplyIfErrorState() {
        Pipe<?> pipe = new Pipe<>(() -> {
            throw new RuntimeException("boom");
        }).then(s -> s + "extra");

        Result<?, Exception> result = pipe.getResult();
        assertTrue(result.isErr());
        assertEquals("boom", result.getErrorMessage());
    }

    @Test
    void testCatchError_transformsErrorState() {
        Pipe<String> pipe = new Pipe<>(() -> {
            throw new RuntimeException("problem");
        });

        Result<String, Exception> handled = pipe.mapResult(res -> {
            if (res.isErr()) {
                return "fallback";
            }
            return res.unwrapOrThrow();
        });

        assertTrue(handled.isOk());
        assertEquals("fallback", handled.unwrapOrThrow());
    }

    @Test
    void testMapResult_preservesOkState() {
        Pipe<String> pipe = Pipe.of("ok");

        Result<String, Exception> result = pipe.mapResult(Result::unwrapOrThrow);
        assertTrue(result.isOk());
        assertEquals("ok", result.unwrapOrThrow());
    }
}
