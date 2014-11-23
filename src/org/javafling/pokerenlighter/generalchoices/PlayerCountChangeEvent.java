package org.javafling.pokerenlighter.generalchoices;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class PlayerCountChangeEvent extends AbstractEvent
{
    public static final String NAME = "gui.generalchoices.playercount";
    
    @Override
    public String getName()
    {
        return PlayerCountChangeEvent.NAME;
    }
}
