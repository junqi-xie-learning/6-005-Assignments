/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import java.io.*;
import java.net.*;
import java.util.*;

import minesweeper.Board;

/**
 * Multiplayer Minesweeper server.
 */
public class MinesweeperServer {

    // System thread safety argument
    //   all operations on board are synchronized by thread-safe type Board
    //   all accesses to players are synchronized by the lock

    /** Instance of Board shared by all players. */
    private static Board board;
    /** Number of players currectly connected. */
    private static int players;

    /** Default server port. */
    private static final int DEFAULT_PORT = 4444;
    /** Maximum port number as defined by ServerSocket. */
    private static final int MAXIMUM_PORT = 65535;
    /** Default square board size. */
    private static final int DEFAULT_SIZE = 10;

    /** Socket for receiving incoming connections. */
    private final ServerSocket serverSocket;
    /** True if the server should *not* disconnect a client after a BOOM message. */
    private final boolean debug;

    // Abstraction function:
    //   static variables and methods represent a minesweeper server with a board
    //   instance variables and methods represent a thread serving a client user
    // Rep invariant:
    //   none
    // Safety from rep exposure:
    //   board and players are never returned, and are declared as private

    /**
     * Make a MinesweeperServer that listens for connections on port.
     * 
     * @param port port number, requires 0 <= port <= 65535
     * @param debug debug mode flag
     * @throws IOException if an error occurs opening the server socket
     */
    public MinesweeperServer(int port, boolean debug) throws IOException {
        serverSocket = new ServerSocket(port);
        this.debug = debug;
    }

