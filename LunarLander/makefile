# super simple makefile
# call it using 'make NAME=name_of_code_file_without_extension'
# (assumes a .java extension)
NAME = A3Basic
# you may need to pass OS=win to run on windows
OS = 

# HACK: vecmath is included regardless if needed
all:
	@echo "Compiling..."
	javac -cp vecmath.jar $(NAME).java EditView.java GameModel.java MessageView.java PlayView.java Ship.java ToolBarView.java

run: all
# windows needs a semicolon
ifeq ($(OS),win)
		@echo "Running on windows ..."
		java -cp "vecmath.jar;." $(NAME)
# everyone else likes a colon
else
		@echo "Running ..."
		java -cp "vecmath.jar:." $(NAME)
endif

clean:
	rm -rf *.class
