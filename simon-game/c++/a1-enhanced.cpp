/*
 *  - Enhanced note -
 *
 * 1. when mouse hover over the button, button will have a special animation, with changing color
 * 2. when pressed correct button, button will change to green.
 * 3. when pressed wrong button, button will flashing with red color.
 *
 */

#define ENHANCED
#include <cstdlib>
#include <iostream>
#include <X11/Xlib.h>

#include "eventloop.h"

int main ( int argc, char* argv[] ) {
    XInfo xinfo;
    initX(argc, argv, xinfo);

	// get the number of buttons from args
	// (default to 4 if no args)
	int n = 4;
    if (argc > 1) {
        n = atoi(argv[1]);
    }
    n = max(1, min(n, 9));
    Controller c(n, xinfo);

    eventloop(xinfo, c);
}
