package gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import concurrent.LabListener;
import concurrent.LabMonitor;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 *  Panel with buttons to control the program
 *  @author Visone Sara
 */
public class ControlPanel extends JPanel implements LabListener
{
    //---------MESSAGES--------

    private static final String MSG_ERROR_IN_SELECTED_FILE = "Make sure to select a previously saved file\n";
    private static final String MSG_ERROR_DURING_LOAD = "Error during file reading:\n";
    private static final String MSG_LAB_STATE_LOADED = "The state of the lab has been successfully loaded";
    private static final String MSG_UNSAVED_DATA_WARNING = "All unsaved data will be lost, are you sure you want to proceed?";
    private static final String MSG_ERROR_DURING_SAVE = "Error during save file writing:\n";
    private static final String MSG_LAB_STATE_SAVED = "The state of the lab has been saved";
    private static final String MSG_ACTIVE_REQUESTS_WARNING = "There are ACTIVE requests in the lab, these won't be saved to the save file.";

    //--------CONSTANTS--------

    /**
     * Button size constant
     */
    private static final int BTN_SIZE = 30;

    /**
     * Button dimension constant
     */
    private static final Dimension BTN_DIMENSION = new Dimension(BTN_SIZE,BTN_SIZE);

    /**
     * Indicator dimension constant
     */
    private static final Dimension INDICATOR_DIMENSION = new Dimension(20,20);

    //--------INSTANCE VARIABLES--------

    /**
     * The referenced LabMonitor
     */
    private LabMonitor monitor;

    private JLabel pauseIndicator;

    //--------CONSTRUCTOR--------

    /**
     * Constructor for the ControlPanel
     * @param frame the parent frame
     * @param monitor the referenced LabMonitor
     */
    public ControlPanel(Frame frame, LabMonitor monitor)
    {
        this.monitor = monitor;
        setLayout(new BorderLayout());

        //--------BUTTONS--------

        //  Initialization 
        JButton saveBtn = new JButton();
        JButton loadBtn = new JButton();
        JButton settingsBtn = new JButton();
        JButton playBtn = new JButton();
        JButton pauseBtn = new JButton();

        //----Setting icons----

        //  Set Image for settingsBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/settingsIcon.png"));
            Image settingsIcon = temp.getScaledInstance(BTN_SIZE, BTN_SIZE, Image.SCALE_SMOOTH);
            settingsBtn.setIcon(new ImageIcon(settingsIcon));
            settingsBtn.setPreferredSize(BTN_DIMENSION);
        }
        catch(IOException e) {System.out.println(e.getStackTrace());}
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
        catch(IOException e) {System.out.println(e.getStackTrace());}
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
            loadBtn.setPreferredSize(BTN_DIMENSION);
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
            Image playIcon = temp.getScaledInstance(BTN_SIZE, BTN_SIZE, Image.SCALE_SMOOTH);
            playBtn.setIcon(new ImageIcon(playIcon));
            playBtn.setPreferredSize(BTN_DIMENSION);
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
            Image pauseIcon = temp.getScaledInstance(BTN_SIZE, BTN_SIZE, Image.SCALE_SMOOTH);
            pauseBtn.setIcon(new ImageIcon(pauseIcon));
            pauseBtn.setPreferredSize(BTN_DIMENSION);
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        pauseBtn.setMargin(new Insets(0, 0, 0, 0));
        pauseBtn.setBorder(null);
        pauseBtn.setContentAreaFilled(false);    
        pauseBtn.setFocusPainted(false);         
        pauseBtn.setBorderPainted(false);        

        //----Adding action listeners----

