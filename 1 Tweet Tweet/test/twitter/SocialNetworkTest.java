/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class SocialNetworkTest {

    //
    // Testing strategy:
    //
    // Partition for guessFollowsGraph(tweets) -> result:
    //
    //   tweets.size: 0, >0
    //   tweets contains self-mentions or doesn't
    //   tweets contains repeated username-mentions or doesn't
    //   tweets contains repeated authors or doesn't
    //
    // Partition for influencers(followsGraph) -> result:
    //
    //   followsGraph.size: 0, 1, >1
    //   followsGraph contains follows of the same length or doesn't
    //

    private static final Instant d = Instant.parse("2016-02-17T10:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "MITOCW", "@MITopenlearning component, free lecture notes, exams, and videos from @MIT.", d);
    private static final Tweet tweet2 = new Tweet(2, "mitopenlearning", "@mit component, transforming teaching and learning at @mit, home of @mitocw", d);
    private static final Tweet tweet3 = new Tweet(3, "mitocw", "Interactive online courses from @MITOCW, delivered to you via @MITxonedX." , d);
    private static final Tweet tweet4 = new Tweet(4, "mit6005", "an email address like bitdiddle@mit.edu does NOT contain a mention", d);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    /**
     * Find key in graph, ignoring cases.
     * 
     * @param graph
     *            graph represented in adjacency lists, not modified by this method.
     * @param key
     *            key of exactly one node in the graph
     * @return a set of adjacent nodes in the graph.
     */
    private static Set<String> getIgnoreCase(Map<String, Set<String>> graph, String key) {
        for (String node : graph.keySet()) {
            if (node.equalsIgnoreCase(key)) {
                return graph.get(node);
            }
        }
        return new HashSet<>();
    }

    /**
     * Transform the strings in the set into lower cases.
     * 
     * @param strings
     *            set of strings, not modified by this method.
     * @return a set of strings transformed into lower cases.
     */
    private static Set<String> toLowerCase(Set<String> strings) {
        Set<String> result = new HashSet<>();
        for (String string : strings) {
            result.add(string.toLowerCase());
        }
        return result;
    }

    @Test
    public void testGuessFollowsNoMentions() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet4));
        Set<String> expectedUsernames = Set.of("mit6005");
        
        if (!followsGraph.isEmpty()) {
            assertTrue("expected empty following", getIgnoreCase(followsGraph, "mit6005").isEmpty());
            assertTrue("expected usernames", expectedUsernames.equals(toLowerCase(followsGraph.keySet())));
        }
    }

    @Test
    public void testGuessFollowsGraphNoRepeats() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        Set<String> expectedFollowing = Set.of("mit", "mitopenlearning");
        Set<String> expectedUsernames = Set.of("mit", "mitocw", "mitopenlearning");
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertTrue("expected following", expectedFollowing.equals(toLowerCase(getIgnoreCase(followsGraph, "mitocw"))));
        assertTrue("expected usernames", expectedUsernames.containsAll(toLowerCase(followsGraph.keySet())));
        assertEquals("expected no repeats", followsGraph.keySet().size(), toLowerCase(followsGraph.keySet()).size());
    }

    @Test
    public void testGuessFollowsGraphRepeatedMentions() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2));
        Set<String> expectedFollowing = Set.of("mit", "mitocw");
        Set<String> expectedUsernames = Set.of("mit", "mitocw", "mitopenlearning");
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertTrue("expected following", expectedFollowing.equals(toLowerCase(getIgnoreCase(followsGraph, "mitopenlearning"))));
        assertTrue("expected usernames", expectedUsernames.containsAll(toLowerCase(followsGraph.keySet())));
        assertEquals("expected no repeats", followsGraph.keySet().size(), toLowerCase(followsGraph.keySet()).size());
    }

    @Test
    public void testGuessFollowsGraphRepeatedAuthors() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet3));
        Set<String> expectedFollowing = Set.of("mit", "mitopenlearning", "mitxonedx");
        Set<String> expectedUsernames = Set.of("mit", "mitocw", "mitopenlearning", "mitxonedx");
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertTrue("expected following", expectedFollowing.equals(toLowerCase(getIgnoreCase(followsGraph, "mitocw"))));
        assertTrue("expected usernames", expectedUsernames.containsAll(toLowerCase(followsGraph.keySet())));
        assertEquals("expected no repeats", followsGraph.keySet().size(), toLowerCase(followsGraph.keySet()).size());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    /**
     * Transform the strings in the list into lower cases.
     * 
     * @param strings
     *            list of strings, not modified by this method.
     * @return a list of strings transformed into lower cases.
     */
    private static List<String> toLowerCase(List<String> strings) {
        List<String> result = new ArrayList<>();
        for (String string : strings) {
            result.add(string.toLowerCase());
        }
        return result;
    }

    @Test
    public void testInfluenceSingleUsername() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("MITOCW", Set.of("MIT"));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        List<String> expected = Arrays.asList("mit", "mitocw");
        
        assertTrue("expected list", expected.equals(toLowerCase(influencers)));
    }

    @Test
    public void testInfluenceMultipleUsernamesNoRepeated() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("MITOCW", Set.of("MIT"));
        followsGraph.put("mitopenlearning", Set.of("mit", "mitocw"));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        List<String> expected = Arrays.asList("mit", "mitocw", "mitopenlearning");
        
        assertTrue("expected list", expected.equals(toLowerCase(influencers)));
    }

    @Test
    public void testInfluenceMultipleUsernamesRepeated() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("MIT", Set.of("MITOCW", "MITopenlearning"));
        followsGraph.put("MITOCW", Set.of("MIT", "MITopenlearning"));
        followsGraph.put("mitopenlearning", Set.of("mit", "mitocw"));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        List<String> expected = Arrays.asList("mit", "mitocw", "mitopenlearning");
        
        assertTrue("expected list to contain usernames", influencers.containsAll(expected));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
