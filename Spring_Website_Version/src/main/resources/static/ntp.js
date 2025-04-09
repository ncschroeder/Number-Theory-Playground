'use strict';

/**
 * @param {string} id 
 * @returns {HTMLElement | null}
 */
const getElementById = (id) => document.getElementById(id);

/**
 * @param {string} elementType 
 * @param {string} [textContent]
 * @returns {HTMLElement}
 */
function createElement(elementType, textContent) {
    const element = document.createElement(elementType);
    if (textContent) {
        element.textContent = textContent;
    }
    return element;
}

/**
 * @param {string} [textContent]
 * @returns {HTMLHeadingElement}
 */
const createH3 = (textContent) => createElement('h3', textContent);

/**
 * @param {string} [textContent]
 * @returns {HTMLButtonElement}
 * @returns {HTMLHeadingElement}
 */
const createH4 = (textContent) => createElement('h4', textContent);

/**
 * @param {string} [textContent]
 * @returns {HTMLParagraphElement}
 */
const createP = (textContent) => createElement('p', textContent);

/**
 * @param {string} innerHtml 
 * @returns {HTMLParagraphElement}
 */
function createPWithInnerHtml(innerHtml) {
    const p = createP();
    p.innerHTML = innerHtml;
    return p;
}

/**
 * @param  {...Appendable} children 
 * @returns {HTMLLIElement}
 */
const createLi = (...children) => createElement('li', ...children);

 * @returns {HTMLDivElement}
 */
const createBtn = (textContent) => createElement('button', textContent);
const createDiv = () => createElement('div');

/**
 * @param {string} [textContent]
 * @returns {HTMLElement}
 */
const createSuperscript = (textContent) => createElement('sup', textContent);

/**
 * @returns {HTMLElement}
 */
const createSuperscriptWith2 = () => createSuperscript('2');

/**
 * @returns {HTMLSpanElement}
 */
const createSpan = () => createElement('span');

/**
 * @param {number} number
 * @returns {HTMLSpanElement}
 */
function getNumberAndSquareSpan(number) {
    const span = createSpan();
    span.append(getNumberStringWithCommas(number), createSuperscriptWith2(), ` (${getNumberStringWithCommas(number * number)})`);
    return span;
}

/**
 * @param {any[]} array
 * @param {(obj: any) => HTMLElement | string} transform
 * @param {boolean} useAnswerListClass
 * @returns {HTMLOListElement}
    */
function arrayToOl(array, transform, useAnswerListClass = true) {
    const ol = createElement('ol');
    if (useAnswerListClass) {
        ol.className = 'answerList';
    }

    for (const element of array) {
        const transformObj = transform(element);
        const li = transformObj instanceof HTMLLIElement ? transformObj : createLi(transformObj);
        ol.appendChild(li);
    }

    return ol;
}

/**
 * @param {string[]} headings 
 * @param {any[]} bodyDataSource 
 * @param {(obj: any) => (number | string)[]} bodyTransform
 * @returns {HTMLTableElement}
 */
function createTable(headings, bodyDataSource, bodyTransform, caption) {
    const table = createElement('table');
    const thead = createElement('thead');

    /**
     * @returns {HTMLTableRowElement}
     */
    const createTr = () => createElement('tr');

    let tr = createTr();
    for (const heading of headings) {
        tr.appendChild(createElement('th', heading));
    }
    thead.appendChild(tr);
    table.append(thead);

    const tableBody = createElement('tbody');

    for (const obj of bodyDataSource) {
        tr = createTr();
        for (const num of bodyTransform(obj)) {
            tr.appendChild(createElement('td', getNumberStringWithCommas(num)));
        }
        tableBody.appendChild(tr);
    }

    table.appendChild(tableBody);
    return table;
}

/**
 * @param {number} a 
 * @param {number} b 
 * @returns {boolean}
 */
const isDivisible = (a, b) => a % b === 0;

/**
 * @param {HTMLInputElement} inputField 
 * @returns {number}
 * @param {number} n 
 * @returns {boolean}
 */
function getNum(inputField) {
    const trimmedValue = inputField.value.trim().replaceAll(',', '');
    if (trimmedValue.length === 0) return NaN;
    const containsNonDigit = /\D/.test(trimmedValue);
    return containsNonDigit ? NaN : Number(trimmedValue);
}
const isEven = (n) => isDivisible(n, 2);

/**
 * @returns {HTMLInputElement}
 */
function addInputArea() {
    /**
     * @type {HTMLInputElement}
     */
    const inputField = createElement('input');
    inputField.setAttribute('type', 'number');

    const randomBtn = createBtn('Randomize');
    randomBtn.onclick = () => {
        let randomNumber = Math.floor(Math.random() * (maxInput - minInput + 1) + minInput);
        if (curSection.needsEvenInput && !isEven(randomNumber)) {
            randomNumber++;
        }
        inputField.value = randomNumber;
    }

    const plusBtn = createBtn('+');
    plusBtn.onclick = () => {
        let inputNum = getNum(inputField);
        if (!inputNum) {
            if (inputField.value.length === 0) {
                inputField.value = minInput;
            }
            return;
        }

        inputNum =
            inputNum < minInput || inputNum >= maxInput
            ? minInput
            : inputNum + (curSection.needsEvenInput && isEven(inputNum) ? 2 : 1);
        inputField.value = inputNum;
    };

    const minusBtn = createBtn('-');
    minusBtn.onclick = () => {
        let inputNum = getNum(inputField);
        if (!inputNum) {
            if (inputField.value.length === 0) {
                inputField.value = maxInput;
            }
            return;
        }

        inputNum =
            inputNum <= minInput || inputNum > maxInput
            ? maxInput
            : inputNum - (curSection.needsEvenInput && isEven(inputNum) ? 2 : 1);
        inputField.value = inputNum;
    };

    const div = createElement('div');
    div.className = 'inputArea';
    div.append(inputField, randomBtn, plusBtn, minusBtn);
    getElementById('inputAreaDiv').appendChild(div);

    return inputField;
}

const inputField1 = addInputArea(), inputField2 = addInputArea();

const commaAdder = new Intl.NumberFormat();
const getNumberStringWithCommas = commaAdder.format;

