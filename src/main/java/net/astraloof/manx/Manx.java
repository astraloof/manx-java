package net.astraloof.manx;

import java.io.IOException;

import net.astraloof.manx.engine.AudioEngine;
import net.astraloof.manx.route.Channel;
import net.astraloof.manx.route.InputReceive;
import net.astraloof.manx.route.OutputChannel;

public class Manx
{
	public static final Manx instance = new Manx();
	public static final String APP_NAME = "Manx";
	public static final String APP_VERS = "1.0.0-M1";
	private Logger log = new Logger(Manx.class);
	
	public AudioEngine engine;
	
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
		engine.device.getAsioDriver().openControlPanel();
		
		engine.outputs = new OutputChannel[engine.outputBuffers.length];
		for (int i = 0; i < engine.outputs.length; i ++)
			engine.outputs[i] = new OutputChannel(i);
		engine.channels = new Channel[engine.inputBuffers.length];
		
		for (int i = 0; i < engine.channels.length; i ++)
		{
			engine.channels[i] = new Channel();
			engine.channels[i].receives.add(new InputReceive(i));
			for (OutputChannel target : engine.outputs)
				engine.channels[i].createSend(target);
		}
		
		try
		{
			log.info("ENTERING ETERNAL STASUS, enter anything to end");
			System.in.read();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		engine.halted = true;
		engine.device.release();
		log.info("Bye-bye");
	}
}
