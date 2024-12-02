# Number Theory Playground &nbsp;&nbsp; :heavy_plus_sign: &nbsp; :heavy_minus_sign: &nbsp; :heavy_multiplication_x: &nbsp; :heavy_division_sign:

*Number theory* is a branch of math that involves the study of *integers*, which are whole numbers that can be positive, negative, or 0. The Number Theory Playground is an application where a user can navigate among sections that give info and do calculations for number theory concepts. Only positive integers are used in these calculations. The calculations are done based on input numbers provided by the user. Some sections do calculations that require 1 input number and some sections require 2 input numbers. Plenty of info about the sections can be found in the "All Sections" section.

There are 3 versions of this application: Java command line interface (CLI), Java graphical user interface (GUI), and website. The Java versions have been refactored recently and the website version hasn't. The Java versions share a lot of code, namely the code for the calculations and text that gets displayed. The Java versions have some unit testing. The JUnit testing framework was used. The code for the tests is in the "Tests" folder located within the "Java Versions" folder. The Java GUI version was made with the Swing library. The files for the Java versions are copies of the files in an IntelliJ IDEA project of mine.


## App Overview

Here are some collapsible sections that show some info and screenshots for the different versions.

<details>
<summary>Command Line Interface Info</summary>

The `NTPCLI` class has the `main` method and some other code for running this version of the app. If you're wondering, I was using the IntelliJ IDEA terminal with the Darcula :vampire: theme when I took the screenshots below.

The app will start at a menu section that looks like this:

![CLI main menu](/Java%20Versions/screenshots/CLI%20menu.JPG)

A single input section:

![CLI single input section](/Java%20Versions/screenshots/CLI%20single%20input%20section.JPG)

A double input section:

![CLI double input section](/Java%20Versions/screenshots/CLI%20double%20input%20section.JPG)

Info about a section getting displayed:

![CLI section info](/Java%20Versions/screenshots/CLI%20section%20info.JPG)

An answer being displayed:

![CLI answer](/Java%20Versions/screenshots/CLI%20answer.JPG)

An invalid input message being displayed:

![CLI invalid input](/Java%20Versions/screenshots/CLI%20invalid%20input.JPG)

#### Input Remarks

- Leading and trailing whitespace is ignored for all input.
- Inputs involving letters are case-insensitive.
- For the input for the 2 numbers for the double input sections, the 2 numbers can be separated by any amount of whitespace.
- Any time section info, an answer, or an invalid input message are displayed, the options for the menu or section will be redisplayed.

</details>

<details>
<summary>GUI and Website Info</summary>

The GUI and website versions of the app are similar. The screenshots below show the GUI version. The `NTPGUI` class has the `main` method to launch the GUI app and a little of the code for running it. A lot of the other code for doing this is in the `MainPanel` class.

The app will start at a home section that looks like this:

![GUI main menu](/Java%20Versions/screenshots/GUI%20home.JPG)

A single input section:

![GUI single input section](/Java%20Versions/screenshots/GUI%20single%20input%20section.JPG)

A double input section:

![GUI double input section](/Java%20Versions/screenshots/GUI%20double%20input%20section.JPG)

An answer being displayed:

![GUI answer](/Java%20Versions/screenshots/GUI%20answer.JPG)

An invalid input message being displayed:

![GUI invalid input](/Java%20Versions/screenshots/GUI%20invalid%20input.JPG)

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

A *prime number*, or a *prime*, is an integer $\geq 2$ whose only integer factors are 1 and itself. A *composite number* is an integer $\geq 2$ that has an integer factor other than 1 and itself. The first 10 primes are 2, 3, 5, 7, 11, 13, 17, 19, 23, and 29. There are an infinite amount of them. The largest known prime is $2^{136,279,841} - 1$. It has 41,024,320 digits! Primes are used in 7 of the 10 sections in this application.

With the exception of 2 and 3, all primes are either 1 above or 1 below a multiple of 6. To show why this is the case, let's have a variable $i$ and let it represent any integer $\geq$ 6 that's a multiple of 6. We know that $i$ is divisible by 2 and 3 so $i + 2$ and $i + 4$ are divisible by 2 and $i + 3$ is divisible by 3 but we don't have any guarantees about what $i + 1$ and $i + 5$ are divisible by. Therefore, that's where primes can be.

