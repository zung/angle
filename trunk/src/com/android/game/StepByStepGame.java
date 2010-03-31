package com.android.game;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.FPSCounter;

//PASO 1:
//Lo 1º que debemos hacer es crear la Activity de nuestro juego derivándola de AngleActivity.
//Con esto conseguiremos que el motor haga por si solo muchas de las cosas que necesitaremos.
/** 
* @author Ivan Pajuelo
*/
public class StepByStepGame extends AngleActivity
{
	//PASO 2:
	//En lugar de trabajar directamente sobre la Activity, crearemos dos interfaces de usuario 
	//derivándolas de AngleUI. Una para el juego y otra para el menú
	//De esta forma veremos como cambiar instantáneamente de una a otra.
	GameUI mTheGame;
	MenuUI mTheMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//Esta linea es opcional. Añade un objeto FPSCounter directamente a la View principal
		//Así podremos ver el rendimiento de nuestro juego a través de LogCat
		mGLSurfaceView.addObject(new FPSCounter());

		//PASO 3:
		//Importante. No olvidemos instanciar los objetos (esto debería ser obvio)
		mTheGame=new GameUI(this);
		mTheMenu=new MenuUI(this);

		//PASO 4:
		//Establecemos la interface de usuario activa. En este caso el menú.
		setUI(mTheMenu);
		
		//PASO 5:
		//Todo el motor corre sobre una View principal que crea AngleActivity (mGLSurfaceView)
		//Aún así, en lugar de usar esta View directamente, la insertaremos dentro de un 
		//FrameLayout por si queremos añadir Views de las API.
		FrameLayout mainLayout=new FrameLayout(this);
		mainLayout.addView(mGLSurfaceView);
		setContentView(mainLayout);
		
		//(continua en MenuUI)
	}

}
