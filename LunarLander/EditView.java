import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

class MouseController extends MouseAdapter {
    GameModel model;

    public MouseController(GameModel model) {
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            model.setLandPadPos(mouseEvent.getX(), mouseEvent.getY());
            model.saveState(true, false);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        model.clickHandler(mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        model.releaseHandler();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        model.dragHandler(mouseEvent.getX(), mouseEvent.getY());
    }
}

// the editable view of the terrain and landing pad
public class EditView extends JPanel implements Observer {
    GameModel model;

    private boolean playViewMode = false;

    public EditView(GameModel model) {
        this.model = model;

        // want the background to be black
        this.setBackground(Color.BLACK);

        this.addMouseListener(new MouseController(model));
        this.addMouseMotionListener(new MouseController(model));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // get info from game model
        int [] xPoints = model.getTerrainXPoints();
        int [] yPoints = model.getTerrainYPoints();
        Rectangle2D.Double world = (Rectangle2D.Double) model.getWorldBounds();
        Rectangle2D.Double landPad = (Rectangle2D.Double) model.getLandPad();

        // draw light grey "world"
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect((int)world.x, (int)world.y, (int)world.width, (int)world.height);

        // draw terrain
        g2.setColor(Color.DARK_GRAY);
        g2.fillPolygon(model.getTerrainPolygon());

        if (!playViewMode) {
            // draw grey circle around peak
            g2.setColor(Color.GRAY);
            for (int i = 0; i < model.getNPoints() - 2; i++) {
                g2.drawOval(xPoints[i] - 15, yPoints[i] - 15, 30, 30);
            }
        }

        // draw land pad
        g2.setColor(Color.RED);
        g2.fillRect((int)landPad.x, (int)landPad.y, (int)landPad.width, (int)landPad.height);
    }

    public void togglePlayViewMode() {
        playViewMode = !playViewMode;
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

}