package org.javafling.pokerenlighter.results;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.javafling.pokerenlighter.event.EventTriggerer;

public class ResultXMLListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        ResultXMLButtonEvent event = new ResultXMLButtonEvent();
        
        EventTriggerer.getInstance().fire(event);
    }
}