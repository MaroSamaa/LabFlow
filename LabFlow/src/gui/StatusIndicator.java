package gui;

import javax.swing.*;
import java.awt.*;

public class StatusIndicator extends JPanel
{  
    //--------INSTANCE VARIABLES--------
    /**
     * The color of the indicator
     */
    private Color color;

    //--------CONSTRUCTORS--------
    /**
     * Constructor method for StatusIndicator
     * @param size the size of the indicator
     */
    public StatusIndicator(int size) 
    {
        // Set fixed dimentions to ensure the circle won't turn into an oval
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));
        setOpaque(false);

        this.color = Color.WHITE; // Set the inititial color to a generic white 
    }

    //--------ACCESSOR METHODS--------

    /**
     * Setter method for the color of the indicator
     * @param color the wanted color for the indicator
     */
    public void setColor(Color color) 
    {
        this.color = color ;
        repaint(); // Repaint the indicator with the new color
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        int margin = 2;                             //  Small margin just to be safe
        int diameter = getWidth() - (margin * 2);   //Make the diameter a bit smaller that the space so the circle won't "collide" with the panel's border

        // Draw the indicator
        g2d.setColor(color);
        g2d.fillOval(margin, margin, diameter, diameter);
        g2d.setColor(new Color(0, 0, 0, 60));   // Add a bit border to give the 3d effect
        g2d.drawOval(margin, margin, diameter, diameter);
    }
}

