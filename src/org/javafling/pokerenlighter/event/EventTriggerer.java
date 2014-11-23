package org.javafling.pokerenlighter.event;

import java.util.HashMap;
import java.util.ArrayList;

public class EventTriggerer
{
    private static EventTriggerer _instance;
    
    private HashMap<String, ArrayList<ListenerInterface>> listeners;
    
    public static EventTriggerer getInstance()
    {
        if (_instance == null) {
            _instance = new EventTriggerer();
        }
        
        return _instance;
    }
    
    private EventTriggerer()
    {
        this.listeners = new HashMap<>();
    }
    
    public void fire(AbstractEvent event)
    {
        String eventName = event.getName();
        
        ArrayList<ListenerInterface> listenerList = this.listeners.get(eventName);
        if (listenerList != null) {
            for (ListenerInterface listener : listenerList) {
                listener.onUIUpdate(event);
            }
        }
    }
    
    public void addListener(ListenerInterface listener, String eventName)
    {
        if (! this.listeners.containsKey(eventName)) {
            this.listeners.put(eventName, new ArrayList<ListenerInterface>());
        }
        
        this.listeners.get(eventName).add(listener);
    }
}
