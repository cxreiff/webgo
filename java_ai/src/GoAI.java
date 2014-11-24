/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Java AI for Web Go game. Decides computer's moves and analyzes endgame positions.
 */

public class GoAI
{

	public static void main(String[] args)
	{
		String input = "0zzz";	//input string representation of game board state.

		int op = Integer.parseInt(input.substring(0, 1));
		String pos = input.substring(1);

		String result;

		if(op == 0) result = nextMove(pos);
		else result = evalEnd(pos);

		System.out.println(result);
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
}