/**
 * @param {number} a
 * @param {number} b
 * @returns 
 */
const numPairToString = (a, b) => `${getNumberStringWithCommas(a)} & ${getNumberStringWithCommas(b)}`;

/**
 * @type {?Section}
 */
let curSection = null;

/**
 * @type {?number}
 */
let minInput = null;

/**
 * @type {?number}
 */
let maxInput = null;

const
    sectionHeading = getElementById('sectionHeading'),
    sectionInfo = getElementById('sectionInfo'),
    sectionDirections = getElementById('sectionDirections'),
    answerArea = getElementById('answerArea'),
    interactionContent = getElementById('interactionContent');

function clearAnswerArea() {
    answerArea.innerHTML = '';
}

function clearInputBoxesAndAnswerArea() {
    inputField1.value = '';
    inputField2.value = '';
    clearAnswerArea();
}

class Section {
    // Use private fields with public getters to allow for read-only access outside of class.

    #isSingleInputSection;
    /**
     */
    #minInput;
    #maxInput;
    #needsEvenInput;
    #apiEndpoint;
    #getElements;

    /**
     * @callback GetElementsFunction
     * @param {any} responseObj
     * @param {string} input1String
     * @param {?string} input2String
     * @param {number} input1Num
     * @returns {HTMLElement[]}
     * 
     * @typedef {Object} SectionParams
     * @property {string} btnIdStart
     * @property {string | HTMLElement[]} infoHtml
     * @property {boolean} [isSingleInputSection=true]
     * @property {string} actionSentenceEnding
     * @property {number} minInput
     * @property {number} maxInput
     * @property {boolean} [needsEvenInput=false]
     * @property {string} apiEndpoint
     * @property {GetElementsFunction} getElements
     * 
     * @param {SectionParams} obj 
     */
    constructor(obj) {
        const { btnIdStart, infoHtml, isSingleInputSection, actionSentenceEnding, minInput, maxInput, needsEvenInput, apiEndpoint, getElements } = obj;

        this.#minInput = minInput;
        this.#maxInput = maxInput;
        this.#isSingleInputSection = isSingleInputSection !== undefined ? isSingleInputSection : true;
        this.#needsEvenInput = needsEvenInput !== undefined ? needsEvenInput : false;
        this.#apiEndpoint = apiEndpoint;
        this.#getElements = getElements;
        
        const sectionBtn = getElementById(btnIdStart + 'Btn');
        const heading = sectionBtn.textContent;

        /**
         * @type {HTMLElement[]}
         */
        let infoHtmlElements;

        if (Array.isArray(infoHtml)) {
            infoHtmlElements = infoHtml;
        } else {
            infoHtmlElements =
                infoHtml
                .split('\n\n')
                .map(s => {
                    const p = createP();
                    p.innerHTML = s;
                    return p;
                });
        }

        const directions =
            `Enter or generate ${isSingleInputSection ? 'an integer' : '2 integers'} and click the \
            Calculate button to get the ${actionSentenceEnding}. Have \
            ${isSingleInputSection ? 'this integer' : 'these integers'} be \
            ${needsEvenInput ? 'even && ' : ''}>= ${getNumberStringWithCommas(minInput)} && \
            <= ${getNumberStringWithCommas(maxInput)}. Commas are optional.`;

        const show = () => {
            clearInputBoxesAndAnswerArea();
            curSection = this;
            sectionHeading.textContent = heading;
            sectionInfo.replaceChildren(...infoHtmlElements);
            sectionDirections.textContent = directions;
        }

        sectionBtn.onclick = show;
    }

    get isSingleInputSection() {
        return this.#isSingleInputSection;
    }

    get minInput() {
        return this.#minInput;
    }

    get maxInput() {
        return this.#maxInput;
    }

    get needsEvenInput() {
        return this.#needsEvenInput;
    }

    get apiEndpoint() {
        return this.#apiEndpoint;
    }

    get getElements() {
        return this.#getElements;
    }

    /**
     * @param {number} n 
     * @returns {boolean}
     */
    isValidInput(n) {
        return n >= this.minInput && n <= this.maxInput && (!this.needsEvenInput || isEven(n));
    }
}

getElementById('calculateBtn').onclick = () => {
    const inputNum1 = getNum(inputField1);
    if (!inputNum1 || !curSection.isValidInput(inputNum1)) return;
    const urlParams = new URLSearchParams();
    let inputNum2, inputString2;

    if (curSection.isSingleInputSection) {
        urlParams.append('input', inputNum1);
        inputNum2 = null;
        inputString2 = null;
    } else {
        inputNum2 = getNum(inputField2);
        if (!inputNum2 || !curSection.isValidInput(inputNum2)) return;
        inputString2 = getNumberStringWithCommas(inputNum2);
        urlParams.append('input1', inputNum1);
        urlParams.append('input2', inputNum2);
    }

    const inputString1 = getNumberStringWithCommas(inputNum1);
    const errorMessage = 'Error with request.';

    fetch(`calculate/${curSection.apiEndpoint}?${urlParams}`)
    .then(async response =>
        response.ok
        ? curSection.getElements(await response.json(), inputString1, inputString2, inputNum1)
        : [errorMessage]
    )
    .then(elementsOrErrorMessage => answerArea.replaceChildren(...elementsOrErrorMessage))
    .catch(reason => {
        answerArea.replaceChildren(errorMessage);
        console.error(errorMessage, reason);
    });
}


