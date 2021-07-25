/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.IOException;

import graph.Graph;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    //   constructor
    //     corpus: exists, doesn't exist
    //     words in contents: 0, 1, >1
    //     words contain duplicates or don't
    //   poem
    //     words in input: 0, 1, >1
    //     input word pairs in graph or not
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGraphPoetNotFound() {
        try {
            File corpus = new File("test/poet/foo.txt");
            GraphPoet poet = new GraphPoet(corpus);
            assert false; // should not reach here
        }
        catch (IOException e) {
            assert true;
        }
    }

    @Test
    public void testGraphPoetEmpty() {
        try {
            File corpus = new File("test/poet/empty.txt");
            GraphPoet poet = new GraphPoet(corpus);
            Graph<String> graph = poet.getGraph();
            assertEquals("expected empty graph",
                    Collections.emptySet(), graph.vertices());
        }
        catch (IOException e) {
            assert false; // should not reach here
        }
    }

    @Test
    public void testGraphPoetSingle() {
        try {
            File corpus = new File("test/poet/single.txt");
            GraphPoet poet = new GraphPoet(corpus);
            Graph<String> graph = poet.getGraph();
            assertEquals("expected single vertex in the graph",
                    Set.of("hello"), graph.vertices());
            assertEquals("expected no edges from vertex",
                    Collections.emptyMap(), graph.targets("hello"));
        }
        catch (IOException e) {
            assert false; // should not reach here
        }
    }

    @Test
    public void testGraphPoetMultiple() {
        try {
            File corpus = new File("test/poet/multiple.txt");
            GraphPoet poet = new GraphPoet(corpus);
            Graph<String> graph = poet.getGraph();
            assertEquals("expected 2 vertices in the graph",
                    Set.of("hello,", "goodbye!"), graph.vertices());
            assertEquals("expected edges from \"hello,\"",
                    Map.of("hello,", 2, "goodbye!", 1), graph.targets("hello,"));
            assertEquals("expected no edges from \"goodbye!\"",
                    Collections.emptyMap(), graph.targets("goodbye!"));
        }
        catch (IOException e) {
            assert false; // should not reach here
        }
    }

    @Test
    public void testGraphPoetPoemEmpty() {
        try {
            File corpus = new File("test/poet/where-no-man-has-gone-before.txt");
            GraphPoet poet = new GraphPoet(corpus);
            String input = "";
            assertEquals("expected empty poem",
                    input, poet.poem(input));
        }
        catch (IOException e) {
            assert false; // should not reach here
        }
    }

    @Test
    public void testGraphPoetPoemSingle() {
        try {
            File corpus = new File("test/poet/where-no-man-has-gone-before.txt");
            GraphPoet poet = new GraphPoet(corpus);
            String input = "hello";
            assertEquals("expected identical poem",
                    input, poet.poem(input));
        }
        catch (IOException e) {
            assert false; // should not reach here
        }
    }

    @Test
    public void testGraphPoetPoemMultiple() {
        try {
            File corpus = new File("test/poet/where-no-man-has-gone-before.txt");
            GraphPoet poet = new GraphPoet(corpus);
            String input = "Seek to explore new and exciting synergies!";
            assertEquals("expected poem",
                    "Seek to explore strange new life and exciting synergies!", poet.poem(input));
        }
        catch (IOException e) {
            assert false; // should not reach here
        }
    }

    @Test
    public void testGraphPoetToString() {
        try {
            File corpus = new File("test/poet/multiple.txt");
            GraphPoet poet = new GraphPoet(corpus);
            assertEquals("expected graph with multiple edges to string",
            "(hello, -> hello,, 2)\n(hello, -> goodbye!, 1)", poet.toString());
        }
        catch (IOException e) {
            assert false; // should not reach here
        }
    }

}
