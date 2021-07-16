/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.size() == 0) {
            return new Timespan(Instant.MIN, Instant.MIN);
        }

        Instant start = Instant.MAX, end = Instant.MIN;
        for (Tweet tweet : tweets) {
            Instant timestamp = tweet.getTimestamp();
            start = timestamp.isBefore(start) ? timestamp : start;
            end = timestamp.isAfter(end) ? timestamp : end;
        }
        return new Timespan(start, end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> result = new HashSet<>();
        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            for (int i = text.indexOf('@'); i != -1; i = text.indexOf('@', i + 1)) {
                if ((i == 0 || !isValidChar(text.charAt(i - 1))) &&
                    (i != text.length() - 1 || isValidChar(text.charAt(i + 1)))) {
                    String username = getUsernameMention(text, i);
                    result.add(username.toLowerCase());
                }
            }
        }
        return result;
    }

    /**
     * @param ch
     * @return The letter can be part of a twitter username.
     *         A Twitter username is a nonempty sequence of letters (A-Z or
     *         a-z), digits, underscore ("_"), or hyphen ("-").
     */
    private static boolean isValidChar(char ch) {
        return 'A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' ||
            '0' <= ch && ch <= '9' || ch == '-' || ch == '_';
    }

    /**
     * @param text
     *            text of a tweets, not modified by this method.
     * @param start
     *            starting position of a username-mention.
     * @return The username-mention starting at pos.
     */
    private static String getUsernameMention(String text, int start) {
        StringBuilder builder = new StringBuilder("@");
        for (int i = start + 1; i < text.length() && isValidChar(text.charAt(i)); i++) {
            builder.append(text.charAt(i));
        }
        return builder.toString();
    }

}
