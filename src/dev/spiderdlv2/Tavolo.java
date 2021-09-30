package dev.spiderdlv2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JOptionPane;


public class Tavolo extends JComponent
{

	// Metto tutto public perche' sto sistemando il codice
	
	
    public static final int pile = 10;
    public static final int mazzi = 6; // mazzi mazzi, each slot have 10 (pile) cartas

    public final int margine = 10;

    public final int larghezzaCarta = 71;
    public final int altezzaCarta = 96;

    public final Color bgColor = new Color(0, 120, 0);

    public List<Carta> carte; // also it's a junk pile

    public Image retroCarta;

    public List<Carta>[] piles;
    public List<Carta>[] deck;

    public int top; // pointer to top
    public int ptr;

    public int yCutoff;

    public int punteggio;
    public int mosse;

    public List<Carta> muoviPila;

    public int index;

    public int prevMX;
    public int prevMY;

    public String difficolta;

    public Stack<Stato> undoStack;

    public Insets insets;
    
    // DOPO AVER SISTEMATO IL CODICE , FIXARE PUBLIC/PRIVATE/PROTECTED
  
    
    // METODO OK 
    public Tavolo(String difficolta) 
    {
    	
        retroCarta = Utility.leggiImmagine("images\\back.png");		
        
        undoStack = new Stack<>();								
        
        insets = new Insets(0, 0, 0, 0);			 		

        caricaImmagini(difficolta);
        calcYCutoff();
        nuovaPartita();
    }
    
    
    // METODO OK 
    public void caricaImmagini(String difficolta) 
    {
    	// System.out.println("Metodo loadImage() ");
    	
        this.difficolta = difficolta;

        int k = -1;

        if (difficolta.equals("Facile")) {
            k = 4;
        }
        else if (difficolta.equals("Media")) {
            k = 3;
        }
        else {
            k = 1;
        }

        carte = new ArrayList<>();

        int cont = 0;
        
        //int contaNumero = 1;		//DEBUG
        
        //  QUI CARICO TUTTE LE IMMAGINI DELLE CARTE, sia per le PILE che per i mazzi

        while (cont < 8) {
        	
        	// System.out.println("Counter = " + counter);
            
        	for (int seme = k; seme <= 4; seme++) {
                for (int valore = 1; valore <= 13; valore++) {
                	
                    carte.add(new Carta(Utility.leggiImmagine("images\\" + valore + "" + seme + ".png"), seme, valore, true));
                    
                    //System.out.println("Carico l'immagine della carta " + valore + " " + seme );
                    
                    // stampaDebug(seme,valore);
                     	
                    //contaNumero++;
                }

                cont++;
            }
            
        }
        
         // System.out.println("Il numero di immagini caricate e': " + contaNumero );
    }

    
    // METODO OK 
    public void stampaDebug(int seme,int valore)
    {
    	if(seme == 1)
        	System.out.print("Fiori : ");
        else if(seme == 2)
        	System.out.print("Quadri : ");
        else if(seme == 3)
        	System.out.print("Cuori : ");
        else if(seme == 4)
        	System.out.print("Picche : ");
        
        
        
        if(valore == 11)
        	System.out.println("J");
        else if(valore == 12)
        	System.out.println("Q");
        else if(valore == 13)
        	System.out.println("K");
        else 
        	System.out.println(valore);
    }
    
    
    // METODO OK, DEVO EFFETTIVAMENTE VEDERE A COSA SERVE 
    public void calcYCutoff() {
        yCutoff = SpiderDLV2.height * 3 / 5;
    }
    
    
    /*
     * preconditions:
     * - undoStack is !null
     * - caricaImmagini() is called
     * - calcYCutoff() is called, otherwise fixPile() will be slow
     */

    // CONTROLLO QUESTO 
    