const primesInfoHtml =
    `A <i>prime number</i>, or a <i>prime</i>, is an integer ≥ 2 whose only integer factors are 1 and itself.
    A <i>composite number</i> is an integer ≥ 2 that has an integer factor other than 1 and itself. The first
    10 primes are 2, 3, 5, 7, 11, 13, 17, 19, 23, and 29. There are an infinite amount of them. The largest
    known prime is 2<sup>136,279,841</sup> − 1. It has 41,024,320 digits! Primes are used in 7 of the 10
    sections in this application.
    
    With the exception of 2 and 3, all primes are either 1 above or 1 below a multiple of 6. To show why this
    is the case, let's have a variable <var>i</var> and let it represent any integer ≥ 6 that's a multiple of 6.
    We know that <var>i</var> is divisible by 2 and 3 so <var>i</var> + 2 and <var>i</var> + 4 are divisible by
    2 and <var>i</var> + 3 is divisible by 3 but we don't have any guarantees about what <var>i</var> + 1 and
    <var>i</var> + 5 are divisible by. Therefore, that's where primes can be.
    
    An integer can be determined to be prime if it's not divisible by any primes ≤ the square root of that
    integer. This is called <i>trial division</i>. Let's determine if 29 and 33 are prime. 5<sup>2</sup> = 25
    and 6<sup>2</sup> = 36 so 5 < <math><msqrt><mn>29</mn></msqrt></math> <
    <math><msqrt><mn>33</mn></msqrt></math> < 6. We check if the integers are divisible by 2, 3, or 5; which
    are the primes ≤ 5. 29 isn't divisible by any of those and 33 is divisible by 3 so 29 is prime and 33 isn't.`;

/**
 * @param {number[]} primes
 * @param {string} inputString
 * @returns {HTMLElement[]}
 */
function getPrimesElements(primes, inputString) {
    const heading = createH3(`The first ${primes.length} primes >= ${inputString} are:`);
    const primesOl = arrayToOl(primes, getNumberStringWithCommas);
    return [heading, primesOl];
}

new Section({
    btnIdStart: 'primeNums',
    infoHtml: primesInfoHtml,
    actionSentenceEnding: 'first 30 prime numbers >= that integer',
    minInput: 0,
    maxInput: 10,
    apiEndpoint: 'primes',
    getElements: getPrimesElements
});


const twinPrimePairsInfoHtml =
    `A <i>twin prime pair</i> is a pair of prime numbers that differ by 2. The first 5 twin prime pairs are
    3 & 5, 5 & 7, 11 & 13, 17 & 19, and 27 & 29. The largest known twin prime pair is
    2,996,863,034,895 × 2<sup>1,290,000</sup> ± 1. They have 388,342 digits! It is conjectured that there are
    an infinite amount of twin prime pairs. A <i>conjecture</i> is a statement that is believed to be true but
    has not been proven to be.
    
    All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so this means that all twin
    prime pairs besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and another number that is
    1 above that same multiple of 6. 5 is the only number to be in 2 twin prime pairs, the first 2 mentioned
    above.`;

/**
 * @param {number[]} pairStarts
 * @param {string} inputString
 * @returns {HTMLElement[]}
 */
function getTwinPrimePairsElements(pairStarts, inputString) {
    const heading = createH3(`The first ${pairStarts.length} twin prime pairs >= ${inputString} are:`);
    const twinPrimesOl = arrayToOl(pairStarts, n => numPairToString(n, n + 2));
    return [heading, twinPrimesOl];
}

new Section({
    btnIdStart: 'twinPrimePairs',
    infoHtml: twinPrimePairsInfoHtml,
    actionSentenceEnding: 'first 20 twin prime pairs >= that integer',
    minInput: 0,
    maxInput: 10,
    apiEndpoint: 'twinPrimePairs',
    getElements: getTwinPrimePairsElements
});


const pfInfoHtml =
    `The Fundamental Theorem of Arithmetic says that every integer > 1 can be expressed as the product of prime
    numbers. The <i>prime factorization</i> (PF) of an integer is an expression of the prime numbers whose
    product is that integer. For example, the PF of 5 is just 5, the PF of 25 is 5<sup>2</sup>, and the PF of
    12,250 is 2 × 5<sup>3</sup> × 7<sup>2</sup>. There are some interesting applications for this. See the info
    for the "Divisibility" or "GCD and LCM" sections for some applications.`;

/**
 * @typedef FactorAndPower
 * @type {object}
 * @property {number} factor
 * @property {number} power
 * @typedef {FactorAndPower[]} PfArray
 * @typedef {{pfArr: ?PfArray, correspondingNum: number}} PfArrayAndNumber
 * 
 * @param {FactorAndPower[]} pfArray
 * @returns {HTMLSpanElement}
 */
function getPfSpan(pfArray) {
    const span = createSpan();

    for (let i = 0; i < pfArray.length; i++) {
        const { factor, power } = pfArray[i];
        span.append(getNumberStringWithCommas(factor));

        if (power !== 1) {
            span.appendChild(createSuperscript(power));
        }

        // Add ' x ' between each factor and its power.
        if (i !== pfArray.length - 1) {
            span.append(' x ');
        }
    }

    return span;
}

/**
 * @param {FactorAndPower[]} pfArray
 * @param {string} inputString
 * Array that represents the prime factorization of inputNumber.
 * @returns {HTMLElement[]}
 * An array with a heading and a span that displays the prime factorization from pfArray.
 */
function getPfElements(pfArray, inputString) {
    const heading = createH3(`The prime factorization of ${inputString} is:`);
    return [heading, getPfSpan(pfArray)];
}

new Section({
    btnIdStart: 'pf',
    infoHtml: pfInfoHtml,
    actionSentenceEnding: 'prime factorization of that integer',
    minInput: 2,
    maxInput: 10,
    apiEndpoint: 'primeFactorization',
    getElements: getPfElements
});


