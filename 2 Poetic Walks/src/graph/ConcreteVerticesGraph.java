/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
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
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   vertices represents the vertices in the graph, as well as the outgoing
    //     edges from the vertices to target labels with weights
    // Representation invariant:
    //   vertices don't contain vertices of the same label
    // Safety from rep exposure:
    //   Vertex<L> and Set<Vertex<L>> are never returned, and are declared as private final
    
    /**
     * Check the rep invariant.
     */
    private void checkRep() {
        Set<L> labels = new HashSet<>();
        for (Vertex<L> vertex : vertices) {
            L label = vertex.getLabel();
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
    private Vertex<L> findVertex(L label) throws NoSuchElementException {
        for (Vertex<L> vertex : vertices) {
            if (label.equals(vertex.getLabel())) {
                return vertex;
            }
        }
        throw new NoSuchElementException();
    }

    @Override public boolean add(L vertex) {
        try {
            findVertex(vertex);
            return true;
        }
        catch (NoSuchElementException e) {
            vertices.add(new Vertex<L>(vertex));
            checkRep();
            return false;
        }
    }

    @Override public int set(L source, L target, int weight) {
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

    @Override public boolean remove(L vertex) {
        try {
            vertices.remove(findVertex(vertex));
            for (Vertex<L> v : vertices) {
                v.set(vertex, 0);
            }
            checkRep();
            return true;
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override public Set<L> vertices() {
        Set<L> result = new HashSet<>();
        for (Vertex<L> vertex : vertices) {
            result.add(vertex.getLabel());
            for (L target : vertex.getTargets().keySet()) {
                result.add(target);
            }
        }
        return result;
    }

    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> result = new HashMap<>();
        for (Vertex<L> vertex : vertices) {
            Map<L, Integer> targets = vertex.getTargets();
            if (targets.keySet().contains(target)) {
                result.put(vertex.getLabel(), targets.get(target));
            }
        }
        return result;
    }

    @Override public Map<L, Integer> targets(L source) {
        try {
            return findVertex(source).getTargets();
        }
        catch (NoSuchElementException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * @return a list of strings in the form "(source -> target, weight)",
     *         representing the edges in the graph
     */
    @Override public String toString() {
        String result = "";
        for (Vertex<L> vertex : vertices) {
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
class Vertex<L> {
    
    private final L label;
    private final Map<L, Integer> targets = new HashMap<>();
    
    // Abstraction function:
    //   represents a set of edges from label to targets with weights
    // Representation invariant:
    //   weights in targets are positive integers
    // Safety from rep exposure:
    //   L is immutable, and is declared as private final
    //   Map<L, Integer> is copied when returned, and is declared as
    //     private final
    
    /**
     * Make a vertex with empty outgoing edge targets.
     * 
     * @param label label of the vertex
     */
    public Vertex(L label) {
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
    public L getLabel() {
        return label;
    }

    /**
     * Get the outgoing edge targets and weights from the vertex.
     * 
     * @return a copy of the map containing outgoing edge targets and weights
     */
    public Map<L, Integer> getTargets() {
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
    public int set(L target, int weight) {
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
        Map<L, Integer> targets = this.getTargets();
        for (L target : targets.keySet()) {
            result = result.concat(
                "(" + this.getLabel().toString()
                + " -> " + target.toString()
                + ", " + targets.get(target)
                + ")\n"
            );
        }
        return result.strip();
    }

}
