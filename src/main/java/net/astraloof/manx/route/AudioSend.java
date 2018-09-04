package net.astraloof.manx.route;

import java.io.Serializable;

public abstract class AudioSend implements Serializable
{
	private static final long serialVersionUID = 1L;

	public long lastMix = -1L;
	
	public abstract float[] getBuffer();
	
	public void checkedFlush(long samplePosition)
	{
		if (lastMix != samplePosition)
			for (int i = 0; i < getBuffer().length; i ++)
				getBuffer()[i] = 0.0f;
		lastMix = samplePosition;
	}
}
