/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    //
    // Testing strategy:
    //
    // Partition for writtenBy(tweets, username) -> result:
    //
    //   tweets.size: 0, >0
    //   result.size: 0, 1, >1
    //   the usernames are exactly the same or differ in cases
    //
    // Partition for inTimespan(tweets, timespan) -> result:
    //
    //   tweets.size: 0, >0
    //   result.size: 0, 1, >1
    //   the start of timespan equals its end or doesn't
    //
    // Partition for containing(tweets, words) -> result:
    //
    //   tweets.size: 0, >0
    //   words.size: 0, 1, >1
    //   result.size: 0, 1, >1
    //   tweets contains exactly one or multiple of the words
    //   the words are exactly the same or differ in cases
    //
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "A portrait in the sum room.", d2);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testWrittenByNoTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "alyssa");
        
        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByMultipleTweetsNoResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "mit");
        
        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleTweetsMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        
        assertFalse("expected non-empty list", writtenBy.isEmpty());
        assertTrue("expected list to contain tweets", writtenBy.containsAll(Arrays.asList(tweet1, tweet3)));
        assertEquals("expected same order", 0, writtenBy.indexOf(tweet1));
    }

    @Test
    public void testInTimespanNoTweets() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(), new Timespan(Instant.MIN, Instant.MAX));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanMultipleTweetsNoResults() {
        Instant testStart = Instant.parse("2016-02-17T08:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T09:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanMultipleTweetsSingleResult() {
        Instant testStart = Instant.parse("2016-02-17T10:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T10:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertEquals("expected singleton list", 1, inTimespan.size());
        assertTrue("expected list to contain tweet", inTimespan.contains(tweet1));
    }

    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    @Test
    public void testContainingNoTweets() {
        List<Tweet> containing = Filter.containing(Arrays.asList(), Arrays.asList("talk"));
        
        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingNoWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1), Arrays.asList());
        
        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingMultipleTweetsNoResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("reason"));
        
        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingMultipleTweetsSingleResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("reasonable"));
        
        assertEquals("expected singleton list", 1, containing.size());
        assertTrue("expected list to contain tweet", containing.contains(tweet1));
    }

    @Test
    public void testContainingMultipleTweetsSingleWord() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    @Test
    public void testContainingMultipleTweetsMultipleWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("rivest", "Hype"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
