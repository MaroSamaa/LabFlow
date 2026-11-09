package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import concurrent.LabMonitor;
import concurrent.LabListener;

import java.awt.*;

public class EventLogPanel extends JPanel implements LabListener 
{
    private final static String[] columnNames = {"Time", "Event"};

    private LabMonitor monitor;
    private DefaultTableModel eventTableModel;

    public EventLogPanel(LabMonitor monitor) 
    {
        this.monitor = monitor;
        this.monitor.addLabListener(this); 
        setLayout(new BorderLayout());

        // Create the table model and don't allow direct modification 
        eventTableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false; 
            }
        };

        JTable eventTable = new JTable(eventTableModel);
        
        // (CHECK IF OK) Set the time column to a fixed width
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        eventTable.getColumnModel().getColumn(0).setMaxWidth(100);

        JScrollPane scrollTable = new JScrollPane(eventTable);
        
        // Panel title
        JLabel panelTitle = new JLabel(" Event Log", JLabel.LEFT);
        panelTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //  EventLogPanel assembly
        add(panelTitle, BorderLayout.NORTH);
        add(scrollTable, BorderLayout.CENTER);

        // Load initial event data
        updateEventLog();
    }

    private void updateEventLog() 
    {
        
    }

    @Override
    public void onLabStateChange()
    {
        //Do doUpdateEventLog whenever the EDT can
        SwingUtilities.invokeLater(()->
        {
            updateEventLog();
        });
    }
}