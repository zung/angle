package com.android.angle;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class AngleSoundSystem
{
	private static final boolean isMusicDisabled = true;
	private Activity mActivity;
	private MediaPlayer mMediaPlayer;
	private SoundPool mSoundPool;

	public AngleSoundSystem(Activity activity)
	{
		try
		{
			Thread.sleep(100);
			mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
			mMediaPlayer = null;
			mActivity = activity;
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
			Thread.sleep(100);
			if (mSoundPool != null)
			{
				mSoundPool.release();
				mSoundPool = null;
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		java.lang.System.gc();
	}

	public void playMusic(String fileName, float volume, boolean loop)
	{
		if (!isMusicDisabled)
		{
			try
			{
				if (mMediaPlayer != null)
					stopMusic();
				mMediaPlayer = new MediaPlayer();
				if (mMediaPlayer != null)
				{
					AssetFileDescriptor afd = mActivity.getAssets().openFd(fileName);
					mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
					mMediaPlayer.prepare();
					mMediaPlayer.setVolume(volume, volume);
					mMediaPlayer.setLooping(loop);
					mMediaPlayer.start();
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
		if (!isMusicDisabled)
		{
			if (mMediaPlayer != null)
				stopMusic();
			mMediaPlayer = MediaPlayer.create(mActivity, resId);
			if (mMediaPlayer != null)
			{
				mMediaPlayer.setVolume(volume, volume);
				mMediaPlayer.setLooping(loop);
				mMediaPlayer.start();
			}
		}
	}

	public void stopMusic()
	{
		if (mMediaPlayer != null)
		{
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public void pauseMusic()
	{
		if (mMediaPlayer != null)
			mMediaPlayer.pause();
	}

	public void resumeMusic()
	{
		if (mMediaPlayer != null)
			mMediaPlayer.start();
	}

	public int loadSound(int resId)
	{
		if (mSoundPool != null)
			return mSoundPool.load(mActivity, resId, 0);
		return -1;
	}

	public int loadSound(String fileName, int priority)
	{
		int id = -1;
		try
		{
			if (mSoundPool != null)
				id = mSoundPool.load(mActivity.getAssets().openFd(fileName), priority);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return id;
	}

	public void playSound(int id)
	{
		if (mSoundPool != null)
			mSoundPool.play(id, 1, 1, 0, 0, 1);
	}

	public void playSound(int id, float leftVolume, float rightVolume, int priority, int loop, float rate)
	{
		if (mSoundPool != null)
			mSoundPool.play(id, leftVolume, rightVolume, priority, loop, rate);
	}

	public void unloadSound(int soundID)
	{
		if (mSoundPool != null)
			mSoundPool.unload(soundID);
	}
};
