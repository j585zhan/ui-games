import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static java.lang.Double.max;
import static java.lang.Double.min;

class Point extends Point2D {
    private double x;
    private double y;

    public Point (double x_, double y_) {
        x = x_;
        y = y_;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }

    @Override
    public void setLocation(double x_, double y_) {
        x = x_;
        y = y_;
    }
}

class Stroke {
    private ArrayList<Point>points;

    private int rotate = 0;
    private int scale = 10;
    private boolean hightlight = false;
    private boolean isDeleted = false;

    private float frontThickness = 2.0f;
    private float backThickness = 6.0f;

    private Color frontColor = Color.BLACK;
    private Color backColor = Color.YELLOW;

    Stroke() {
        points = new ArrayList<>();
    }

    public void clear() {
        isDeleted = true;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
        pointsChanged = true;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
        pointsChanged = true;
    }

    public void setHightlight() {
        hightlight = true;
    }

    public void unsetHightlight() {
        hightlight = false;
    }

    public void addPoint(Point point) {
        points.add(point);
        pointsChanged = true;
    }

    public void addPoint(int x, int y) {
        addPoint(new Point(x, y));
    }

    public boolean isClosed() {
        if (n_points <= 2) return false;

        int xdiff = (x_points[n_points-1] - x_points[0]);
        int ydiff = (y_points[n_points-1] - y_points[0]);

        return xdiff * xdiff + ydiff * ydiff <= 25;
    }

    public double minimum_distance(Point2D v, Point2D w, Point2D p) {
        final double l2 = v.distanceSq(w);

        if (l2 == 0.0) return p.distance(v);

        double t = ((p.getX() - v.getX()) * (w.getX() - v.getX())
            + (p.getY() - v.getY()) * (w.getY() - v.getY())) / l2;
        t = max(0, min(1, t));
        return p.distance(new Point(
            v.getX()+t*(w.getX() - v.getX()),
            v.getY()+t*(w.getY() - v.getY())));
    }

    public boolean checkPoint(int i, int x, int y) {
        return minimum_distance(translate(points.get(i)), translate(points.get(i+1)), new Point(x, y)) <= 5;
    }

    public Point translate(Point p) {
        double scaleD = (double)scale/10;

        AffineTransform af = new AffineTransform();
        af.translate(centerX, centerY);
        af.rotate(Math.toRadians(rotate));
        af.scale(scaleD, scaleD);
        af.translate(-centerX, -centerY);
        Point2D newp = af.transform(p, new Point(0, 0));
        return new Point(newp.getX(), newp.getY());
    }

    public boolean isClicked(int x, int y) {
        for (int i = 0; i < points.size() - 1; i++) {
            if (checkPoint(i, x, y)) {
                return true;
            }
        }
        return false;
    }

    private boolean pointsChanged = false;
    private int [] x_points, y_points;
    private int n_points = 0;
    private int centerX = 0, centerY = 0;
    int preferedX = 0, preferedY = 0;

    public int getN_points() {
        return n_points;
    }

    public void translatePoint(int x, int y) {
        for (int i = 0; i < points.size(); i++) {
            points.get(i).addX(x);
            points.get(i).addY(y);
        }
        pointsChanged = true;
    }

    public void cachePointsArray() {
        int maxX = 0, minX = 0, maxY = 0, minY = 0;
        x_points = new int[points.size()];
        y_points = new int[points.size()];
        preferedX = 0;
        preferedY = 0;
        for (int i = 0; i < points.size(); i++) {
            x_points[i] = (int)points.get(i).getX();
            y_points[i] = (int)points.get(i).getY();
            if (i == 0) {
                maxX = minX = x_points[0];
                maxY = minY = y_points[0];
            }
            if (x_points[i] > maxX) maxX = x_points[i];
            if (x_points[i] < minX) minX = x_points[i];
            if (y_points[i] > maxY) maxY = y_points[i];
            if (y_points[i] < minY) minY = y_points[i];
            Point p = translate(new Point(x_points[i], y_points[i]));
            if (preferedX < p.getX()) preferedX = (int) p.getX();
            if (preferedY < p.getY()) preferedY = (int) p.getY();
        }
        n_points = points.size();
        centerX = (maxX + minX) / 2;
        centerY = (maxY + minY) / 2;
        pointsChanged = false;
    }

    public boolean pointsChanged() {
        return pointsChanged;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public boolean isHighlighted() {
        return hightlight;
    }

    public Color getBackColor() {
        return backColor;
    }

    public float getBackThickness() {
        return backThickness;
    }

    public Color getFrontColor() {
        return frontColor;
    }

    public float getFrontThickness() {
        return frontThickness;
    }

    public int[] getX_points() {
        return x_points;
    }

    public int[] getY_points() {
        return y_points;
    }

    public int getMaxX() {
        return preferedX;
    }

    public int getMaxY() {
        return preferedY;
    }
}
