package dev.spiderdlv2;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import dev.spiderdlv2.input.*;
import dev.spiderdlv2.input.ComponentManager;
import dev.spiderdlv2.input.WindowsManager;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class SpiderDLV2 extends JFrame 
{
	    public static int width = 1100;
	    public static int height = 700;

	    public static Tavolo tavolo = null;

	    public static DataTracker tracker;

	    private ImageIcon icon;

	    public static boolean debug;
	    
		//Input
		private ComponentManager componentManager;
		private WindowsManager windowsManager;
		private MouseManager mouseManager;

	    static {
	        try {
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());			
	        }
	        catch (ClassNotFoundException | InstantiationException | IllegalAccessException
	                | UnsupportedLookAndFeelException ex) {
	            ex.printStackTrace();
	        }
	    }

	    public SpiderDLV2() 
	    {
	        super("SpiderDLV2");
	        
	        
	        setMinimumSize(new Dimension(width, height));
	        setLocationRelativeTo(null);

	        
	        // Prima di iniziare a giocare scelgo la difficoltà
	        scegliDifficolta();		
	        
	        setContentPane(tavolo);
	        
	        tavolo.setInsets(getInsets());
	        
	        // DEVO VEDERE PERCHE' HO QUESTO PROBLEMA CON DATA TRACKER
	        //tracker = new DataTracker("stats.db");

	        icon = new ImageIcon(Utility.leggiImmagine("images\\icon.png"));
	        setIconImage(icon.getImage());

	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setExtendedState(MAXIMIZED_BOTH);
	       
	   
	        componentManager = new ComponentManager();
	        windowsManager = new WindowsManager();
	        mouseManager = new MouseManager();
	        
	        
	        this.addComponentListener(componentManager);
			this.addMouseListener(mouseManager);
			this.addMouseMotionListener(mouseManager);
			this.addWindowListener(windowsManager);


	        debug = false;
	    }
	    
	    
	    private void scegliDifficolta() 
	    {
	        String[] diff = { "Facile", "Media", "Difficile" };
	        
	        String scelta = (String) JOptionPane.showInputDialog(this, "Scegli la difficolta' :", "Nuova Partita",
	                JOptionPane.QUESTION_MESSAGE, null, diff, diff[0]);

	        if (tavolo == null && scelta != null) {
	            tavolo = new Tavolo(scelta);
	        }
	        else if (scelta != null) {
	            tavolo.pulisciCarte();
	            tavolo.caricaImmagini(scelta);
	            tavolo.nuovaPartita();
	        }

	    }
	    
	    
	    
	    

	   
	   
}
