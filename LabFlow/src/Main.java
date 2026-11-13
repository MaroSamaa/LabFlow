import concurrent.LabMonitor;
import gui.Frame;
import java.awt.*;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

public class Main 
{
    public static void main(String[] args)
    {
        try 
        {
            UIManager.setLookAndFeel(new FlatLightLaf());

            UIManager.put("Table.showHorizontalLines", true);   // Enable table's horizontal lines globally
            UIManager.put("Table.showVerticalLines", true);     // Enable table's vertical lines globally
            UIManager.put("TableHeader.background", new Color(203, 195, 227));

            // Reinstate spacing for the table cells to give roomm for the lines
            UIManager.put("Table.intercellSpacing", new Dimension(1, 1));  
        } 
        catch (Exception e) 
        {
            System.out.println("FlatLaf wasn't successfully initialized");
        }     
        new Frame(new LabMonitor());
    }
}
