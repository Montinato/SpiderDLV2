package dev.spiderdlv2.input;

import dev.spiderdlv2.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.spiderdlv2.Carta;
import dev.spiderdlv2.SpiderDLV2;
import dev.spiderdlv2.Stato;
import dev.spiderdlv2.Tavolo;

public class MouseManager implements MouseListener,MouseMotionListener
{
     @Override
    public void mousePressed(MouseEvent e) 
    {
    	System.out.println("MousePressed() OK ");
    	
    	
        int mouseX = e.getX();
        int mouseY = e.getY();

        SpiderDLV2.tavolo.prevMX = mouseX;
        SpiderDLV2.tavolo.prevMY = mouseY;

        int index = -1;
        int startFrom = -1;
        

          outer: for (int i = 0; i < SpiderDLV2.tavolo.pile; i++) {
            int carte = SpiderDLV2.tavolo.piles[i].size();

            for (int j = carte - 1; j >= 0; j--) {
                Carta card = SpiderDLV2.tavolo.piles[i].get(j);

                if (card.contains(mouseX, mouseY)) {
                    index = i;
                    startFrom = j;
                    break outer;
                }
            }
            
           
        }
        
        

        if (index != -1) {
            if (SpiderDLV2.tavolo.piles[index].get(startFrom).isFaceDown()) {
            	
            	System.out.println("MouseManager1 -> repaint()");
                return;
            }

            List<Carta> touched = SpiderDLV2.tavolo.piles[index].subList(startFrom, SpiderDLV2.tavolo.piles[index].size())
                                            .stream()
                                            .collect(Collectors.toCollection(ArrayList::new)); // I know
                                                                                               // it's
                                                                                               // ugly but
                                                                                               // it
                                                                                               // resolves
                                                                                               // ConcurrentModificationException

            if (!SpiderDLV2.debug) {
                int seme = -1; // ensures that the selected carte have same semee
                int valore = -1; // ensures that the selected carte follow decreasing numerical order

                int size = SpiderDLV2.tavolo.piles[index].size() - startFrom;

                for (int i = 0; i < size; i++, valore--) {
                    if (seme == -1) {
                        seme = touched.get(i).getSeme();
                    }
                    if (seme == -1) {
                        seme = touched.get(i).getValore();
                    }
                    if (seme != touched.get(i).getSeme()) {
                        return;
                    }
                    if (valore != touched.get(i).getValore()) {
                        return;
                    }
                }
                
                System.out.println("MouseManager2 -> repaint()");
                
            }

            SpiderDLV2.tavolo.undoStack.push(new Stato(SpiderDLV2.tavolo.carte, SpiderDLV2.tavolo.piles, SpiderDLV2.tavolo.deck, SpiderDLV2.tavolo.top, SpiderDLV2.tavolo.ptr));
            SpiderDLV2.tavolo.piles[index].removeAll(touched);
            SpiderDLV2.tavolo.muoviPila = touched;
            
            for(Carta c : touched)
            {
            	System.out.println(c.seme);
            	System.out.println(c.valore);
            }
            
            SpiderDLV2.tavolo.index = index;
            
            
            System.out.println("MouseManager3 -> repaint()");
            SpiderDLV2.tavolo.repaint();
            
            
        }
        else if (SpiderDLV2.tavolo.top >= 0) {
        	
        	
        	
        	
            Carta topCarta = SpiderDLV2.tavolo.deck[SpiderDLV2.tavolo.top].get(0);
            Carta botCarta = SpiderDLV2.tavolo.deck[0].get(0);

            Rectangle rect = new Rectangle(topCarta.x, topCarta.y, botCarta.x + botCarta.width - topCarta.x,
                    topCarta.height);

            if (rect.contains(mouseX, mouseY)) {
                // make sure that there's no empty pile
                for (int i = 0; i < Tavolo.pile; i++) {
                    if (SpiderDLV2.tavolo.piles[i].size() == 0) {
                        return;
                    }
                }

                SpiderDLV2.tavolo.deal();
            }
        }
        
        
        
       // System.out.println("Esco da MousePressed() ");
    }

   

