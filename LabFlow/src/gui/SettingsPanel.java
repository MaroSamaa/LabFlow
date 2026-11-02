package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;

public class SettingsPanel extends JPanel
{
    private static final String TAB_NAME = "Settings and credits";
    private static final int LOGO_SIZE = 300;
    private static final int BTN_DIMENTION = 30;

    public SettingsPanel(Frame frame)
    {
        setLayout(new BorderLayout());
       
        //  Back button implementation 
        JButton backBtn = new JButton();

         //  Set Image for backBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/backIcon.png"));
            Image settingsIcon = temp.getScaledInstance(BTN_DIMENTION, BTN_DIMENTION, Image.SCALE_SMOOTH);
            backBtn.setIcon(new ImageIcon(settingsIcon));
            backBtn.setPreferredSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        backBtn.setMargin(new Insets(0, 0, 0, 0));
        backBtn.setBorder(null);
        backBtn.setContentAreaFilled(false);    // Remove java swing "standard" bkg
        backBtn.setFocusPainted(false);         // Remove java swing "hover" border
        backBtn.setBorderPainted(false);        // Remove "excess" borders

        //  On back btn push "go back" and visualize the main view panel
        backBtn.addActionListener((ActionEvent e) ->
        {
            frame.showMainView();
        });

        //  North panel assembly 
        //  Tab name 
        JLabel tabLabel = new JLabel(TAB_NAME);
        tabLabel.setHorizontalAlignment(JLabel.CENTER);
        tabLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(backBtn,BorderLayout.EAST);
        northPanel.add(tabLabel,BorderLayout.CENTER);

        JPanel creditPanel = new JPanel();
        creditPanel.setLayout(new BoxLayout(creditPanel, BoxLayout.Y_AXIS));

        JLabel authorLabel = new JLabel("\"<html><center>Authors:<br>Marini Enrico<br>Visone Sara</center></html>\"");    
        
        //  Logo label implementation
        JLabel logoLabel = new JLabel();

        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/LabFlowLogo.png"));
            Image logoIcon = temp.getScaledInstance(LOGO_SIZE, LOGO_SIZE, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(logoIcon));
            logoLabel.setPreferredSize(new Dimension(LOGO_SIZE, LOGO_SIZE));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        logoLabel.setBorder(null);
        
        //  Setting the horizontal center allignments
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        authorLabel.setHorizontalAlignment(JLabel.CENTER);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        // Assembly of the center panel with flexible spacers
        creditPanel.add(Box.createVerticalGlue()); // push to vertical center
        creditPanel.add(logoLabel);
        creditPanel.add(Box.createRigidArea(new Dimension(0, 15))); // 15px from logo to authors text
        creditPanel.add(authorLabel);
        creditPanel.add(Box.createVerticalGlue()); // Balance the lower vertical center space

        //  Assembly of the SettingsPanel
        add(northPanel, BorderLayout.NORTH);
        add(creditPanel, BorderLayout.CENTER);
        
    }
}


