import javax.swing.*;
import java.awt.*;

class Assignment {
    public static boolean enhanced = false;
}

public class A3Basic extends JPanel {

    A3Basic() {
        // create the model
        GameModel model = new GameModel(60, 700, 200, 20);

        EditView editView = new EditView(model);
        PlayView playView = new PlayView(model, editView);
        MessageView messageView = new MessageView(model);
        ToolBarView toolbarView = new ToolBarView(model);

        // adding observer to game model
        model.addObserver(playView);
        model.addObserver(editView);
        model.addObserver(messageView);
        model.addObserver(toolbarView);
        model.setChangedAndNotify();

        editView.setPreferredSize(new Dimension(700, 200));

        // layout the views
        setLayout(new BorderLayout());

        add(messageView, BorderLayout.NORTH);

        // nested Border layout for edit view
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout());
        editPanel.add(toolbarView, BorderLayout.NORTH);
        editPanel.add(editView, BorderLayout.CENTER);
        add(editPanel, BorderLayout.SOUTH);

        // main playable view will be resizable
        add(playView, BorderLayout.CENTER);

        // for getting key events into PlayView
        playView.requestFocusInWindow();

    }

    public static void main(String[] args) {
        // create the window
        JFrame f = new JFrame("A3Basic"); // jframe is the app window
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(700, 600); // window size
        f.setContentPane(new A3Basic()); // add main panel to jframe
        f.setVisible(true); // show the window
    }
}