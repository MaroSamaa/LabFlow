package gui;

import concurrent.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.*;
import java.io.IOException;

/**
 * @author Visone Sara
 * JPanel to show all the requests made in the session.
 * It implements LabListener to auto update the request table whenever
 * a change in the LabMonitor happens.
 * It also has two buttons, one to open the UserManager Dialog
 * and one to open the RequestManager dialog.
 */
public class RequestPanel extends JPanel implements LabListener
{
    private final static String[] columnNames = {"Requesting User", "Requested Equipment", "Request status"};
    private static final int BTN_SIZE = 30;
    private static final Dimension BTN_DIMENSION = new Dimension(BTN_SIZE,BTN_SIZE);

    private DefaultTableModel requestTableModel;
    private JLabel activeRequestCounter;
    private JLabel userCounter;

    private LabMonitor monitor;

    public RequestPanel(Frame frame, LabMonitor monitor)
    {
        this.monitor = monitor;
        setLayout(new BorderLayout());

        //  Assembly of the center panel
        JPanel centerPanel = new JPanel();

        // Create the table model and don't allow direct modification 
        requestTableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false; 
            }
        };
        JTable requestTable = new JTable(requestTableModel);
        JScrollPane scrollTable = new JScrollPane(requestTable); // Make the table scrollable
        centerPanel.add(scrollTable);

        //  Buttons and actionlisteners
        JButton userBtn = new JButton();
        JButton addRequestBtn = new JButton();

        //  Set Image for userBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/userIcon.png"));
            Image userIcon = temp.getScaledInstance(BTN_SIZE, BTN_SIZE, Image.SCALE_SMOOTH);
            userBtn.setIcon(new ImageIcon(userIcon));
            userBtn.setMinimumSize(BTN_DIMENSION);
            userBtn.setMaximumSize(BTN_DIMENSION);
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        userBtn.setMargin(new Insets(0, 0, 0, 0));
        userBtn.setBorder(null);
        userBtn.setContentAreaFilled(false);    // Remove java swing "standard" bkg
        userBtn.setFocusPainted(false);         // Remove java swing "hover" border
        userBtn.setBorderPainted(false);        // Remove "excess" borders

        //  Set Image for requestBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/requestIcon.png"));
            Image requestIcon = temp.getScaledInstance(BTN_SIZE, BTN_SIZE, Image.SCALE_SMOOTH);
            addRequestBtn.setIcon(new ImageIcon(requestIcon));
            addRequestBtn.setMinimumSize(BTN_DIMENSION);
            addRequestBtn.setMaximumSize(BTN_DIMENSION);
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        addRequestBtn.setMargin(new Insets(0, 0, 0, 0));
        addRequestBtn.setBorder(null);
        addRequestBtn.setContentAreaFilled(false);    
        addRequestBtn.setFocusPainted(false);         
        addRequestBtn.setBorderPainted(false);        

        //  Open and visualize UserManager 
        userBtn.addActionListener((ActionEvent) ->
        {
            UserManager userManager = new UserManager(frame,this.monitor);
            userManager.setVisible(true);
        });

        //  Open and visualize RequestManager
        addRequestBtn.addActionListener((ActionEvent) ->
        {
            RequestManager reqManager = new RequestManager(frame,this.monitor);
            reqManager.setVisible(true);
        });

        JLabel panelTitle = new JLabel("REQUESTS", JLabel.CENTER);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        userCounter = new JLabel();
        userPanel.add(userBtn);
        userPanel.add(userCounter);
        
        JPanel addRequestPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,3,0));
        activeRequestCounter = new JLabel();
        addRequestPanel.add(activeRequestCounter);
        addRequestPanel.add(addRequestBtn);

        //  Assembly of the northPanel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(userPanel, BorderLayout.WEST);
        northPanel.add(panelTitle, BorderLayout.CENTER);
        northPanel.add(addRequestPanel, BorderLayout.EAST);

        //  Assembly of the request panel
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        this.monitor.addLabListener(this);
    }

    /**
     * Service method to load the table with LabMonitor request data
     */
    private void updateRequestTable()
    {
        // Reset to blank table
        requestTableModel.setRowCount(0);

        // Get the request list from the LabMonitor
        List<Request> requests = monitor.getRequests();

        // Add every request (one per row)
        for (Request r : requests) 
        {
            String[] rowData = 
            {
                r.getUser().toString(),      // Requesting user name
                r.getEquipmentType(),       // Requested equipment type
                r.getStatus().getLabel()    // Request status
            };
            requestTableModel.addRow(rowData);
        }
    }

    /**
     * Service method to update the activity counters (user counter and active request counter)
     */
    private void updateActivityCounters()
    {
        userCounter.setText(": "+monitor.getUserCounter());
        activeRequestCounter.setText(monitor.getActiveRequestCounter() + " :");
    }

    /**
     * Method to update the request table when the listener gets notified
     */
    @Override 
    public void onLabStateChange()
    {
        //Do updateRequestTable whenever the EDT can
        SwingUtilities.invokeLater(()->
        {
            updateRequestTable();
            updateActivityCounters();
        });
    }
}
