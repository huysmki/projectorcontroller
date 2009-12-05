package be.rhea.projector.controller.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.net.URL;

import javax.media.CachingControl;
import javax.media.CachingControlEvent;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DurationUpdateEvent;
import javax.media.MediaLocator;
import javax.media.MediaTimeSetEvent;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RateChangeEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.SizeChangeEvent;
import javax.media.StartEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.TransitionEvent;
import javax.media.bean.playerbean.MediaPlayer;
import javax.media.format.FormatChangeEvent;
import javax.swing.JPanel;

public class VideoMediaPanel extends JPanel implements ControllerListener{
	private static final long serialVersionUID = 3632647220675028768L;
	private MediaPlayer mediaPlayer;
    private boolean boolErrorClose = false;
    private boolean  boolMediaClosed = true;
	private boolean realized = false;

	public VideoMediaPanel() {
		setLayout(new BorderLayout()); 
		this.setBackground(Color.BLACK);
	}

	public Player getMediaPlayer() {
		return mediaPlayer;
	}

	public void realize(URL url) {

		killCurrentPlayer ();

		MediaLocator mediaLocator = new MediaLocator ( url );
	    mediaPlayer = new MediaPlayer ();
		mediaPlayer.setMediaLocator ( mediaLocator );

        mediaPlayer.addControllerListener ( this );
        mediaPlayer.realize();
		
	} 

	public void play(Dimension size, boolean loop) {
		killCurrentView ();
        Component compControl = mediaPlayer.getVisualComponent();
        mediaPlayer.setSize(size);
        if ( compControl != null) {
            this.add ( compControl);
            compControl.setVisible(true);
        }
        mediaPlayer.setPlaybackLoop(loop);
        mediaPlayer.start();
        this.repaint();
        this.revalidate();
        this.setVisible(true);
	}
	
	public boolean isRealized() {
		return realized;
	}

	public void killCurrentPlayer () {
        killCurrentView ();
        if ( mediaPlayer != null ) {
            boolMediaClosed = false;
            mediaPlayer.close ();
            if ( boolErrorClose == false ) {
                while ( boolMediaClosed == false ) {
                    try {
                        Thread.sleep ( 50 );
                    }
                    catch ( Exception exception ) {
                    }
                }
            }
            mediaPlayer.removeControllerListener ( this );
        }
    }
    
    private void killCurrentView () {
        int         i;
        Component   component;

        i = this.getComponentCount();
        while ( i > 0 ) {
            i--;
            component = this.getComponent ( i );
            this.remove ( component );
        }
    }
    
    public synchronized void controllerUpdate ( ControllerEvent event ) {
        if ( event instanceof RealizeCompleteEvent ) {
            processRealizeComplete ( (RealizeCompleteEvent) event );
        }
        else if ( event instanceof PrefetchCompleteEvent ) {
            processPrefetchComplete ( (PrefetchCompleteEvent) event );
        }
        else if ( event instanceof ControllerErrorEvent ) {
            processControllerError ( (ControllerErrorEvent) event );
        }
        else if ( event instanceof ControllerClosedEvent ) {
            processControllerClosed ( (ControllerClosedEvent) event );
        }
        else if ( event instanceof DurationUpdateEvent ) {
        }
        else if ( event instanceof CachingControlEvent ) {
            processCachingControl ( (CachingControlEvent) event );
        }
        else if ( event instanceof StartEvent ) {
        }
        else if ( event instanceof MediaTimeSetEvent ) {
        }
        else if ( event instanceof TransitionEvent ) {
        }
        else if ( event instanceof RateChangeEvent ) {
        }
        else if ( event instanceof StopTimeChangeEvent ) {
        }
        else if ( event instanceof FormatChangeEvent ) {
            processFormatChange ( (FormatChangeEvent) event );
        }
        else if ( event instanceof SizeChangeEvent ) {
        }

    }

    private void processRealizeComplete ( RealizeCompleteEvent event ) {
        realized = true;	
	}


    private void processPrefetchComplete ( PrefetchCompleteEvent event ) {
        mediaPlayer.start();
    }

    private void processControllerError ( ControllerErrorEvent event ) {
        if ( boolMediaClosed == true ) {
            boolErrorClose = true;
            killCurrentPlayer ();
            boolErrorClose = false;
        }
        boolMediaClosed = true;
    }

    private void processControllerClosed ( ControllerClosedEvent event ) {
        boolMediaClosed = true;
    }

    private void processCachingControl ( CachingControlEvent event ) {
        CachingControl      controlCaching;
        Component           progressBarCacheNew = null;

        controlCaching = event.getCachingControl ();
        if ( controlCaching == null )
            return;

        if ( progressBarCacheNew == null )
            progressBarCacheNew = controlCaching.getControlComponent();
        if ( progressBarCacheNew == null )
            progressBarCacheNew = controlCaching.getProgressBarComponent();
        if ( progressBarCacheNew == null )
            return;

        if ( mediaPlayer!= null  &&  mediaPlayer.getState() >= Player.Realized )
            return;

        killCurrentView ();
    }

    private void processFormatChange ( FormatChangeEvent event ) {
        killCurrentView ();

        Component compControl = mediaPlayer.getVisualComponent();
        if ( compControl != null) {
        	this.add ( compControl);
            compControl.setVisible(true);
        }
        this.repaint();
        this.revalidate();
        this.setVisible(true);			
    }


}
