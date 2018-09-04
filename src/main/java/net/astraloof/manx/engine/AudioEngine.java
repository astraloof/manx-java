package net.astraloof.manx.engine;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverListener;

import net.astraloof.manx.Logger;
import net.astraloof.manx.route.Channel;
import net.astraloof.manx.route.OutputChannel;

public class AudioEngine implements AsioDriverListener
{
	private Logger log = new Logger(AudioEngine.class);
	
	public AudioInterface device = null;
	public float[][] inputBuffers, outputBuffers;
	public boolean halted = false;
	
	public OutputChannel[] outputs;
	public Channel[] channels;
	
	public void setDevice(String deviceName)
	{
		if (device != null)
			device.release();
		log.log(Level.INFO, "Switching to device '" + deviceName + "'");
		device = new AudioInterface(deviceName);
		
		if (device.getAsioDriver() == null)
			log.log(Level.WARNING, "Device driver failed to load");
		else
		{
			device.getAsioDriver().addAsioDriverListener(this);
//			device.createAllBuffers();
//			device.start();
		}
	}
	
	public String[] getDriverNames()
	{
		log.log(Level.INFO, "Searching for ASIO audio drivers");
		String[] driverNames = AsioDriver.getDriverNames().toArray(new String[0]);
		log.logArray(Level.INFO, "Discovered " + driverNames.length + " ASIO drivers", driverNames);
		return driverNames;
	}
	
	public void initialize()
	{
		device.getAsioDriver().addAsioDriverListener(this);
		device.createAllBuffers();
		device.start();
		
		int preferred = device.getBufferSize();
		inputBuffers = new float[device.getInputs().size()][preferred];
		outputBuffers = new float[device.getOutputs().size()][preferred];
	}
	
	public void setSampleRate(double sampleRate)
	{
		log.info("Setting engine sample rate to " + (sampleRate / 1000) + "kHz");
		device.setSampleRate(sampleRate);
	}

	@Override
	public void sampleRateDidChange(double sampleRate)
	{
		log.info("Sample rate change detected");
		setSampleRate(sampleRate);
	}

	@Override
	public void resetRequest()
	{
		log.info("Reset request received, resetting engine");
		setDevice(device.getAsioDriver().getName());
		initialize();
	}

	@Override
	public void resyncRequest()
	{
		
	}

	@Override
	public void bufferSizeChanged(int bufferSize)
	{
		log.info("Buffer size change detected");
	}

	@Override
	public void latenciesChanged(int inputLatency, int outputLatency)
	{
		log.info("Input/output latency change detected");
	}

	@Override
	public void bufferSwitch(long sampleTime, long samplePosition, Set<AsioChannel> activeChannels)
	{
		if (!halted)
		{
			List<AsioChannel> ins = device.getInputs();
			List<AsioChannel> outs = device.getOutputs();
			
			// Swap input buffers
			for (int i = 0; i < ins.size(); i ++)
				ins.get(i).read(inputBuffers[i]);
			
			// Recursive mix (replace with channel mixing)
//			for (int buf = 0; buf < bS; buf ++)
//				for (int in = 0; in < ins.size(); in ++)
//					outputBuffers[0][buf] += inputBuffers[in][buf];
			for (OutputChannel output : outputs)
				output.promptMix(samplePosition);
			
			// Swap output buffers
			for (int i = 0; i < outs.size(); i ++)
			{
				outs.get(i).write(outputBuffers[i]);
//				for (int buf = 0; buf < bS; buf ++)
//					outputBuffers[i][buf] = 0.0f;
			}
		}
	}
}
