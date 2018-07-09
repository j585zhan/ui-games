import javax.swing.*;
import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

class KeyController extends KeyAdapter {
    GameModel model;

    public KeyController(GameModel model) {
        this.model = model;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                if (model.isShipCrashed() || model.isShipLanded()) {
                    model.restartShip();
                } else {
                    model.toggleShip();
                }
                break;
            case KeyEvent.VK_W:
                model.thrustUp();
                break;
            case KeyEvent.VK_S:
                model.thrustDown();
                break;
            case KeyEvent.VK_A:
                model.thrustLeft();
                break;
            case KeyEvent.VK_D:
                model.thrustRight();
                break;
        }
    }
}
// the actual game view
public class PlayView extends JPanel implements Observer {
    EditView editView;
    GameModel model;

    public PlayView(GameModel model, EditView editView) {

        // save model
        this.model = model;

        // save editView
        this.editView = editView;

        // needs to be focusable for keylistener
        setFocusable(true);

        // want the background to be black
        setBackground(Color.BLACK);

        this.addKeyListener(new KeyController(model));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;

        Point2d shipPos = model.getShipPos();

        // save affine transform matrix
        AffineTransform savedMatrix = g2.getTransform();
        g2.translate(this.getWidth()/2-5, this.getHeight()/2-5);
        g2.scale(3, 3);
        g2.translate(-shipPos.getX()-5, -shipPos.getY()-5);

        editView.togglePlayViewMode(); /* avoiding draw dragging cycles */
        editView.paintComponent(g2);
        editView.togglePlayViewMode(); /* avoiding draw dragging cycles */

        // draw ship
        g2.setColor(Color.BLUE);
        if (Assignment.enhanced && model.isShipCrashed()) {
            Rectangle2D.Double shipShape = new Rectangle2D.Double(shipPos.getX(), shipPos.getY(), 10, 10);
            int crash_gap = 2; // gap between 2 flight components

            // y crashed component ENHANCED **
            int [] ycrash = {
                (int) shipPos.getY(),
                (int) (shipPos.getY() + 10), /* width of ship */
                (int) (shipPos.getY() + 10),
                (int) (shipPos.getY() + 7),  /* 1/4 of width */
                (int) (shipPos.getY() + 5),  /* 2/4 of width */
                (int) (shipPos.getY() + 3),  /* 3/4 of width */
                (int) (shipPos.getY()),
            };

            // x for left crashed component ENHANCED **
            int [] xcrashl = {
                (int) (shipPos.getX() - crash_gap/2),           /* left most point */
                (int) (shipPos.getX() - crash_gap/2),
                (int) (shipPos.getX() + 6 - crash_gap/2),       /* out point */
                (int) (shipPos.getX() + 6 - crash_gap/2) - 1,   /* in point */
                (int) (shipPos.getX() + 6 - crash_gap/2),
                (int) (shipPos.getX() + 6 - crash_gap/2) - 1,
                (int) (shipPos.getX() + 6 - crash_gap/2)
            };

            // x for right crashed component ENHANCED **
            int [] xcrashr = {
                (int) (shipPos.getX() + 10 + crash_gap/2),      /* right most point */
                (int) (shipPos.getX() + 10 + crash_gap/2),
                (int) (shipPos.getX() + 6 + crash_gap/2),       /* in point */
                (int) (shipPos.getX() + 6 + crash_gap/2) - 1,   /* out point */
                (int) (shipPos.getX() + 6 + crash_gap/2),
                (int) (shipPos.getX() + 6 + crash_gap/2) - 1,
                (int) (shipPos.getX() + 6 + crash_gap/2)
            };

            /* draw two parts */
            g2.fillPolygon(xcrashl, ycrash, 7);
            g2.fillPolygon(xcrashr, ycrash, 7);

        } else {
            /* without enhanced */
            Rectangle2D.Double shipShape = new Rectangle2D.Double(shipPos.getX(), shipPos.getY(), 10, 10);
            g2.fill(shipShape);
        }

        // reset g2
        g2.setTransform(savedMatrix);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}