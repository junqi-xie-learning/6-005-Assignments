/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   vertices represents the vertices in the graph, as well as the outgoing
    //     edges from the vertices to target labels with weights
    // Representation invariant:
    //   vertices don't contain vertices of the same label
    // Safety from rep exposure:
    //   Vertex and Set<Vertex> are never returned, and are declared as private final
    
    /**
     * Check the rep invariant.
     */
    private void checkRep() {
        Set<String> labels = new HashSet<>();
        for (Vertex vertex : vertices) {
            String label = vertex.getLabel();
            assert labels.contains(label) == false;
            labels.add(label);
        }
    }

    /**
     * Find the vertex with label in vertices.
     * 
     * @param label label of the vertex
     * @return the vertex with label
     * @throws NoSuchElementException if not found
     */
    private Vertex findVertex(String label) throws NoSuchElementException {
        for (Vertex vertex : vertices) {
            if (label == vertex.getLabel()) {
                return vertex;
            }
        }
        throw new NoSuchElementException();
    }

    @Override public boolean add(String vertex) {
        try {
            findVertex(vertex);
            return true;
        }
        catch (NoSuchElementException e) {
            vertices.add(new Vertex(vertex));
            checkRep();
            return false;
        }
    }

    @Override public int set(String source, String target, int weight) {
        assert weight >= 0;
        if (weight > 0) {
            this.add(source);
            this.add(target);
        }

        try {
            int original = findVertex(source).set(target, weight);
            checkRep();
            return original;
        }
        catch (NoSuchElementException e) {
            return 0;
        }
    }

    @Override public boolean remove(String vertex) {
        try {
            vertices.remove(findVertex(vertex));
            for (Vertex v : vertices) {
                v.set(vertex, 0);
            }
            checkRep();
            return true;
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override public Set<String> vertices() {
        Set<String> result = new HashSet<>();
        for (Vertex vertex : vertices) {
            result.add(vertex.getLabel());
            for (String target : vertex.getTargets().keySet()) {
                result.add(target);
            }
        }
        return result;
    }

    @Override public Map<String, Integer> sources(String target) {
        Map<String, Integer> result = new HashMap<>();
        for (Vertex vertex : vertices) {
            Map<String, Integer> targets = vertex.getTargets();
            if (targets.keySet().contains(target)) {
                result.put(vertex.getLabel(), targets.get(target));
            }
        }
        return result;
    }

    @Override public Map<String, Integer> targets(String source) {
        return findVertex(source).getTargets();
    }

    /**
     * @return a list of strings in the form "(source -> target, weight)",
     *         representing the edges in the graph
     */
    @Override public String toString() {
        String result = "";
        for (Vertex vertex : vertices) {
            result = result.concat(vertex.toString() + "\n");
        }
        return result.strip();
    }
    
}

/**
 * Vertex represents a vertex in a graph with a mutable map of outgoing edge
 *   targets and weights.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex {
    
    // TODO fields
    private final String label;
    private final Map<String, Integer> targets = new HashMap<>();
    
    // Abstraction function:
    //   represents a set of edges from label to targets with weights
    // Representation invariant:
    //   weights in targets are positive integers
    // Safety from rep exposure:
    //   String is immutable, and is declared as private final
    //   Map<String, Integer> is copied when returned, and is declared as
    //     private final
    
    /**
     * Make a vertex with empty outgoing edge targets.
     * 
     * @param label label of the vertex
     */
    public Vertex(String label) {
        this.label = label;
    }

    /**
     * Check the rep invariant.
     */
    private void checkRep() {
        for (int weight : targets.values()) {
            assert weight > 0;
        }
    }

    /**
     * Get the label of the vertex.
     * 
     * @return a string of the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the outgoing edge targets and weights from the vertex.
     * 
     * @return a copy of the map containing outgoing edge targets and weights
     */
    public Map<String, Integer> getTargets() {
        return new HashMap<>(targets);
    }

    /**
     * Add, change, or remove a weighted directed edge from the vertex.
     * If weight is nonzero, add an edge or update the weight of that edge.
     * If weight is zero, remove the edge if it exists.
     * 
     * @param target label of the target vertex
     * @param weight nonnegative weight of the edge
     * @return the previous weight of the edge, or zero if there was no such
     *         edge
     */
    public int set(String target, int weight) {
        assert weight >= 0;

        int original;
        if (targets.containsKey(target)) {
            original = targets.remove(target);
        }
        else {
            original = 0;
        }
        if (weight > 0) {
            targets.put(target, weight);
        }

        checkRep();
        return original;
    }

    /**
     * @return a list of strings in the form "(vertex -> target, weight)",
     *         representing the edges outgoing from the vertex
     */
    @Override public String toString() {
        String result = "";
        Map<String, Integer> targets = this.getTargets();
        for (String target : targets.keySet()) {
            result = result.concat(
                "(" + this.getLabel()
                + " -> " + target
                + ", " + targets.get(target)
                + ")\n"
            );
        }
        return result.strip();
    }

}
