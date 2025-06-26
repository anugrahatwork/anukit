package com.anugrahatwork.anukit.result;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void testOkResult() {
        Result<String, Exception> result = Result.ok("Success");

        assertTrue(result.isOk());
        assertFalse(result.isErr());
        assertFalse(result.isNone());

        assertEquals(Optional.of("Success"), result.Ok());
        assertDoesNotThrow(() -> result.unwrapOrThrow());
        assertEquals("Success", result.unwrapOrThrow());
        assertEquals("Success", result.unwrapOrThrow("Somehow Java is broken"));
    }

    @Test
    void testErrResult_withException() {
        Exception error = new IOException("File error");
        Result<String, Exception> result = Result.err(error);

        assertTrue(result.isErr());
        assertFalse(result.isOk());
        assertFalse(result.isNone());

        assertEquals(Optional.of(error), result.Err());
        assertEquals("File error", result.getErrorMessage());

        RuntimeException thrown = assertThrows(RuntimeException.class, result::unwrapOrThrow);
        assertEquals("Error occurred", thrown.getMessage());
    }

    @Test
    void testErrResult_withOnErrorHandling() {
        AtomicBoolean isErrorHandled = new AtomicBoolean(false);
        Result.err(new IllegalArgumentException("Invalid"), "Custom message").onError((error) -> {
            if(Objects.nonNull(error)) {
                isErrorHandled.set(true);
            }
        });

        assertTrue(isErrorHandled.get());
    }

    @Test
    void testErrResult_withCustomMessage() {
        Result<String, Exception> result = Result.err(new IllegalArgumentException("Invalid"), "Custom message");

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> result.unwrapOrThrow("Fail reason"));
        assertTrue(thrown.getMessage().contains("Fail reason"));
    }

    @Test
    void testNoneResult() {
        Result<String, Exception> result = Result.none();

        assertTrue(result.isNone());
        assertFalse(result.isOk());
        assertFalse(result.isErr());

        assertEquals("result is uninitialized", result.getErrorMessage());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, result::unwrapOrThrow);
        assertEquals("result is uninitialized (NONE)", thrown.getMessage());
    }

    @Test
    void testInterceptMessage() {
        Result<?, RuntimeException> result = Result.err(new RuntimeException("Original error"))
                .intercept("Intercepted context");

        assertEquals("Intercepted context", result.getMessage());

        RuntimeException thrown = assertThrows(RuntimeException.class, result::unwrapOrThrow);
        assertTrue(thrown.getMessage().contains("Intercepted context"));
    }

    @Test
    void testError() {
        Exception error = new IllegalStateException("bad state");
        Result<String, Exception> result = Result.err(error);

        assertSame(error, result.getError());
    }

    @Test
    void testErrorMessage_nonThrowable() {
        Result<String, String> result = Result.err("Simple error");
        assertEquals("Simple error", result.getErrorMessage());
    }
}
