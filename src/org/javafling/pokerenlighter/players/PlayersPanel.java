package org.javafling.pokerenlighter.players;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.javafling.pokerenlighter.simulation.HandType;

public class PlayersPanel extends JPanel
{
    private JComboBox<Integer> playerIDBox;
    private JComboBox<String> handTypeBox;
    private JButton selectButton;
    private JTable choicesTable;
    
    public PlayersPanel(int maxPlayers)
    {
        super(new BorderLayout());
        
        JPanel topPanel = this.createTopPanel(maxPlayers);
        this.add(topPanel, BorderLayout.NORTH);
        
        JPanel choicesPanel = this.createChoicesPanel(maxPlayers, 2);
        this.add(choicesPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel(int maxPlayers)
    {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        topPanel.add (new JLabel("Player:"));
        
        Integer[] IDs = new Integer[maxPlayers];
        for (int i = 0; i < IDs.length; i++) {
            IDs[i] = i + 1;
        }
        this.playerIDBox = new JComboBox<>(IDs);
        this.playerIDBox.setEditable(false);
        topPanel.add(this.playerIDBox);
        
        topPanel.add(new JLabel("Hand Type:"));
        
        String[] handBoxOptions = {"Random", "Range", "Exact Cards"};

        this.handTypeBox = new JComboBox<>(handBoxOptions);
        this.handTypeBox.setEditable(false);
        this.handTypeBox.addItemListener(new HandTypeItemListener());
        topPanel.add(this.handTypeBox);
        
        this.selectButton = new JButton("Choose Range");
        topPanel.add(this.selectButton);
        this.selectButton.addActionListener(new SelectButtonListener());
        
        return topPanel;
    }
    
    private JPanel createChoicesPanel(int maxPlayers, int initialPlayerCount)
    {
        JPanel panel = new JPanel(new BorderLayout());
    
        String[] titles = {"Player", "Hand Type", "Selection"};
        
        Object[][] rows = new String[maxPlayers][3];
        for (int i = 0; i < initialPlayerCount; i++) {
            rows[i][0] = Integer.toString(i + 1);
        }
        for (int i = initialPlayerCount; i < maxPlayers; i++) {
            rows[i][0] = " ";
        }

        DefaultTableModel model = new DefaultTableModel(rows, titles)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        this.choicesTable = new JTable(model);
        
        this.choicesTable.getColumnModel().setColumnSelectionAllowed(false);
        this.choicesTable.getTableHeader().setReorderingAllowed(false);
        
        for (int i = 0; i < 3; i++) {
            TableColumn col = this.choicesTable.getColumnModel().getColumn(i);
            
            DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
            
            dtcr.setHorizontalAlignment(SwingConstants.CENTER);
            
            col.setCellRenderer(dtcr);
        }

        panel.add(this.choicesTable.getTableHeader(), BorderLayout.PAGE_START);
        panel.add(this.choicesTable, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void setHandTypeSelectedIndex(int index)
    {
        this.handTypeBox.setSelectedIndex(index);
    }
    
    public HandType getSelectedHandType()
    {
        int selectedType = this.handTypeBox.getSelectedIndex();
        
        switch(selectedType) {
            case 0: return HandType.RANDOM;
            case 1: return HandType.RANGE;
            case 2: return HandType.EXACTCARDS;
            default: return null;
        }
    }
    
    public void setHandTypeEnabled(boolean enabled)
    {
        this.handTypeBox.setEnabled(enabled);
    }
    
    public void setPlayerIDEnabled(boolean enabled)
    {
        this.playerIDBox.setEnabled(enabled);
    }
    
    public TableModel getChoicesTableModel()
    {
        return this.choicesTable.getModel();
    }
    
    public int getPlayerIDSelectedIndex()
    {
        return this.playerIDBox.getSelectedIndex();
    }
    
    public void setSelectButtonText(String text)
    {
        this.selectButton.setText(text);
    }
    
    public void playerIDRemoveAllItems()
    {
        this.playerIDBox.removeAllItems();
    }
    
    public void playerIDAddItem(Integer item)
    {
        this.playerIDBox.addItem(item);
    }
}
