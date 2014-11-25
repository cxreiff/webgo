/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Java AI for Web Go game. Decides computer's moves and analyzes endgame positions.
 */

public class GoAI
{

	public static void main(String[] args)
	{
		String input = "0x01e02e03b04w05e06e07e08e09e10e11e12e13e14e15e16e17e18e19e20e21e22e23e24e25e26e27e28e29e30e31e32e33e34e35e36e37e38e39e40e41e42e43e44e45e46e47e48e49b";	//input string representation of game board state.

		int op = Integer.parseInt(input.substring(0, 1));
		String pos = input.substring(2);

		int n = (int)(Math.sqrt((pos.length()/3)));

		int[][] board = new int[n][n];

		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				char stone = pos.charAt((i*7*3)+j*3+2);

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

		printBoard(board);

		String result;

		if(op == 0) result = nextMove(pos);
		else result = evalEnd(pos);

		System.out.println("\n"+result);
		//return result to javascript page.
	}

	public static String nextMove(String pos)
	{
		return pos+"yyy";
	}

	public static String evalEnd(String pos)
	{
		return pos+"xxx";
	}

	public static void printBoard(int[][] board)
	{
		for(int i=0; i<board.length; i++)
		{
			for(int j=0; j<board[0].length; j++)
			{
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
}
