package com.android.game;

import android.graphics.Typeface;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;

/** 
* @author Ivan Pajuelo
*/
public class MenuUI extends AngleUI
{
	//STEP 6:
	//As we have said, the engine uses a main View. In this View we have added an UI (MenuUI)
	//And now in this UI, we will add more. (Do the same with the IU for the game)
	//The main characteristic of the UIs, is that there can be only one active
	//In this way, when we set one, what we do is to leave active only a branch of the object tree
	//Both for drawing and logic.
	//Now. Instead of adding all our objects directly to the UIs, it is more convenient to use
	//intermediate branches.
	//For this we use AngleObjects as groups of objects.	
	//PASO 6:
	//>Como ya hemos dicho, el motor usa una View principal. En esta View hemos añadido una UI (MenuUI)
	//>y ahora en esta UI añadiremos más cosas.
	//>Haremos lo mismo con la UI de juego. La gracia de las UIs, es que sólo hay una activa,
	//>de esta manera, cuando establecemos una, lo que hacemos es dejar activa sólo una de las ramas del árbol
	//>tanto para su dibujado como para su lógica.
	//>Ahora bien. En lugar de añadir directamente todos nuestros objetos a las UIs, es más conveniente usar
	//>ramas intermedias.
	//>Para eso usaremos AngleObjects como grupos de objetos.
	private AngleObject ogMenuTexts;
	
	//STEP 7:
	//We will create 3 strings for the menu
	//PASO 7:
	//Vamos a crear 3 textos para el menú 
	private AngleString strPlay;
	private AngleString strHiScore;
	private AngleString strExit;

	private int mHiScore;

	public MenuUI(AngleActivity activity)
	{
		super(activity);
		ogMenuTexts = new AngleObject();

		//PASO 8:
		//Ya que estamos, añadiremos también una imagen de fondo
		//Esta no es la forma más clara, pero si es la más rápida de añadir un objeto (en este caso un sprite)
		addObject(new AngleSprite(160, 240, new AngleSpriteLayout(mActivity.mGLSurfaceView, 320, 480, com.android.tutorial.R.drawable.bg_menu)));
		//Por partes:
		//AddObject añade un objeto. en este caso a this y además devuelve el objeto añadido (más adelante veremos para que usarlo)
		//El objeto que estamos añadiendo es un AngleSprite y usamos su constructor (X, Y, AngleSpriteLayout)
		//El AngleSpriteLayout lo creamos también inline. Usando su 2º constructor más sencillo (como especificamos las dimensiones, no podemos usar el más simple)
		//En cualquier caso, siempre hay que pasarle la instancia de la view principal y el Drawable con la imagen
		
		//PASO 9:
		//Por encima del fondo, añadimos un grupo de objetos, donde pondremos el menú en si.
		//En el caso actual, no seria necesario, pero si más adelante queremos añadir otros objetos sobre el fondo y bajo los textos (sin tener que usar las Z) para conseguir algunos efectos visuales,
		//ya lo tenemos.
		addObject(ogMenuTexts);

		//PASO 10:
		//Creamos una fuente para nuestros textos. En este caso, la crearemos usando un ttf.
		//Altura=25, 222 caracteres, sin borde ni espacio entre caracteres, de color cyan y con el canal alfa al 100% 
		AngleFont fntCafe=new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(mActivity.getAssets(),"cafe.ttf"), 222, 0, 0, 30, 200, 255, 255);
		
		//PASO 11:
		//Creamos y añadimos los 3 textos del menú a ogMenuTexts
		//Ahora veremos la utilidad de que addObject devuelva el objeto añadido para hacerlo todo inline
		//Podríamos añadirlos antes del paso 9. No importa el orden en el que se construya el árbol
		//En el constructor del String, tenemos: la fuente que usa, la posición en pantalla de la linea de escritura 
		//(en este caso, al usar una alineación al centro, la posición en X, marca el centro) y la alineación horizontal
		strPlay = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Play", 160, 180, AngleString.aCenter));
		strHiScore = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Hi Score", 160, 210, AngleString.aCenter));
		strExit = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Exit", 160, 390, AngleString.aCenter));
		
		//La estructura por ahora nos queda así:
		//mGLSurfaceView
		// >mTheGame
		// >mTheMenu
		//   >sprBackground
		//   >ogMenuTexts
		//     >strPlay
		//     >strExit
	}

	//PASO 12:
	//Sobrecargamos el método onTouchEvent para responder cuando uno de los textos sea pulsado 
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			float eX = event.getX();
			float eY = event.getY();

			//PASO 13:
			//El método test de AngleString, comprueba si una posición determinada, está dentro de las dimensiones del texto 
			if (strPlay.test(eX, eY))
				//Si es así, en este caso, cambiamos la UI por la del juego
				((StepByStepGame) mActivity).setUI(((StepByStepGame) mActivity).mTheGame);
			else if (strExit.test(eX, eY))
				mActivity.finish();

			return true;
		}
		return false;
	}

	//PASO 14:
	//Sobrecargamos el callback onActivate para actualizar el HiScore 
	@Override
	public void onActivate()
	{
		if (((StepByStepGame) mActivity).mTheGame.mScore>mHiScore)
				mHiScore=((StepByStepGame) mActivity).mTheGame.mScore;
		strHiScore.set("Hi Score: "+mHiScore);
		super.onActivate();
	}
	
	//(continua en GameUI)
}
