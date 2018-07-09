package com.example.jim.simon;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by jim on 25/11/17.
 */

enum Difficulty {
    EASY, NORMAL, DIFFICULT
}

public class Model extends Observable {

    private int screenWidth, screenHeight;
    private int buttonNumber = 4;
    private Difficulty difficulty = Difficulty.NORMAL;
    private Simon simon;
    private ArrayList<Cycle>cycles;

    private static final Model instance = new Model();
    private GameActivity gameActivity;

    static Model getInstance() {
        return instance;
    }

    /* set the screen size and cycles */
    public void init(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        setGame();
    }

    public void setGame() {
        /* new simon game */
        simon = new Simon(buttonNumber);

        /* construct array of cycles */
        cycles = new ArrayList<>();
        for (int i = 0; i < buttonNumber; i++) {
            cycles.add(new Cycle(i, buttonNumber, screenWidth, screenHeight));
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        setGame();
    }

    public int getButtonNumber() {
        return buttonNumber;
    }

    public void setButtonNumber(int buttonNumber) {
        this.buttonNumber = buttonNumber;
        setGame();
    }

    public void initObservers() {
        setChanged();
        notifyObservers();
    }

    public ArrayList<Cycle> getCycles() {
        return cycles;
    }

    public int getDifficultyAsNum() {
        switch (difficulty) {
            case EASY:
                return 0;
            case NORMAL:
                return 1;
            case DIFFICULT:
                return 2;
            default:
                return -1; /* indicate error (but should not happen) */
        }
    }

    public String getText() {
        switch (simon.state) {
            case START:
                return "Touch anywhere to play";
            case WIN:
                return "You won! Touch to continue.";
            case LOSE:
                return "You lose. Touch to play again.";
            case COMPUTER:
                return "Watch what I do ...";
            case HUMAN:
                return "Now it's your turn";
            default:
                return "";
        }
    }

    public Simon getSimon() {
        return simon;
    }

    public void cancelAnimation() {
        for (Cycle c: cycles) {
            c.cancelAnimation();
        }
    }

    public void startAnimation () {
        for (Cycle c: cycles) {
            c.startAnimation();
        }
    }

    public int getPlayBackSpeed() {
        switch (difficulty) {
            case EASY:
                return 60;// frames to animate
            case NORMAL:
                return 30; // frames to animate
            case DIFFICULT:
                return 10; // frames to animate
            default:
                return -1; /* indicate error (but should not happen) */
        }
    }

    public void addGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    /* fields for view */
    boolean updateMsg = true;
    int nextButton = -1;
    int frameCounter = 0;

    public int getFrameCounter() {
        return frameCounter;
    }

    public void increaseFrameCounter() {
        frameCounter++;
        frameCounter%=getPlayBackSpeed(); /* setting computer demo speed */
    }

    public int getNextButton() {
        return nextButton;
    }

    public void resetNextButton() {
        this.nextButton = simon.nextButton();
    }

    public boolean isUpdateMsg() {
        return updateMsg;
    }

    public void setUpdateMsg(boolean updateMsg) {
        this.updateMsg = updateMsg;
    }

    public void onTouch(MotionEvent motionEvent) {
        if (simon.getState() == Simon.State.START
                || simon.getState() == Simon.State.LOSE
                || simon.getState() == Simon.State.WIN) {
            if (motionEvent.getY() > screenHeight - screenHeight / 5) {
                        /* return to welcome screen */
                gameActivity.finish();
            } else {
                        /* touch to start new game */
                simon.newRound();
                cancelAnimation();
                updateMsg = true;
                frameCounter = 1;
            }
        } else if (simon.getState() == Simon.State.HUMAN) {
                    /* hitting test */
            int mx = (int) motionEvent.getX();
            int my = (int) motionEvent.getY();
            int count = 0;
            boolean hit = false;
            for (Cycle c: cycles) {
                if (c.isInCycle(mx, my)) {
                    hit = true;
                    break;
                }
                count++;
            }
            if (hit) {
                simon.verifyButton(count);
                nextButton = count;
                updateMsg = true;
                frameCounter = 1;
            }
        }
    }
}
