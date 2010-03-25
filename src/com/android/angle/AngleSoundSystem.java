package com.android.angle;

import java.io.IOException;
import java.lang.reflect.Field;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class AngleSoundSystem
{
	private static final boolean isSoundDisabled = false;
	private static final boolean isMusicDisabled = false;
	private static final int sMusicStream = AudioManager.STREAM_MUSIC;
	private static final int sMaxSounds = 3;
	private Activity mActivity;
	public float roMusicVolume;
	private MediaPlayer mMusicPlayer;
	private MediaPlayer[] mSoundPlayer;
	private int mCurrentSound;
	private int[] mResID;

	public AngleSoundSystem(Activity activity)
	{
		mActivity = activity;
		mSoundPlayer = new MediaPlayer[sMaxSounds];
		mResID = new int[sMaxSounds];
		try
		{
			Thread.sleep(100);
			for (int s = 0; s < sMaxSounds; s++)
				mSoundPlayer[s] = new MediaPlayer();
			mMusicPlayer = null;
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void delete()
	{
		try
		{
			stopMusic();
			for (int s = 0; s < sMaxSounds; s++)
				stopSound(mResID[s]);
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		java.lang.System.gc();
	}

	public void setMusicVolume(float volume)
	{
		if (!isMusicDisabled)
		{
			if (mMusicPlayer != null)
			{
				roMusicVolume = volume;
				if (roMusicVolume > 1)
					roMusicVolume = 1;
				if (roMusicVolume < 0)
					roMusicVolume = 0;
				mMusicPlayer.setVolume(roMusicVolume, roMusicVolume);
			}
		}
	}

	public void playMusic(String fileName, float volume, boolean loop)
	{
		if (!isMusicDisabled)
		{
			try
			{
				if (mMusicPlayer != null)
					stopMusic();
				mMusicPlayer = new MediaPlayer();
				if (mMusicPlayer != null)
				{
					AssetFileDescriptor afd = mActivity.getAssets().openFd(fileName);
					mMusicPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
					mMusicPlayer.prepare();
					roMusicVolume = volume;
					mMusicPlayer.setAudioStreamType(sMusicStream);
					mMusicPlayer.setVolume(roMusicVolume, roMusicVolume);
					mMusicPlayer.setLooping(loop);
					mMusicPlayer.start();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void playMusic(int resId, float volume, boolean loop)
	{
		if ((!isMusicDisabled) && (resId > 0))
		{
			if (mMusicPlayer != null)
				stopMusic();
			mMusicPlayer = MediaPlayer.create(mActivity, resId);
			if (mMusicPlayer != null)
			{
				roMusicVolume = volume;
				mMusicPlayer.setAudioStreamType(sMusicStream);
				mMusicPlayer.setVolume(roMusicVolume, roMusicVolume);
				mMusicPlayer.setLooping(loop);
				mMusicPlayer.start();
			}
		}
	}

	public void stopMusic()
	{
		if (mMusicPlayer != null)
		{
			mMusicPlayer.stop();
			mMusicPlayer.release();
			mMusicPlayer = null;
		}
	}

	public void playSound(int resId, float volume, boolean loop)
	{
		if ((!isSoundDisabled) && (resId > 0))
		{
			if (mSoundPlayer[mCurrentSound] != null)
				stopSound(mResID[mCurrentSound]);
			mResID[mCurrentSound] = resId;
			mSoundPlayer[mCurrentSound] = MediaPlayer.create(mActivity, mResID[mCurrentSound]);
			if (mSoundPlayer[mCurrentSound] != null)
			{
				mSoundPlayer[mCurrentSound].setVolume(volume, volume);
				mSoundPlayer[mCurrentSound].setLooping(loop);
				mSoundPlayer[mCurrentSound].start();
			}
			mCurrentSound++;
			mCurrentSound %= sMaxSounds;
		}
	}

	public void stopSound(int resId)
	{
		int s = 0;
		for (; s < sMaxSounds; s++)
			if (mResID[s] == resId)
				break;

		if (s < sMaxSounds)
		{
			if (mSoundPlayer[s] != null)
			{
				if (mSoundPlayer[s].isPlaying())
					mSoundPlayer[s].stop();
				mSoundPlayer[s].release();
				mSoundPlayer[s] = null;
			}
		}
	}

	public void pauseMusic()
	{
		if (mMusicPlayer != null)
			mMusicPlayer.pause();
	}

	public void resumeMusic()
	{
		if (mMusicPlayer != null)
			mMusicPlayer.start();
	}

	public void playSound(int resId)
	{
		playSound(resId, 1, false);
	}

	public void playSound(int resId, float volume)
	{
		playSound(resId, volume, false);
	}
};
