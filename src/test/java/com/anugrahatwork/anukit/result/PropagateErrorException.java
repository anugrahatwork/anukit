package com.anugrahatwork.anukit.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropagateErrorExceptionTest {

    @Test
    void testConstructor_withErrorObject_only() {
        PropagateErrorException ex = new PropagateErrorException("something went wrong");

        assertEquals("something went wrong", ex.getMessage());
        assertEquals("something went wrong", ex.getError());
    }

    @Test
    void testConstructor_withNullError() {
        PropagateErrorException ex = new PropagateErrorException(null);

        assertEquals(ex.getMessage(), "Unknown error");
        assertNull(ex.getError());
    }

    @Test
    void testConstructor_withMessageAndError() {
        PropagateErrorException ex = new PropagateErrorException("custom message", "internal error");

        assertEquals("custom message", ex.getMessage());
        assertEquals("internal error", ex.getError());
    }

    @Test
    void testConstructor_withMessageCauseAndError() {
        Throwable cause = new IllegalArgumentException("bad argument");

        PropagateErrorException ex = new PropagateErrorException("wrapped error", cause, "myErrorObject");

        assertEquals("wrapped error", ex.getMessage());
        assertEquals("myErrorObject", ex.getError());
        assertSame(cause, ex.getCause());
    }

    @Test
    void testConstructor_withThrowableAsError() {
        Throwable error = new IllegalStateException("state issue");
        PropagateErrorException ex = new PropagateErrorException(error);

        assertEquals("state issue", ex.getMessage());
        assertEquals(error, ex.getError());
    }

    @Test
    void testCustomObjectAsError() {
        class CustomError {
            @Override
            public String toString() {
                return "CustomError#42";
            }
        }

        CustomError errObj = new CustomError();
        PropagateErrorException ex = new PropagateErrorException(errObj);

        assertEquals("CustomError#42", ex.getMessage());
        assertSame(errObj, ex.getError());
    }
}
