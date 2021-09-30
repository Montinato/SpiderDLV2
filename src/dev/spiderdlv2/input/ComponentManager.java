package dev.spiderdlv2.input;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


import dev.spiderdlv2.*;

public class ComponentManager implements ComponentListener
{
	
	
	 public void componentResized(ComponentEvent e) 
	 {
		 SpiderDLV2.width = e.getComponent().getWidth();
		 SpiderDLV2.height = e.getComponent().getHeight();

	        if (SpiderDLV2.debug) {
	            System.err.println("width:" + SpiderDLV2.width + ", height:" + SpiderDLV2.height);
	        }

	        SpiderDLV2.tavolo.calcYCutoff();
	        SpiderDLV2.tavolo.fixPile();
	        SpiderDLV2.tavolo.fixMazzo();
	        SpiderDLV2.tavolo.fixJunk();
	        SpiderDLV2.tavolo.repaint();
	    }

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	


}
