import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;

/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Java AI for Web Go game. Decides computer's moves and analyzes endgame positions.
 *
 * Board states are encoded in the form PTxSSSSSS...S, representing the following:
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
 * 	This class handles http requests from the game running with javascript on an html web page.
 */
public class GoAI
{
	public static void main(String[] args)
	{

	}

	public static class GoHandler implements HttpHandler
	{
		static int n;
		static int turn;

		static String[] kolist;

		static Timer timer;

		@Override
		public void handle(HttpExchange httpExchange) throws IOException
		{
			//TODO Take input from javascript go page.

			//input string representation of game board state.
			String input = "00x"+"bweewwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwebbbbbb";

			kolist = new String[6];
			kolist[0] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbb1";
			kolist[1] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbb2";
			kolist[2] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbb3";
			kolist[3] = "bwebwwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwebbbbbb";
			kolist[4] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbb5";
			kolist[5] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbb6";

			//Determines whether the AI is being asked for 0: the best next move, or 1: an endgame evaluation.
			int op = Integer.parseInt(input.substring(0, 1));

			//Determines whether 0: black plays next or 1: white plays next.
			turn = Integer.parseInt(input.substring(1, 2));

			//string representation of the board and its stones.
			String pos = input.substring(3);

			//Timer to determine how long to "think" before making a move.
			timer = new Timer();

			//Length of each side of the board.
			n = (int)(Math.sqrt((pos.length())));

			//2D matrix representation of input board, 0: empty, 1: black, 2: white.
			printBoard(genBoard(pos));
			System.out.println();

			System.out.println(((turn == 0)?"black":"white")+" turn"+"\n");

			String result = (op == 0) ? nextMove(pos, turn) : evalEnd(pos);

			System.out.println("R: "+result);
			System.out.println();

			//2D matrix representation of output board, 0: empty, 1: black, 2: white.
			printBoard(genBoard(result));

			//TODO Return result to javascript go page.
		}

		public static String nextMove(String pos, int turn)		//Returns an intelligent next move for the player
		{
			timer.reset();

			GameNode root = new GameNode(turn, pos, kolist, null);

			ArrayList<GameNode> open = new ArrayList<GameNode>();
			open.add(root);

			int patience = 1000;	//Time limit in ms to calculate each move.

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

			System.out.println("V: "+root.bestChild().getValue());

			//When all node adjustments have been made and time has run out, choose the depth:1 option with the best value.
			return root.bestChild().getPos();
		}
		public static String evalEnd(String pos)	//Returns an evaluation of the final board position.
		{
			return new GameNode(turn, pos, kolist, null).countTerritory();
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