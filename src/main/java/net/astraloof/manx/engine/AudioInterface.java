package net.astraloof.manx.engine;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;

import net.astraloof.manx.Logger;

public class AudioInterface
{
	private Logger log = new Logger(AudioInterface.class);
	
	public AsioDriver asioDriver = null;
	
	public List<AsioChannel> inputs = new LinkedList<>();
	public List<AsioChannel> outputs = new LinkedList<>();
	
	public AudioInterface(AsioDriver asioDriver)
	{
		if (asioDriver != null)
		{
			this.asioDriver = asioDriver;
			
			for (int i = 0; i < asioDriver.getNumChannelsInput(); i ++)
			{
				AsioChannel input = asioDriver.getChannelInput(i);
				log.verbose("Loaded input channel " + i + ", '" + input.getChannelName() + "'");
				inputs.add(input);
			}
				
			for (int i = 0; i < asioDriver.getNumChannelsOutput(); i ++)
			{
				AsioChannel output = asioDriver.getChannelOutput(i);
				log.verbose("Loaded output channel " + i + ", '" + output.getChannelName() + "'");
				outputs.add(output);
			}
			
			log.info("Device '" + asioDriver.getName() + "' has " + inputs.size()
					+ " inputs and " + outputs.size() + " outputs");
		}
	}
	
	public AudioInterface(String driverName)
	{
		this(AsioDriver.getDriver(driverName));
	}
	
	public void release()
	{
		if (asioDriver != null)
		{
			log.info("Releasing and unloading device '" + asioDriver.getName() + "'");
			asioDriver.shutdownAndUnloadDriver();
		}
	}
	
	public void setSampleRate(double sampleRate)
	{
		log.info("Setting device sample rate to " + (sampleRate / 1000) + "kHz");
		asioDriver.setSampleRate(sampleRate);
	}
	
	public String getName()
	{
		return asioDriver.getName();
	}
	
	public double getSampleRate()
	{
		return asioDriver.getSampleRate();
	}
	
	public int getBufferSize()
	{
		return asioDriver.getBufferPreferredSize();
	}
	
	public void createAllBuffers()
	{
		Set<AsioChannel> active = new HashSet<>();
		active.addAll(inputs);
		active.addAll(outputs);
		
		log.info("Creating buffers for " + active.size() + " channels");
		asioDriver.createBuffers(active);
	}
	
	public void start()
	{
		log.info("Starting device ASIO driver");
		asioDriver.start();
	}
}
