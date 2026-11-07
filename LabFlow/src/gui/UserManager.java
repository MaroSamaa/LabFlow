package gui;

import concurrent.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * @author Visone Sara
 * JDialog to show the user all of the lab's users 
 * it also gives the possibility to add a new user by inserting the user's name 
 * in the textfield and pressing a confirm button 
 */
public class UserManager extends JDialog 
{
    /**
     * Written list of all users in the lab
     */
    private DefaultListModel<String> listModel;

    /**
     * Textfield where the name of the new user gets written
     */
    private JTextField newUserName;

    /**
     * Constructor method for the UserManager Class
     * @param parent the parent frame of the JDialog
     * @param monitor the referenced LabMonitor
     */
    public UserManager(Frame parent, LabMonitor monitor)
    {
        super(parent, "User Manager",true); //Modal = true to block the access to the frame below 

        setSize(400, 300);
        setLocationRelativeTo(parent);  //Set it on top of the frame
        setLayout(new BorderLayout());

        //  Center panel (user list)
        listModel = new DefaultListModel<>();
        updateList(monitor);    //  Load initial data from monitor

        //  Make the list scrollable 
        JList<String> userList = new JList<>(listModel);
        JScrollPane scrollableUserList = new JScrollPane(userList);

        //  South panel (new user input)
        JPanel southPanel = new JPanel(new BorderLayout());

        //  Title of the south panel
        TitledBorder panelTitle = new TitledBorder("Add new user");
        panelTitle.setTitleJustification(TitledBorder.CENTER);
        southPanel.setBorder(panelTitle);

        newUserName = new JTextField(20);   //See if the intial size is needed given it's setting in center of borderLayout
        JButton addUserBtn = new JButton("ADD");

        //  AddUserBtn actionlistener
        addUserBtn.addActionListener((ActionEvent) -> 
        {
            String name = newUserName.getText().trim(); //  Get the string in the textfield
            try 
            {
                //  Create the new user and add it to the monitor's list
                User newUser = new User(name);
                monitor.addUser(newUser);

                //  Update the shown list
                updateList(monitor);

                //  Reset the text field to blank
                newUserName.setText("");
                
            } catch (IllegalStateException e) 
            {
                // Error popup if the user couldn't be added (user's name is the name of another user in the list)
                JOptionPane.showMessageDialog(this, e.getMessage(), "Duplicate user error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //  Assembly of the south panel
        southPanel.add(new JLabel("User name: "), BorderLayout.WEST);
        southPanel.add(newUserName, BorderLayout.CENTER);
        southPanel.add(addUserBtn, BorderLayout.EAST);

        //  Assembly of the UserManager
        add(scrollableUserList, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Service method to update the written user list
     * @param monitor the referenced LabMonitor
     */
    private void updateList(LabMonitor monitor) 
    {
        listModel.clear();  //Reset the list
        listModel.addElement("LAB USERS:");
        List<User> users = monitor.getUsers();
        for (User u : users) 
        {
            listModel.addElement(u.getName());
        }
    }
}