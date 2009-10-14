package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class AngleTest extends Activity
{
	private AngleSurfaceView mView = null; //El View donde se pintará todo
	private AngleSpriteRenderer mSprites = null; //Único renderizador que se usará en este ejemplo
	private MyGameEngine mGame = null; //Motor de juego

	class Pelota extends AngleSimpleSprite //Sobrecarga del sprite básico para que tenga velocidad
	{
		public float vX;
		public float vY;

		public Pelota()
		{
			super(14, 14, R.drawable.pelota, 0, 0, 14, 14);
			vX = 0;
			vY = 0;
		}
	}
	
	class Bola  extends AngleSprite
	{
		boolean isFocused;
		boolean isSelected;
		public Bola()
		{
			super(34, 34, R.drawable.ball, 0, 0, 34, 34);
			isFocused=false;
			isSelected=false;
		}
		
		@Override
		public void draw(GL10 gl)
		{
			super.draw(gl);
		}
	}

	class MyGameEngine implements Runnable //Motor de juego
	{
		//máquina de estados
		private static final int smLoad = 1;
		private static final int smMove = 2;
		private static final int MAX_PELOTAS = 0;
		private static final int MAX_BOLAS = 100;
		private int mStateMachine = 0;
		
		//sprites
		private Pelota[] pelota=new Pelota[MAX_PELOTAS];
		private AngleSprite nave=null;

		private Bola[] bolas=new Bola[MAX_BOLAS];
		
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
					Log.v("FPS", "" + (100.f / ((CTM - lCTM) / 1000.f)));
				lCTM = CTM;
			}
			//-----------------------

			switch (mStateMachine)
			{
				case smLoad:
					if (AngleRenderEngine.mWidth > 0			//Cargará una vez que se haya inicializado todo el motor gráfico
							&& AngleRenderEngine.mHeight > 0)//Puede cargarse antes. Pero como uso las dimensiones.... 
					{
						for (int t = 0; t < MAX_BOLAS; t++)
						{
							bolas[t] = new Bola();
							bolas[t].mX = (float) (Math.random()
									* AngleRenderEngine.mWidth - bolas[t].mWidth) + bolas[t].mWidth;
							bolas[t].mY = (float) (Math.random()
									* AngleRenderEngine.mHeight - bolas[t].mHeight) + bolas[t].mHeight;
							mSprites.addSprite(bolas[t]);
						}
						
						nave = new AngleSprite(54, 29, R.drawable.tortuga, 0, 0, 54, 29);
						nave.mY = AngleRenderEngine.mHeight - nave.mHeight-100;
						mSprites.addSprite(nave); //Al incluir la nave en el renderizador mSprites, se pintará sola
						for (int t = 0; t < MAX_PELOTAS; t++)
						{
							pelota[t] = new Pelota();
							pelota[t].mX = (float) (Math.random()
									* AngleRenderEngine.mWidth - pelota[t].mWidth - 20) + 10;
							pelota[t].mY = (float) (Math.random()
									* AngleRenderEngine.mHeight - pelota[t].mHeight - 20) + 10;
							pelota[t].vX = (float) (Math.random() * 300) - 150;
							pelota[t].vY = (float) (Math.random() * 300) - 150;
							mSprites.addSprite(pelota[t]); //lo mismo con la pelota(s)
						}
						AngleRenderEngine.loadTextures(); //Como he cargado sprites nuevos con el motor activo. He de forzar a que las texturas vuelvan a cargarse
						mStateMachine = smMove;
					}
					break;
				case smMove: //movimiento básico
				{
					for (int t = 0; t < MAX_PELOTAS; t++)
					{
					float dX = pelota[t].vX * AngleRenderEngine.secondsElapsed;
					float dY = pelota[t].vY * AngleRenderEngine.secondsElapsed;
					pelota[t].mX += dX;
					pelota[t].mY += dY;
					if (((pelota[t].vX > 0) && (pelota[t].mX + pelota[t].mWidth > AngleRenderEngine.mWidth))
							|| ((pelota[t].vX < 0) && (pelota[t].mX < 0)))
					{
						pelota[t].vX = -pelota[t].vX;
						pelota[t].mX += dX;
					}
					if (((pelota[t].vY > 0) && (pelota[t].mY + pelota[t].mHeight > AngleRenderEngine.mHeight))
							|| ((pelota[t].vY < 0) && (pelota[t].mY < 0)))
					{
						pelota[t].vY = -pelota[t].vY;
						pelota[t].mY += dY;
					}
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
			mGame.nave.mX = event.getX();
		
		for (int t=0;t<MyGameEngine.MAX_BOLAS;t++)
		{
			mGame.bolas[t].isFocused=false;
			float X=event.getX();
			float Y=event.getY();
			if ((X>mGame.bolas[t].mX)&&(Y>mGame.bolas[t].mY)&&(X<mGame.bolas[t].mX+mGame.bolas[t].mWidth)&&(Y<mGame.bolas[t].mY+mGame.bolas[t].mHeight))
			{
				if (event.getPressure()>0.3f)
					mGame.bolas[t].isSelected=true;
				else
					mGame.bolas[t].isFocused=true;
			}
		}

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
		AngleRenderEngine.onDestroy(); //Destruyo el motor de renderizado
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