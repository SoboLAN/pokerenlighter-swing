package org.javafling.pokerenlighter.generalchoices;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.javafling.pokerenlighter.event.EventTriggerer;

public final class PokerTypeListener implements ItemListener
{
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        PokerTypeChangeEvent event = new PokerTypeChangeEvent();
        
        EventTriggerer.getInstance().fire(event);
    }
}
