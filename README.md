# Number Theory Playground
This project has been worked on solo and currently consists of 2 Java programs, one for a command line interface and the other for a 
graphical user interface. In each version, the user can go to different sections. Each section can teach the user about and do calculations 
for number theory concepts. I am currently working on a website version of this project.

## All Sections

Shown below is info about all sections featured in the application, including concept info, what can be calculated and displayed, and the range of valid input integers. The concept info in this document is mostly the same as the concept info displayed in the application, though there are some mathematical expressions and symbols featured in this document that can be written with GitHub Markdown, such as $\sqrt{29}$ and $\Phi$. Here's a [*GitHub Docs* article on writing mathematical expressions](https://docs.github.com/en/get-started/writing-on-github/working-with-advanced-formatting/writing-mathematical-expressions). There are also usages of *italics and emphasis* in this document.

### Prime Numbers

#### Info

A *prime number* is an integer $\geq 2$ whose only integer factors are 1 and itself. There are an infinite amount of them. A *composite number* is an integer $\geq 2$ that has an integer factor other than 1 and itself. Prime numbers are used in 7 of the 10 sections in this application.

An integer can be determined to be prime if it is not divisible by any prime numbers $\leq$ the square root of that integer. For example, let's determine if 29 and 33 are prime. $5^2 = 25$ and $6^2 = 36$ so $5 < \sqrt{29} < \sqrt{33} < 6$. We check if the numbers are divisible by 2, 3, or 5, which are the prime numbers $\leq$ 5. 29 isn't divisible by any of those and 33 is divisible by 3 so 29 is prime and 33 isn't.

Fun fact: with the exception of 2 and 3, all prime numbers are either 1 above or 1 below a multiple of 6. To show why this is the case, let's have a variable $i$ and let it represent any integer $\geq$ 6 that is a multiple of 6. We know that $i$ is divisible by 2 and 3 so $i + 2$ and $i + 4$ are divisible by 2 and $i + 3$ is divisible by 3 but we don't have any guarantees about what $i + 1$ and $i + 5$ are divisible by. Therefore, that is where prime numbers can be.

#### Calculation

Find the first 30 prime numbers that are $\geq$ an input number.

#### Input Range: 0 - 1,000,000,000 (1 billion)


### Twin Prime Numbers

#### Info

*Twin primes* are pairs of prime numbers that differ by 2. It is conjectured that there are infinitely many of them. A *conjecture* is a statement that is believed to be true but has not been proven to be. Fun fact: all prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so this means that all pairs of twin primes besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and another number that is 1 above that same multiple of 6.

#### Calculation

Find the first 20 pairs of twin primes where the lowest number in the pair is $\geq$ an input number. For example, if the input number is 3, then the pair 3 and 5 will be the first one found since the lowest number in that pair is 3. If the input number is 4, then the pair 5 and 7 will be the first one found.

#### Input Range: 0 - 1,000,000,000 (1 billion)


### Prime Factorization

#### Info

The Fundamental Theorem of Arithmetic says that every integer $> 1$ can be expressed as the product of prime numbers. The *prime factorization* (PF) of an integer is an expression of the prime numbers whose product is that integer. For example, the PF of 5 is just $5$, the PF of 25 is $5^2$, and the PF of 12,250 is $2 \times 5^3 \times 7^2$. There are some interesting applications for this. Visit the GCD and LCM or the Divisibility sections for some applications.

#### Calculation

Find the PF of an input number faster than you can say "prime factorization". :slightly_smiling_face:

#### Input Range: 2 - 10,000


### Divisibility &nbsp; :heavy_division_sign:

#### Info

Say we have 2 integers that we'll represent with the variables $a$ and $b$. If we divide $a$ by $b$ and get no remainder, then $a$ is said to be *divisible* by $b$ and $b$ is said to be a *factor* or *divisor* of $a$. If you want to find all the factors of an integer, you could manually find all of them but there are some other ways to find them.

Some special tricks can be used to find some of the factors of a number. Let's have a variable $i$ and let it represent an integer. If the sum of the digits of $i$ is divisible by 3, then $i$ is divisible by 3. If the sum of the digits of $i$ is divisible by 9, then $i$ is divisible by 9. If $i$ is even and divisible by 3, then it is also divisible by 6. If the last 2 digits of $i$ is divisible by 4, then $i$ is divisible by 4. If the last 3 digits of $i$ is divisible by 8, then $i$ is divisible by 8. If $i$ is divisible by both 3 and 4 then it is also divisible by 12.

