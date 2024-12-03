package numbertheoryplayground.gui;

import javax.swing.JTextArea;
import java.awt.Dimension;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static numbertheoryplayground.Misc.getWhiteSpace;

/**
 * JTextArea with custom default settings and constructors.
 */
public class NTPTextArea extends JTextArea {
    public NTPTextArea() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        setFont(NTPGUI.garamondFontSize25);
        setBackground(NTPGUI.backgroundColor);
        setMaximumSize(new Dimension(800, 100_000));
    }
    
    public NTPTextArea(String text) {
        this();
        setText(text);
    }
    
    /**
     * Sets the text to the elements of the Stream provided with each element separated by the separator.
     */
    public NTPTextArea(Stream<String> strings, String separator) {
        this(strings.collect(Collectors.joining(separator)));
    }
    
    public NTPTextArea(Stream<String> strings) {
        this(strings, getWhiteSpace(7));
    }
}