const divisbilityInfoHtml =
    `Say we have 2 integers that we'll represent with the variables <var>a</var> and <var>b</var>. If we divide
    <var>a</var> by <var>b</var> and get no remainder, then <var>a</var> is said to be <i>divisible</i> by
    <var>b</var> and <var>b</var> is said to be a <i>factor</i> or <i>divisor</i> of <var>a</var>. If you want
    to find some factors of an integer, you could manually do some division but there are other ways to find
    them.
    
    The factors of an integer can be found by looking at its prime factorization (PF). Let's have a variable
    <var>i</var> and let it represent an integer > 1. First, you can find how many factors <var>i</var> has by
    looking at <var>i</var>'s PF, taking all the powers of the factors, adding 1 to each, and then multiplying
    all these together. For example, the PF of 36 is 2<sup>2</sup> × 3<sup>2</sup>. The powers are 2 and 2, so
    there are 3 × 3 = 9 factors. However, that count includes 1 and the integer that the PF is for (36 in this
    case). If you want to exclude those, then subtract 2. That would give us 7 factors. You can find the
    factors of <var>i</var> by finding all the PFs within <var>i</var>'s PF, or the "sub-factorizations", as I
    like to call them. For 2<sup>2</sup> × 3<sup>2</sup>, the sub-factorizations are
    2, 3, 2<sup>2</sup> (4), 2 × 3 (6), 3<sup>2</sup> (9), 2<sup>2</sup> × 3 (12), and 2 × 3<sup>2</sup> (18).
    
    Some rules can be used to find some of the factors of an integer. I'll go over 1 rule for each integer in
    the range of 3 to 12, excluding 5 and 10, though there are rules for more integers and many integers have
    multiple rules. I'll go over an example of using these rules to find the factors of an integer in the next
    paragraph. Let's have a variable <var>i</var> and let it represent an integer. If the last 2 digits of
    <var>i</var> is divisible by 4, then <var>i</var> is divisible by 4. If the last 3 digits of <var>i</var>
    is divisible by 8, then <var>i</var> is divisible by 8. If the sum of the digits of <var>i</var> is
    divisible by 3, then <var>i</var> is divisible by 3. If the sum of the digits of <var>i</var> is divisible
    by 9, then <var>i</var> is divisible by 9. If <var>i</var> is even and divisible by 3, then it's also
    divisible by 6. If <var>i</var> is divisible by both 3 and 4, then it's also divisible by 12. For 7, we
    split the integer into blocks of 3 from right to left. Coincidentally, these are the blocks separated by
    commas if we write the integer with commas. We do an alternating sum of the blocks from right to left.
    We start with 0, add the last block, subtract the 2<sup>nd</sup> to last block, add the 3<sup>rd</sup> to
    last block, and so on for all the blocks. If this alternating sum is divisible by 7, then <var>i</var> is
    divisible by 7. For 11, we do an alternating sum of digits from left to right. We start with 0, add the
    1<sup>st</sup> digit, subtract the 2<sup>nd</sup> digit, add the 3<sup>rd</sup> digit, and so on for all
    digits. If this alternating sum is divisible by 11, then <var>i</var> is divisible by 11.
    
    Here's an example. Let <var>i</var> be 4,695,768. The PF of <var>i</var> is
    2<sup>3</sup> × 3<sup>2</sup> × 7<sup>2</sup> × 11<sup>3</sup>. We can tell from that PF that <var>i</var>
    is divisible by all the integers mentioned above. Let's check using the rules. The last 2 digits are 68,
    which is divisible by 4. The last 3 digits are 768, which is divisible by 8. The sum of the digits is
    4 + 6 + 9 + 5 + 7 + 6 + 8 = 45, which is divisible by 3 and 9. Since <var>i</var> is even and divisible by
    3, it's also divisible by 6. Since <var>i</var> is divisible by both 3 and 4, it's also divisible by 12.
    The alternating sum of blocks of 3 from right to left is 768 − 695 + 4 = 77 , which is divisible by 7.
    The alternating sum of digits from left to right is  4 − 6 + 9 − 5 + 7 − 6 + 8 = 11, which, of course, is
    divisible by 11.`;

/**
 * @typedef {{ sum: number, expression: string }} AlternatingSumAndExpression
 * 
 * @typedef {Object} DivisibilityRulesData
 * @property {number} last2Digits
 * @property {number} last3Digits
 * @property {number} sumOfDigits
 * @property {?AlternatingSumAndExpression} blocksOf3AltSumAndExpression
 * @property {AlternatingSumAndExpression} digitsAltSumAndExpression
 * 
 * @typedef {Object} DivisibilityPfAnswer
 * @property {PfArray} inputPfArr
 * @property {string} numFactorsExpression
 * @property {number} numFactors
 * @property {PfArrayAndNumber[]} factorPfArrsAndNums
 */

/** 
 * @param {{ rulesData: ?DivisibilityRulesData, pfAnswer: ?DivisibilityPfAnswer }}
 * @param {string} inputString
 * @param {number} inputNum 
 * @returns {HTMLElement[]}
 * An array that contains elements with divisibility info about the argument number based on the info in infoObject.
 */
function getDivisibilityInfoElements({ rulesData, pfAnswer }, inputString, inputString2Ignored, inputNum) {
    const elements = [createH3(`Divisibility Info for ${inputString}`)];
    if (rulesData) {
        elements.push(getDivisibiityRulesInfoDiv(rulesData, inputString, inputNum));
    }
    elements.push(getDivisibiltyPfInfoDiv(pfAnswer, inputString));
    return elements;
}

/**
 * @param {DivisibilityRulesData} rulesData 
 * @param {string} inputString 
 * @param {number} inputNum 
 * @returns {HTMLDivElement}
 */
