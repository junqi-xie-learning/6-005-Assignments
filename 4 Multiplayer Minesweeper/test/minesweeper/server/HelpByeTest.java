package minesweeper.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

/**
 * Test HELP_REQ and BYE message.
 */
public class HelpByeTest {

    @Test(timeout = 10000)
    public void testHelp() throws IOException, InterruptedException {
        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("help");
            assertTrue(in.readLine() != null);
        } 
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testBye() throws IOException, InterruptedException {
        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("bye");
            assertTrue(in.readLine() == null);
        } 
        finally {
            socket.close();
        }
    }
}
