/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Java AI for Web Go game. Decides computer's moves and analyzes endgame positions.
 */

public class GoAI
{

    public static String main(String[] args)
    {
        String input = new String("0xxx");    //input string representation of game board state.

        int op = Integer.parseInt(input.substring(0,1));
        String pos = input.substring(1);

        String result;

        if(op==0) result = nextMove(pos);
        else result = evalEnd(pos);

        return result;
    }

    public static String evalEnd(String pos)
    {
        return pos;
    }

    public static String nextMove(String pos)
    {
        return "yyy";
    }
}
