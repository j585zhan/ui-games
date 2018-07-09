#include <X11/Xlib.h>
#include <math.h>

#include "common.h"

using namespace std;

#define PI 3.1415926

class Cycle : public Displayable {
	const int diameter = 100;
	int id;
	int x;
	int y;
    int total;
    int white = 100; /* diameter of white cycle during animation */
    bool hover = false;
    bool press = false;
    bool demoing = false;
    bool animation = false;
    bool correct = false;

    int offset() {
        return sin((now() / 3000 + id * 90) * PI / 180) * 10;
    }

public:
    Cycle(int id, int total, int width, int height) {
		this->id = id + 1;
        this->total = total;
        recenter(width, height);
    }

	void paint(XInfo& xinfo) {

        int y = this->y;
        if (!animation) {
            y += offset();
        }

        if (press || demoing) {
            if (white >= 0) {
#ifdef ENHANCED
                if (press && correct) {
                    XSetForeground(xinfo.display, xinfo.gc, 0x88FF88);
                } else if (press && !correct){
                    if (white / 10 % 2 == 0) {
                        XSetForeground(xinfo.display, xinfo.gc, 0xFF8888);
                    } else {
                        XSetForeground(xinfo.display, xinfo.gc, 0xFFFFFF);
                    }
                }
#endif
                XFillArc(xinfo.display, xinfo.window, xinfo.gc,
                         x, y, diameter, diameter, 0, 64 * 360);

                XSetForeground(xinfo.display, xinfo.gc, WhitePixel(xinfo.display, xinfo.screen));

                XDrawArc(xinfo.display, xinfo.window, xinfo.gc,
                         x + (diameter - white) / 2, y + (diameter - white) / 2, white, white, 0, 64 * 360);

                XSetForeground(xinfo.display, xinfo.gc, BlackPixel(xinfo.display, xinfo.screen));
            } else {
                XDrawArc(xinfo.display, xinfo.window, xinfo.gc,
                         x, y, diameter, diameter, 0, 64 * 360);
            }

            white-= 2;
            if (press && white == 0) {
                press = false;
                white = 100;
            }
            if (demoing && white == -50) {
                demoing = false;
                white = 100;
            }

        } else if (hover) {
#ifdef ENHANCED
            XSetForeground(xinfo.display, xinfo.gc, (now()/10000)%0xFFFFFF);
            XFillArc(xinfo.display, xinfo.window, xinfo.gc,
                     x - 4, y - 4, diameter + 8, diameter + 8, 64 * (now() / 3000), 64 * now() / 3000);
#else
            XFillArc(xinfo.display, xinfo.window, xinfo.gc,
                     x - 4, y - 4, diameter + 8, diameter + 8, 0, 64 * 360);
#endif

            XSetForeground(xinfo.display, xinfo.gc, WhitePixel(xinfo.display, xinfo.screen));

            XFillArc(xinfo.display, xinfo.window, xinfo.gc,
                     x, y, diameter, diameter, 0, 64 * 360);

            XSetForeground(xinfo.display, xinfo.gc, BlackPixel(xinfo.display, xinfo.screen));
        } else {
            XDrawArc(xinfo.display, xinfo.window, xinfo.gc,
                     x, y, diameter, diameter, 0, 64 * 360);
        }

        if (!press && !demoing || white < 0) {
            XDrawString(xinfo.display, xinfo.window, xinfo.gc, x + 45, y + 55,
                        IntToStr(id).c_str(), IntToStr(id).length());
        }
	}

    void recenter(int width, int height) {
        int gap = (width - total * 100) / (total + 1);
        this->x = (diameter + gap) * (id - 1) + gap;
        this->y = 100 /* diameter */ + (height - 100 /* draw two string */ - 100 /* diameter */ ) / 2;
    }

    void checkHover(int x, int y) {
        hover = isInCycle(x, y);
    }

    bool checkPress(int x, int y) {
        press = isInCycle(x, y);
        return press;
    }

    void demo () {
        demoing = true;
    }

    bool isInCycle(int x, int y) {
        int center_x = this->x + diameter / 2;
        int center_y = this->y + diameter / 2;
        return (x - center_x) * (x - center_x) + (y - center_y) * (y - center_y) <= diameter / 2 * diameter / 2;
    }

    void setAnimation(bool isAnimation) {
        animation = isAnimation;
    }

    bool isAnimation() {
        return press || demoing;
    }

    bool setCorrect(bool correct) {
        this->correct = correct;
    }
};

