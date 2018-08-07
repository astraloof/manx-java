package net.astur.hotlynx;

import java.io.IOException;

import com.synthbot.jasiohost.AsioChannel;

import net.astur.hotlynx.engine.AudioEngine;

public class HotLynx
{
	public static final HotLynx instance = new HotLynx();
	private Logger log = new Logger(HotLynx.class);
	
	AsioChannel outL, outR;
	AudioEngine engine;
	
	public static void main(String[] args)
	{
		System.setProperty("java.library.path", "./natives");
		instance.start(args);
	}
	
	public void start(String[] args)
	{
		log.info("HotLynx 1.0.0-SNAPSHOT");
		log.info("Initializing audio engine");
		
		engine = new AudioEngine();
		String[] driverNames = engine.getDriverNames();
		engine.setDevice(driverNames[1]);
		engine.initialize();
		engine.getDevice().getAsioDriver().openControlPanel();
		
		try
		{
			log.info("ENTERING ETERNAL STASUS, enter anything to end");
			System.in.read();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		engine.getDevice().release();
		log.info("Bye-bye");
	}
}
