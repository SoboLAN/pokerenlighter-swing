package org.javafling.pokerenlighter.results;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.javafling.pokerenlighter.event.EventTriggerer;

public class ViewGraphListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        ViewGraphButtonEvent event = new ViewGraphButtonEvent();
        
        EventTriggerer.getInstance().fire(event);
    }
}