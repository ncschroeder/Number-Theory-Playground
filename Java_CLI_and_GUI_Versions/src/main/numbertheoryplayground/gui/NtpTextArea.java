package numbertheoryplayground.gui;

import javax.swing.JTextArea;
import java.awt.Dimension;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static numbertheoryplayground.Misc.getSpace;

/**
 * JTextArea with custom default settings and constructors.
 */
public final class NtpTextArea extends JTextArea {
    public NtpTextArea(int width) {
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        setFont(NtpGui.GARAMOND_25);
        setBackground(NtpGui.BACKGROUND_COLOR);
        setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
    }
    
    public NtpTextArea(String text, int width) {
        this(width);
        if (text.length() > 50_000) {
            throw StringTooLongException.instance;
        }
        setText(text);
    }
    
    public NtpTextArea(String text) {
        this(text, 750);
    }
    
    /**
     * Sets the text to the elements of the Stream provided with each element separated by the separator.
     */
    public NtpTextArea(Stream<String> stream, String separator, int width) {
        this(stream.collect(Collectors.joining(separator)), width);
    }
    
    private static final String sevenSpaces = getSpace(7);
    
    public static NtpTextArea createNarrowOneWithStreamElements(Stream<String> stream) {
        return new NtpTextArea(stream, sevenSpaces, 700);
    }
    
    public static NtpTextArea createWideOneWithStreamElements(Stream<String> stream) {
        return new NtpTextArea(stream, sevenSpaces, 1_000);
    }
    
    public static NtpTextArea createWithStreamElementsOnSeparateLines(Stream<String> stream) {
        return new NtpTextArea(stream, "\n", 1_000);
    }
    
    public void clear() {
        setText("");
    }
    
    public static final class StringTooLongException extends IllegalArgumentException {
        private StringTooLongException() {}
        
        private static final StringTooLongException instance = new StringTooLongException();
        
        public static final String ERROR_MESSAGE =
            "They take up too much text so they won't be displayed.";
    }
}
