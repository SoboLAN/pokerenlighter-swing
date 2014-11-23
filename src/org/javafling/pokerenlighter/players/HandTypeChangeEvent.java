package org.javafling.pokerenlighter.players;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class HandTypeChangeEvent extends AbstractEvent
{
    @Override
    public String getName()
    {
        return "gui.players.handtype";
    }
}
