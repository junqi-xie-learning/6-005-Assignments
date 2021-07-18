/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class ExtractTest {

    //
    // Testing strategy:
    //
    // Partition for getTimespan(tweets) -> result:
    //
    //   tweets.size: 0, 1, >1
    //   tweets contains repeated timestamps or doesn't
    //   tweets comes in time order or doesn't
    //
    // Partition for getMentionedUsers(tweets) -> result:
    //
    //   tweets contains username-mentions or doesn't
    //   tweets contains username-mentions preceded by valid characters or doesn't
    //   tweets contains repeated username-mentions or doesn't
    //   the username-mentions comes in the beginning, the end or the middle
    //
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T10:30:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "MITOCW", "@MITopenlearning component, free lecture notes, exams, and videos from @MIT.", d1);
    private static final Tweet tweet4 = new Tweet(4, "mitopenlearning", "@mit component, transforming teaching and learning at @mit, home of @mitocw", d2);
    private static final Tweet tweet5 = new Tweet(5, "mit6005", "an email address like bitdiddle@mit.edu does NOT contain a mention", d3);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testGetTimespanNoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());

        // any results without exceptions are acceptable
    }

    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanRepeated() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet4));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanOutOfOrder() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1, tweet5));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersPrecedingValid() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
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
    public void testGetMentionedUsersNoRepeated() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        Set<String> expected = Set.of("mit", "mitopenlearning");
        
        assertTrue("expected results", expected.equals(toLowerCase(mentionedUsers)));
    }

    @Test
    public void testGetMentionedUsersRepeated() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        Set<String> expected = Set.of("mit", "mitocw");

        assertTrue("expected results", expected.equals(toLowerCase(mentionedUsers)));
    }

    @Test
    public void testGetMentionedUsersCaseInsensitive() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));
        Set<String> expected = Set.of("mit", "mitopenlearning", "mitocw");

        assertTrue("expected results", expected.equals(toLowerCase(mentionedUsers)));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
