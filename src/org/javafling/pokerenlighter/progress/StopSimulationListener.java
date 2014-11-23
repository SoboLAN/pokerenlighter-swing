package org.javafling.pokerenlighter.progress;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.javafling.pokerenlighter.event.EventTriggerer;

public class StopSimulationListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        SimulationStopEvent event = new SimulationStopEvent();
        
        EventTriggerer.getInstance().fire(event);
    }
}
