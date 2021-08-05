package minesweeper.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

/**
 * Test HELLO message.
 */
public class HelloTest {

    @Test(timeout = 10000)
    public void testHello() throws IOException, InterruptedException {
        final int THREADS = 2, sizeX = 13, sizeY = 5;

        ThreadWithObituary serverThread = MinesweeperServerTest.startServer(false, sizeX, sizeY);

        Thread threads[] = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    try {
                        Socket socket = MinesweeperServerTest.connect(serverThread);
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            assertTrue(in.readLine() != null);
                        }
                        finally {
                            // keep the connection alive.
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for (int i = 0; i < THREADS; i++) {
            threads[i].start();
        }

        String expected = "Welcome to Minesweeper. Board: " + Integer.toString(sizeY) + " columns by " + 
            Integer.toString(sizeX) + " rows. Players: " + Integer.toString(THREADS + 1) +
            " including you. Type 'help' for help.";

        Thread.sleep(1000);
        Socket socket = MinesweeperServerTest.connect(serverThread);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            assertEquals(expected, in.readLine());
        }
        finally {
            socket.close();
        }
    }
}
