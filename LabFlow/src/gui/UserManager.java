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
    //--------MESSAGES--------

    private static final String MSG_INPUT_WARNING = "Input all of the necessary data";

    //--------INSTANCE VARIABLES--------

    /**
     * Written list of all users in the lab
     */
    private DefaultListModel<String> listModel;

    //--------CONSTRUCTOR--------

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

        //--------CENTER PANEL(User list)--------

        JPanel centerPanel = new JPanel(new BorderLayout());

        listModel = new DefaultListModel<>();
        updateList(monitor);    //  Load initial data from monitor

        //  Make the list scrollable 
        JList<String> userList = new JList<>(listModel);
        JScrollPane scrollableUserList = new JScrollPane(userList);

        JLabel userListTitle = new JLabel("Registered Users", JLabel.CENTER);
        userListTitle.setFont(userListTitle.getFont().deriveFont(Font.BOLD));

        centerPanel.add(scrollableUserList,BorderLayout.CENTER);
        centerPanel.add(userListTitle,BorderLayout.NORTH);

        
        //--------SOUTH PANEL(Textfield inputs)--------

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 15));

        //  Title of the south panel
        JLabel panelTitle = new JLabel("New User Creation", JLabel.CENTER);
        panelTitle.setFont(panelTitle.getFont().deriveFont(Font.BOLD));

        southPanel.add(panelTitle,BorderLayout.NORTH);

        JTextField newUserName = new JTextField(20);
        JTextField newUserCode = new JTextField(20);
        
        inputPanel.add(new JLabel("New User Name:"));
        inputPanel.add(newUserName);
        inputPanel.add(new JLabel("New User Code:"));
        inputPanel.add(newUserCode);

        //----Add User Button----

        JButton addUserBtn = new JButton("ADD");

        addUserBtn.addActionListener((ActionEvent) -> 
        {
            //  Get the string in the textfields
            String userName = newUserName.getText().trim(); 
            String userCode = newUserCode.getText().trim();

            if (userName.isEmpty() || userCode.isEmpty()) //  If either of the two textfields was left empty
            {
                JOptionPane.showMessageDialog(this, MSG_INPUT_WARNING, "Input Warning",JOptionPane.WARNING_MESSAGE);
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
                
            } catch (DuplicateUserCodeException e){JOptionPane.showMessageDialog(this, e.getMessage(), "Duplicate user code error", JOptionPane.ERROR_MESSAGE);}
        });

        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(addUserBtn, BorderLayout.SOUTH);


        //  Assembly of the UserManager
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Service method to update the written user list
     * @param monitor the referenced LabMonitor
     */
    private void updateList(LabMonitor monitor) 
    {
        listModel.clear();  //Reset the list
        List<User> users = monitor.getUsers();
        for (User u : users) 
        {
            listModel.addElement(u.toString());
        }
    }
}