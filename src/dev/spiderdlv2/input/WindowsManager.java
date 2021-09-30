package dev.spiderdlv2.input;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import dev.spiderdlv2.SpiderDLV2;

public class WindowsManager implements WindowListener
{
	
	 
	    public void windowClosing(WindowEvent e) {
	        //int[] a = SpiderDLV2.tracker.getData(SpiderDLV2.tavolo.getDifficolta());
	        //a[3]++;
	        //SpiderDLV2.tracker.scriviSulFile();
	    }

	   
	    @Override
	    public void windowOpened(WindowEvent e) {
	    }

	    @Override
	    public void windowClosed(WindowEvent e) {
	    }

	    @Override
	    public void windowIconified(WindowEvent e) {
	    }

	    @Override
	    public void windowDeiconified(WindowEvent e) {
	    }

	    @Override
	    public void windowActivated(WindowEvent e) {
	    }

	    @Override
	    public void windowDeactivated(WindowEvent e) {
	    }

}
