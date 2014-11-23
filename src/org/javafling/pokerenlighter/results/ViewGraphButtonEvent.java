package org.javafling.pokerenlighter.results;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class ViewGraphButtonEvent extends AbstractEvent
{
    public static final String NAME = "gui.results.viewgraph";
    
    @Override
    public String getName()
    {
        return ViewGraphButtonEvent.NAME;
    }
}
