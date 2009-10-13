package com.android.angle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class AngleTest extends Activity
{
	private AngleSurfaceView mView = null; //El View donde se pintará todo
	private AngleSpriteRenderer mSprites = null; //Único renderizador que se usará en este ejemplo
	private MyGameEngine mGame = null; //Motor de juego

	class Pelota extends AngleSprite //Sobrecarga del sprite básico para que tenga velocidad
	{
		public float vX;
		public float vY;

		public Pelota()
		{
			super(34, 34, R.drawable.ball, 0, 0, 34, 34);
			vX = 0;
			vY = 0;
		}
	}

	class MyGameEngine implements Runnable //Motor de juego
	{
		//máquina de estados
		private static final int smLoad = 1;
		private static final int smMove = 2;
		private int mStateMachine = 0;
		
		//sprites
		private Pelota pelota=null;
		private AngleSprite nave=null;
		
		//contador fps
		private int frameCount = 0;
		private long lCTM = 0;

		MyGameEngine()
		{
		}

		public void Start()
		{
			mStateMachine = smLoad;
		}

		public void run()
		{
			//--------FPS------------
			frameCount++;
			if (frameCount >= 100)
			{
				long CTM = System.currentTimeMillis();
				frameCount = 0;
				if (lCTM > 0)
					Log.v("FPS", String.valueOf(100.f / ((CTM - lCTM) / 1000.f)));
				lCTM = CTM;
			}
			//-----------------------

			switch (mStateMachine)
			{
				case smLoad:
					if (AngleRenderEngine.mWidth > 0			//Cargará una vez que se haya inicializado todo el motor gráfico
							&& AngleRenderEngine.mHeight > 0)//Puede cargarse antes. Pero como uso las dimensiones.... 
					{
						nave = new AngleSprite(34, 34, R.drawable.ball, 0, 0, 34, 34);
						nave.mY = AngleRenderEngine.mHeight - nave.mHeight;
						mSprites.addSprite(nave); //Al incluir la nave en el renderizador mSprites, se pintará sola
						for (int t = 0; t < 5; t++)
						{
							pelota = new Pelota();
							pelota.mX = (float) (Math.random()
									* AngleRenderEngine.mWidth - 20) + 10;
							pelota.mY = (float) (Math.random()
									* AngleRenderEngine.mHeight - 20) + 10;
							pelota.vX = (float) (Math.random() * 300) - 150;
							pelota.vY = (float) (Math.random() * 300) - 150;
							mSprites.addSprite(pelota); //lo mismo con la pelota(s)
						}
						AngleRenderEngine.loadTextures(); //Como he cargado sprites nuevos con el motor activo. He de forzar a que las texturas vuelvan a cargarse
						mStateMachine = smMove;
					}
					break;
				case smMove: //movimiento básico
				{
					float dX = pelota.vX * AngleRenderEngine.secondsElapsed;
					float dY = pelota.vY * AngleRenderEngine.secondsElapsed;
					pelota.mX += dX;
					pelota.mY += dY;
					if (((pelota.vX > 0) && (pelota.mX + pelota.mWidth > AngleRenderEngine.mWidth))
							|| ((pelota.vX < 0) && (pelota.mX < 0)))
					{
						pelota.vX = -pelota.vX;
						pelota.mX += dX;
					}
					if (((pelota.vY > 0) && (pelota.mY + pelota.mHeight > AngleRenderEngine.mHeight))
							|| ((pelota.vY < 0) && (pelota.mY < 0)))
					{
						pelota.vY = -pelota.vY;
						pelota.mY += dY;
					}
					break;
				}
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mGame = new MyGameEngine(); //creo un motor de juego propio

		mSprites = new AngleSpriteRenderer();
		AngleRenderEngine.addRenderer(mSprites); //Añado un renderizador para sprites
		
		mView = new AngleSurfaceView(this);
		mView.setBeforeDraw(mGame); //Aqui le digo a la view, que Runnable.run() ha de ejecutar antes de cada frame 
		setContentView(mView);

		mGame.Start(); //Inicia el tema
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//Esto evita que la aplicación sea inundada con mensajes.
		//Máximo unos 62 por segundo (como los FPS máximos)
		try
		{
			Thread.sleep(16); 
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		//------------------------------------------------------
		
		if (mGame.nave!=null) //Posiciona la nave
			mGame.nave.mX = event.getX() - mGame.nave.mWidth / 2;
		
		return super.onTouchEvent(event);
	}

	@Override
	protected void onPause()
	{
		Log.d("AngleTest", "Pause");
		mView.onPause(); //He de informar al view que ha habido una pausa
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		Log.d("AngleTest", "Resume");
		mView.onResume();//He de informar al view que la pausa ha acabado
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		Log.d("AngleTest", "Destroy");
		AngleRenderEngine.shutdown(); //Destruyo el motor de renderizado
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		Log.d("AngleTest", "SaveInstanceState");
	}

	@Override
	protected void onRestart()
	{
		Log.d("AngleTest", "Restart");
		super.onRestart();
	}

	@Override
	protected void onStart()
	{
		Log.d("AngleTest", "Start");
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		Log.d("AngleTest", "Stop");
		super.onStop();
	}

}