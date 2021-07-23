/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //   edges in graph: 0, 1, >1
    
    @Test
    public void testVerticesGraphToStringEmpty() {
        assertEquals("expected empty graph to string",
                "", new ConcreteVerticesGraph<>().toString());
    }

    @Test
    public void testVerticesGraphToStringSingleEdge() {
        Graph<String> graph = new ConcreteVerticesGraph<>();
        graph.set("source", "target", 1);
        assertEquals("expected graph with a single edge to string",
                "(source -> target, 1)", graph.toString());
    }

    @Test
    public void testVerticesGraphToStringMultipleEdges() {
        Graph<String> graph = new ConcreteVerticesGraph<>();
        graph.set("source", "target", 1);
        graph.set("target", "source", 2);
        assertEquals("expected graph with multiple edges to string",
                "(source -> target, 1)\n(target -> source, 2)", graph.toString());
    }
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    //   set(target, weight)
    //     target: exists, doesn't exist
    //     source equals target or doesn't
    //   toString()
    //     targets number: 0, 1, >1
    
    @Test
    public void testVertexAddEdge() {
        Vertex<String> vertex = new Vertex<>("source");
        int result = vertex.set("target", 1);
        assertEquals("expected edge from source",
                Map.of("target", 1), vertex.getTargets());
        assertEquals("expected no such edge", 0, result);
    }

    @Test
    public void testVertexAddLoop() {
        Vertex<String> vertex = new Vertex<>("init");
        int result = vertex.set("init", 1);
        assertEquals("expected edge from init",
                Map.of("init", 1), vertex.getTargets());
        assertEquals("expected no such edge", 0, result);
    }

    @Test
    public void testVertexChange() {
        Vertex<String> vertex = new Vertex<>("source");
        vertex.set("target", 1);
        int result = vertex.set("target", 2);
        assertEquals("expected edge from source",
                Map.of("target", 2), vertex.getTargets());
        assertEquals("expected previous weight of edge", 1, result);
    }

    @Test
    public void testVertexRemoveEdge() {
        Vertex<String> vertex = new Vertex<>("source");
        vertex.set("target", 1);
        int result = vertex.set("target", 0);
        assertEquals("expected no edges from source",
                Collections.emptyMap(), vertex.getTargets());
        assertEquals("expected previous weight of edge", 1, result);
    }

    @Test
    public void testVertexRemoveEmpty() {
        Vertex<String> vertex = new Vertex<>("source");
        int result = vertex.set("target", 0);
        assertEquals("expected no edges from source",
                Collections.emptyMap(), vertex.getTargets());
        assertEquals("expected no such edge", 0, result);
    }

    @Test
    public void testVertexRemoveLoop() {
        Vertex<String> vertex = new Vertex<>("init");
        vertex.set("init", 1);
        int result = vertex.set("init", 0);
        assertEquals("expected no edges from init",
                Collections.emptyMap(), vertex.getTargets());
        assertEquals("expected previous weight of edge", 1, result);
    }

    @Test
    public void testVertexToStringEmpty() {
        assertEquals("expected empty graph to string",
                "", new Vertex<>("init").toString());
    }

    @Test
    public void testEdgesGraphToStringSingleEdge() {
        Vertex<String> vertex = new Vertex<>("source");
        vertex.set("target", 1);
        assertEquals("expected graph with a single edge to string",
                "(source -> target, 1)", vertex.toString());
    }

    @Test
    public void testEdgesGraphToStringMultipleEdges() {
        Vertex<String> vertex = new Vertex<>("source");
        vertex.set("target", 1);
        vertex.set("source", 2);
        assertEquals("expected graph with multiple edges to string",
                "(source -> source, 2)\n(source -> target, 1)", vertex.toString());
    }

}
