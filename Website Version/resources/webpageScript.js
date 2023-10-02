(function() {
    'use strict';

    const sectionHeading = document.getElementById('sectionHeading');
    const sectionInfo = document.getElementById('sectionInfo');
    const sectionDirections = document.getElementById('sectionDirections');
    const inputBox1 = document.getElementById('inputBox1');
    const inputBox2 = document.getElementById('inputBox2');
    const answerArea = document.getElementById('answerArea');
    const interactionContent = document.getElementById('interactionContent');
    let currentSection;

    function showHomeSection() {
        currentSection = 'home';
        sectionHeading.innerText = 'Home';

        sectionInfo.innerHTML = 
            '<p>Welcome to the Number Theory Playground. This project was first conceived by me, Nicholas Schroeder, in Fall 2019 when \
            I was in my final year as a computer science student at Missouri State University. I was thinking of software projects \
            I could work on. During the Summer of 2019, I took a number theory course on <a href=\'https://brilliant.org\'>brilliant.org</a>\
            and learned some interesting stuff. I came up with the idea that I could make a program that does calculations for some of the \
            concepts I learned from that course. In addition to some concepts from that course, I also added some concepts that I learned \
            from other sources such as my discrete math textbook, <i>Discrete Mathematics and Its Applications</i> by Kenneth Rosen, and \
            YouTube channels such as \
            <a href=\'https://www.youtube.com/channel/UCoxcjq-8xIDTYp3uz647V5A\'>Numberphile</a>, \
            <a href=\'https://www.youtube.com/channel/UC1_uAIS3r8Vu6JjXWvastJg\'>Mathologer</a>, \
            <a href=\'https://www.youtube.com/channel/UCYO_jab_esuFRV4b17AJtAw\'>3Blue1Brown</a>, and \
            <a href=\'https://www.youtube.com/c/zachstar/videos\'>Zach Star</a>.</p>';

        interactionContent.setAttribute('data-input-areas-visible', '0');
    }

    showHomeSection();

    document.getElementById('homeBtn').onclick = showHomeSection;

    function showSingleInputSection() {
        interactionContent.setAttribute('data-input-areas-visible', '1');
    }

    function showDoubleInputSection() {
        interactionContent.setAttribute('data-input-areas-visible', '2');
    }

    function clearAnswerArea() {
        answerArea.innerHTML = '';
    }

    function clearInputBoxesAndAnswerArea() {
        inputBox1.value = '';
        inputBox2.value = '';
        clearAnswerArea();
    }


    let minInputNumber, maxInputNumber;

    document.getElementById('primesBtn').onclick = () => {
        // Show prime numbers section
        currentSection = 'primes';
        sectionHeading.innerText = 'Prime Numbers';

        sectionInfo.innerHTML = 
            '<p>Prime numbers are numbers that are only divisible by 1 and themself. There are an infinite amount \
            of them. A number can be determined to be prime if it is not divisible by any prime numbers less \
            than or equal to the square root of that number. Fun fact: with the exception of 2 and 3, all prime \
            numbers are either 1 above or 1 below a multiple of 6.</p>';

        sectionDirections.innerText = 
            'Enter a number greater than or equal to 0 and less than or equal to 1 billion to get the first 30 prime numbers that \
            appear after that number.';

        minInputNumber = 0;
        maxInputNumber = 1_000_000_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    }

    document.getElementById('twinPrimesBtn').onclick = () => {
        // Show twin primes section
        currentSection = 'twinPrimes';
        sectionHeading.innerText = 'Twin Prime Numbers';

        sectionInfo.innerHTML = 
            '<p>Twin primes are prime numbers that differ by 2. It is conjectured that there are infinitely many of them. A conjecture is a \
            statement that is believed to be true but has not been proven to be.</p>';

        sectionDirections.innerText = 
            'Enter a number greater than or equal to 0 and less than or equal to 1 billion to get the first 20 pairs of prime numbers \
            that appear after that number.';

        minInputNumber = 0;
        maxInputNumber = 1_000_000_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    }
    
    document.getElementById('pfBtn').onclick = () => {
        currentSection = 'primeFactorization';
        sectionHeading.innerText = 'Prime Factorization';

        sectionInfo.innerHTML = 
            '<p>The fundamental theorem of arithmetic states that every integer greater than 1 can be expressed as the product of prime \
            numbers. This sequence of numbers is called it\'s prime factorization. There are some interesting applications for this. \
            Visit the GCD and LCM or the divisibility sections for some applications.</p>';

        sectionDirections.innerText = 
            'Enter a number greater than or equal to 2 and less than or equal to 10 thousand to get that number\'s prime factorization.';

        minInputNumber = 2;
        maxInputNumber = 10_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    }

    document.getElementById('divisBtn').onclick = () => {
        currentSection = 'divisibility';
        sectionHeading.innerText = 'Divisibility';

        sectionInfo.innerHTML = 
            '<p>The factors of a number are all the numbers that can evenly divide that number.</p>\
            \
            <p>Some special tricks can be used to find some of the factors of a number. If the sum of the digits of a \
            number is divisible by 3, then that number is divisible by 3. If the sum of the digits of a number is divisible \
            by 9, then that number is divisible by 9. If a number is even and divisible by 3, then it is also divisible by 6. \
            If the last 2 digits of a number is divisible by 4, then that number is divisible by 4. If the last 3 digits of a \
            number is divisible by 8, then that number is divisible by 8. If a number is divisible by both 3 and 4 then it is \
            also divisible by 12.</p>\
            \
            <p>Another way you can tell what factors a number has and how many factors it has is by looking at it\'s prime \
            factorization. To find the number of factors, you take all the powers of the prime factors, add 1 to each and \
            then multiply them all together. All the "sub-factorizations" of the prime factorization are the prime factorizations \
            of all the factors. Some examples of "sub-factorizations" are 2 and 2 * 3 in the prime factorization 2 * 3 * 5.</p>';

        sectionDirections.innerText = 
            'Enter a number greater than or equal to 2 and less than or equal to 10 thousand to get that number\'s divisibility info.';

        minInputNumber = 2;
        maxInputNumber = 10_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    }

    document.getElementById('gcdLcmBtn').onclick = () => {
        currentSection = 'gcdAndLcm';
        sectionHeading.innerText = 'GCD and LCM';

        sectionInfo.innerHTML = 
            `<p>GCD stands for Greatest Common Divisor and LCM stands for Least Common Multiple.</p>\
            \
            <p>One way to find the GCD and LCM of 2 numbers is to look at the prime factorizations of those numbers. \
            If those numbers do not have any common prime factors, then the GCD is 1. If they do have common prime \
            factors, then the prime factorization of the GCD consists of all the common prime factors and the power \
            of each factor is the minimum power of that factor in the 2 prime factorizations. The prime factorization \
            of the LCM consists of all factors that are in either of the prime factorizations of the 2 numbers. The \
            power for each factor is the maximum power of that factor in the 2 prime factorizations.</p>\
            \
            <p>The Euclidean algorithm can be used to find the GCD of 2 numbers, usually faster than calculating the \
            prime factorizations. For the Euclidean algorithm, first take 2 numbers. If the bigger number is divisible \
            by the smaller number, then the smaller number is the GCD. Otherwise, the GCD of the 2 numbers is the same \
            as the GCD of the smaller number and the remainder when the bigger number is divided by the smaller number. \
            Repeat.</p>`;

        sectionDirections.innerText = 
            `Enter 2 numbers greater than or equal to 2 and less than or equal to 10 thousand and greater than or equal to 2 to get \
            the GCD and LCM info of those numbers.`;

        minInputNumber = 2;
        maxInputNumber = 10_000;
        showDoubleInputSection();
        clearInputBoxesAndAnswerArea();
    }

    document.getElementById('goldbachBtn').onclick = () => {
        currentSection = 'goldbach';
        sectionHeading.innerText = 'Goldbach Conjecture';
        
        sectionInfo.innerHTML = 
            '<p>The Goldbach conjecture states that every even number greater than or equal to 4 can be expressed as the sum of 2 \
            prime numbers. It was named after 1700s mathematician Christian Goldbach. A conjecture is a statement that is believed \
            to be true but has not been proven to be true. The Goldbach conjecture has been verified to be true for all even numbers \
            greater than or equal to 4 and less than or equal to a very high number. I don\'t know this number off the top of my head \
            but it\'s way, way bigger than the maximum number you\'re allowed to use for input.</p>';

        sectionDirections.innerText = 
            'Enter an even number greater than or equal to 4 and less than or equal to 100 thousand to get the pairs of prime numbers \
            that sum to that number.';

        minInputNumber = 4;
        maxInputNumber = 100_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    }

    document.getElementById('pythagBtn').onclick = () => {
        currentSection = 'pythagTriples';
        sectionHeading.innerText = 'Pythagorean Triples';

        sectionInfo.innerHTML = 
            '<p>The Pythagorean theorem says that for the side lengths of a right triangle, the sum of the squares of the 2 short \
            sides equals the square of the long side (hypotenuse) or <var>a</var><sup>2</sup> + <var>b</var><sup>2</sup> = \
            <var>c</var><sup>2</sup>. There are an infinite amount of trios of integers that a, b, and c can be. These trios \
            are called Pythagorean triples.</p>';

        sectionDirections.innerText = 
            'Enter a number greater than or equal to 0 and less than or equal to 1 thousand to get the first 10 Pythagorean triples \
            after that number.';

        minInputNumber = 0;
        maxInputNumber = 1_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    }

    document.getElementById('twoSquareBtn').onclick = () => {
        currentSection = 'twoSquare';
        sectionHeading.innerText = 'Two Square Theorem';

        sectionInfo.innerHTML = 
            '<p>The two square theorem says that every prime number that is 1 above a multiple of 4 can be expressed \
            as the sum of 2 squares.</p>'

        sectionDirections.innerText = 
            'Enter a number greater than or equal to 0 and less than or equal to one billion to get the first number greater \
            than or equal to that number that is prime and is 1 above a multiple of 4 and the squares that sum to that number.';

        minInputNumber = 0;
        maxInputNumber = 1_000_000_000;

        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    }

    document.getElementById('fiboSeqBtn').onclick = () => {
        currentSection = 'fiboSeq';
        sectionHeading.innerText = 'Fibonacci-Like Sequences';

        sectionInfo.innerHTML = 
            `<p>For any sequence of numbers that starts with 2 numbers and has every following term be the sum of the 2 previous terms, \
            as this sequence goes on and on, the ratio between consecutive terms gets closer and closer to the Golden Ratio, which \
            is approximately ${(1 + Math.sqrt(5)) / 2}. The most notable sequence of this type is the Fibonacci sequence, whose first \
            2 numbers are 1 and 1. Another notable sequence are the Lucas numbers, whose first 2 numbers are 2 and 1.</p>`;

        sectionDirections.innerText = 
            'Enter 2 numbers greater than 0 and less than or equal to one thousand to display the first 20 numbers of the sequence of \
            numbers that begins with those 2 numbers and every following is the sum of the 2 previous terms. The ratio between \
            the last 2 terms of that sequence will also be displayed.';

        minInputNumber = 1;
        maxInputNumber = 1_000;
        
        showDoubleInputSection();
        clearInputBoxesAndAnswerArea();
    }

    document.getElementById('egyptMultBtn').onclick = () => {
        currentSection = 'egyptMult';
        sectionHeading.innerText = 'Ancient Egyptian Multiplication';

        sectionInfo.innerHTML = 
            '<p>The Ancient Egyptians had an interesting algorithm for multiplication. Given 2 numbers, my way of explaining \
            the algorithm goes like this:</p>\
            <ol>\
                <li>Find all the powers of 2 that are less than or equal to the min of the 2 numbers. This could be done without \
                modern multiplication by starting with 1 and having each following term be the previous term added to itself.</li> \
                <li>Find the multiples of the max number that correspond to all these powers of 2. Like with the powers of 2, this \
                could be done by starting with the max number and have each following term be the previous term added to itself.</li> \
                <li>Find the powers of 2 that sum up to the min number.</li> \
                <li>Sum up the max number multiples that correspond to these powers of 2.</li> \
            </ol> \
            <p>This gives us the product of the 2 numbers.</p>';

        sectionDirections.innerText = 
            'Enter 2 numbers to get the ancient Egyptian multiplication info for those numbers. Have these numbers be greater than or \
            equal to 2 and less than or equal to one thousand';

        minInputNumber = 2;
        maxInputNumber = 1_000;

        showDoubleInputSection();
        clearInputBoxesAndAnswerArea();
    }

    document.getElementById('calculateBtn').onclick = () => {
        // Do nothing if inputBox1 is empty
        if (inputBox1.value === '') {
            return;
        }

        let inputNumber1 = Math.floor(inputBox1.value);
        if (inputNumber1 < minInputNumber) {
            inputNumber1 = minInputNumber;
        } else if (inputNumber1 > maxInputNumber) {
            inputNumber1 = maxInputNumber;
        }
        inputBox1.value = inputNumber1;

        let apiUrl = `calculations?section=${currentSection}&`;

        // check for sections that use 2 input boxes
        if (['gcdAndLcm', 'fiboSeq', 'egyptMult'].includes(currentSection)) {
            // Do nothing if inputBox2 is empty
            if (inputBox2.value === '') {
                return;
            }

            var inputNumber2 = Math.floor(inputBox2.value);
            if (inputNumber2 < minInputNumber) {
                inputNumber2 = minInputNumber;
            } else if (inputNumber2 > maxInputNumber) {
                inputNumber2 = maxInputNumber;
            }
            inputBox2.value = inputNumber2;
            apiUrl += `firstNumber=${inputNumber1}&secondNumber=${inputNumber2}`;
        } else {
            // Goldbach section must have even number
            if (currentSection === 'goldbach' && inputNumber1 % 2 !== 0) {
                inputNumber1++;
                inputBox1.value = inputNumber1;
            }
            apiUrl += `number=${inputNumber1}`;
        }

        const apiRequest = new XMLHttpRequest();
        apiRequest.responseType = 'json';
        apiRequest.open('GET', apiUrl);

        function getErrorMessageNode() {
            return document.createTextNode('Aw snap, there was a problem');
        }

        apiRequest.onload = function() {
            let newAnswerAreaElement;
            switch (currentSection) {
                case 'primes': {
                    const primes = this.response;
                    newAnswerAreaElement = getPrimesElement(inputNumber1, primes);
                    break;
                }
                
                case 'twinPrimes': {
                    const twinPrimePairs = this.response;
                    newAnswerAreaElement = getTwinPrimesElement(inputNumber1, twinPrimePairs);
                    break;
                }

                case 'primeFactorization': {
                    const pfArray = this.response;
                    newAnswerAreaElement = getPfElement(inputNumber1, pfArray);
                    break;
                }

                case 'divisibility': {
                    const infoObject = this.response;
                    newAnswerAreaElement = getDivisInfoDiv(inputNumber1, infoObject);
                    break;
                }

                case 'gcdAndLcm': {
                    const infoObject = this.response;
                    newAnswerAreaElement = getGcdAndLcmInfoDiv(inputNumber1, inputNumber2, infoObject);
                    break;
                }

                case 'goldbach': {
                    const pairs = this.response;
                    newAnswerAreaElement = getGoldbachElement(inputNumber1, pairs);
                    break;
                }

                case 'pythagTriples': {
                    const triples = this.response;
                    newAnswerAreaElement = getPythagTriplesElement(inputNumber1, triples);
                    break;
                }

                case 'twoSquare': {
                    const twoSquareInfo = this.response;
                    newAnswerAreaElement = getTwoSquareInfoElement(inputNumber1, twoSquareInfo);
                    break;
                }

                case 'fiboSeq': {
                    const fiboSeqInfo = this.response;
                    newAnswerAreaElement = getFiboSeqInfoElement(inputNumber1, inputNumber2, fiboSeqInfo);
                    break;
                }

                case 'egyptMult': {
                    const infoObject = this.response;
                    newAnswerAreaElement = getEgyptMultInfoElement(inputNumber1, inputNumber2, infoObject);
                    break;
                }

                default:
                    newAnswerAreaElement = getErrorMessageNode();
            }

            if (answerArea.firstChild) {
                answerArea.replaceChild(newAnswerAreaElement, answerArea.firstChild);
            } else {
                answerArea.appendChild(newAnswerAreaElement);
            }
        }

        apiRequest.onerror = function() {
            if (answerArea.firstChild) {
                answerArea.replaceChild(getErrorMessageNode(), answerArea.firstChild);
            } else {
                answerArea.appendChild(getErrorMessageNode());
            }
        }

        apiRequest.send();
    }

    /**
     * 
     * @param {number | string} number 
     * @returns 
     */
    function getNumberStringWithCommas(number) {
        const numberString = number.toString();
        let numberStringWithCommas = '';
        let digitsAddedSinceLastComma = 0;
        for (let i = numberString.length - 1; i >= 0; i--) {
            if (digitsAddedSinceLastComma === 3) {
                numberStringWithCommas = ',' + numberStringWithCommas;
                digitsAddedSinceLastComma = 0;
            }
            const digit = numberString.charAt(i);
            numberStringWithCommas = digit + numberStringWithCommas;
            digitsAddedSinceLastComma++;
        }
        return numberStringWithCommas;
    }

    /**
     * 
     * @param {Array<any>} array 
     * @returns An html ordered list element where each list item has an inner text of each element of the argument
     * array.
     */
    function getOlFromArray(array) {
        const ol = document.createElement('ol');
        for (const element of array) {
            const li = document.createElement('li');
            li.innerText = element;
            ol.appendChild(li);
        }
        return ol;
    }

    /**
     * 
     * @param {number} number 
     * @param {Array.<number>} primesArray Contains first 30 prime numbers that appear after the argument number.
     * @returns A div that contains a heading and ordered list of the primes in primesArray.
     */
    function getPrimesElement(number, primesArray) {
        const div = document.createElement('div');
        const heading = document.createElement('h3');
        heading.innerText = `The first 30 primes after ${getNumberStringWithCommas(number)} are`;
        div.appendChild(heading);
        const primesOl = getOlFromArray(primesArray);
        primesOl.className = 'answerList';
        div.appendChild(primesOl);
        return div;
    }

    /**
     * 
     * @param {number} number 
     * @param {Array.<string>} twinPrimesArray Contains first 20 pairs of twin prime numbers that appear after the argument number.
     * @returns A div with a heading and ordered list of the twin prime pairs in twinPrimesArray.
     */
    function getTwinPrimesElement(number, twinPrimesArray) {
        const div = document.createElement('div');
        const heading = document.createElement('h3');
        heading.innerText = `The first 20 pairs of twin primes after ${getNumberStringWithCommas(number)} are`;
        div.appendChild(heading);
        const twinPrimesOl = getOlFromArray(twinPrimesArray);
        twinPrimesOl.className = 'answerList';
        div.appendChild(twinPrimesOl);
        return div;
    }

    /**
     * 
     * @param {object} pfObject An object representing a number's prime factorization. The keys are the prime factors and the 
     * values are the powers that those prime factors are raised to in the prime factorization of a number.
     * @param {Array} pfArray
     * 
     * @returns A span element that contains the prime factors and powers of pfObject. The prime factors are displayed
     * as normal text. If a power is greater than 1, it is displayed in a superscript element following it's prime factor. Each 
     * prime factor and it's power if applicable is separated by an ' x '.
     */
    function getPfSpan(pfArray) {
        const span = document.createElement('span');
        span.className = 'pfSpan';
        for (let i = 0; i < pfArray.length; i++) {
            const [primeFactor, power] = pfArray[i];
            span.insertAdjacentText('beforeend', primeFactor);
            if (power != 1) {
                // display power as superscript text
                const supEl = document.createElement('sup');
                supEl.innerText = power;
                span.appendChild(supEl);
            }

            // Add ' x ' between each prime factor and it's power
            if (i !== pfArray.length - 1) {
                span.insertAdjacentText('beforeend', ' x ');
            }
        }
        return span;
    }

    function getPfElement(number, pfArray) {
        const div = document.createElement('div');
        const heading = document.createElement('h3');
        heading.innerText = `The prime factorization of ${getNumberStringWithCommas(number)} is`;
        div.appendChild(heading);
        div.appendChild(getPfSpan(pfArray));
        return div;
    }

    /**
     * 
     * @param {number} number The number to display divisibility info for.
     * @param {object} infoObject An object that is obtained by making an API call for divisibility info.
     * @returns A div html element that contains divisibility info about the argument number based on the info in infoObject.
     */
    function getDivisInfoDiv(number, infoObject) {
        const div = document.createElement('div');

        const tricksHeading = document.createElement('h3');
        tricksHeading.innerText = 'Divisibility info acquired by using special tricks';
        div.appendChild(tricksHeading);

        const tricksParagraph = document.createElement('p');
        tricksParagraph.innerText = infoObject.tricksInfo;
        div.appendChild(tricksParagraph);

        const pfHeading = document.createElement('h3');
        pfHeading.innerText = 'Info acquired from the prime factorization';
        div.appendChild(pfHeading);

        const {pfInfo} = infoObject;
        const pfInfoParagraph = document.createElement('p');
        const numberString = getNumberStringWithCommas(number);
        pfInfoParagraph.insertAdjacentText('beforeend', `The prime factorization for ${numberString} is `);

        if (pfInfo.isPrime) {
            pfInfoParagraph.insertAdjacentText('beforeend', `${numberString}. ${numberString} is prime and doesn't have any factors other than itself and 1`);
            div.appendChild(pfInfoParagraph);
        } else {
            pfInfoParagraph.appendChild(getPfSpan(pfInfo.pf));
            pfInfoParagraph.insertAdjacentText('beforeend', `. ${pfInfo.numberOfFactorsInfo}`);
            div.appendChild(pfInfoParagraph);

            div.insertAdjacentHTML('beforeend', '<h4>By looking at all the "sub-factorizations", we can see the factors are</h4>');
            // const factorsHeading = document.createElement('h4');
            // factorsHeading.innerText = 'By looking at all the "sub-factorizations", we can see the factors are';
            // div.appendChild(factorsHeading);

            const factorsOl = document.createElement('ol');
            factorsOl.className = 'answerList';

            for (const factor of pfInfo.factors) {
                const factorLi = document.createElement('li');
                if (factor.isPrime) {
                    factorLi.innerText = factor.number;
                } else {
                    factorLi.appendChild(getPfSpan(factor.pf));
                    factorLi.insertAdjacentText('beforeend', ` (${factor.number})`);
                }
                factorsOl.appendChild(factorLi);
            }
            div.appendChild(factorsOl);
        }
        return div;
    }

    /**
     * 
     * @param {number} firstNumber 
     * @param {number} secondNumber 
     * @param {object} infoObject An object obtained by making an API call for GCD and LCM info.
     * @returns An html div element that contains info about the GCD and LCM of firstNumber and secondNumber. The info is based on
     * the info in infoObject.
     */
    function getGcdAndLcmInfoDiv(firstNumber, secondNumber, infoObject) {
        const mainDiv = document.createElement('div');

        const euclideanDiv = document.createElement('div');
        const euclideanTable = document.createElement('table');
        euclideanTable.innerHTML = 
            '<caption>Euclidean Algorithm Info</caption>\
            <thead>\
                <tr>\
                    <th>Max Number</th>\
                    <th>Min Number</th>\
                    <th>Remainder when Max Number is divided by Min Number</th>\
                </tr>\
            </thead>\
            <tbody></tbody>';
        let tableBody = euclideanTable.lastElementChild;
        let tableRow, dataCell;
        const {euclideanInfo} = infoObject;
        for (const iteration of euclideanInfo) {
            tableRow = document.createElement('tr');
            tableRow.innerHTML = '<td></td>'.repeat(3);
            tableRow.firstChild.innerText = iteration.maxNumber;
            tableRow.firstChild.nextSibling.innerText = iteration.minNumber;
            tableRow.lastChild.innerText = iteration.remainder;
            tableBody.appendChild(tableRow);
        }
        euclideanDiv.appendChild(euclideanTable);

        const firstNumberString = getNumberStringWithCommas(firstNumber);
        const secondNumberString = getNumberStringWithCommas(secondNumber);

        let gcd = euclideanInfo[euclideanInfo.length - 1].minNumber;
        let paragraph = document.createElement('p');
        paragraph.innerText = `The GCD of ${firstNumberString} and ${secondNumberString} is ${gcd}`;
        paragraph.style.textAlign = 'center';
        euclideanDiv.appendChild(paragraph);
        euclideanDiv.style.marginBottom = '1rem';

        mainDiv.appendChild(euclideanDiv);
        
        const pfTable = document.createElement('table');
        pfTable.innerHTML = 
            '<caption>Prime Factorization Info</caption>\
            <thead>\
                <tr>\
                    <th></th>\
                    <th>Number</th>\
                    <th>Prime Factorization</th>\
                </tr>\
            </thead>\
            <tbody>\
                <tr></tr>\
                <tr></tr>\
                <tr></tr>\
                <tr></tr>\
            </tbody>';
        
        const {pfInfo} = infoObject;
        tableBody = pfTable.lastElementChild;
        // tableBody = document.createElement('tbody');

        // tableRow = document.createElement('tr');
        const input1InfoRow = tableBody.firstChild;
        input1InfoRow.innerHTML = '<th>Input 1</th> <td></td> <td></td>';
        input1InfoRow.firstChild.nextSibling.innerText = firstNumberString;
        input1InfoRow.lastChild.appendChild(getPfSpan(pfInfo.firstNumberPf));
        // tableBody.appendChild(input1InfoRow);
        // dataCell = document.createElement('td');
        // dataCell.innerText = firstNumberString;
        // tableRow.appendChild(dataCell);
        // dataCell = document.createElement('td');
        // dataCell.appendChild(getPfSpan(pfInfo.firstNumberPf));
        // tableRow.appendChild(dataCell);
        // tableBody.appendChild(tableRow);

        // tableRow = document.createElement('tr');
        const input2InfoRow = input1InfoRow.nextSibling;
        input2InfoRow.innerHTML = '<th>Input 2</th> <td></td> <td></td>';
        input2InfoRow.firstChild.nextSibling.innerText = secondNumberString;
        input2InfoRow.lastChild.appendChild(getPfSpan(pfInfo.secondNumberPf));
        // dataCell = document.createElement('td');
        // dataCell.innerText = secondNumberString;
        // tableRow.appendChild(dataCell);
        // dataCell = document.createElement('td');
        // dataCell.appendChild(getPfSpan(pfInfo.secondNumberPf));
        // tableRow.appendChild(dataCell);
        // tableBody.appendChild(tableRow);

        gcd = pfInfo.gcd;
        const gcdInfoRow = input2InfoRow.nextSibling;
        // tableRow = document.createElement('tr');
        gcdInfoRow.innerHTML = '<th>GCD</th> <td></td> <td></td>';
        gcdInfoRow.firstChild.nextSibling.innerText = gcd.number;
        // const gcdPfCell = tableRow.lastChild;
        if (gcd.number == 1) {
            // No common prime factors
            gcdInfoRow.lastChild.innerText = 'No common prime factors so no prime factorization';
        } else {
            if (gcd.isPrime) {
                gcdInfoRow.lastChild.innerText = gcd.number;
            } else {
                gcdInfoRow.lastChild.appendChild(getPfSpan(gcd.pf));
            }
        }
        // dataCell = document.createElement('td');
        // dataCell.innerText = gcd.number;
        // tableRow.appendChild(dataCell);
        // dataCell = document.createElement('td');
        // if (gcd.number == 1) {
        //     // No common prime factors
        //     dataCell.innerText = 'No common prime factors so no prime factorization';
        // } else {
        //     if (gcd.isPrime) {
        //         dataCell.innerText = gcd.number;
        //     } else {
        //         dataCell.appendChild(getPfSpan(gcd.pf));
        //     }
        // }
        // tableRow.appendChild(dataCell);
        tableBody.appendChild(tableRow);

        const {lcm} = pfInfo;
        tableRow = document.createElement('tr');
        tableRow.innerHTML = '<th>LCM</th> <td></td> <td></td>';
        tableRow.firstChild.nextSibling.innerText = lcm.number;
        if (lcm.isPrime) {
            tableRow.lastChild.innerText = lcm.number;
        } else {
            tableRow.lastChild.appendChild(getPfSpan(lcm.pf));
        }
        // dataCell = document.createElement('td');
        // dataCell.innerText = lcm.number;
        // tableRow.appendChild(dataCell);
        // dataCell = document.createElement('td');

        // if (lcm.isPrime) {
        //     dataCell.innerText = lcm.number;
        // } else {
        //     dataCell.appendChild(getPfSpan(lcm.pf));
        // }
        // tableRow.appendChild(dataCell);
        tableBody.appendChild(tableRow);
        // pfTable.appendChild(tableBody);

        mainDiv.appendChild(pfTable);
        return mainDiv;
    }

    /**
     * 
     * @param {number} number 
     * @param {Array.<string>} goldbachPrimePairsArray An array of strings. Each one has 2 prime numbers 
     * that sum to the argument number and each number is separated by ' and '.
     * @returns A div with a heading and ordered list of the the contents of goldbachPrimePairsArray.
     */
    function getGoldbachElement(number, goldbachPrimePairsArray) {
        const div = document.createElement('div');
        const heading = document.createElement('h3');
        heading.innerText = `The pairs of prime numbers that sum to ${getNumberStringWithCommas(number)} are`;
        div.appendChild(heading);
        const pairsOl = getOlFromArray(goldbachPrimePairsArray);
        pairsOl.className = 'answerList';
        div.appendChild(pairsOl);
        return div;
    }

    /**
     * 
     * @returns A superscript element with 2 as it's inner text
     */
    function getSupElWith2() {
        const supEl = document.createElement('sup');
        supEl.innerText = '2';
        return supEl;
    }

    /**
     * 
     * @param {number} number
     * @param {Array.<object>} pythagTriplesArray Contains 10 info objects for Pythagorean triples that appear after the argument number. This array 
     * is obtained by making an API call for Pythagorean triples.
     * @returns An html div element that contains the Pythagorean triples that are in pythagTriplesArray.
     */
    function getPythagTriplesElement(number, pythagTriplesArray) {
        const div = document.createElement('div');
        const heading = document.createElement('h3');
        heading.innerText = `The first 10 Pythagorean triples after ${number} are`;
        div.appendChild(heading);

        /**
         * 
         * @param {number} number 
         * @param {number} square
         * @returns A span element with the number argument followed by a 2 in a superscript element followed
         * by the square argument surrounded by parentheses. 
         */
        function getNumberAndSquareSpan(number, square) {
            const span = document.createElement('span');
            span.insertAdjacentText('beforeend', number);
            span.appendChild(getSupElWith2());
            span.insertAdjacentText('beforeend', ` (${square})`);
            return span;
        }

        const ol = document.createElement('ol');

        for (const tripleObject of pythagTriplesArray) {
            const li = document.createElement('li');
            const {numbers, squares} = tripleObject;
            li.appendChild(getNumberAndSquareSpan(numbers[0], squares[0]));
            li.insertAdjacentText('beforeend', ' + ');
            li.appendChild(getNumberAndSquareSpan(numbers[1], squares[1]));
            li.insertAdjacentText('beforeend', ' = ');
            li.appendChild(getNumberAndSquareSpan(numbers[2], squares[2]));
            ol.appendChild(li);
        }

        div.appendChild(ol);
        return div;
    }

    /**
     * 
     * @param {number} inputNumber
     * @param {object} infoObject An object obtained by making an API call for two square info.
     * @returns A paragraph element that contains info that is to be displayed to the user. This info is from inputNumber and infoObject.
     */
    function getTwoSquareInfoElement(inputNumber, infoObject) {
        const {primeNumber, numbers, squares} = infoObject;
        const paragraph = document.createElement('p');
        paragraph.innerText = 
            `The number greater than or equal to ${getNumberStringWithCommas(inputNumber)} that is prime and is 1 above a \
            multiple of 4 is ${primeNumber}, which is equal to ${numbers[0]}`;
        paragraph.appendChild(getSupElWith2());
        paragraph.insertAdjacentText('beforeend', ` (${squares[0]}) + ${numbers[1]}`);
        paragraph.appendChild(getSupElWith2());
        paragraph.insertAdjacentText('beforeend', ` (${squares[1]}).`);
        return paragraph;
    }

    /**
     * 
     * @param {number} inputNumber1
     * @param {number} inputNumber2
     * @param {object} infoObject An object obtained by making an API call for Fibonacci like sequence info
     * @returns A div element that is to be displayed to the user that contains info from the input numbers and infoObject.
     */
    function getFiboSeqInfoElement(inputNumber1, inputNumber2, infoObject) {
        const div = document.createElement('div');

        const heading = document.createElement('h3');
        heading.innerText = `The first 20 terms of the sequence that starts with ${inputNumber1} and ${inputNumber2} are`;
        div.appendChild(heading);

        const {sequence} = infoObject;
        const sequenceOl = getOlFromArray(sequence);
        sequenceOl.className = 'answerList';
        div.appendChild(sequenceOl);

        const paragraph = document.createElement('p');
        paragraph.innerText = `The ratio of ${sequence[sequence.length - 1]} to ${sequence[sequence.length - 2]} is ${infoObject.endRatio}`;
        paragraph.style.textAlign = 'center';
        div.appendChild(paragraph);

        return div;
    }

    /**
     * 
     * @param {number} inputNumber1 
     * @param {number} inputNumber2 
     * @param {object} infoObject An object obtained from making an API call for ancient Egyptian multiplication info.
     * @returns A div element that contains info from infoObject and the input numbers.
     */
    function getEgyptMultInfoElement(inputNumber1, inputNumber2, infoObject) {
        const div = document.createElement('div');

        const heading = document.createElement('h3');
        heading.innerText = `Ancient Egyptian Multiplication info for ${inputNumber1} and ${inputNumber2}`;
        div.appendChild(heading);

        const {minNumber, maxNumber} = infoObject;

        const paragraph = document.createElement('p');
        paragraph.innerText = `The min number is ${minNumber} and the max number is ${maxNumber}`;
        div.appendChild(paragraph);

        const table1 = document.createElement('table');
        table1.innerHTML = 
            '<thead>\
                <tr>\
                    <th></th>\
                    <th></th>\
                </tr>\
            </thead>';
        const headerRow = table1.firstChild.firstChild;
        // let tableHead = document.createElement('thead');
        // tableHead.innerHTML = '<tr> <th></th> <th></th> </tr>';
        headerRow.firstChild.innerText = `All powers of 2 less than or equal to ${minNumber}`;
        headerRow.lastChild.innerText = `Corresponding multiples of ${maxNumber}`;
        // let tableRow = document.createElement('tr');

        // let headerCell = document.createElement('th');
        // headerCell.innerText = `All powers of 2 less than or equal to ${minNumber}`;
        // tableRow.appendChild(headerCell);

        // headerCell = document.createElement('th');
        // headerCell.innerText = `Corresponding multiples of ${maxNumber}`;
        // tableRow.appendChild(headerCell);

        // tableHead.appendChild(tableRow);
        table1.appendChild(tableHead);

        const {powersOf2, maxNumberMultiples} = infoObject;
        let dataCell;

        // Add all powers of 2 and all max number multiples to table1
        for (let i = 0; i < powersOf2.all.length; i++) {
            tableRow = document.createElement('tr');

            dataCell = document.createElement('td');
            dataCell.innerText = powersOf2.all[i];
            tableRow.appendChild(dataCell);

            dataCell = document.createElement('td');
            dataCell.innerText = maxNumberMultiples.all[i];
            tableRow.appendChild(dataCell);

            table1.appendChild(tableRow);
        }

        div.appendChild(table1);
        
        const table2 = document.createElement('table');

        tableHead = document.createElement('thead');
        tableRow = document.createElement('tr');
        headerCell = document.createElement('th');
        headerCell.innerText = `Powers of 2 that sum to ${minNumber}`;
        tableRow.appendChild(headerCell);

        headerCell = document.createElement('th');
        headerCell.innerText = 
            `Corresponding multiples of ${maxNumber}. The sum of this column is ${infoObject.product}, which is the product.`;
        tableRow.appendChild(headerCell);
        tableHead.appendChild(tableRow);

        table2.appendChild(tableHead);

        for (let i = 0; i < powersOf2.sumToMinNumber.length; i++) {
            tableRow = document.createElement('tr');

            dataCell = document.createElement('td');
            dataCell.innerText = powersOf2.sumToMinNumber[i];
            tableRow.appendChild(dataCell);

            dataCell = document.createElement('td');
            dataCell.innerText = maxNumberMultiples.sumToProduct[i];
            tableRow.appendChild(dataCell);

            table2.appendChild(tableRow);
        }

        const tableContainer = document.createElement('div');
        tableContainer.id = 'egyptMultTableContainer';
        tableContainer.appendChild(table1);
        tableContainer.appendChild(table2);

        div.appendChild(tableContainer);

        return div;
    }

    /**
     * Either randomizes, increments, or decrements the input box value
     * @param {1 | 2} inputBoxNumber Should be 1 or 2 for the first or second input box, respectively
     * @param {'increment' | 'decrement' | 'randomize'} action Should be either 'increment', 'decrement', or 'randomize'
     */
    function changeInputBoxValue(inputBoxNumber, action) {
        let inputBox;
        if (inputBoxNumber === 1) {
            inputBox = inputBox1;
        } else if (inputBoxNumber === 2) {
            inputBox = inputBox2;
        } else {
            throw 'Invalid input box number: ' + inputBoxNumber;
        }

        if (!['randomize', 'increment', 'decrement'].includes(action)) {
            throw 'invalid action: ' + action;
        }

        if (action === 'randomize') {
            // Random number generation based on function from 
            // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
            let randomNumber = Math.floor(Math.random() * (maxInputNumber - minInputNumber + 1) + minInputNumber);

            // Goldbach section must have even number
            if (currentSection === 'goldbach' && randomNumber % 2 !== 0) {
                randomNumber++;
            }

            inputBox.value = randomNumber;
        } else {
            const numberInBox = Math.floor(inputBox.value);
            let newNumberInBox;
            if (action === 'increment') {
                if (inputBox.value === '' || numberInBox >= maxInputNumber || numberInBox < minInputNumber) {
                    inputBox.value = minInputNumber;
                } else {
                    newNumberInBox = numberInBox + 1;
                    // Make even number for Goldbach section
                    if (currentSection === 'goldbach' && newNumberInBox % 2 !== 0) {
                        newNumberInBox++;
                    }
                    inputBox.value = newNumberInBox;
                }
            } else {
                // action is 'decrement'
                if (inputBox.value === '' || numberInBox <= minInputNumber || numberInBox > maxInputNumber) {
                    inputBox.value = maxInputNumber;
                } else {
                    newNumberInBox = numberInBox - 1;
                    // Make even number for Goldbach section
                    if (currentSection === 'goldbach' && newNumberInBox % 2 !== 0) {
                        newNumberInBox--;
                    }
                    inputBox.value = newNumberInBox;
                }
            }
        }
    }

    document.getElementById('randomBtn1').onclick = () => {
        changeInputBoxValue(1, 'randomize');
    }

    document.getElementById('randomBtn2').onclick = () => {
        changeInputBoxValue(2, 'randomize');
    }

    document.getElementById('incrementBtn1').onclick = () => {
        changeInputBoxValue(1, 'increment');
    }

    document.getElementById('incrementBtn2').onclick = () => {
        changeInputBoxValue(2, 'increment'); 
    }

    document.getElementById('decrementBtn1').onclick = () => {
        changeInputBoxValue(1, 'decrement');
    }

    document.getElementById('decrementBtn2').onclick = () => {
        changeInputBoxValue(2, 'decrement');
    }

})();