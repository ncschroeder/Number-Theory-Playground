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
        StringTooLongException.throwIfTooLong(text);
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
    
    /**
     * It can take a long time to display a long string in a JTextArea, the parent class of NtpTextArea.
     * NtpTextAreas are mostly used to display short strings that are less than a thousand chars long.
     * The Divisibility class uses this class to display PFs of factors of an input number. The
     * GoldbachConjecture class uses this class to display prime number pairs. The strings that get
     * built that contain those can be pretty long, like over 50,000 chars. This exception will be
     * thrown if they exceed that length. Then, instead of displaying that long string, the error
     * message below, which is appropriate for both situations, will be displayed.
     */
    public static final class StringTooLongException extends IllegalArgumentException {
        private StringTooLongException() {}
        
        private static final StringTooLongException instance = new StringTooLongException();
        
        private static void throwIfTooLong(String s) {
            if (s.length() > 50_000) {
                throw instance;
            }
        }
        
        public static final String ERROR_MESSAGE =
            "They take up too much text so they won't be displayed.";
    }
}
