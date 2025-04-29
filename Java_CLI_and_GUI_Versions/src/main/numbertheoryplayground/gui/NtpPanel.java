package numbertheoryplayground.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static numbertheoryplayground.gui.NtpGui.*;

/**
 * JPanel whose background color gets automatically set to the correct one and has additional
 * functionality. All methods do an action and then return the instance to allow for chaining.
 */
public class NtpPanel extends JPanel {
    public NtpPanel() {
        setBackground(BACKGROUND_COLOR);
    }
    
    /**
     * setLayout has a void return type so, unlike with the add method below, it can't be overridden
     * to return this NtpPanel.
     */
    public NtpPanel chainedSetLayout(LayoutManager lm) {
        setLayout(lm);
        return this;
    }
    
    public NtpPanel setToBoxLayoutWithPageAxis() {
        return chainedSetLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }
    
    public NtpPanel setToBoxLayoutWithLineAxis() {
        return chainedSetLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }
    
    public NtpPanel chainedSetMaxSize(Dimension dimension) {
        setMaximumSize(dimension);
        return this;
    }
    
    public NtpPanel setMaxSizeToPreferredSize() {
        return chainedSetMaxSize(getPreferredSize());
    }
    
    public NtpPanel center() {
        centerComponent(this);
        return this;
    }
    
    /**
     * The add method in the Container class (super.add) returns a Component and NtpPanel is an
     * indirect subclass of Component so we can override the add method to return this NtpPanel.
     */
    @Override
    public NtpPanel add(Component c) {
        super.add(c);
        return this;
    }
    
    public NtpPanel addAll(Component... comps) {
        for (var comp : comps) {
            add(comp);
        }
        return this;
    }
    
    public NtpPanel addAll(Stream<? extends Component> comps) {
        comps.forEachOrdered(this::add);
        return this;
    }
    
    public static <T> NtpPanel createTablePanel(
        List<String> headings,
        Stream<T> contentSourceStream,
        Function<T, Stream<String>> getRowStrings
    ) {
        var layout = new GridLayout(0, headings.size(), 4, 4);
        
        Stream<JLabel> headingLabels =
            headings.stream().map(NtpGui::createAnswerTableColumnHeadingLabel);
        
        Stream<JLabel> contentLabels =
            contentSourceStream
            .flatMap(getRowStrings)
            .map(NtpGui::createUncenteredAnswerContentLabel);
        
        return
            new NtpPanel()
            .chainedSetLayout(layout)
            .addAll(headingLabels)
            .addAll(contentLabels);
    }
}
