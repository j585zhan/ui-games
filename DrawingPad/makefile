# super simple makefile
# call it using 'make NAME=name_of_code_file_without_extension'
# (assumes a .java extension)
NAME = "a2-basic"

all:
	# (a bit of a hack to compile everything each time ...)
	@echo "Compiling..."
	javac $(NAME).java CanvasView.java Model.java StatusbarView.java Stroke.java ToolbarView.java

run: all
	@echo "Running..."
	java $(NAME)

clean:
	rm -rf *.class