	@Override
    public void mouseReleased(MouseEvent e) 
    {
    	System.out.println("MouseReleased() OK");
    	
        if (SpiderDLV2.tavolo.muoviPila != null) 
        {
            boolean success = false;
            boolean b = false;

            Carta firstCarta = SpiderDLV2.tavolo.muoviPila.get(0);
            
            System.out.print("First Carta : ");
            
            if(firstCarta != null)
            {
            	SpiderDLV2.tavolo.stampaDebug(firstCarta.seme, firstCarta.valore);
            }
            
            Carta lastCarta = SpiderDLV2.tavolo.muoviPila.get(SpiderDLV2.tavolo.muoviPila.size() - 1);
            
            System.out.print("Last Carta : ");
            
            if(firstCarta != null)
            {
            	SpiderDLV2.tavolo.stampaDebug(lastCarta.seme, lastCarta.valore);
            }

            Rectangle dragRegion = new Rectangle(firstCarta.x, firstCarta.y, firstCarta.width,
                    lastCarta.y + lastCarta.height - firstCarta.y);

           
            for (int i = 0; i < Tavolo.pile; i++) {
                if (i == SpiderDLV2.tavolo.index) {
                    continue;
                }
                
                System.out.println("ENTRO NEL FOR ");

                Carta card = (SpiderDLV2.tavolo.piles[i].size() > 0) ? SpiderDLV2.tavolo.piles[i].get(SpiderDLV2.tavolo.piles[i].size() - 1) : null; // last card
                
                System.out.println("Controllo l'ultima carta ");
                
                if(card != null)
                {
                	SpiderDLV2.tavolo.stampaDebug(card.seme, card.valore);
                }

                if (card != null && card.intersects(dragRegion)
                        && (!SpiderDLV2.debug ? (card.getValore() == firstCarta.getValore() + 1) : true)) {
                	
                	System.out.println("Confronto le first card e la card corrente ");
                	
                	System.out.print("First Carta: ");
                	SpiderDLV2.tavolo.stampaDebug(firstCarta.seme, firstCarta.valore);
                	
                	System.out.print("Carta corrente: ");
                	SpiderDLV2.tavolo.stampaDebug(card.seme, card.valore);
                	
                	SpiderDLV2.tavolo.piles[i].addAll(SpiderDLV2.tavolo.muoviPila);
                    
                    System.out.println("OK, sono ordinate correttamente ! ");

                    System.out.println("Provo a rimuovere le carte ! ");
                    
                    if (SpiderDLV2.tavolo.checkForCardsToRemove(i)) 
                    {
                    	SpiderDLV2.tavolo.punteggio += 100;
                        System.out.println("Aumento il punteggio che e' di " + SpiderDLV2.tavolo.punteggio + " ");
                        
                        SpiderDLV2.tavolo.undoStack.push(new Stato());
                        System.out.println("Creo un nuovo GameState() ");
                        
                        
                        b = true;

                        if (SpiderDLV2.tavolo.carte.size() == 104) {
                        	SpiderDLV2.tavolo.muoviPila = null;
                        	SpiderDLV2.tavolo.repaint();
                            
                            System.out.println("Le carte raccolte sono 104.");
                            System.out.println("La partita finisce qui.");

                            if (SpiderDLV2.tavolo.showPlayAgainDialog()) {
                            	System.out.println("Se vuoi continuare a giocare rispondi ! ");
                                return;
                            }
                        }
                    }

                    SpiderDLV2.tavolo.fixPiles(i);
                    success = true;
                    break;
                }
                else if (card == null && SpiderDLV2.tavolo.piles[i].size() == 0) {
                    int xGap = (SpiderDLV2.width - Tavolo.pile * SpiderDLV2.tavolo.larghezzaCarta) / (SpiderDLV2.tavolo.pile + 1);
                    int topX = xGap + i * SpiderDLV2.tavolo.larghezzaCarta + i * xGap - SpiderDLV2.tavolo.insets.left;

                    Rectangle rect = new Rectangle(topX, SpiderDLV2.tavolo.margine, SpiderDLV2.tavolo.larghezzaCarta, SpiderDLV2.tavolo.altezzaCarta); // white
                                                                                         // rectangle
                                                                                         // border
                    
                    System.out.println("Se non ci sono più carte nella pila disegno LO SPAZIO VUOTO");

                    if (rect.intersects(dragRegion)) {
                    	SpiderDLV2.tavolo.piles[i].addAll(SpiderDLV2.tavolo.muoviPila);
                    	SpiderDLV2.tavolo.fixPiles(i);
                        success = true;
                        break;
                    }
                }
            }

            if (!success) {
            	
            	System.out.println("Success = FALSE ");
            	
            	SpiderDLV2.tavolo.piles[SpiderDLV2.tavolo.index].addAll(SpiderDLV2.tavolo.muoviPila);
            	SpiderDLV2.tavolo.undoStack.pop();
            }
            else {
            	System.out.println("Success = TRUE ");
            	
                Carta last = (SpiderDLV2.tavolo.piles[SpiderDLV2.tavolo.index].size() > 0) ? SpiderDLV2.tavolo.piles[SpiderDLV2.tavolo.index].get(SpiderDLV2.tavolo.piles[SpiderDLV2.tavolo.index].size() - 1) : null;

                if (last != null && last.isFaceDown()) {
                    last.flip();
                }

                SpiderDLV2.tavolo.punteggio--;
                SpiderDLV2.tavolo.mosse++;

                
            }

            SpiderDLV2.tavolo.fixPiles(SpiderDLV2.tavolo.index);
            SpiderDLV2.tavolo.muoviPila = null;

            System.out.println("MouseManager 2-> repaint()");
            SpiderDLV2.tavolo.repaint();

            if (SpiderDLV2.tavolo.carte.size() == 104) {
            	SpiderDLV2.tavolo.showPlayAgainDialog();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	System.out.println("MouseDragged OK");
        if (SpiderDLV2.tavolo.muoviPila != null) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            int dx = mouseX - SpiderDLV2.tavolo.prevMX;
            int dy = mouseY - SpiderDLV2.tavolo.prevMY;

            SpiderDLV2.tavolo.muoviPila.stream().forEach(c -> c.translate(dx, dy));

            SpiderDLV2.tavolo.prevMX = mouseX;
            SpiderDLV2.tavolo.prevMY = mouseY;

            System.out.println("MouseDRAGGED -> repaint()");
            SpiderDLV2.tavolo.repaint();
            
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
	
    
    // MOUSE MOTION LISTENER

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    
    

}
