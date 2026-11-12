package gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import concurrent.LabListener;
import concurrent.LabMonitor;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ControlPanel extends JPanel implements LabListener
{
    private static final int BTN_DIMENTION = 30;

    private LabMonitor monitor;
    private JLabel pauseIndicator;

    public ControlPanel(Frame frame, LabMonitor monitor)
    {
        this.monitor = monitor;
        setLayout(new BorderLayout());

        JButton saveBtn = new JButton();
        JButton loadBtn = new JButton();
        JButton settingsBtn = new JButton();
        JButton playBtn = new JButton();
        JButton pauseBtn = new JButton();
        pauseIndicator = new JLabel("\u2022");
        pauseIndicator.setFont(new Font(pauseIndicator.getFont().getName(), Font.PLAIN,50));
        pauseIndicator.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseIndicator.setPreferredSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));
        pauseIndicator.setMinimumSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));
        pauseIndicator.setMaximumSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));


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

        //  Set Image for pauseBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/pauseIcon.png"));
            Image pauseIcon = temp.getScaledInstance(BTN_DIMENTION, BTN_DIMENTION, Image.SCALE_SMOOTH);
            pauseBtn.setIcon(new ImageIcon(pauseIcon));
            pauseBtn.setPreferredSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        pauseBtn.setMargin(new Insets(0, 0, 0, 0));
        pauseBtn.setBorder(null);
        pauseBtn.setContentAreaFilled(false);    
        pauseBtn.setFocusPainted(false);         
        pauseBtn.setBorderPainted(false);        

        //--------ADD ACTION LISTENERS TO BUTTONS--------

        //  On save btn press prompt the user to select a file and write the monitor's information (@author Marini Enrico)
        saveBtn.addActionListener((ActionEvent) ->
        {
            if(this.monitor.hasActiveRequests()) //If the monitor has active requests warn the user that they won't be saved
            {
                JOptionPane.showMessageDialog(frame,"There are ACTIVE requests in the lab, these won't be saved to the save file.","Active requests won't be saved",JOptionPane.WARNING_MESSAGE);
            }
            //  Show the user a File Chooser with a custom title
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Lab State");

            //  Center the file chooser to the frame
            int userSelection = fileChooser.showSaveDialog(frame);
        
            if(userSelection == JFileChooser.APPROVE_OPTION) 
            {
                File file = fileChooser.getSelectedFile();
                
                try 
                {
                    // Chiamata al metodo del monitor che usa il try-with-resources
                    this.monitor.saveLabState(file);
                    
                    JOptionPane.showMessageDialog(frame,"The state of the lab has been saved","Save File Confirmation",JOptionPane.INFORMATION_MESSAGE);
                        
                } 
                catch (IOException e) {JOptionPane.showMessageDialog(frame,"Error during save file writing:\n" + e.getMessage(),"Save File Error",JOptionPane.ERROR_MESSAGE);}
            }
        });

        //  On load btn press prompt the user to select a file and load the data into the monitor (@author Marini Enrico)
        loadBtn.addActionListener((ActionEvent) ->
        {

            //  If there is information that get's potentially overritten make the user choose
            if(!this.monitor.getEvents().isEmpty())
            {
                int userChoice = JOptionPane.showConfirmDialog(frame, "All unsaved data will be lost, are you sure you want to proceed?", "Overwrite Confirmation",JOptionPane.YES_NO_OPTION);
                if(userChoice == JOptionPane.NO_OPTION)
                {
                    return;
                }
            }

            //  If no data is present or the user chose to continue and overwrite

            //  Show the user a File Chooser with a custom title
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Lab State");
    
            //  Center the file chooser to the frame
            int userSelection = fileChooser.showOpenDialog(frame);
    
            if (userSelection == JFileChooser.APPROVE_OPTION) 
            {
                File file = fileChooser.getSelectedFile();
                
                try 
                {
                    // Chiamata al metodo del monitor
                    this.monitor.loadLabState(file);
                    
                    JOptionPane.showMessageDialog(frame,"The state of the lab has been successfully loaded","Load File Confirmation",JOptionPane.INFORMATION_MESSAGE);
                        
                } 
                catch (IOException e){JOptionPane.showMessageDialog(frame,"Error during file reading:\n" + e.getMessage(),"Loading Error",JOptionPane.ERROR_MESSAGE);}
                catch(ClassNotFoundException e){JOptionPane.showMessageDialog(frame,"Make sure to select a previously saved file\n" + e.getMessage(),"File Selection Error",JOptionPane.ERROR_MESSAGE);}
            }
        });

        //  On settings btn push visualize the settings panel
        settingsBtn.addActionListener((ActionEvent) ->
        {
            frame.showSettings();
        });

        playBtn.addActionListener((ActionEvent) ->
        {
            this.monitor.play();
        });

        pauseBtn.addActionListener((ActionEvent) ->
        {
            this.monitor.pause();
        });


        //--------ASSEMBLY OF THE CONTROL PANEL--------

        add(settingsBtn, BorderLayout.WEST);    //put the settingsBtn to the left

        //  Put the Save and Load Buttons together to the right
        JPanel saveAndLoadPanel = new JPanel();
        saveAndLoadPanel.add(saveBtn);
        saveAndLoadPanel.add(loadBtn);
        add(saveAndLoadPanel, BorderLayout.EAST);

        //  Put the Play and Stop Buttons together to the center with the pause indicator in the middle of them
        JPanel playAndPausePanel = new JPanel();
        playAndPausePanel.add(playBtn);
        playAndPausePanel.add(pauseIndicator);
        playAndPausePanel.add(pauseBtn);
        add(playAndPausePanel, BorderLayout.CENTER);

        this.monitor.addLabListener(this);
    }

    public void updatePauseIndicator()
    {
        if(monitor.isPaused())
        {
            pauseIndicator.setForeground(Color.RED);
        }
        else
        {
            pauseIndicator.setForeground(Color.GREEN);
        }
    }

    /**
     * Method to update the pauseIndicator when the listener gets notified
     */
    @Override 
    public void onLabStateChange()
    {
        //Do updatePauseIndicator whenever the EDT can
        SwingUtilities.invokeLater(()->
        {
            updatePauseIndicator();
        });
    }

}
