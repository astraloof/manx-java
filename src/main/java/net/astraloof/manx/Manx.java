package net.astraloof.manx;

import java.io.IOException;

import com.synthbot.jasiohost.AsioChannel;

import net.astraloof.manx.engine.AudioEngine;

public class Manx
{
	public static final Manx instance = new Manx();
	public static final String APP_NAME = "Manx";
	public static final String APP_VERS = "1.0.0-M1";
	private Logger log = new Logger(Manx.class);
	
	AsioChannel outL, outR;
	AudioEngine engine;
	
	public static void main(String[] args)
	{
//		System.setProperty("java.library.path", "natives");
		System.setProperty("sun.java2d.opengl", "true");
		instance.start(args);
	}
	
	public void start(String[] args)
	{
		log.info(APP_NAME + " " + APP_VERS);
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
