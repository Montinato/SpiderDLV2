package dev.spiderdlv2;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Stato 
{	
	public List<Carta> carte; // also it's a junk pile
	
	
    public List<Carta>[] piles;
    public List<Carta>[] deck;
    
    private boolean flag;

    public int top; // pointer to top
    public int ptr;

    public Stato() {
		// TODO Auto-generated constructor stub
		flag = true;
	}

    public Stato(List<Carta> carte, List<Carta>[] piles, List<Carta>[] deck, int top, int ptr) 
    {
        this.carte = new ArrayList<>();

        for (Carta card : carte) {
            this.carte.add((Carta) card.clone());
        }

        this.piles = new List[SpiderDLV2.tavolo.pile];

        for (int i = 0; i < SpiderDLV2.tavolo.pile; i++) {
            this.piles[i] = new ArrayList<>();

            for (Carta card : piles[i]) {
                this.piles[i].add((Carta) card.clone());
            }
        }

        this.deck = new List[SpiderDLV2.tavolo.mazzi];

        for (int i = 0; i < SpiderDLV2.tavolo.mazzi; i++) {
            if (deck[i] == null) {
                this.deck[i] = null;
            }
            else {
                this.deck[i] = new ArrayList<>();

                for (Carta card : deck[i]) {
                    this.deck[i].add((Carta) card.clone());
                }
            }
        }

        this.top = top;
        this.ptr = ptr;

        flag = false;
    }

	

	 public List<Carta> getCarte() {
	        return carte;
	    }

	    public List<Carta>[] getPile() {
	        return piles;
	    }

	    public List<Carta>[] getDeck() {
	        return deck;
	    }

	    public int getTop() {
	        return top;
	    }

	    public int getPtr() {
	        return ptr;
	    }

	    public boolean isFlagged() {
	        return flag;
	    }

}
