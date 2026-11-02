package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ControlPanel extends JPanel
{
    private static final int BTN_DIMENTION = 30;

    public ControlPanel()
    {
        setLayout(new BorderLayout());

        JButton saveBtn = new JButton();
        JButton loadBtn = new JButton();
        JButton settingsBtn = new JButton();

        //  SETTING BUTTON IMAGE ICONS
        //  Set Image for settingsBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/settingsIcon.png"));
            Image settingsIcon = temp.getScaledInstance(BTN_DIMENTION, BTN_DIMENTION, Image.SCALE_SMOOTH);
            settingsBtn.setIcon(new ImageIcon(settingsIcon));
            settingsBtn.setPreferredSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));
        }
        catch (Exception e) {System.out.println(e.getStackTrace());}
        settingsBtn.setMargin(new Insets(0, 0, 0, 0));
        settingsBtn.setBorder(null);
        settingsBtn.setContentAreaFilled(false);    // Remove java swing "standard" bkg
        settingsBtn.setFocusPainted(false);         // Remove java swing "hover" border
        settingsBtn.setBorderPainted(false);        // Remove "excess" borders

        //  Set Image for saveBtn
        try 
        {
            Image saveIcon = ImageIO.read(getClass().getResource("/images/saveIcon.png"));
            saveBtn.setIcon(new ImageIcon(saveIcon));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        saveBtn.setMargin(new Insets(0, 0, 0, 0));
        saveBtn.setBorder(null);
        saveBtn.setContentAreaFilled(false);    
        saveBtn.setFocusPainted(false);         
        saveBtn.setBorderPainted(false);        

        //  Set Image for loadBtn
        try 
        {
            Image loadIcon = ImageIO.read(getClass().getResource("/images/loadIcon.png"));
            loadBtn.setIcon(new ImageIcon(loadIcon));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        loadBtn.setMargin(new Insets(0, 0, 0, 0));
        loadBtn.setBorder(null);
        loadBtn.setContentAreaFilled(false);    
        loadBtn.setFocusPainted(false);         
        loadBtn.setBorderPainted(false);        



    }
}
