# ANET exercises
## How to compile and run
For example, cd into TME0_HelloWorld/broadcast:exercise
java -cp ../../lib/jbotsim-standalone-1.2.0.jar:../ broadcast_exercise.Main
The jbotsim jar has to be passed with the cp argument, also the folder that contains the target file (in this case broadcast_exercise) has to be appended to the class path, otherwise the tool will look for the broadcast_exercise folder inside the broadcast_exercise folder!

To compile run:

javac -cp ../../lib/jbotsim-standalone-1.2.0.jar:../ MovingNode.java
