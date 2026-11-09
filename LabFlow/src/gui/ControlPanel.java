package gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import concurrent.LabMonitor;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ControlPanel extends JPanel
{
    private static final int BTN_DIMENTION = 30;

    public ControlPanel(Frame frame, LabMonitor monitor)
    {
        setLayout(new BorderLayout());

        JButton saveBtn = new JButton();
        JButton loadBtn = new JButton();
        JButton settingsBtn = new JButton();
        JButton playBtn = new JButton();
        JButton stopBtn = new JButton();

        //--------SETTING BUTTONS IMAGE ICONS--------
        //  Set Image for settingsBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/settingsIcon.png"));
            Image settingsIcon = temp.getScaledInstance(BTN_DIMENTION, BTN_DIMENTION, Image.SCALE_SMOOTH);
            settingsBtn.setIcon(new ImageIcon(settingsIcon));
            settingsBtn.setPreferredSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
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
        
        //  Set Image for startBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/playIcon.png"));
            Image playIcon = temp.getScaledInstance(BTN_DIMENTION, BTN_DIMENTION, Image.SCALE_SMOOTH);
            playBtn.setIcon(new ImageIcon(playIcon));
            playBtn.setPreferredSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        playBtn.setMargin(new Insets(0, 0, 0, 0));
        playBtn.setBorder(null);
        playBtn.setContentAreaFilled(false);    
        playBtn.setFocusPainted(false);         
        playBtn.setBorderPainted(false);        

        //  Set Image for stopBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/stopIcon.png"));
            Image stopIcon = temp.getScaledInstance(BTN_DIMENTION, BTN_DIMENTION, Image.SCALE_SMOOTH);
            stopBtn.setIcon(new ImageIcon(stopIcon));
            stopBtn.setPreferredSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        stopBtn.setMargin(new Insets(0, 0, 0, 0));
        stopBtn.setBorder(null);
        stopBtn.setContentAreaFilled(false);    
        stopBtn.setFocusPainted(false);         
        stopBtn.setBorderPainted(false);        

        //--------ADD ACTION LISTENERS TO BUTTONS--------

        //  On save btn press prompt the user to select a file and write the monitor's information (@author Marini Enrico)
        saveBtn.addActionListener((ActionEvent) ->
        {
            //  Show the user a File Chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Lab State");
        
            int userSelection = fileChooser.showSaveDialog(this);
        
            if(userSelection == JFileChooser.APPROVE_OPTION) 
            {
                File file = fileChooser.getSelectedFile();
                
                try 
                {
                    // Chiamata al metodo del monitor che usa il try-with-resources
                    monitor.saveLabState(file);
                    
                    JOptionPane.showMessageDialog(this,"The state of the lab has been saved","Save File Confirmation",JOptionPane.INFORMATION_MESSAGE);
                        
                } 
                catch (IOException e) {JOptionPane.showMessageDialog(this,"Error during save file writing:\n" + e.getMessage(),"Save File Error",JOptionPane.ERROR_MESSAGE);}
            }
        });

        //  On load btn press prompt the user to select a file and load the data into the monitor (@author Marini Enrico)
        loadBtn.addActionListener((ActionEvent) ->
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Lab State");
    
            int userSelection = fileChooser.showOpenDialog(this);
    
            if (userSelection == JFileChooser.APPROVE_OPTION) 
            {
                File file = fileChooser.getSelectedFile();
                
                try 
                {
                    // Chiamata al metodo del monitor
                    monitor.loadLabState(file);
                    
                    JOptionPane.showMessageDialog(this,"The state of the lab has been successfully loaded","Load File Confirmation",JOptionPane.INFORMATION_MESSAGE);
                        
                } 
                catch (IOException e){JOptionPane.showMessageDialog(this,"Error during file reading:\n" + e.getMessage(),"Loading Error",JOptionPane.ERROR_MESSAGE);}
                catch(ClassNotFoundException e){JOptionPane.showMessageDialog(this,"Make shure to select a previously saved file\n" + e.getMessage(),"File Selection Error",JOptionPane.ERROR_MESSAGE);}
            }
        });

        //  On settings btn push visualize the settings panel
        settingsBtn.addActionListener((ActionEvent) ->
        {
            frame.showSettings();
        });

        playBtn.addActionListener((ActionEvent) ->
        {
            //Temporary placeholder instead of the actual playing action
            System.out.println("play pressed");
        });

        stopBtn.addActionListener((ActionEvent) ->
        {
            //Temporary placeholder instead of the actual stop action
            System.out.println("stop pressed");
        });


        //--------ASSEMBLY OF THE CONTROL PANEL--------

        add(settingsBtn, BorderLayout.WEST);    //put the settingsBtn to the left

        //  Put the Save and Load Buttons together to the right
        JPanel saveAndLoadPanel = new JPanel();
        saveAndLoadPanel.add(saveBtn);
        saveAndLoadPanel.add(loadBtn);
        add(saveAndLoadPanel, BorderLayout.EAST);

        //  Put the Play and Stop Buttons together to the center
        JPanel playAndStopPanel = new JPanel();
        playAndStopPanel.add(playBtn);
        playAndStopPanel.add(stopBtn);
        add(playAndStopPanel, BorderLayout.CENTER);

    }
}
