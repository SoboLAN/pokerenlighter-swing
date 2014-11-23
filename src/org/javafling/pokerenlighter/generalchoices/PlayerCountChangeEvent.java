package org.javafling.pokerenlighter.generalchoices;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class PlayerCountChangeEvent extends AbstractEvent
{
    @Override
    public String getName()
    {
        return "gui.generalchoices.playercount";
    }
}
