import javax.swing.*;

import java.awt.*;

class assignment {
    public final static boolean enhanced = false;
}

public class A2Basic {
    public static void main(String[] args) {
        JFrame frame = new JFrame("A2 Basic");

        /* Logic Model */
        Model model = new Model();

        /* Three Part Views */
        ToolbarView toolbar= new ToolbarView(model);
        model.addObserver(toolbar);

        CanvasView canvas = new CanvasView(model);
        model.addObserver(canvas);

        StatusbarView statusbar= new StatusbarView(model);
        model.addObserver(statusbar);

        model.notifyObservers();

        JPanel panel = new JPanel(new BorderLayout());

        /* Preferred Size */
        toolbar.setPreferredSize(new Dimension(800, 50));
        canvas.setPreferredSize(new Dimension(0, 0));
        statusbar.setPreferredSize(new Dimension(800, 50));

        /* ScrollPane */
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setPreferredSize(new Dimension(800, 500));
        jScrollPane.setViewportView(canvas);

        /* Add all to Panel */
        frame.getContentPane().add(panel);
        panel.add(toolbar, BorderLayout.PAGE_START);
        panel.add(jScrollPane, BorderLayout.CENTER);
        panel.add(statusbar, BorderLayout.PAGE_END);

        /* Frame Setup */
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
