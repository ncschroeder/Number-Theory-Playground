package numbertheoryplayground.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;

import static numbertheoryplayground.gui.NTPGUI.*;

/**
 * JPanel whose background color gets automatically set to the correct one and has additional
 * functionality. All methods do an action and then return the instance to allow for chaining.
 */
public class NTPPanel extends JPanel {
    public NTPPanel() {
        setBackground(backgroundColor);
    }
    
    /**
     * setLayout has a void return type so, unlike with the add method below, it can't be overridden to
     * return this NTPPanel.
     */
    public NTPPanel chainedSetLayout(LayoutManager lm) {
        setLayout(lm);
        return this;
    }
    
    public NTPPanel setToBoxLayoutWithPageAxis() {
        return chainedSetLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }
    
    public NTPPanel setToBoxLayoutWithLineAxis() {
        return chainedSetLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }
    
    public NTPPanel setMaxSizeToPreferredSize() {
        setMaximumSize(getPreferredSize());
        return this;
    }
    
    public NTPPanel center() {
        centerComponent(this);
        return this;
    }
    
    /**
     * The add method in the Container class (super.add) returns a Component and NTPPanel is an indirect
     * subclass of Component so we can override the add method to return this NTPPanel.
     */
    @Override
    public NTPPanel add(Component c) {
        super.add(c);
        return this;
    }
    
    /**
     * Adds a rigid square area that's as wide and tall as the size param. Whether components are
     * laid out horizontally or vertically, this will add a gap.
     */
    public NTPPanel addGap(int size) {
        return add(createGap(size));
    }
    
    public NTPPanel addLabel(String text, Font font) {
        return add(createLabel(text, font));
    }
    
    public NTPPanel addCenteredLabel(String text, Font font) {
        return add(createCenteredLabel(text, font));
    }
}