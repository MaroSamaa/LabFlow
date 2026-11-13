package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import concurrent.LabMonitor;
import concurrent.LabListener;

import java.awt.*;
import java.util.List;

/**
 * Panel that shows the user a table containing event log messages.
 * It's a table with 2 columns, one for the timestamp and one for the event's
 * description given by the LabMonitor.
 * @author Visone Sara
 */
public class EventLogPanel extends JPanel implements LabListener 
{
    /**
     * Column names for the event log table
     */
    private final static String[] columnNames = {"Time", "Event"};

    /**
     * The referenced LabMonitor
     */
    private LabMonitor monitor;
    private DefaultTableModel eventTableModel;
    private JTable eventTable; 

    /**
     * Constructor for the EventLogPanel
     * @param monitor the referenced LabMonitor
     */
    public EventLogPanel(LabMonitor monitor) 
    {
        this.monitor = monitor;
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

        eventTable = new JTable(eventTableModel);

        //  Set the table row size and font
        eventTable.setRowHeight(30);
        eventTable.getTableHeader().setFont(eventTable.getFont().deriveFont(Font.PLAIN, 13f)); //   Set font for the table header
        eventTable.setFont(eventTable.getFont().deriveFont(Font.PLAIN, 13f));

        //  Table cell renderer to center the text in the time column 
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        eventTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    
        // (CHECK IF OK) Set the time column to a fixed width
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        eventTable.getColumnModel().getColumn(0).setMaxWidth(100);

        JScrollPane scrollTable = new JScrollPane(eventTable);
        
        // Panel title
        JLabel panelTitle = new JLabel("EVENT LOG", JLabel.LEFT);
        panelTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //  EventLogPanel assembly
        add(panelTitle, BorderLayout.NORTH);
        add(scrollTable, BorderLayout.CENTER);

        this.monitor.addLabListener(this); 
    }

    /**
     * Service method to update the eventTable
     */
    private void updateEventLog() 
    {
        eventTableModel.setRowCount(0); // Reset to blank table
        
        List<String> events = monitor.getEvents();
        
        for (String e : events) 
        {
            //  Divide the message in two parts and use the space to separate them
            String[] splitMessage = e.split(" ",2); 
            eventTableModel.addRow(splitMessage);
        }

        // Auto-scroll down to see the latest row added
        if (eventTable.getRowCount() > 0) 
        {
            int lastRow = eventTable.getRowCount() - 1;
            eventTable.scrollRectToVisible(eventTable.getCellRect(lastRow, 0, true));
        }
    }

    /**
     * Method to update the event log table when the listener gets notified
     */
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