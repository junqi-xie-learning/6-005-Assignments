/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    //   Graph<String>
    //     add(), set(), remove(), vertices(), sources(), targets(), toString()
    //   Graph<Integer>
    //     add(), set(), remove(), vertices(), sources(), targets(), toString()
    //   Graph<BigInteger>
    //     add(), set(), remove(), vertices(), sources(), targets(), toString()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }

    @Test
    public void testGraphString() {
        Graph<String> graph = Graph.empty();
        graph.add("init");
        graph.set("init", "init", 1);
        graph.set("source", "target", 2);
        graph.set("target", "source", 3);
        graph.remove("target");
        graph.set("init", "source", 4);
        assertEquals("expected source vertex in the graph",
                Set.of("init", "source"), graph.vertices());
        assertEquals("expected edges from init",
                Map.of("init", 1, "source", 4), graph.targets("init"));
        assertEquals("expected edge to init",
                Map.of("init", 1), graph.sources("init"));
        assertEquals("expected no edges from source",
                Collections.emptyMap(), graph.targets("source"));
        assertEquals("expected edges to source",
                Map.of("init", 4), graph.sources("source"));
        assertEquals("expected graph with multiple edges to string",
                "(init -> init, 1)\n(init -> source, 4)", graph.toString());
    }

    @Test
    public void testGraphInteger() {
        Graph<Integer> graph = Graph.empty();
        graph.add(0);
        graph.set(0, 0, 1);
        graph.set(1, 2, 2);
        graph.set(2, 1, 3);
        graph.remove(2);
        graph.set(0, 1, 4);
        assertEquals("expected source vertex in the graph",
                Set.of(0, 1), graph.vertices());
        assertEquals("expected edges from init",
                Map.of(0, 1, 1, 4), graph.targets(0));
        assertEquals("expected edge to init",
                Map.of(0, 1), graph.sources(0));
        assertEquals("expected no edges from source",
                Collections.emptyMap(), graph.targets(1));
        assertEquals("expected edges to source",
                Map.of(0, 4), graph.sources(1));
        assertEquals("expected graph with multiple edges to string",
                "(0 -> 0, 1)\n(0 -> 1, 4)", graph.toString());
    }

    @Test
    public void testGraphBigInteger() {
        Graph<BigInteger> graph = Graph.empty();
        graph.add(BigInteger.ZERO);
        graph.set(BigInteger.ZERO, BigInteger.ZERO, 1);
        graph.set(BigInteger.ONE, BigInteger.TWO, 2);
        graph.set(BigInteger.TWO, BigInteger.ONE, 3);
        graph.remove(BigInteger.TWO);
        graph.set(BigInteger.ZERO, BigInteger.ONE, 4);
        assertEquals("expected source vertex in the graph",
                Set.of(BigInteger.ZERO, BigInteger.ONE), graph.vertices());
        assertEquals("expected edges from init",
                Map.of(BigInteger.ZERO, 1, BigInteger.ONE, 4), graph.targets(BigInteger.ZERO));
        assertEquals("expected edge to init",
                Map.of(BigInteger.ZERO, 1), graph.sources(BigInteger.ZERO));
        assertEquals("expected no edges from source",
                Collections.emptyMap(), graph.targets(BigInteger.ONE));
        assertEquals("expected edges to source",
                Map.of(BigInteger.ZERO, 4), graph.sources(BigInteger.ONE));
        assertEquals("expected graph with multiple edges to string",
                "(0 -> 0, 1)\n(0 -> 1, 4)", graph.toString());
    }

}