An integer can be determined to be prime if it's not divisible by any primes $\leq$ the square root of that integer. This is called *trial division*. Let's determine if 29 and 33 are prime. $5^2 = 25$ and $6^2 = 36$ so $5 < \sqrt{29} < \sqrt{33} < 6$. We check if the numbers are divisible by 2, 3, or 5; which are the primes $\leq$ 5. 29 isn't divisible by any of those and 33 is divisible by 3 so 29 is prime and 33 isn't.

#### Calculation

Find the first 30 primes that are $\geq$ an input number.

#### Input Range: 0 - 10,000,000,000,000 (10 trillion)

</details>

<details>
<summary>Twin Prime Pairs</summary>

#### Info

A *twin prime pair* is a pair of prime numbers that differ by 2. The first 5 twin prime pairs are 3 & 5, 5 & 7, 11 & 13, 17 & 19, and 27 & 29. The largest known twin prime pair is $2,996,863,034,895 \times 2^{1,290,000} \pm 1$. They have 388,342 digits! It is conjectured that there are an infinite amount of twin prime pairs. A *conjecture* is a statement that is believed to be true but has not been proven to be.

All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so this means that all twin prime pairs besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and another number that is 1 above that same multiple of 6. 5 is the only number to be in 2 twin prime pairs.

#### Calculation

Find the first 20 pairs of twin primes where the lowest number in the pair is $\geq$ an input number. For example, if the input number is 3, then the pair 3 and 5 will be the first one found since the lowest number in that pair is 3. If the input number is 4, then the pair 5 and 7 will be the first one found.

#### Input Range: 0 - 500,000,000,000 (500 billion)

</details>

<details>
<summary>Prime Factorization</summary>

#### Info

The Fundamental Theorem of Arithmetic says that every integer $> 1$ can be expressed as the product of prime numbers. The *prime factorization* (PF) of an integer is an expression of the prime numbers whose product is that integer. For example, the PF of 5 is just $5$, the PF of 25 is $5^2$, and the PF of 12,250 is $2 \times 5^3 \times 7^2$. There are some interesting applications for this. See the info for the "Divisibility" or "GCD and LCM" sections for some applications.

The input integer with the highest amount of prime factors is $2^53$, or 9,007,199,254,740,992; the largest power of 2 $\leq$ 10 quadrillion. The input integer with the highest amount of *unique* prime factors is 304,250,263,527,210. This integer is the product of the first 13 prime numbers so it has 13 unique prime factors and its PF is $2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \times 37 \times 41$. You could also multiply that integer by 2 or 3 and those integers are $\leq$ the max input and have the same amount of unique prime factors.

#### Calculation

Find the PF of an input number faster than you can say "prime factorization". :slightly_smiling_face:

#### Input Range: 2 - 10,000,000,000,000,000 (10 quadrillion)

</details>

<details>
<summary>Divisibility &nbsp; :heavy_division_sign:</summary>

#### Info

Say we have 2 integers that we'll represent with the variables $a$ and $b$. If we divide $a$ by $b$ and get no remainder, then $a$ is said to be *divisible* by $b$ and $b$ is said to be a *factor* or *divisor* of $a$. If you want to find some factors of an integer, you could manually do some division to find all of them but there are other ways to find them.

The factors of an integer can be found by looking at its prime factorization (PF). Let's have a variable $i$ and let it represent an integer $>$ 1. First, you can find how many factors $i$ has by looking at $i$'s PF, taking all the powers of the factors, adding 1 to each, and then multiplying all these together. For example, the PF of 36 is $2^2 \times 3^2$. The powers are 2 and 2, so there are $3 \times 3 = 9$ factors. However, that count includes 1 and the number that the PF is for (36 in this case). If you want to exclude those, then subtract 2. That would give us 7 factors. You can find the factors of $i$ by finding all the PFs within $i$'s PF, or the *sub-factorizations*, as I like to call them. For $2^2 \times 3^2$, the sub-factorizations are $2$, $3$, $2^2 (4)$, $2 \times 3 (6)$, $3^2 (9)$, $2^2 x 3 (12)$, and $2 x 3^2 (18)$.

