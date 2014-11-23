package org.javafling.pokerenlighter.general;

import org.javafling.pokerenlighter.utilities.GUIUtilities;
import org.javafling.pokerenlighter.utilities.OptionsContainer;
import org.javafling.pokerenlighter.statusbar.StatusBar;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.community.CommunityMouseEvent;
import org.javafling.pokerenlighter.community.CommunityPanel;
import org.javafling.pokerenlighter.event.AbstractEvent;
import org.javafling.pokerenlighter.event.EventTriggerer;
import org.javafling.pokerenlighter.event.ListenerInterface;
import org.javafling.pokerenlighter.generalchoices.GeneralChoicesPanel;
import org.javafling.pokerenlighter.generalchoices.PlayerCountChangeEvent;
import org.javafling.pokerenlighter.generalchoices.PokerTypeChangeEvent;
import org.javafling.pokerenlighter.players.HandTypeChangeEvent;
import org.javafling.pokerenlighter.players.PlayersPanel;
import org.javafling.pokerenlighter.players.SelectButtonEvent;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.PlayerProfile;
import org.javafling.pokerenlighter.simulation.PokerType;
import org.javafling.pokerenlighter.simulation.Range;
import org.javafling.pokerenlighter.simulation.SimulationEvent;
import org.javafling.pokerenlighter.simulation.SimulationExport;
import org.javafling.pokerenlighter.simulation.SimulationFinalResult;
import org.javafling.pokerenlighter.simulation.SimulationNotifiable;
import org.javafling.pokerenlighter.simulation.Simulator;

/** 
 * Main GUI (Graphical User Interface) class.
 */
public final class GUI implements SimulationNotifiable, ListenerInterface
{
    private static GUI _instance;
    
    private static final int MAX_PLAYERS = 7;
    
    private String windowTitle = "Poker Enlighter";
    private String blankCardPath = "images/blank.card.gif";
    
    private JFrame mainframe;
    
    private PlayersPanel playersPanel;
    private JPanel progressPanel;
    private GeneralChoicesPanel generalChoicesPanel;
    private CommunityPanel communityPanel;
    private JPanel resultsPanel;
    
    private StatusBar statusBar;
    
    private JTable resultsTable;
    
    private JButton startButton, stopButton, exportButton, viewGraphButton;
    
    private JProgressBar progressBar;
    
    private PlayerProfile[] holdemProfiles, omahaProfiles, omahaHiLoProfiles;
    private Card[] holdemCommunityCards, omahaCommunityCards, omahaHiLoCommunityCards;
    
    private Simulator simulator;

    public static GUI getGUI()
    {
        if (_instance == null) {
            _instance = new GUI();
        }

        return _instance;
    }
    
    private GUI()
    {
        EventTriggerer.getInstance().addListener(this, new PlayerCountChangeEvent().getName());
        EventTriggerer.getInstance().addListener(this, new PokerTypeChangeEvent().getName());
        EventTriggerer.getInstance().addListener(this, new HandTypeChangeEvent().getName());
        EventTriggerer.getInstance().addListener(this, new SelectButtonEvent().getName());
        EventTriggerer.getInstance().addListener(this, new CommunityMouseEvent().getName());
        
        this.holdemProfiles = new PlayerProfile[GUI.MAX_PLAYERS];
        this.omahaProfiles = new PlayerProfile[GUI.MAX_PLAYERS];
        this.omahaHiLoProfiles = new PlayerProfile[GUI.MAX_PLAYERS];
        
        this.holdemCommunityCards = new Card[5];
        this.omahaCommunityCards = new Card[5];
        this.omahaHiLoCommunityCards = new Card[5];

        this.mainframe = new JFrame(this.windowTitle);
        this.mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.mainframe.setIconImage(
            Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ace_spades_icon.jpg"))
        );
        
        MenuBar menuBar = new MenuBar(this.mainframe);
        this.mainframe.setJMenuBar(menuBar.getMenuBar());
        this.mainframe.setLayout(new BorderLayout());
        
        JPanel customPanel = createCustomPanel();
        this.mainframe.add(customPanel, BorderLayout.CENTER);
        
        this.statusBar = new StatusBar("Ready");
        this.mainframe.add(this.statusBar, BorderLayout.SOUTH);
        
        this.newSimulation();
        
        this.setChoicesTableContent();

        this.mainframe.pack();
    }

