package org.javafling.pokerenlighter.players;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class HandTypeChangeEvent extends AbstractEvent
{
    public static final String NAME = "gui.players.handtype";
    
    @Override
    public String getName()
    {
        return HandTypeChangeEvent.NAME;
    }
}
