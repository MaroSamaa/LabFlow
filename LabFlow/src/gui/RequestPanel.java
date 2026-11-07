package gui;

import concurrent.LabMonitor;
import concurrent.Request;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.*;

/**
 * @author Visone Sara
 * JPanel to show all the requests made in the session.
 * It also has two buttons, one to open the UserManager Dialog
 * and one to open the RequestManager dialog.
 */
public class RequestPanel extends JPanel
{
    private final static String[] columnNames = {"Requesting User", "Requested Equipment", "Request status"};

    private DefaultTableModel requestTableModel;

    public RequestPanel(Frame frame, LabMonitor monitor)
    {
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
        JScrollPane scrollTable = new JScrollPane(requestTable);
        centerPanel.add(scrollTable);
        updateRequestTable(monitor);

        

        //  Buttons and actionlisteners
        JButton userBtn = new JButton("User");
        JButton addRequestBtn = new JButton("Add request");

        //  Open and visualize UserManager 
        userBtn.addActionListener((ActionEvent) ->
        {
            UserManager userManager = new UserManager(frame,monitor);
            userManager.setVisible(true);
        });

        //  Open and visualize RequestManager
        addRequestBtn.addActionListener((ActionEvent) ->
        {
            RequestManager reqManager = new RequestManager(frame,monitor);
            reqManager.setVisible(true);
        });

        //  Assembly of the northPanel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(userBtn, BorderLayout.WEST);
        northPanel.add(addRequestBtn, BorderLayout.EAST);

        //  Assembly of the request panel
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Service method to load the table with LabMonitor request data
     * @param monitor the referenced LabMonitor
     */
    private void updateRequestTable(LabMonitor monitor)
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
                r.getUser().getName(),                  // Requesting user name
                r.getRequestedEquipment().getType(),    // Requested equipment type
                r.getStatus().toString()                // Request status
            };
            requestTableModel.addRow(rowData);
        }
    }
}
