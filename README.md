# Number Theory Playground &nbsp;&nbsp; :heavy_plus_sign: &nbsp; :heavy_minus_sign: &nbsp; :heavy_multiplication_x: &nbsp; :heavy_division_sign:

*Number theory* is a branch of math and is the study of integers. Let's go over a few types of numbers. *Natural numbers* are 1, 2, 3, and so on. *Whole numbers* are the natural numbers along with 0. *Integers* are the whole numbers along with negative natural numbers; so ..., -2, -1, 0, 1, 2, ...

The *Number Theory Playground* (NTP) is an application where a user can visit sections that show info and do calculations for number theory concepts. These calculations are done based on input numbers provided by the user. Some sections require 1 input number and some require 2. An example of a section is the prime numbers section. It has 17 sentences of info and can find the first 30 prime numbers that are ≥ an input number. Plenty of info about the sections can be found in the ["All Sections" section](#all-sections).

Almost all of the calculations involve only natural numbers. There are 2 exceptions to this:

1. The divisibility section does calculations that might involve negative integers or 0.
2. The Fibonacci-like sequences section does calculations where the result is a floating-point number.

The term "whole number" is often used in this README and the app since I find it to be more self-explanatory than "natural number."

<br/>


## Collapsible Sections

In case it's not obvious, the black triangles or play buttons below are where collapsible sections are and they can be expanded and collapsed by clicking on the triangle or to the right of it.

<br/>


## App Versions

There are 5 versions of this app. 4 of them were made entirely by me: Java command line interface (CLI), Java graphical user interface (GUI), Java and Spring website, and Node website. I used AI to make another Java CLI version, which is mentioned in the next section. The Node website version hasn't been worked on for years and there aren't many notable things to say about it, so it's mentioned very little throughout this README. Any time I mention the website version, I'm referring to the Spring website version.

### AI CLI Version

I used Claude to make another CLI version and unit tests for it. I had it generate most of the code and documentation. In addition, I also had it come up with an overview of the app and info for the sections. For the sections info, I prompted it to come up with 5 sentences or less. The overview it generated is below and the sections info is in the ["All Sections" section](#all-sections) in the "AI-Generated Info" collapsible sections below the info I came up with for each section.

<details>
<summary>AI-Generated Overview of the App</summary>
<br/>

Number theory is the branch of mathematics concerned with the integers and the relationships between them: primes, divisibility, factorization, and the patterns they form. It is one of the oldest parts of mathematics, studied for its elegance for millennia before it became the foundation of modern cryptography, computing, and coding theory.

The Number Theory Playground is an interactive tour of these ideas. Each section takes a number or two and works a classic construction out in full — the primes above a value, a number's prime factorization and divisors, GCD and LCM, Goldbach pairs, Pythagorean triples, and more. At any input prompt you can enter 'i' for background on that topic, 'r' for a random input, or 'm' to return to this menu. Choose an option above to begin exploring.

</details>

<details>
<summary>A Mistake Made by Claude</summary>
<br/>

Unsurprisingly, Claude made some mistakes. For example, for the GCD and LCM section, one of the calculations is finding the GCD and LCM of 2 numbers by using the prime factorizations (PFs) of those numbers. A method used as part of that is:

```java
static BigInteger product(List<FactorAndExponent> fes) {
    var result = BigInteger.ONE;
    for (FactorAndExponent fe : fes) {
        result =
            result.multiply(
                BigInteger.valueOf(fe.factor())
                .pow(fe.exponent())
            );
    }
    return result;
}
```

The max input of the GCD and LCM section is 5 quadrillion (5,000,000,000,000,000). I used the same input constraints for the AI version as I did for the CLI and GUI versions that I made myself. It's possible that the LCM of 2 input numbers can exceed the max value of a `long`. I knew that and used a `BigInteger` for this situation in the CLI and GUI versions that I made myself. The method above needs to return a `BigInteger` but Claude originally had that method return a `long`. I went through the `history.jsonl` file and saw that I prompted Claude to create a section where the user can enter 2 input integers and the Euclidean algorithm will be done on them and the PFs of them will be calculated and those PFs will be used to find the GCD and LCM and that the input integers can be ≥ 2 and ≤ 5 quadrillion. Claude wrote some code, including the method above, without realizing that a `BigInteger` needs to be used.

</details>

<br/>


## App Overview

There isn't any software design or implementation info here but the ["Some Design and Implementation Info" section](#some-design-and-implementation-info) has that.

### Website and GUI

<details>
<summary>Info</summary>
<br/>

The website and GUI versions of the app are similar. The screenshots below show the website version.

A single page application is used for the website version. Only 1 HTML document gets rendered and the content of  this document gets changed when a section changes, an answer is displayed, or an invalid input message is displayed. When a user enters a valid input number(s) and clicks the "Calculate" button, an HTTP request is made to the server, which does the calculations. This means that an internet connection is required to be able to do those calculations.

The app starts at a home section:

![Website home](/Spring_Website_Version/screenshots/app_overview/home.JPG)

The user can visit sections using the buttons at the top.

A single input section without info:

![Website single input section without info](/Spring_Website_Version/screenshots/app_overview/single_input_section_without_info.JPG)

The info for the section can be shown and hidden by clicking "Info" or the triangle to the left of that.

A single input section with info:

![Website single input section with info](/Spring_Website_Version/screenshots/app_overview/single_input_section_with_info.JPG)

A double input section:

![Website double input section](/Spring_Website_Version/screenshots/app_overview/double_input_section.JPG)

The user can enter input in the text fields or can change the text of one by clicking one of the buttons below it. As shown in the screenshot, commas are optional. Below, when I'm talking about numbers in the text fields, I mean numbers optionally with commas.

Clicking the "Randomize" button will generate a random input number and put it in the text field. First, a valid random number of digits will be generated and then a valid random number with that number of digits will be generated.

For the "+" and "−" buttons, nothing will happen if any of these are clicked in these situations:
- The text field has something other than a whole number. Recall that the whole numbers are 0, 1, 2, 3, and so on.
- Website version: the text field has a number > the max value of a JavaScript safe integer (2<sup>53</sup> − 1, or 9,007,199,254,740,991, or 9 quadrillion 7 trillion ...).
- GUI version: the text field has a number > the max value of a Java `long` (2<sup>63</sup> − 1, or 9,223,372,036,854,775,807, or 9 quintillion 223 quadrillion ...).

Otherwise:

Clicking the "+" button will have one of these effects:
- If the text field has a number < the max input number, then the text field will have its text set to the next highest valid input number. For the Goldbach conjecture section, this'll be the next highest even number and for all other sections, this'll be the next highest number.
- If the text field is blank or has a number ≥ the max input number, then the text field will have its text set to the min input number.

Clicking the "−" button will have one of these effects:
- If the text field has a number > the min input number, then the text field will have its text set to the next lowest valid input number. For the Goldbach conjecture section, this'll be the next lowest even number and for all other sections, this'll be the next lowest number.
- If the text field is blank or has a number ≤ the min input number, then the text field will have its text set to the max input number.

Some input and a calculation:

![Website input and calculation](/Spring_Website_Version/screenshots/app_overview/input_and_calculation.JPG)

More example calculations screenshots can be seen in the ["All Sections" section](#all-sections) below.

Invalid input:

![Website invalid input](/Spring_Website_Version/screenshots/app_overview/invalid_input.JPG)

<br/>
</details>

### CLI

<details>
<summary>Info</summary>
<br/>

I used the IntelliJ IDEA terminal with the Darcula :vampire: theme for these screenshots.

The app starts at a main menu:

![CLI main menu](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/main_menu.JPG)

Leading and trailing whitespace is ignored for all input. Letter inputs are case-**in**sensitive.

Number Theory Playground info:

![CLI NTP info](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/NTP_info.JPG)

Any time info, calculations, or an invalid input message is displayed, the options for the menu or section get redisplayed.

A single input section:

![CLI single input section](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/single_input_section.JPG)

A double input section:

![CLI double input section](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/double_input_section.JPG)

Info about a section:

![CLI section info](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/section_info.JPG)

Single input section typed input and calculation:

![CLI single input section typed input and calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/single_input_section_typed_input_and_calculation.JPG)

Random input and calculation:

![CLI random input and calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/random_input_and_calculation.JPG)

Random input numbers are generated by first generating a valid random number of digits and then generating a valid random number with that number of digits.

For the double input sections, typed input numbers can be separated by any amount of whitespace:

![CLI double input section typed input and calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/double_input_section_typed_input_and_calculations.JPG)

More example calculations screenshots can be seen in the ["All Sections" section](#all-sections) below.

Invalid input:

![CLI invalid input](/Java_CLI_and_GUI_Versions/CLI_screenshots/app_overview/invalid_input.JPG)

</details>

<br/>


## All Sections

Shown in the sections below (starting with the "Prime Numbers" section) is info about all sections featured in the app, including info about the number theory concept, what can be calculated and displayed, input constraints, and example calculation(s) screenshots. A few ideas I have for new sections are for [sexy primes](https://en.wikipedia.org/wiki/Sexy_primes) 👙, [sphenic numbers](https://en.wikipedia.org/wiki/Sphenic_number), and the [4-square theorem](https://en.wikipedia.org/wiki/Lagrange%27s_four-square_theorem). The main reason I want to make a sexy primes section is because of its name. 🙂

<details>
<summary>Mathematical Expressions and Unicode Chars</summary>
<br/>

There are some mathematical expressions featured in this README that can be written with GitHub Markdown. The Spring website version webpage uses MathML for some mathematical expressions. For example, in the concept info for the prime numbers section; $\sqrt{29}$, the square root of 29, is used. This can be written using `$\sqrt{29}$` in the Markdown for this README and `<math><msqrt><mn>29</mn></msqrt></math>` using MathML. Here's a [*GitHub Docs* article on writing mathematical expressions](https://docs.github.com/en/get-started/writing-on-github/working-with-advanced-formatting/writing-mathematical-expressions) and here's an [MDN article on MathML](https://developer.mozilla.org/en-US/docs/Web/MathML).

There are also some Unicode chars for math symbols such as ≥ and × that were copied and pasted into this README and the source code. × is used for multiplication and is different from the letter x or X. A Unicode char for the Greek letter 𝚽 (Phi) is used for the Fibonacci-like sequences concept info.

</details>

<details>
<summary>Max Input Info</summary>
<br/>

The calculations for the Fibonacci-like sequences and ancient Egyptian multiplication sections are pretty cheap, like so cheap that I was able to have the input numbers be a googol (10<sup>100</sup>) and the calculations were done instantly. For the CLI and GUI versions, I decided to have the max inputs for these sections be 9 quintillion (9,000,000,000,000,000,000), which is a little lower than the max value of a Java `long`, which is 2<sup>63</sup> − 1 or 9,223,372,036,854,775,807 (9 quintillion 223 quadrillion ...). For the website version, I decided to have the max inputs be 1 billion (1,000,000,000), which is a lot lower than the max value of a JavaScript safe integer, which is 2<sup>53</sup> − 1 or 9,007,199,254,740,991 (9 quadrillion ...). 1 billion is pretty high and a benefit to having that be the max inputs is that I can use `long`s to do the calculations on the back end and don't need to use `BigInteger`s.

For other sections in the CLI and GUI versions, I chose the max inputs such that the input(s) ≤ the max input that required the most work or close to the most work to be done would take a few seconds or so on my computer.

For those other sections in the website version, the max inputs are a lot lower. The calculations are done on a server and I'm not gonna use my computer for that server in production. 🙂 The server might be less powerful than my computer and might have more things to do than serve this website. Also, I don't expect this website to get many visitors but I still prepared for multiple visitors by carefully choosing max inputs.

I used the [`System.nanoTime` method](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/System.html#nanoTime()) to find out about how long it takes to do calculations with various input number sizes and chose the max inputs based on this. This, of course, is still affected by how powerful my computer is but I studied how much calculation times increased as the input increased. For example, the prime numbers section calculates the first 30 prime numbers that are ≥ an input number. It often takes about 2 milliseconds (ms) if the input is 0 or 1,000, 2.5-3 ms for 10,000 or 100,000, 3-3.5 ms for 1 million, and 3.5-4 ms for 10 million. 100,000 is pretty high and it only takes a little longer to do the calculation with that as the input than how long it takes to do it with 0, so I decided to have 100,000 be the max input.

</details>


### Prime Numbers

<details>
<summary>Info</summary>
<br/>

A *prime number*, or a *prime*, is a whole number > 1 that isn't divisible by any whole numbers other than 1 and
itself. A *composite number* is a whole number > 1 that is divisible by a whole number other than 1 and itself.
The first 10 primes are 2, 3, 5, 7, 11, 13, 17, 19, 23, and 29. There are an infinite amount of primes. The
largest known one is $2^{136,279,841} - 1 \text{.}$ It has 41,024,320 digits! Primes are used in 8 of the 11 sections in
the Number Theory Playground.

With the exception of 2 and 3, all primes are either 1 above or 1 below a multiple of 6. To show why this is the
case, let's have a variable $n$ and let it represent a whole number ≥ 6 that's a multiple of 6. We know that
$n$ is divisible by 2 and 3 so $n$ + 2 and $n$ + 4 are divisible by 2 and $n$ + 3 is divisible by 3 but we don't
have any guarantees about what $n$ + 1 and $n$ + 5 are divisible by. Therefore, that's where primes can be.

We can check if a whole number > 1 is prime by checking if it's divisible by any primes ≤ its square root. If it is, then it's not prime. This is called *trial division*. Let's check if 29 and 33 are prime. $5^2 = 25$ and $6^2 = 36$ so $5 < \sqrt{29} < \sqrt{33} < 6 \text{.}$ We check if 29 and 33 are divisible by 2, 3, or 5; which are the primes ≤ 5. 29 isn't divisible by any of those and 33 is divisible by 3 so 29 is prime and 33 isn't.

<details>
<summary>AI-Generated Info</summary>

A prime is an integer greater than 1 whose only positive divisors are 1 and itself. The primes begin 2, 3, 5, 7, 11, 13, 17, ..., and 2 is the only even prime. Euclid proved around 300 BC that there are infinitely many primes. The Prime Number Theorem says primes near n are spaced roughly ln(n) apart, so they thin out as numbers grow but never run dry.

</details>

#### Calculation

Find the first 30 primes that are ≥ an input number.

#### Input Constraints

Min: 0
<br/>
Website max: 100,000
<br/>
CLI and GUI max: 10 trillion (10,000,000,000,000)

#### Example Calculation Screenshots

##### Website

![Website primes calculation](/Spring_Website_Version/screenshots/section_calculations/primes_calculation.JPG)

##### CLI

![CLI primes calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/primes_calculation.JPG)

<br/>
</details>


### Semiprimes

<details>
<summary>Info</summary>
<br/>

A *semiprime*, also known as a *biprime*, is a number made by multiplying 2, possibly equal, prime numbers. The first 5 semiprimes and their prime number factors are 4 (2 × 2), 6 (2 × 3), 9 (3 × 3), 10 (2 × 5), and 14 (2 × 7). Since there are an infinite amount of prime numbers, there are also an infinite amount of semiprimes. The largest known semiprime is the square of the largest known prime number, which is $2^{136,279,841} - 1 \text{.}$

<details>
<summary>AI-Generated Info</summary>

A semiprime is a positive integer that is the product of exactly two primes; the two primes may be equal, as in 4 = 2 × 2 or 9 = 3 × 3. The first few semiprimes are 4, 6, 9, 10, 14, 15, 21, 22, 25, 26. Semiprimes underpin RSA cryptography: factoring a large semiprime n = p × q into its prime factors is believed to be computationally hard, and the security of RSA rests on that hardness.

</details>

#### Calculations

Find the first 20 semiprimes that are ≥ an input number, as well as their prime number factors.

#### Input Constraints

Min: 0
<br/>
Website max: 100,000
<br/>
CLI and GUI max: 50 trillion (50,000,000,000,000)

#### Example Calculation Screenshots

##### Website

![Website semiprimes calculation](/Spring_Website_Version/screenshots/section_calculations/semiprimes_calculation.JPG)

##### CLI

![CLI semiprimes calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/semiprimes_calculation.JPG)

<br/>
</details>


### Twin Prime Pairs

<details>
<summary>Info</summary>
<br/>

A *twin prime pair* is a pair of prime numbers that differ by 2. The first 5 twin prime pairs are
3 & 5, 5 & 7, 11 & 13, 17 & 19, and 27 & 29. The largest known twin prime pair is
$(2,996,863,034,895 \times 2^{1,290,000}) \pm 1 \text{.}$ They have 388,342 digits! The *twin prime conjecture* says that
there are an infinite amount of twin prime pairs. A *conjecture* is a statement that's believed to be true but
hasn't been proven to be.

All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so this means that all twin prime
pairs besides 3 and 5 consist of 1 number that's 1 below a multiple of 6 and another number that's 1 above that
same multiple of 6. 5 is the only number to be in 2 twin prime pairs, the first 2 mentioned above.

<details>
<summary>AI-Generated Info</summary>

A twin prime pair is two primes that differ by 2, such as (3, 5), (11, 13), or (29, 31). The Twin Prime Conjecture — that infinitely many such pairs exist — is one of the oldest open problems in number theory. In 2013, Yitang Zhang proved that infinitely many prime pairs differ by at most 70 million; that bound has since been reduced to 246. Apart from (3, 5), every twin prime pair has the form (6k − 1, 6k + 1).

</details>

#### Calculation

Find the first 20 twin prime pairs where the lowest number in the pair is ≥ an input number. For example, if the input number is 3, then the pair 3 and 5 will be the first one found. If the input number is 4, then the pair 5 and 7 will be the first one found.

#### Input Constraints

Min: 0
<br/>
Website max: 100,000
<br/>
CLI and GUI max: 500 billion (500,000,000,000)

#### Example Calculation Screenshots

##### Website

![Website twin prime pairs calculation](/Spring_Website_Version/screenshots/section_calculations/twin_prime_pairs_calculation.JPG)

##### CLI

![CLI twin prime pairs calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/twin_prime_pairs_calculation.JPG)

<br/>
</details>


### Prime Factorization

<details>
<summary>Info</summary>
<br/>

The fundamental theorem of arithmetic says that every whole number > 1 is either prime or can be expressed as the product of prime numbers in 1 way if you ignore the order of those prime numbers. The *prime factorization* (PF) of a whole number > 1 is an expression of that number if it's prime or the prime numbers whose product is that number. These prime numbers are factors of that number. For example, the PF of 3 is just $3 \text{,}$ the PF of 12 is $2^2 \times 3 \text{,}$ and the PF of 25,137 is $3^3 \times 7^2 \times 19 \text{.}$ 25,137 can also be expressed as $7^2 \times 3^3 \times 19$ but that's the same expression if you ignore the order of the factors. There are some interesting applications for PFs. See the info for the "Divisibility" or "GCD and LCM" sections for some applications.

For the website version, the input number with the most prime factors is 524,288 (2<sup>19</sup>), the largest power of 2 ≤ 1 million, the max input. The input number with the most *unique* prime factors is 510,510, which is the product of the first 7 prime numbers and has a PF of $2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \text{.}$

For the CLI and GUI versions, the input number with the most prime factors is 9,007,199,254,740,992 (2<sup>53</sup>), the largest power of 2 ≤ 10 quadrillion, the max input. The input number with the most *unique* prime factors is 304,250,263,527,210, which is the product of the first 13 prime numbers and has a PF of $2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \times 37 \times 41 \text{.}$ You could also multiply that number by a whole number ≤ 32 and the result would be ≤ the max input and have the same amount of unique prime factors.

<details>
<summary>AI-Generated Info</summary>

The Fundamental Theorem of Arithmetic states that every integer greater than 1 has a unique factorization into primes, up to the order of the factors. For example, 60 = 2² × 3 × 5. This factorization is the canonical fingerprint of an integer and underlies divisibility, GCD, LCM, and functions like Euler's totient. No fast classical algorithm is known for factoring large integers, which is why RSA-style cryptography remains secure.

</details>

#### Calculation

Find the PF of an input number faster than you can say "prime factorization." :slightly_smiling_face:

#### Input Constraints

Min: 2
<br/>
Website max: 1 million (1,000,000)
<br/>
CLI and GUI max: 10 quadrillion (10,000,000,000,000,000)

#### Example Calculation Screenshots

##### Website

![Website PF calculation](/Spring_Website_Version/screenshots/section_calculations/PF_calculation.JPG)

##### CLI

![CLI PF calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/PF_calculation.JPG)

<br/>
</details>


### Divisibility &nbsp; :heavy_division_sign:

<details>
<summary>Info</summary>
<br/>

Say we have 2 whole numbers that we'll represent with the variables $a$ and $b \text{.}$ If we divide $a$ by $b$ and get no remainder, then $a$ is *divisible* by $b$ and $b$ is  a *factor* or *divisor* of $a \text{.}$ If you want to find some whole number factors of a whole number, you could manually do some division but there are other ways to find them.

#### Prime Factorization

The factors of a whole number > 1 can be found by looking at its prime factorization (PF). Let's have a variable $n$ and let it represent a whole number > 1. First, you can find how many factors $n$ has by looking at $n$'s PF, taking the exponents of the factors, adding 1 to each, and multiplying them. For example, the PF of 36 is $2^2 \times 3^2 \text{.}$ The exponents are 2 and 2, so there are $3 \times 3 = 9$ factors. This amount includes 1 and the number that the PF is for (36 in this case). You can find the factors of $n$ by finding the PFs within $n$'s PF, or the *subfactorizations* as I like to call them. For $2^2 \times 3^2 \text{,}$ the subfactorizations are $2 \text{,}$ $3 \text{,}$ $2^2 \text{ (4),}$ $2 \times 3 \text{ (6),}$ $3^2 \text{ (9),}$ $2^2 \times 3 \text{ (12),}$ and $2 \times 3^2 \text{ (18).}$

Whole numbers that are ≤ 1 million, the max input of this section in the website version, generally have a small amount of factors, like < 50. An example of an input number with a high amount of factors is 510,510. This number is the product of the first 7 prime numbers so it has $2^7 = 128$ factors. Its PF is $2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \text{.}$

Whole numbers that are ≤ 10 quadrillion, the max input of this section in the CLI and GUI versions, still generally have a small amount of factors, like < 100. An example of an input number with a high amount of factors is 9,736,008,432,870,720 (9 quadrillion 736 trillion ...). This number is the product of 2<sup>6</sup> and the next 12 prime numbers so it has $7 \times 2^{12} = 28,672$ factors! Its PF is $2^6 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \times 37 \times 41 \text{.}$

#### Divisibility Rules

Some rules can be used to determine if a whole number is divisible by another whole number. I'll go over 1 rule for each number in the range of 3 to 15, excluding 5 and 10, though there are rules for more numbers and many numbers have multiple rules. I'll go over an example of using these rules to find the factors of a number in the "Example" section below. Let's have a variable $n$ and let it represent a whole number. If the number formed from the last 2 digits of $n$ is divisible by 4, then $n$ is divisible by 4. If the number formed from the last 3 digits is divisible by 8, then $n$ is divisible by 8. If the sum of the digits of $n$ is divisible by 3, then $n$ is divisible by 3. If the sum of the digits is divisible by 9, then $n$ is divisible by 9. If $n$ is even and divisible by 3, then it's also divisible by 6. If $n$ is divisible by both 3 and 4, then it's also divisible by 12. If $n$ is even and divisible by 7 (see rule below), then $n$ is divisible by 14. If $n$ is divisible by both 3 and 5, then it's also divisible by 15.

For 11, we do an alternating sum of the digits of $n$ from left to right. We start with 0, add the 1<sup>st</sup> digit, subtract the 2<sup>nd</sup> digit, add the 3<sup>rd</sup> digit, and so on for all the digits. If this sum is divisible by 11, then $n$ is divisible by 11.

For 7 and 13, we split $n$ into 3-digit blocks from right to left, though the leftmost block can contain 1 or 2 digits. Coincidentally, these are the blocks separated by commas if we write $n$ with commas. We do an alternating sum of the blocks from right to left. We start with 0, add the rightmost block, subtract the block to the left of that, add the block to the left of that, and so on for all the blocks. If this sum is divisible by 7, then $n$ is divisible by 7. If this sum is divisible by 13, then $n$ is divisible by 13. These alternating sums might involve negative integers or 0, so that makes them some of the few calculations done by the Number Theory Playground that involve numbers other than natural numbers.

##### Example

Let $n$ be 720,720. Its PF is $2^4 \times 3^2 \times 5 \times 7 \times 11 \times 13 \text{.}$ We can tell from that PF that $n$ is divisible by all the numbers that had rules mentioned about them above. Let's check using those rules. The last 2 digits form the number 20, which is divisible by 4. The last 3 digits form the number 720, which is divisible by 8. The sum of the digits is $7 + 2 + 0 + 7 + 2 + 0 = 18 \text{,}$ which is divisible by both 3 and 9. Since $n$ is even and divisible by 3, it's also divisible by 6. Since $n$ is divisible by both 3 and 4, it's also divisible by 12. Since $n$ is divisible by both 3 and 5, it's also divisible by 15. The alternating sum of the digits from left to right is $7 - 2 + 0 - 7 + 2 - 0 = 0 \text{,}$ which is divisible by 11. The alternating sum of 3-digit blocks from right to left is $720 - 720 = 0 \text{,}$ which is divisible by both 7 and 13. Since $n$ is even and divisible by 7, it's also divisible by 14.

<details>
<summary>AI-Generated Info</summary>

A factor (or divisor) of n is a positive integer that divides n with no remainder. Divisibility rules give shortcuts for small divisors: digit sum (3 and 9), last two digits (4), last three digits (8), alternating digit sum (11), and the alternating sum of 3-digit blocks (7). Divisibility by 6 follows from divisibility by 2 and 3, and divisibility by 12 follows from divisibility by 3 and 4. The complete factor list comes from the prime factorization n = p₁^a₁ × ... × pₖ^aₖ — each factor corresponds to a choice of exponents 0 ≤ eᵢ ≤ aᵢ, giving (a₁ + 1)(a₂ + 1)...(aₖ + 1) factors total. For example, 12 = 2² × 3 has 6 factors: 1, 2, 3, 4, 6, 12.

</details>

#### Calculations

Given an input number:
1. Use the divisibility rules to see if we can find some factors of the number and display a paragraph with info from this.
2. Find the PF of the number. If we can determine from this PF that the number is composite (not prime), then find its factors by finding the subfactorizations in its PF.

#### Input Constraints

Min: 10
<br/>
Website max: 1 million (1,000,000)
<br/>
CLI and GUI max: 10 quadrillion (10,000,000,000,000,000)

#### Example Calculations Screenshots

##### Website

![Website divisibility calculations](/Spring_Website_Version/screenshots/section_calculations/divisibility_calculations.JPG)

##### CLI

![CLI divisibility calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/divisibility_calculations.JPG)

<br/>
</details>


### Greatest Common Divisor (GCD) and Least Common Multiple (LCM) &nbsp; :heavy_division_sign: &nbsp; :heavy_multiplication_x:

<details>
<summary>Info</summary>
<br/>

Greatest common divisor is also known as greatest common factor, or GCF. To find the GCD and LCM of 2 whole
numbers, you could manually do some division and multiplication but there are other ways to find them.

#### The Euclidean Algorithm

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

Let's find the GCD of 6 and 35 using the Euclidean algorithm. Here are the iterations:

| Max | Min | Remainder |
| --- | --- | --------- |
|  35 |  6  |     5     |
|  6  |  5  |     1     |
|  5  |  1  |     0     |

The GCD is 1.

Let's find the GCD of 54 and 99 using the Euclidean algorithm. Here are the iterations:

| Max | Min | Remainder |
| --- | --- | --------- |
|  99 |  54 |     45    |
|  54 |  45 |     9     |
|  45 |  9  |     0     |

The GCD is 9.

#### Prime Factorizations

The GCD and LCM of 2 whole numbers > 1 can be found by looking at their prime factorizations (PFs). If those numbers don't have any common prime factors, then the GCD is 1. If they do have common prime factors, then the GCD PF consists of all the common prime factors and the exponent of each factor is the min of the exponents of that factor in the 2 PFs. The LCM PF consists of all the prime factors that are in either of the PFs of the 2 numbers. If a factor is in both PFs, then the exponent of that factor in the LCM PF is the max of the exponents of that factor in the 2 PFs. If a factor is unique to one of the PFs, then that factor and its exponent are in the LCM PF.

Let's find the GCD and LCM of 6 and 35 using their PFs. The PF of 6 is $2 \times 3$ and the PF of 35 is $5 \times 7 \text{.}$ There are no common prime factors so the GCD is 1. The LCM PF is $2 \times 3 \times 5 \times 7 \text{,}$ which is 210.

Let's find the GCD and LCM of 54 and 99. The PF of 54 is $2 \times 3^3$ and the PF of 99 is $3^2 \times 11 \text{.}$ 3 is the only common prime factor and the min exponent of it is 2 so the GCD PF is $3^2 \text{,}$ which is 9. The max exponent of 3 is 3 so $3^3$ is in the LCM PF. The LCM PF is $2 \times 3^3 \times 11 \text{,}$ which is 594.

#### Other Info

2 whole numbers are said to be *coprime* if their GCD is 1. Therefore, coprime numbers don't have any common prime factors in their PFs.

For the website version, the input numbers that have the largest LCM are 1,000,000, the max input; and 999,999. Their LCM is 999,999,000,000. A pair of input numbers whose LCM has the most prime factors is 524,288 (2<sup>19</sup>) and 531,441 (3<sup>12</sup>). Their LCM has a PF of $2^{19} \times 3^{12} \text{,}$ has 29 prime factors, and is 278,628,139,008. A pair of input numbers whose LCM might have the most *unique* prime factors is 510,510, the product of the first 7 prime numbers; and 392,863, the product of the next 4 prime numbers. Their LCM is the product of the first 11 prime numbers, or 200,560,490,130. Its PF is $2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \text{.}$

For the CLI and GUI versions, the input numbers that have the largest LCM are 5 quadrillion (5,000,000,000,000,000), the max input; and 5 quadrillion − 1. Their LCM is 24,999,999,999,999,995,000,000,000,000,000 (24 nonillion 999 octillion 999 septillion 999 sextillion 999 quintillion 995 quadrillion)! It has 32 digits. Trillion is before quadrillion. A pair of input numbers whose LCM has the most prime factors is 4,503,599,627,370,496 (2<sup>52</sup>) and 1,853,020,188,851,841 (3<sup>32</sup>). Their LCM has a PF of $2^{52} \times 3^{32} \text{,}$ has 84 prime factors, and is 8,345,261,032,023,157,253,752,158,683,136 (8 nonillion ...). A pair of input numbers whose LCM might have the most *unique* prime factors is 304,250,263,527,210, the product of the first 13 prime numbers; and 133,869,006,807,307, the product of the next 8 prime numbers. Their LCM is the product of the first 21 prime numbers, or 40,729,680,599,249,024,150,621,323,470 (40 octillion ...). Its PF is $2 \times 3 \times 5 \times 7 \times 11 \times 13 \times 17 \times 19 \times 23 \times 29 \times 31 \times 37 \times 41 \times 47 \times 53 \times 59 \times 61 \times 67 \times 71 \times 73 \text{!}$

<details>
<summary>AI-Generated Info</summary>

The greatest common divisor (GCD) of a and b is the largest integer dividing both, and the least common multiple (LCM) is the smallest positive integer divisible by both. Euclid's algorithm computes the GCD by repeatedly replacing the larger value with its remainder modulo the smaller, terminating when the remainder is 0. From the prime factorizations, the GCD takes the minimum of each shared prime's exponent and the LCM takes the maximum. They satisfy the identity GCD(a, b) × LCM(a, b) = a × b.

</details>

#### Calculations

Given 2 input numbers:
1. Perform the Euclidean algorithm on the numbers and display a table with info about all iterations. This table
   will look like the tables shown in the info above.
2. Find the PFs of the numbers and use these to find the PFs of the GCD and LCM.

#### Input Constraints

Min: 2
<br/>
Website max: 1 million (1,000,000)
<br/>
CLI and GUI max: 5 quadrillion (5,000,000,000,000,000)

#### Example Calculations Screenshots

##### Website

![Website GCD and LCM calculations](/Spring_Website_Version/screenshots/section_calculations/GCD_and_LCM_calculations.JPG)

##### CLI

![CLI GCD and LCM calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/GCD_and_LCM_calculations.JPG)

<br/>
</details>


### Goldbach Conjecture

<details>
<summary>Info</summary>
<br/>

The *Goldbach conjecture* says that every even number ≥ 4 can be expressed as the sum of 2 prime numbers. This
was named after 1700s Prussian mathematician Christian Goldbach. A *conjecture* is a statement that's believed to
be true but hasn't been proven to be. The Goldbach conjecture has been verified to be true for all even numbers
≥ 4 and ≤ 4 × 10<sup>18</sup>.


<details>
<summary>AI-Generated Info</summary>

Christian Goldbach conjectured in 1742 that every even integer greater than 2 is the sum of two primes; for example, 10 = 3 + 7 = 5 + 5. It has been verified by computer for all even numbers up to roughly 4 × 10¹⁸ but remains unproven. The related "weak" Goldbach conjecture — that every odd integer greater than 5 is a sum of three primes — was proven by Harald Helfgott in 2013. The number of representations tends to grow with n, making counterexamples increasingly unlikely as n increases.

</details>

#### Calculation

Find the pairs of prime numbers that sum to an input number.

#### Input Constraints

Must be even
<br/>
Min: 4
<br/>
Website max: 10,000
<br/>
CLI max: 1.5 million (1,500,000)
<br/>
GUI max: 250,000

Why are there separate max inputs for the CLI and GUI? See the comment by the start of the `GoldbachConjecture` class and the documentation comment for `StringTooLongException` in the `NtpTextArea` class.

#### Example Calculation Screenshots

##### Website

![Website Goldbach conjecture calculation](/Spring_Website_Version/screenshots/section_calculations/Goldbach_Conjecture_calculation.JPG)

##### CLI

![CLI Goldbach conjecture calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/Goldbach_Conjecture_calculation.JPG)

<br/>
</details>


### Pythagorean Triples

<details>
<summary>Info</summary>
<br/>

The Pythagorean theorem says that for a right triangle, the sum of the squares of the lengths of the 2 shortest sides (legs) equals the square of the longest side (hypotenuse) length, or $a^2 + b^2 = c^2 \text{.}$ This was named after the ancient Greek mathematician Pythagoras. A *Pythagorean triple* is a triple of whole numbers that $a \text{,}$ $b \text{,}$ and $c$ can be. For example; 3, 4, and 5 is a Pythagorean triple since $3^2 \text{ (} 9 \text{)}  + 4^2 \text{ (} 16 \text{)} = 5^2 \text{ (} 25 \text{)}$ and 11, 60, and 61 is another one since $11^2 \text{ (} 121 \text{)} + 60^2 \text{ (} 3,600 \text{)} = 61^2 \text{ (} 3,721 \text{).}$

Once we know a Pythagorean triple, we can form another one by multiplying $a \text{,}$ $b \text{,}$ and $c$ by the same whole number > 1. Because of this, there are an infinite amount of Pythagorean triples. A Pythagorean triple is ~~said to be~~ *primitive* if the GCD of $a \text{,}$ $b \text{,}$ and $c$ is 1. Therefore, a primitive triple can't be formed by taking another triple and multiplying $a$, $b$, and $c$ by the same whole number. The triples mentioned above; 3, 4, and 5, and 11, 60, and 61; are primitive. 6 (3 × 2), 8 (4 × 2), and 10 (5 × 2) is another triple. $6^2 \text{ (} 36 \text{)} + 8^2 \text{ (} 64 \text{)} = 10^2 \text{ (} 100 \text{).}$ 55 (11 × 5), 300 (60 × 5), and 305 (61 × 5) is another one. $55^2 \text{ (} 3,025 \text{)} + 300^2 \text{ (} 90,000 \text{)} = 305^2 \text{ (} 93,025 \text{).}$

<details>
<summary>AI-Generated Info</summary>

A Pythagorean triple is three positive integers (a, b, c) satisfying a² + b² = c², corresponding to a right triangle with integer side lengths. The smallest is (3, 4, 5). Euclid's formula generates all primitive triples — those with gcdFps(a, b, c) = 1 — as a = m² − n², b = 2mn, c = m² + n² for coprime m > n of opposite parity. Every Pythagorean triple is a positive integer multiple of a primitive one, and there are infinitely many.

</details>

#### Calculation

Find the first 10 Pythagorean triples where the short leg length, the lowest number in the triple, is ≥ an input number. For example, if the input number is 3, then the triple 3, 4, and 5 will be the first one found. If the input number is 4, then the triple 5, 12, and 13 will be the first one found. The algorithm I came up with first tries to find triples where the short leg length equals the input number and then tries to find triples where the short leg length equals the input number + 1, and so on until 10 are found.

These triples will be displayed like the examples at the end of the paragraphs in the info above are displayed. If a triple is primitive, then it'll be followed by "(primitive)".

#### Input Constraints

Min: 0
<br/>
Website max: 100
<br/>
CLI and GUI max: 10,000

#### Example Calculation Screenshots

##### Website

![Website Pythagorean triples calculation](/Spring_Website_Version/screenshots/section_calculations/pythag_triples_calculation.JPG)

##### CLI

![CLI Pythagorean triples calculation](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/pythag_triples_calculation.JPG)

<br/>
</details>


### Two Square Theorem

<details>
<summary>Info</summary>
<br/>

Fermat's two square theorem says that every prime number that's 1 above a multiple of 4 can be expressed as the sum of 2 squares in 1 way. This was named after 1600s French mathematician Pierre de Fermat. In the context of this theorem, *square* is a shortening of *square number* or *perfect square* and is a number that can be formed by taking an integer and multiplying it by itself, or *squaring* it. The first 4 squares are 0 (0<sup>2</sup>), 1 (1<sup>2</sup>), 4 (2<sup>2</sup>), and 9 (3<sup>2</sup>). Because of this theorem, a prime number that's 1 above a multiple of 4 is known as a *Pythagorean prime*. An example of a Pythagorean prime is 29 and it can be expressed as $2^2 \text{ (} 4 \text{)} + 5^2 \text{ (} 25 \text{).}$

<details>
<summary>AI-Generated Info</summary>

Fermat's Theorem on Sums of Two Squares states that an odd prime p can be written as a² + b² if and only if p ≡ 1 (mod 4). Such primes are called Pythagorean primes; the first few are 5, 13, 17, 29, 37, 41. When the representation exists, it is unique up to the order and signs of a and b. The theorem connects to the Gaussian integers ℤ[i], in which these primes split as p = (a + bi)(a − bi).

</details>

#### Calculations

Find the 1<sup>st</sup> Pythagorean prime ≥ an input number, as well as the whole numbers whose squares sum to that prime.

#### Input Constraints

Min: 0
<br/>
Website max: 1 million (1,000,000)
<br/>
CLI and GUI max: 1 quadrillion (1,000,000,000,000,000)

#### Example Calculations Screenshots

##### Website

![Website two square theorem calculations](/Spring_Website_Version/screenshots/section_calculations/two_square_theorem_calculations.JPG)

##### CLI

![CLI two square theorem calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/two_square_theorem_calculations.JPG)

<br/>
</details>


### Fibonacci-Like Sequences

<details>
<summary>Info</summary>
<br/>

I consider a number sequence to be "Fibonacci-like" if it starts with 2 numbers and has every following number be
the sum of the 2 previous numbers. The Fibonacci sequence does this and the first 8 numbers of it are
1, 1, 2, 3, 5, 8, 13, and 21. Fibonacci was a mathematician from the 1100s to 1200s from modern-day Italy. Another
Fibonacci-like sequence is the Lucas sequence and the first 8 numbers of it are 2, 1, 3, 4, 7, 11, 18, and 29.
This sequence was named after 1800s French mathematician François Édouard Anatole Lucas.

The *Golden Ratio* is an irrational number symbolized by the Greek letter 𝚽 (Phi). $𝚽 = \frac{1 + \sqrt{5}}{2} \approx 1.618033988749895 \text{.}$ As we advance further and further into a Fibonacci-like sequence, the ratio between a number and the number before it gets closer and closer to 𝚽. For example, recall that the first 8 numbers of the Fibonacci sequence are 1, 1, 2, 3, 5, 8, 13, and 21. $\frac{2}{1} = 2 \text{,}$ $\frac{8}{5} = 1.6 \text{,}$ and $\frac{21}{13} \approx 1.615384615384615 \text{.}$

<details>
<summary>AI-Generated Info</summary>

A Fibonacci-like sequence starts with two seed values; each subsequent term is the sum of the previous two: a, b, a+b, a+2b, 2a+3b, .... The classical Fibonacci sequence uses seeds 1, 1, and the Lucas sequence uses 2, 1. For any starting pair (with at least one positive value), the ratio of consecutive terms converges to the golden ratio φ = (1 + √5) / 2 ≈ 1.618. This convergence reflects the fact that the dominant eigenvalue of the recurrence's 2×2 transition matrix is φ.

</details>

#### Calculations

1. Find the first 20 numbers of the Fibonacci-like sequence that starts with 2 input numbers.
2. Find the ratios between the 5<sup>th</sup> and 4<sup>th</sup>, 10<sup>th</sup> and 9<sup>th</sup>,
   15<sup>th</sup> and 14<sup>th</sup>, and 20<sup>th</sup> and 19<sup>th</sup> numbers in the sequence. These
   ratios get closer and closer to 𝚽 and are floating-point numbers most of the time, so the calculations for
   them are some of the few calculations done by the Number Theory Playground that involve numbers other than
   natural numbers.

#### Input Constraints

Min: 1
<br/>
Website max: 1 billion (1,000,000,000)
<br/>
CLI and GUI max: 9 quintillion (9,000,000,000,000,000,000)

#### Example Calculations Screenshots

##### Website

![Website Fibonacci-like sequences calculations](/Spring_Website_Version/screenshots/section_calculations/fibo-like_sequences_calculations.JPG)

##### CLI

![CLI Fibonacci-like sequences calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/fibo-like_sequences_calculations.JPG)

<br/>
</details>


### Ancient Egyptian Multiplication

<details>
<summary>Info</summary>
<br/>

The ancient Egyptians had an interesting algorithm for multiplication of 2 whole numbers. My way of explaining the algorithm goes like this:

1. Let variable $a$ represent one of the numbers and variable $b$ represent the other number.
2. Find all powers of 2 that are ≤ $a \text{.}$ This could be done without modern multiplication by starting with 1, the 1<sup>st</sup> power of 2 or 2<sup>0</sup>, and finding the next power by adding the previous power to itself. This process will look like: $1 + 1 = 2 \text{ (} 2^1 \text{),}$ $2 + 2 = 4 \text{ (} 2^2 \text{),}$ $4 + 4 = 8 \text{ (} 2^3 \text{),}$ and so on until we find a power that's > $a \text{,}$ which we won't use.
3. Find the products of $b$ and these powers of 2. Like with the powers of 2, this could be done by starting with $b$ and finding the next product by adding the previous product to itself. If we let $b$ be 5, this process will look like: $5 + 5 = 10 \text{ (} 5 \times 2 \text{),}$ $10 + 10 = 20 \text{ (} 5 \times 4\text{),}$ $20 + 20 = 40 \text{ (} 5 \times 8 \text{),}$ and so on.
4. Find the powers of 2 that sum to $a \text{.}$
5. Add up the products of $b$ and these powers.

This gives us the product of the 2 numbers.

Let's find the product of 5 and 12. Let's first use 5 for the number represented by $a$ in the algorithm above and 12 for $b \text{.}$ The powers of 2 ≤ 5 are 1, 2, and 4. The products of 12 and these powers are 12, 24, and 48. The powers of 2 that sum to 5 are 1 and 4. The products of 12 and these powers are 12 and 48.
$12 + 48 = (12 \times 1) + (12 \times 4) = 12 \times (1 + 4) = 60 \text{.}$

Now let's use 12 for $a$ and 5 for $b \text{.}$ The powers of 2 ≤ 12 are 1, 2, 4, and 8. The products of 5 and these powers are 5, 10, 20, and 40. The powers of 2 that sum to 12 are 4 and 8. The products of 5 and these powers are 20 and 40. $20 + 40 = (5 \times 4) + (5 \times 8) = 5 \times (4 + 8) = 60 \text{.}$

<details>
<summary>AI-Generated Info</summary>

Ancient Egyptian (or "peasant") multiplication computes a × b using only doubling and addition: write a in binary as a sum of distinct powers of 2, then add the corresponding doublings of b. For example, 13 × 12 = 8 × 12 + 4 × 12 + 1 × 12 = 96 + 48 + 12 = 156. The method appears in the Rhind Mathematical Papyrus from around 1650 BC. It is essentially the same algorithm as modern shift-and-add binary multiplication.

</details>

#### Calculations

Given 2 input numbers:
1. Find the powers of 2 ≤ the 1<sup>st</sup> input number and the corresponding multiples of the 2<sup>nd</sup>
   input number. Display these in a table.
2. Find the powers of 2 that sum to the 1<sup>st</sup> input number and the corresponding multiples of the
   2<sup>nd</sup> input number. Display these in another table.

This process will be done faster than you can say "ancient Egyptian multiplication." :slightly_smiling_face:

#### Input Constraints

Min: 2
<br/>
Website max: 1 billion (1,000,000,000)
<br/>
CLI and GUI max: 9 quintillion (9,000,000,000,000,000,000)

#### Example Calculations Screenshots

##### Website

![Website ancient Egyptian multiplication calculations](/Spring_Website_Version/screenshots/section_calculations/ancient_mult_calculations.JPG)

##### CLI

![CLI ancient Egyptian multiplication calculations](/Java_CLI_and_GUI_Versions/CLI_screenshots/section_calculations/ancient_mult_calculations.JPG)

</details>

<br/>


## Deployment to the Internet

I used GoDaddy as my registrar for the domain `numbertheoryplayground.com` and used [Microsoft Azure Container Apps](https://learn.microsoft.com/en-us/azure/container-apps/overview) to deploy this app to the internet. I created a container image for this app by first creating a `jar` file and then using the Docker CLI and a `Dockerfile` in the `Spring_Website_Version` directory with this code:

```dockerfile
FROM mcr.microsoft.com/openjdk/jdk:21-mariner
COPY target/number-theory-playground-1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

I then pushed my image to this [repository of mine on Docker Hub](https://hub.docker.com/repository/docker/nschroeder131/number-theory-playground/general). The container app pulls the image from this repo.

<details>
<summary>Info about mcr.microsoft.com/openjdk/jdk:21-mariner</summary>
<br/>

This is a container image for the [Microsoft Build of OpenJDK](https://learn.microsoft.com/en-us/java/openjdk/overview) for Java 21 for [Mariner, AKA Azure Linux](https://learn.microsoft.com/en-us/azure/azure-linux/faq). I decided to use this JDK image as my base image simply because there's a [sample app for Azure Container Apps](https://github.com/Azure-Samples/containerapps-albumapi-java) that used it as its base image and the [plain OpenJDK Docker image](https://hub.docker.com/_/openjdk) is deprecated.

</details>

<br/>


## Some Design and Implementation Info

### Website

#### Front End

<details>
<summary>Info</summary>
<br/>

A single page application is used. The content of the page changes when a section changes, an answer is displayed, or an error message is displayed.

`index.html` has the main heading, the section buttons, and home content. This is the content that a user sees first when visiting the site. Having all this content in `index.html` allows it to be rendered quickly. The script stores the `div` that contains the home content to a variable. When the home section is revisited, that `div` gets shown.

##### `Section` Classes

The webpage script has a `Section` class with `SingleInputSection` and `DoubleInputSection` as subclasses. There's a `curSection` variable that starts off as `undefined` but gets set to a `Section` when the section changes. `Section` is effectively abstract, since its constructor is only called from the constructors of its subclasses. The only param of the `Section` constructor is a `SectionParams` object.

The params for the `SingleInputSection` and `DoubleInputSection` constructors are a `SectionParams` object and a `createAnswerElements` function. For `SingleInputSection`, `createAnswerElements` has params for an HTTP response object, an input string, and maybe an input number. Some `SingleInputSection`s have a `createAnswerElements` that has the input number as its 3<sup>rd</sup> param and the rest omit that param. For `DoubleInputSection`, `createAnswerElements` has params for an HTTP response object and 2 input strings. The input string(s) will have commas.

###### Some `SectionParams` Fields

**`infoHtmlStringOrArr`** (`string` or `HTMLElement[]`)

The webpage has a collapsible section with info about a number theory concept. For most sections, the info to show is 1 or more paragraphs and we can just use `p` elements to show these paragraphs. For these sections, we create a `SingleInputSection` or `DoubleInputSection` and pass in a string with the paragraphs of info, optionally with HTML, for `infoHtmlStringOrArr`. Each paragraph is separated by a blank line. The `Section` constructor splits this string to get the paragraphs and maps each to a `p` element with its `innerHTML` set to that paragraph. More info about the use of `innerHTML` is in the [`innerHTML` section](#innerhtml) below. The `Section` constructor will then assign this array of `p` elements to an `infoHtmlElements` variable.

For a few sections, the info to show is more complex. For example, for the divisibility section, this info is an intro paragraph, multiple paragraphs about prime factoizations, and multiple paragraphs about divisiblity rules. We can use `p` elements for the paragraphs, but we also want to use `div`s to divide these paragraphs and use headings. For the divisibility section, we create a `SingleInputSection` and pass in an array of those info elements for `infoHtmlStringOrArr`. The `Section` constructor will assign a copy of this array to `infoHtmlElements`.

**`btnIdStart`** (`string`)

This is the start of the ID of a button in `section-btns-div` in `index.html`. The `Section` constructor accesses this button and adds an on-click listener to it which does the work to switch to that section, including but not limited to setting `curSection` to that `Section`, setting the section heading text to the section button text, making the elements in `infoHtmlElements` appear, and updating the directions.

Another way of implementing the buttons in `section-btns-div` is to not have any buttons be in that `div` in `index.html` but instead replace the `btnIdStart` field with a `heading` field, have the `Section` constructor create a button element, set the text of it to the value of `heading`, add the on-click listener to it, and add it to `section-btns-div`. However, if this was done, then there would be a little delay between the webpage loading and these buttons appearing.

**`apiEndpointEnd`** (`string`)

Usage of this is explained in the next section.

###### Calculate Button

When the calculate button is clicked, the user's input will be checked and if it's valid, then the webpage will make an HTTP request to `numbertheoryplayground.com/calculate/` followed by the value of `curSection.apiEndpointEnd`. If the request is successful, then `curSection.createAnswerElements` will be called and the returned elements will be shown. If `curSection` is a `SingleInputSection`, then the args for the call will be the HTTP response object, the input string, and the input number. If `curSection` is a `DoubleInputSection`, then the args will be the response object and the input strings. The input string(s) will have commas.

##### `innerHTML`

<details>
<summary>Info</summary>
<br/>

For the webpage script, the [`innerHTML`](https://developer.mozilla.org/en-US/docs/Web/API/Element/innerHTML) of some elements are set. This is done when creating section info elements and setting the text of the directions for the current section. Some of the `innerHTML` values are string literals; some are strings created by taking a string literal variable, calling `toLowerCase` on it, and concatenating it with another string (for the `twinPrimePairsActionSentenceEnding` and `pythagTriplesActionSentenceEnding` variables); and some are strings with interpolated values. For that last option, I'm aware that *can* be unsafe but I'm being safe. The interpolated values are string literal variables, calls to functions that I wrote that return [Math ML](https://developer.mozilla.org/en-US/docs/Web/MathML) strings, or variables for Math ML strings that were returned by calls to those functions. The args for those function calls are hardcoded values, though the args aren't always hardcoded. You see, an example of a Math ML string variable that gets interpolated is `nVarMl`. This is done to use the variable $n$ in the info of some sections. `nVarMl` comes from this code:

```js
const createMiMl = (identifier, endText) =>
    `<math><mi>${identifier}</mi>${endText ? `<mtext>${endText}</mtext>` : ''}</math>`;

const [aVarMl, bVarMl, cVarMl, nVarMl] = ['a', 'b', 'c', 'n'].map(letter => createMiMl(letter));
```

`letter` is the arg in the call to `createMiMl` and isn't hardcoded, though the only possible values of `letter` are those string literals.

</details>

</details>

#### Calculations

<details>
<summary>Info</summary>
<br/>

A server API is used for most of the calculations. Spring and Java 21 are used for the server. The code for these calculations is in the `calculationsimpl` (calculations implementation) package. If the text field(s) have valid input and the "Calculate" button is clicked, an HTTP request will be sent to the server with the input number(s). There's an endpoint for each section of the app. The server will do the calculation(s) for the section and send a JSON response of data about the calculation. The webpage script will then use this data and create some HTML elements to display to the user.

The calculations can be done on the front end. I originally had it like this when I started working on the Node website version in 2020. At some point while working on this version, I decided to have the server do the calculations to get some practice with front-end and back-end communication. When I started the Spring website version, I decided to do the same thing.

##### API Endpoints Documentation

<details>
<summary>Info</summary>
<br/>

All of the endpoints are for `GET` requests. The start of each endpoint is `numbertheoryplayground.com/calculate/`. [JSDoc syntax](https://jsdoc.app/tags-type) is used to document response types.

Query params are used for input numbers. For single input section endpoints, the only query param is `input`. For double input section endpoints, the query params are `input1` and `input2`. For the Goldbach prime pair starts endpoint, valid query param values are even numbers in the input range specified. For all other endpoints, valid query param values are whole numbers in the input range. If a request is made to an endpoint with an invalid query param value(s), the server will respond with error code 400 (bad request).

PF stands for prime factorization. The responses for the PF, divisiblity PF answer data, and GCD and LCM answer data endpoints have PF data. Sometimes, this data is in the form of an object of this type: `{ correspondingNum: number, fes: ?({ factor: number, exponent: number }[])}`. `correspondingNum` is the number that a PF is for. If `fes` is **not** `null`, then it contains the factors and exponents in that PF. If `fes` is `null`, then that means that `correspondingNum` is prime and therefore its PF just consists of 1 factor with 1 as its exponent. Sometimes, PF data is in the form of just a factor and exponent object array. Whether one of these arrays is used on its own or as part of an object, it'll always be sorted by factors in ascending order.

<br/>


<details>
<summary>Prime Numbers</summary>
<br/>

Endpoint end: `primes`
<br/>
Query param: `input`
<br/>
Input Range: 0 - 100,000

###### Response

Type: `number[]`

An array of the first 30 primes ≥ the input.

<br/>
</details>


<details>
<summary>Semiprimes Data</summary>
<br/>

Endpoint end: `semiprimes-data`
<br/>
Query param: `input`
<br/>
Input Range: 0 - 100,000

###### Response

Type: `{ semiprime: number, primeFactor1: number, primeFactor2: number }[]`

An array of objects with data about the first 20 semiprimes ≥ the input.

<br/>
</details>


<details>
<summary>Twin Prime Pair Starts</summary>
<br/>

Endpoint end: `twin-prime-pair-starts`
<br/>
Query param: `input`
<br/>
Input Range: 0 - 100,000

###### Response

Type: `number[]`

Finds the first 20 twin prime pairs where the lowest number in the pair is ≥ the input. For example, if the input is 3, then the pair 3 and 5 will be the first one found. If the input is 4, then the pair 5 and 7 will be the first one found. The response is an array that contains the lowest numbers of those pairs.

<br/>
</details>


<details>
<summary>Prime Factorization</summary>
<br/>

Endpoint end: `prime-factorization`
<br/>
Query param: `input`
<br/>
Input Range: 2 - 1 million (1,000,000)

###### Response

Type: `{ factor: number, exponent: number }[]`

An array of the factors and exponents of the PF of the input.

<br/>
</details>


<details>
<summary>Divisibility PF Answer Data</summary>
<br/>

Endpoint end: `divisibility-pf-answer`
<br/>
Query param: `input`
<br/>
Input Range: 10 - 1 million (1,000,000)

###### Response

Type:
```
?{
    inputPfFes: { factor: number, exponent: number }[],
    factorPfs: {
        correspondingNum: number,
        fes: ?({ factor: number, exponent: number }[])
    }[]
}
```

If the input is prime, then the response is empty. Otherwise, `inputPfFes` contains the factors and exponents of the PF of the input and `factorPfs` contains objects for the PFs of the factors of the input, excluding 1 and the input. 


<br/>
</details>


<details>
<summary>GCD and LCM Answer Data</summary>
<br/>

Endpoint end: `gcd-and-lcm-answer`
<br/>
Query params: `input1` and `input2`
<br/>
Input Range: 2 - 1 million (1,000,000)

###### Response

Type:
```
{
    euclideanIterations: { max: number, min: number, remainder: number }[],
    pfAnswer: {
        input1PfFes: { factor: number, exponent: number }[],
        input2PfFes: { factor: number, exponent: number }[],
        gcdPf: ?{
            correspondingNum: number,
            fes: ?({ factor: number, exponent: number }[])
        },
        lcmPf: {
            correspondingNum: number,
            fes: ?({ factor: number, exponent: number }[])
        }
    }
}
```

`euclideanIterations` is a list of objects for all iterations of the Euclidean algorithm performed on `input1` and `input2`.


<br/>
</details>


<details>
<summary>Goldbach Prime Pair Starts</summary>
<br/>

Endpoint end: `goldbach-prime-pair-starts`
<br/>
Query param: `input`
<br/>
Input Range: 4 - 10,000

###### Response

Type: `number[]`

Find the pairs of primes that sum to the input. The response is an array that contains the lowest numbers of those pairs, meaning that the other number in the pair can be found by subtracting the lowest number of the pair from the input number.

<br/>
</details>


<details>
<summary>Pythagorean Triples</summary>
<br/>

Endpoint end: `pythagorean-triples`
<br/>
Query param: `input`
<br/>
Input Range: 0 - 100

###### Response

Type: `{ a: number, b: number, c: number, isPrimitive: boolean }[]`

An array of objects for the first 10 Pythagorean triples where the short leg length, the lowest number in the triple, is ≥ the input. For example, if the input is 3, then an object for the triple 3, 4, and 5 will be the first one. If the input is 4, then an object for the triple 5, 12, and 13 will be the first one. For each object, `a` is the short leg length, `b` is the long leg length, and `c` is the hypotenuse. The algorithm I came up with first tries to find triples where the short leg length equals the input and then tries to find triples where the short leg length equals the input + 1, and so on until 10 are found.


<br/>
</details>


<details>
<summary>Two Square Theorem Answer Data</summary>
<br/>

Endpoint end: `two-square-theorem-answer`
<br/>
Query param: `input`
<br/>
Input Range: 0 - 1 million (1,000,000)

###### Response

Type: `{ pythagPrime: number, a: number, b: number }`

`pythagPrime` is the first Pythagorean prime ≥ the input. `a` and `b` are the whole numbers whose squares sum to `pythagPrime`.

<br/>
</details>


<details>
<summary>Fibonacci-like Sequences Answer Data</summary>
<br/>

Endpoint end: `fibonacci-like-sequences-answer`
<br/>
Query params: `input1` and `input2`
<br/>
Input Range: 1 - 1 billion (1,000,000,000)

###### Response

Type:
```
{
    fiboLikeSequence: number[],
    ratiosData: { num1: number, num2: number, ratio: string, isRounded: boolean }[]
}
```

`fiboLikeSequence` contains the first 20 numbers in the Fibonacci-like sequence that starts with `input1` and `input2`. `ratiosData` contains objects with data about the ratios of the 5<sup>th</sup> and 4<sup>th</sup>, 10<sup>th</sup> and 9<sup>th</sup>, 15<sup>th</sup> and 14<sup>th</sup>, and 20<sup>th</sup> and 19<sup>th</sup> numbers in the sequence.

`ratio` is a string since a decimal64 format is used for this and JavaScript numbers have a binary64 format.

<br/>
</details>


<details>
<summary>Ancient Egyptian Multiplication Answer Data</summary>
<br/>

Endpoint end: `ancient-multiplication-answer`
<br/>
Query params: `input1` and `input2`
<br/>
Input Range: 2 - 1 billion (1,000,000,000)

###### Response

Type:
```
{
    table1Rows: { powerOf2: string, correspondingMultiple: string }[],
    table2Rows: { powerOf2: string, correspondingMultiple: string }[],
    product: string
}
```

`table1Rows` contains rows for all the powers of 2 ≤ `input1` and the corresponding multiples of `input2`. `table2Rows` contains rows for all the powers of 2 that sum to `input1` and the corresponding multiples of `input2`, which sum to the product of `input1` and `input2`, which is what `product` is for.

Strings are used for numbers since a corresponding multiple or product might be too big for a safe JavaScript integer. A power of 2 will always be small enough for a safe JavaScript integer but a string is still used for consistency.

<br/>
</details>

</details>

</details>


### CLI and GUI Versions

For most of the history of this project, Java 11 was used for these versions, though Java 21 is used now. I have these versions in the same IntelliJ IDEA project and they share a lot of code, such as code for the calculations and text that gets displayed. The `NtpCli` class has the `main` method and some other code for running the CLI version of the app. The `NtpGui` class has the `main` method and a little of the code for running the GUI version of the app. A lot of the other code for running it is in the `MainPanel` class. The Swing GUI library was used.

#### Section Classes

<details>
<summary>Info</summary>
<br/>

There’s an abstract `Section` class that has data and functionality that the app will use. The direct subclasses of this are the abstract classes `SingleInputSection` and `DoubleInputSection`. These 3 classes are in the `numbertheoryplayground.sectionclasses.abstract_` package. If it's unclear why there's an `_`, it's because `abstract` is a reserved keyword in Java and you can't have reserved keywords in package names. `SingleInputSection` and `DoubleInputSection` each have 2 abstract methods, `getCliAnswer` and `getGuiComponents`. The `SingleInputSection` methods have 1 param for an input long and 1 for a string that contains that long and commas. The `DoubleInputSection` methods have 2 params for the 1<sup>st</sup> input long and its string and 2 more params for the 2<sup>nd</sup> input long and its string. These methods do the calculation(s) for the section and return something that contains info from the calculation and get displayed in the app. `getCliAnswer` returns a string and `getGuiComponents` returns a list of GUI components.

For each section in the app, there's a class with code related to that section. These classes will be referred to as *outer section classes*. They're in the `numbertheoryplayground.sectionclasses.outer` package and the names of them are similar to the section names in the ["All Sections" section](#all-sections). The `PrimeFactorization` class has instance and static members and all other outer section classes are *utility classes*, meaning that they contain only static members. Some members of these classes are used by other classes as well.

Each outer section class has a nested, static, concrete class called `Section` that extends either `SingleInputSection` or `DoubleInputSection`. These classes will be referred to as *nested section classes*. These implement `getCliAnswer` and `getGuiComponents` using code in their outer section class and sometimes code from other classes as well. Methods and classes that do a calculation(s) are members of outer section classes for both `Section` methods to use. For strings used in both methods, string constants and methods that return strings are used.

The nested section class constructors call their superclass constructors and one of the args for this is a string that contains paragraphs of info that'll be displayed to the user. Text blocks are used for these and are placed near related code, usually at the top of an outer section class. This is done in an attempt to help explain that related code.

Input validation for a calculation is implemented by having a method or class constructor that does a calculation call `Misc.assertIsInRange` with the input number as the 1<sup>st</sup> arg and the min and max input numbers for the section as the 2<sup>nd</sup> and 3<sup>rd</sup> args, respectively. `Misc.assertIsInRange` will throw the instance of `Misc.InvalidInputNumberException` if the input number is out of range. The Goldbach conjecture calculation method also throws this exception if the input number is odd. For the CLI, this exception bubbles up to `getCliAnswer` and then to `NtpCli.goToSection`, where it's handled. For the GUI, it bubbles up to `getGuiComponents` and then to the action listener for `calcBtn` in `MainPanel`, where it's handled. Implementations of `getCliAnswer` and `getGuiComponents` call a method or class constructor for a calculation first to check if it throws an exception.

</details>

<br/>


## Unit Testing

The JUnit framework was used. There are tests for the website, CLI, and GUI versions. The location of the test classes is the `src/test` directory. For the website, CLI, and GUI versions, there are tests for methods and classes that do calculations. For just the website version, there are also [a couple tests for the calculations controller](https://github.com/ncschroeder/Number-Theory-Playground/blob/master/Spring_Website_Version/src/test/java/com/numbertheoryplayground/CalculationsControllerTests.java). For just the CLI and GUI versions, there are also tests for methods in the `NtpCli` and `Misc` classes. Almost all tests are parameterized and the ones that aren't are ones that use the `@Test` annotation. For the website version, there are 17 tests, all but 1 of which are parameterized. For the CLI and GUI versions, there are 23 tests, all but 4 of which are parameterized.

### Expected Results

<details>
<summary>Info</summary>

The examples below are for the website version.

For some tests, I was able to come up with expected results.

<details>
<summary>Example</summary>
<br/>

`FibonacciLikeSequencesAnswer.fiboLikeSequence` is a `List<Long>` field. The `FibonacciLikeSequencesAnswerTests.getIntFiboLikeSequences` method creates the list below, which is a `List<Integer>` with the numbers that are expected to be in the `fiboLikeSequence` list in a `FibonacciLikeSequencesAnswer` that gets created with `304` and `5` as the args.

```java
var anotherSequence =
    List.of(
        304, 5, 309, 314, 623, 937, 1_560, 2_497, 4_057, 6_554, 10_611, 17_165,
        27_776, 44_941, 72_717, 117_658, 190_375, 308_033, 498_408, 806_441
    );
```

I was able to come up with this list by starting with 304 and 5 and then calculating the following numbers by summing the 2 numbers prior to it. $309 = 304 + 5$, $314 = 5 + 309$, and so on.

</details>

<br/>

For some tests, I got expected results from Wikipedia and The [On-Line Encyclopedia of Integer Sequences (OEIS)](https://oeis.org/).

<details>
<summary>Example</summary>
<br/>

The `Calculations.getPrimes` method returns an `int[]` of the first 30 prime numbers ≥ an input number. In the `CalculationsTests.getArgsForGetPrimes` method, this `int[]` is expected to be equal to the `int[]` returned by calling `getPrimes(0)`:

```java
var first30Primes =
    new int[] {
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53,
        59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113
    };
```

I got these from [Wikipedia's list of prime numbers](https://en.wikipedia.org/wiki/List_of_prime_numbers).

</details>

<br/>

For 2 tests, I got expected results partially from Wikipedia or the OEIS and partially by running the NTP CLI and having it do a calculation and then I verify the result. This is done for the test in the example below and the test for calculating Pythagorean triples.

<details>
<summary>Example</summary>
<br/>

The `TwoSquareTheoremAnswer` class has `pythagPrime`, `a`, and `b` `int` fields. `pythagPrime` is the first Pythagorean prime ≥ an input number. `a` and `b` are the ints whoses squares sum to `pythagPrime`. The `TwoSquareTheoremAnswerTests.allMethods` test method is parameterized and the source of the parameters is this CSV data:

```
INPUT,  EXPECTED_PYTHAG_PRIME,  EXPECTED_A,  EXPECTED_B
5,               5,                 1,           2
45,              53,                2,           7
275,             277,               9,           14
5_090,           5_101,             50,          51
```

I got the expected Pythagorean primes from [Wikipedia's list of prime numbers](https://en.wikipedia.org/wiki/List_of_prime_numbers). I got the expected a's and b's by running the NTP CLI and having it do a Two Square Theorem section calculation but I verified that the sum of the squares of a and b equals the expected Pythagorean prime.

</details>

<br/>

In test classes, I mention where I got expected results if I got them using one of the last 2 methods above. If nothing is mentioned, then that means I came up with the results.

</details>


### Methods and Classes that Exist for Unit Testing

<details>
<summary>Info</summary>
<br/>

For the CLI and GUI versions, there are several methods and classes that exist only for the purpose of unit testing. An example of a method is `PrimesNumbers.getPrimes`, which returns a `List<Long>` of prime numbers. 1 place where this is used is the `getPrimesStrings` method, which returns a `Stream<String>` with strings that are displayed in the app. This stream is simply created with `getPrimes(input).stream().map(Misc::createStringWithCommas)`. The only other place `getPrimes` is used is in a test in the `PrimeNumbersTests` class. If there wasn't any unit testing, then I would be able to just have `getPrimesStrings` and have it do what `getPrimes` does but have it create a `List<String>` and add strings to that by calling `Misc.createStringWithCommas` on primes that get found.

An example of a class that exists because of unit testing is `TwoSquareTheorem.Answer`:

```java
static final class Answer {
    private long pythagPrime;
    
    private long a;
    
    private long b;
    
    private final String infoSentence;
    
    // Constructor and getters
}
```

`infoSentence` is displayed in the app and the other fields are used in a test in the `TwoSquareTheoremTests` class. If there wasn't any unit testing, then I would be able to just have a static method that does what the `Answer` constructor does and returns an info sentence string.

</details>

<br/>


## Project History

<details>
<summary>Info</summary>
<br/>

During the summer of 2019, I took a number theory course on [brilliant.org](https://brilliant.org/) for fun and was enlightened by some of the stuff I was learning. Later that year, I was thinking of what kind of programming projects I could work on and some of the stuff I learned from that number theory course came to mind. I realized I could make a program that makes calculations about some of the stuff I learned. Along with the content of that number theory course, I also included additional number theory concepts that I was familiar with. I learned these other concepts from sources such as my discrete math textbook, *Discrete Mathematics and Its Applications* by Kenneth Rosen, and YouTube videos from channels such as [Numberphile](https://www.youtube.com/@numberphile), [Mathologer](https://www.youtube.com/@Mathologer), [3blue1brown](https://www.youtube.com/@3blue1brown), and [Zach Star](https://www.youtube.com/@zachstar). I decided to use Java because it's a popular programming language that I had no familiarity with at the time. However, I found Java easy to learn since some of the syntax is similar to C++, which I had some familiarity with. In fact, the 3 languages I had the most familiarity with at the time were Python :snake:, C++, and JavaScript.
Growing up, I don't recall having much interest in math. During my 3<sup>rd</sup> year of college and my 1<sup>st</sup> year at Missouri State University, in the middle of the Fall 2017 semester, I decided to major in Computer Science and minor in math. In the Spring 2018 semester, I took calculus 1 and discrete math. This was when I started having an interest in math. I then finished all the math courses needed for the minor after the Spring 2019 semester.

I started in December 2019 and from then to October 2020, I was on and off this project. I had my last normal semester of college from January to May. Well, it was normal until the middle of March. I then had an independent study course over the summer where I worked on my Singletonopoly project. The first completed versions of this project were uploaded to GitHub in October 2020. These were Java CLI and GUI versions. These had the first 7 of the 10 sections that are in the app now, starting with the prime numbers section and ending with the Pythagorean triples section.

I think it was around this time (October 2020) when I decided to make a website version. For the back end, I used Node.js and Express. For the front end, I used the Pug template engine for the HTML and vanilla JavaScript and CSS. These are the same tools that I used for a web development course. I originally had the calculations done on the front end but moved them to the back end. For this website version, I included the 7 sections that were in the Java CLI and GUI versions and also added 3 more sections; the Two Square Theorem, Fibonacci-like sequences, and ancient Egyptian multiplication sections. These would later be added to the Java CLI and GUI versions.

Refactoring was done and other changes were made throughout 2021, 2022, and 2023. Unit tests were created for the Java versions in 2022.

In December 2024, I decided to make a website version using Java and Spring for the back end and vanilla HTML, CSS, and JavaScript for the front end.
</details>

### Refactoring Examples

#### Code Duplication

<details>
<summary>Info</summary>
<br/>

1 problem with early versions of this project was code duplication. For example, in the `NTPCLI` class (now called `NtpCli`), in the code in the [commit from October 16<sup>th</sup>, 2020](https://github.com/ncschroeder/Number-Theory-Playground/commit/31b9e4f05cc396f8ba05a49c4146717e2c468a23), there was a static method for each section that was used to interact with the user for that section. There's a **lot** of code that's common to each of these methods. The code below shows an overview of what each method looked like. The min and max input numbers were hardcoded and I replaced these with `x` and `y`, respectively. I also replaced some code with comments and some string text with `...`. Here's a [GitHub link to the whole `NTPCLI` class at the time of this commit](https://github.com/ncschroeder/Number-Theory-Playground/blob/31b9e4f05cc396f8ba05a49c4146717e2c468a23/NTPCLI.java).

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

This is fixed in the current version by having `Section` objects that have the data and functionality necessary for doing all of the things in the code above. As a result, the one place in the code for interacting with a user for a section is the `goToSection` method:

[GitHub link for the code below](https://github.com/ncschroeder/Number-Theory-Playground/blob/e35d6a223dd57a85e358745fa9ac6265eea83724/Java_CLI_and_GUI_Versions/src/main/numbertheoryplayground/NtpCli.java#L109-171)

```java
/**
 * Displays the section heading and choices for the section and then lets the user either
 * enter custom input to use for the calculation(s) of the section, generate random input
 * to use for the calculation(s), get info about the section, or go back to the main menu.
 */
private static void goToSection(Section section) {
    // Valid input values
    final String randomValue = "r";
    final String infoValue = "i";
    final String menuValue = "m";
    
    var sectionChoicesString =
        buildSectionChoicesString(section, randomValue, infoValue, menuValue);
    String sectionHeading = section.getHeading();
    String sectionInfo =
        buildStringWithHeadingAndInfoParagraphs(
            sectionHeading,
            section.getInfoParagraphs().stream()
        );
    
    while (true) {
        println(sectionHeading);
        println();
        println(sectionChoicesString);
        String input = getFormattedInput();
        if (input.equals(menuValue)) return;
        println();
        
        switch (input) {
            case infoValue:
                println(sectionInfo);
                break;
                
            case randomValue:
                println(section.getRandomCliAnswer());
                break;
                
            default:
                try {
                    if (section instanceof SingleInputSection sis) {
                        var inputLong = stripCommasAndParse(input);
                        var inputString = createStringWithCommas(inputLong);
                        println(sis.getCliAnswer(inputLong, inputString));
                    } else if (section instanceof DoubleInputSection dis) {
                        String[] inputContents = input.split("\\s+");
                        if (inputContents.length != 2) {
                            printInvalidInput();
                        } else {
                            var input1Long = stripCommasAndParse(inputContents[0]);
                            var input2Long = stripCommasAndParse(inputContents[1]);
                            var input1String = createStringWithCommas(input1Long);
                            var input2String = createStringWithCommas(input2Long);
                            println(dis.getCliAnswer(input1Long, input2Long, input1String, input2String));
                        }
                    }
                } catch (NumberFormatException | InvalidInputNumberException ex) {
                    printInvalidInput();
                }
        }
        
        println();
    }
}
```

In addition, as mentioned above, the min and max input numbers were hardcoded and, as can be seen in the code, were used multiple times. 1 billion (1,000,000,000) was the max input number for a few sections so the int literal `1_000_000_000` was used multiple times for those sections. What if I typed out that literal correctly the 1<sup>st</sup> time but then accidentally typed `1_000_00_000` the 2<sup>nd</sup> time? Subtle bugs would've happened. :bug: :lady_beetle: :ant: :cricket: This is fixed in the current version by having `MIN_INPUT` and `MAX_INPUT` constants in the outer section classes and by having these max input constants in the `Misc` class:

```java
public static final long ONE_POINT_FIVE_MILLION = 1_500_000;
public static final long FIVE_HUNDRED_BILLION = 500_000_000_000L;
public static final long TEN_TRILLION = 10_000_000_000_000L;
public static final long FIFTY_TRILLION = TEN_TRILLION * 5;
public static final long ONE_QUADRILLION = 1_000_000_000_000_000L;
public static final long FIVE_QUADRILLION = ONE_QUADRILLION * 5;
public static final long TEN_QUADRILLION = ONE_QUADRILLION * 10;
public static final long NINE_QUINTILLION = 9_000_000_000_000_000_000L;
```

</details>

#### Finding Factors of a Number and Their PFs

<details>
<summary>Info</summary>
<br/>

Throughout the history of this project, almost all calculations had only minor changes made to their implementation. A calculation that had a significant implementation change is finding factors of an input number and their prime factorizations (PFs). This is one of the calculations done by the [divisibility section](#divisibility--heavy_division_sign).

I learned from the brilliant.org number theory course mentioned close to the start of the "Project History" section that you can find the factors of a whole number > 1 by looking at that number's PF and finding the PFs within that PF, or the *subfactorizations* as I like to call them. For example, the PF of 36 is $2^2 \times 3^2$ and its subfactorizations are $2 \text{,}$ $3 \text{,}$ $2^2 \text{ (4),}$ $2 \times 3 \text{ (6),}$ $3^2 \text{ (9),}$ $2^2 \times 3 \text{ (12),}$ and $2 \times 3^2 \text{ (18).}$ In order to find subfactorizations programatically, you have to find combinations of prime factors and exponents. I used to not know how to do that and didn't bother to research it. Instead, I calculated the PF of an input number so it could be shown in the app and found the factors and their PFs by just using brute force and iterating through ints in the range of 2 to half of the input number and calculating the PFs of the factors. Calculating a PF has a time complexity of $O(\sqrt{n}) \text{.}$ Code for this was first added to the `Functions` class in the [commit from August 6<sup>th</sup>, 2020](https://github.com/ncschroeder/Number-Theory-Playground/commit/b83df662d79bf702c28d857371b6613476f0e455):

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

Some changes were made over time and this was the code for this calculation in the `Divisibility` class in the [commit from November 11<sup>th</sup>, 2022](https://github.com/ncschroeder/Number-Theory-Playground/commit/9adfdda4e34afe4c31b0947776dccc20a0c5d5b1):

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

In 2024, I got some practice with combinations by ~~doing~~ solving LeetCode problems with them. Then, I came up with a way to find subfactorizations by finding combinations of prime factors and exponents. I added the methods below to the `PrimeFactorization` class in the [commit from December 2<sup>nd</sup>, 2024](https://github.com/ncschroeder/Number-Theory-Playground/commit/d25c5d4fd789da760b8c2d84b567e41776827297). For the calculation, a `PrimeFactorization` object was created for an input number and `getFactorPfStrings` was called with it. `factorsAndPowers` was a map where the keys are factors and the values are exponents in a PF. At the time, I was incorrectly referring to exponents as powers.

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

When I made the AI version in 2026, Claude came up with a similar way to use the PF of a number to find its factors and their PFs. The code for this calculation looks a little different now and is no longer in the `PrimeFactorization` class but instead is in the `getFactorPfs` method in the [`DivisibilityPrimeFactorizationAnswer` class](https://github.com/ncschroeder/Number-Theory-Playground/blob/master/Spring_Website_Version/src/main/java/com/numbertheoryplayground/calculationsimpl/DivisibilityPrimeFactorizationAnswer.java) for the website version and the [`Divisibility` class](https://github.com/ncschroeder/Number-Theory-Playground/blob/075babeaf828a10d9d86084ede5dbdaa349bf4c8/Java_CLI_and_GUI_Versions/src/main/numbertheoryplayground/sectionclasses/outer/Divisibility.java#L425-L469) for the CLI and GUI versions.

</details>

