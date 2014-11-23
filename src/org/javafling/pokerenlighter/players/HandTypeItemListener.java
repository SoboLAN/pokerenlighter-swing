package org.javafling.pokerenlighter.players;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.javafling.pokerenlighter.event.EventTriggerer;

public class HandTypeItemListener implements ItemListener
{
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            return;
        }
        
        HandTypeChangeEvent event = new HandTypeChangeEvent();
        
        EventTriggerer.getInstance().fire(event);
    }
}
