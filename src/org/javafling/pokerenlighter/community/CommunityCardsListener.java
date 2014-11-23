package org.javafling.pokerenlighter.community;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.javafling.pokerenlighter.event.EventTriggerer;
import org.javafling.pokerenlighter.general.Street;

public class CommunityCardsListener extends MouseAdapter
{
    private Street street;
    
    public CommunityCardsListener(Street street)
    {
        this.street = street;
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        CommunityMouseEvent event = new CommunityMouseEvent();
        event.setData(this.street);
        
        EventTriggerer.getInstance().fire(event);
    }
}
