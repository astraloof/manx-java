package net.astraloof.manx.engine;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverListener;

import net.astraloof.manx.Logger;

public class AudioEngine implements AsioDriverListener
{
	private Logger log = new Logger(AudioEngine.class);
	
	private AudioInterface device = null;
	float[][] inputBuffers, outputBuffers;
	
	public AudioEngine()
	{
		
	}
	
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
	
	public AudioInterface getDevice()
	{
		return device;
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
		List<AsioChannel> ins = device.getInputs();
		List<AsioChannel> outs = device.getOutputs();
		
		double bS = device.getBufferSize();
		for (int i = 0; i < ins.size(); i ++)
			ins.get(i).read(inputBuffers[i]);
		for (int buf = 0; buf < bS; buf ++)
			for (int in = 0; in < ins.size(); in ++)
				outputBuffers[0][buf] += inputBuffers[in][buf];
		for (int i = 0; i < outs.size(); i ++)
			outs.get(i).write(outputBuffers[0]);
		for (int buf = 0; buf < bS; buf ++)
			outputBuffers[0][buf] = 0.0f;
	}
}
