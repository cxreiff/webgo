import java.util.ArrayList;
import java.util.List;

/**
 * Class for testing functionality of the node class for Go AI.
 */

public class NodeTest
{
	public static void main(String[] args)
	{
		String[] kolist = {"", "", "bwbewwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwebbbbbb", "", "", ""};

		GameNode test = new GameNode(0, "bweewwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwebbbbbb", kolist, null);

		int target;
		String childPos;
		String pos = "bweewwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwbwwwwwwebbbbbb";

		List<Integer> empty = new ArrayList<Integer>();

		for(int i = 0; i < pos.length(); i++)
		{
			if(pos.charAt(i) == 'e') empty.add(i);
		}

		System.out.println("E: "+empty.toString());

		do
		{
			target = empty.get((int)(Math.random() * empty.size()));
			System.out.println("T: "+target);

			childPos = pos.substring(0, target) + "b" + pos.substring(target + 1);

			childPos = test.captureWhite(childPos);
			System.out.println("P: "+childPos);

			System.out.println("A: "+test.captureBlack(childPos).charAt(target));

		} while((test.captureBlack(childPos).charAt(target) == 'e') || !test.checkKo(childPos));

		System.out.println("R: "+childPos);
	}
}
