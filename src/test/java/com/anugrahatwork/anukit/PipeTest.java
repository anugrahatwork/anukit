package com.anugrahatwork.anukit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PipeTest {

    @Test
    void testPipe_singleTransformation() {
        Pipe<String> pipe = Pipe.of("hello")
                .then(String::toUpperCase);

        assertEquals("HELLO", pipe.getValue());
    }

    @Test
    void testPipe_multipleTransformations() {
        String result = Pipe.of("anu")
                .then(String::toUpperCase)
                .then(str -> str + "KIT")
                .then(str -> str.replace("U", "Ü"))
                .getValue();

        assertEquals("ANÜKIT", result);
    }

    @Test
    void testPipe_numericOperations() {
        Pipe<Integer> pipe = Pipe.of(5)
                .then(n -> n * 2)
                .then(n -> n + 3);

        assertEquals(13, pipe.getValue());
    }

    @Test
    void testPipe_mutateValueDirectly() {
        Pipe<String> pipe = Pipe.of("start");
        pipe.setValue("reset");

        assertEquals("reset", pipe.getValue());
    }

    @Test
    void testPipeTransformer_interface() {
        Pipe.PipeTransformer<String> reverse = s -> new StringBuilder(s).reverse().toString();

        String result = Pipe.of("anu").then(reverse).getValue();

        assertEquals("una", result);
    }

    @Test
    void testPipe_ofFactoryMethod() {
        Pipe<Double> pipe = Pipe.of(3.14);
        assertEquals(3.14, pipe.getValue());
    }
}
