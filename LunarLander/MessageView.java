import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class MessageView extends JPanel implements Observer {
    GameModel model;

    // status messages for game
    JLabel fuel = new JLabel("fuel");
    JLabel speed = new JLabel("speed");
    JLabel message = new JLabel("message");

    public MessageView(GameModel model) {

        // store model
        this.model = model;

        // want the background to be black
        setBackground(Color.BLACK);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(fuel);
        add(speed);
        add(message);

        for (Component c: this.getComponents()) {
            c.setForeground(Color.WHITE);
            c.setPreferredSize(new Dimension(100, 20));
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        // load info from model
        double shipFuel = model.getShipFuel();
        double shipSpeed = model.getShipSpeed();

        // set text and color
        fuel.setText("fuel: " + toString(shipFuel));
        fuel.setForeground(shipFuel < 10 ? Color.RED : Color.WHITE);
        speed.setText("speed: " + toTwoDec(shipSpeed));
        speed.setForeground(model.isShipSpeedSafe() ? Color.GREEN : Color.WHITE);
        message.setText(model.isShipPaused() ? "(Paused)" :
            model.isShipLanded() ? "LANDED!" :
                model.isShipCrashed() ? "CRASH" : "");
    }

    private String toTwoDec(double value) {
        return String.format("%.2f", value);
    }

    private String toString(double value) {
        return String.format("%.0f", value);
    }
}