Some rules can be used to find some of the factors of an integer. I'll go over 1 rule for each integer in the range of 3 to 12, excluding 5 and 10, though there are rules for more integers and many integers have multiple rules. I'll go over an example of using these rules to find the factors of an integer in the next paragraph. Let's have a variable $i$ and let it represent an integer. If the last 2 digits of $i$ is divisible by 4, then $i$ is divisible by 4. If the last 3 digits of $i$ is divisible by 8, then $i$ is divisible by 8. If the sum of the digits of $i$ is divisible by 3, then $i$ is divisible by 3. If the sum of the digits of $i$ is divisible by 9, then $i$ is divisible by 9. If $i$ is even and divisible by 3, then it's also divisible by 6. If $i$ is divisible by both 3 and 4, then it's also divisible by 12. For 7, we split the integer into blocks of 3 from right to left. Coincidentally, these are the blocks separated by commas if we write the integer with commas. We do an alternating sum of the blocks from right to left. We start with 0, add the last block, subtract the 2<sup>nd</sup> to last block, add the 3<sup>rd</sup> to last block, and so on for all the blocks. If this alternating sum is divisible by 7, then $i$ is divisible by 7. For 11, we do an alternating sum of digits from left to right. We start with 0, add the 1<sup>st</sup> digit, subtract the 2<sup>nd</sup> digit, add the 3<sup>rd</sup> digit, and so on for all digits. If this alternating sum is divisible by 11, then $i$ is divisible by 11.

Here's an example. Let $i$ be 4,695,768. The PF of $i$ is $2^3 \times 3^2 \times 7^2 \times 11^3$. We can tell from that PF that $i$ is divisible by all the integers mentioned above. Let's check using the rules. The last 2 digits are 68, which is divisible by 4. The last 3 digits are 768, which is divisible by 8. The sum of the digits is 45, which is divisible by 3 and 9. Since $i$ is even and divisible by 3, it's also divisible by 6. Since $i$ is divisible by both 3 and 4, it's also divisible by 12. The alternating sum of blocks of 3 from right to left is $768 - 695 + 4 = 77$, which is divisible by 7. The alternating sum of digits from left to right is $4 - 6 + 9 - 5 + 7 - 6 + 8 = 11$, which, of course, is divisible by 11.

#### Calculations

1. Use the special tricks to see if we can find some factors of an input number and build a paragraph that says info from this.
2. Find the PF of the input number. If we can determine from this PF that the input number is composite, then manually find the factors and their PFs and show that they are sub-factorizations of the PF of the input number.

#### Input Range: 2 - 10,000,000,000,000,000 (10 quadrillion)

</details>

<details>
<summary>Greatest Common Divisor (GCD) and Least Common Multiple (LCM) &nbsp; :heavy_division_sign: &nbsp; :heavy_multiplication_x:</summary>

#### Info

Greatest common divisor is also known as greatest common factor, or GCF. To find the GCD and LCM of 2 numbers, you could manually do some division and multiplication but there are some other ways to find them.

The Euclidean algorithm can be used to find the GCD of 2 numbers. This algorithm was named after the ancient Greek mathematician Euclid. For this algorithm, first take 2 numbers. If the bigger number is divisible by the smaller number, then the smaller number is the GCD. Otherwise, the GCD of the 2 numbers is the same as the GCD of the smaller number and the remainder when the bigger number is divided by the smaller number. Repeat.

The GCD and LCM of 2 integers can be found by looking at their prime factorizations (PFs). If those integers don't have any common prime factors, then the GCD is 1. If they do have common prime factors, then the GCD PF consists of all the common prime factors and the power of each factor is the min of the powers of that factor in the 2 PFs. The LCM PF consists of all factors that are in either of the PFs of the 2 integers. If a factor is in both PFs then the power of that factor in the LCM PF is the max of the powers of that factor in the 2 PFs. If a factor is unique to one of the PFs then that factor and its power are in the LCM PF.

Let's find the GCD and LCM of 6 and 35 using their PFs. The PF of 6 is $2 \times 3$ and the PF of 35 is $5 \times 7$. There are no common prime factors so the GCD is 1. The LCM PF is $2 \times 3 \times 5 \times 7$, which is 210.

