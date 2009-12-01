package be.rhea.projector.controller.remote.commands.server;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.CachingControl;
import javax.media.CachingControlEvent;
import javax.media.CannotRealizeException;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DurationUpdateEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.MediaTimeSetEvent;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RateChangeEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.SizeChangeEvent;
import javax.media.StartEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.Time;
import javax.media.TransitionEvent;
import javax.media.bean.playerbean.MediaPlayer;
import javax.media.format.FormatChangeEvent;
import javax.swing.JFrame;

import jmapps.ui.MessageDialog;
import jmapps.ui.VideoPanel;

import com.sun.media.util.JMFI18N;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaStartServerCommand implements
		SimpleProtocolServerExecuteCommand, ControllerListener {
	
	private final VideoMediaPanel mediaPanel;
	private String mediaDir;
	private final JFrame frame;
	private MediaPlayer mediaPlayer;
    private boolean boolErrorClose = false;
    private boolean  boolMediaClosed = true;

	public PCPVideoMediaStartServerCommand(JFrame frame, VideoMediaPanel mediaPanel) {
		this.frame = frame;
		this.mediaPanel = mediaPanel;
	}

	@Override
	public String execute(String[] parameters) {
		frame.getContentPane().removeAll();
		frame.getContentPane().add(mediaPanel);
		
	    URL url = null;
		try {
			url = new File(mediaDir + "/" + parameters[0]).toURI().toURL();
			Player oldPlayer = mediaPanel.getMediaPlayer();

			MediaLocator mediaLocator = new MediaLocator ( url );
		    mediaPlayer = new MediaPlayer ();
			mediaPlayer.setMediaLocator ( mediaLocator );

			killCurrentPlayer (oldPlayer);

	        mediaPanel.setMediaPlayer(mediaPlayer);
	        mediaPlayer.addControllerListener ( this );
	        mediaPlayer.realize();


		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "Video Media Loaded";
	}

    protected void killCurrentPlayer (Player oldPlayer) {
        killCurrentView ();
        if ( oldPlayer != null ) {
            boolMediaClosed = false;
            oldPlayer.close ();
            if ( boolErrorClose == false ) {
                while ( boolMediaClosed == false ) {
                    try {
                        Thread.sleep ( 50 );
                    }
                    catch ( Exception exception ) {
                    }
                }
            }
            oldPlayer.removeControllerListener ( this );
        }
    }
    
    protected void killCurrentView () {
        int         i;
        Component   component;

        i = mediaPanel.getComponentCount();
        while ( i > 0 ) {
            i--;
            component = mediaPanel.getComponent ( i );
            mediaPanel.remove ( component );
        }

    }    
	
	public void setMediaDir(String mediaDir) {
		this.mediaDir = mediaDir;
	}

	@Override
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

    protected void processRealizeComplete ( RealizeCompleteEvent event ) {
        killCurrentView ();
        Component compControl = mediaPlayer.getVisualComponent();
        if ( compControl != null) {
            mediaPanel.add ( compControl);
            compControl.setVisible(true);
        }

        mediaPlayer.prefetch ();
		mediaPanel.repaint();
		mediaPanel.revalidate();
		mediaPanel.setVisible(true);			

	}

    protected void processPrefetchComplete ( PrefetchCompleteEvent event ) {
        mediaPlayer.start();
    }

    protected void processControllerError ( ControllerErrorEvent event ) {
        if ( boolMediaClosed == true ) {
            boolErrorClose = true;
            killCurrentPlayer (mediaPanel.getMediaPlayer());
            boolErrorClose = false;
        }
        boolMediaClosed = true;
    }

    protected void processControllerClosed ( ControllerClosedEvent event ) {
        boolMediaClosed = true;
    }

    protected void processCachingControl ( CachingControlEvent event ) {
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

    protected void processFormatChange ( FormatChangeEvent event ) {
        killCurrentView ();

        Component compControl = mediaPlayer.getVisualComponent();
        if ( compControl != null) {
            mediaPanel.add ( compControl);
            compControl.setVisible(true);
        }
		mediaPanel.repaint();
		mediaPanel.revalidate();
		mediaPanel.setVisible(true);			
    }
}
