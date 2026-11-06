package gui;

import concurrent.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class UserManager extends JDialog 
{
    private DefaultListModel<String> listModel;
    private JTextField newUserName;

    public UserManager(Frame parent, LabMonitor monitor)
    {
        super(parent, "User Manager",true); //Modal = true to block the access to the frame below 

        setSize(400, 300);
        setLocationRelativeTo(parent);  //Set it on top of the frame
        setLayout(new BorderLayout());

        //  Center panel (user list)
        listModel = new DefaultListModel<>();
        updateList(monitor);    //  Load initial data from monitor

        JList<String> userList = new JList<>(listModel);
        JScrollPane scrollableUserList = new JScrollPane(userList);

        //  South panel (new user input)
        JPanel southPanel = new JPanel(new BorderLayout());

        //  Title of the south panel
        TitledBorder panelTitle = new TitledBorder("Add new user");
        panelTitle.setTitleJustification(TitledBorder.CENTER);
        southPanel.setBorder(panelTitle);

        newUserName = new JTextField(20);
        JButton addUserBtn = new JButton("ADD");

        //  AddUserBtn actionlistener
        addUserBtn.addActionListener((ActionEvent) -> 
        {
            String name = newUserName.getText().trim(); //  Get the string in the textfield
            try 
            {
                User newUser = new User(name);
                monitor.addUser(newUser);

                //  Update the shown list
                updateList(monitor);

                //  Reset the text field to blank
                newUserName.setText("");
                
            } catch (IllegalStateException e) 
            {
                // Error popup if the user couldn't be added
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

    // Service method to load users from the monitor's list
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