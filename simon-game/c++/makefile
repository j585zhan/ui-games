BASIC = "a1-basic"
ENHANCED = "a1-enhanced"

MAC_OPT = -I/opt/X11/include

all: basic enhanced

basic:
	@echo "Compiling basic"
	g++ -o $(BASIC) $(BASIC).cpp -L/opt/X11/lib -lX11 -lstdc++ $(MAC_OPT) -std=c++14

enhanced:
	@echo "Compiling enhanced"
	g++ -o $(ENHANCED) $(ENHANCED).cpp -L/opt/X11/lib -lX11 -lstdc++ $(MAC_OPT) -std=c++14

run: all
	@echo "Running..."
	./$(BASIC)
	./$(ENHANCED)

clean:
	-rm a1-basic
	-rm a1-enhanced
