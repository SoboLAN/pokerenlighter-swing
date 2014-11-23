package org.javafling.pokerenlighter.event;

public abstract class AbstractEvent
{
    private Object data;
    
    public void setData(Object data)
    {
        this.data = data;
    }
    
    public Object getData()
    {
        return this.data;
    }
    
    public abstract String getName();
}
