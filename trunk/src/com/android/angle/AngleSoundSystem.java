package com.android.angle;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Sounds and music manager
 * @author Ivan Pajuelo
 *
 */
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
	private float mVolumeTo;
	private float mVolumeChangeSpeed;

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

	public void setMusicVolume(float volume, float time)
	{
		mVolumeTo=volume;
		if (time>0)
			mVolumeChangeSpeed=Math.abs(roMusicVolume-mVolumeTo)/time;
		else
			setMusicVolume(volume);
	}
	
	private void setMusicVolume(float volume)
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
				if (mMusicPlayer.isPlaying())
				{
					if (roMusicVolume>0)
						mMusicPlayer.setVolume(roMusicVolume, roMusicVolume);
					else
						stopMusic();
				}
			}
		}
	}

	/**
	 * Play music from asset
	 * @param fileName asset file name
	 * @param volume normalized volume
	 * @param loop infinite loop
	 */
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
					mVolumeTo=volume;
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
	/**
	 * Play music from raw resource
	 * @param resId resource id
	 * @param volume normalized volume 
	 * @param loop infinite loop
	 */
	public void playMusic(int resId, float volume, boolean loop)
	{
		if ((!isMusicDisabled) && (resId > 0))
		{
			if (mMusicPlayer != null)
				stopMusic();
			mMusicPlayer = MediaPlayer.create(mActivity, resId);
			if (mMusicPlayer != null)
			{
				mVolumeTo=volume;
				roMusicVolume = volume;
				mMusicPlayer.setAudioStreamType(sMusicStream);
				mMusicPlayer.setVolume(roMusicVolume, roMusicVolume);
				mMusicPlayer.setLooping(loop);
				mMusicPlayer.start();
				while (!mMusicPlayer.isPlaying());
			}
		}
	}

	public void stopMusic()
	{
		if (mMusicPlayer != null)
		{
			mMusicPlayer.stop();
			while (mMusicPlayer.isPlaying());
			mMusicPlayer.release();
			mMusicPlayer = null;
		}
	}

	public void seekMusic(int pSeek)
	{
		if (mMusicPlayer != null)
			mMusicPlayer.seekTo(pSeek);
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

	/**
	 * Play sound from raw resource
	 * @param resId resource id
	 * @param volume normalized volume 
	 * @param loop infinite loop
	 */
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

	/**
	 * stop specific sound
	 * @param resId resource id
	 */
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
				mResID[s]=0;
			}
		}
	}

	/**
	 * Play sound from raw resource at maximum volume with no loop
	 * @param resId resource id
	 */
	public void playSound(int resId)
	{
		playSound(resId, 1, false);
	}

	/**
	 * Play sound from raw resource with no loop
	 * @param resId resource id
	 * @param volume normalized volume 
	 */
	public void playSound(int resId, float volume)
	{
		playSound(resId, volume, false);
	}

	public boolean isMusicPlaying()
	{
		if (!isMusicDisabled) 
		{
			if (mMusicPlayer != null)
				return mMusicPlayer.isPlaying();
		}
		return false;
	}
	
	public void step (float secondsElapsed)
	{
		if (roMusicVolume<mVolumeTo)
		{
			roMusicVolume+=secondsElapsed*mVolumeChangeSpeed;
			if (roMusicVolume>mVolumeTo)
				roMusicVolume=mVolumeTo;
			setMusicVolume(roMusicVolume);
		}
		else if (roMusicVolume>mVolumeTo)
		{
			roMusicVolume-=secondsElapsed*mVolumeChangeSpeed;
			if (roMusicVolume<mVolumeTo)
				roMusicVolume=mVolumeTo;
			setMusicVolume(roMusicVolume);
		}
	}

};
