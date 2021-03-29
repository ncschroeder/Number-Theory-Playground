(function() {
    'use strict';

    const sectionInfo = document.getElementById('sectionInfo');
    const sectionHeading = document.getElementById('sectionHeading');
    const inputArea1 = document.getElementById('inputArea1');
    const inputBox1 = document.getElementById('inputBox1');
    const inputArea2 = document.getElementById('inputArea2');
    const inputBox2 = document.getElementById('inputBox2');
    const calculateBtn = document.getElementById('calculateBtn');
    const answerArea = document.getElementById('answerArea');
    const inputAreaContainer = document.getElementById('inputAreaContainer');
    const interactionContent = document.getElementById('interactionContent');

    let currentSection;

    function showHomeSection() {
        currentSection = 'home';
        sectionHeading.innerText = 'Home';
        sectionInfo.textContent = 'Welcome to the Number Theory Playground';
        interactionContent.setAttribute('data-input-areas-visible', '0');
        // inputArea1.style.visibility = 'hidden';
        // inputArea2.style.display = 'none';
        // calculateBtn.style.visibility = 'hidden';
        // answerArea.style.visibility = 'hidden';
        // interactionContent.forEach(e => e.style.visibility = 'hidden');
        // document.querySelectorAll('#interactionContent').forEach(e => e.style.visibility = 'hidden');
        // for (let i = 0; i < interactionContent.length; i++) {
        //     interactionContent[i].style.visibility = 'hidden';
        // }
    };

    showHomeSection();

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

    document.getElementById('homeBtn').addEventListener('click', showHomeSection);

    function showSingleInputSection() {
        interactionContent.setAttribute('data-input-areas-visible', '1');
        // inputArea1.style.visibility = 'visible';
        // // inputArea1
        // inputArea2.style.display = 'none';
        // calculateBtn.style.visibility = 'visible';
        // answerArea.style.visibility = 'visible';
    };

    function showDoubleInputSection() {
        interactionContent.setAttribute('data-input-areas-visible', '2');
        // inputArea1.style.visibility = 'visible';
        // inputArea2.style.display = 'grid';
        // calculateBtn.style.visibility = 'visible';
        // answerArea.style.visibility = 'visible';
    };

    function clearAnswerArea() {
        answerArea.innerHTML = '';
        // while (answerArea.lastChild) {
        //     answerArea.removeChild(answerArea.lastChild);
        // }
    }

    function clearInputBoxesAndAnswerArea() {
        inputBox1.value = '';
        inputBox2.value = '';
        clearAnswerArea();
    };

    // function hideContent

    let minInputNumber, maxInputNumber;

    document.getElementById('primesBtn').addEventListener('click', function() {
        // Show prime numbers section
        currentSection = 'primes';
        sectionHeading.innerText = 'Prime Numbers';
        sectionInfo.innerText = 
            'Enter a number greater than or equal to 0 and less than or equal to 1 billion to get the first 30 prime numbers that appear after that number';
        minInputNumber = 0;
        maxInputNumber = 1_000_000_000;
        // inputBox1.setAttribute('min', 0);
        // inputBox1.setAttribute('max', maxInputNumber.toString());
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    });

    document.getElementById('twinPrimesBtn').addEventListener('click', function() {
        // Show twin primes section
        currentSection = 'twinPrimes';
        sectionHeading.innerText = 'Twin Prime Numbers';
        sectionInfo.innerText = 
            'Enter a number greater than or equal to 0 and less than or equal to 1 billion to get the first 20 pairs of prime numbers that appear after that number';
        minInputNumber = 0;
        maxInputNumber = 1_000_000_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    });
    
    document.getElementById('pfBtn').addEventListener('click', function() {
        currentSection = 'primeFactorization';
        sectionHeading.innerText = 'Prime Factorization';
        sectionInfo.innerHTML = 
            'Enter a number greater than or equal to 2 and less than or equal to 10 thousand to get that number\'s prime factorization';
        minInputNumber = 2;
        maxInputNumber = 10_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    });

    document.getElementById('divisBtn').addEventListener('click', function() {
        currentSection = 'divisibility';
        sectionHeading.innerText = 'Divisibility';
        sectionInfo.innerHTML = 
            'Enter a number greater than or equal to 2 and less than or equal to 10 thousand to get that number\'s divisibility info';
        minInputNumber = 2;
        maxInputNumber = 10_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    });

    document.getElementById('gcdLcmBtn').addEventListener('click', function() {
        currentSection = 'gcdAndLcm';
        sectionHeading.innerText = 'GCD and LCM';
        sectionInfo.innerHTML = 
            'Enter 2 numbers greater than or equal to 2 and less than or equal to 10 thousand and greater than or equal to 2 to get the GCD and LCM info of those numbers';
        minInputNumber = 2;
        maxInputNumber = 10_000;
        showDoubleInputSection();
        clearInputBoxesAndAnswerArea();
    });

    document.getElementById('goldbachBtn').addEventListener('click', function() {
        currentSection = 'goldbach';
        sectionHeading.innerText = 'Goldbach Conjecture';
        sectionInfo.innerHTML = 
            'Enter an even number greater than or equal to 4 and less than or equal to 100 thousand to get the pairs of prime numbers ' + 
            'that sum to that number';
        minInputNumber = 4;
        maxInputNumber = 100_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    });

    document.getElementById('pythagBtn').addEventListener('click', function() {
        currentSection = 'pythagTriples';
        sectionHeading.innerText = 'Pythagorean Triples';
        sectionInfo.innerHTML = 
            'Enter a number greater than or equal to 0 and less than or equal to 1 thousand to get the first 10 Pythagorean triples after that number';
        minInputNumber = 0;
        maxInputNumber = 1_000;
        showSingleInputSection();
        clearInputBoxesAndAnswerArea();
    });

    document.getElementById('calculateBtn').addEventListener('click', function() {
        // console.log(inputBox1.value);
        // Do nothing if inputBox1 is empty
        if (inputBox1.value === '') {
            return;
        }

        let inputNumber1 = Math.floor(inputBox1.value);
        if (inputNumber1 < minInputNumber) {
            inputNumber1 = minInputNumber;
        } else if (inputNumber1 > maxInputNumber) {
            inputNumber1 = minInputNumber;
        }
        inputBox1.value = inputNumber1;

        let apiUrl;
        // gcd and lcm section is the only one to use 2 input boxes
        if (currentSection === 'gcdAndLcm') {
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
            apiUrl = `/calculations?section=gcdAndLcm&firstNumber=${inputNumber1}&secondNumber=${inputNumber2}`;
        } else {
            // Goldbach section must have even number
            if (currentSection === 'goldbach' && inputNumber1 % 2 !== 0) {
                inputNumber1++;
                inputBox1.value = inputNumber1;
            }
            apiUrl = `calculations?section=${currentSection}&number=${inputNumber1}`;
        }

        const xhr = new XMLHttpRequest();
        xhr.responseType = 'json';

        function getErrorMessageNode() {
            return document.createTextNode('Aw snap, there was a problem');
        }

        xhr.onload = function() {
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
                    newAnswerAreaElement = getPythagTriplesDiv(inputNumber1, triples);
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

        xhr.onerror = function() {
            if (answerArea.firstChild) {
                answerArea.replaceChild(getErrorMessageNode(), answerArea.firstChild);
            } else {
                answerArea.appendChild(getErrorMessageNode());
            }
        }

        xhr.open('GET', apiUrl);
        xhr.send();
    });

    /**
     * 
     * @param {number} number
     * @param {Array.<number>} primesArray Contains first 30 prime numbers that appear after the argument number.
     * @returns {HTMLTableElement} An html table that consists of a caption and rows consisting of data entries. Each data entry
     * contains an element of primesArray. There are 5 data entries in each row.
     */
    // function getPrimesTable(number, primesArray) {
    //     const table = document.createElement('table');
    //     const caption = document.createElement('caption');
    //     caption.innerText = `The first 30 primes after ${number} are`;
    //     table.appendChild(caption);

    //     let row = document.createElement('tr');
    //     for (const prime of primesArray) {
    //         const primeEntry = document.createElement('td');
    //         primeEntry.innerText = prime;
    //         row.appendChild(primeEntry);
    //         if (row.children.length === 5) {
    //             table.appendChild(row);
    //             row = document.createElement('tr');
    //         }
    //     }
    //     return table;
    // }

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
        primesOl.setAttribute('class', 'answerList');
        div.appendChild(primesOl);
        // const ol = document.createElement('ol');
        // ol.setAttribute('class', 'answerList');
        // for (const prime of primesArray) {
        //     const li = document.createElement('li');
        //     li.innerText = prime;
        //     ol.appendChild(li);
        // }
        // div.appendChild(ol);
        return div;
    }

    /**
     * 
     * @param {number} number
     * @param {Array.<string>} twinPrimesArray Contains first 20 pairs of twin prime numbers that appear after the argument number.
     * @returns {HTMLTableElement} An html table element that consists of a caption and rows of data entries. Each data entry 
     * contains an element of twinPrimesArray. There are 4 data entries in each row.
     */
    // function getTwinPrimesTable(number, twinPrimesArray) {
    //     const table = document.createElement('table');
    //     const caption = document.createElement('caption');
    //     caption.innerText = `The first 20 pairs of twin prime numbers after ${number} are`;
    //     table.appendChild(caption);
    //     let row = document.createElement('tr');
    //     for (const twinPrimePair of twinPrimesArray) {
    //         const entry = document.createElement('td');
    //         entry.innerText = twinPrimePair;
    //         row.appendChild(entry);
    //         if (row.children.length === 4) {
    //             table.appendChild(row);
    //             row = document.createElement('tr');
    //         }
    //     }
    //     return table;
    // }

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
        twinPrimesOl.setAttribute('class', 'answerList');
        div.appendChild(twinPrimesOl);
        // const ol = document.createElement('ol');
        // ol.setAttribute('class', 'answerList');
        // for (const pair of twinPrimesArray) {
        //     const li = document.createElement('li');
        //     li.innerText = pair;
        //     ol.appendChild(li);
        // }
        // div.appendChild(ol);
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
        // for (const primeFactor of pfArray) {
        //     span.appendChild(document.createTextNode(primeFactor));
        //     const power = pfObject[primeFactor];
        //     if (power != 1) {
        //         // display power as superscript text
        //         const supEl = document.createElement('sup');
        //         supEl.innerText = power;
        //         span.appendChild(supEl);
        //     }
        //     span.appendChild(document.createTextNode(' x '));
        // }
        // // Remove last ' x '
        // span.removeChild(span.lastChild);
    }

    /**
     * 
     * @param {number} number The number to display the prime factorization for.
     * @param {object} pfObject An object representing the argument number's prime factorization.
     * @returns An html p element that says what the prime factorization of the argument number is.
     */
    // function getPfParagraph(number, pfObject) {
    //     const paragraph = document.createElement('p');
    //     paragraph.style.textAlign = 'center';
    //     paragraph.innerText = `The prime factorization of ${number} is`;
    //     paragraph.appendChild(document.createElement('br'));
    //     paragraph.appendChild(getPfSpan(pfObject));
    //     // for (const primeFactor in pfObject) {
    //     //     paragraph.appendChild(document.createTextNode(primeFactor));
    //     //     const power = pfObject[primeFactor];
    //     //     if (power != 1) {
    //     //         // display power as superscript text
    //     //         const supEl = document.createElement('sup');
    //     //         supEl.innerText = power;
    //     //         paragraph.appendChild(supEl);
    //     //     }
    //     //     paragraph.appendChild(document.createTextNode(' x '));
    //     // }
    //     // // Remove last ' x '
    //     // paragraph.removeChild(paragraph.lastChild);
    //     return paragraph;
    // }

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

            const factorsHeading = document.createElement('h4');
            factorsHeading.innerText = 'By looking at all the "sub-factorizations", we can see the factors are';
            div.appendChild(factorsHeading);

            const factorsOl = document.createElement('ol');
            factorsOl.setAttribute('class', 'answerList');
            const {factors} = pfInfo;
            for (const factor of factors) {
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

            // for (const factor in factors) {
            //     const factorLi = document.createElement('li');
            //     if (factors[factor].isPrime) {
            //         factorLi.innerText = factor;
            //     } else {
            //         const factorPfObject = factors[factor].pf;
            //         factorLi.appendChild(getPfSpan(factorPfObject));
            //         factorLi.insertAdjacentText('beforeend', ` (${factor})`);
            //     }
            //     factorsOl.appendChild(factorLi);
            // }

            // Display factors and their prime factorizations in a table
            // const factorsTable = document.createElement('table');
            // const caption = document.createElement('caption');
            // caption.innerText = 'By looking at all the "sub-factorizations, we can see the factors are';
            // factorsTable.appendChild(caption);

            // let row = document.createElement('tr');
            // // console.log(factors);
            // const {factors} = pfInfo;
            // for (const factor in factors) {
            //     const factorEntry = document.createElement('td');
            //     if (factors[factor].isPrime) {
            //         // factor is prime so just display the factor
            //         factorEntry.innerText = factor;
            //     } else {
            //         const factorPfObject = factors[factor].pf;
            //         factorEntry.appendChild(getPfSpan(factorPfObject));
                    
            //         // Display the factor, which is the product of these prime factors, in parentheses
            //         factorEntry.appendChild(document.createTextNode(` (${factor})`));
            //     }

            //     row.appendChild(factorEntry);
            //     // Have each row contain a max of 5 entries
            //     if (row.children.length === 5) {
            //         factorsTable.appendChild(row);
            //         row = document.createElement('tr');
            //     }
            // }

            // If the amount of factors is not a multiple of 5 then there should be some children
            // in tableRow since the if statement above would not have been executed for the last 
            // few children.
            // if (row.children.length > 0) {
            //     factorsTable.appendChild(row);
            // }
            // div.appendChild(factorsTable);

    /**
     * 
     * @param {number} firstNumber 
     * @param {number} secondNumber 
     * @param {object} infoObject An object obtained by making an API call for GCD and LCM info.
     * @returns An html div element that contains info about the GCD and LCM of firstNumber and secondNumber. The info is based on
     * the info in infoObject.
     */
    function getGcdAndLcmInfoDiv(firstNumber, secondNumber, infoObject) {
        const div = document.createElement('div');

        const euclideanHeading = document.createElement('h3');
        euclideanHeading.innerText = 'Euclidean algorithm info';
        div.appendChild(euclideanHeading);

        const euclideanOL = document.createElement('ol');

        const {euclideanInfo} = infoObject;
        for (const infoLine of euclideanInfo) {
            const infoLI = document.createElement('li');
            infoLI.innerText = infoLine;
            euclideanOL.appendChild(infoLI);
        }
        div.appendChild(euclideanOL);

        const pfHeading = document.createElement('h3');
        pfHeading.innerText = 'Prime factorization info';
        div.appendChild(pfHeading);

        const {pfInfo} = infoObject;
        const pfOL = document.createElement('ol');
        let pfLi = document.createElement('li');
        pfLi.appendChild(document.createTextNode(`The prime factorization of ${getNumberStringWithCommas(firstNumber)} is `));
        pfLi.appendChild(getPfSpan(pfInfo.firstNumberPf));
        pfOL.appendChild(pfLi);

        pfLi = document.createElement('li');
        pfLi.appendChild(document.createTextNode(`The prime factorization of ${getNumberStringWithCommas(secondNumber)} is `));
        pfLi.appendChild(getPfSpan(pfInfo.secondNumberPf));
        pfOL.appendChild(pfLi);

        const {gcd} = pfInfo;
        pfLi = document.createElement('li');
        if (gcd.number == 1) {
            // No common prime factors
            pfLi.innerText = 'There are no common prime factors so the GCD is 1';
        } else {
            pfLi.insertAdjacentText('beforeend', 'The prime factorization of the GCD is ');
            if (gcd.isPrime) {
                pfLi.insertAdjacentText('beforeend', gcd.number);
            } else {
                pfLi.appendChild(getPfSpan(gcd.pf));
                pfLi.insertAdjacentText('beforeend', `, which is ${gcd.number}`);
            }
        }

        pfOL.appendChild(pfLi);
        
        const {lcm} = pfInfo;
        pfLi = document.createElement('li');
        pfLi.appendChild(document.createTextNode('The prime factorization of the LCM is '));
        if (lcm.isPrime) {
            pfLi.appendChild(document.createTextNode(lcm.number));
        } else {
            pfLi.appendChild(getPfSpan(lcm.pf));
            pfLi.appendChild(document.createTextNode(`, which is ${lcm.number}`));
        }
        pfOL.appendChild(pfLi);

        div.appendChild(pfOL);
        return div;
    }

    /**
     * 
     * @param {number} number 
     * @param {Array.<string>} goldbachPrimePairsArray An array of strings. Each one has 2 prime numbers that sum to the number argument
     * and each is separated by 'and'. This is an array that is obtained by making an API call for Goldbach prime pairs.
     * @returns An html table element that contains a caption and rows that contain data entries. Each data entry contains one of 
     * the elements of goldbachPrimePairsArray. There are a max of 4 data entries in each row.
     */
    // function getGoldbachTable(number, goldbachPrimePairsArray) {
    //     const table = document.createElement('table');
    //     const caption = document.createElement('caption');
    //     caption.innerText = `The prime number pairs that sum to ${number} are`;
    //     table.appendChild(caption);

    //     let row = document.createElement('tr');
    //     for (const pair of goldbachPrimePairsArray) {
    //         const pairEntry = document.createElement('td');
    //         pairEntry.innerText = pair;
    //         row.appendChild(pairEntry);
    //         if (row.children.length === 4) {
    //             table.appendChild(row);
    //             row = document.createElement('tr');
    //         }
    //     }

    //     // If the number of pairs is not a multiple of 4, there still will be pairs in row 
    //     // that weren't added to pairsTable yet since the if statement above would not have been 
    //     // executed to add the last row to table.
    //     if (row.children.length > 0) {
    //         table.appendChild(row);
    //     }

    //     return table;
    // }

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
        heading.innerText = `The pairs of prime numbers that sum to ${number} are`;
        div.appendChild(heading);
        const pairsOl = getOlFromArray(goldbachPrimePairsArray);
        pairsOl.setAttribute('class', 'answerList');
        div.appendChild(pairsOl);
        // const ol = document.createElement('ol');
        // ol.setAttribute('class', 'answerList');
        // for (const pair of goldbachPrimePairsArray) {
        //     const li = document.createElement('li');
        //     li.innerText = pair;
        //     ol.appendChild(li);
        // }
        // div.appendChild(ol);
        return div;
    }

    /**
     * 
     * @param {number} number
     * @param {Array.<object>} pythagTriplesArray Contains 10 info objects for Pythagorean triples that appear after the argument number. This array 
     * is obtained by making an API call for Pythagorean triples.
     * @returns An html ordered list element that contains the Pythagorean triples that are in pythagTriplesArray.
     */
    function getPythagTriplesDiv(number, pythagTriplesArray) {
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
            span.appendChild(document.createTextNode(number));

            const supEl = document.createElement('sup');
            supEl.innerText = '2';
            span.appendChild(supEl);

            span.appendChild(document.createTextNode(` (${square})`));
            return span;
        }

        const ol = document.createElement('ol');

        for (const tripleObject of pythagTriplesArray) {
            const li = document.createElement('li');
            const {numbers, squares} = tripleObject;
            li.appendChild(getNumberAndSquareSpan(numbers[0], squares[0]));
            li.appendChild(document.createTextNode(' + '));
            li.appendChild(getNumberAndSquareSpan(numbers[1], squares[1]));
            li.appendChild(document.createTextNode(' = '));
            li.appendChild(getNumberAndSquareSpan(numbers[2], squares[2]));
            ol.appendChild(li);
        }

        div.appendChild(ol);
        return div;
    }

    /**
     * Either randomizes, increments, or decrements the input box value
     * @param {number} inputBoxNumber Should be 1 or 2 for the first or second input box, respectively
     * @param {string} action Should be either 'increment', 'decrement', or 'randomize'
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
            let randomNumber = Math.floor(Math.random() * (maxInputNumber - minInputNumber + 1) + minInputNumber);

            // Goldbach section must have even number
            if (currentSection === 'goldbach' && randomNumber % 2 !== 0) {
                randomNumber++;
            }

            inputBox.value = randomNumber;
            // Send request to get a random number for the current section and display in appropriate input box
            // const xhr = new XMLHttpRequest();
            // xhr.onreadystatechange = function() {
            //     // Goldbach conjecture section must have even numbers
            //     if (currentSection === 'goldbach') {
            //         let randomNumber = Number(xhr.responseText);
            //         if (randomNumber % 2 !== 0) {
            //             randomNumber++;
            //         }
            //         inputBox.value = randomNumber;
            //     } else {
            //         inputBox.value = xhr.responseText;
            //     }
            // }
            // const apiUrl = `/randomNumber/section/${currentSection}`;
            
            // xhr.open('GET', apiUrl);
            // xhr.send();
        } else {
            const numberInBox = Number(inputBox.value);

            if (inputBox.value === '' || !Number.isInteger(numberInBox)) {
                // do nothing
                return;
            }
            let newNumberInBox;
            if (action === 'increment') {
                if (numberInBox >= maxInputNumber || numberInBox < minInputNumber) {
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
                if (numberInBox <= minInputNumber || numberInBox > maxInputNumber) {
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

    document.getElementById('randomBtn1').addEventListener('click', () => {
        changeInputBoxValue(1, 'randomize');
    });

    document.getElementById('randomBtn2').addEventListener('click', function() {
        changeInputBoxValue(2, 'randomize');
    });

    document.getElementById('incrementBtn1').addEventListener('click', function() {
        changeInputBoxValue(1, 'increment');
    });

    document.getElementById('incrementBtn2').addEventListener('click', function() {
        changeInputBoxValue(2, 'increment'); 
    });

    document.getElementById('decrementBtn1').addEventListener('click', function() {
        changeInputBoxValue(1, 'decrement');
    });

    document.getElementById('decrementBtn2').addEventListener('click', function() {
        changeInputBoxValue(2, 'decrement');
    });
})();