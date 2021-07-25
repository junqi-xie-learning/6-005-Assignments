/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    //   add(vertex)
    //     vertex: exists, doesn't exist
    //     observe with vertices()
    //   set(source, target, weight)
    //     source: exists, doesn't exist
    //     target: exists, doesn't exist
    //     source equals target or doesn't
    //     directed edge: exists, doesn't exist
    //     weight: zero, nonzero
    //     observe with vertices(), sources(), targets()
    //   remove(vertex)
    //     vertex: exists, doesn't exist
    //     vertices in graph: 0, 1, >1
    //     observe with vertices(), sources(), targets()
    //   vertices()
    //     vertices in graph: 0, 1, >1
    //   sources(target)
    //     edges to target: 0, 1, >1
    //   targets(source)
    //     edges from source: 0, 1, >1
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    @Test
    public void testAddVertexToEmptyGraph() {
        Graph<String> graph = emptyInstance();
        boolean result = graph.add("init");
        assertEquals("expected initial vertex in the graph",
                Set.of("init"), graph.vertices());
        assertFalse("expected graph did not already include initial vertex", result);
    }

    @Test
    public void testAddVertexToExistingGraph() {
        Graph<String> graph = emptyInstance();
        graph.add("init");
        boolean result = graph.add("init");
        assertEquals("expected initial vertex in the graph",
                Set.of("init"), graph.vertices());
        assertTrue("expected graph already included initial vertex", result);
    }
    
    @Test
    public void testAddEdgeToExistingGraph() {
        Graph<String> graph = emptyInstance();
        graph.add("source");
        graph.add("target");
        int result = graph.set("source", "target", 1);
        assertEquals("expected edge from source",
                Map.of("target", 1), graph.targets("source"));
        assertEquals("expected edge to target",
                Map.of("source", 1), graph.sources("target"));
        assertEquals("expected no such edge", 0, result);
    }

    @Test
    public void testAddEdgeToEmptyGraph() {
        Graph<String> graph = emptyInstance();
        int result = graph.set("source", "target", 1);
        assertEquals("expected source vertex and target vertex",
                Set.of("source", "target"), graph.vertices());
        assertEquals("expected edge from source",
                Map.of("target", 1), graph.targets("source"));
        assertEquals("expected edge to target",
                Map.of("source", 1), graph.sources("target"));
        assertEquals("expected no such edge", 0, result);
    }

    @Test
    public void testAddLoopToEmptyGraph() {
        Graph<String> graph = emptyInstance();
        int result = graph.set("init", "init", 1);
        assertEquals("expected initial vertex in the graph",
                Set.of("init"), graph.vertices());
        assertEquals("expected edge from init",
                Map.of("init", 1), graph.targets("init"));
        assertEquals("expected edge to init",
                Map.of("init", 1), graph.sources("init"));
        assertEquals("expected no such edge", 0, result);
    }

    @Test
    public void testChangeEdgeInExistingGraph() {
        Graph<String> graph = emptyInstance();
        graph.set("source", "target", 1);
        int result = graph.set("source", "target", 2);
        assertEquals("expected edge from source",
                Map.of("target", 2), graph.targets("source"));
        assertEquals("expected edge to target",
                Map.of("source", 2), graph.sources("target"));
        assertEquals("expected previous weight of edge", 1, result);
    }

    @Test
    public void testRemoveEdgeFromExistingGraph() {
        Graph<String> graph = emptyInstance();
        graph.set("source", "target", 1);
        int result = graph.set("source", "target", 0);
        assertEquals("expected source vertex and target vertex",
                Set.of("source", "target"), graph.vertices());
        assertEquals("expected no edges from source",
                Collections.emptyMap(), graph.targets("source"));
        assertEquals("expected no edges to target",
                Collections.emptyMap(), graph.sources("target"));
        assertEquals("expected previous weight of edge", 1, result);
    }

    @Test
    public void testRemoveEdgeFromEmptyGraph() {
        Graph<String> graph = emptyInstance();
        int result = graph.set("source", "target", 0);
        assertEquals("expected graph to have no vertices",
                Collections.emptySet(), graph.vertices());
        assertEquals("expected no such edge", 0, result);
    }

    @Test
    public void testRemoveLoopFromExistingGraph() {
        Graph<String> graph = emptyInstance();
        graph.set("init", "init", 1);
        int result = graph.set("init", "init", 0);
        assertEquals("expected initial vertex in the graph",
                Set.of("init"), graph.vertices());
        assertEquals("expected no edges from init",
                Collections.emptyMap(), graph.targets("init"));
        assertEquals("expected no edges to init",
                Collections.emptyMap(), graph.sources("init"));
        assertEquals("expected previous weight of edge", 1, result);
    }

    @Test
    public void testRemoveVertexFromExistingGraph() {
        Graph<String> graph = emptyInstance();
        graph.set("source", "target", 1);
        graph.set("target", "source", 2);
        boolean result = graph.remove("target");
        assertEquals("expected source vertex in the graph",
                Set.of("source"), graph.vertices());
        assertEquals("expected no edges from source",
                Collections.emptyMap(), graph.targets("source"));
        assertEquals("expected no edges to source",
                Collections.emptyMap(), graph.sources("source"));
        assertTrue("expected graph included target vertex", result);
    }

    @Test
    public void testRemoveVertexFromEmptyGraph() {
        Graph<String> graph = emptyInstance();
        boolean result = graph.remove("init");
        assertEquals("expected graph to have no vertices",
                Collections.emptySet(), graph.vertices());
        assertFalse("expected graph didn't include init vertex", result);
    }

}
