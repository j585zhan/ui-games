import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javax.swing.undo.*;
import javax.vecmath.Point2d;

public class GameModel extends Observable {

    public GameModel(int fps, int width, int height, int peaks) {

        // undo manager init
        undoManager = new UndoManager();

        ship = new Ship(60, width/2, 50);

        landPad = new Rectangle2D.Double(330, 100, 40, 10);

        worldBounds = new Rectangle2D.Double(0, 0, width, height);

        // init terrain
        terrain = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            terrain.add(new Point2D.Double(i*700/19,random.nextInt(70) + 115));
        }

        // anonymous class to monitor ship updates
        ship.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (!ship.isPaused()) {
                    checkShipStatus();
                }
                setChangedAndNotify();
            }
        });
    }

    public void clickHandler(int x, int y) {
        if (inLandPad(x, y)) {
            padSelected = true;
        } else {
            for (int i = 0; i < terrain.size(); i++) {
                Point2D p = terrain.get(i);
                double xCenter = p.getX();
                double yCenter = p.getY();
                double xDist = x - xCenter;
                double yDist = y - yCenter;
                if (xDist * xDist + yDist * yDist <= 15 * 15) {
                    terrainSelected = true;
                    currentTerrain = i;
                    cachedTerrainY = terrain.get(i).getY();
                }
            }
        }
        cachedLandPadXPos = landPad.getX();
        cachedLandPadYPos = landPad.getY();
        cacheMousePos(x, y);
    }

    public void releaseHandler() {
        if ((padSelected || terrainSelected) && edited) {
            saveState(padSelected, terrainSelected);
        }
        padSelected = false;
        terrainSelected = false;
        edited = false;
        cachedTerrainY = terrain.get(currentTerrain).getY();
        cachedLandPadXPos = landPad.getX();
        cachedLandPadYPos = landPad.getY();
    }

    public void dragHandler(int x, int y) {
        edited = true;

        // manage mouse outside world
        y = y > 200 ? 200 : y < 0 ? 0 : y;
        x = x > 700 ? 700 : x < 0 ? 0 : x;

        if (padSelected) { /* moving pad */
            transLandPadPos(x, y);
        } else if (terrainSelected) {
            transTerrainPos(y);
        }
        cacheMousePos(x, y);
        setChangedAndNotify();
    }

    // Undo Handler
    // - - - - - - - - - - -
    private UndoManager undoManager;
    private boolean edited = false;
    private Double cachedLandPadXPos;
    private Double cachedLandPadYPos;
    private double cachedTerrainY;

    public void saveState(boolean padUpdated, boolean terrainUpdated) {
        UndoableEdit undoableEdit = new AbstractUndoableEdit() {

            // capture variables for closure
            final double newY = terrain.get(currentTerrain).getY();
            final Double newPadXPos = landPad.getX();
            final Double newPadYPos = landPad.getY();
            final int changedTerrain = currentTerrain;
            final double oldY = cachedTerrainY;
            final Double oldPadXPos = cachedLandPadXPos;
            final Double oldPadYPos = cachedLandPadYPos;


            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                if (terrainUpdated) {
                    terrain
                        .get(changedTerrain)
                        .setLocation(terrain.get(changedTerrain).getX(), newY);
                }
                if (padUpdated) {
                    landPad.setRect(newPadXPos, newPadYPos, landPad.getWidth(), landPad.getHeight());
                }
                setChangedAndNotify();
            }

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                if (terrainUpdated) {
                    terrain
                        .get(changedTerrain)
                        .setLocation(terrain.get(changedTerrain).getX(), oldY);
                }
                if (padUpdated) {
                    landPad.setRect(oldPadXPos, oldPadYPos, landPad.getWidth(), landPad.getHeight());
                }
                setChangedAndNotify();
            }
        };

        undoManager.addEdit(undoableEdit);
        setChangedAndNotify();
    }

    public void undo() {
        if (canUndo()) undoManager.undo();
    }

    public void redo() {
        if (canRedo()) undoManager.redo();
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    // LandPad
    // - - - - - - - - - - -

    public Rectangle2D.Double landPad;

    private int cachedMouseX, cachedMouseY;
    private boolean padSelected = false;
    public Rectangle2D getLandPad() {
        return landPad;
    }

    public void setLandPadPos(int x, int y) {
        landPad.setRect(x - 20, y - 5, 40, 10);
        setChangedAndNotify();
    }

    public boolean inLandPad(int x, int y) {
        return x >= landPad.getX() && x <= landPad.getX() + landPad.getWidth()
            && y >= landPad.getY() && y <= landPad.getY() + landPad.getHeight();
    }

    public void transLandPadPos(int x, int y) {
        double newX = landPad.x + x - cachedMouseX;
        double newY = landPad.y + y - cachedMouseY;
        newX = newX > 660 ? 660 : newX < 0 ? 0 : newX;
        newY = newY > 190 ? 190 : newY < 0 ? 0 : newY;
        landPad.setRect(newX, newY, 40, 10);
    }

    public void cacheMousePos(int x, int y) {
        cachedMouseX = x;
        cachedMouseY = y;
    }

    // Terrain
    // - - - - - - - - - - -
    ArrayList<Point2D>terrain;

    private boolean terrainSelected = false;
    private int currentTerrain = 0;

    private void transTerrainPos(int y) {
        Point2D.Double curTerrain = (Point2D.Double) terrain.get(currentTerrain);
        double newY = curTerrain.getY() + y - cachedMouseY;
        newY = newY > 200 ? 200 : newY < 0 ? 0 : newY;
        terrain.get(currentTerrain).setLocation(new Point2D.Double(curTerrain.getX(), newY));
        setChangedAndNotify();
    }

    public int [] getTerrainXPoints() {
        int size = terrain.size();
        int [] xPoints = new int[size + 2];
        for (int i = 0; i < size; i++) {
            xPoints[i] = (int) terrain.get(i).getX();
        }
        xPoints[size] = 700;
        xPoints[size + 1] = 0;
        return xPoints;
    }

    public int [] getTerrainYPoints() {
        int size = terrain.size();
        int [] yPoints = new int[size + 2];
        for (int i = 0; i < size; i++) {
            yPoints[i] = (int) terrain.get(i).getY();
        }
        yPoints[size] = 200;
        yPoints[size + 1] = 200;
        return yPoints;
    }

    public Polygon getTerrainPolygon() {
        return new Polygon(getTerrainXPoints(), getTerrainYPoints(), getNPoints());
    }

    public int getNPoints() {
        return terrain.size()+2;
    }

    // World
    // - - - - - - - - - - -
    public final Rectangle2D getWorldBounds() {
        return worldBounds;
    }

    Rectangle2D.Double worldBounds;


    // Ship
    // - - - - - - - - - - -

    public Ship ship;
    public boolean paused = true;
    public boolean crashed = false;
    public boolean landed = false;

    public Point2d getShipPos() {
        return ship.getPosition();
    }

    double getShipFuel() {
        return ship.getFuel();
    }

    double getShipSpeed() {
        return ship.getSpeed();
    }

    double getShipSafeLandingSpeed() {
        return ship.getSafeLandingSpeed();
    }

    boolean isShipPaused() {
        return paused;
    }

    boolean isShipCrashed() {
        if (ship.isPaused()) return crashed;
        crashed = !worldBounds.contains(ship.getShape())
            || getTerrainPolygon().intersects(ship.getShape())
            || (landPad.intersects(ship.getShape()) && !isShipSpeedSafe());
        return crashed;
    }

    boolean isShipSpeedSafe() {
        return getShipSpeed() <= getShipSafeLandingSpeed();
    }

    boolean isShipLanded() {
        if (ship.isPaused()) return landed;
        landed = landPad.intersects(ship.getShape()) && isShipSpeedSafe();
        return landed;
    }

    void toggleShip() {
        paused = !paused;
        ship.setPaused(!ship.isPaused());
    }

    void restartShip() {
        paused = false;
        ship.reset(new Point2d(350, 50));
        ship.setPaused(false);
    }

    void thrustUp() {
        ship.thrustUp();
    }

    void thrustDown() {
        ship.thrustDown();
    }

    void thrustLeft() {
        ship.thrustLeft();
    }

    void thrustRight() {
        ship.thrustRight();
    }

    void checkShipStatus() {
        if (isShipCrashed() || isShipLanded()) {
            ship.stop();
        }
    }

    // Observerable
    // - - - - - - - - - - -

    // helper function to do both
    void setChangedAndNotify() {
        setChanged();
        notifyObservers();
    }
}


