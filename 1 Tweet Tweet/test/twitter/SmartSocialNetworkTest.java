package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class SmartSocialNetworkTest {

    //
    // Testing strategy:
    //
    // Partition for getHashtags(tweets) -> result:
    //
    //   tweets contains hashtags or doesn't
    //   tweets contains hashtags preceded by valid characters or doesn't
    //   tweets contains repeated hashtags or doesn't
    //   the hashtags comes in the beginning, the end or the middle
    //
    // Partition for guessFollowsGraphHashtag(tweets) -> result:
    //
    //   tweets.size: 0, >0
    //   tweets contains self-mentions or doesn't
    //   tweets contains repeated hashtags or doesn't
    //   tweets contains repeated authors or doesn't
    //

    private static final Instant d = Instant.parse("2016-02-17T10:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about #rivest so much?", d);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "#Rivest talk in 30 minutes #hype", d);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "A portrait in the sum room.", d);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testGetHashtagsNoHashtag() {
        Set<String> hashtags = SmartSocialNetwork.getHashtags(Arrays.asList(tweet3));
        
        assertTrue("expected empty set", hashtags.isEmpty());
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
    public void testGetHashtagsNoRepeated() {
        Set<String> hashtags = SmartSocialNetwork.getHashtags(Arrays.asList(tweet2));
        Set<String> expected = Set.of("#rivest", "#hype");
        
        assertTrue("expected results", expected.equals(toLowerCase(hashtags)));
    }

    @Test
    public void testGetHashtagsRepeated() {
        Set<String> hashtags = SmartSocialNetwork.getHashtags(Arrays.asList(tweet1, tweet2));
        Set<String> expected = Set.of("#rivest", "#hype");

        assertTrue("expected results", expected.equals(toLowerCase(hashtags)));
    }

    @Test
    public void testGuessFollowsGraphHashtagEmpty() {
        Map<String, Set<String>> followsGraphHashtag = SmartSocialNetwork.guessFollowsGraphHashtag(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraphHashtag.isEmpty());
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

    @Test
    public void testGuessFollowsNoMentions() {
        Map<String, Set<String>> followsGraphHashtag = SmartSocialNetwork.guessFollowsGraphHashtag(Arrays.asList(tweet3));
        Set<String> expectedUsernames = Set.of("alyssa");
        
        if (!followsGraphHashtag.isEmpty()) {
            assertTrue("expected empty following", getIgnoreCase(followsGraphHashtag, "alyssa").isEmpty());
            assertTrue("expected usernames", expectedUsernames.equals(toLowerCase(followsGraphHashtag.keySet())));
        }
    }

    @Test
    public void testGuessFollowsGraphHashtagNoRepeats() {
        Map<String, Set<String>> followsGraphHashtag = SmartSocialNetwork.guessFollowsGraphHashtag(Arrays.asList(tweet1, tweet2));
        Set<String> expectedFollowing = Set.of("bbitdiddle");
        Set<String> expectedUsernames = Set.of("alyssa", "bbitdiddle");
        
        assertFalse("expected non-empty graph", followsGraphHashtag.isEmpty());
        assertTrue("expected following", expectedFollowing.equals(toLowerCase(getIgnoreCase(followsGraphHashtag, "alyssa"))));
        assertTrue("expected usernames", expectedUsernames.containsAll(toLowerCase(followsGraphHashtag.keySet())));
        assertEquals("expected no repeats", followsGraphHashtag.keySet().size(), toLowerCase(followsGraphHashtag.keySet()).size());
    }

    @Test
    public void testGuessFollowsGraphHashtagRepeatedAuthors() {
        Map<String, Set<String>> followsGraphHashtag = SmartSocialNetwork.guessFollowsGraphHashtag(Arrays.asList(tweet1, tweet2, tweet3));
        Set<String> expectedFollowing = Set.of("bbitdiddle");
        Set<String> expectedUsernames = Set.of("alyssa", "bbitdiddle");
        
        assertFalse("expected non-empty graph", followsGraphHashtag.isEmpty());
        assertTrue("expected following", expectedFollowing.equals(toLowerCase(getIgnoreCase(followsGraphHashtag, "alyssa"))));
        assertTrue("expected usernames", expectedUsernames.containsAll(toLowerCase(followsGraphHashtag.keySet())));
        assertEquals("expected no repeats", followsGraphHashtag.keySet().size(), toLowerCase(followsGraphHashtag.keySet()).size());
    }

}
