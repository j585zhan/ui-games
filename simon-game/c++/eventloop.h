#include <cstdlib>
#include <iostream>
#include <X11/Xlib.h>
#include <unistd.h>
#include <list>
#include <vector>
#include <X11/Xutil.h>

#include "controller.h"
using namespace std;

// The loop responding to events from the user.
void eventloop(XInfo& xinfo, Controller controller) {
    XEvent event;

    while ( true ) {

        if (XPending(xinfo.display) > 0) {
            XNextEvent( xinfo.display, &event );

            switch ( event.type ) {

                    // add item where mouse clicked
                case ButtonPress:
                    controller.mousePress(event.xbutton.x, event.xbutton.y);
                    break;

                    // add item where mouse moved
                case MotionNotify:
                    controller.mouseHover(event.xbutton.x, event.xbutton.y);
                    break;

                case ConfigureNotify:
                    controller.recenter(event.xconfigure.width, event.xconfigure.height);
                    break;

                case KeyPress:
                    if (controller.keyPress(event, xinfo)) {
                        XCloseDisplay(xinfo.display);
                        return;
                    }
                    break;
            }
        }

        usleep(1000000/FPS);
        controller.animation();
        repaint(xinfo);
    }
}

//  Create the window;  initialize X.
void initX(int argc, char* argv[], XInfo& xinfo) {

    xinfo.display = XOpenDisplay( "" );
    if ( !xinfo.display ) {
        error( "Can't open display." );
    }

    int screen = DefaultScreen( xinfo.display );
    xinfo.screen = screen;
    unsigned long background = WhitePixel( xinfo.display, screen );
    unsigned long foreground = BlackPixel( xinfo.display, screen );


    XSizeHints hints;
    hints.x = 100;
    hints.y = 100;
    hints.width = xinfo.width;
    hints.height = xinfo.height;
    hints.flags = PPosition | PSize;
    xinfo.window = XCreateSimpleWindow( xinfo.display, DefaultRootWindow( xinfo.display ),
                                        hints.x, hints.y, hints.width, hints.height,
                                        Border, foreground, background );
    XSetStandardProperties( xinfo.display, xinfo.window, "Simon", "Simon", None,
                            argv, argc, &hints );


    xinfo.gc = XCreateGC (xinfo.display, xinfo.window, 0, 0 );
    XSetBackground( xinfo.display, xinfo.gc, background );
    XSetForeground( xinfo.display, xinfo.gc, foreground );


    XFontStruct * font;
    font = XLoadQueryFont (xinfo.display, "12x24");
    XSetFont (xinfo.display, xinfo.gc, font->fid);

    // Tell the window manager what input events you want.
    XSelectInput( xinfo.display, xinfo.window,
                  ButtonPressMask | KeyPressMask |
                  ButtonMotionMask | StructureNotifyMask | PointerMotionMask);

    XMapRaised( xinfo.display, xinfo.window );
}