Let's find the GCD and LCM of 54 and 99. The PF of 54 is $2 \times 3^3$ and the PF of 99 is $3^2 \times 11$. 3 is the only common prime factor and the min power of it is 2 so the GCD PF is $3^2$, which is 9. The max power of 3 is 3 so the LCM PF consists of $3^3$. The LCM PF is $2 \times 3^3 \times 11$, which is 594.

The input integers whose LCM is the highest are 5,000,000,000,000,000, the max input, and 4,999,999,999,999,999, the max input $-$ 1. The LCM is 24,999,999,999,999,995,000,000,000,000,000, or 24 nonillion 999 octillion 999 septillion 999 sextillion 999 quintillion 995 quadrillion! It has 32 digits. Trillion is before quadrillion.

A pair of input integers whose LCM might have the highest amount of unique prime factors is 304,250,263,527,210, the product of the 1<sup>st</sup> 13 prime numbers, and 133,869,006,807,307, the product of the next 8 prime numbers. The LCM is 40,729,680,599,249,024,150,621,323,470, or 40 octillion ... It has 29 digits and 21 unique prime factors and its PF is $2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \times 37 \times 41 \times 47 \times 53 \times 59 \times 61 \times 67 \times 71 \times 73$! Other pairs of input integers have the same LCM, such as that 1<sup>st</sup> input integer divided by 2 and the 2<sup>nd</sup> input integer multiplied by 2.

#### Calculations

1. Perform the Euclidean algorithm on 2 input numbers and display a table with info about all iterations. Each iteration has a max number, min number, and remainder when the max is divided by the min.
2. Find the PFs of the input numbers and use these to find the PFs of the GCD and LCM. Display a table with all 4 numbers and their PFs.

#### Input Range: 2 - 5,000,000,000,000,000 (5 quadrillion)

</details>

<details>
<summary>Goldbach Conjecture</summary>

#### Info

The Goldbach Conjecture says that every even number $\geq 4$ can be expressed as the sum of 2 prime numbers. This was named after 1700s Prussian mathematician Christian Goldbach. A *conjecture* is a statement that is believed to be true but has not been proven to be true. The Goldbach Conjecture has been verified to be true for all even numbers $\geq 4$ and $\leq 4 \times 10^18$.

#### Calculation

Find the pairs of prime numbers that sum to an even input number.

#### Input Range: 4 - 1,500,000

</details>

<details>
<summary>Pythagorean Triples</summary>

#### Info

The Pythagorean Theorem says that for a right triangle, the sum of the squares of the lengths of the 2 short sides equals the square of the long side (hypotenuse) length, or $a^2 + b^2 = c^2$. This theorem was named after the ancient Greek mathematician Pythagoras. A *Pythagorean triple* is a triple of integers that $a$, $b$, and $c$ can be. For example; 3, 4, and 5 is a Pythagorean triple since $3^2 (9) + 4^2 (16) = 5^2 (25)$ and 11, 60, and 61 is another one since $11^2 (121) + 60^2 (3,600) = 61^2 (3,721)$.

Once we know a Pythagorean triple, we can form another one by multiplying $a$, $b$, and $c$ by the same positive integer. Because of this, there are an infinite amount of Pythagorean triples. A Pythagorean triple is considered to be *primitive* if the GCD of $a$, $b$, and $c$ is 1. Therefore, a primitive triple can't be formed by taking another triple and multiplying $a$, $b$, and $c$ by something. The triples mentioned above; 3, 4, and 5, and 11, 60, and 61; are primitive. 6 ($3 \times 2$), 8 ($4 \times 2$), and 10 ($5 \times 2$) is another triple. $6^2 (36) + 8^2 (64) = 10^2 (100)$. 55 ($11 \times 5$), 300 ($60 \times 5$), and 305 ($61 \times 5$) is another one. $55^2 (3,025) + 300^2 (90,000) = 305^2 (93,025)$.

#### Calculation

Find the first 10 Pythagorean triples where the lowest number in the triple is $\geq$ an input number. For example, if the input number is 3, then the triple 3, 4, and 5 will be the first one found since the lowest number in that triple is 3. If the input number is 4, then the triple 5, 12, and 13 will be the first one found. These triples will be displayed similarly to how the examples at the end of the paragraphs in the "Info" section above are displayed. If a triple is primitive, then it will be followed by "(primitive)".

