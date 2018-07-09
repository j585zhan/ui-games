import java.util.ArrayList;
import java.util.Observable;

class Model extends Observable {
    private int DEFAULT_ROTATE = 0;
    private int DEFAULT_SCALE = 10;

    private Stroke currentStroke = null;

    private ArrayList<Stroke> strokes;
    private Stroke drawingStroke = null;
    boolean isHit = false;
    boolean drawing = false;
    boolean moving = false;

    int cachedX = 0, cachedY = 0;
    Model() {
        strokes = new ArrayList<>();
        setChanged();
    }

    public void setCurrentStroke(Stroke stroke) {
        currentStroke = stroke;
        setChanged();
        notifyObservers();
    }

    public void setRotate(int rotate) {
        currentStroke.setRotate(rotate);
        setChanged();
        notifyObservers();
    }

    public int getRotate() {
        return currentStroke == null ? DEFAULT_ROTATE : currentStroke.getRotate();
    }

    public void setScale(int scale) {
        currentStroke.setScale(scale);
        setChanged();
        notifyObservers();
    }

    public int getScale() {
        return currentStroke == null ? DEFAULT_SCALE : currentStroke.getScale();
    }

    public int getNPoints() {
        return currentStroke.getN_points();
    }

    public boolean isToolbarActive () {
        return currentStroke != null;
    }

    public void delete() {
        if (currentStroke != null) {
            currentStroke.clear();
            currentStroke = null;

            // remove deleted from strokes
            for (int i = 0; i < strokes.size(); i++) {
                if (strokes.get(i).isDeleted()) {
                    strokes.remove(i);
                    drawingStroke = null;
                }
            }

            setChanged();
            notifyObservers();
        }
    }

    public int getStrokeCounter() {
        return strokes.size();
    }

    public int getPreferedX() {
        int preferedX = 0;
        for (Stroke s : strokes) {
            if (preferedX < s.getMaxX()) {
                preferedX = s.getMaxX();
            }
        }
        return preferedX;
    }

    public int getPreferedY() {
        int preferedY = 0;
        for (Stroke s : strokes) {
            if (preferedY < s.getMaxY()) {
                preferedY = s.getMaxY();
            }
        }
        return preferedY;
    }

    public void mousePressed(int mx, int my) {
        for (int i = strokes.size() - 1; i >= 0; i--) {
            if (strokes.get(i).isClicked(mx, my)) {
                isHit = true;
                if (drawingStroke != null) {
                    drawingStroke.unsetHightlight();
                }
                drawingStroke = strokes.get(i);
                drawingStroke.setHightlight();
                setCurrentStroke(drawingStroke);
                break;
            }
        }
        cachedX = mx;
        cachedY = my;

        setChanged();
        notifyObservers();
    }


    public void mouseReleased() {
        if (!isHit && !moving) {
            if (drawingStroke != null) {
                drawingStroke.unsetHightlight();
            }
            drawingStroke = null;
            setCurrentStroke(null);
        }
        drawing = false;
        moving = false;
        isHit = false;

        setChanged();
        notifyObservers();
    }


    public void mouseDragged(int mx, int my) {
        // start drawing
        if (drawingStroke == null){
            Stroke stroke = new Stroke();
            drawing = true;

            strokes.add(stroke);
            drawingStroke = stroke;
        }

        if (drawing) {
            // drawing
            drawingStroke.addPoint(mx, my);
        } else {
            // not drawing && dragging => moving shape
            moving = true;
            drawingStroke.translatePoint(mx - cachedX, my - cachedY);
        }
        cachedX = mx;
        cachedY = my;

        setChanged();
        notifyObservers();
    }

    public ArrayList<Stroke> getStrokes() {
        return strokes;
    }

    public void duplicate() {
        Stroke dup = new Stroke();
        strokes.add(dup);
        int [] x_points = currentStroke.getX_points();
        int [] y_points = currentStroke.getY_points();
        for (int i = 0; i < currentStroke.getN_points(); i++) {
            dup.addPoint(new Point(x_points[i], y_points[i]));
        }
        dup.translatePoint(10, 10);
        dup.setScale(currentStroke.getScale());
        dup.setRotate(currentStroke.getRotate());
        dup.cachePointsArray();

        setChanged();
        notifyObservers();
    }
}
