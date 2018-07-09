import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

class DeleteController implements ActionListener {
    Model model;

    DeleteController(Model model_) {
        model = model_;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        model.delete();
    }
}

class DuplicateController implements ActionListener {
    Model model;

    DuplicateController(Model model_) {
        model = model_;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        model.duplicate();
    }
}
class ScaleController implements ChangeListener {
    Model model;
    private JSlider scaleSlider;

    ScaleController(Model model_, JSlider jSlider) {
        model = model_;
        scaleSlider = jSlider;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        model.setScale(scaleSlider.getValue());
    }
}

class RotateController implements ChangeListener {
    Model model;
    private JSlider rotateSlider;

    RotateController(Model model_, JSlider jSlider) {
        model = model_;
        rotateSlider = jSlider;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        model.setRotate(rotateSlider.getValue());
    }
}

class ToolbarView extends JPanel implements Observer{
    private JButton deleteButton;
    private JButton duplicateButton;
    private JLabel scaleLabel;
    private JSlider scaleSlider;
    private JLabel scaleValue;
    private JLabel rotateLabel;
    private JSlider rotateSlider;
    private JLabel rotateValue;

    private Model model;

    private String toOneDecimal(int value) {
        String s1, s2;
        s1 = toString(value / 10);
        s2 = toString(value % 10);
        return s1 + "." + s2;
    }

    private String toString(int value) {
        return Integer.toString(value);
    }

    ToolbarView (Model model_) {
        deleteButton = new JButton("Delete");
        duplicateButton = new JButton("Duplicate");
        scaleSlider = new JSlider(5, 20, 10);
        rotateSlider = new JSlider(-180, 180, 0);
        scaleLabel = new JLabel("Scale");
        scaleValue= new JLabel("1.0");
        rotateLabel = new JLabel("Rotate");
        rotateValue= new JLabel("0");

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        this.setBorder(new BevelBorder(BevelBorder.RAISED));
        this.setSize(800, 10);

        if (assignment.enhanced) {
            this.add(duplicateButton);
        }
        this.add(deleteButton);
        this.add(scaleLabel);
        this.add(scaleSlider);
        this.add(scaleValue);
        this.add(rotateLabel);
        this.add(rotateSlider);
        this.add(rotateValue);

        model = model_;

        deleteButton.addActionListener(new DeleteController(model));
        duplicateButton.addActionListener(new DuplicateController(model));
        scaleSlider.addChangeListener(new ScaleController(model, scaleSlider));
        rotateSlider.addChangeListener(new RotateController(model, rotateSlider));
    }

    @Override
    public void update(Observable observable, Object o) {
        int scale = scaleSlider.getValue();
        int rotate = rotateSlider.getValue();
        boolean isToolbarActive = model.isToolbarActive();
        if (isToolbarActive) {
            scale = model.getScale();
            rotate = model.getRotate();
        }

        /* Setup Values */
        scaleValue.setText(toOneDecimal(scale));
        scaleSlider.setValue(scale);
        rotateValue.setText(toString(rotate));
        rotateSlider.setValue(rotate);

        /* Detect Grey Out */
        deleteButton.setEnabled(isToolbarActive);
        duplicateButton.setEnabled(isToolbarActive);
        scaleLabel.setEnabled(isToolbarActive);
        scaleSlider.setEnabled(isToolbarActive);
        scaleValue.setEnabled(isToolbarActive);
        rotateLabel.setEnabled(isToolbarActive);
        rotateSlider.setEnabled(isToolbarActive);
        rotateValue.setEnabled(isToolbarActive);
    }
}
