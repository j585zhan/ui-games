import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;

class CanvasController extends MouseAdapter{
    Model model;

    public CanvasController(Model model_) {
        model = model_;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        model.mouseDragged(mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        model.mousePressed(mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        model.mouseReleased();
    }
}

class CanvasView extends JPanel implements Observer{

    private Model model;
    CanvasView(Model model_){
        model = model_;

        this.addMouseListener(new CanvasController(model));

        this.addMouseMotionListener(new CanvasController(model));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for (Stroke stroke : model.getStrokes()) {
            /* get value from model */
            int centerX = stroke.getCenterX();
            int centerY = stroke.getCenterY();
            int rotate = stroke.getRotate();
            int [] x_points = stroke.getX_points();
            int [] y_points = stroke.getY_points();
            int n_points = stroke.getN_points();
            double scaleD = (double)stroke.getScale() / 10;

            AffineTransform M = g2.getTransform();

            if (stroke.pointsChanged()) stroke.cachePointsArray();

            /* transformation */
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(centerX, centerY);
            g2.rotate(Math.toRadians(rotate));
            g2.scale(scaleD, scaleD);
            g2.translate(-centerX, -centerY);

            /* if selected */
            if (stroke.isHighlighted()) {
                g2.setColor(stroke.getBackColor());
                g2.setStroke(new BasicStroke((float) (stroke.getBackThickness()/scaleD)));
                g2.drawPolyline(x_points, y_points, n_points);
            }

            /* draw stroke */
            g2.setColor(stroke.getFrontColor());
            g2.setStroke(new BasicStroke((float) (stroke.getFrontThickness()/scaleD)));

            if (stroke.isClosed() && assignment.enhanced) {
                g2.fillPolygon(x_points, y_points, n_points);
            } else {
                g2.drawPolyline(x_points, y_points, n_points);
            }
            g2.setTransform(M);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        setPreferredSize(new Dimension(model.getPreferedX(), model.getPreferedY()));
        revalidate();
        repaint();
    }
}
