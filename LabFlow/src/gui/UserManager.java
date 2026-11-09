package gui;

import concurrent.*;
import concurrent.exceptions.DuplicateUserCodeException;

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
   // private JTextField newUserName;

    /**
     * Constructor method for the UserManager Class
     * @param parent the parent frame of the JDialog
     * @param monitor the referenced LabMonitor
     */
    public UserManager(Frame parent, LabMonitor monitor)
    {
        super(parent, "User Manager",true); //Modal = true to block the access to the frame below 

        setSize(400, 350);
        setLocationRelativeTo(parent);  //Set it on top of the frame
        setLayout(new BorderLayout());

        //  Center panel (user list)
        listModel = new DefaultListModel<>();
        updateList(monitor);    //  Load initial data from monitor

        //  Make the list scrollable 
        JList<String> userList = new JList<>(listModel);
        JScrollPane scrollableUserList = new JScrollPane(userList);

        
        //  South Panel (textfield inputs)
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 15));

        //  Title of the south panel
        TitledBorder panelTitle = new TitledBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20),"New user creation");
        panelTitle.setTitleJustification(TitledBorder.CENTER);
        southPanel.setBorder(panelTitle);

        JTextField newUserName = new JTextField(20);
        JTextField newUserCode = new JTextField(20);
        
        inputPanel.add(new JLabel("New User Name:"));
        inputPanel.add(newUserName);
        inputPanel.add(new JLabel("New User Code:"));
        inputPanel.add(newUserCode);

        JButton addUserBtn = new JButton("ADD");

        //  AddUserBtn actionlistener
        addUserBtn.addActionListener((ActionEvent) -> 
        {
            //  Get the string in the textfields
            String userName = newUserName.getText().trim(); 
            String userCode = newUserCode.getText().trim();

            if (userName.isEmpty() || userCode.isEmpty()) //  If either of the two textfields was left empty
            {
                //Warning popup if either of the two textfields was left empty
                JOptionPane.showMessageDialog(this, "Input all of the necessary data", "Input Warning",JOptionPane.WARNING_MESSAGE);
                return;
            }

            try 
            {
                //  Create the new user and add it to the monitor's list
                User newUser = new User(userName,userCode);
                monitor.addUser(newUser);

                //  Update the shown list
                updateList(monitor);

                //  Reset the text field to blank
                newUserName.setText("");
                newUserCode.setText("");
                
            } catch (DuplicateUserCodeException e) 
            {
                // Error popup if the user couldn't be added (user's code is the code of another user in the list)
                JOptionPane.showMessageDialog(this, e.getMessage(), "Duplicate user code error", JOptionPane.ERROR_MESSAGE);
            }
        });

        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(addUserBtn, BorderLayout.SOUTH);


        //  Assembly of the UserManager
        add(southPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
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
            listModel.addElement(u.toString());
        }
    }
}