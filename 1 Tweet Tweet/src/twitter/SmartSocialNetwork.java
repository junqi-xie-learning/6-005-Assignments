package twitter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * SmartSocialNetwork extends methods in SocialNetwork that operate on a social network.
 */
public class SmartSocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet.
     *         Another kind of evidence that Ernie follows Bert is if Ernie and
     *         Bert both mentions #mit in their tweets.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> mentionsGraph = SmartSocialNetwork.guessFollowsGraphMentions(tweets);
        Map<String, Set<String>> hashtagGraph = SmartSocialNetwork.guessFollowsGraphHashtag(tweets);

        for (String username : hashtagGraph.keySet()) {
            mentionsGraph.putIfAbsent(username, new HashSet<>());
            mentionsGraph.get(username).addAll(hashtagGraph.get(username));
        }
        return mentionsGraph;
    }

    public static Map<String, Set<String>> guessFollowsGraphMentions(List<Tweet> tweets) {
        return SocialNetwork.guessFollowsGraph(tweets);
    }

    public static Map<String, Set<String>> guessFollowsGraphHashtag(List<Tweet> tweets) {
        Map<String, Set<String>> result = new HashMap<>();
        Set<String> hashtags = getHashtags(tweets);
        for (String hashtag : hashtags) {
            List<Tweet> containing = Filter.containing(tweets, Arrays.asList(hashtag));
            for (Tweet tweet : containing) {
                String author = tweet.getAuthor().toLowerCase();
                for (Tweet otherTweet : containing) {
                    String otherAuthor = otherTweet.getAuthor().toLowerCase();
                    if (!author.equals(otherAuthor)) {
                        result.putIfAbsent(author, new HashSet<>());
                        result.get(author).add(otherAuthor);
                        result.putIfAbsent(otherAuthor, new HashSet<>());
                        result.get(otherAuthor).add(author);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Get hashtags in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of hashtags in the text of the tweets.
     *         A hashtag is "#" followed by a Twitter identifier (as defined by
     *         Tweet.getAuthor()'s spec).
     *         The hashtag cannot be immediately preceded or followed by any
     *         character valid in a Twitter identifier.
     *         Twitter hashtags are case-insensitive, and the returned set may
     *         include a hashtag at most once.
     */
    public static Set<String> getHashtags(List<Tweet> tweets) {
        Set<String> result = new HashSet<>();
        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            for (int i = text.indexOf('#'); i != -1; i = text.indexOf('#', i + 1)) {
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
        StringBuilder builder = new StringBuilder("#");
        for (int i = start + 1; i < text.length() && isValidChar(text.charAt(i)); i++) {
            builder.append(text.charAt(i));
        }
        return builder.toString();
    }

}
