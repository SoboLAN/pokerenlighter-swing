package org.javafling.pokerenlighter.players;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class SelectButtonEvent extends AbstractEvent
{
    public static final String NAME = "gui.players.selectbutton";
    
    @Override
    public String getName()
    {
        return SelectButtonEvent.NAME;
    }
}
