package minesweeper.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

/**
 * Test DIG message.
 * 
 * Testing strategy
 *   (x, y) is _flagged_, _dug_, or _untouched_
 *   (x, y) contains a bomb or doesn't
 *   (x, y) has neighbor squares with bombs or doesn't
 *   server is in debug mode or isn't
 */
public class DigTest {

    @Test(timeout = 10000)
    public void testDigUntouched() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - 2 - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -"
        };

        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false, "board_file_2"));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("dig 2 0");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testRecursiveDig() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - 2      ",
            "- - 2      ",
            "2 2 1      ",
            "           ",
            "           ",
            "2 3 3 3 3 2",
            "- - - - - -"
        };

        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false, "board_file_2"));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            // throw away HELLO msg
            in.readLine();

            out.println("dig 4 4");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testDigBombDisconnect() throws IOException, InterruptedException {
        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false, "board_file_2"));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("dig 1 1");
            assertEquals("BOOM!", in.readLine());
            assertTrue(in.readLine() == null);
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testDigBombDebug() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - - - - -",
            "- 2 - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -"
        };

        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(true, "board_file_2"));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("dig 1 1");
            assertEquals("BOOM!", in.readLine());

            out.println("look");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testDigDug() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "3 - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -"
        };

        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false, "board_file_2"));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // throw away HELLO msg
            in.readLine();

            out.println("dig 0 0");
            // throw away BOARD msg
            MinesweeperServerTest.readLines(in, 7);

            out.println("dig 0 0");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testDigFlagged() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - - - - -",
            "- - - - - -",
            "- - F - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -",
            "- - - - - -"
        };

        Socket socket = MinesweeperServerTest.connect(MinesweeperServerTest.startServer(false, "board_file_2"));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            // throw away HELLO msg
            in.readLine();

            out.println("flag 2 2");
            // throw away BOARD msg
            MinesweeperServerTest.readLines(in, 7);

            out.println("dig 2 2");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }
}
