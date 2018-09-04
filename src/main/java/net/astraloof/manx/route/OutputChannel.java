package net.astraloof.manx.route;

public class OutputChannel extends Channel
{
	private static final long serialVersionUID = 1L;
	
	public OutputChannel(int channel)
	{
		super();
		sends.add(new OutputSend(channel));
	}
}
