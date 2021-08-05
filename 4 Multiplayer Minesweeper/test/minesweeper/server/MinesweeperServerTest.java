/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import org.junit.Test;

/**
 * Module Test for Minesweeper Server
 */
public class MinesweeperServerTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static final int PORT = 4000 + new Random().nextInt(1 << 15);
    private static final String PORT_STRING = Integer.toString(PORT);

    private static final int MAX_CONNECTION_ATTEMPTS = 20;

    private static final String BOARDS_PKG = "minesweeper/boards/";

    /**
     * Return the absolute path of the specified file resource on the classpath in BOARDS_PKG.
     * 
     * @throws IOException if a valid path to an existing file cannot be returned
     * @see autograder.PublishedTest#startMinesweeperServer(String)
     */
    private static String getResourcePath(String filename) throws IOException {
        final URL url = ClassLoader.getSystemClassLoader().getResource(BOARDS_PKG + filename);
        if (url == null) {
            throw new IOException("Failed to locate resource " + filename);
        }
        final File file;
        try {
            file = new File(url.toURI());
        }
        catch (URISyntaxException urise) {
            throw new IOException("Invalid URL " + url, urise);
        }
        final String path = file.getAbsolutePath();
        if (!file.exists()) {
            throw new IOException("File " + path + " does not exist");
        }
        return path;
    }

    static ThreadWithObituary startServer() {
        return startServer("--port", PORT_STRING);
    }

    static ThreadWithObituary startServer(boolean debug) {
        return startServer(debug ? "--debug" : "--no-debug", "--port", PORT_STRING);
    }

    static ThreadWithObituary startServer(boolean debug, int size) {
        return startServer(debug ? "--debug" : "--no-debug", "--port", PORT_STRING, "--size",
                Integer.toString(size) + "," + Integer.toString(size));
    }

    static ThreadWithObituary startServer(boolean debug, int sizeX, int sizeY) {
        return startServer(debug ? "--debug" : "--no-debug", "--port", PORT_STRING, "--size",
                Integer.toString(sizeX) + "," + Integer.toString(sizeY));
    }

    /**
     * Start a MinesweeperServer with the given debug mode and board file.
     */
    static ThreadWithObituary startServer(boolean debug, String boardFile) throws IOException {
        final String boardPath = getResourcePath(boardFile);
        return startServer(debug ? "--debug" : "--no-debug", "--port", PORT_STRING, "--file", boardPath);
    }

    /**
     * Start a MinesweeperServer with the given command-line arguments.
     */
    static ThreadWithObituary startServer(final String... args) {
        return new ThreadWithObituary(() -> MinesweeperServer.main(args));
    }

    /**
     * Connect to a MinesweeperServer and return the connected socket.
     * 
     * @param server if not null, abort connection attempts if that thread dies
     * @see autograder.PublishedTest#connectToMinesweeperServer(Thread)
     */
    static Socket connect(ThreadWithObituary server) throws IOException {
        int attempts = 0;
        while (true) {
            try {
                Socket socket = new Socket(LOCALHOST, PORT);
                socket.setSoTimeout(3000);
                return socket;
            }
            catch (ConnectException ce) {
                if (server != null && !server.thread().isAlive()) {
                    throw new IOException("Server thread not running", server.error());
                }
                if (++attempts > MAX_CONNECTION_ATTEMPTS) {
                    throw new IOException("Exceeded max connection attempts", ce);
                }
                try {
                    Thread.sleep(attempts * 10);
                }
                catch (InterruptedException ie) { }
            }
        }
    }

    /**
     * Read and ignore several lines from a buffered reader.
     * 
     * @param in buffered reader to read from
     * @param length number of lines to read
     */
    public static void readLines(BufferedReader in, int length) throws IOException {
        for (int i = 0; i < length; i++) {
            in.readLine();
        }
    }

    public static void assertReadLines(BufferedReader in, String[] expected) throws IOException {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], in.readLine());
        }
    }

    @Test(timeout = 20000)
    public void testServer() throws IOException, InterruptedException {
        final int THREADS = 3;

        String expected[] = new String[] {
            "- F - - - F",
            "- - - F - -",
            "- F - - - -",
            "2 3 3 3 3 2",
            "1 1        ",
            "- 1        "
        };
        
        ThreadWithObituary serverThread = MinesweeperServerTest.startServer(false, "board_file_3");

        Thread threads[] = new Thread[THREADS];
        threads[0] = new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = MinesweeperServerTest.connect(serverThread);
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        in.readLine();
                        out.println("flag 1 0");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("dig 0 3");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("flag 5 0");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("dig 0 4");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("flag 3 1");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("bye");
                        out.close();
                        in.close();
                    }
                    finally {
                        socket.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        threads[1] = new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = MinesweeperServerTest.connect(serverThread);
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        in.readLine();
                        out.println("dig 4 4");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("flag 3 1");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("dig 3 5");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("flag 1 2");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("dig 0 3");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("bye");
                        out.close();
                        in.close();
                    }
                    finally {
                        socket.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        threads[2] = new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = MinesweeperServerTest.connect(serverThread);
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        in.readLine();
                        out.println("flag 1 2");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("dig 0 4");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("flag 1 0");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("dig 4 4");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("flag 5 0");
                        MinesweeperServerTest.readLines(in, 6);
                        out.println("bye");
                        out.close();
                        in.close();
                    }
                    finally {
                        socket.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for (int i = 0; i < THREADS; i++) {
            threads[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            threads[i].join();
        }

        Thread.sleep(1000);
        Socket socket = MinesweeperServerTest.connect(serverThread);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            in.readLine();

            out.println("look");
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], in.readLine());
            }
        }
        finally {
            socket.close();
        }
    }
}

/**
 * A thread and possibly the error that terminated it.
 */
class ThreadWithObituary {

    private final Thread thread;
    private Throwable error = null;

    /** Create and start a new thread. */
    ThreadWithObituary(Runnable runnable) {
        thread = new Thread(runnable);
        thread.setUncaughtExceptionHandler((thread, error) -> {
            error.printStackTrace();
            this.error = error;
        });
        thread.start();
    }

    /** Return the thread. */
    synchronized Thread thread() {
        return thread;
    }

    /** Return the error that terminated the thread, if any. */
    synchronized Throwable error() {
        return error;
    }
}