    public void setLocation(int x, int y)
    {
        this.mainframe.setLocation(x, y);
    }
    
    public void setLocationToCenterOfScreen()
    {
        this.mainframe.setLocationRelativeTo(null);
    }
    
    public void setVisible(boolean on)
    {
        this.mainframe.setVisible(on);
    }
    
    public void setResizable(boolean on)
    {
        this.mainframe.setResizable(on);
    }
    
    @Override
    public void onUIUpdate(AbstractEvent event)
    {
        if (event instanceof PlayerCountChangeEvent) {
            this.setChoicesTableContent();
            this.adjustAvailablePlayerIDs();
        } else if (event instanceof PokerTypeChangeEvent) {
            this.setChoicesTableContent();
            this.setCommunityCardsContent();
        } else if (event instanceof HandTypeChangeEvent) {
            this.handTypeRefresh();
        } else if (event instanceof SelectButtonEvent) {
            this.playersButtonSelectAction();
        } else if (event instanceof CommunityMouseEvent) {
            Street street = (Street) event.getData();
            this.communityCardMouseClickAction(street);
        }
    }
    
    public void newSimulation()
    {
        if (this.simulator != null) {
            if (this.simulator.isRunning()) {
                return;
            }
        }
        
        this.simulator = null;
        
        for (int i = 0; i < GUI.MAX_PLAYERS; i++) {
            this.holdemProfiles[i] = new PlayerProfile(HandType.RANDOM, null, null);
            this.omahaProfiles[i] = new PlayerProfile(HandType.RANDOM, null, null);
            this.omahaHiLoProfiles[i] = new PlayerProfile(HandType.RANDOM, null, null);
        }
        
        for (int i = 0; i < 5; i++) {
            this.holdemCommunityCards[i] = null;
            this.omahaCommunityCards[i] = null;
            this.omahaHiLoCommunityCards[i] = null;
        }
        
        this.generalChoicesPanel.setPlayerCount(2);
        this.adjustAvailablePlayerIDs();
        
        this.generalChoicesPanel.setSelectedPokerTypeIndex(0);
        
        this.playersPanel.setHandTypeSelectedIndex(0);
        
        setCommunityCardsContent();
        
        setResultsTableContent(null);
        
        setChoicesTableContent();
        
        this.stopButton.setEnabled(false);
        this.viewGraphButton.setEnabled(false);
        this.exportButton.setEnabled(false);
        
        this.progressBar.setValue(0);
        
        this.statusBar.setText("Ready");
    }
    
