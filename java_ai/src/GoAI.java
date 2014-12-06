import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Java AI for Web Go game. Decides computer's moves and analyzes endgame positions.
 *
 * Board states are encoded in the form PTxSSSSSS(...)SSSxKKKKK(...)K, representing the following:
 *
 * 		P :	Respresents the type of information being asked for. Is "0" when asking
 * 			for the next move, and "1" when asking for a count of surrounded territory.
 *
 * 		T :	Represents the current turn of the game state. Is "0" when player 1 (black)
 * 			is next to move, and "1" when player 2 (white) is next to move.
 *
 * 		x : Divider character, isn't read.
 *
 * 		S : Represents the state of a given tile, can be "e" when empty, "b" when containing
 * 			a black stone, and "w" when containing a white stone. There are as many S
 * 			characters as board tiles, and are ordered from the top left to bottom right in
 * 			left to right, top to bottom order (english reading order).
 *
 * 		K :	Represents the state of a given tile for a state in the list of recent board
 * 			positions, collected in order to enforce the rule of ko (you cannot return the
 * 			board to a previous position. There are six times as many K characters as S
 * 			characters, to fill the six most recent previous board states.
 *
 * 	This class handles http requests from the game running with javascript on an html web page.
 */
public class GoAI
{
	public static void main(String[] args)
	{
		int port = 8080;

		if(args.length > 0)
		{
			try
			{
				port = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException ex)
			{
				System.err.println("USAGE: java YahtzeeService [port]");
				System.exit(1);
			}
		}

		try
		{
			InetSocketAddress addr = new InetSocketAddress(port);
			HttpServer server = HttpServer.create(addr, 1);

			server.createContext("/go.html", new GoHandler());

			server.start();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.err.println("Could not start server");
		}
	}

	public static class GoHandler implements HttpHandler
	{
		static int n;
		static int turn;

		static String[] kolist;

		static Timer timer;

		@Override
		public void handle(HttpExchange ex) throws IOException
		{

			System.out.println("========");
			System.out.println();

			//input string representation of game board state.
			String input = ex.getRequestURI().getQuery();

			//Determines whether the AI is being asked for 0: the best next move, or 1: an endgame evaluation.
			int op = Integer.parseInt(input.substring(0, 1));

			//Determines whether 0: black plays next or 1: white plays next.
			turn = Integer.parseInt(input.substring(1, 2));

			int split = 52;
			for(int i = 3; i < input.length(); i++)
			{
				if(input.charAt(i) == 'x') split = i;
			}

			//string representation of the board and its stones.
			String pos = input.substring(3, split);

			//Length of each side of the board.
			n = (int)(Math.sqrt((pos.length())));

			//Populate kolist.
			kolist = new String[6];
			for(int i = 0; i < 6; i++)
			{
				kolist[i] = input.substring(split+1+(n*n*i), split+1+(n*n*(i+1)));
			}

			//Timer to determine how long to "think" before making a move.
			timer = new Timer();

			//2D matrix representation of input board, 0: empty, 1: black, 2: white.
			if(op == 1)
			{
				printBoard(genBoard(pos));
				System.out.println();
			}

			if(op == 1) System.out.println(((turn == 0)?"black":"white")+" turn"+"\n");

			String result = (op == 0) ? nextMove(pos, turn) : evalEnd(pos);

			//2D matrix representation of output board, 0: empty, 1: black, 2: white.

			if(op == 0)
			{
				System.out.println("~~~~~~~");
				printBoard(genBoard(result));
				System.out.println("~~~~~~~");
			}

			String response = "{" + "\"result\":\"" + result + "\", " + "\"state\":\"" + pos + "\" }";

			ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
			byte[] responseBytes = response.getBytes();
			ex.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseBytes.length);
			ex.getResponseBody().write(responseBytes);
			ex.close();
		}

		public static String nextMove(String pos, int turn)		//Returns an intelligent next move for the player
		{
			timer.reset();

			GameNode root = new GameNode(turn, pos, kolist, null);

			ArrayList<GameNode> open = new ArrayList<GameNode>();
			open.add(root);

			int patience = 2000;	//Time limit in ms to calculate each move.

			//Before each loop of MCTS, make sure that the time limit has not been exceeded.
			while(timer.elapsed() < patience)
			{

				//Optimally (using UCT) choose a node in the game tree.
				//Expand the chosen node by picking a random child state.
				GameNode current = open.get((int) (Math.random() * open.size())).expand(); //TODO Change from random to UCT.

				//Add new node to pool of nodes that can be expanded.
				open.add(current);

				//Use evaluated random play from that new node to evaluate it.
				current.evaluate();
			}

			//When all node adjustments have been made and time has run out, choose the depth:1 option with the best value.
			return root.bestChild().getPos();
		}
		public static String evalEnd(String pos)	//Returns an evaluation of the final board position.
		{
			return (new GameNode(turn, pos, kolist, null).countTerritory());
		}

		public static int[][] genBoard(String pos)	//Generates a matrix representation of the board.
		{
			int[][] board = new int[n][n];

			for(int i = 0; i < n; i++)
			{
				for(int j = 0; j < n; j++)
				{
					char stone = pos.charAt((i*7)+j);

					switch(stone)
					{
						case 'e':
							board[i][j] = 0;
							break;
						case 'b':
							board[i][j] = 1;
							break;
						case 'w':
							board[i][j] = 2;
							break;
					}
				}
			}

			return board;
		}
		public static void printBoard(int[][] board)	//Prints the matrix representation of the board.
		{
			for(int i=0; i < board.length; i++)
			{
				for(int j=0; j < board[0].length; j++)
				{
					System.out.print(board[i][j]);
				}
				System.out.println();
			}
		}
	}
}