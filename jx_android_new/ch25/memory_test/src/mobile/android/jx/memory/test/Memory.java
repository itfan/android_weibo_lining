package mobile.android.jx.memory.test;

public class Memory
{
	public static long used()
	{
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		return (total - free);
	}
}
