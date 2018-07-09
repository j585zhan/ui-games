#include "cycle.h"
#include "simon.h"
#include <vector>

using namespace std;

class Controller{

public:
    string text;
	vector<Cycle *> cycles;
    Simon *simon;
    int score = 0;

	public:
    Controller(int total, XInfo &info) {
        simon = new Simon(total, false);

        for (int i = 0; i < total; i++) {
            cycles.push_back(new Cycle (i, total, info.width, info.height));
        }
    }

    bool isAnimation() {
        bool isAnimation = false;
        for (auto c : cycles) {
            isAnimation = isAnimation || c->isAnimation();
        }
        return isAnimation;
    }

	void animation() {
        for (auto c : cycles) {
            c->setAnimation(isAnimation() || simon->getState() == Simon::HUMAN);
        }

        if (!isAnimation()) {
            switch (simon->getState()) {

                // will only be in this state right after Simon object is constructed
                case Simon::START:
                    this->text = "Press SPACE to play";
                    break;
                    // they won last round
                    // score is increased by 1, sequence length is increased by 1
                case Simon::WIN:
                    this->text = "You won! Press SPACE to continue.";
                    break;
                    // they lost last round
                    // score is reset to 0, sequence length is reset to 1
                case Simon::LOSE:
                    this->text = "You lose. Press SPACE to play again.";
                    break;
                default:
                    // should never be any other state at this point ...
                    break;
            }
            score = simon->getScore();
        }

        if (!isAnimation()) {
            if (simon->getState() == Simon::HUMAN && !isAnimation()) {
                this->text = "Now it's your turn";
            }
            if (simon->getState() == Simon::COMPUTER) {
                cycles.at(simon->nextButton())->demo();
            }
        }

        for (Cycle* c : cycles) {
            dList.push_back(std::move(c));
        }

        dList.push_back(new Text(30, 50, IntToStr(score)));
        dList.push_back(new Text(30, 90, text));
	}

    int keyPress(XEvent event, XInfo &info) {
        const int BufferSize = 10;
        KeySym key;
        char text[BufferSize];

        int i = XLookupString(
                (XKeyEvent *) &event, text, BufferSize, &key, 0);

        bool waitSpace = simon->getState() == Simon::START ||
                simon->getState() == Simon::WIN ||
                simon->getState() == Simon::LOSE;

        if ((i == 1) && text[0] == ' ' && waitSpace && !isAnimation()) {
            this->text = "Watch what I do ...";
            simon->newRound();
        }

        return i == 1 && text[0] == 'q';

    }

    void recenter(int width, int height) {
        for (Cycle* c : cycles) {
            c->recenter(width, height);
        }
    }

    void mouseHover(int x, int y) {
        for (Cycle* c : cycles) {
            c->checkHover(x, y);
        }
    }


    void mousePress(int x, int y) {
        if (simon->getState() == Simon::HUMAN && !isAnimation()) {
            int counter = 0;
            for (Cycle *c : cycles) {
                counter ++;
                if (c->checkPress(x, y)) break;
            }
            bool correct = simon->verifyButton(counter - 1);
            cycles.at(counter - 1)->setCorrect(correct);
        }
    }

};

