package org.javafling.pokerenlighter.community;

import java.awt.FlowLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.javafling.pokerenlighter.general.Street;

public class CommunityPanel extends JPanel
{
    private JLabel flopCard1, flopCard2, flopCard3, turnCard, riverCard;
    private JCheckBox enableFlop, enableTurn, enableRiver;
    
    public CommunityPanel()
    {
        super(new FlowLayout(FlowLayout.CENTER));
        
        this.enableFlop = new JCheckBox("Flop:");
        
        this.flopCard1 = new JLabel();
        this.flopCard2 = new JLabel();
        this.flopCard3 = new JLabel();
        
        this.add(this.enableFlop);
        this.add(this.flopCard1);
        this.add(this.flopCard2);
        this.add(this.flopCard3);
    
        this.enableTurn = new JCheckBox("Turn:");
        
        this.turnCard = new JLabel();
        
        this.add(this.enableTurn);
        this.add(this.turnCard);

        this.enableRiver = new JCheckBox("River:");
        
        this.riverCard = new JLabel();
        
        this.add(this.enableRiver);
        this.add(this.riverCard);
        
        this.flopCard1.addMouseListener(new CommunityCardsListener(Street.FLOP));
        this.flopCard2.addMouseListener(new CommunityCardsListener(Street.FLOP));
        this.flopCard3.addMouseListener(new CommunityCardsListener(Street.FLOP));
        this.turnCard.addMouseListener(new CommunityCardsListener(Street.TURN));
        this.riverCard.addMouseListener(new CommunityCardsListener(Street.RIVER));
    }
    
    public void setFlopEnabled(boolean enabled)
    {
        this.enableFlop.setEnabled(enabled);
    }
    
    public void setTurnEnabled(boolean enabled)
    {
        this.enableTurn.setEnabled(enabled);
    }
    
    public void setRiverEnabled(boolean enabled)
    {
        this.enableRiver.setEnabled(enabled);
    }
    
    public boolean isFlopSelected()
    {
        return this.enableFlop.isSelected();
    }
    
    public boolean isTurnSelected()
    {
        return this.enableTurn.isSelected();
    }
    
    public boolean isRiverSelected()
    {
        return this.enableRiver.isSelected();
    }
    
    public void setFlopSelected(boolean selected)
    {
        this.enableFlop.setSelected(selected);
    }
    
    public void setTurnSelected(boolean selected)
    {
        this.enableTurn.setSelected(selected);
    }
    
    public void setRiverSelected(boolean selected)
    {
        this.enableRiver.setSelected(selected);
    }
    
    public boolean isFlopEnabled()
    {
        return this.enableFlop.isEnabled();
    }
    
    public void setIconFlop1(Icon icon)
    {
        this.flopCard1.setIcon(icon);
    }
    
    public void setIconFlop2(Icon icon)
    {
        this.flopCard2.setIcon(icon);
    }
    
    public void setIconFlop3(Icon icon)
    {
        this.flopCard3.setIcon(icon);
    }
    
    public void setIconTurn(Icon icon)
    {
        this.turnCard.setIcon(icon);
    }
    
    public void setIconRiver(Icon icon)
    {
        this.riverCard.setIcon(icon);
    }
}
