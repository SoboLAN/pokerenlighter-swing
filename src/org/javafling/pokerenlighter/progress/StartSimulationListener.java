package org.javafling.pokerenlighter.progress;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.javafling.pokerenlighter.event.EventTriggerer;

public class StartSimulationListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        SimulationStartEvent event = new SimulationStartEvent();
        
        EventTriggerer.getInstance().fire(event);
    }
}
