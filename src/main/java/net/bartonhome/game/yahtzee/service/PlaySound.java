package net.bartonhome.game.yahtzee.service;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlaySound {
	String RESOURCES = "/home/rob/workspace.luna/yahtzee/src/main/resources/";
	private AudioInputStream woohoo;
	private AudioInputStream rollDice;
	private AudioInputStream highScore;
	
	public static void main(String[] args) {
		PlaySound sound = new PlaySound();
		
		while(true){
		//sound.woohoo();
		sound.rollDice();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	public PlaySound () {
		woohoo = createAudioStream ("woohoo.wav");
		rollDice = createAudioStream ("diceThrowShort.wav");
		highScore = createAudioStream ("highScore.wav");
		 
		
	}
	
	private AudioInputStream createAudioStream(String filename) {
		try {
			URL url = this.getClass().getClassLoader().getResource(filename);
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			AudioFormat littleEndianFormat = getAudioFormat();
			AudioInputStream stream = AudioSystem.getAudioInputStream(littleEndianFormat, ais);
			stream.mark(0);
			return stream;
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		int sampleInbits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
	}
	
	public void highScore() {
		playSound("highScore.wav");
		//playSound(highScore);
		//highScore.run();
	}

	public void rollDice() {
		playSound("diceThrowShort.wav");
		//playSound(rollDice);
		//rollDice.run();
	}

	public void woohoo() {
		playSound("woohoo.wav");
		//playSound(woohoo);
		//woohoo.run();
	}
	
	public Thread createSoundThread(final String filename) {
	  return new Thread(new Runnable() {
	    public void run() {
	      try {
	      	URL url = this.getClass().getClassLoader().getResource(filename);
	        Clip clip = AudioSystem.getClip();
	        AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	        clip.open(audioIn);
	        clip.start();
	      } catch (Exception e) {
	        System.err.println(e.getMessage());
	      }
	    }
	  });
	}

	public synchronized void playSound(final AudioInputStream audioIn) {
	  new Thread(new Runnable() {
	    public void run() {
	      try {
	      	
	      	Clip clip = AudioSystem.getClip();
	        clip.open(audioIn);
	        clip.start();
	      } catch (Exception e) {
	        System.err.println(e.getMessage());
	      }
	    }
	  }).start();
	}
	
	public synchronized void playSound(final String filename) {
	  new Thread(new Runnable() {
	    public void run() {
	      try {
	      	//URL url = this.getClass().getClassLoader().getResource(filename);
	        Clip clip = AudioSystem.getClip();
	        //AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	        AudioInputStream audioIn = createAudioStream(filename);
	        clip.open(audioIn);
	        clip.start();
	        do {
	        	Thread.sleep(20);
	        } while  (clip.isActive());
	        //System.out.println("Done clip " + filename);
	        clip.close();
	        
	      } catch (Exception e) {
	        System.err.println(e.getMessage());
	      }
	    }
	  }).start();
	}
}