Another way you can tell what factors a number has and how many factors it has is by looking at its prime factorization (PF). Let's have a variable $i$ and let it represent an integer $> 1$. You can find how many factors $i$ has by looking at $i$'s PF, taking all the powers of the prime factors, adding 1 to each, and then multiplying all these together. For example, the PF of 294 is $2 \times 3 \times 7^2$. The powers are 1, 1, and 2; so there are $2 \times 2 \times 3 = 12$ factors. However, that count includes 1 and the number that the PF is for (294 in this case). If you want to exclude those, then subtract 2. That would give us 10 factors. You can find the factors of $i$ by finding all the PFs within $i$'s PF, or the *sub-factorizations*, as I like to call them. For $2 \times 3 \times 7^2$, some sub-factorizations include $2$, $2 \times 7$, and $3 \times 7^2$. This means that 2, 14, and 147 are factors of 294.

#### Calculations

1. Use the special tricks to see if we can find some factors of an input number and build a paragraph that says info from this.
2. Find the PF of the input number. If we can determine from this PF that the input number is composite, then manually find the factors and their PFs and show that they are sub-factorizations of the PF of the input number.

#### Input Range: 2 - 10,000


### Greatest Common Divisor (GCD) and Least Common Multiple (LCM) &nbsp; :heavy_division_sign: &nbsp; :heavy_multiplication_x:

#### Info

One way to find the GCD and LCM of 2 numbers is to look at the prime factorizations (PFs) of those numbers. If those numbers do not have any common prime factors, then the GCD is 1. If they do have common prime factors, then the PF of the GCD consists of all the common prime factors and the power of each factor is the minimum power of that factor in the 2 PFs. The PF of the LCM consists of all factors that are in either of the PFs of the 2 numbers. If a factor is in both PFs then the power of that factor in the LCM PF is the maximum of the powers of that factor in the 2 PFs. If a factor is unique to one of the PFs then that factor and its power are in the LCM PF.

For example, let's find the GCD and LCM of 6 and 35 using their PFs. 6 has a PF of $2 \times 3$ and 35 has a PF of $5 \times 7$. There are no common factors so the GCD is 1. The LCM has a PF of $2 \times 3 \times 5 \times 7$ and this equals 210. Let's find the GCD and LCM of 54 and 99. 54 has a PF of $2 \times 3^3$ and 99 has a PF of $3^2 \times 11$. 3 is the only common factor and the minimum power of that factor is 2 so the GCD has a PF of $3^2$ and this equals 9. The maximum power of that factor is 3 so the LCM has a PF that consists of $3^3$. The PF of the LCM is $2 \times 3^3 \times 11$ and this equals 594.

The Euclidean algorithm can be used to find the GCD of 2 numbers, usually faster than calculating the prime factorizations. This algorithm was named after the ancient Greek mathematician Euclid. For this algorithm, first take 2 numbers. If the bigger number is divisible by the smaller number, then the smaller number is the GCD. Otherwise, the GCD of the 2 numbers is the same as the GCD of the smaller number and the remainder when the bigger number is divided by the smaller number. Repeat.

#### Calculations

1. Perform the Euclidean algorithm on 2 input numbers and display a table with info about all iterations. Each iteration has a max number, min number, and remainder when the max is divided by the min.
2. Find the PFs of the input numbers and use these to find the PFs of the GCD and LCM. Display a table with all 4 numbers and their PFs.

#### Input Range: 2 - 10,000


### Goldbach Conjecture

#### Info

The Goldbach Conjecture says that every even number $\geq 4$ can be expressed as the sum of 2 prime numbers. A *conjecture* is a statement that is believed to be true but has not been proven to be true. The Goldbach Conjecture has been verified to be true for all even numbers $\geq 4$ and $\leq$ a very high number. I don't know this number off the top of my head but it's way, way bigger than the max number you're allowed to use for input.

#### Calculation

Find the pairs of prime numbers that sum to an even input number.

#### Input Range: 4 - 100,000


### Pythagorean Triples

#### Info

The Pythagorean Theorem says that for the side lengths of a right triangle, the sum of the squares of the 2 short sides equals the square of the long side (hypotenuse) or $a^2 + b^2 = c^2$. This theorem was named after the ancient Greek mathematician Pythagoras. There are an infinite amount of trios of integers that $a$, $b$, and $c$ can be. These trios are called *Pythagorean Triples*. For example, $3^2 (9) + 4^2 (16) = 5^2 (25)$ and $11^2 (121) + 60^2 (3,600) = 61^2 (3,721)$.

