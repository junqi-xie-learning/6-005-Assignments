/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.List;
import java.util.ArrayList;
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
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   vertices represents the vertices in the graph
    //   edges represents the edges in the graph, from source vertex to target
    //     vertex with weight
    // Representation invariant:
    //   vertices contains all the vertices in edges
    //   edges don't contain edges from the same source to the same target
    // Safety from rep exposure:
    //   L and int are immutable
    //   Set<L> and List<Edge<L>> are never returned, and are declared as private final
    
    /**
     * Check the rep invariant.
     */
    private void checkRep() {
        Map<L, Set<L>> relations = new HashMap<>();
        for (Edge<L> edge : edges) {
            L source = edge.getSource(), target = edge.getTarget();
            assert vertices.contains(source) == true;
            assert vertices.contains(target) == true;
            relations.putIfAbsent(source, new HashSet<>());
            assert relations.get(source).contains(target) == false;
            relations.get(source).add(target);
        }
    }

    @Override public boolean add(L vertex) {
        boolean included = !vertices.add(vertex);
        checkRep();
        return included;
    }

    /**
     * Find the edge from source vertex to target vertex in edges.
     * 
     * @param source label of the source vertex
     * @param target label of the target vertex
     * @return the edge from source vertex to target vertex
     * @throws NoSuchElementException if not found
     */
    private Edge<L> findEdge(L source, L target) throws NoSuchElementException {
        for (Edge<L> edge : edges) {
            if (source.equals(edge.getSource()) && target.equals(edge.getTarget())) {
                return edge;
            }
        }
        throw new NoSuchElementException();
    }

    @Override public int set(L source, L target, int weight) {
        assert weight >= 0;
        if (weight > 0) {
            vertices.add(source);
            vertices.add(target);
        }

        int original;
        try {
            Edge<L> edge = findEdge(source, target);
            original = edge.getWeight();
            edges.remove(edge);
        }
        catch (NoSuchElementException e) {
            original = 0;
        }
        if (weight > 0) {
            edges.add(new Edge<L>(source, target, weight));
        }
        
        checkRep();
        return original;
    }

    /**
     * Find the edge from or to vertex in edges.
     * 
     * @param vertex label of the vertex
     * @return the set of edges from or to vertex
     */
    private Set<Edge<L>> findVertexEdges(L vertex) {
        Set<Edge<L>> result = new HashSet<>();
        for (Edge<L> edge : edges) {
            if (vertex.equals(edge.getSource()) || vertex.equals(edge.getTarget())) {
                result.add(edge);
            }
        }
        return result;
    }

    @Override public boolean remove(L vertex) {
        boolean included = vertices.remove(vertex);
        edges.removeAll(findVertexEdges(vertex));
        checkRep();
        return included;
    }

    @Override public Set<L> vertices() {
        return vertices;
    }

    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> result = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (target.equals(edge.getTarget())) {
                result.put(edge.getSource(), edge.getWeight());
            }
        }
        return result;
    }

    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> result = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (source.equals(edge.getSource())) {
                result.put(edge.getTarget(), edge.getWeight());
            }
        }
        return result;
    }

    /**
     * @return a list of strings in the form "(source -> target, weight)",
     *         representing the edges in the graph
     */
    @Override public String toString() {
        String result = "";
        for (Edge<L> edge : edges) {
            result = result.concat(edge.toString() + "\n");
        }
        return result.strip();
    }

}

/**
 * Edge represents an immutable edge in a graph with source, target and weight.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {
    
    private final L source, target;
    private final int weight;
    
    // Abstraction function:
    //   represents the edge from source vertex to target vertex with weight
    // Representation invariant:
    //   weight is a positive integer
    // Safety from rep exposure:
    //   L and int are immutable, and are declared as private final
    
    /**
     * Make an edge.
     * 
     * @param source label of the source vertex
     * @param target label of the target vertex
     * @param weight positive weight of the edge
     * @return an edge from source vertex to target vertex with weight
     */
    public Edge(L source, L target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    /**
     * Check the rep invariant.
     */
    private void checkRep() {
        assert source != null;
        assert target != null;
        assert weight > 0;
    }

    /**
     * Get the label of the source vertex.
     * 
     * @return source label of type L
     */
    public L getSource() {
        return source;
    }

    /**
     * Get the label of the target vertex.
     * 
     * @return target label of type L
     */
    public L getTarget() {
        return target;
    }

    /**
     * Get the weight of the edge.
     * 
     * @return weight as a positive int
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return a string in the form "(source -> target, weight)"
     */
    @Override public String toString() {
        return "(" + this.getSource().toString()
                + " -> " + this.getTarget().toString()
                + ", " + this.getWeight()
                + ")";
    }

}
