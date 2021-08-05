package minesweeper.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

/**
 * Test FLAG and DEFLAG message.
 * 
 * Testing strategy
 *   (x, y) is _flagged_, _dug_, or _untouched_
 */
public class FlagDeflagTest {

    @Test(timeout = 10000)
    public void testFlagUntouched() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - - - - -",
            "- F - - - -",
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

            out.println("flag 1 1");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testFlagFlagged() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - - - - -",
            "- F - - - -",
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

            out.println("flag 1 1");
            // throw away BOARD msg
            MinesweeperServerTest.readLines(in, 7);

            out.println("flag 1 1");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testDeflagFlagged() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - - - - -",
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

            out.println("flag 1 1");
            // throw away BOARD msg
            MinesweeperServerTest.readLines(in, 7);

            out.println("deflag 1 1");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testDeflagUntouched() throws IOException, InterruptedException {
        String expected[] = new String[] {
            "- - - - - -",
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

            out.println("deflag 1 1");
            MinesweeperServerTest.assertReadLines(in, expected);
        }
        finally {
            socket.close();
        }
    }
}
