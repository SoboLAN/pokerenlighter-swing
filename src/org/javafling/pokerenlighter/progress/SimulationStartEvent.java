package org.javafling.pokerenlighter.progress;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class SimulationStartEvent extends AbstractEvent
{
    public static final String NAME = "gui.progress.start";
    
    @Override
    public String getName()
    {
        return SimulationStartEvent.NAME;
    }
}
