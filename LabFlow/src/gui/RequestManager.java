package gui;

import concurrent.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog that lets the user create new requests.
 * @author Visone Sara
 */
public class RequestManager extends JDialog 
{
    private JComboBox<User> userCombo;
    private JComboBox<String> equipmentCombo;

    /**
     * Constructor for the RequestManager
     * @param parent the parent frame
     * @param monitor the referenced LabMonitor
     */
    public RequestManager(Frame parent, LabMonitor monitor) 
    {

        super(parent, "Request Manager", true); //Modal = true to block the access to the frame below

        setSize(380, 220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Center Panel (drop down menus with labels)
        JPanel selectorPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        selectorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        userCombo = new JComboBox<>();
        equipmentCombo = new JComboBox<>();

        // Populate the drop down menus
        loadMonitorData(monitor);

        //  Assembly of the selector panel
        selectorPanel.add(new JLabel("Requesting User:"));
        selectorPanel.add(userCombo);
        selectorPanel.add(new JLabel("Wanted Equipment:"));
        selectorPanel.add(equipmentCombo);

        //  SouthPanel (add request btn)
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addRequestBtn = new JButton("Insert new request");

        //  addRequestBtn Actionlistener (@author Marini Enrico)
        addRequestBtn.addActionListener((ActionEvent) -> 
        {
            User selectedUser = (User) userCombo.getSelectedItem();
            String selectedEquipmentType = (String) equipmentCombo.getSelectedItem();

            // Check if selection is not null
            if (selectedUser == null || selectedEquipmentType == null) 
            {
                //  Warning popup if one of the selector was empty
                JOptionPane.showMessageDialog(this, "Select a user and an equipment", "Selection Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Request newRequest = new Request(selectedUser, selectedEquipmentType);

            RequestTask task = new RequestTask(newRequest, monitor);

            Thread requestThread = new Thread(task);
            requestThread.start();

            // Confirmation popup and dialog closure
            JOptionPane.showMessageDialog(this, selectedUser.toString()+ " has successfully made a request!", "Request Confirmation", JOptionPane.INFORMATION_MESSAGE);
                
            dispose(); // Close the confirmation popup and the request manager

        });

        southPanel.add(addRequestBtn);

        //  Assembly of the Request Manager
        add(selectorPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

       
    }

    /**
     * Service method to load the monitor's data about users and equipment types
     * @param monitor the LabMonitor to load the data from
     */
    private void loadMonitorData(LabMonitor monitor) 
    {
        // Load lab user list
        List<User> users = monitor.getUsers();
        for (User u : users) 
        {
            userCombo.addItem(u);
        }

        // Load lab equipment list
        List<Equipment> equipment = monitor.getEquipment();
        for (Equipment e : equipment) 
        {
            //  Insert an Equipment type once in the drop down menu
            boolean duplicate = false;
            for(int i = 0; i < equipmentCombo.getItemCount(); i++)
            {
                if(equipmentCombo.getItemAt(i).equals(e.getType()))
                {
                    duplicate = true;
                    break;
                }
            }
            if(!duplicate)
            {
                equipmentCombo.addItem(e.getType());
            }
        }
    }
}