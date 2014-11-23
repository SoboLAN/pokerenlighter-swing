package org.javafling.pokerenlighter.community;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class CommunityMouseEvent extends AbstractEvent
{
    public static final String NAME = "gui.community.mouseevent";
    
    @Override
    public String getName()
    {
        return CommunityMouseEvent.NAME;
    }
}
