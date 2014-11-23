package org.javafling.pokerenlighter.progress;

import org.javafling.pokerenlighter.event.AbstractEvent;

public class SimulationStopEvent extends AbstractEvent
{
    public static final String NAME = "gui.progress.stop";
    
    @Override
    public String getName()
    {
        return SimulationStopEvent.NAME;
    }
}
