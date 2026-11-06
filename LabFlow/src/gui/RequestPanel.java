package gui;

import concurrent.LabMonitor;
import javax.swing.*;
import java.awt.*;

/**
 * @author Visone Sara
 * JPanel to show all the requests made in the session.
 * It also has two buttons, one to open the UserManager Dialog
 * and one to open the RequestManager dialog.
 */
public class RequestPanel extends JPanel
{
    public RequestPanel(Frame frame, LabMonitor monitor)
    {
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();

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
}
