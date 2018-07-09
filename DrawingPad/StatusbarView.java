import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.util.Observable;
import java.util.Observer;

class StatusbarView extends JPanel implements Observer{
    private JLabel status;

    private Model model;

    private String toStatusMsg(int value) {
        return Integer.toString(value) + " Strokes";
    }

    StatusbarView(Model model_){
        status = new JLabel("0 Strokes");
        model = model_;

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(status);
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));

        layout.putConstraint(SpringLayout.WEST, status, 10,
            SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.NORTH, status, 10,
            SpringLayout.NORTH, this);
    }

    @Override
    public void update(Observable observable, Object o) {
        String text = toStatusMsg(model.getStrokeCounter());
        if (model.isToolbarActive()) {
            text += ", Selection (" + model.getNPoints()
                + " points, scale: " + model.getScale()/10 + "." + model.getScale()%10
                + ", rotation " + model.getRotate() + ")";
        }
        status.setText(text);
    }
}