        //  On save btn press prompt the user to select a file and write the monitor's information (@author Marini Enrico)
        saveBtn.addActionListener((ActionEvent) ->
        {
            //  If the monitor has active requests warn the user that they won't be saved
            if(this.monitor.hasActiveRequests()) 
            {
                JOptionPane.showMessageDialog(frame, MSG_ACTIVE_REQUESTS_WARNING,"Active requests won't be saved",JOptionPane.WARNING_MESSAGE);
            }

            //  Show the user a File Chooser with a custom title
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Lab State");
            int userSelection = fileChooser.showSaveDialog(frame);  //  Center the file chooser to the frame
        
            if(userSelection == JFileChooser.APPROVE_OPTION) 
            {
                File file = fileChooser.getSelectedFile();
                
                try 
                {
                    // Tell the monitor to write the file with it's state
                    this.monitor.saveLabState(file);
                    
                    JOptionPane.showMessageDialog(frame, MSG_LAB_STATE_SAVED,"Save File Confirmation",JOptionPane.INFORMATION_MESSAGE); 
                } 
                catch(IOException e){JOptionPane.showMessageDialog(frame, MSG_ERROR_DURING_SAVE + e.getMessage(),"Save File Error",JOptionPane.ERROR_MESSAGE);}
            }
        });

        //  On load btn press prompt the user to select a file and load the data into the monitor (@author Marini Enrico)
        loadBtn.addActionListener((ActionEvent) ->
        {
            //  If there is information that get's potentially overritten make the user choose to continue or not
            if(!this.monitor.getEvents().isEmpty())
            {
                int userChoice = JOptionPane.showConfirmDialog(frame, MSG_UNSAVED_DATA_WARNING, "Overwrite Confirmation",JOptionPane.YES_NO_OPTION);
                if(userChoice == JOptionPane.NO_OPTION)
                {
                    return;
                }
            }

            //  Show the user a File Chooser with a custom title
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Lab State");
            int userSelection = fileChooser.showOpenDialog(frame);  //  Center the file chooser to the frame
    
            if (userSelection == JFileChooser.APPROVE_OPTION) 
            {
                File file = fileChooser.getSelectedFile();
                
                try 
                {
                    // Tell the monitor to read the state from the file
                    this.monitor.loadLabState(file);
                    
                    JOptionPane.showMessageDialog(frame,MSG_LAB_STATE_LOADED,"Load File Confirmation",JOptionPane.INFORMATION_MESSAGE);
                } 
                catch(IOException e){JOptionPane.showMessageDialog(frame, MSG_ERROR_DURING_LOAD + e.getMessage(),"Loading Error",JOptionPane.ERROR_MESSAGE);}
                catch(ClassNotFoundException e){JOptionPane.showMessageDialog(frame,MSG_ERROR_IN_SELECTED_FILE + e.getMessage(),"File Selection Error",JOptionPane.ERROR_MESSAGE);}
            }
        });

        //  On settings btn push visualize the settings panel
        settingsBtn.addActionListener((ActionEvent) ->
        {
            frame.showSettings();
        });

        //  On play btn push tell the monitor to play
        playBtn.addActionListener((ActionEvent) ->
        {
            this.monitor.play();
        });

        //  On pause btn push tell the monitor to pause
        pauseBtn.addActionListener((ActionEvent) ->
        {
            this.monitor.pause();
        });

        //--------PAUSE INDICATOR--------

        //  Initialization
        pauseIndicator = new JLabel("\u2022");  // Unicode dot

        //  Size and font setting
        pauseIndicator.setFont(new Font(pauseIndicator.getFont().getName(), Font.PLAIN,35));
        pauseIndicator.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseIndicator.setPreferredSize(INDICATOR_DIMENSION);
        pauseIndicator.setMinimumSize(INDICATOR_DIMENSION);
        pauseIndicator.setMaximumSize(INDICATOR_DIMENSION);

        //--------ASSEMBLY OF THE CONTROL PANEL--------

        //  Put the settingsBtn to the left
        add(settingsBtn, BorderLayout.WEST);    

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

        this.monitor.addLabListener(this);  //  Register listener to the LabMonitor
    }

    /**
     * Service method to update the color of the pause indicator
     */
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
