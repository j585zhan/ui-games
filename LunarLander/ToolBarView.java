import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

// button controller
class UndoController implements ActionListener {
    GameModel model;

    public UndoController(GameModel model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        model.undo();
    }
}

class RedoController implements ActionListener {
    GameModel model;

    public RedoController(GameModel model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        model.redo();
    }
}

// the edit toolbar
public class ToolBarView extends JPanel implements Observer {
    GameModel model;

    JButton undo = new JButton("Undo");
    JButton redo = new JButton("Redo");

    public ToolBarView(GameModel model) {

        // save model
        this.model = model;

        setLayout(new FlowLayout(FlowLayout.LEFT));

        // prevent buttons from stealing focus
        undo.setFocusable(false);
        redo.setFocusable(false);

        // add button controller
        undo.addActionListener(new UndoController(model));
        redo.addActionListener(new RedoController(model));

        // set default disabled
        undo.setEnabled(false);
        redo.setEnabled(false);

        add(undo);
        add(redo);
    }

    @Override
    public void update(Observable o, Object arg) {
        undo.setEnabled(model.canUndo());
        redo.setEnabled(model.canRedo());
    }
}