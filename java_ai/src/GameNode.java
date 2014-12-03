import java.util.ArrayList;
import java.util.List;

/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Node representing a game state that can play games to self-evaluate.
 */

public class GameNode
{
	private static int searchiness = 42;

	private int turn;
	private String pos;
	private GameNode parent;
	private ArrayList<GameNode> children;

	private int value;

	public GameNode(int turn, String pos, GameNode parent)
	{
		this.turn = turn;
		this.pos = pos;
		this.parent = parent;
		this.children = new ArrayList<GameNode>();
	}

	public GameNode expand()
	{
		String childPos;

		int target = (int)(Math.random() * pos.length());

		while(pos.charAt(target) != 'e') {target = (int)(Math.random() * pos.length());}

		if (turn % 2 == 0)
		{
			childPos = pos.substring(0, target) + "b" + pos.substring(target + 1);
		}
		else
		{
			childPos = pos.substring(0, target) + "w" + pos.substring(target + 1);
		}

		GameNode newChild = new GameNode((turn + 1) % 2, childPos, this);

		children.add(newChild);

		return newChild;
	}

	public void evaluate()
	{
		//Add 2 * searchiness to the turn value, searchiness being some value representing the
		//depth into the game to randomly play.
		int turns = turn + 2 * searchiness;

		int blackcap = 0;
		int whitecap = 0;

		//Make random moves, stone color depending on whether the turn count is odd or even.
		while(turns > 0)
		{
			if(turns % 2 == 0)
			{

			}
			else
			{

			}

			//Apply capturing logic.

			turn--;

		}//When turn count reaches 0, break.

		//Evaluate position using endgame evaluation method and running count of captured pieces.

		//Use evaluation to update values of each ancestor node.
	}

	public static String countTerritory(String pos)		//Returns an evaluation of the final board position.
	{
		int n = (int) Math.sqrt(pos.length());

		int blackTerr = 0;
		int whiteTerr = 0;

		//Add all empty tiles into an array.
		List<Integer> empties = new ArrayList<Integer>();

		for(int i = 0; i < pos.length(); i++)
		{
			if(pos.charAt(i) == 'e') empties.add(i);
		}

		while(empties.size() > 0) {

			//Group empty tiles into groups of adjacent tiles.

			List<Integer> adj = new ArrayList<Integer>();
			genAdj(n, adj, empties);

			//If a tile group is only adjacent to one type of stone,
			//add number of empty tiles in group to score of stone type.

			int adjColor = 0;	//0 if no adjacent stones so far, 1 if black is found, 2 if white is found, 3 if both.

			for(int i = 0; i < adj.size(); i++) {
				int temp = adj.get(i);

				if (temp - n >= 0) {
					switch (pos.charAt(temp - n)) {
						case 'b':
							if (adjColor == 0) adjColor = 1;
							else if (adjColor == 2) adjColor = 3;
							break;
						case 'w':
							if (adjColor == 0) adjColor = 2;
							else if (adjColor == 1) adjColor = 3;
							break;
						default:
							break;
					}
				}
				if (temp + n < n * n) {
					switch (pos.charAt(temp + n)) {
						case 'b':
							if (adjColor == 0) adjColor = 1;
							else if (adjColor == 2) adjColor = 3;
							break;
						case 'w':
							if (adjColor == 0) adjColor = 2;
							else if (adjColor == 1) adjColor = 3;
							break;
						default:
							break;
					}
				}
				if ((temp > 0) && (temp - 1) % n != n-1) {
					switch (pos.charAt(temp - 1)) {
						case 'b':
							if (adjColor == 0) adjColor = 1;
							else if (adjColor == 2) adjColor = 3;
							break;
						case 'w':
							if (adjColor == 0) adjColor = 2;
							else if (adjColor == 1) adjColor = 3;
							break;
						default:
							break;
					}
				}
				if ((temp < n*n) && (temp + 1) % n != 0) {
					switch (pos.charAt(temp + 1)) {
						case 'b':
							if (adjColor == 0) adjColor = 1;
							else if (adjColor == 2) adjColor = 3;
							break;
						case 'w':
							if (adjColor == 0) adjColor = 2;
							else if (adjColor == 1) adjColor = 3;
							break;
						default:
							break;
					}
				}
			}

			if(adjColor == 1) blackTerr += adj.size();
			else if(adjColor == 2) whiteTerr += adj.size();
		}

		return blackTerr+"x"+whiteTerr;
	}
	public static void genAdj(int n, List<Integer> adj, List<Integer> empties)
	{
		int current = empties.remove(0);

		if(!adj.contains(current))
		{
			adj.add(current);

			if (empties.contains(current - 7)) {

				empties.add(0, empties.remove(empties.indexOf(current - 7)));
				genAdj(n, adj, empties);
			}
			if (empties.contains(current + 7)) {

				empties.add(0, empties.remove(empties.indexOf(current + 7)));
				genAdj(n, adj, empties);
			}
			if ((current + 1 % n != 0) && empties.contains(current + 1)) {

				empties.add(0, empties.remove(empties.indexOf(current + 1)));
				genAdj(n, adj, empties);
			}
			if ((current - 1 % n != n-1) && empties.contains(current - 1)) {

				empties.add(0, empties.remove(empties.indexOf(current - 1)));
				genAdj(n, adj, empties);
			}
		}
	}

	public GameNode bestChild()
	{
		int max = this.getValue();
		GameNode best = this;

		for (int i = 0; i < children.size(); i++)
		{
			int val = children.get(i).getValue();

			if(val > max) { max = val; best = children.get(i); }
		}

		return best;
	}

	public String getPos()
	{
		return pos;
	}
	public int getValue()
	{
		return this.value;
	}

	public int numChildren()
	{
		return children.size();
	}
}