#### Input Range: 0 - 10,000

</details>

<details>
<summary>Two Square Theorem</summary>

#### Info

The Two Square Theorem says that every prime number that is 1 above a multiple of 4 can be expressed as the sum of 2 square numbers. A *square number* is a number that can be formed by taking a number and multiplying it by itself, or squaring it. The first few square numbers are $1 (1^2), 4 (2^2), and 9 (3^2)$. 29 is prime and is 1 above $28 (4 \times 7)$, and can be expressed as $2^2 (4) + 5^2 (25)$.

#### Calculations

Find the first prime number $\geq$ an input number that is 1 above a multiple of 4, as well as the numbers whose squares sum to that number.

#### Input Range: 0 - 1,000,000,000,000,000 (1 quadrillion)

</details>

<details>
<summary>Fibonacci-like Sequences</summary>

#### Info

I consider a number sequence to be "Fibonacci-like" if it starts with 2 numbers and has every following number be the sum of the 2 previous numbers. The Fibonacci sequence does this and has 1 and 1 as its first 2 numbers. Fibonacci was a mathematician from the 1100s to 1200s from modern-day Italy. Another Fibonacci-like sequence is the Lucas sequence, which has 2 and 1 as its first 2 numbers. This sequence was named after 1800s French mathematician Francois Edouard Anatole Lucas.

The *Golden Ratio* is an irrational number symbolized by the Greek letter $\Phi$ (Phi). $\Phi = \frac{1 + \sqrt{5}}{2} \approx 1.618$. As we advance further and further into a Fibonacci-like sequence, the ratio between a number and the number before it gets closer and closer to $\Phi$. For example, the first 8 numbers of the Fibonacci sequence are 1, 1, 2, 3, 5, 8, 13, and 21. $\frac{2}{1} = 2$. $\frac{8}{5} = 1.6$. $\frac{21}{13} \approx 1.615$.

#### Calculations

Find the first 20 numbers of the Fibonacci-like sequence that starts with 2 input numbers, as well as the ratios between the 5<sup>th</sup> and 4<sup>th</sup>, 10<sup>th</sup> and 9<sup>th</sup>, 15<sup>th</sup> and 14<sup>th</sup> and 20<sup>th</sup> and 19<sup>th</sup> numbers.

#### Input Range: 1 - 9,000,000,000,000,000,000 (9 quintillion)

</details>

<details>
<summary>Ancient Egyptian Multiplication</summary>

#### Info

The ancient Egyptians had an interesting algorithm for multiplication. My way of explaining the algorithm goes like this:
1. Let variable $a$ represent one of the numbers and variable $b$ represent the other number.
2. Find all powers of 2 that are $\leq a$. This could be done without modern multiplication by starting with 1, the 1st power of 2 or $2^0$, and finding the next power by adding the previous power to itself. This process will look like: $1 + 1 = 2 (2^1), 2 + 2 = 4 (2^2), 4 + 4 = 8 (2^3)$, and so on until we find a power that's $> a$, which we won't use.
3. Find the products of $b$ and these powers of 2. Like with the powers of 2, this could be done by starting with $b$ and finding the next product by adding the previous product to itself. If we let $b$ be 5, this process will look like: $5 + 5 = 10 (5 \times 2)$, $10 + 10 = 20 (5 \times 4)$, $20 + 20 = 40 (5 \times 8)$, and so on.
4. Find the powers of 2 that sum to $a$.
5. Add up the products of $b$ and these powers.

This gives us the product of the 2 numbers.

Let's find the product of 5 and 12. Let's first use 5 for the number represented by $a$ in the algorithm above and 12 for $b$. The powers of 2 $\leq$ 5 are 1, 2, and 4. The products of 12 and these powers are 12, 24, and 48. The powers of 2 that sum to 5 are 1 and 4. The products of 12 and these powers are 12 and 48. $12 + 48 = (12 \times 1) + (12 \times 4) = 12 \times (1 + 4) = 60$. Now let's use 12 for $a$ and 5 for $b$. The powers of 2 $\leq$ 12 are 1, 2, 4, and 8. The products of 5 and these powers are 5, 10, 20, and 40. The powers of 2 that sum to 12 are 4 and 8. The products of 5 and these powers are 20 and 40. $20 + 40 = (5 \times 4) + (5 \times 8) = 5 \times (4 + 8) = 60$.

