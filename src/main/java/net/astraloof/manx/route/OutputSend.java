package net.astraloof.manx.route;

import net.astraloof.manx.Manx;

public class OutputSend extends AudioSend
{
	private static final long serialVersionUID = 1L;

	private int channel;
	
	public OutputSend(int channel)
	{
		this.channel = channel;
	}
	
	@Override
	public float[] getBuffer()
	{
		int max = Manx.instance.engine.outputBuffers.length;
		if (channel >= max)
			return null;
		else
			return Manx.instance.engine.outputBuffers[channel];
	}
}