function getDivisibiityRulesInfoDiv(rulesData, inputString, inputNum) {
    const { last2Digits, last3Digits, sumOfDigits, blocksOf3AltSumAndExpression, digitsAltSumAndExpression } = rulesData;
    const heading = createH4('Rules Info');

    /**
     * @param {number} possibleFactor 
     * @param {number} numFromCalculation 
     * @param {boolean} isDivisible 
     */
    function getDivisibilitySentence(possibleFactor, numFromCalculation, isDivisible) {
        const isOrIsnt = isDivisible ? 'is' : 'isn\'t';
        return `${getNumberStringWithCommas(numFromCalculation)} ${isOrIsnt} divisible by ${possibleFactor} \
            so ${inputString} ${isOrIsnt} divisible by ${possibleFactor}.`;
    }

    /**
     * @type {string[]}
     */
    const sentences = [];
    const isEvenVar = isEven(inputNum);
    const isDivisibleBy4 = isDivisible(last2Digits, 4);

    if (!isEvenVar) {
        sentences.push(`${inputString} isn't even so it isn't divisible by any even numbers.`);
    } else if (inputNum >= 100) {
        sentences.push(
            `The last 2 digits form the integer ${last2Digits}.`,
            getDivisibilitySentence(4, last2Digits, isDivisibleBy4)
        );

        if (isDivisibleBy4) {
            if (inputNum >= 1_000) {
                const isDivisibleBy8 = isDivisible(last3Digits, 8);
                sentences.push(
                    `The last 3 digits form the integer ${last3Digits}`,
                    getDivisibilitySentence(8, last3Digits, isDivisibleBy8)
                );
            }
        } else {
            sentences.push(
                `Since ${inputString} isn't divisible by 4, it's also not divisible by 8, 12, and any other \
                multiples of 4.`
            );
        }
    }

    const isDivisibleBy3 = isDivisible(sumOfDigits, 3);
    sentences.push(
        `The sum of the digits is ${sumOfDigits}.`,
        getDivisibilitySentence(3, sumOfDigits, isDivisibleBy3)
    );

    if (isDivisibleBy3) {
        if (isEvenVar) {
            sentences.push(`${inputString} is even and divisible by 3 so it's also divisible by 6.`);

            if (isDivisibleBy4) {
                sentences.push(`${inputString} is divisible by both 3 and 4 so it's also divisible by 12.`);
            } else if (inputNum < 100) {
                sentences.push(`${inputString} isn't divisible by 4 so it isn't divisible by 12.`)
            }
        }

        const isDivisibleBy9 = isDivisible(sumOfDigits, 9);
        sentences.push(getDivisibilitySentence(9, sumOfDigits, isDivisibleBy9));
    } else {
        sentences.push(
            `Since ${inputString} isn't divisible by 3, it's also not divisible by 6, 9, 12, and any \
            other multiples of 3.`
        );
    }

    if (blocksOf3AltSumAndExpression) {
        const { sum: blocksOf3AltSum, expression: blocksOf3Expression } = blocksOf3AltSumAndExpression;
        const isDivisibleBy7 = isDivisible(blocksOf3AltSum, 7);
        sentences.push(
            `The alternating sum of blocks of 3 from right to left is ${blocksOf3Expression} = ${blocksOf3AltSum}.`,
            getDivisibilitySentence(7, blocksOf3AltSum, isDivisibleBy7)
        );
    }

    const { sum: digitsAltSum, expression: digitsExpression } = digitsAltSumAndExpression;
    const isDivisibleBy11 = isDivisible(digitsAltSum, 11);
    sentences.push(
        `The alternating sum of digits from left to right is ${digitsExpression} = ${digitsAltSum}.`,
        getDivisibilitySentence(11, digitsAltSum, isDivisibleBy11)
    );

    return createDiv(heading, createP(sentences.join(' ')));
}

/**
 * @param {?DivisibilityPfAnswer} pfAnswer 
 * @param {string} inputString 
 * @returns {HTMLDivElement}
 */
function getDivisibiltyPfInfoDiv(pfAnswer, inputString) {
    const heading = createH4('Prime Factorization Info');
    const pfInfoParagraph = createP(`The prime factorization of ${inputString} is `);
    const pfDiv = createDiv(heading, pfInfoParagraph);

    if (!pfAnswer) {
        pfInfoParagraph.append(
            `${inputString}. ${inputString} is prime and doesn't have any factors other than itself and 1.`
        );
        return pfDiv;
    }

    const { inputPfArr, numFactorsExpression, numFactors, factorPfArrsAndNums } = pfAnswer;
    pfInfoParagraph.append(getPfSpan(inputPfArr), '. ');

    const numFactorsInfo =
        `By looking at the powers, we can see that there are ${numFactorsExpression} = \
        ${getNumberStringWithCommas(numFactors)} factors. If 1 and ${inputString} are excluded then there are \
        ${getNumberStringWithCommas(numFactors - 2)} factors.`;

    const subfactorizationsSentence =
        'By looking at all the "sub-factorizations", we can see that the factors are:';

    pfInfoParagraph.append(numFactorsInfo, ' ', subfactorizationsSentence);

    /**
     * @param {PfArrayAndNumber}
     * @returns {HTMLLIElement | string}
     */
    function getLiOrLiText({ pfArr, correspondingNum }) {
        const numString = getNumberStringWithCommas(correspondingNum);
        return pfArr ? createLi(getPfSpan(pfArr), ` (${numString})`) : numString;
    }

    pfDiv.appendChild(arrayToOl(factorPfArrsAndNums, getLiOrLiText));
    return pfDiv;
}

new Section({
    btnIdStart: 'divisibility',
    infoHtml: divisbilityInfoHtml,
    actionSentenceEnding: 'divisbility info for that integer',
    minInput: 2,
    maxInput: 10,
    apiEndpoint: 'divisibilityAnswer',
    getElements: getDivisibilityInfoElements
});


const gcdAndLcmInfoHtml =
    `GCD stands for greatest common divisor and LCM stands for least common multiple. GCD is also known as
    greatest common factor, or GCF. To find the GCD and LCM of 2 integers, you could manually do some division
    and multiplication but there are other ways to find them.
    
    The Euclidean algorithm can be used to find the GCD of 2 integers. This algorithm was named after the
    ancient Greek mathematician Euclid. For this algorithm, first take 2 integers. If the max integer is
    divisible by the min integer, then the min integer is the GCD. Otherwise, the GCD of the 2 integers is the
    same as the GCD of the min integer and the remainder when the max integer is divided by the min integer.
    Repeat.

    The GCD and LCM of 2 integers can be found by looking at their prime factorizations (PFs). If those
    integers don't have any common prime factors, then the GCD is 1. If they do have common prime factors, then
    the GCD PF consists of all the common prime factors and the power of each factor is the min of the powers
    of that factor in the 2 PFs. The LCM PF consists of all factors that are in either of the PFs of the 2
    integers. If a factor is in both PFs then the power of that factor in the LCM PF is the max of the powers
    of that factor in the 2 PFs. If a factor is unique to one of the PFs then that factor and its power are in
    the LCM PF.

    Let's find the GCD and LCM of 6 and 35 using their PFs. The PF of 6 is 2 × 3 and the PF of 35 is 5 × 7.
    There are no common prime factors so the GCD is 1. The LCM PF is 2 × 3 × 5 × 7, which is 210.
    
    Let's find the GCD and LCM of 54 and 99. The PF of 54 is 2 × 3<sup>3</sup> and the PF of 99 is
    3<sup>2</sup> × 11. 3 is the only common prime factor and the min power of it is 2 so the GCD PF is
    3<sup>2</sup>, which is 9. The max power of 3 is 3 so the LCM PF consists of 3<sup>3</sup>.
    The LCM PF is 2 × 3<sup>3</sup> × 11, which is 594.`;

