/**
 * openOptima: framework and implementations for commonly used algorithms in Graph Theory
 * and Network Optimization in Operations Research.  
 * 
 * Copyright (C) 2008 Yaxiong Lin
 * 
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with 
 * this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, 
 * Suite 330, Boston, MA 02111-1307 USA 
 * 
 * @author Yaxiong Lin
 * @version 1.0
 * 
 */
package openOptima;


import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
 * A dialog window to allow the user to load, start and abort the algorithm execution.
 * @author yxl01
 *
 */
public class ProgressMonitor extends JFrame implements ActionListener {
    public final static int ONE_SECOND = 1000;
    public final static String START = "Start";
    public final static String ABORT = "Abort";
    public final static String LOAD = "Load";
    
    private JProgressBar progressBar;
    private Timer timer;
    private JButton abortButton;
    private JButton startButton;
    private JButton loadButton;
    private JTextArea consoleOutput;
    private ActionListener callerListener;

    public ProgressMonitor(String title_p, ActionListener listener_p) {
        super(title_p);

        this.callerListener = listener_p;
        
        abortButton = new JButton("Abort");
        abortButton.setActionCommand(ABORT);
        abortButton.addActionListener(this);
        
        startButton = new JButton("Start");
        startButton.setActionCommand(START);
        startButton.addActionListener(this);

        loadButton = new JButton("Load");
        loadButton.setActionCommand(LOAD);
        loadButton.addActionListener(this);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        consoleOutput = new JTextArea(50, 20);
        consoleOutput.setMargin(new Insets(5,5,5,5));
        consoleOutput.setEditable(true);

        JPanel panel = new JPanel();
        panel.add(this.progressBar);
        panel.add(this.abortButton);
        panel.add(this.startButton);
        panel.add(this.loadButton);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(consoleOutput), BorderLayout.CENTER);
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        this.timer = new Timer (ONE_SECOND, this);
        this.timer.stop();
        this.pack();
        this.setVisible(true);
        this.startButton.setEnabled(false);
        this.abortButton.setEnabled(false);
        this.loadButton.setEnabled(true);
        this.progressBar.setValue(0);
    }
    

    /**
     * Sets the progress bar to the value between 0 and 100 (%).
     * @param newValue_p
     */
    public void setProgress (int newValue_p) {
    	this.progressBar.setValue(newValue_p);
    }
    
    /**
     * Sets the console text.
     * @param text_p
     */
    public void setText(String text_p) {
    	this.consoleOutput.setText(text_p);
    }
    
    /**
     * Returns the console text.
     * @return
     */
    public String getText() {
    	return this.consoleOutput.getText();
    }
    
    /**
     * Stops the progress monitor (Timer event).
     *
     */
    public void stop () {
        this.timer.stop();
        this.startButton.setEnabled(false);
        this.abortButton.setEnabled(false);
        this.loadButton.setEnabled(true);
        this.progressBar.setValue(100);
    }
    
    /**
     * Event action performer.
     */
    public void actionPerformed(ActionEvent evt) {
       String evtString = evt.getActionCommand();
       if (evtString==null) this.callerListener.actionPerformed(evt);
       else if (evtString.equalsIgnoreCase(START)) {
           Toolkit.getDefaultToolkit().beep();
           this.startButton.setEnabled(false);
           this.abortButton.setEnabled(true);
           this.loadButton.setEnabled(false);
           this.progressBar.setValue(0);
           this.timer.start();
    	   this.callerListener.actionPerformed(evt);
       }
       else if (evtString.equalsIgnoreCase(ABORT)) {
           Toolkit.getDefaultToolkit().beep();
           this.timer.stop();
           this.startButton.setEnabled(true);
           this.abortButton.setEnabled(false);
           this.loadButton.setEnabled(true);
           this.progressBar.setValue(0);
    	   this.callerListener.actionPerformed(evt);
       }
       else if (evtString.equalsIgnoreCase(LOAD)) {
           Toolkit.getDefaultToolkit().beep();
           this.timer.stop();
           this.startButton.setEnabled(true);
           this.abortButton.setEnabled(false);
           this.loadButton.setEnabled(true);
           this.progressBar.setValue(0);
    	   this.callerListener.actionPerformed(evt);
       }
       else this.callerListener.actionPerformed(evt);
    } 

    /**
     * Sets the alert message.  Right now just writes the message to console. 
     * @param msg_p
     */
    public void alert (String msg_p) {
        Toolkit.getDefaultToolkit().beep();
    	this.consoleOutput.setText(msg_p);
    }
}
