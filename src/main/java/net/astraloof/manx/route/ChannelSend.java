package net.astraloof.manx.route;

import net.astraloof.manx.Manx;
import net.astraloof.manx.engine.AudioEngine;

public class ChannelSend extends AudioSend
{
	private static final long serialVersionUID = 1L;

	public Channel origin, target;
	
	private float[] buffer = null;
	private AudioEngine engine;
	
	public ChannelSend(Channel origin, Channel target)
	{
		this.origin = origin;
		this.target = target;
		
		this.engine = Manx.instance.engine;
	}
	
	@Override
	public float[] getBuffer()
	{
		if (buffer == null || buffer.length != engine.device.getBufferSize())
			buffer = new float[engine.device.getBufferSize()];
		return buffer;
	}
}