    /**
     * Run the server, listening for client connections and handling them.
     * Never returns unless an exception is thrown.
     * 
     * @throws IOException if the main server socket is broken
     *                     (IOExceptions from individual clients do *not* terminate serve())
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            Socket socket = serverSocket.accept();

            // handle the client with a thread
            new Thread(() -> {
                try {
                    try {
                        handleConnection(socket);
                    }
                    finally {
                        socket.close();
                    }
                }
                catch (IOException ioe) {
                    ioe.printStackTrace(); // handle exceptions within the thread
                }
            }).start();
        }
    }

    /**
     * Handle a single client connection. Returns when client disconnects.
     * 
     * @param socket socket where the client is connected
     * @throws IOException if the connection encounters an error or terminates unexpectedly
     */
    private void handleConnection(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        synchronized (this) {
            players++;
        }

        try {
            out.println("Welcome to Minesweeper. Board: " + Integer.toString(board.getColumn()) + " columns by " +
                Integer.toString(board.getRow()) + " rows. Players: " + Integer.toString(players) +
                " including you. Type 'help' for help.");
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                String output = handleRequest(line);
                out.println(output);
                if (output == "BOOM!") {
                    if (!debug) {
                        break;
                    }
                }
            }
        }
        catch (InterruptedIOException e) { }
        finally {
            out.close();
            in.close();
            synchronized (this) {
                players--;
            }
        }
    }

    /**
     * Handler for client input, performing requested operations and returning an output message.
     * 
     * @param input message from client
     * @return message to client
     * @throws InterruptedIOException if requested to disconnect
     */
    private String handleRequest(String input) throws InterruptedIOException {
        String regex = "(look)|(help)|(bye)|"
                     + "(dig -?\\d+ -?\\d+)|(flag -?\\d+ -?\\d+)|(deflag -?\\d+ -?\\d+)";
        String[] help = {
            "Your input is specified by the following grammar: ",
            "MESSAGE ::= ( LOOK | DIG | FLAG | DEFLAG | HELP_REQ | BYE ) NEWLINE",
            "LOOK ::= \"look\"",
            "DIG ::= \"dig\" SPACE X SPACE Y",
            "FLAG ::= \"flag\" SPACE X SPACE Y",
            "DEFLAG ::= \"deflag\" SPACE X SPACE Y",
            "HELP_REQ ::= \"help\"",
            "BYE ::= \"bye\"",
            "NEWLINE ::= \"\\n\" | \"\\r\" \"\\n\"?",
            "X ::= INT",
            "Y ::= INT",
            "SPACE ::= \" \"",
            "INT ::= \"-\"? [0-9]+"
        };

        if (!input.matches(regex)) {
            // invalid input
            return String.join("\r\n", help);
        }
        String[] tokens = input.split(" ");
        if (tokens[0].equals("look")) {
            // 'look' request
            return board.toString();
        }
        else if (tokens[0].equals("help")) {
            // 'help' request
            return String.join("\r\n", help);
        }
        else if (tokens[0].equals("bye")) {
            // 'bye' request
            throw new InterruptedIOException();
        }
        else {
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            if (tokens[0].equals("dig")) {
                // 'dig x y' request
                return board.dig(y, x) ? "BOOM!" : board.toString();
            }
            else if (tokens[0].equals("flag")) {
                // 'flag x y' request
                board.flag(y, x);
                return board.toString();
            }
            else if (tokens[0].equals("deflag")) {
                // 'deflag x y' request
                board.deflag(y, x);
                return board.toString();
            }
        }
        // Should never get here, make sure to return in each of the cases above
        throw new UnsupportedOperationException();
    }

    /**
     * Start a MinesweeperServer using the given arguments.
     * 
     * <br> Usage:
     *      MinesweeperServer [--debug | --no-debug] [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]
     * 
     * <br> The --debug argument means the server should run in debug mode. The server should disconnect a
     *      client after a BOOM message if and only if the --debug flag was NOT given.
     *      Using --no-debug is the same as using no flag at all.
     * <br> E.g. "MinesweeperServer --debug" starts the server in debug mode.
     * 
     * <br> PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port the server
     *      should be listening on for incoming connections.
     * <br> E.g. "MinesweeperServer --port 1234" starts the server listening on port 1234.
     * 
     * <br> SIZE_X and SIZE_Y are optional positive integer arguments, specifying that a random board of size
     *      SIZE_X*SIZE_Y should be generated.
     * <br> E.g. "MinesweeperServer --size 42,58" starts the server initialized with a random board of size
     *      42*58.
     * 
     * <br> FILE is an optional argument specifying a file pathname where a board has been stored. If this
     *      argument is given, the stored board should be loaded as the starting board.
     * <br> E.g. "MinesweeperServer --file boardfile.txt" starts the server initialized with the board stored
     *      in boardfile.txt.
     * 
     * <br> The board file format, for use with the "--file" option, is specified by the following grammar:
     * <pre>
     *   FILE ::= BOARD LINE+
     *   BOARD ::= X SPACE Y NEWLINE
     *   LINE ::= (VAL SPACE)* VAL NEWLINE
     *   VAL ::= 0 | 1
     *   X ::= INT
     *   Y ::= INT
     *   SPACE ::= " "
     *   NEWLINE ::= "\n" | "\r" "\n"?
     *   INT ::= [0-9]+
     * </pre>
     * 
     * <br> If neither --file nor --size is given, generate a random board of size 10x10.
     * 
     * <br> Note that --file and --size may not be specified simultaneously.
     * 
     * @param args arguments as described
     */
    public static void main(String[] args) {
        // Command-line argument parsing is provided. Do not change this method.
        boolean debug = false;
        int port = DEFAULT_PORT;
        int sizeX = DEFAULT_SIZE;
        int sizeY = DEFAULT_SIZE;
        Optional<File> file = Optional.empty();

        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while (!arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--debug")) {
                        debug = true;
                    }
                    else if (flag.equals("--no-debug")) {
                        debug = false;
                    }
                    else if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());
                        if (port < 0 || port > MAXIMUM_PORT) {
                            throw new IllegalArgumentException("port " + port + " out of range");
                        }
                    }
                    else if (flag.equals("--size")) {
                        String[] sizes = arguments.remove().split(",");
                        sizeX = Integer.parseInt(sizes[0]);
                        sizeY = Integer.parseInt(sizes[1]);
                        file = Optional.empty();
                    }
                    else if (flag.equals("--file")) {
                        sizeX = -1;
                        sizeY = -1;
                        file = Optional.of(new File(arguments.remove()));
                        if (!file.get().isFile()) {
                            throw new IllegalArgumentException("file not found: \"" + file.get() + "\"");
                        }
                    }
                    else {
                        throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
                    }
                }
                catch (NoSuchElementException nsee) {
                    throw new IllegalArgumentException("missing argument for " + flag);
                }
                catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        }
        catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("usage: MinesweeperServer [--debug | --no-debug] [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]");
            return;
        }

        try {
            runMinesweeperServer(debug, file, sizeX, sizeY, port);
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Start a MinesweeperServer running on the specified port, with either a random new board or a
     * board loaded from a file.
     * 
     * @param debug The server will disconnect a client after a BOOM message if and only if debug is false.
     * @param file If file.isPresent(), start with a board loaded from the specified file,
     *             according to the input file format defined in the documentation for main(..).
     * @param sizeX If (!file.isPresent()), start with a random board with width sizeX
     *              (and require sizeX > 0).
     * @param sizeY If (!file.isPresent()), start with a random board with height sizeY
     *              (and require sizeY > 0).
     * @param port The network port on which the server should listen, requires 0 <= port <= 65535.
     * @throws IOException if a network error occurs
     */
    public static void runMinesweeperServer(boolean debug, Optional<File> file, int sizeX, int sizeY, int port) throws IOException {
        assert file.isPresent() || sizeX > 0 && sizeY > 0;
        assert 0 <= port && port <= 65535;
        
        int[][] boardRep = file.isPresent() ? loadBoard(file.get()) : generateBoard(sizeY, sizeX);
        board = new Board(boardRep);
        MinesweeperServer server = new MinesweeperServer(port, debug);
        server.serve();
    }

    /**
     * Load the board from the specified file.
     * 
     * @param file the input file format defined in the documentation for main(..)
     * @return board representation of the board in integers
     * @throws RuntimeException if the file doesn't exist or improperly formatted
     */
    private static int[][] loadBoard(File file) throws RuntimeException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] line = reader.readLine().split("\\s");
            int sizeX = Integer.parseInt(line[0]), sizeY = Integer.parseInt(line[1]);
            int[][] boardRep = new int[sizeY][sizeX];

            for (int i = 0; i < sizeY; i++) {
                line = reader.readLine().split("\\s");
                for (int j = 0; j < sizeX; j++) {
                    boardRep[i][j] = Integer.parseInt(line[j]);
                }
            }
            reader.close();
            return boardRep;
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Randomly generate a board, with probability .25 to contain a bomb for each
     * square
     * 
     * @param sizeY the height of the random board
     * @param sizeX the width of the random board
     * @return board representation of the board in integers
     */
    private static int[][] generateBoard(int sizeY, int sizeX) {
        int[][] boardRep = new int[sizeY][sizeX];
        Random random = new Random();

        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                boardRep[i][j] = random.nextDouble() < .25 ? 1 : 0;
            }
        }
        return boardRep;
    }
}
