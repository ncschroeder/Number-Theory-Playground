# Number Theory Playground
This project has been worked on solo and currently consists of 2 Java programs, one for a command line interface and the other for a 
graphical user interface. In each version, the user can go to different sections. Each section can teach the user about and do calculations 
for number theory concepts. I am currently working on a website version of this project.

## All the sections:
- One that teaches the user about prime numbers and allows for input of a valid number and the program outputs the first 30 prime numbers that appear after that number
- One that teaches the user about twin prime numbers and allows for input of a valid number and the program outputs the first 20 pairs of twin prime numbers that appear after that number
- One that teaches the user about prime factorizations and allows for input of a valid number and the program outputs that number's prime factorization
- One that teaches the user about divisibility tricks and allows for input of a valid number and the program outputs the divisibility info for that number
- One that teaches the user about greatest common factors (GCDs) and least common multiples (LCMs) and allows for input of 2 valid numbers and the program outputs the GCD and LCM info for those numbers. The GCD can be calculated using 2 methods and the LCM can be calculated using 1 method.
- One that teaches the user about the Goldbach conjecture and allows for input of a valid number and the program outputs the pairs of prime numbers that sum to that number
- One that teaches the user about Pythagorean triples and allows for input of a valid number and the program outputs the first 10 Pythagorean triples that appear after that number

## Directions for running Java versions from command line
1. Have a java development kit that can compile a Java program that has all the Java features I used. My jdk is version 11.0.8.
2. Download this repository
3. Open a terminal and navigate to the directory where the repository was downloaded and then into the repository folder and then the "Java Versions" folder
4. Run command "javac *.java"
5. Run command "java NTPGUI" for the GUI version or "java NTPCLI" for the command line version

## Why I decided to make this
During the summer of 2019, I took a number theory course on brilliant.org for fun and I was enlightened by some of the stuff I was
learning. Later that year, I was thinking of what kind of programming projects I could work on and some of the stuff
I learned from that number theory course came to mind. I realized I could make a program that makes calculations about some of the stuff I learned.
Along with the content of that number theory course, I also included additional number theory concepts that I was familiar with.
I learned these other concepts from sources such as my discrete math textbook and YouTube videos from channels such as
Numberphile, Mathologer, 3blue1brown, and Zach Star. 
I decided to use Java because it is a popular programming language that I had no familiarity with at the time. However, 
I found Java easy to learn since some of the syntax is similar to C++, which I had some familiarity with. 

## Thoughts on this project
I liked that I got to implement the number theory concepts that enlightened me. I'd say they weren't hard to implement though it did require some thinking. The hardest concept to implement was 
the part of the divisibility section where a number's prime factorization is used to find its factors. I had trouble making the graphical user interface with Java Swing, namely laying out all the content.
As I did more and more research, I became more familiar with Java Swing and eventually was able to do what I wanted. I'm almost done with a website version of this project. I had some trouble laying
out the content with HTML and CSS but came up with solutions by using CSS flexbox and grid. I originally had the calculation functions in the script that was attached to the HTML/pug document. I
felt that it would be more professional to make an API for these functions so that's what I did after I researched and thought about how to do that.