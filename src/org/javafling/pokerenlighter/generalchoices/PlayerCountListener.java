package org.javafling.pokerenlighter.generalchoices;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.javafling.pokerenlighter.event.EventTriggerer;

public class PlayerCountListener implements ChangeListener
{
    @Override
    public void stateChanged(ChangeEvent e)
    {
        PlayerCountChangeEvent event = new PlayerCountChangeEvent();
        
        EventTriggerer.getInstance().fire(event);
    }
}
