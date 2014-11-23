package org.javafling.pokerenlighter.generalchoices;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerNumberModel;
import org.javafling.pokerenlighter.simulation.PokerType;

public final class GeneralChoicesPanel extends JPanel
{
    private JSpinner playersCount;
    private JComboBox<String> variationBox;
    
    public GeneralChoicesPanel(int maxPlayers)
    {
        super(new GridLayout(1, 2));
        
        JPanel nrPlayersPanel = this.createNrPlayersPanel(maxPlayers);
        JPanel variationPanel = this.createPokerTypePanel();
        
        this.add(nrPlayersPanel);
        this.add(variationPanel);
    }
    
    private JPanel createNrPlayersPanel(int maxPlayers)
    {
        JPanel nrPlayersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        SpinnerNumberModel playersModel = new SpinnerNumberModel(2, 2, maxPlayers, 1);        
        this.playersCount = new JSpinner (playersModel);
        this.playersCount.addChangeListener(new PlayerCountListener());
        
        DefaultEditor editor = ((DefaultEditor) this.playersCount.getEditor());
        editor.getTextField().setEditable(false);
        
        nrPlayersPanel.add(new JLabel("Number of Players:"));
        nrPlayersPanel.add(this.playersCount);
        
        return nrPlayersPanel;
    }
    
    private JPanel createPokerTypePanel()
    {
        JPanel pokerTypePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        String[] variationNames = new String[]{
            PokerType.TEXAS_HOLDEM.toString(),
            PokerType.OMAHA.toString(),
            PokerType.OMAHA_HILO.toString()
        };
        
        this.variationBox = new JComboBox<>(variationNames);
        this.variationBox.addItemListener(new PokerTypeListener());
        this.variationBox.setEditable(false);
        pokerTypePanel.add(new JLabel("Poker Type:"));
        pokerTypePanel.add(this.variationBox);
        
        return pokerTypePanel;
    }
    
    public void setSelectedPokerTypeIndex(int index)
    {
        this.variationBox.setSelectedIndex(index);
    }
    
    public void setSelectedPokerTypeEnabled(boolean enabled)
    {
        this.variationBox.setEnabled(enabled);
    }
    
    public PokerType getSelectedVariation()
    {
        int selectedVariation = this.variationBox.getSelectedIndex();

        switch(selectedVariation) {
            case 0: return PokerType.TEXAS_HOLDEM;
            case 1: return PokerType.OMAHA;
            case 2: return PokerType.OMAHA_HILO;
            default: return null;
        }
    }
    
    public void setPlayerCount(int value)
    {
        this.playersCount.setValue(value);
    }
    
    public void setPlayerCountEnabled(boolean enabled)
    {
        this.playersCount.setEnabled(enabled);
    }
    
    public int getPlayerCount()
    {
        //playersCount might not yet be created(depends on the order of creation of panels)
        if (this.playersCount == null) {
            return 2;
        } else {
            Integer c = (Integer) this.playersCount.getValue();
        
            return c;
        }
    }
}
