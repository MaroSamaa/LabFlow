import concurrent.LabMonitor;
import gui.Frame;
import java.awt.*;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * Main class of the project, contains the main method.
 * Also set's UIManager global preferences.
 * @author Marini Enrico
 */
public class Main 
{

    private static final String MSG_FLATLAF_ERROR = "FlatLaf wasn't successfully initialized";

    public static void main(String[] args)
    {
        //  Set global ui manager preferences
        try 
        {
            UIManager.setLookAndFeel(new FlatLightLaf());       //  Set L&F to flatlaf (Light theme)

            UIManager.put("Table.showHorizontalLines", true);   // Enable table's horizontal lines globally
            UIManager.put("Table.showVerticalLines", true);     // Enable table's vertical lines globally

            UIManager.put("TableHeader.background", new Color(203, 195, 227));  //  Table header color

            // Reinstate spacing for the table cells to give roomm for the lines
            UIManager.put("Table.intercellSpacing", new Dimension(1, 1));  
        } 
        catch (Exception e) 
        {
            System.out.println(MSG_FLATLAF_ERROR);
        }     
        new Frame(new LabMonitor());
    }
}
