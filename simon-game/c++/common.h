#include <X11/Xlib.h>
#include <sstream>
#include <sys/time.h>

using namespace std;

const int FPS = 60;
const int Border = 5;

// Function to put out a message on error exits.
void error( string str ) {
    cerr << str << endl;
    exit(0);
}

struct XInfo {
    Display* display;
    Window window;
    GC gc;
    int width = 800;
    int height = 400;
    int screen;
};

string IntToStr(int id) {
    stringstream ss;
    ss << id;
    return ss.str();
}

// get microseconds
unsigned long now() {
    timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec * 1000000 + tv.tv_usec;
}

// An abstract class representing displayable things.
class Displayable {
public:
    virtual void paint(XInfo& xinfo) = 0;
};

list<Displayable*> dList;


// Function to repaint a display list
void repaint( XInfo& xinfo) {
    list<Displayable*>::const_iterator begin = dList.begin();
    list<Displayable*>::const_iterator end = dList.end();

    XClearWindow( xinfo.display, xinfo.window );
    while ( begin != end ) {
        Displayable* d = *begin;
        d->paint(xinfo);
        begin++;
    }

    while (dList.size() != 0) {
        dList.pop_back();
    }
    XFlush( xinfo.display );
}

// A text displayable
class Text : public Displayable {
public:
    virtual void paint(XInfo& xinfo) {
        XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
                          this->x, this->y, this->s.c_str(), this->s.length() );
    }

    Text(int x, int y, string s): x(x), y(y), s(s)  {}

private:
    int x;
    int y;
    string s; // string to show
};