package org.javafling.pokerenlighter.generalchoices;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class PokerTypeChangeEvent extends AbstractEvent
{
    public static final String NAME = "gui.generalchoices.pokertype";
    
    @Override
    public String getName()
    {
        return PokerTypeChangeEvent.NAME;
    }
}
