package net.astraloof.manx.route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.astraloof.manx.Manx;

public class Channel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public List<ChannelSend> receives = new ArrayList<>();
	public List<AudioSend> sends = new ArrayList<>();
	
	private long lastMix = -1L;
	
	public void createSend(Channel target)
	{
		ChannelSend send = new ChannelSend(this, target);
		sends.add(send);
		target.receives.add(send);
	}

	public void promptMix(long samplePosition)
	{
		if (lastMix < samplePosition)
		{
			for (AudioSend send : sends)
				send.checkedFlush(samplePosition);
			
			for (ChannelSend receive : receives)
			{
				if (receive.origin != null)
					receive.origin.promptMix(samplePosition);
				if (receive.getBuffer() != null)
					for (int i = 0; i < Manx.instance.engine.device.getBufferSize(); i ++)
						for (AudioSend send : sends)
							if (send.getBuffer() != null)
								send.getBuffer()[i] += receive.getBuffer()[i];
			}
		}
	}
	
//	boolean isMuted();
//	
//	boolean isSoloed();
//	
//	boolean isRecording();
//	
//	boolean isMonitored();
}
