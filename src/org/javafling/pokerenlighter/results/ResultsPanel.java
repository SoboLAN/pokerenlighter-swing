package org.javafling.pokerenlighter.results;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ResultsPanel extends JPanel
{
    private JTable resultsTable;
    private JButton exportButton, viewGraphButton;
    
    public ResultsPanel(int maxPlayers)
    {
        super(new BorderLayout());
        
        String[] titles = {"Player", "Wins", "Loses", "Ties"};
        
        Object[][] rows = new String[maxPlayers][4];
        
        for (int i = 0; i < maxPlayers; i++) {
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
        
        this.resultsTable = new JTable(model);
    
        this.resultsTable.getTableHeader().setReorderingAllowed(false);
        this.resultsTable.getColumnModel().setColumnSelectionAllowed(false);
        
        for (int i = 0; i < 4; i++) {
            DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
            dtcr.setHorizontalAlignment(SwingConstants.CENTER);
            
            TableColumn col = this.resultsTable.getColumnModel().getColumn(i);
            col.setCellRenderer(dtcr);
        }

        this.add(this.resultsTable.getTableHeader(), BorderLayout.PAGE_START);
        this.add(this.resultsTable, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        this.exportButton = new JButton("Export to XML");
        this.exportButton.addActionListener(new ResultXMLListener());
        this.viewGraphButton = new JButton("View Graph");
        this.viewGraphButton.addActionListener(new ViewGraphListener());
        
        buttonsPanel.add(viewGraphButton);
        buttonsPanel.add(exportButton);

        this.add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    public void setViewGraphButtonEnabled(boolean enabled)
    {
        this.viewGraphButton.setEnabled(enabled);
    }
    
    public void setExportButtonEnabled(boolean enabled)
    {
        this.exportButton.setEnabled(enabled);
    }
    
    public TableModel getResultsTableModel()
    {
        return this.resultsTable.getModel();
    }
}
