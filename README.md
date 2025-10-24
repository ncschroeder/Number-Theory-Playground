# Number Theory Playground &nbsp;&nbsp; :heavy_plus_sign: &nbsp; :heavy_minus_sign: &nbsp; :heavy_multiplication_x: &nbsp; :heavy_division_sign:

*Number theory* is a branch of math and is the study of integers. Let's go over a few types of numbers. *Natural numbers* are 1, 2, 3, and so on. *Whole numbers* are the natural numbers along with 0. *Integers* are the whole numbers along with negative natural numbers; so ..., -2, -1, 0, 1, 2, ...

There are 3 versions of this application: Java command line interface (CLI), Java graphical user interface (GUI), and website. The Java versions have been refactored recently and the website version hasn't. The Java versions share a lot of code, namely the code for the calculations and text that gets displayed. The Java versions have some unit testing. The JUnit testing framework was used. The code for the tests is in the "Tests" folder located within the "Java Versions" folder. The Java GUI version was made with the Swing library. The files for the Java versions are copies of the files in an IntelliJ IDEA project of mine.
The *Number Theory Playground* is an application where a user can visit sections that show info and do calculations for number theory concepts. These calculations are done based on input numbers provided by the user. Some sections require 1 input number and some require 2. An example of a section is the prime numbers section. It has 17 sentences of info and can find the first 30 prime numbers that are ≥ an input number. Plenty of info about the sections can be found in the ["All Sections" section](#all-sections).

Almost all of the calculations involve only natural numbers. There are 2 exceptions to this:

1. The divisibility section does calculations that might involve negative integers or 0.
2. The Fibonacci-like sequences section does calculations where the result is a floating-point number.

The term "whole number" is often used in this app since I find it to be more self-explanatory than "natural number."


## App Overview

Here are some collapsible sections that show some info and screenshots for the different versions.

<details>
<summary>Command Line Interface Info</summary>

The `NTPCLI` class has the `main` method and some other code for running this version of the app. If you're wondering, I was using the IntelliJ IDEA terminal with the Darcula :vampire: theme when I took the screenshots below.

The app starts at a main menu:

![CLI main menu](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/main_menu.JPG)

A single input section:

![CLI single input section](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/single_input_section.JPG)

A double input section:

![CLI double input section](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/double_input_section.JPG)

Info about a section:

![CLI section info](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/section_info.JPG)

Some input and calculations:

![CLI input and calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/double_input_section_calculations.JPG)

Invalid input:

![CLI invalid input](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/invalid_input.JPG)

#### Input Remarks

- Leading and trailing whitespace is ignored for all input.
- Inputs involving letters are case-insensitive.
- For the input for the 2 numbers for the double input sections, the 2 numbers can be separated by any amount of whitespace.
- Any time section info, an answer, or an invalid input message are displayed, the options for the menu or section will be redisplayed.

</details>

<details>
<summary>GUI and Website Info</summary>

The GUI and website versions of the app are similar. The screenshots below show the GUI version. The `NTPGUI` class has the `main` method to launch the GUI app and a little of the code for running it. A lot of the other code for doing this is in the `MainPanel` class.


A single input section without info:

![Website single input section without info](/Spring_Website_Version/screenshots/app_overview/single_input_section_without_info.JPG)

A single input section with info:
![Website single input section with info](/Spring_Website_Version/screenshots/app_overview/single_input_section_with_info.JPG)

A double input section:

![Website double input section](/Spring_Website_Version/screenshots/app_overview/double_input_section.JPG)
Some input and a calculation:

![Website input and calculation](/Spring_Website_Version/screenshots/app_overview/calculation.JPG)




The user can navigate among the sections using the buttons at the top. The user can enter input in the text boxes or can change the text of one by clicking one of the buttons below it. Clicking the "Randomize" button will generate a random number in the range of valid input numbers and set the text of the text box to that number. For the "Goldbach Conjecture" section, this number will also be even.

Clicking the "+" button will have one of the effects below:
- If the text box has a number that's lower than the max input number, the text box will have its text set to the next highest valid input number.
- If the text box is empty or has a number greater than or equal to the max input number, the text box will have its text set to the min input number.

Clicking the "-" button will have one of the effects below:
- If the text box has a number that's higher than the min input number, the text box will have its text set to the next lowest valid input number.
- If the text box is empty or has a number less than or equal to the min input number, the text box will have its text set to the max input number.

In addition to the above, if the text box has something other than a number or a number that's not in the range of the integers that can fit in an `int` type (-2<sup>31</sup> to 2<sup>31</sup> - 1, or -2,147,483,648 to 2,147,483,647), nothing will happen if either the "+" or "-" buttons are clicked.


</details>

## All Sections

Shown in the collapsible sections below is info about all sections featured in the application, including concept info, what can be calculated and displayed, and the range of valid input integers. The concept info in this document is mostly the same as the concept info displayed in the application, though there are some mathematical expressions and symbols featured in this document that can be written with GitHub Markdown, such as $\sqrt{29}$ and $\Phi$. Here's a [*GitHub Docs* article on writing mathematical expressions](https://docs.github.com/en/get-started/writing-on-github/working-with-advanced-formatting/writing-mathematical-expressions). There are also usages of *italics and emphasis* in this document.

<details>
<summary>Prime Numbers</summary>

#### Info

A *prime number*, or a *prime*, is a whole number > 1 that isn't divisible by any whole numbers other than 1 and
itself. A *composite number* is a whole number > 1 that is divisible by a whole number other than 1 and itself.
The first 10 primes are 2, 3, 5, 7, 11, 13, 17, 19, 23, and 29. There are an infinite amount of primes. The
largest known one is $2^{136,279,841} - 1$. It has 41,024,320 digits! Primes are used in 7 of the 10 sections in
the Number Theory Playground.

With the exception of 2 and 3, all primes are either 1 above or 1 below a multiple of 6. To show why this is the
case, let's have a variable $n$ and let it represent a whole number ≥ 6 that's a multiple of 6. We know that
$n$ is divisible by 2 and 3 so $n$ + 2 and $n$ + 4 are divisible by 2 and $n$ + 3 is divisible by 3 but we don't
have any guarantees about what $n$ + 1 and $n$ + 5 are divisible by. Therefore, that's where primes can be.

A whole number can be determined to be prime if it's not divisible by any primes ≤ the square root of that number.
This is called *trial division*. Let's determine if 29 and 33 are prime. $5^2 = 25$ and $6^2 = 36$ so
$5 < \sqrt{29} < \sqrt{33} < 6$. We check if 29 and 33 are divisible by 2, 3, or 5; which are the primes ≤ 5. 29
isn't divisible by any of those and 33 is divisible by 3 so 29 is prime and 33 isn't.

#### Calculation

Find the first 30 primes that are $\geq$ an input number.

#### Example Calculation Screenshots

![CLI primes calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/primes_calculation.JPG)

#### Input Range: 0 - 10,000,000,000,000 (10 trillion)
![Website primes calculation](/Spring_Website_Version/screenshots/section_calculations/primes_calculation.JPG)

</details>

<details>
<summary>Twin Prime Pairs</summary>

#### Info

A *twin prime pair* is a pair of prime numbers that differ by 2. The first 5 twin prime pairs are
3 & 5, 5 & 7, 11 & 13, 17 & 19, and 27 & 29. The largest known twin prime pair is
$2,996,863,034,895 \times 2^{1,290,000} \pm 1$. They have 388,342 digits! It's conjectured that there are an
infinite amount of twin prime pairs. A *conjecture* is a statement that's believed to be true but hasn't been
proven to be.

All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so this means that all twin prime
pairs besides 3 and 5 consist of 1 number that's 1 below a multiple of 6 and another number that's 1 above that
same multiple of 6. 5 is the only number to be in 2 twin prime pairs, the first 2 mentioned above.

#### Calculation

Find the first 20 pairs of twin primes where the lowest number in the pair is $\geq$ an input number. For example, if the input number is 3, then the pair 3 and 5 will be the first one found since the lowest number in that pair is 3. If the input number is 4, then the pair 5 and 7 will be the first one found.

#### Input Range: 0 - 500,000,000,000 (500 billion)

#### Example Calculation Screenshots

![CLI twin prime pairs calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/twin_prime_pairs_calculation.JPG)

![Website twin prime pairs calculation](/Spring_Website_Version/screenshots/section_calculations/twin_prime_pairs_calculation.JPG)

</details>

<details>
<summary>Prime Factorization</summary>

#### Info

The Fundamental Theorem of Arithmetic says that every whole number > 1 can be expressed as the product of
prime numbers in 1 way if you ignore the order of those prime numbers. The *prime factorization* (PF) of a whole
number > 1 is an expression of the prime numbers whose product is that number. For example, the PF of 5 is just
$5$, the PF of 25 is $5^2$, and the PF of 12,250 is $2 \times 5^3 \times 7^2$ if the prime numbers are in
ascending order. 12,250 could also be expressed as $5^3 \times 2 \times 7^2$ but that's the same expression as the
previous one if you ignore the order of the prime numbers. The Number Theory Playground displays PFs with the
prime numbers in ascending order. There are some interesting applications for PFs. See the info for the
"Divisibility" or "GCD and LCM" sections for some applications.

For the website version, the input number with the highest amount of prime factors is 8,192 (2<sup>13</sup>),
the largest power of 2 ≤ 10,000, the max input. An input number with the highest amount of *unique* prime
factors is 2,310. This number has a PF of $2 \times 3 \times 5 \times 7 \times 11$. You could also multiply that
number by 2, 3, or 4 and those numbers are ≤ the max input and have the same amount of unique prime factors.

For the CLI and GUI versions, the input number with the highest amount of prime factors is 9,007,199,254,740,992
(2<sup>53</sup>), the largest power of 2 ≤ 10 quadrillion, the max input. The input number with the highest
amount of *unique* prime factors is 304,250,263,527,210. This number is the product of the first 13 prime
numbers so it has 13 unique prime factors and its PF is
$2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \times 37 \times 41$.
You could also multiply that number by 2 or 3 and those numbers are ≤ the max input and have the same
amount of unique prime factors.

#### Calculation

Find the PF of an input number faster than you can say "prime factorization". :slightly_smiling_face:

#### Input Range: 2 - 10,000,000,000,000,000 (10 quadrillion)

#### Example Calculation Screenshots

![CLI PF calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/PF_calculation.JPG)

![Website PF calculation](/Spring_Website_Version/screenshots/section_calculations/PF_calculation.JPG)

</details>

<details>
<summary>Divisibility &nbsp; :heavy_division_sign:</summary>

#### Info

Say we have 2 whole numbers that we'll represent with the variables $a$ and $b$. If we divide $a$ by $b$ and get
no remainder, then $a$ is said to be *divisible* by $b$ and $b$ is said to be a *factor* or *divisor* of $a$. If
you want to find some whole number factors of a whole number, you could manually do some division but there are
other ways to find them.

##### Prime Factorization

The factors of a whole number > 1 can be found by looking at its prime factorization (PF). Let's have a variable $n$
and let it represent a whole number > 1. First, you can find how many factors $n$ has by looking at $n$'s PF,
taking all the powers of the factors, adding 1 to each, and then multiplying all these together. For example, the
PF of 36 is $2^2 \times 3^2$. The powers are 2 and 2, so there are $3 \times 3 = 9$ factors. However, that count
includes 1 and the number that the PF is for (36 in this case). If you want to exclude those, then subtract 2.
That would give us 7 factors. You can find the factors of $n$ by finding all the PFs within $n$'s PF, or the
"sub-factorizations", as I like to call them. For $2^2 \times 3^2$, the sub-factorizations are
$2$, $3$, $2^2 \text{ } (4)$, $2 \times 3 \text{ } (6)$, $3^2 \text{ } (9)$, $2^2 \times 3 \text{ } (12)$, and
$2 \times 3^2 \text{ } (18)$.

For the website version, whole numbers that are ≤ the max input of this section generally have a pretty small
amount of factors, like < 20. An example of an input number with a high amount of factors is 9,240. This number
has a PF of $2^3 \times 3 \times 5 \times 7 \times 11$, so it has $4 \times 2^4 = 2^6 = 64$ total factors!

For the CLI and GUI versions, whole numbers that are ≤ the max input of this section still generally have a
pretty small amount of factors, like < 100. An example of an input number with a high amount of factors is
9,736,008,432,870,720, or 9 quadrillion 736 trillion ... This number is the product of 2<sup>6</sup> and the
next 12 prime numbers so it has 13 unique prime factors and its PF is
$2^6 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \times 37 \times 41$.
It has $7 \times 2^12 = 28,672$ total factors!

##### Divisibility Rules

Some rules can be used to determine if a whole number is divisible by another whole number. I'll go over 1 rule
for each number in the range of 3 to 12, excluding 5 and 10, though there are rules for more numbers and many
numbers have multiple rules. I'll go over an example of using these rules to find the factors of a
number in the "Example" section below. Let's have a variable $n$ and let it represent a whole number. If the
last 2 digits of $n$ is divisible by 4, then $n$ is divisible by 4. If the last 3 digits of $n$ is divisible by
8, then $n$ is divisible by 8. If the sum of the digits of $n$ is divisible by 3, then $n$ is divisible by 3. If
the sum of the digits of $n$ is divisible by 9, then $n$ is divisible by 9. If $n$ is even and divisible by 3,
then it's also divisible by 6. If $n$ is divisible by both 3 and 4, then it's also divisible by 12.

For 7, we split $n$ into 3-digit blocks from right to left, though the leftmost block can contain 1 or 2 digits.
Coincidentally, these are the blocks separated by commas if we write $n$ with commas. We do an alternating sum
of the blocks from right to left. We start with 0, add the rightmost block, subtract the block to the left of
that, add the block to the left of that, and so on for all the blocks. If this sum is divisible by 7, then $n$
is divisible by 7.

For 11, we do an alternating sum of the digits of $n$ from left to right. We start with 0, add the
1<sup>st</sup> digit, subtract the 2<sup>nd</sup> digit, add the 3<sup>rd</sup> digit, and so on for all the
digits. If this sum is divisible by 11, then $n$ is divisible by 11. These alternating sums might involve
negative integers or 0, so that makes them some of the few calculations done by the Number Theory Playground
that involve numbers other than natural numbers.

###### Example

Let $n$ be 5,544. Its PF is $2^3 \times 3^2 \times 7 \times 11$. We can tell from that PF that $n$ is divisible
by all the numbers that had rules mentioned about them above. Let's check if $n$ is divisible by those numbers
using those rules. The last 2 digits are 44, which is divisible by 4. The last 3 digits are 544, which is
divisible by 8. The sum of the digits is $5 + 5 + 4 + 4 = 18$, which is divisible by both 3 and 9. Since $n$ is
even and divisible by 3, it's also divisible by 6. Since $n$ is divisible by both 3 and 4, it's also divisible
by 12. The alternating sum of 3-digit blocks from right to left is $544 - 4 = 539$, which is divisible by 7.
The alternating sum of digits from left to right is $5 - 5 + 4 - 4 = 0$, which is divisible by 11.

#### Calculations

1. Use the special tricks to see if we can find some factors of an input number and build a paragraph that says info from this.
2. Find the PF of the input number. If we can determine from this PF that the input number is composite, then manually find the factors and their PFs and show that they are sub-factorizations of the PF of the input number.

#### Input Range: 2 - 10,000,000,000,000,000 (10 quadrillion)

#### Example Calculations Screenshots

![CLI divisibility calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/divisibility_calculations.JPG)

![Website divisibility calculations](/Spring_Website_Version/screenshots/section_calculations/divisibility_calculations.JPG)

</details>

<details>
<summary>Greatest Common Divisor (GCD) and Least Common Multiple (LCM) &nbsp; :heavy_division_sign: &nbsp; :heavy_multiplication_x:</summary>

#### Info

Greatest common divisor is also known as greatest common factor, or GCF. To find the GCD and LCM of 2 whole
numbers, you could manually do some division and multiplication but there are other ways to find them.

##### The Euclidean Algorithm

The Euclidean algorithm can be used to find just the GCD of 2 whole numbers. This was named after the ancient
Greek mathematician Euclid.

A simple way of explaining this algorithm is that it starts with 2 whole numbers that we want to find the GCD of
and if the max of those numbers is divisible by the min, then that min is the GCD. Otherwise, the GCD of the 2
numbers is the same as the GCD of the min and the remainder when the max is divided by the min. Repeat.

Another way of explaining this algorithm is that it consists of iterations and each one consists of a max number,
min number, and remainder when the max number is divided by the min number. These'll be referred to as just the
max, min, and remainder. The 1<sup>st</sup> iteration has a max of the max of 2 whole numbers that you want to
find the GCD of. The min of this iteration is the min of those 2 numbers. If the remainder is 0, then the
algorithm is done and the GCD of the 2 numbers that we wanted to find the GCD of is the min of this iteration.
Otherwise, we do another iteration and the max of the new iteration is the min of the last iteration and the min
of the new iteration is the remainder of the last iteration. Again, we check if the remainder is 0 and if it is,
then the min of this iteration is the GCD. Otherwise, we keep doing iterations until we get a remainder of 0.

Let's find the GCD of 6 and 35 using the Euclidean algorithm: Here are the iterations:

| Max | Min | Remainder |
| --- | --- | --------- |
|  35 |  6  |     5     |
|  6  |  5  |     1     |
|  5  |  1  |     0     |

The GCD is 1.

Let's find the GCD of 54 and 99 using the Euclidean algorithm: Here are the iterations:

| Max | Min | Remainder |
| --- | --- | --------- |
|  99 |  54 |     45    |
|  54 |  45 |     9     |
|  45 |  9  |     0     |

The GCD is 9.

##### Prime Factorizations

The GCD and LCM of 2 whole numbers > 1 can be found by looking at their prime factorizations (PFs). If those
numbers don't have any common prime factors, then the GCD is 1. If they do have common prime factors, then the GCD
PF consists of all the common prime factors and the power of each factor is the min of the powers of that factor
in the 2 PFs. The LCM PF consists of all factors that are in either of the PFs of the 2 numbers. If a factor is in
both PFs, then the power of that factor in the LCM PF is the max of the powers of that factor in the 2 PFs. If a
factor is unique to one of the PFs, then that factor and its power are in the LCM PF.

Let's find the GCD and LCM of 6 and 35 using their PFs. The PF of 6 is $2 \times 3$ and the PF of 35 is $5 \times 7$.
There are no common prime factors so the GCD is 1. The LCM PF is $2 \times 3 \times 5 \times 7$, which is 210.

Let's find the GCD and LCM of 54 and 99. The PF of 54 is $2 \times 3^3$ and the PF of 99 is $3^2 \times 11$. 3 is
the only common prime factor and the min power of it is 2 so the GCD PF is $3^2$, which is 9. The max power of 3
is 3 so $3^3$ is in the LCM PF. The LCM PF is $2 \times 3^3 \times 11$, which is 594.

##### Other Info

2 whole numbers are said to be *coprime* if their GCD is 1. Therefore, coprime numbers don't have any common
factors in their PFs.

For the website version, the input numbers whose LCM is the highest are 10,000, the max input, and 9,999. Their
LCM is 99,990,000. A pair of input numbers whose LCM has the highest amount of prime factors is
8,192 (2<sup>13</sup>) and 6,561 (3<sup>8</sup>). Their LCM is 53,747,712. A pair of input numbers whose LCM
might have the highest amount of *unique* prime factors is 2,310, the product of the first 5 prime numbers; and
4,199, the product of the next 3 prime numbers. The LCM is 9,699,690 and its PF is
$2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19$.

For the CLI and GUI versions, the input numbers whose LCM is the highest are 5 quadrillion
(5,000,000,000,000,000), the max input, and 4,999,999,999,999,999, the max input - 1. Their LCM is
24,999,999,999,999,995,000,000,000,000,000, or
24 nonillion 999 octillion 999 septillion 999 sextillion 999 quintillion 995 quadrillion! It has 32 digits.
Trillion is before quadrillion. A pair of input numbers whose LCM has the highest amount of prime factors is
4,503,599,627,370,496 (2<sup>52</sup>) and 1,853,020,188,851,841 (3<sup>32</sup>). Their LCM is
8,345,261,032,023,157,253,752,158,683,136, or 8 nonillion ... A pair of input numbers whose LCM might have the
highest amount of *unique* prime factors is 304,250,263,527,210, the product of the first 13 prime numbers, and
133,869,006,807,307, the product of the next 8 prime numbers. Their LCM is
40,729,680,599,249,024,150,621,323,470, or 40 octillion ... It has 29 digits and 21 unique prime factors and its
PF is
$2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \times 37
\times 41 \times 47 \times 53 \times 59 \times 61 \times 67 \times 71 \times 73$!

#### Calculations

1. Perform the Euclidean algorithm on 2 input numbers and display a table with info about all iterations. Each iteration has a max number, min number, and remainder when the max is divided by the min.
2. Find the PFs of the input numbers and use these to find the PFs of the GCD and LCM. Display a table with all 4 numbers and their PFs.

#### Input Range: 2 - 5,000,000,000,000,000 (5 quadrillion)

#### Example Calculations Screenshots

![CLI GCD and LCM calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/GCD_and_LCM_calculations.JPG)

![Website GCD and LCM calculations](/Spring_Website_Version/screenshots/section_calculations/GCD_and_LCM_calculations.JPG)

</details>

<details>
<summary>Goldbach Conjecture</summary>

#### Info

The Goldbach Conjecture says that every even number ≥ 4 can be expressed as the sum of 2 prime numbers. This
was named after 1700s Prussian mathematician Christian Goldbach. A *conjecture* is a statement that's believed to
be true but hasn't been proven to be. The Goldbach Conjecture has been verified to be true for all even numbers
≥ 4 and ≤ 4 × 10<sup>18</sup>.

#### Calculation

Find the pairs of prime numbers that sum to an even input number.

#### Input Range: 4 - 1,500,000

#### Example Calculation Screenshots

![CLI Goldbach Conjecture calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/Goldbach_Conjecture_calculation.JPG)

![Website Goldbach Conjecture calculation](/Spring_Website_Version/screenshots/section_calculations/Goldbach_Conjecture_calculation.JPG)

</details>

<details>
<summary>Pythagorean Triples</summary>

#### Info

The Pythagorean Theorem says that for a right triangle, the sum of the squares of the lengths of the 2 short sides
equals the square of the long side (hypotenuse) length, or $a^2 + b^2 = c^2$. This theorem was named after the
ancient Greek mathematician Pythagoras. A *Pythagorean triple* is a triple of whole numbers that $a$, $b$, and $c$
can be. For example; 3, 4, and 5 is a Pythagorean triple since
$3^2 \text{ } (9) + 4^2 \text{ } (16) = 5^2 \text{ } (25)$ and 11, 60, and 61 is another one since
$11^2 \text{ } (121) + 60^2 \text{ } (3,600) = 61^2 \text{ } (3,721)$.

Once we know a Pythagorean triple, we can form another one by multiplying $a$, $b$, and $c$ by the same whole
number > 1. Because of this, there are an infinite amount of Pythagorean triples. A Pythagorean triple is
considered to be *primitive* if the GCD of $a$, $b$, and $c$ is 1. Therefore, a primitive triple can't be formed
by taking another triple and multiplying $a$, $b$, and $c$ by the same whole number. The triples mentioned above;
3, 4, and 5, and 11, 60, and 61; are primitive.
$6 \text{ } (3 \times 2)$, $8 \text{ } (4 \times 2)$, and $10 \text{ } (5 \times 2)$ is another triple.
$6^2 \text{ } (36) + 8^2 \text{ } (64) = 10^2 \text{ } (100)$.
$55 \text{ } (11 \times 5)$, $300 \text{ } (60 \times 5)$, and $305 \text{ } (61 \times 5)$ is another one.
$55^2 \text{ } (3,025) + 300^2 \text{ } (90,000) = 305^2 \text{ } (93,025)$.

#### Calculation

Find the first 10 Pythagorean triples where the lowest number in the triple is $\geq$ an input number. For example, if the input number is 3, then the triple 3, 4, and 5 will be the first one found since the lowest number in that triple is 3. If the input number is 4, then the triple 5, 12, and 13 will be the first one found. These triples will be displayed similarly to how the examples at the end of the paragraphs in the "Info" section above are displayed. If a triple is primitive, then it will be followed by "(primitive)".

#### Input Range: 0 - 10,000

#### Example Calculation Screenshots

![CLI Pythagorean triples calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/pythag_triples_calculation.JPG)

![Website Pythagorean triples calculation](/Spring_Website_Version/screenshots/section_calculations/pythag_triples_calculation.JPG)

</details>

<details>
<summary>Two Square Theorem</summary>

#### Info

The Two Square Theorem says that every prime number that's 1 above a multiple of 4 can be expressed as the sum of
2 square numbers. A *square number*, also known as a *perfect square*, is a number that can be formed by taking an
integer and multiplying it by itself, or squaring it. The first 4 square numbers are $0 \text{ } (0^2)$,
$1 \text{ } (1^2 \text{ or } (-1)^2)$, $4 \text{ } (2^2 \text{ or } (-2)^2)$, and $9 \text{ } (3^2 \text{ or } (-3)^2)$.
An example of a number that's prime and is 1 above a multiple of 4 is 29 and it can be expressed as
$2^2 \text{ } (4) + 5^2 \text{ } (25)$.

#### Calculations

Find the first prime number $\geq$ an input number that is 1 above a multiple of 4, as well as the numbers whose squares sum to that number.

#### Example Calculations Screenshots

#### Input Range: 0 - 1,000,000,000,000,000 (1 quadrillion)
![CLI Two Square Theorem calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/two_square_theorem_calculations.JPG)

![Website Two Square Theorem calculations](/Spring_Website_Version/screenshots/section_calculations/two_square_theorem_calculations.JPG)

</details>

<details>
<summary>Fibonacci-like Sequences</summary>

#### Info

I consider a number sequence to be "Fibonacci-like" if it starts with 2 numbers and has every following number be
the sum of the 2 previous numbers. The Fibonacci sequence does this and the first 8 numbers of it are
1, 1, 2, 3, 5, 8, 13, and 21. Fibonacci was a mathematician from the 1100s to 1200s from modern-day Italy. Another
Fibonacci-like sequence is the Lucas sequence and the first 8 numbers of it are 2, 1, 3, 4, 7, 11, 18, and 29.
This sequence was named after 1800s French mathematician Francois Edouard Anatole Lucas

The *Golden Ratio* is an irrational number symbolized by the Greek letter 𝚽 (Phi).
𝚽 $= \frac{1 + \sqrt{5}}{2} \approx 1.618033988749895$. As we advance further and further into a Fibonacci-like
sequence, the ratio between a number and the number before it gets closer and closer to 𝚽. For example, recall
that the first 8 numbers of the Fibonacci sequence are 1, 1, 2, 3, 5, 8, 13, and 21. $\frac{2}{1} = 2$,
$\frac{8}{5} = 1.6$, and $\frac{21}{13} \approx 1.615384615384615$.


#### Calculations

#### Example Calculations Screenshots
1. Find the first 20 numbers of the Fibonacci-like sequence that starts with 2 input numbers.
2. Find the ratios between the 5<sup>th</sup> and 4<sup>th</sup>, 10<sup>th</sup> and 9<sup>th</sup>,
   15<sup>th</sup> and 14<sup>th</sup>, and 20<sup>th</sup> and 19<sup>th</sup> numbers. These ratios are 
   floating-point numbers most of the time, so the calculations for them are some of the few calculations done by
   the Number Theory Playground that involve numbers other than natural numbers.

#### Input Range: 1 - 9,000,000,000,000,000,000 (9 quintillion)
![CLI Fibonacci-like sequences calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/fibo-like_sequences_calculations.JPG)

![Website Fibonacci-like sequences calculations](/Spring_Website_Version/screenshots/section_calculations/fibo-like_sequences_calculations.JPG)

</details>

<details>
<summary>Ancient Egyptian Multiplication</summary>

#### Info

The ancient Egyptians had an interesting algorithm for multiplication of 2 whole numbers. My way of explaining the
algorithm goes like this:

1. Let variable $a$ represent one of the numbers and variable $b$ represent the other number.
2. Find all powers of 2 that are ≤ $a$. This could be done without modern multiplication by starting with 1, the
   1<sup>st</sup> power of 2 or 2<sup>0</sup>, and finding the next power by adding the previous power to itself.
   This process will look like: $1 + 1 = 2 \text{ } (2^1)$, $2 + 2 = 4 \text{ } (2^2)$, $4 + 4 = 8 \text{ } (2^3)$,
   and so on until we find a power that's > $a$, which we won't use.
3. Find the products of $b$ and these powers of 2. Like with the powers of 2, this could be done by starting with
   $b$ and finding the next product by adding the previous product to itself. If we let $b$ be 5, this process
   will look like: $5 + 5 = 10 \text{ } (5 \times 2)$, $10 + 10 = 20 \text{ } (5 \times 4)$,
   $20 + 20 = 40 \text{ } (5 \times 8)$, and so on.
4. Find the powers of 2 that sum to $a$.
5. Add up the products of $b$ and these powers.

This gives us the product of the 2 numbers.

Let's find the product of 5 and 12. Let's first use 5 for the number represented by $a$ in the algorithm above and
12 for $b$. The powers of 2 ≤ 5 are 1, 2, and 4. The products of 12 and these powers are 12, 24, and 48. The
powers of 2 that sum to 5 are 1 and 4. The products of 12 and these powers are 12 and 48.
$12 + 48 = (12 \times 1) + (12 \times 4) = 12 \times (1 + 4) = 60$.

Now let's use 12 for $a$ and 5 for $b$. The powers of 2 ≤ 12 are 1, 2, 4, and 8. The products of 5 and these
powers are 5, 10, 20, and 40. The powers of 2 that sum to 12 are 4 and 8. The products of 5 and these powers are
20 and 40. $20 + 40 = (5 \times 4) + (5 \times 8) = 5 \times (4 + 8) = 60$.

#### Calculations

Given 2 input numbers:
1. Find the powers of 2 $\leq$ the 1<sup>st</sup> input number and the corresponding multiples of the 2<sup>nd</sup> input number. Display these in a table.
2. Find the powers of 2 that sum to the 1<sup>st</sup> input number and the corresponding multiples of the 2<sup>nd</sup> input number. Display these in another table.

This process will be done faster than you can say "ancient Egyptian multiplication". :slightly_smiling_face:

#### Input Range: 2 - 9,000,000,000,000,000,000 (9 quintillion)

#### Example Calculations Screenshots

![CLI ancient Egyptian multiplication calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/ancient_mult_calculations.JPG)

![Website ancient Egyptian multiplication calculations](/Spring_Website_Version/screenshots/section_calculations/ancient_mult_calculations.JPG)

</details>

## Some Design and Implementation Info


### Java Versions

There’s an abstract `Section` class that has data and functionality that the app will use. The direct subclasses of this are the abstract classes `SingleInputSection` and `DoubleInputSection`. There are 2 abstract methods on each of these, `getCliAnswer` and `getGuiComponents`. Both of these do the calculation(s) for the section and return something that contains info from the calculation and will be displayed in the app. `getCliAnswer` returns a string and `getGuiComponents` returns a list of GUI components. The number of parameters for these is either 1 or 2, depending on the class.

For each section in the app, there's a class with code related to that section. The names of these classes are similar to the section names in the “All Sections” section. These classes will be referred to as *outer section classes*. Some methods in these classes are used by other classes as well. The `PrimeFactorization` class has instance and static members and all other outer section classes are *utility classes*, meaning that they contain only static members.

Each outer section class has a nested, concrete, static class called `Section` that extends either `SingleInputSection` or `DoubleInputSection`. These classes will be referred to as *nested section classes*. These implement `getCliAnswer` and `getGuiComponents` using the code in the outer section class and sometimes code from other classes as well.

Input validation for a calculation is implemented by having a method that does a calculation call `Misc.assertIsInRange`, which will throw an `IllegalArgumentException` for args that are out of range. The Goldbach Conjecture calculation method also throws this exception for odd number args. These exceptions bubble up to either `getCliAnswer` or `getGuiComponents`, and then to either the `NTPCLI` or the `MainPanel`, where the exception is handled.

The nested section class constructors call their superclass constructors and one of the args for this is a list of strings to represent paragraphs of info that will be displayed to the user. The string literals used for these are placed near related code, usually at the top of an outer section class. This is done in an attempt to help explain that related code.

### Website

A single page application is used. The content of the page changes when a section changes or an answer is displayed. A RESTful API is also used. If the "Calculate" button is clicked and the text boxes have valid input, an API request is sent to the server with info about the section and user input. The server does the calculation(s) for the section and sends a JSON response of data about the calculation. The webpage will then use this data and create some elements to display the data to the user.

#### API Documentation

All of the paths below are for `GET` requests and are preceded by `numbertheoryplayground.com/calculate/`. `{input}`, `{input1}`, and `{input2}` represent input values in the path. JSDoc syntax will be used to document return types.

##### Prime Numbers

`primes/{input}`
`number[]`
Returns an array of the first 30 primes ≥ the input.


##### Twin Prime Pair Starts

`twinPrimePairStarts/{input}`
`number[]`
Finds the first 20 twin prime pairs where the lower of the numbers in the pair is ≥ the input. For example, if the input is 3, then the pair 3 and 5 will be the first one found since the lower number in that pair is 3. If the input is 4, then the pair 5 and 7 will be the first one found. An array that contains the lower numbers of those pairs gets returned.


##### Prime Factorization

`primeFactorization/{input}`
`{ factor: number, power: number }[]`
Returns an array of objects that represents the prime factorization of the input. Each object in the array is for a prime factor and power in this prime factorization.


##### Divisibility Answer Data

`divisibilityAnswer/{input}`
```
{
    rulesData: {
        last2Digits: number,
        last3Digits: number,
        sumOfDigits: number,
        blocksOf3AltSumAndExpression: ?{ sum: number, expression: string },
        digitsAltSumAndExpression: { sum: number, expression: string }
    },
    pfAnswer: {
        inputFpArr: { factor: number, power: number }[],
        numFactorsExpression: string,
        numFactors: number,
        factorFpArrsAndNumStrings: {
            fpArr: ?{ factor: number, power: number }[],
            correspondingNumString: string
        }[]
    }
}
```

##### GCD and LCM Answer Data

`gcdAndLcmAnswer/{input1}/{input2}`
```
{
    euclideanIterations: { max: number, min: number, remainder: number }[]
    pfAnswer: {
        input1FpArr: { factor: number, power: number }[]
        input2FpArr: { factor: number, power: number }[]
        gcdFpArrAndNumString: ?{
            fpArr: ?{ factor: number, power: number }[],
            correspondingNumString: string
        }
        lcmFpArrAndNumString: {
            fpArr: ?{ factor: number, power: number }[],
            correspondingNumString: string
        }
    }
}
```
If `gcdFpArrAndNumString` is null, then that means that the GCD is 1.


##### Goldbach Prime Pair Starts

`goldbachPrimePairStarts/{input}`
`number[]`

Find the pairs of primes that sum to the input and returns an array that contains the lower numbers of those pairs.


##### Pythagorean Triples

`pythagoreanTriples/{input}`
`{ a: number, b: number, c: number, isPrimitive: number }[]`

Returns an array of objects for the first 10 Pythagorean triples where the lowest number in the triple is ≥ the input. For example, if the input is 3, then an object for the triple 3, 4, and 5 will be the first one since the lowest number in that triple is 3. If the input is 4, then an object for the triple 5, 12, and 13 will be the first one.


##### Two Square Theorem Answer Data

`twoSquareTheoremAnswer/{input}`
`{ primeNum: number, a: number, b: number }`
`primeNum` is the first prime number ≥ the input that's 1 above a multiple of 4. `a` and `b` are the numbers whose squares sum to `primeNum`.


##### Fibonacci-like Sequences Answer Data

`fibonacciLikeSequencesAnswer/{input1}/{input2}`
```
{
    stringFiboLikeSequence: string[],
    ratioDataArray: { num1String: string, num2String, ratio: number, isRounded: boolean }[]
}
```
`stringFiboLikeSequence` contains string representations of the first 20 numbers in the Fibonacci-like sequence that starts with `input1` and `input2`. String representations are used since the numbers might be too big to be a safe JavaScript integer.
1-9 quadrillion (9,000,000,000,000,000)


##### Ancient Egyptian Multiplication Answer Data

`ancientMultiplicationAnswer/{input1}/{input2}`
```
{
    table1Rows: { powerOf2String: string, correspondingMultipleString: string }[],
    table2Rows: { powerOf2String: string, correspondingMultipleString: string }[],
    productString: string
}
```
`table1Rows` contains rows for all the powers of 2 ≤ `input1` and the corresponding multiples of `input2`.
`table2Rows` contains rows for all the powers of 2 that sum to `input1` and the corresponding multiples of `input2`, which sum to the product of `input1` and `input2`.
String representations are used for numbers in the row objects since a number in a `correspondingMultipleString` might be too big to be a safe JavaScript integer. The numbers in each `powerOf2String` will be small enough to be a safe JavaScript integer but strings are still used for these for consistency.
2-9 quadrillion (9,000,000,000,000,000)


## Project History

During the summer of 2019, I took a number theory course on [brilliant.org](https://brilliant.org/) for fun and was enlightened by some of the stuff I was learning. Later that year, I was thinking of what kind of programming projects I could work on and some of the stuff I learned from that number theory course came to mind. I realized I could make a program that makes calculations about some of the stuff I learned. Along with the content of that number theory course, I also included additional number theory concepts that I was familiar with. I learned these other concepts from sources such as my discrete math textbook and YouTube videos from channels such as Numberphile, Mathologer, 3blue1brown, and Zach Star. I decided to use Java because it's a popular programming language that I had no familiarity with at the time. However, I found Java easy to learn since some of the syntax is similar to C++, which I had some familiarity with. In fact, the 3 languages I had the most familiarity with at the time were Python :snake:, C++, and JavaScript.

I started in December 2019 and from then to October 2020, I was on and off this project. I had my last normal semester of college from January to May. Well, it was normal until the middle of March. I then had an independent study course over the summer where I worked on my Singletonopoly project. The first completed version of this project was uploaded to GitHub in October. This included a Java command line interface and graphical user interface. This included the first 7 of the 10 sections that are in the application now, starting with the prime numbers section and ending with the Pythagorean Triples section.

I think it was around this time (October 2020) when I decided to make a website version. I used the Pug template engine for the HTML and vanilla JavaScript and CSS. I used Node.js and Express for the back end. These are the same tools that I used for a web development course. I originally had the calculations done on the front end but moved these to the back end. For this website version, I included the 7 sections that were in the Java versions and also added 3 more sections, the Two Square Theorem, Fibonacci-like sequences, and ancient Egyptian multiplication sections. These would later be added to the Java versions.

Refactoring was done and other changes were made throughout 2021, 2022, and 2023. Unit tests were created for the Java versions in 2022.

### Refactoring Examples

#### Implementation of Calculations

<details>
<summary>Code</summary>

[GitHub code link](https://github.com/ncschroeder/Number-Theory-Playground/blob/b0afb8fc9e406987a212e8ce54c487f3ed3a4b9c/Primes.java#L48-L110)

```java
/**
 * @return A list of the first 30 prime numbers that appear after the argument number.
 */
public static List<Integer> getPrimesAfter(int number) {
    ArrayList<Integer> primes = new ArrayList<>(30);
    int potentialPrime;

    // First, check if 2 and 3 should be added to the primes list.
    if (number <= 5) {
        // Make potentialPrime the first positive number that is 1 below a multiple of 6.
        potentialPrime = 5;
        if (number <= 2) {
            primes.add(2);
        }
        if (number <= 3) {
            primes.add(3);
        }
    } else {
        switch (number % 6) {
            case 0:
                // For numbers that are divisible by 6, check if the next number is prime and set potentialPrime
                // to the next number that is 1 below a multiple of 6.
                if (isPrime(number + 1)) {
                    primes.add(number + 1);
                }
                potentialPrime = number + 5;
                break;

            case 1:
                // For numbers that are 1 above a multiple of 6, check if this number is prime and set
                // potentialPrime to the next number that is 1 below a multiple of 6.
                if (isPrime(number)) {
                    primes.add(number);
                }
                potentialPrime = number + 4;
                break;

            default:
                // Have potentialPrime be the next number that is 1 below a multiple of 6
                potentialPrime = number;
                while ((potentialPrime + 1) % 6 != 0) {
                    potentialPrime++;
                }
                break;
        }
    }

    // Iterate through potential prime numbers, which are numbers that are either 1 below or 1 above a multiple
    // of 6, and check these numbers for primality.
    for (;; potentialPrime += 4) {
        for (int i = 0; i < 2; i++) {
            if (isPrime(potentialPrime)) {
                primes.add(potentialPrime);
                if (primes.size() == 30) {
                    return primes;
                }
            }
            if (i == 0) {
                potentialPrime += 2;
            }
        }
    }
}
```

</details>

This is the current implementation:

<details>
<summary>Code</summary>

[GitHub code link](https://github.com/ncschroeder/Number-Theory-Playground/blob/9824944ed91cf2ff0e289bd7d9bb8d6a378592bf/Java_CLI_and_GUI_Versions/src/main/numbertheoryplayground/sectionclasses/outer/PrimeNumbers.java#L66-L81)

```java
/**
 * Returns a stream of the first 30 primes ≥ the input.
 */
static LongStream getPrimes(long input) {
    assertIsInRange(input, MIN_INPUT, MAX_INPUT);
    
    long iterationStart = isOdd(input) ? input : input + 1;
    LongStream oddPrimes =
        LongStream.iterate(iterationStart, l -> l + 2)
        .filter(PrimeNumbers::isPrime);
    
    // 2 is the only even prime.
    return
        (input <= 2 ? LongStream.concat(LongStream.of(2), oddPrimes) : oddPrimes)
        .limit(NUM_PRIMES_TO_FIND);
}
```

</details>

A calculation that did have a significant algorithm change is for finding factors of an input number and the 
prime factorizations (PFs) of those factors. This started out by using brute force and iterating through ints in
the range of 2 to half of the input number. This was implemented with a for-loop in the
[commit from August 6<sup>th</sup>, 2020](https://github.com/ncschroeder/Number-Theory-Playground/commit/b83df662d79bf702c28d857371b6613476f0e455):

<details>
<summary>Code</summary>

[GitHub code link](https://github.com/ncschroeder/Number-Theory-Playground/blob/b83df662d79bf702c28d857371b6613476f0e455/Functions.java#L263-L289)

```java
public static String[] getDivisInfoViaPF(long number) {
    TreeMap<Long, Integer> PFMap = getPrimeFactorizationMap(number);
    if (PFMap.size() == 1 && PFMap.containsValue(1)) {
        return new String[] {number + " doesn't have any factors, which makes it prime."};
    }
    int numFactors = getNumFactorsViaPF(PFMap);
    String[] lines = new String[numFactors + 3];
    lines[0] = "The prime factorization of " + number + " is " + getPrimeFactorizationString(PFMap);
    lines[1] = "Excluding 1 and " + number + ", there are " + numFactors + " factors";
    lines[2] = "The factors are:";


    int insertionIndex = 3;
    long lastPossibleFactor = number / 2;
    for (long i = 2; i <= lastPossibleFactor; i++) {
        if (number % i == 0) {
            if (PFMap.containsKey(i)) {
                // If the found factor is prime, then only display the number and not it's prime factorization
                lines[insertionIndex] = String.valueOf(i);
            } else {
                // If the found factor is not prime, then display it's prime factorization as well
                lines[insertionIndex] = i + ", which is " + getPrimeFactorizationString(i);
            }
            insertionIndex++;
        }
    }
    return lines;
}
```

</details>

This was then implemented with streams in the
[commit from November 11<sup>th</sup>, 2022](https://github.com/ncschroeder/Number-Theory-Playground/commit/9adfdda4e34afe4c31b0947776dccc20a0c5d5b1):

<details>
<summary>Code</summary>

[GitHub code link](https://github.com/ncschroeder/Number-Theory-Playground/blob/9adfdda4e34afe4c31b0947776dccc20a0c5d5b1/Java%20Versions/Divisibility.java#L65-L94)

```java
/**
 * Creates a sequential stream containing the factors of intParam besides 1 and intParam. The tests
 * will collect this stream to a list and the applications will do some mapping on this before collecting
 * it to a list, as seen in the getFactorPfStrings method below.
 * @param intParam
 */
public static Stream<Integer> getFactorsStream(int intParam) {
    return
        IntStream.rangeClosed(2, intParam / 2)
        .filter(i -> isDivisible(intParam, i))
        .boxed();
}


/**
 * PFs can be used to find the factors of an integer. This can be done by finding what I consider
 * to be "sub-factorizations" of a PF. These are PFs within a PF. For the PF 2 x 3 x 5 x 7, some
 * examples of sub-factorizations are 2, 2 x 3, and 2 x 3 x 5. In order to find sub-factorizations
 * programmatically, you would have to use the factorsAndPowers map of a PF object and find all possible combinations
 * of factors and powers. I don't know how to do that so I'll just do some iteration to find the factors
 * and then create PF objects from those factors.
 * @return A list of prime factorization strings for all factors of intParam.
 * @throws IllegalArgumentException
 */
public static List<String> getFactorPfStrings(int intParam) {
    assertIsInRange(intParam, minInputInt, maxInputInt);
    return
        getFactorsStream(intParam)
        .map(i -> new PrimeFactorization(i).toStringWithCorrespondingLong())
        .collect(Collectors.toList());
}
```

</details>

I learned from that brilliant.org course that the PFs within a PF are the PFs of the factors. In order to find
these programatically, you would have to find combinations of factors and powers. I used to not know how to do
that and I didn't bother to research it. It was in 2024 when I got some practice with combinations by doing
LeetCode problems with them. Then, I came up with a way to find combinations of factors and powers and added
this code to the repo in the
[commit from December 2<sup>nd</sup>, 2024](https://github.com/ncschroeder/Number-Theory-Playground/commit/d25c5d4fd789da760b8c2d84b567e41776827297):

<details>
<summary>Code</summary>

[GitHub code link](https://github.com/ncschroeder/Number-Theory-Playground/blob/d25c5d4fd789da760b8c2d84b567e41776827297/Java%20Versions/PrimeFactorization.java#L213-L249)

```java
/**
 * This method finds the sub-factorizations by finding combinations of factors and powers.
 */
public List<PrimeFactorization> getFactorPfs() {
    var factorPfs = new ArrayList<PrimeFactorization>();
    
    factorsAndPowers.forEach((factor, thisPfPower) -> {
        /*
        In the 2nd for loop below, we want to iterate through all the PFs that are in factorPfs
        at this point, and not the ones that get added below. Use this variable for that.
            */
        int lastPfIndexToUse = factorPfs.size() - 1;
        
        for (var factorPfPower = 1; factorPfPower <= thisPfPower; factorPfPower++) {
            factorPfs.add(new PrimeFactorization(Map.of(factor, factorPfPower)));
            
            for (var i = 0; i <= lastPfIndexToUse; i++) {
                var factorPfFactorsAndPowers =
                    new HashMap<Long, Integer>(factorPfs.get(i).factorsAndPowers);
                factorPfFactorsAndPowers.put(factor, factorPfPower);
                factorPfs.add(new PrimeFactorization(factorPfFactorsAndPowers));
            }
        }
    });
    
    /*
    The last PF has all the factors that this PF has and each power is the same as the powers in
    this PF, so it's the same as this PF. We don't want to include that as part of the factors.
     */
    factorPfs.removeLast();
    factorPfs.sort(Comparator.comparing(PrimeFactorization::getCorrespondingBigInt));
    return factorPfs;
}

public Stream<String> getFactorPfStrings() {
    return getFactorPfs().stream().map(PrimeFactorization::toStringWithCorrespondingBigInt);
}
```

</details>

For the first 2 implementations, the iterating through the ints has a time complexity of $O(n^2)$, where $n$ is
the input number. Then, for every factor that gets found while iterating, the prime factorization for it gets
calculated and this has a time complexity of $O(\sqrt{n})$. For the current implementation, the amount of work
that needs to be done is proportional to the number of factors of the input number, which is proportional to the
product of the powers in its prime factorization. For more info about this, see the collapsible section below.

<details>
<summary>More info about how many factors a whole number has</summary>

Here's an excerpt from the Divisibility section info in the ["All Sections" section](#all-sections):

> you can find how many factors $n$ has by looking at $n$'s PF, taking all the powers of the factors, adding 1
> to each, and then multiplying all these together. For example, the PF of 36 is $2^2 \times 3^2$. The powers
> are 2 and 2, so there are $3 \times 3 = 9$ factors.

For whole numbers that are < the max input of the Divisibility section, the number of factors they have are
generally pretty small. I ran the CLI version of the app, which has a higher max input than the website version,
and used random input for the Divisibility section 10 times. The random numbers and the number of factors they
have are shown in the table below.

|    Input Number     | Number of Factors |
| ------------------- | ----------------- |
|   672,570,401,060   |        24         |
|       452,724       |        24         |
|          7          |        2          |
|         30          |        8          |
|       66,251        |        4          |
| 562,281,183,612,351 |        24         |
|    7,855,947,976    |        8          |
| 82,940,375,257,302  |        64         |
|    8,408,979,082    |        48         |
| 56,361,505,554,770  |        32         |

Here's an excerpt from the Divisibility section info about an input number that has a high amount of factors:

> For the CLI and GUI versions, an example of an input number with a high number of factors is
> 9,736,008,432,870,720, or 9 quadrillion 736 trillion ... This number is the product of 2<sup>6</sup> 
> and the next 12 prime numbers so it has 13 unique prime factors and its PF is
> $2^6 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \times 37 \times 41$.
> It has 28,672 total factors!

</details>


#### Code Duplication

A problem with early versions of this project was code duplication. For example, in the `NTPCLI` class (now
called `NtpCli`), in the
[commit from October 16<sup>th</sup>, 2020](https://github.com/ncschroeder/Number-Theory-Playground/commit/31b9e4f05cc396f8ba05a49c4146717e2c468a23),
there was a static method for each section that was used to interact with the user for that section. There's a
**lot** of code that's common to each of these methods. The collapsible section below shows an overview of what
each method looked like. The min and max input numbers were hardcoded and I replaced these with `x` and `y`,
respectively. I also replaced some code with comments and some string text with `...`. Here's a
[GitHub link to the whole `NTPCLI` class at the time of this commit](https://github.com/ncschroeder/Number-Theory-Playground/blob/31b9e4f05cc396f8ba05a49c4146717e2c468a23/NTPCLI.java).

<details>
<summary>Method Overview</summary>

```java
while (true) {
    System.out.println(
        "\nType one of the following and press enter:" +
        "\nA number to ... This number should be greater than or " +
        "equal to x and less than or equal to y" +
        "\n\"r\" to generate a random number and ..." +
        "\n\"i\" for information about ..." +
        "\n\"m\" to go to the menu"
    );
    int number;
    String input = inputReader.nextLine();
    switch (input) {
        case "m": return;
        
        case "i":
            // Print info
            break;

        case "r":
            // Generate a random number that is greater than or equal to x and less than or equal to y.
            number = Math.max(x, random.nextInt(y));
            // Print answer
            break;

        default:
            // If the user typed something that is not one of the above options, check if it's a number
            // in the valid range and display the appropriate calculations if it is.
            try {
                number = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("\nInvalid input");
                break;
            }
            if (number >= x && number <= y) {
                // Print answer
            } else {
                System.out.println("\nInvalid input");
            }
            break;
    }
}
```

</details>

This is fixed in the current version by having `Section` objects that have the data and functionality necessary
for doing all of the things in the code above. As a result, the one place in the code for interacting with a
user for a section is the `goToSection` method, as can be seen in the collapsible section below:

<details>


</details>

In addition, as mentioned above, the min and max input numbers were hardcoded and, as can be seen in the code,
were used multiple times. 1 billion (1,000,000,000) was the max input number for a few sections so the int
literal `1_000_000_000` was used multiple times for those sections. What if I typed out that int literal
correctly the 1<sup>st</sup> time but then accidentally typed `1_000_00_000` the 2<sup>nd</sup> time? Subtle
bugs would've happened. :bug: :lady_beetle: :ant: :cricket: This is fixed in the current version by having
`MIN_INPUT` and `MAX_INPUT` constants in the outer section classes and by having these max input constants in
the `Misc` class:

```java
public static final long ONE_POINT_FIVE_MILLION = 1_500_000;
public static final long FIVE_HUNDRED_BILLION = 500_000_000_000L;
public static final long TEN_TRILLION = 10_000_000_000_000L;
public static final long ONE_QUADRILLION = 1_000_000_000_000_000L;
public static final long FIVE_QUADRILLION = ONE_QUADRILLION * 5;
public static final long TEN_QUADRILLION = ONE_QUADRILLION * 10;
public static final long NINE_QUINTILLION = 9_000_000_000_000_000_000L;
```

Another improvement made to this code that reduces repetition is the use of `String.join` to create the options string to avoid having to type out so many `\n`s, as can seen in the snippet above.
