package org.javafling.pokerenlighter.progress;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressPanel extends JPanel
{
    private JButton startButton, stopButton;
    private JProgressBar progressBar;
    
    public ProgressPanel()
    {
        super(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        this.startButton = new JButton("Start");
        this.startButton.addActionListener(new StartSimulationListener());
        
        this.stopButton = new JButton("Stop");
        this.stopButton.addActionListener(new StopSimulationListener());

        this.progressBar = new JProgressBar(0, 100);
        this.progressBar.setPreferredSize(new Dimension(220, 20));        
        this.progressBar.setStringPainted(true);

        this.add(this.startButton);
        this.add(this.stopButton);
        this.add(new JLabel("Progress:"));
        this.add(this.progressBar);
    }
    
    public void setStopButtonEnabled(boolean enabled)
    {
        this.stopButton.setEnabled(enabled);
    }
    
    public void setStartButtonEnabled(boolean enabled)
    {
        this.startButton.setEnabled(enabled);
    }
    
    public void setProgressBarValue(int value)
    {
        this.progressBar.setValue(value);
    }
}