#### Calculations

Given 2 input numbers:
1. Find the powers of 2 $\leq$ the 1<sup>st</sup> input number and the corresponding multiples of the 2<sup>nd</sup> input number. Display these in a table.
2. Find the powers of 2 that sum to the 1<sup>st</sup> input number and the corresponding multiples of the 2<sup>nd</sup> input number. Display these in another table.

This process will be done faster than you can say "ancient Egyptian multiplication". :slightly_smiling_face:

#### Input Range: 2 - 9,000,000,000,000,000,000 (9 quintillion)

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

## Project History

During the summer of 2019, I took a number theory course on [brilliant.org](https://brilliant.org/) for fun and was enlightened by some of the stuff I was learning. Later that year, I was thinking of what kind of programming projects I could work on and some of the stuff I learned from that number theory course came to mind. I realized I could make a program that makes calculations about some of the stuff I learned. Along with the content of that number theory course, I also included additional number theory concepts that I was familiar with. I learned these other concepts from sources such as my discrete math textbook and YouTube videos from channels such as Numberphile, Mathologer, 3blue1brown, and Zach Star. I decided to use Java because it's a popular programming language that I had no familiarity with at the time. However, I found Java easy to learn since some of the syntax is similar to C++, which I had some familiarity with. In fact, the 3 languages I had the most familiarity with at the time were Python :snake:, C++, and JavaScript.

I started in December 2019 and from then to October 2020, I was on and off this project. I had my last normal semester of college from January to May. Well, it was normal until the middle of March. I then had an independent study course over the summer where I worked on my Singletonopoly project. The first completed version of this project was uploaded to GitHub in October. This included a Java command line interface and graphical user interface. This included the first 7 of the 10 sections that are in the application now, starting with the prime numbers section and ending with the Pythagorean Triples section.

I think it was around this time (October 2020) when I decided to make a website version. I used the Pug template engine for the HTML and vanilla JavaScript and CSS. I used Node.js and Express for the back end. These are the same tools that I used for a web development course. I originally had the calculations done on the front end but moved these to the back end. For this website version, I included the 7 sections that were in the Java versions and also added 3 more sections, the Two Square Theorem, Fibonacci-like sequences, and ancient Egyptian multiplication sections. These would later be added to the Java versions.

Refactoring was done and other changes were made throughout 2021, 2022, and 2023. Unit tests were created for the Java versions in 2022.

### A Refactoring Example

1 problem with early versions of this project was code duplication. For example, in the `NTPCLI` class, there was a static method for each section that was used to interact with the user for that section. There's a **lot** of code that's common to each of these methods. Here's an overview of what each method looked like. The min and max input numbers were hardcoded and I replaced these with `x` and `y`, respectively. I also replaced some code with comments and some string text with `...`.

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

This is fixed in the modern version by having the `Section` objects that have the data and functionality necessary for doing all of the things in the code above. As a result, the 1 place in the code for interacting with a user for a section is the `goToSection` method, as can be seen in this snippet:

https://github.com/ncschroeder/Number-Theory-Playground/blob/3d3a77066b737b35d9a9018e1b5deec0f7e7124b/Java%20Versions/NTPCLI.java#L101-L198

In addition, as mentioned above, the min and max input numbers were hardcoded and used multiple times. 1,000,000,000 (1 billion) is the max input number for a few sections so the int literal `1_000_000_000` was used multiple times for those sections. The max value for an int is 2 billion something so that means that 1 billion has the max number of digits for an int. What if I typed out that int literal correctly the 1st time but then accidentally typed `1_000_00_000` the 2nd time? Subtle bugs would've happened. :bug: :lady_beetle: :ant: :cricket: This is fixed in the modern version by having int constants named `oneThousand`, `tenThousand`, `oneHundredThousand`, and `oneBillion` in the `Misc` class and having `minInputInt` and `maxInputInt` variables in the outer section classes.

Another improvement made to this code that reduces repetition is the use of `String.join` to create the options string to avoid having to type out so many `\n`s, as can seen in the snippet above.