#### Calculation

Find the first 10 Pythagorean Triples where the lowest number in the triple is $\geq$ an input number. For example, if the input number is 3, then the triple 3, 4, and 5 will be the first one found since the lowest number in that triple is 3. If the input number is 4, then the triple 5, 12, and 13 will be the first one found. The triples that get found will be displayed similarly to how the examples at the end of the "Info" section above are displayed.

#### Input Range: 0 - 1,000


### Two Square Theorem

#### Info

The Two Square Theorem says that every prime number that is 1 above a multiple of 4 can be expressed as the sum of 2 squares.

#### Calculations

Find the first prime number $\geq$ an input number that is 1 above a multiple of 4, as well as the squares that sum to that number.

#### Input Range: 0 - 1,000,000,000 (1 billion)


### Fibonacci-like Sequences

#### Info

I consider a number sequence to be "Fibonacci-like" if it starts with 2 numbers and has every following number be the sum of the 2 previous numbers. The Fibonacci Sequence does this and has 1 and 1 as its first 2 numbers. Fibonacci was a mathematician from the 1100s to 1200s from modern-day Italy. Another Fibonacci-like sequence are the Lucas Numbers, which has 2 and 1 as its first 2 numbers. This sequence was named after 1800s French mathematician Francois Edouard Anatole Lucas. Lucas is pronounced like *Lucaw*.

The *Golden Ratio* is an irrational number symbolized by the Greek letter $\Phi$ (Phi). $\Phi = \frac{1 + \sqrt{5}}{2} \approx 1.618$. As we advance further and further into a Fibonacci-like sequence, the ratio between a number and the number before it gets closer and closer to $\Phi$. For example, the first 8 numbers of the Fibonacci Sequence are 1, 1, 2, 3, 5, 8, 13, and 21. $\frac{2}{1} = 2$. $\frac{8}{5} = 1.6$. $\frac{21}{13} \approx 1.615$.

#### Calculations

Find the first 20 numbers of the Fibonacci-like sequence that starts with 2 input numbers, as well as the ratio between the last number and the second to last number.

#### Input Range: 1 - 1,000


### Ancient Egyptian Multiplication

#### Info

The ancient Egyptians had an interesting algorithm for multiplication. My way of explaining the algorithm goes like this:
1. Let variable $a$ represent one of the numbers and variable $b$ represent the other number.
2. Find all powers of 2 that are $\leq a$. This could be done without modern multiplication by starting with 1, the 1st power of 2 or $2^0$, and finding the next power by adding the previous power to itself. This process will look like: $1 + 1 = 2 (2^1), 2 + 2 = 4 (2^2), 4 + 4 = 8 (2^3)$, and so on until we find a power that's $> a$, which we won't use.
3. Find the products of $b$ and these powers of 2. Like with the powers of 2, this could be done by starting with $b$ and finding the next product by adding the previous product to itself. If we let $b$ be 5, this process will look like: $5 + 5 = 10 (5 \times 2)$, $10 + 10 = 20 (5 \times 4)$, $20 + 20 = 40 (5 \times 8)$, and so on.
4. Find the powers of 2 that add up to $a$.
5. Add up the products of $b$ and these powers.

This gives us the product of the 2 numbers.

Let's find the product of 5 and 12. Let's first use 5 for the number represented by $a$ in the algorithm above and 12 for $b$. The powers of 2 $\leq$ 5 are 1, 2, and 4. The products of 12 and these powers are 12, 24, and 48. The powers of 2 that add up to 5 are 1 and 4. The products of 12 and these powers are 12 and 48. $12 + 48 = (12 \times 1) + (12 \times 4) = 12 \times (1 + 4) = 60$. Now let's use 12 for $a$ and 5 for $b$. The powers of 2 $\leq$ 12 are 1, 2, 4, and 8. The products of 5 and these powers are 5, 10, 20, and 40. The powers of 2 that add up to 12 are 4 and 8. The products of 5 and these powers are 20 and 40. $20 + 40 = (5 \times 4) + (5 \times 8) = 5 \times (4 + 8) = 60$.

#### Calculations

Given 2 input numbers:
1. Find the powers of 2 $\leq$ the first input number and the corresponding multiples of the second input number. Display these in a table.
2. Find the powers of 2 that sum to the first input number and the corresponding multiples of the second input number. Display these in another table.

This process will be done faster than you can say "ancient Egyptian multiplication". :slightly_smiling_face:

#### Input Range: 2 - 10,000

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
