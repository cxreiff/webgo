import java.util.ArrayList;
import java.util.List;

/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Node representing a game state that can play games to self-evaluate.
 */

public class GameNode
{

	private int n;
	private int turn;
	private int value;
	private String pos;
	private String[] kolist;
	private GameNode parent;
	private ArrayList<GameNode> children;

	public GameNode(int turn, String pos, String[] kolist, GameNode parent)
	{
		this.turn = turn;
		this.pos = pos;
		this.n = (int) Math.sqrt(pos.length());
		this.kolist = kolist;
		this.parent = parent;
		this.children = new ArrayList<GameNode>();
	}

	public GameNode expand()	//Generates a child game node, adds it to list of children, and returns it.
	{
		GameNode result = newChild();
		children.add(result);
		return result;
	}
	public GameNode newChild()	//Returns a new node representing a game position reachable in one move from this one.
	{
		String childPos;
		int target;

		List<Integer> empty = new ArrayList<Integer>();

		for(int i = 0; i < pos.length(); i++)
		{
			if(pos.charAt(i) == 'e') empty.add(i);
		}

		int loops = 49;

		if (turn == 0)
		{
			do
			{
				target = empty.get((int)(Math.random() * empty.size()));

				childPos = pos.substring(0, target) + "b" + pos.substring(target + 1);

				childPos = captureWhite(childPos);

				if(--loops < 1) return new GameNode((turn+1) % 2, this.getPos(), kolist, this);

			} while((captureBlack(childPos).charAt(target) == 'e') || !checkKo(childPos));
		}
		else
		{
			do
			{
				target = empty.get((int)(Math.random() * empty.size()));

				childPos = pos.substring(0, target) + "w" + pos.substring(target + 1);

				childPos = captureBlack(childPos);

				if(--loops < 1) return new GameNode((turn+1) % 2, this.getPos(), kolist, this);

			} while((captureWhite(childPos).charAt(target) == 'e') || !checkKo(childPos));
		}

		return new GameNode((turn + 1) % 2, childPos, advanceKo(kolist), this);
	}

	public void evaluate()	//Plays a random game from this node, evaluates the result, and updates values.
	{
		//Add 2 * searchiness to the turn value.
		//Searchiness is some value representing the depth into the game to randomly play.
		int searchiness = 49;

		int blackcap = 0;
		int whitecap = 0;

		GameNode descendant = this.newChild();

		//Make random moves.
		while(searchiness > 0)
		{
			GameNode next = descendant.newChild();

			//Update capture counts.
			if(descendant.getTurn() == 0)
			{
				for(int i = 0; i < descendant.getPos().length(); i++)
				{
					if(descendant.getPos().charAt(i) == 'w')
					{
						if(next.getPos().charAt(i) == 'e') whitecap++;
					}
				}
			}
			else
			{
				for(int i = 0; i < descendant.getPos().length(); i++)
				{
					if(descendant.getPos().charAt(i) == 'b')
					{
						if(next.getPos().charAt(i) == 'e') blackcap++;
					}
				}
			}

			//Check for endgame conditions.
			if(next.getPos().equals(descendant.getPos())) break;	//Break if child is same as parent.

			//Move to generated child.
			descendant = next;

			searchiness--;

		}//When turn count reaches 0, break.

		//Evaluate position using endgame evaluation method and running count of captured pieces.
		String territory = descendant.countTerritory();
		int divider = 0;

		for(int i = 0; i < territory.length(); i++)
		{
			if(territory.charAt(i) == 'x') divider = i;
		}

		int evaluation = 1000 * (Integer.parseInt(descendant.countTerritory().substring(0,divider)) + whitecap)
							- 1000 * (Integer.parseInt(descendant.countTerritory().substring(divider+1)) + blackcap);

		//Use evaluation to update values of this and each ancestor node.
		this.update(evaluation);
	}

	public void update(int evaluation)	//Adjusts the current value based on input evaluation, and then updates parent.
	{
		value = (value + evaluation) / 2;

		if(parent != null) parent.update(value);
	}

