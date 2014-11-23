package org.javafling.pokerenlighter.players;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.javafling.pokerenlighter.event.EventTriggerer;

public class SelectButtonListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        SelectButtonEvent event = new SelectButtonEvent();
        
        EventTriggerer.getInstance().fire(event);
    }
}
