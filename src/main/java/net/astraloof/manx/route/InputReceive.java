package net.astraloof.manx.route;

import net.astraloof.manx.Manx;

public class InputReceive extends ChannelSend
{
	private static final long serialVersionUID = 1L;
	
	private int channel;
	
	public InputReceive(int channel)
	{
		super(null, null);
		this.channel = channel;
	}

	@Override
	public float[] getBuffer()
	{
		int max = Manx.instance.engine.inputBuffers.length;
		if (channel >= max)
			return null;
		else
			return Manx.instance.engine.inputBuffers[channel];
	}
}
