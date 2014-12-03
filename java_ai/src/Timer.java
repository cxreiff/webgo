/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Timer to time amount of searching before deciding on a move.
 */

public class Timer
{

	private long start;

	public Timer()
	{
		start = System.currentTimeMillis();
	}

	public long elapsed()
	{
		return System.currentTimeMillis() - start;
	}

	public void reset()
	{
		start = System.currentTimeMillis();
	}

	public String toString()
	{
		return elapsed() + "ms elapsed";
	}

}
