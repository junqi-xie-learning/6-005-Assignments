package abc.player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abc.sound.Music;
import abc.sound.Together;

/**
 * Main entry point of your application.
 */
public class Main {

    private static final int DEFAULT_TICKS_PER_BEAT = 96;

    /**
     * Parse string representation of an rational.
     * 
     * @param rational string representation of an rational
     * @return the value of the rational
     */
    private static double parseRational(String rational) {
        Matcher m = Pattern.compile("(\\d+)/(\\d+)").matcher(rational);
        if (m.find()) {
            double numerator = Integer.parseInt(m.group(1));
            double denominator = Integer.parseInt(m.group(2));
            return numerator / denominator;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Parse string representation of a tempo notation.
     * 
     * @param tempo string representation of a tempo notation
     * @param defaultNote string representation of the default note duration
     * @return beats per minute represented in default notes
     */
    private static int parseTempo(String tempo, String defaultNote) {
        Matcher m = Pattern.compile("(\\d+/\\d+)=(\\d+)").matcher(tempo);
        if (m.matches()) {
            double defaultDuration = parseRational(defaultNote);
            double notationDuration = parseRational(m.group(1));
            double notationTempo = Integer.parseInt(m.group(2));
            return (int)(notationTempo * notationDuration / defaultDuration);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Parse the header of an abc file.
     * 
     * @param reader buffered reader containing the header of an abc file
     * @return information of the abc file (with defaults added)
     * @throws IOException if the abc file is invalid
     */
    private static Map<String, String> parseHeader(BufferedReader reader) throws IOException {
        Map<String, String> info = new HashMap<>();

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            Matcher m = Pattern.compile("([A-Z]):\\s?([^%]+)").matcher(line);
            if (m.find()) {
                info.put(m.group(1), m.group(2).strip());
                if (m.group(1).equals("K")) {
                    break;
                }
            }
            else {
                throw new IOException("Invalid abc file header");
            }
        }

        // Add default values for header
        info.putIfAbsent("C", "Unknown");
        info.putIfAbsent("M", "4/4");
        switch (info.get("M")) {
            case "C":
                info.put("M", "4/4");
                break;
            case "C|":
                info.put("M", "2/2");
                break;
        }
        info.putIfAbsent("L", parseRational(info.get("M")) < 0.75 ? "1/16" : "1/8");
        info.putIfAbsent("Q", String.join("=", Arrays.asList(info.get("L"), "100")));
        return info;
    }

    /**
     * Parse the body of an abc file with a single voice.
     * 
     * @param reader buffered reader containing the body of an abc file
     * @return abc notation of the voice
     * @throws IOException if the abc file is invalid
     */
    private static String parseSingle(BufferedReader reader) throws IOException {
        List<String> notations = new ArrayList<>();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            Matcher m = Pattern.compile("([^%]+)").matcher(line);
            if (m.find()) {
                notations.add(m.group(1));
            }
        }
        return String.join(" ", notations);
    }

    /**
     * Parse the body of an abc file with multiple voices.
     * 
     * @param reader buffered reader containing the body of an abc file
     * @return abc notations of different voices
     * @throws IOException if the abc file is invalid
     */
    private static Map<String, String> parseMultiple(BufferedReader reader) throws IOException {
        Map<String, List<String>> notations = new HashMap<>();

        String label = "";
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            Matcher m = Pattern.compile("V:\\s?([^%]+)").matcher(line);
            if (m.matches()) {
                label = m.group(1).strip();
                notations.putIfAbsent(label, new ArrayList<>());
            }
            else {
                m = Pattern.compile("([^%]+)").matcher(line);
                if (m.find()) {
                    notations.get(label).add(line);
                }
            }
        }

        Map<String, String> result = new HashMap<>();
        for (String key : notations.keySet()) {
            result.put(key, String.join(" ", notations.get(key)));
        }
        return result;
    }

    /**
     * Plays the input file using Java MIDI API and displays
     * header information to the standard output stream.
     * 
     * (Your code should not exit the application abnormally using
     * System.exit().)
     * 
     * @param file the name of input abc file
     */
    public static void play(String file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        // Parse and output the header
        Map<String, String> info = parseHeader(reader);
        System.out.println("Index number: " + info.get("X"));
        System.out.println("Title: " + info.get("T"));
        System.out.println("Composer: " + info.get("C"));
        System.out.println("Meter: " + info.get("M"));
        System.out.println("Tempo: " + info.get("Q"));
        System.out.println("Key: " + info.get("K"));

        // Parse the body
        Music music = Music.empty();
        if (!info.containsKey("V")) {
            music = Music.parse(parseSingle(reader), info.get("K"), DEFAULT_TICKS_PER_BEAT);
        }
        else {
            Map<String, String> notations = parseMultiple(reader);
            for (String notation : notations.values()) {
                Music single = Music.parse(notation, info.get("K"), DEFAULT_TICKS_PER_BEAT);
                music = new Together(single, music);
            }
        }

        // Play the music
        int beatsPerMinute = parseTempo(info.get("Q"), info.get("L"));
        MusicPlayer.play(music, beatsPerMinute, DEFAULT_TICKS_PER_BEAT);
        reader.close();
    }

    public static void main(String[] args) {
        String file = args[0];
        try {
            play(file);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        // System.exit(0);
    }
}
