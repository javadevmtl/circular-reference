package com.acme;

import io.quarkus.test.junit.QuarkusTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class Test {

    @org.junit.jupiter.api.Test
    public void test() {

        assertEquals(true, true);
    }
}