/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   edges in graph: 0, 1, >1

    @Test
    public void testEdgesGraphToStringEmpty() {
        assertEquals("expected empty graph to string",
                "", new ConcreteEdgesGraph().toString());
    }

    @Test
    public void testEdgesGraphToStringSingleEdge() {
        Graph<String> graph = new ConcreteEdgesGraph();
        graph.set("source", "target", 1);
        assertEquals("expected graph with a single edge to string",
                "(source -> target, 1)", graph.toString());
    }

    @Test
    public void testEdgesGraphToStringMultipleEdges() {
        Graph<String> graph = new ConcreteEdgesGraph();
        graph.set("source", "target", 1);
        graph.set("target", "source", 2);
        assertEquals("expected graph with multiple edges to string",
                "(source -> target, 1)\n(target -> source, 2)", graph.toString());
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    //   constructor
    //     weight: nonpositive, positive
    //   toString()

    @Test
    public void testEdgePositive() {
        Edge edge = new Edge("source", "target", 1);
        assertEquals("expected source label", "source", edge.getSource());
        assertEquals("expected source label", "target", edge.getTarget());
        assertEquals("expected source label", 1, edge.getWeight());
    }

    @Test(expected=AssertionError.class)
    public void testEdgeNonpositive() {
        Edge edge = new Edge("source", "target", 0);
        // exception expected
    }

    @Test
    public void testEdgeToString() {
        Edge edge = new Edge("source", "target", 1);
        assertEquals("expected string", "(source -> target, 1)", edge.toString());
    }
    
}