	public String captureWhite(String pos)	//Returns a position string with surrounded white pieces changed to empties.
	{

		String result = pos;

		List<Integer> candidates = new ArrayList<Integer>();

		for(int i = 0; i < result.length(); i++)
		{
			if(result.charAt(i) == 'w') candidates.add(i);
		}

		while(candidates.size() > 0)
		{
			List<Integer> adj = new ArrayList<Integer>();

			genAdj(n, adj, candidates);

			boolean life = false;

			for(int i = 0; i < adj.size(); i++)
			{
				int current = adj.get(i);

				if(current - n > -1 && result.charAt(current - n) == 'e') { life = true; break; }
				if(current + n < n*n && result.charAt(current + n) == 'e') { life = true; break; }
				if((current - 1 > -1) && (current - 1) % n != n-1 && result.charAt(current - 1) == 'e') {life = true; break; }
				if((current + 1) % n != 0 && result.charAt(current + 1) == 'e') {life = true; break; }

			}

			if(!life)
			{
				for(int i = 0; i < adj.size(); i++)
				{
					result = result.substring(0, adj.get(i)) + "e" + result.substring(adj.get(i)+1);
				}
			}
		}

		return result;
	}
	public String captureBlack(String pos)	//Returns a position string with surrounded black pieces changed to empties.
	{
		String result = pos;

		List<Integer> candidates = new ArrayList<Integer>();

		for(int i = 0; i < result.length(); i++)
		{
			if(result.charAt(i) == 'b') candidates.add(i);
		}

		while(candidates.size() > 0)
		{
			List<Integer> adj = new ArrayList<Integer>();

			genAdj(n, adj, candidates);

			boolean life = false;

			for(int i = 0; i < adj.size(); i++)
			{
				int current = adj.get(i);

				if(current - n > -1 && result.charAt(current - n) == 'e') { life = true; break; }
				if(current + n < n*n && result.charAt(current + n) == 'e') { life = true; break; }
				if(current - 1 > -1 && (current - 1) % n != n-1 && result.charAt(current - 1) == 'e') {life = true; break; }
				if((current + 1) % n != 0 && result.charAt(current + 1) == 'e') {life = true; break; }

			}

			if(!life)
			{
				for(int i = 0; i < adj.size(); i++)
				{
					result = result.substring(0, adj.get(i)) + "e" + result.substring(adj.get(i)+1);
				}
			}
		}

		return result;
	}
	public String[] advanceKo(String[] kolist)	//Updates the list of recent game board positions for enforcing ko.
	{
		String result[] = new String[6];

		System.arraycopy(kolist, 1, result, 0, 5);

		result[5] = this.getPos();

		return result;
	}
	public boolean checkKo(String pos)	//Checks the input position against the list of recent board positions.
	{
		for(int i = 0; i < kolist.length; i++)
		{
			if(kolist[i].equals(pos)) {return false;}
		}

		return true;
	}

	public String countTerritory()		//Returns an evaluation of the final board position.
	{

		int blackTerr = 0;
		int whiteTerr = 0;

		//Add all empty tiles into an array.
		List<Integer> candidates = new ArrayList<Integer>();

		for(int i = 0; i < pos.length(); i++)
		{
			if(pos.charAt(i) == 'e') candidates.add(i);
		}

		while(candidates.size() > 0) {

			//Group empty tiles into groups of adjacent tiles.

			List<Integer> adj = new ArrayList<Integer>();
			genAdj(n, adj, candidates);

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

	//Extracts a list of connected candidate nodes from the list of candidates, putting it in adj.
	public void genAdj(int n, List<Integer> adj, List<Integer> candidates)
	{
		int current = candidates.remove(0);

		if(!adj.contains(current))
		{
			adj.add(current);

			if (candidates.contains(current - 7)) {

				candidates.add(0, candidates.remove(candidates.indexOf(current - 7)));
				genAdj(n, adj, candidates);
			}
			if (candidates.contains(current + 7)) {

				candidates.add(0, candidates.remove(candidates.indexOf(current + 7)));
				genAdj(n, adj, candidates);
			}
			if ((current + 1 % n != 0) && candidates.contains(current + 1)) {

				candidates.add(0, candidates.remove(candidates.indexOf(current + 1)));
				genAdj(n, adj, candidates);
			}
			if ((current - 1 % n != n-1) && candidates.contains(current - 1)) {

				candidates.add(0, candidates.remove(candidates.indexOf(current - 1)));
				genAdj(n, adj, candidates);
			}
		}
	}

	public GameNode bestChild()	//Returns child with the best value (highest if black's turn, lowest if white's turn).
	{
		GameNode best = this;

		if(turn == 0)
		{
			int max = Integer.MIN_VALUE;

			for (int i = 0; i < children.size(); i++)
			{
				int val = children.get(i).getValue();

				if(val > max) { max = val; best = children.get(i); }
			}
		}
		else
		{
			int min = Integer.MAX_VALUE;

			for(int i = 0; i < children.size(); i++)
			{
				int val = children.get(i).getValue();

				if(val < min) { min = val; best = children.get(i); }
			}
		}

		return best;
	}

	public int getTurn()
	{
		return this.turn;
	}
	public String getPos()
	{
		return this.pos;
	}
	public int getValue()
	{
		return this.value;
	}
	public String toString()
	{
		return this.getPos() + "--" + this.getValue();
	}
}
