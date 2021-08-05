package minesweeper.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

/**
 * Test board initialization.
 * 
 * Testing strategy
 *   parameters: file, size, none
 */
public class BoardInitTest {

    @Test(timeout = 10000)
    public void testBlankBoardFile() throws IOException, InterruptedException {
        String expected = "- - - - - - - - - - -";

        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false, "board_file_1"));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("look");
            for (int i = 0; i < 11; i++) {
                assertEquals(expected, in.readLine());
            }
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testBlankBoardSize() throws IOException, InterruptedException {
        String expected = "- - - - - - - - - - - - -";

        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false, 13, 5));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("look");
            for (int i = 0; i < 5; i++) {
                assertEquals(expected, in.readLine());
            }
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testBlankBoard() throws IOException, InterruptedException {
        String expected = "- - - - - - - - - -";

        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("look");
            for (int i = 0; i < 10; i++) {
                assertEquals(expected, in.readLine());
            }
        }
        finally {
            socket.close();
        }
    }
}
