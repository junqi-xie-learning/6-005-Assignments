/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   graph represents the generated word affinity graph
    // Representation invariant:
    //   graph isn't modified by any functions
    // Safety from rep exposure:
    //   Graph<String> is never returned or modified, and is declared as private final
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        Scanner scanner = new Scanner(corpus);
        if (scanner.hasNext()) {
            String current = scanner.next().toLowerCase();
            graph.add(current);
            while (scanner.hasNext()) {
                String next = scanner.next().toLowerCase();
                int original = graph.set(current, next, 1);
                if (original != 0) {
                    graph.set(current, next, original + 1);
                }
                current = next;
            }
        }
        scanner.close();
    }

    /**
     * Get the generated word affinity graph
     * 
     * @return a copy of the generated word affinity graph
     */
    public Graph<String> getGraph() {
        Graph<String> result = Graph.empty();
        for (String vertex : graph.vertices()) {
            result.add(vertex);
            Map<String, Integer> targets = graph.targets(vertex);
            for (String target : targets.keySet()) {
                result.set(vertex, target, targets.get(target));
            }
        }
        return result;
    }

    /**
     * Try to find a bridge word in the word affinity graph
     * 
     * @param current current word in the input poem
     * @param next next word in the input poem
     * @return the bridge word if found, empty string if not found
     * @throws NoSuchElementException if not found
     */
    private String findBridgeWord(String current, String next) throws NoSuchElementException {
        current = current.toLowerCase();
        next = next.toLowerCase();
        Set<String> targets = graph.targets(current).keySet();
        for (String middle : targets) {
            if (graph.targets(middle).keySet().contains(next)) {
                return middle;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        Scanner scanner = new Scanner(input);
        List<String> words = new ArrayList<>();
        if (scanner.hasNext()) {
            String current = scanner.next();
            words.add(current);
            while (scanner.hasNext()) {
                String next = scanner.next();
                try {
                    words.add(findBridgeWord(current, next));
                }
                catch (NoSuchElementException e) { }
                words.add(next);
                current = next;
            }
        }
        scanner.close();
        return String.join(" ", words);
    }

    /**
     * @return string representation of the word affinity graph
     */
    @Override public String toString() {
        return this.getGraph().toString();
    }

}
