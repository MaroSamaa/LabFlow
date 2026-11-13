package gui;

import concurrent.*;
import concurrent.exceptions.DuplicateUserCodeException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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

    //--------CONSTANTS--------
    private static final String[] columnNames = {"User Name", "User Code"};

    //--------INSTANCE VARIABLES--------

    private DefaultTableModel userTableModel;
    private JTable userTable; 

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

        // Create the table model and don't allow direct modification 
        userTableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false; 
            }
        };
        userTable = new JTable(userTableModel);

        //  Set the table row size and font
        userTable.setRowHeight(30);
        userTable.getTableHeader().setFont(userTable.getFont().deriveFont(Font.PLAIN, 13f)); //   Set font for the table header
        userTable.setFont(userTable.getFont().deriveFont(Font.PLAIN, 13f));

        //  Table cell renderer to center the text in the table's cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for(int i = 0; i < userTable.getColumnCount(); i++)    //  Apply the renderer to all columns
        {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollableUserTable = new JScrollPane(userTable);   //  Make the table scrollable

        updateList(monitor);    //  Load initial data from monitor

        JLabel userListTitle = new JLabel("Registered Users", JLabel.CENTER);
        userListTitle.setFont(userListTitle.getFont().deriveFont(Font.BOLD));

        centerPanel.add(scrollableUserTable,BorderLayout.CENTER);
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

        newUserName.setHorizontalAlignment(JTextField.CENTER);
        newUserCode.setHorizontalAlignment(JTextField.CENTER);
        
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
     * Service method to update the user table
     * @param monitor the referenced LabMonitor
     */
    private void updateList(LabMonitor monitor) 
    {
        userTableModel.setRowCount(0);  // Reset to blank table
        List<User> users = monitor.getUsers();
    
        for (User u : users) 
        {
            String[] rowData = {u.getName(), u.getCode()};
            userTableModel.addRow(rowData);
        }

        // Auto-scroll down to see the latest row added
        if (userTable.getRowCount() > 0) 
        {
            int lastRow = userTable.getRowCount() - 1;
            userTable.scrollRectToVisible(userTable.getCellRect(lastRow, 0, true));
        }
    }
}