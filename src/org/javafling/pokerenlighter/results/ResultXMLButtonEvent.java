package org.javafling.pokerenlighter.results;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class ResultXMLButtonEvent extends AbstractEvent
{
    public static final String NAME = "gui.results.xml";
    
    @Override
    public String getName()
    {
        return ResultXMLButtonEvent.NAME;
    }
}