    public void nuovaPartita() 
    {
    	
        raccogliTutteLeCarte();
        
       /* System.out.println("Stampa dopo il metodo raccogliTutteLeCarte() ");
        System.out.println("                                              ");
        
        for (Carta c : carte) 
        {
        	stampaDebug(c.seme,c.valore);        
        }
        System.out.println("                                              ");
        System.out.println("Numero di carte presente in allCarta (dopo raccogliTutteLeCarte() ) : " + carte.size());
        System.out.println("                                              ");	*/
        
        // Mischio le carte 
        Collections.shuffle(carte);
        
        
        /*System.out.println("Stampa dopo il metodo Collection.shuffle() ");
        System.out.println("                                              ");
        
        for (Carta c : carte) 
        {
        	stampaDebug(c.seme,c.valore);        
        }
        
        System.out.println("                                              ");
        System.out.println("Numero di carte presente in allCarta (dopo Collection.shuffle() ) : " + carte.size());
        System.out.println("                                              ");	*/
       
        
        initMazzi();
        
        initPile();
        
        deal();
        
        
        undoStack.clear();
        

        ptr = -1;
        punteggio = 500;
        mosse = 0;
        muoviPila = null;
    }
    
    public String getDifficolta() {
        return difficolta;
    }
   
    //  METODO OK - Mi serve quando faccio una partita,distribuisce il primo mazzi di carte e gli altri mazzi
    private void raccogliTutteLeCarte() 
    {
    	int contDebug = 0;
    	
    	//System.out.println("Metodo raccogliTutteLeCarte() ");
    	
        if (carte.size() < 104) 
        {
        	// Se le carte presenti in carte sono meno di 104 entro qui
        	//System.out.println("Entro nell'if del metodo collectAllCarta() ");
        	
        	
        	
            for (int i = 0; i < pile; i++)
            {
            	//System.out.println("La pila " + i);
            	
                int cartas = piles[i].size();
                
                //System.out.println("Ha dimensione " + cartas);

                for (int j = 0; j < cartas; j++) {
                    Carta carta = piles[i].get(j);
                    
                    //System.out.println("Inserisco la carta in posizione " +  i + " " + j );
                    
                    //System.out.println("La carta e' : " );
                    
                    
                    //stampaDebug(carta.seme,carta.valore);
                    
                   // contDebug++;

                    if (!carta.isFaceDown()) {
                    	
                        carta.flip();
                    }

                   
                    carte.add(carta);
                    
                   // System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                }
            }
            
            
            //System.out.println("---------------------------------------------------------------------------------------------------------------------");
            //System.out.println("---------------------------------------------------------------------------------------------------------------------");

            for (int i = 0; i <= top; i++) 
            {
            	
            	//System.out.println("TOP vale = " + top);
            	
            
                List<Carta> slot = deck[i];
                
                //System.out.println("Scelgo il mazzi numero " + i );

                int size = slot.size();

                for (int j = 0; j < size; j++) {
                //	System.out.println("Metto a terra la carta " + j);
                    carte.add(slot.get(j));
                }
            }
            
           // System.out.println("*************************************************************************************************");
            
           // System.out.println("ESCO dall'if del metodo collectAllCarta() ");
        }
        
       // System.out.println("Numero di carte =  " + contDebug);
        //System.out.println("Esco dal metodo raccogliTutteLeCarte()");	
        } 
    
    // METODO OK 
    @SuppressWarnings("unchecked")
    private void initMazzi() 
    {
    	 System.out.println("Metodo initMazzi() ");
    	 
    	 int cont = 0;
    	
        deck = new List[mazzi];

        top = mazzi - 1;

        // System.out.println("Ci sono in totale " +  top + " righe ");
        
        int y = SpiderDLV2.height - altezzaCarta - margine - 40 - insets.bottom - 16;
        //System.out.println("y = " + y);

        for (int i = 0; i < mazzi; i++) 
        {
        	// System.out.println("Posizione riga = " + i);
        	
            deck[i] = new ArrayList<>();

            int x = SpiderDLV2.width - larghezzaCarta - margine - insets.left - 10 - (margine + 2) * i;
            //System.out.println("x = " + x );

            for (int j = 0; j < pile; j++) 
            {
            	// System.out.println("Invoco il metodo draw() ");
            	
                Carta carta = draw();

                carta.x = x;
                carta.y = y;
                carta.width = larghezzaCarta;
                carta.height = altezzaCarta;

                deck[i].add(carta);
                cont++;
                
                if(i == mazzi-1)
                {
                	//System.out.print("La carta scoperta della pila " + j + " -> ");
                	//System.out.print("Aggiungo a riga " + i + " -> ");
                	//stampaDebug(carta.seme, carta.valore);
                }
            }
            
            // System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        }
        
        //System.out.println("Il numero di carte di initMazzi = " + cont);
    }
    
    
    // METODO OK 
    @SuppressWarnings("unchecked")
    private void initPile() 
    {
    	//System.out.println("                                            ");
    	System.out.println("Metodo initPile() ");
    	//System.out.println("                                            ");
    	
    	int cont = 0;
    	
        piles = new List[pile];

        for (int i = 0; i < pile; i++) {
            piles[i] = new ArrayList<>();

            int cartas = (i < 4) ? 5 : 4;

            for (int j = 0; j < cartas; j++) {
                Carta carta = draw();

                carta.width = larghezzaCarta;
                carta.height = altezzaCarta;

                piles[i].add(carta);
                cont++;
                
                //System.out.print("Pila " + i + " in posizione " + j + " -> ");
                //stampaDebug(carta.seme, carta.valore);
                
            }
            //System.out.println("                                                                                          ");

            fixPiles(i);
            //chiamateFixPile++;
        }
        
        // System.out.println("Numero di carte initPile = " + cont);
    }

    
    // METDO OK 
    private Carta draw() 
    {
        Carta carta = carte.get(0);
        carte.remove(carta);
        return carta;
    }
    
    
    // METODO OK - Con questo metodo scopro l'ultima carta di ogni pila e la mostro al player, richiama anche checkForCartaToRemove()
    public void deal() 
    {
    	System.out.println("Metodo deal() ");
    	
        for (int i = 0; i < pile; i++) 
        {
        	//System.out.println("Pila : " + i );
        	
            Carta carta = deck[top].get(i);
            
            piles[i].add(carta);
            carta.flip();
            
            //System.out.print("Giro la carta della pila " + i + " : ");	// CONTROLLARE IL DEBUG DI QUESTA PARTE 
            
            
            
            //stampaDebug(carta.seme, carta.valore);
            
            fixPiles(i);
            
        }

        deck[top--] = null;
        undoStack.push(new Stato());

        for (int i = 0; i < pile; i++) {
            if (checkForCardsToRemove(i)) {
                punteggio += 100;
            }
        }

        repaint();
        
    }
  
    // METODO OK 
    public void setInsets(Insets insets) {
        this.insets = insets;
        repaint();
    }



    // METODO OK 
    public void undo() 
    {
        Stato stato = undoStack.pop();

        carte = stato.getCarte();
        piles = stato.getPile();
        top = stato.getTop();
        ptr = stato.getPtr();

        repaint();

        punteggio--;
        mosse++;

        
    }

    // METODO OK 
    public boolean canUndo() {
        return !undoStack.empty() && !undoStack.peek().isFlagged();
    }

    // METODO OK 
    public void resetGame() 
    {
    	// Se lo stack in cui salvo i GameState e' vuoto return
        if (undoStack.empty()) {
            return;
        }

        // Finché sono presenti GameState in undoStack li rimuovo. Ne rimane solo 1 
        while (undoStack.size() > 1) {
            undoStack.pop();
        }

        // Creo uno stato e salvo l'ultima istanza rimasta in undoStack()
        Stato stato = undoStack.pop();

        // Richiamo i metodi di utilita' 
        carte = stato.getCarte();
        piles = stato.getPile();
        deck = stato.getDeck();
        top = stato.getTop();
        ptr = stato.getPtr();

        repaint();

        punteggio = 500;
        mosse = 0;
    }

    
    // METODO OK 
    public void pulisciCarte() {
        raccogliTutteLeCarte();
        carte = null;
    }

    
    /*
     * This method is an extension of Graphics::drawString, it handles strings with newlines, thanks to
     * SO!
     */
    private void drawString(Graphics g, String str, int x, int y) {
        for (String line : str.split("\n")) {
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
        }
    }


    public void fixJunk() {
        int y = SpiderDLV2.height - altezzaCarta - margine - 40 - insets.bottom - 16;

        for (int i = 0; i <= ptr; i++) {
            Carta carta = carte.get(i);

            carta.y = y;
        }
    }

    public void fixMazzo() {
        int y = SpiderDLV2.height - altezzaCarta - margine - 40 - insets.bottom - 16;

        for (int i = 0; i <= top; i++) {
            int x = SpiderDLV2.width - larghezzaCarta - margine - insets.left - 10 - (margine + 2) * i;

            deck[i].stream().forEach(carta -> {
                carta.x = x;
                carta.y = y;
            });
        }
    }

    public void fixPile() {
        for (int i = 0; i < pile; i++) {
            fixPiles(i);
        }
    }

    // METODO OK 
    public void fixPiles(int index) 
    {
    	//System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    	//System.out.println("Metodo fixPile() : ");
    	
    	if (piles[index].size() == 0) {
            return;
        }

        int xGap = (SpiderDLV2.width - pile * larghezzaCarta) / (pile + 1);
       // System.out.println("xGap :" + xGap);
        
        int topX = xGap + index * larghezzaCarta + index * xGap - insets.left;
        //System.out.println("topX :" + topX);
        
        int yGap = 35;
        //System.out.println("yGap :" + yGap);

        for (int i = 0; i < 6; i++) 
        {
        	//System.out.print("Sono sulla pila " + index );
        	
            int cartas = piles[index].size();
         //   System.out.println(" che ha dimensione " + cartas + ".");

            Carta prevCarta = null;

            yGap -= (i < 4) ? 4 : 1;
            
           /* if(i<4)
            	yGap -= 4;
            else
            	yGap-=1;	*/
            
            //System.out.println("yGap :" + yGap);
            

            for (int j = 0; j < cartas; j++) {
                Carta carta = piles[index].get(j);

                // 		???????????????????????????????????
                int topY = (prevCarta == null) ? margine : prevCarta.y + (prevCarta.isFaceDown() ? margine : yGap);
                
               // System.out.println("topY:" + topY);

                carta.x = topX;
                carta.y = topY;

                prevCarta = carta;
            }

            int lastY = piles[index].get(piles[index].size() - 1).y;
            
            //System.out.println("lastY : " + lastY);
           // System.out.println("                                                                                     ");

            if (lastY < yCutoff) {
                break;
            }
            
            
           // System.out.println("                                                                                     ");
        }
        
      //  System.out.println("Ho invocato fixPile() " + chiamateFixPile + " volte.");
        
    }

   
    // METODO OK 
    public boolean showPlayAgainDialog() {
        int[] a = SpiderDLV2.tracker.getData(difficolta);

        a[0] = Math.max(a[0], punteggio);
        a[1] = a[1] == 0 ? mosse : Math.min(a[1], mosse);
        a[2]++;

        int resp = JOptionPane.showConfirmDialog(this, "Vuoi giocare ancora? ", "Game over! You won!",
                JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            nuovaPartita();
            return true;
        }

        SpiderDLV2.tracker.scriviSulFile();
        System.exit(0);

        return false; 
    }

    
    // METODO OK  
    public boolean checkForCardsToRemove(int index)
    {
        int seme = -1;
        int valore = 1;

        // Faccio dei controlli sull'ultima carta della pila, che deve essere scoperta
        
        System.out.println("                                                 ");
        System.out.println("Metodo checkForCartaToRemove() ");
        System.out.println("PRIMO FOR ");
               
        for (int i = piles[index].size() - 1; i >= 0 && valore <= 13; i--, valore++)
        {
        	System.out.println("Siamo in posizione " + " " + index + " " +  i + ".");
        	
            Carta carta = piles[index].get(i);
            
            System.out.print("Considero la carta -> ");
            
            stampaDebug(carta.seme,carta.valore);

            if (seme == -1) {
                seme = carta.getSeme();
                
            }
            if (seme != carta.getSeme()) {
                return false;
            }
            if (carta.isFaceDown()) {
                return false;
            }
            if (carta.getValore() != valore) {
                return false;
            }
        }

        // Da qui in poi controllo la sequenza della pila 
        if (valore == 14) {
        	
        	System.out.println("                                                                                         ");
        	System.out.println(" SEQUENZA COMPLETATA                                         ");
        	System.out.println("                                                                                         ");
        	
            int y = SpiderDLV2.height - altezzaCarta - margine - 40 - insets.bottom - 16;

            Carta prevCarta = (ptr == -1) ? null : carte.get(ptr);
            System.out.print("Considero la carta 2 -> ");
            
            //stampaDebug(prevCarta.seme,prevCarta.valore);
            
            System.out.println("                                                                       ");
            System.out.println("SECONDO FOR ");
            System.out.println("                                                                       ");
            

            for (int i = piles[index].size() - 1; i >= 0 && --valore >= 1; i--) 
            {
                Carta carta = piles[index].get(i);
                
                System.out.print("Considero la carta 3 -> ");
                stampaDebug(carta.seme,carta.valore);

                carta.x = (prevCarta == null) ? margine : prevCarta.x + margine + 2;
                carta.y = y;
                
                System.out.print("Giro la carta ");
                
                if(carta!=null)
                {
                	stampaDebug(carta.seme,carta.valore);
                }
                
                carta.flip(); // the carta must be flipped face down for new game

                
                piles[index].remove(carta);
                System.out.print("Rimuovo dalla pila " + index + " la carta ");
                stampaDebug(carta.seme,carta.valore);
                
                carte.add(carta);
                System.out.println("Aggiungo in carte la carta ");
                
                ptr++;
            }

            Carta last = (piles[index].size() > 0) ? piles[index].get(piles[index].size() - 1) : null;
            
            System.out.print("L'ultima carta della pila " + index + " adesso e' ");
            
            if(last != null)
            {
            	stampaDebug(last.seme,last.valore);
            }
            

            if (last != null && last.isFaceDown()) {
            	System.out.print("Scopro la carta ");
            	stampaDebug(last.seme,last.valore);
                last.flip();
            }

            return true;
        }

        return false;
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(bgColor);
        g.fillRect(0, 0, SpiderDLV2.width, SpiderDLV2.height);

        int xGap = (SpiderDLV2.width - pile * larghezzaCarta) / (pile + 1);
        int y = margine;

        for (int i = 0; i < pile; i++) {
            int x = xGap + i * larghezzaCarta + i * xGap - insets.left;

            g.setColor(Color.WHITE);
            g.fillRect(x, y, larghezzaCarta, altezzaCarta); // a white rectangle border indicates that there's no
                                                     // cartas at that pile
            g.setColor(bgColor);
            g.fillRect(x + 1, y + 1, larghezzaCarta - 2, altezzaCarta - 2);

            piles[i].stream().forEach(carta -> {
                g.drawImage(carta.isFaceDown() ? retroCarta : carta.getImage(), carta.x, carta.y, this);
            });
        }

        for (int i = 0; i <= top; i++) {
            Carta carta = deck[i].get(0);
            g.drawImage(retroCarta, carta.x, carta.y, this);
        }

        for (int i = 12; i <= ptr; i += 13) {
            Carta carta = carte.get(i);
            g.drawImage(carta.getImage(), carta.x, carta.y, this);
        }

        g.setColor(bgColor.darker());
        g.fillRect(SpiderDLV2.width / 2 - 100 - insets.left, SpiderDLV2.height - altezzaCarta - margine - insets.bottom - 50,
                200,
                altezzaCarta);

        StringBuilder sb = new StringBuilder();
        sb.append("Punteggio: ").append(punteggio);
        sb.append("\nMosse: ").append(mosse);

        g.setColor(Color.WHITE);
        g.setFont(new Font("consolas", Font.BOLD, 14));
        drawString(g, sb.toString(), SpiderDLV2.width / 2 - 50,
                SpiderDLV2.height - altezzaCarta - margine - insets.bottom - 50 + 25);

        if (muoviPila != null) {
            muoviPila.stream().forEach(carta -> {
                g.drawImage(carta.getImage(), carta.x, carta.y, this);
            });
        }
    }
    
    public void doRepaint()
    {
    	System.out.println("Faccio la repaint di Tavolo");
    	this.repaint();
    }


}
