package dev.spiderdlv2;

import java.awt.Image;
import java.awt.Rectangle;

public class Carta extends Rectangle 
{
	private Image immagineCarta;
	public int seme;
	public int valore;
	public boolean isFaceDown;

	 public Carta(Image immagineCarta, int seme, int valore, boolean isFaceDown) 
	 {
	    super();
	    this.immagineCarta = immagineCarta;
	    this.seme = seme;
	    this.valore = valore ;
	    this.isFaceDown = isFaceDown;
	 }
	 
	 
	 public void flip() 
	 {
	     isFaceDown = !isFaceDown;
	 }

	 public Image getImage() 
	 {
	     return immagineCarta;
	 }

	 public int getSeme() 
	 {
	     return seme;
	 }

	 public int getValore()
	 {
	     return valore;
	 }

	 public boolean isFaceDown() 
	 {
	      return isFaceDown;
	 }

	 @Override
	 public Object clone()
	 {
	     Carta copy = new Carta(immagineCarta, seme, valore, isFaceDown);

	     copy.x = x;
	     copy.y = y;
	     copy.width = width;
	     copy.height = height;

	     return copy;
	  }
	 

}