    private JPanel createCustomPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());

        this.generalChoicesPanel = new GeneralChoicesPanel(MAX_PLAYERS);
        GUIUtilities.setBorder(this.generalChoicesPanel, "General Options", TitledBorder.CENTER);
        panel.add(this.generalChoicesPanel, BorderLayout.NORTH);

        JPanel middlePanel = new JPanel(new BorderLayout());
        
        this.playersPanel = new PlayersPanel(MAX_PLAYERS);
        GUIUtilities.setBorder(this.playersPanel, "Hand Options", TitledBorder.CENTER);
        middlePanel.add(this.playersPanel, BorderLayout.NORTH);
        
        this.communityPanel = new CommunityPanel();
        GUIUtilities.setBorder(this.communityPanel, "Community Cards", TitledBorder.CENTER);
        middlePanel.add(this.communityPanel, BorderLayout.CENTER);
        
        progressPanel = createProgressPanel();
        middlePanel.add(progressPanel, BorderLayout.SOUTH);

        panel.add(middlePanel, BorderLayout.CENTER);
        
        resultsPanel = createResultsPanel();
        panel.add(resultsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createProgressPanel()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        GUIUtilities.setBorder(panel, "Controls", TitledBorder.CENTER);
                
        startButton = new JButton("Start");
        startButton.addActionListener(new StartSimulationListener());
        
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopSimulationListener());

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(220, 20));        
        progressBar.setStringPainted(true);

        panel.add(startButton);
        panel.add(stopButton);
        panel.add(new JLabel("Progress:"));
        panel.add(progressBar);
        
        return panel;
    }
    
    private JPanel createResultsPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        GUIUtilities.setBorder(panel, "Results", TitledBorder.CENTER);
        
        String[] titles = {"Player", "Wins", "Loses", "Ties"};
        
        Object[][] rows = new String[MAX_PLAYERS][4];
        
        for (int i = 0; i < MAX_PLAYERS; i++) {
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
        
        resultsTable = new JTable(model);
    
        resultsTable.getTableHeader().setReorderingAllowed(false);
        resultsTable.getColumnModel().setColumnSelectionAllowed(false);
        
        for (int i = 0; i < 4; i++) {
            DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
            dtcr.setHorizontalAlignment(SwingConstants.CENTER);
            
            TableColumn col = resultsTable.getColumnModel().getColumn(i);
            col.setCellRenderer(dtcr);
        }

        panel.add(resultsTable.getTableHeader(), BorderLayout.PAGE_START);
        panel.add(resultsTable, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        exportButton = new JButton("Export to XML");
        exportButton.addActionListener(new ResultXMLListener());
        viewGraphButton = new JButton("View Graph");
        viewGraphButton.addActionListener(new ViewGraphListener());
        
        buttonsPanel.add(viewGraphButton);
        buttonsPanel.add(exportButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    @Override
    public void onSimulationStart(final SimulationEvent event)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                playersPanel.setHandTypeEnabled(false);
                generalChoicesPanel.setSelectedPokerTypeEnabled(false);
                playersPanel.setPlayerIDEnabled(false);
                viewGraphButton.setEnabled(false);
                exportButton.setEnabled(false);
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                generalChoicesPanel.setPlayerCountEnabled(false);
                communityPanel.setFlopEnabled(false);
                communityPanel.setTurnEnabled(false);
                communityPanel.setRiverEnabled(false);

                mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                StringBuilder sb = new StringBuilder();
                sb.append("Running");
                sb.append(" - ");
                sb.append(generalChoicesPanel.getPlayerCount());
                sb.append(" Players");
                sb.append(" - ");
                sb.append(OptionsContainer.getOptionsContainer().getRounds());
                sb.append(" Rounds");
                sb.append(" - ");
                sb.append(event.getEventData());
                sb.append(" Threads");

                statusBar.setText(sb.toString());
            }
        });
    }

    @Override
    public void onSimulationDone(final SimulationEvent event)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                SimulationFinalResult result = (SimulationFinalResult) event.getEventData();
                progressBar.setValue(100);
                
                setGUIElementsDone(false, result);
                
                setResultsTableContent(result);
            }
        });
    }

    @Override
    public void onSimulationCancel(SimulationEvent event)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                setGUIElementsDone(true, null);
            }
        });
    }

    @Override
    public void onSimulationProgress(final SimulationEvent event)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                int newValue = (Integer) event.getEventData();
                progressBar.setValue(newValue);
            }
        });
    }

    @Override
    public void onSimulationError(SimulationEvent event)
    {
        
    }
    
    private class StartSimulationListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            simulator = new Simulator(
                generalChoicesPanel.getSelectedVariation(),
                OptionsContainer.getOptionsContainer().getRounds(),
                _instance
            );
            
            simulator.setUpdateInterval(20);
            
            try {
                setPlayers();
                
                setCommunityCards();
                
                simulator.start();
            } catch(Exception ex) {
                GUIUtilities.showErrorDialog(
                    mainframe,
                    "The simulator encountered an error: " + ex.getMessage(),
                    "Simulator Error"
                );
            }
        }
        
        private void setPlayers()
        {
            for (int i = 0; i < generalChoicesPanel.getPlayerCount(); i++) {
                if (generalChoicesPanel.getSelectedVariation() == PokerType.TEXAS_HOLDEM) {
                    simulator.addPlayer(holdemProfiles[i]);
                } else if (generalChoicesPanel.getSelectedVariation() == PokerType.OMAHA) {
                    simulator.addPlayer(omahaProfiles[i]);
                } else if (generalChoicesPanel.getSelectedVariation() == PokerType.OMAHA_HILO) {
                    simulator.addPlayer(omahaHiLoProfiles[i]);
                }
            }
        }

        private void setCommunityCards()
        {
            Card[] flop = new Card[3];
            Card turnCard = null;
            Card riverCard = null;
            if (generalChoicesPanel.getSelectedVariation() == PokerType.TEXAS_HOLDEM) {
                flop[0] = holdemCommunityCards[0];
                flop[1] = holdemCommunityCards[1];
                flop[2] = holdemCommunityCards[2];
                turnCard = holdemCommunityCards[3];
                riverCard = holdemCommunityCards[4];
            } else if (generalChoicesPanel.getSelectedVariation() == PokerType.OMAHA) {
                flop[0] = omahaCommunityCards[0];
                flop[1] = omahaCommunityCards[1];
                flop[2] = omahaCommunityCards[2];
                turnCard = omahaCommunityCards[3];
                riverCard = omahaCommunityCards[4];
            } else if (generalChoicesPanel.getSelectedVariation() == PokerType.OMAHA_HILO) {
                flop[0] = omahaHiLoCommunityCards[0];
                flop[1] = omahaHiLoCommunityCards[1];
                flop[2] = omahaHiLoCommunityCards[2];
                turnCard = omahaHiLoCommunityCards[3];
                riverCard = omahaHiLoCommunityCards[4];
            }
            
            if (communityPanel.isFlopSelected() && flop[0] != null) {
                simulator.setFlop(flop);
            }
            if (communityPanel.isTurnSelected() && turnCard != null) {
                simulator.setTurn(turnCard);
            }
            if (communityPanel.isRiverSelected() && riverCard != null) {
                simulator.setRiver(riverCard);
            }
        }
    }
    
    private class StopSimulationListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            simulator.stop();
        }
    }
    
    private Card[] getSelectedCommunityCards(PokerType selectedPokerType, Street street)
    {
        Card[] selectedCommunityCards = null;

        int nrCards = (street == Street.FLOP) ? 3 : 1;
        int position = (street == Street.FLOP ) ? 0 : (street == Street.TURN ? 3 : 4);
        if (selectedPokerType == PokerType.TEXAS_HOLDEM) {
            for (int i = position; i < position + nrCards; i++) {
                if (this.holdemCommunityCards[i] == null) {
                    return null;
                }
            }

            selectedCommunityCards = new Card[nrCards];
            System.arraycopy(this.holdemCommunityCards, position, selectedCommunityCards, 0, nrCards);
        } else if (selectedPokerType == PokerType.OMAHA) {
            for (int i = position; i < position + nrCards; i++) {
                if (this.omahaCommunityCards[i] == null) {
                    return null;
                }
            }

            selectedCommunityCards = new Card[nrCards];
            System.arraycopy(this.omahaCommunityCards, position, selectedCommunityCards, 0, nrCards);
        } else if (selectedPokerType == PokerType.OMAHA_HILO) {
            for (int i = position; i < position + nrCards; i++) {
                if (this.omahaHiLoCommunityCards[i] == null) {
                    return null;
                }
            }

            selectedCommunityCards = new Card[nrCards];
            System.arraycopy(this.omahaHiLoCommunityCards, position, selectedCommunityCards, 0, nrCards);
        }

        return selectedCommunityCards;
    }
    
    private void updateCommunityCards(PokerType selectedPokerType, Street street, Card[] newCards)
    {
        if (selectedPokerType == PokerType.TEXAS_HOLDEM) {
            if (street == Street.FLOP) {
                this.holdemCommunityCards[0] = newCards[0];
                this.holdemCommunityCards[1] = newCards[1];
                this.holdemCommunityCards[2] = newCards[2];
            } else if (street == Street.TURN) {
                this.holdemCommunityCards[3] = newCards[0];
            } else if (street == Street.RIVER) {
                this.holdemCommunityCards[4] = newCards[0];
            }
        } else if (selectedPokerType == PokerType.OMAHA) {
            if (street == Street.FLOP) {
                this.omahaCommunityCards[0] = newCards[0];
                this.omahaCommunityCards[1] = newCards[1];
                this.omahaCommunityCards[2] = newCards[2];
            } else if (street == Street.TURN) {
                this.omahaCommunityCards[3] = newCards[0];
            } else if (street == Street.RIVER) {
                this.omahaCommunityCards[4] = newCards[0];
            }
        } else if (selectedPokerType == PokerType.OMAHA_HILO) {
            if (street == Street.FLOP) {
                this.omahaHiLoCommunityCards[0] = newCards[0];
                this.omahaHiLoCommunityCards[1] = newCards[1];
                this.omahaHiLoCommunityCards[2] = newCards[2];
            } else if (street == Street.TURN) {
                this.omahaHiLoCommunityCards[3] = newCards[0];
            } else if (street == Street.RIVER) {
                this.omahaHiLoCommunityCards[4] = newCards[0];
            }
        }
    }
    
    private void communityCardMouseClickAction(Street street)
    {
        if (! communityPanel.isFlopEnabled()) {
            return;
        }

        if ((street == Street.FLOP && ! communityPanel.isFlopSelected()) ||
           (street == Street.TURN && ! communityPanel.isTurnSelected()) ||
           (street == Street.RIVER && ! communityPanel.isRiverSelected())) {
            return;
        }

        PokerType selectedPokerType = generalChoicesPanel.getSelectedVariation();

        ArrayList<Card> usedCards = getUsedCards(selectedPokerType);

        Card[] selectedCommunityCards = getSelectedCommunityCards(selectedPokerType, street);

        int nrCards =(street == Street.FLOP) ? 3 : 1;

        CardsDialog cd = new CardsDialog(mainframe, nrCards, selectedCommunityCards, usedCards);
        cd.setLocationRelativeTo(mainframe);
        cd.setVisible(true);

        Card[] newCards = cd.getCards();

        if (newCards != null) {
            updateCommunityCards(selectedPokerType, street, newCards);

            setCommunityCardsContent();
        }
    }
    
    private void setChoicesTableContent()
    {
        PokerType gameType = this.generalChoicesPanel.getSelectedVariation();
        
        int nrPlayersToFill = this.generalChoicesPanel.getPlayerCount();
        
        TableModel model = this.playersPanel.getChoicesTableModel();
        
        for (int i = 0; i < nrPlayersToFill; i++) {
            model.setValueAt(Integer.toString(i + 1), i, 0);
            
            PlayerProfile profile = null;
            if (gameType == PokerType.TEXAS_HOLDEM) {
                profile = holdemProfiles[i];
            } else if (gameType == PokerType.OMAHA) {
                profile = omahaProfiles[i];
            } else if (gameType == PokerType.OMAHA_HILO) {
                profile = omahaHiLoProfiles[i];
            }
            
            if (profile.getHandType() == HandType.RANDOM) {
                model.setValueAt("Random", i, 1);
                model.setValueAt(" ", i, 2);
            } else if (profile.getHandType() == HandType.RANGE) {
                model.setValueAt("Range", i, 1);
                model.setValueAt(Integer.toString(profile.getRange().getPercentage()) + " %", i, 2);
            } else if (profile.getHandType() == HandType.EXACTCARDS) {
                model.setValueAt("Exact Cards", i, 1);
                StringBuilder sb = new StringBuilder();
                Card[] c = profile.getCards();
                for (Card card : c) {
                    sb.append(card.toString());
                }
                model.setValueAt(sb.toString(), i, 2);
            }
        }
        
        for (int i = nrPlayersToFill; i < MAX_PLAYERS; i++) {
            model.setValueAt(" ", i, 0);
            model.setValueAt(" ", i, 1);
            model.setValueAt(" ", i, 2);
        }
    }
    
    private void setCommunityCardsContent()
    {
        PokerType gameType = this.generalChoicesPanel.getSelectedVariation();
        
        Card[] currentCommunityCards = null;
        
        if (gameType == PokerType.TEXAS_HOLDEM) {
            currentCommunityCards = holdemCommunityCards;
        } else if (gameType == PokerType.OMAHA) {
            currentCommunityCards = omahaCommunityCards;
        } else if (gameType == PokerType.OMAHA_HILO) {
            currentCommunityCards = omahaHiLoCommunityCards;
        }
        
        if (currentCommunityCards[0] != null) {
            communityPanel.setFlopEnabled(true);
            communityPanel.setIconFlop1(
                new ImageIcon(getClass().getResource("images/cards/" + currentCommunityCards[0].toString() + ".gif"))
            );
            communityPanel.setIconFlop2(
                new ImageIcon(getClass().getResource("images/cards/" + currentCommunityCards[1].toString() + ".gif"))
            );
            communityPanel.setIconFlop3(
                new ImageIcon(getClass().getResource("images/cards/" + currentCommunityCards[2].toString() + ".gif"))
            );
        } else {
            communityPanel.setFlopSelected(false);
            communityPanel.setIconFlop1(new ImageIcon(getClass().getResource(blankCardPath)));
            communityPanel.setIconFlop2(new ImageIcon(getClass().getResource(blankCardPath)));
            communityPanel.setIconFlop3(new ImageIcon(getClass().getResource(blankCardPath)));
        }
        
        if (currentCommunityCards[3] != null) {
            communityPanel.setTurnEnabled(true);
            communityPanel.setIconTurn(
                new ImageIcon(getClass().getResource("images/cards/" + currentCommunityCards[3].toString() + ".gif"))
            );
        } else {
            communityPanel.setTurnSelected(false);
            communityPanel.setIconTurn(new ImageIcon(getClass().getResource(blankCardPath)));
        }
        
        if (currentCommunityCards[4] != null) {
            communityPanel.setRiverEnabled(true);
            communityPanel.setIconRiver(
                new ImageIcon(getClass().getResource("images/cards/" + currentCommunityCards[4].toString() + ".gif"))
            );
        } else {
            communityPanel.setRiverSelected(false);
            communityPanel.setIconRiver(new ImageIcon(getClass().getResource(blankCardPath)));
        }
    }
    
    private void setResultsTableContent(SimulationFinalResult result)
    {
        TableModel model = resultsTable.getModel();
        
        if (result == null) {
            for (int i = 0; i < MAX_PLAYERS; i++) {
                model.setValueAt(" ", i, 0);
                model.setValueAt(" ", i, 1);
                model.setValueAt(" ", i, 2);
                model.setValueAt(" ", i, 3);
            }
            
            return;
        }
        
        int nrPlayersToFill = result.getNrOfPlayers();

        for (int i = 0; i < nrPlayersToFill; i++) {
            model.setValueAt(Integer.toString(i + 1), i, 0);

            model.setValueAt(result.getFormattedWinPercentage(i) + " %", i, 1);
            model.setValueAt(result.getFormattedLosePercentage(i) + " %", i, 2);
            model.setValueAt(result.getFormattedTiePercentage(i) + " %", i, 3);
        }
        
        for (int i = nrPlayersToFill; i < MAX_PLAYERS; i++) {
            model.setValueAt(" ", i, 0);
            model.setValueAt(" ", i, 1);
            model.setValueAt(" ", i, 2);
            model.setValueAt(" ", i, 3);
        }
    }
    
    
    private void setGUIElementsDone(boolean stopped, SimulationFinalResult result)
    {
        this.playersPanel.setHandTypeEnabled(true);
        this.generalChoicesPanel.setSelectedPokerTypeEnabled(true);
        exportButton.setEnabled(true);
        this.playersPanel.setPlayerIDEnabled(true);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        this.generalChoicesPanel.setPlayerCountEnabled(true);
        communityPanel.setFlopEnabled(true);
        communityPanel.setTurnEnabled(true);
        communityPanel.setRiverEnabled(true);
        
        mainframe.setCursor(Cursor.getDefaultCursor());

        if (! stopped) {
            long duration = result.getDuration();
            double durationSeconds = duration / 1000.0;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);

            statusBar.setText("Done (" + df.format(durationSeconds) + " seconds)");
            
            viewGraphButton.setEnabled(true);
        } else {
            statusBar.setText("Stopped");
            viewGraphButton.setEnabled(false);
        }
    }
        
    private class ViewGraphListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String title = "Simulation Results Graph";
            ResultChartDialog rcd = new ResultChartDialog(mainframe, title, simulator.getResult());
            rcd.setLocationRelativeTo(mainframe);
            rcd.setVisible(true);
        }
    }
    
    public void playersButtonSelectAction()
    {
        PokerType selectedPokerType = generalChoicesPanel.getSelectedVariation();
        HandType selectedHandType = playersPanel.getSelectedHandType();
        int selectedPlayer = playersPanel.getPlayerIDSelectedIndex();
        
        if (selectedHandType == HandType.RANGE) {
            if (selectedPokerType == PokerType.TEXAS_HOLDEM) {
                RangeDialog rd = new RangeDialog(mainframe, holdemProfiles[selectedPlayer].getRange());
            
                rd.setLocationRelativeTo(mainframe);
                rd.setVisible(true);
                
                Range newRange = rd.getRange();
                
                if (! rd.isCancelled()) {
                    if (newRange.getPercentage() == 100) {
                        holdemProfiles[selectedPlayer] = new PlayerProfile(HandType.RANDOM, null, null);
                        
                        setChoicesTableContent();
                    } else if (newRange.getPercentage() != 0) {
                        holdemProfiles[selectedPlayer] = new PlayerProfile(HandType.RANGE, newRange, null);
                        
                        setChoicesTableContent();
                    }
                }            
            }
        } else if (selectedHandType == HandType.EXACTCARDS) {
            ArrayList<Card> usedCards = getUsedCards(selectedPokerType);

            Card[] playerCards = null;
            if (selectedPokerType == PokerType.TEXAS_HOLDEM) {
                playerCards = holdemProfiles[selectedPlayer].getCards();
            } else if (selectedPokerType == PokerType.OMAHA) {
                playerCards = omahaProfiles[selectedPlayer].getCards();
            } else if (selectedPokerType == PokerType.OMAHA_HILO) {
                playerCards = omahaHiLoProfiles[selectedPlayer].getCards();
            }
            
            int nrOfRequestedCards = selectedPokerType == PokerType.TEXAS_HOLDEM ? 2 : 4;
            
            CardsDialog cd = new CardsDialog(mainframe, nrOfRequestedCards, playerCards, usedCards);
            cd.setLocationRelativeTo(mainframe);
            cd.setVisible(true);
            
            Card[] selectedCards = cd.getCards();
            
            if (selectedCards != null) {
                PlayerProfile newProfile = new PlayerProfile(HandType.EXACTCARDS, null, selectedCards);
                
                if (selectedPokerType == PokerType.TEXAS_HOLDEM) {
                    holdemProfiles[selectedPlayer] = newProfile;
                } else if (selectedPokerType == PokerType.OMAHA) {
                    omahaProfiles[selectedPlayer] = newProfile;
                } else if (selectedPokerType == PokerType.OMAHA_HILO) {
                    omahaHiLoProfiles[selectedPlayer] = newProfile;
                }
                    
                setChoicesTableContent();
            }
        }
    }
    
    private void handTypeRefresh()
    {
        HandType selectedHandType = this.playersPanel.getSelectedHandType();
        if (selectedHandType == HandType.RANGE) {
            this.playersPanel.setSelectButtonText("Choose Range");
        } else if (selectedHandType == HandType.EXACTCARDS) {
            this.playersPanel.setSelectButtonText("Choose Cards");
        } else if (selectedHandType == HandType.RANDOM) {
            PokerType gameType = this.generalChoicesPanel.getSelectedVariation();
            
            int player = this.playersPanel.getPlayerIDSelectedIndex();
            
            if (gameType == PokerType.TEXAS_HOLDEM) {
                this.holdemProfiles[player] = new PlayerProfile(HandType.RANDOM, null, null);
            } else if (gameType == PokerType.OMAHA) {
                this.omahaProfiles[player] = new PlayerProfile(HandType.RANDOM, null, null);
            } else if (gameType == PokerType.OMAHA_HILO) {
                this.omahaHiLoProfiles[player] = new PlayerProfile(HandType.RANDOM, null, null);
            }

            this.setChoicesTableContent();
        }
    }
    
    private class ResultXMLListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(mainframe);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                    bw.write(SimulationExport.getResultXMLString(simulator.getResult()));
                } catch(Exception ex) {
                    GUIUtilities.showErrorDialog(mainframe, "Could not save the result", "Save Error");
                }
            }
        }
    }
    
    private void adjustAvailablePlayerIDs()
    {
        this.playersPanel.playerIDRemoveAllItems();
        
        for (int i = 1; i <= this.generalChoicesPanel.getPlayerCount(); i++) {
            this.playersPanel.playerIDAddItem(i);
        }
    }
    
    private ArrayList<Card> getUsedCards(PokerType selectedVariation)
    {
        ArrayList<Card> usedCards = new ArrayList<>();

        Card[] cards;
        if (selectedVariation == PokerType.TEXAS_HOLDEM) {
            for (int i = 0; i < MAX_PLAYERS; i++) {
                if (holdemProfiles[i].getHandType() != HandType.EXACTCARDS) {
                    continue;
                }
                
                cards = holdemProfiles[i].getCards();
                usedCards.add(cards[0]);
                usedCards.add(cards[1]);
            }
            
            for (int i = 0; i < 5; i++) {
                if (holdemCommunityCards[i] != null) {
                    usedCards.add(holdemCommunityCards[i]);
                }
            }
        } else if (selectedVariation == PokerType.OMAHA) {
            for (int i = 0; i < MAX_PLAYERS; i++) {
                if (omahaProfiles[i].getHandType() != HandType.EXACTCARDS) {
                    continue;
                }
                
                cards = omahaProfiles[i].getCards();
                usedCards.add(cards[0]);
                usedCards.add(cards[1]);
                usedCards.add(cards[2]);
                usedCards.add(cards[3]);
            }
            
            for (int i = 0; i < 5; i++) {
                if (omahaCommunityCards[i] != null) {
                    usedCards.add(omahaCommunityCards[i]);
                }
            }
        } else if (selectedVariation == PokerType.OMAHA_HILO) {
            for (int i = 0; i < MAX_PLAYERS; i++) {
                if (omahaHiLoProfiles[i].getHandType() != HandType.EXACTCARDS) {
                    continue;
                }
                
                cards = omahaHiLoProfiles[i].getCards();
                usedCards.add(cards[0]);
                usedCards.add(cards[1]);
                usedCards.add(cards[2]);
                usedCards.add(cards[3]);
            }
            
            for (int i = 0; i < 5; i++) {
                if (omahaHiLoCommunityCards[i] != null) {
                    usedCards.add(omahaHiLoCommunityCards[i]);
                }
            }
        }
        
        return usedCards;
    }
}