/**
 * @typedef EuclideanIteration
 * @type {object}
 * @property {number} min
 * @property {number} max
 * @property {number} remainder
 * 
 * @typedef GcdAndLcmPfAnswer
 * @type {object}
 * @property {FactorAndPower[]} pf1
 * @property {FactorAndPower[]} pf2
 * @property {?FactorsAndPowersWithNumber} gcd
 * @property {FactorsAndPowersWithNumber} lcm
 * 
 * @param {{euclideanIterations: EuclideanIteration[], gcdAndLcmPfAnswer: GcdAndLcmPfAnswer}} infoObject 
 * @param {string} inputString1
 * @param {string} inputString2
 * @returns {HTMLElement[]}
 */
function getGcdAndLcmInfoElements(infoObject, inputString1, inputString2) {
    const mainHeading = createH3(`GCD and LCM Info for ${inputString1} and ${inputString2}`);
    const euclideanDiv = getEuclideanDiv(infoObject.euclideanIterations, inputString1, inputString2);
    const pfDiv = getGcdAndLcmPfInfoDiv(infoObject.gcdAndLcmPfAnswer, inputString1, inputString2);
    return [mainHeading, euclideanDiv, pfDiv];
}

/**
 * @param {EuclideanIteration[]} iterations
 * @param {string} inputString1 
 * @param {string} inputString2 
 * @returns {HTMLDivElement}
 */
function getEuclideanDiv(iterations, inputString1, inputString2) {
    const div = createDiv();
    const table =
        createTable(['Max', 'Min', 'Remainder'], iterations, (iteration) => [iteration.min, iteration.max, iteration.remainder], 'Euclidean Algorithm Info');
    div.appendChild(table);

    const gcd = iterations[iterations.length - 1].min;
    const gcdMessageP =
        createP(`The GCD of ${inputString1} and ${inputString2} is ${getNumberStringWithCommas(gcd)}.`);

    div.appendChild(gcdMessageP);
    return div;
}

/**
 * @param {GcdAndLcmPfAnswer} data 
 * @param {string} inputString1 
 * @param {string} inputString2 
 * @returns {HTMLDivElement}
 */
function getGcdAndLcmPfInfoDiv(data, inputString1, inputString2) {
    const { pf1, pf2, gcd, lcm } = data;
    const div = createDiv();

    /**
     * @param {string} inputString 
     * @param {FactorAndPower[]} pf 
     */
    function createInnerDiv1(inputString, pf) {
        const div = createDiv();
        div.append(`The PF of ${inputString} is `, getPfSpan(pf));
        return div;
    }

    /**
     * @param {string} gcdOrLcmText 
     * @param {FactorsAndPowersWithNumber} factorsAndPowersWithNumber 
     */
    function createInnerDiv2(gcdOrLcmText, factorsAndPowersWithNumber) {
        const div = createDiv();
        div.append(`The PF of the ${gcdOrLcmText} is `);
        const { factorsAndPowers, number } = factorsAndPowersWithNumber;
        if (factorsAndPowers) {
            div.append(getPfSpan(factorsAndPowers), `, which is `);
        }
        div.append(getNumberStringWithCommas(number));
        return div;
    }

    const gcdInfo = gcd ? createInnerDiv2('GCD', gcd) : 'There are no common prime factors so the GCD is 1.';

    div.append(createInnerDiv1(inputString1, pf1), createInnerDiv1(inputString2, pf2), gcdInfo, createInnerDiv2('LCM', lcm));
    return div;
}

new Section({
    btnIdStart: 'gcdAndLcm',
    infoHtml: gcdAndLcmInfoHtml,
    isSingleInputSection: false,
    actionSentenceEnding: 'GCD and LCM info for those integers',
    minInput: 2,
    maxInput: 10,
    apiEndpoint: 'gcdAndLcmAnswer',
    getElements: getGcdAndLcmInfoElements
});


const goldbachConjectureInfoHtml =
    `The Goldbach Conjecture says that every even number ≥ 4 can be expressed as the sum of 2 prime numbers.
    This was named after 1700s Prussian mathematician Christian Goldbach. A <i>conjecture</i> is a statement
    that is believed to be true but has not been proven to be true. The Goldbach Conjecture has been verified
    to be true for all even numbers ≥ 4 && ≤ 4 × 10<sup>18</sup>.`;

/**
 * @param {number[]} primePairStarts
 * @param {string} inputString
 * @param {number} inputNum 
 * @returns {HTMLElement[]}
 */
    const heading = createH3();
    heading.textContent =
        `There${primePairs.length === 1 ? '\'s 1 pair' : ` are ${getNumberStringWithCommas(primePairs.length)} pairs`} \
        of prime numbers that sum to ${inputString}. They are:`;
    return [heading, pairsOl];
function getGoldbachConjectureElements(primePairStarts, inputString, inputString2Ignored, inputNum) {
    const pairsOl = arrayToOl(primePairStarts, start => numPairToString(start, inputNum - start));
}

new Section({
    btnIdStart: 'goldbachConjecture',
    infoHtml: goldbachConjectureInfoHtml,
    actionSentenceEnding: 'pairs of prime numbers that sum to that integer',
    minInput: 4,
    maxInput: 10,
    needsEvenInput: true,
    apiEndpoint: 'goldbachPrimePairStarts',
    getElements: getGoldbachConjectureElements
});


