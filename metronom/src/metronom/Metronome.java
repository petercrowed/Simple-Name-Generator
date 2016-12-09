package metronom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.util.logging.Level;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;



public class Metronome extends JFrame {
	public static void main(String[] args) {
		Runnable app = new Runnable() {
			public void run() {
				Metronome t = new Metronome();
				t.setVisible(true);
			}
		};
		SwingUtilities.invokeLater(app);
	}

	public Metronome() {
		super("Metronome App");
		// code to use the operating system look.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println(e);
		}

		Thread thread;
		boolean keepPlaying;
		
		final JSlider sBPM = new JSlider(JSlider.VERTICAL, 20, 200, 120);
		sBPM.setPaintLabels(true);
		sBPM.setPaintTicks(true);
		sBPM.setMajorTickSpacing(20);
		sBPM.setMinorTickSpacing(10);
		java.util.Hashtable labelSlider = new java.util.Hashtable();
		labelSlider.put(new Integer(200), new JLabel(" 200 Prestissimo"));
		labelSlider.put(new Integer(180), new JLabel(" 180 Presto"));
		labelSlider.put(new Integer(160), new JLabel(" 160"));
		labelSlider.put(new Integer(140), new JLabel(" 140"));
		labelSlider.put(new Integer(130), new JLabel(" 130 Allegro"));
		labelSlider.put(new Integer(120), new JLabel(" 120"));
		labelSlider.put(new Integer(100), new JLabel(" 100 Moderato"));
		labelSlider.put(new Integer(90), new JLabel(" 90 Andante"));
		labelSlider.put(new Integer(80), new JLabel(" 80"));
		labelSlider.put(new Integer(70), new JLabel(" 70 Adagio"));
		labelSlider.put(new Integer(60), new JLabel(" 60"));
		labelSlider.put(new Integer(50), new JLabel(" 50 Lento"));
		labelSlider.put(new Integer(40), new JLabel(" 40"));
		labelSlider.put(new Integer(20), new JLabel(" 20"));
		sBPM.setLabelTable(labelSlider);
		sBPM.setPaintLabels(true);
		sBPM.setPreferredSize(new Dimension(150, 400));
		sBPM.setBorder(new TitledBorder("Beats per minute (BPM)"));
		final JSlider sDuration = new JSlider(JSlider.VERTICAL, 0, 120, 5);
		sDuration.setPaintLabels(true);
		sDuration.setPaintTicks(true);
		sDuration.setMajorTickSpacing(20);
		sDuration.setMinorTickSpacing(10);
		sDuration.setPreferredSize(new Dimension(70, 400));
		sDuration.setBorder(new TitledBorder("Seconds"));
		JPanel pMain = new JPanel(new BorderLayout());
		pMain.add(sBPM, BorderLayout.EAST);
		pMain.add(sDuration, BorderLayout.CENTER);
		JButton bBeats = new JButton("Listen to Beats");

		bBeats.addActionListener(new ActionListener() {
			boolean hasBeenClicked = true;

			public void actionPerformed(ActionEvent ae) {

				try {
					generateBeats(sBPM.getValue(), sDuration.getValue());
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(bBeats, BorderLayout.WEST);
		pMain.add(panel, BorderLayout.NORTH);
		pMain.setBorder(new javax.swing.border.EmptyBorder(5, 3, 5, 3));
		getContentPane().add(pMain);
		pack();
		setLocation(0, 20);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

	}

	public void generateBeats(double bpm, int duration) throws LineUnavailableException {

	
	        
	    
	        Timer timer = new Timer("MetronomeTimer", true);
			TimerTask tone = new TimerTask(){
			     @Override
			     public void run(){
			    	 byte[] buf = new byte[ 1 ];;
					    AudioFormat af = new AudioFormat( (float )44100, 8, 1, true, false );
					    SourceDataLine sdl = null;
						try {
							sdl = AudioSystem.getSourceDataLine( af );
						} catch (LineUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    try {
							sdl.open();
						} catch (LineUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    sdl.start();
					    for( int i = 0; i < 20 * (float )44100 / 1000; i++ ) {
					        double angle = i / ( (float )44100 / 440 ) * 2.0 * Math.PI;
					        buf[ 0 ] = (byte )( Math.sin( angle ) * 100 );
					        sdl.write( buf, 0, 1 );
					    }
					    sdl.drain();
					    sdl.stop();
			     }
			};
			
		float tempo;

		tempo = (float) (1000 * 60 / bpm);
		System.out.println(tempo);
		timer.scheduleAtFixedRate(tone, (long) tempo, (long) tempo); // 120 BPM.
																		// Executes
																		// every
																		// 500
																		// ms.

	}
}