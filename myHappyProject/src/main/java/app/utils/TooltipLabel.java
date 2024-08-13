package app.utils;

import javax.swing.*;
import java.awt.*;

public class TooltipLabel{
    String labelText;
    String toolTipText;
    public TooltipLabel(String labelText, String toolTipText) {
        this.labelText = labelText;
        this.toolTipText = toolTipText;
    }

    public JPanel getPanel(){
        // Create a label with a tooltip icon that shows the tooltip when hovered
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        labelPanel.add(label);

        // Create a custom tooltip icon (question mark circle)
        try{
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/question-mark-icon.png"));
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
            JLabel tooltipLabel = new JLabel(imageIcon);

            tooltipLabel.setToolTipText(toolTipText);
            labelPanel.add(tooltipLabel);

        } catch (Exception e) {
            System.out.println("Error loading image:" + e.toString());
        }

        return labelPanel;
    }
}