const pythagoreanTriplesInfoHtml =
    `The Pythagorean Theorem says that for a right triangle, the sum of the squares of the lengths of the 2
    short sides equals the square of the long side (hypotenuse) length, or
    <var>a</var><sup>2</sup> + <var>b</var><sup>2</sup> = <var>c</var><sup>2</sup>. This theorem was named
    after the ancient Greek mathematician Pythagoras. A <i>Pythagorean triple</i> is a triple of integers that
    <var>a</var>, <var>b</var>, and <var>c</var> can be. For example; 3, 4, and 5 is a Pythagorean triple since
    3<sup>2</sup> (9) + 4<sup>2</sup> (16) = 5<sup>2</sup> (25). 11, 60, and 61 is another one since
    11<sup>2</sup> (121) + 60<sup>2</sup> (3,600) = 61<sup>2</sup> (3,721).
    
    Once we know a Pythagorean triple, we can form another one by multiplying <var>a</var>, <var>b</var>, and
    <var>c</var> by the same integer > 1, Because of this, there are an infinite amount of Pythagorean triples.
    A Pythagorean triple is considered to be <i>primitive</i> if the GCD of <var>a</var>, <var>b</var>, and
    <var>c</var> is 1. Therefore, a primitive triple can't be formed by taking another triple and multiplying
    <var>a</var>, <var>b</var>, and <var>c</var> by something. The triples mentioned above; 3, 4, and 5, and
    11, 60, and 61; are primitive. 6 (3 × 2), 8 (4 × 2), and 10 (5 × 2) is another triple.
    6<sup>2</sup> (36) + 8<sup>2</sup> (64) = 10<sup>2</sup> (100). 55 (11 × 5), 300 (60 × 5), and 305 (61 × 5)
    is another one. 55<sup>2</sup> (3,025) + 300<sup>2</sup> (90,000) = 305<sup>2</sup> (93,025).`;

/**
 * @typedef {{a: number, b: number, c: number, isPrimitive: boolean}} PythagoreanTriple
 * 
 * @param {PythagoreanTriple[]} triples
 * @param {string} inputString
 * @returns {HTMLElement[]}
 */
function getPythagoreanTriplesElements(triples, inputString) {
    const heading =
        createH3(`The first ${triples.length} Pythagorean triples >= ${inputString} are:`);

    /**
     * @param {PythagoreanTriple} triple 
     * @returns {HTMLSpanElement}
     */
    function tripleToSpan(triple) {
        const { a, b, c, isPrimitive } = triple;
        const span = createSpan();
        const maybePrimitiveString = isPrimitive ? ' (primitive)' : '';
        span.append(getNumberAndSquareSpan(a), ' + ', getNumberAndSquareSpan(b), ' = ', getNumberAndSquareSpan(c), maybePrimitiveString);
        return span;
    }

    const ol = arrayToOl(triples, tripleToSpan, false);

    return [heading, ol];
}

new Section({
    btnIdStart: 'pythagoreanTriples',
    infoHtml: pythagoreanTriplesInfoHtml,
    actionSentenceEnding: 'first 10 Pythagorean triples >= that integer',
    minInput: 0,
    maxInput: 10,
    apiEndpoint: 'pythagoreanTriples',
    getElements: getPythagoreanTriplesElements
});


const twoSquareTheoremInfoHtml =
    `The Two Square Theorem says that every prime number that is 1 above a multiple of 4 can be expressed as
    the sum of 2 square numbers. A <i>square number</i> is a number that can be formed by taking a number and
    multiplying it by itself, or squaring it. The first few square numbers are
    1 (1<sup>2</sup>), 4 (2<sup>2</sup>), and 9 (3<sup>2</sup>). 29 is prime and is 1 above 28 (4 × 7)
    and can be expressed as 2<sup>2</sup> (4) + 5<sup>2</sup> (25).`;

const twoSquareTheoremActionSentenceEnding =
    'first prime number that\'s >= that integer and is 1 above a multiple of 4, \
    as well as the integers whose squares sum to that prime number';

/**
 * @param {{primeNumber: number, a: number, b: number}} infoObject
 * @param {number} inputString
 * @returns {HTMLElement[]}
 */
function getTwoSquareInfoElements(infoObject, inputString) {
    const { primeNumber, a, b } = infoObject;
    const sentenceP = createP();
    const sentenceStart =
        `The first integer >= ${inputString} that's prime and is 1 above a \
        multiple of 4 is ${getNumberStringWithCommas(primeNumber)}, which is `;
    sentenceP.append(sentenceStart, getNumberAndSquareSpan(a), ' + ', getNumberAndSquareSpan(b), '.');
    return [sentenceP];
}

new Section({
    btnIdStart: 'twoSquareTheorem',
    infoHtml: twoSquareTheoremInfoHtml,
    actionSentenceEnding: twoSquareTheoremActionSentenceEnding,
    minInput: 0,
    maxInput: 10,
    apiEndpoint: 'twoSquareTheoremAnswer',
    getElements: getTwoSquareInfoElements
});


const phiLetter = '𝚽';
const phiNum = (1 + Math.sqrt(5)) / 2;

const fiboLikeSequencesInfoHtml = 
    `I consider a number sequence to be "Fibonacci-like" if it starts with 2 numbers and has every following
    number be the sum of the 2 previous numbers. The Fibonacci sequence does this and has 1 and 1 as its first
    2 numbers. Fibonacci was a mathematician from the 1100s to 1200s from modern-day Italy. Another
    Fibonacci-like sequence is the Lucas sequence, which has 2 and 1 as its first 2 numbers. This sequence was
    named after 1800s French mathematician Francois Edouard Anatole Lucas.
    
    The <i>Golden Ratio</i> is an irrational number symbolized by the Greek letter ${phiLetter} (Phi).
    ${phiLetter} = (1 + <math><msqrt><mn>5</mn></msqrt></math>) / 2 ≈ ${phiNum}. As we advance further and
    further into a Fibonacci-like sequence, the ratio between a number and the number before it gets closer
    and closer to ${phiLetter}. For example, the first 8 numbers of the Fibonacci sequence are
    1, 1, 2, 3, 5, 8, 13, and 21. 2 / 1 = 2, 8 / 5 = 1.6, and 21 / 13 ≈ ${21 / 13 /* ~1.615 */}.`;

const fibonacciLikeSequencesActionSentenceEnding =
    'first 20 integers in the Fibonacci-like sequence that begins with those integers, \
    as well as the ratios between some integers in the sequence';

/**
 * @typedef {{ dividend: string, divisor: string, ratio: number }} RatioData
 * 
 * @param {{ sequence: string[], ratioDataArray: RatioData[] }} infoObject
 * @param {string} inputString1
 * @param {string} inputString2
 * @returns {HTMLElement[]}
 */
