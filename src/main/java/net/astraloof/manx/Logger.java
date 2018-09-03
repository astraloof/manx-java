package net.astraloof.manx;

import java.util.logging.Level;

import org.apache.commons.lang3.text.WordUtils;

@SuppressWarnings("deprecation")
public class Logger
{
	private String name;
	
	public Logger(String name)
	{
		this.name = name;
	}
	
	public Logger(Class<?> c)
	{
		this(c.getSimpleName());
	}
	
	public void log(Level level, String message)
	{
		System.out.print("[" + level.getName() + "][" + name + "]: ");
		System.out.println(WordUtils.wrap(message, 80)
				.replace("\n", "\n\t\t"));
	}
	
	public void logArray(Level level, String message, Object[] objects)
	{
		StringBuilder build = new StringBuilder();
		for (Object object : objects)
			build.append(object).append(", ");
		String string = build.toString();
		if (string.length() >= 2)
			string = string.substring(0, build.length() - 2);
		log(level, message + " (" + string + ")");
	}
	
	public void info(String message)
	{
		log(Level.INFO, message);
	}
	
	public void verbose(String message)
	{
		log(new L("VERBOSE", 0), message);
	}
	
	private static final class L extends Level
	{
		private static final long serialVersionUID = 1L;

		public L(String name, int value)
		{
			super(name, value);
		}
	}
}
