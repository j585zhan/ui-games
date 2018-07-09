package com.example.jim.simon;

import java.util.ArrayList;
import java.util.Random;

import static com.example.jim.simon.Simon.State.COMPUTER;
import static com.example.jim.simon.Simon.State.HUMAN;
import static com.example.jim.simon.Simon.State.LOSE;
import static com.example.jim.simon.Simon.State.START;
import static com.example.jim.simon.Simon.State.WIN;

/**
 * Created by jim on 25/11/17.
 */

/* from A1 */
public class Simon {

    // possible game states:
    // PAUSE - nothing happening
    // COMPUTER - computer is play a sequence of buttons
    // HUMAN - human is guessing the sequence of buttons
    // LOSE and WIN - game is over in one of thise states
    enum State { START, COMPUTER, HUMAN, LOSE, WIN };


    // the game state and score
    State state;
    int score;

    // length of sequence
    int length;
    // number of possible buttons
    int buttons;

    // the sequence of buttons and current button
    ArrayList<Integer> sequence;
    int index;

    boolean debug;

    void init(int _buttons){
        length = 1;
        buttons = _buttons;
        state = START;
        score = 0;
    }

    Simon(int _buttons) {
        sequence = new ArrayList<>();
        init(_buttons);
    }


    int getNumButtons() { return buttons; }

    int getScore() { return score; }

    State getState() { return state; }

    String getStateAsString() {

        switch (getState()) {

            case START:
                return "START";

            case COMPUTER:
                return "COMPUTER";

            case HUMAN:
                return "HUMAN";

            case LOSE:
                return "LOSE";

            case WIN:
                return "WIN";
            default:
                return "Unkown State";
        }
    }

    void newRound() {
        // reset if they lost last time
        if (state == LOSE) {
            length = 1;
            score = 0;
        }

        sequence.clear();

        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int b = random.nextInt(buttons);
            sequence.add(b);
        }

        index = 0;
        state = COMPUTER;

    }

    // call this to get next button to show when computer is playing
    int nextButton() {

        if (state != COMPUTER) {
            return -1;
        }

        // this is the next button to show in the sequence
        int button = sequence.get(index);

        // advance to next button
        index++;

        // if all the buttons were shown, give
        // the human a chance to guess the sequence
        if (index >= sequence.size()) {
            index = 0;
            state = HUMAN;
        }

        return button;
    }

    boolean verifyButton(int button) {

        if (state != HUMAN) {
            return false;
        }

        // did they press the right button?
        boolean correct = (button == sequence.get(index));

        // advance to next button
        index++;

        // pushed the wrong buttons
        if (!correct) {
            state = LOSE;

            // they got it right
        } else {
            // if last button, then the win the round
            if (index == sequence.size()) {
                state = WIN;
                // update the score and increase the difficulty
                score++;
                length++;
            }
        }
        return correct;
    }

}