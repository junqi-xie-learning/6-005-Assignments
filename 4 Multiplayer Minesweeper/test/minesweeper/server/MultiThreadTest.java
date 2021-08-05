package minesweeper.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

/**
 * Test multi-thread senarios.
 */
public class MultiThreadTest {

    @Test(timeout = 10000)
    public void testConcurrentOperation() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - - - - -",
            "- F - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -"
        };

        ThreadWithObituary serverThread = MinesweeperServerTest.startServer(false, "board_file_2");

        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = MinesweeperServerTest.connect(serverThread);
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("flag 1 1");
                    }
                    finally {
                        // keep the connection alive
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Socket socket = MinesweeperServerTest.connect(serverThread);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("look");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testRepeatedOperations() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - - - - -",
            "- F - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -"
        };

        ThreadWithObituary serverThread = MinesweeperServerTest.startServer(false, "board_file_2");

        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = MinesweeperServerTest.connect(serverThread);
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("flag 1 1");
                    }
                    finally {
                        // keep the connection alive
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Socket socket = MinesweeperServerTest.connect(serverThread);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("flag 1 1");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }
}