function getFibonacciLikeSequencesInfoElements(infoObject, inputString1, inputString2) {
    const { sequence, ratioDataArray } = infoObject;

    const heading = createH3(
        `The first ${sequence.length} integers in the Fibonacci-like sequence that starts with \
        ${inputString1} and ${inputString2} are:`
    );

    const sequenceOl = arrayToOl(sequence, getNumberStringWithCommas);

    /**
     * @param {RatioData} data 
     * @returns {string}
     */
    function ratioDataToString(data) {
        const { dividend, divisor, ratio } = data;
        return `${getNumberStringWithCommas(dividend)} / ${getNumberStringWithCommas(divisor)} is approximately ${ratio}.`;
    }

    const ratioDataOl = arrayToOl(ratioDataArray, ratioDataToString, false);
    const phiLi = createElement('li', `${capitalPhi} is approximately ${phiNum}.`);
    ratioDataOl.prepend(phiLi);
    return [heading, sequenceOl, ratioDataOl];
}

new Section({
    btnIdStart: 'fibonacciLikeSequences',
    infoHtml: fibonacciLikeSequencesInfoHtml,
    isSingleInputSection: false,
    actionSentenceEnding: fibonacciLikeSequencesActionSentenceEnding,
    minInput: 1,
    maxInput: 10,
    apiEndpoint: 'fibonacciLikeSequencesAnswer',
    getElements: getFibonacciLikeSequencesInfoElements
});


const ancientMultInfoStart =
    'The ancient Egyptians had an interesting algorithm for multiplication of 2 integers. My way of explaining \
    the algorithm goes like this:';

const ancientMultStepsArr = [
    'Let variable <var>a</var> represent one of the integers and variable <var>b</var> represent the other one.',

    `Find all powers of 2 that are ≤ <var>a</var>. This could be done without modern multiplication by starting
    with 1, the 1<sup>st</sup> power of 2 or 2<sup>0</sup>, and finding the next power by adding the previous
    power to itself. This process will look like:
    1 + 1 = 2 (2<sup>1</sup>), 2 + 2 = 4 (2<sup>2</sup>), 4 + 4 = 8 (2<sup>3</sup>), and so on until we find a
    power that's > <var>a</var>, which we won't use.`,
    
    `Find the products of <var>b</var> and these powers of 2. Like with the powers of 2, this could be done by
    starting with <var>b</var> and finding the next product by adding the previous product to itself. If we let
    <var>b</var> be 5, this process will look like:
    5 + 5 = 10 (5 × 2), 10 + 10 = 20 (5 × 4), 20 + 20 = 40 (5 × 8), and so on.`,
    
    `Find the powers of 2 that sum to <var>a</var>.`,
    
    `Add up the products of <var>b</var> and these powers.`
];

/**
 * @param {string} innerHtml 
 * @returns {HTMLLIElement}
 */
function createLiWithInnerHtml(innerHtml) {
    const li = createLi();
    li.innerHTML = innerHtml;
    return li;
}

const ancientMultStepsOl = arrayToOl(ancientMultStepsArr, createLiWithInnerHtml, false);
ancientMultStepsOl.style.listStyleType = 'decimal';

const ancientMultResultP = createP('This gives us the product of the 2 integers.');

const ancientMultExampleParagraph =
    `Let's find the product of 5 and 12. Let's first use 5 for the integer represented by <var>a</var> in the
    algorithm above and 12 for <var>b</var>. The powers of 2 ≤ 5 are 1, 2, and 4. The products of 12 and these
    powers are 12, 24, and 48. The powers of 2 that sum to 5 are 1 and 4. The products of 12 and these powers
    are 12 and 48. 12 + 48 = (12 × 1) + (12 × 4) = 12 × (1 + 4) = 60. Now let's use 12 for <var>a</var> and 5
    for <var>b</var>. The powers of 2 <= 12 are 1, 2, 4, and 8. The products of 5 and these powers are
    5, 10, 20, and 40. The powers of 2 that sum to 12 are 4 and 8. The products of 5 and these powers are
    20 and 40. 20 + 40 = (5 × 4) + (5 × 8) = 5 × (4 + 8) = 60.`;

const ancientMultInfoHtmlElements =
    [createP(ancientMultInfoStart), ancientMultStepsOl, ancientMultResultP, createPWithInnerHtml(ancientMultExampleParagraph)];

/**
 * @typedef {{powerOf2: string, correspondingMultiple: string}} TableRow
 * 
 * @param {{table1Rows: TableRow[], table2Rows: TableRow[], product: string}} infoObject
 * @param {string} inputString1
 * @param {string} inputString2
 * @returns {HTMLElement[]}
 */
function getAncientMultiplicationInfoElements(infoObject, inputString1, inputString2) {
    const { table1Rows, table2Rows, product } = infoObject;
    const correspondingMultiplesHeading = `Corresponding Multiples of ${inputString2}`;
    const table1Headings = [`Powers of 2 <= ${inputString1}`, correspondingMultiplesHeading];
    const table2Headings = [`Powers of 2 That Sum to ${inputString1}`, correspondingMultiplesHeading];

    /**
     * @param {TableRow} row 
     * @returns {string[]}
     */
    const rowToArray = (row) => [row.powerOf2, row.correspondingMultiple];

    const heading =
        createH3(`Ancient Egyptian Multiplication Info for ${inputString1} and ${inputString2}`);
    const table1 = createTable(table1Headings, table1Rows, rowToArray);
    const table2 = createTable(table2Headings, table2Rows, rowToArray);
    const productP =
        createP(`The sum of the bottom right column is ${getNumberStringWithCommas(product)}, which is the product.`);

    return [heading, table1, table2, productP];
}

new Section({
    btnIdStart: 'ancientMultiplication',
    infoHtml: ancientMultiplicationInfoHtmlElements,
    isSingleInputSection: false,
    actionSentenceEnding: 'ancient Egyptian multiplication info for those integers',
    minInput: 2,
    maxInput: 10,
    apiEndpoint: 'ancientMultiplicationAnswer',
    getElements: getAncientMultiplicationInfoElements
});
