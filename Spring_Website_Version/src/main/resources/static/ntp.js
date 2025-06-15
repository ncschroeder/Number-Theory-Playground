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
 * @param {string} infoString A string with paragraphs of info, each of which is separated by a blank line.
 * @returns {HTMLParagraphElement[]} Each p in here contains a paragraph in infoString.
 */
const createPsWithParagraphs = (infoString) => infoString.split(/\n\s*\n/).map(createPWithInnerHtml);

/**
 * @param {string} text
 * @returns {HTMLButtonElement}
 */
function createBtn(text) {
    /**
     * @type {HTMLButtonElement}
     */
    const btn = createElement('button', text);
    btn.type = 'button';
    return btn;
}

/**
 * @param  {...Appendable} children 
 * @returns {HTMLLIElement}
 */
const createLi = (...children) => createElement('li', ...children);

 * @returns {HTMLDivElement}
 */
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
 * @param {number} n 
 * @returns {boolean}
 */

/**
 * @param {number} num 
 * @returns {boolean}
 */
const isOdd = (num) => !isEven(num);

/**
 * @type {Section}
 */
let curSection;

/**
 * @param {HTMLInputElement} inputField 
 * @returns {?number}
 */
function getNum(inputField) {
    const trimmedValue = inputField.value.trim().replaceAll(',', '');
    if (trimmedValue.length === 0) return null;
    const containsNonDigit = /\D/.test(trimmedValue);
    if (containsNonDigit) return null;
    const num = Number(trimmedValue);
    return Number.isSafeInteger(num) ? num : null;
}
const isEven = (n) => isDivisible(n, 2);

/**
 * @param {string} id 
 * @returns {{ inputDiv: HTMLDivElement, inputField: HTMLInputElement }}
 */
function createInputDiv(id) {
    /**
     * @type {HTMLInputElement}
     */
    const inputField = createElement('input');
    inputField.setAttribute('type', 'number');
    inputField.className = 'inputField';

    const randomizeBtn = createBtn('Randomize');
    randomizeBtn.className = 'randomizeBtn';
    randomizeBtn.onclick = () => inputField.value = curSection.getRandomInput();

    const plusBtn = createBtn('+');
    plusBtn.className = 'plusBtn';
    plusBtn.onclick = () => {
        const { minInput, maxInput, needsEvenInput } = curSection;

        let inputNum = getNum(inputField);
        if (inputNum === null) {
            if (inputField.value.length === 0) {
                inputField.value = minInput;
            }
            return;
        }

        inputNum =
            inputNum < minInput || inputNum >= maxInput
            ? minInput
            : inputNum + (needsEvenInput && isEven(inputNum) ? 2 : 1);
        inputField.value = inputNum;
    };

    const minusBtn = createBtn('−');
    minusBtn.className = 'minusBtn';
    minusBtn.onclick = () => {
        const { minInput, maxInput, needsEvenInput } = curSection;

        let inputNum = getNum(inputField);
        if (inputNum === null) {
            if (inputField.value.length === 0) {
                inputField.value = maxInput;
            }
            return;
        }

        inputNum =
            inputNum <= minInput || inputNum > maxInput
            ? maxInput
            : inputNum - (needsEvenInput && isEven(inputNum) ? 2 : 1);
        inputField.value = inputNum;
    };

    const inputDiv = createDiv(inputField, randomizeBtn, plusBtn, minusBtn);
    inputDiv.id = id;
    inputDiv.className = 'inputDiv';

    return { inputDiv, inputField };
}


const commaAdder = new Intl.NumberFormat();
const getNumberStringWithCommas = commaAdder.format;
const sectionHeading = getElementById('sectionHeading');
const homeHeadingText = sectionHeading.textContent;
const homeContentDiv = getElementById('homeContentDiv');

const sectionInfoDetailsSummary = createElement('summary', 'Info');
const sectionInfoDiv = createDiv();
sectionInfoDiv.id = 'sectionInfoDiv';
sectionInfoDiv.className = 'nonAnswerInfoDiv';
/**
 * @param {number} a
 * @param {number} b
 * @returns 
 * @type {HTMLDetailsElement}
 */
const numPairToString = (a, b) => `${getNumberStringWithCommas(a)} & ${getNumberStringWithCommas(b)}`;
const sectionInfoDetails = createElement('details', sectionInfoDetailsSummary, sectionInfoDiv);

const LEFT_INPUT_DIV_ID = 'leftInputDiv';
const { inputDiv: inputDiv1, inputField: inputField1 } = createInputDiv(LEFT_INPUT_DIV_ID);
const { inputDiv: inputDiv2, inputField: inputField2 } = createInputDiv('rightInputDiv');
const inputDivDiv = createDiv(inputDiv1, inputDiv2);
inputDivDiv.id = 'inputDivDiv';

const sectionDirectionsP = createP();
const calculateBtn = createBtn('Calculate');
calculateBtn.id = 'calculateBtn';
const answerDiv = createDiv();
answerDiv.id = 'answerDiv';

const sectionContentDiv =
    createDiv(sectionInfoDetails, sectionDirectionsP, inputDivDiv, calculateBtn, answerDiv);
sectionContentDiv.id = 'sectionContentDiv';

getElementById('homeBtn').onclick = () => {
    if (Object.is(document.body.lastElementChild, sectionContentDiv)) {
        sectionHeading.textContent = homeHeadingText;
        document.body.replaceChild(homeContentDiv, sectionContentDiv);
    }
};







// Max input constants.
const ONE_MILLION = 1_000_000;
const ONE_HUNDRED_MILLION = ONE_MILLION * 100;
const ONE_BILLION = 1_000_000_000;
const NINE_QUADRILLION = 9_000_000_000_000_000;


class Section {
    /*
    For this class and its subclasses, use private fields with
    public getters to allow for read-only access outside the class.
     */

    #minInput;
    #maxInput;
    #apiEndpoint;

    /**
     * 
     * @typedef {Object} SectionParams
     * @property {string} btnIdStart
     * @property {string | HTMLElement[]} infoHtml
     * @property {boolean} [isSingleInputSection=true]
     * @property {string} actionSentenceEnding
     * @property {number} minInput
     * @property {number} maxInput
     * @property {string} apiEndpoint
     * 
     * @param {SectionParams} params 
     */
    constructor(params) {
        const { 
            btnIdStart, infoHtml, actionSentenceEnding, minInput, maxInput, apiEndpoint
        } = params;

        this.#minInput = minInput;
        this.#maxInput = maxInput;
        this.#apiEndpoint = apiEndpoint;
        
        const isSingleInputSection = this instanceof SingleInputSection;
        const sectionBtn = getElementById(btnIdStart + 'Btn');
        const heading = sectionBtn.textContent;

        /**
         * @type {HTMLElement[]}
         */
        const infoHtmlElements =
            Array.isArray(infoHtml)
            ? infoHtml
            : createPsWithParagraphs(infoHtmlStringOrArr);

        const maxInputString = getNumberStringWithCommas(maxInput);

        /*
        If the max input is one of the nums that's a key in the map below, then have the 2nd directions sentence
        say that the input num(s) should be ≤ the corresponding string value in the map followed by the num with
        commas in parentheses. If the max input isn't one of the nums that's a key in the map, then have that
        sentence just say that input num(s) should be ≤ that num with commas.
        
        The max inputs that aren't keys are 500 and 100,000, the max inputs for the Pythagorean triples and
        Goldbach Conjecture sections, respectively.
         */
        const maxInputsAndStringsWithWords =
            new Map([
                [ONE_MILLION, '1 million'],
                [ONE_HUNDRED_MILLION, '100 million'],
                [ONE_BILLION, '1 billion'],
                [NINE_QUADRILLION, '9 quadrillion']
            ]);

        /**
         * @type {?string}
         */
        const maxInputStringWithWord = maxInputsAndStringsWithWords.get(maxInput);
        const maxInputSentencePart =
            maxInputStringWithWord ? `${maxInputStringWithWord} (${maxInputString})` : maxInputString;

        const validNumSentencePart1 =
            this.isSingleInputSection
            ? `this number be a whole number that's`
            : 'these numbers be whole numbers that are';

        const validNumSentencePart2 =
            this.needsEvenInput
            ? `even, ≥ ${minInput}, & ≤ ${maxInputSentencePart}`
            : `≥ ${minInput} & ≤ ${maxInputSentencePart}`;

        const directions =
            `Enter or generate ${this.isSingleInputSection ? 'a number' : '2 numbers'} and click the \
            "Calculate" button to get ${actionSentenceEnding}. Have ${validNumSentencePart1} \
            ${validNumSentencePart2}. Commas are optional.`;


        // Use arrow function so that this refers to the Section instance.
        const goToThisSection = () => {
            curSection = this;
            sectionHeading.textContent = heading;
            sectionInfoDetails.open = false;
            sectionInfoDiv.replaceChildren(...infoHtmlElements);
            sectionDirectionsP.textContent = directions;
            inputField1.value = '';
            inputField2.value = '';
            answerDiv.innerHTML = '';

            if (isSingleInputSection) {
                if (Object.is(inputDivDiv.lastElementChild, inputDiv2)) {
                    inputDivDiv.removeChild(inputDiv2);
                    inputDiv1.id = 'onlyInputDiv';
                }
            } else if (Object.is(inputDivDiv.lastElementChild, inputDiv1)) {
                inputDivDiv.appendChild(inputDiv2);
                inputDiv1.id = LEFT_INPUT_DIV_ID;
            }

            if (Object.is(document.body.lastElementChild, homeContentDiv)) {
                document.body.replaceChild(sectionContentDiv, homeContentDiv);
            }
        }

        sectionBtn.onclick = goToThisSection;
    }


    get minInput() {
        return this.#minInput;
    }

    get maxInput() {
        return this.#maxInput;
    }

    get apiEndpoint() {
        return this.#apiEndpoint;
    }

    get needsEvenInput() {
        return this instanceof GoldbachConjectureSection;
    }


    /**
     * @param {number} num 
     * @returns {boolean}
     */
    isInvalidInput(num) {
        return num < this.minInput || num > this.maxInput;
    }

    /**
     * @param {number} lowerBound 
     * @param {number} upperBound This is an exclusive bound.
     */
    static #getRandomNum = (lowerBound, upperBound) =>
        Math.floor(Math.random() * (upperBound - lowerBound)) + lowerBound;

    /**
     * First, a random number of digits will be generated for a random number. The min number of random digits
     * is 1 since all sections have a single digit integer for their min input. If the max input is a power of
     * 10, then the max number of digits for the random number is the number of digits of the max input - 1.
     * Otherwise, the max number of random digits is the number of digits of the max input.
     * 
     * Then, a random number with the generated random number of digits will be generated and returned. The
     * random number will be > the min input & < the max input of this section.
     * 
     * @returns {number}
     */
    getRandomInput() {
        const log10MaxInput = Math.log10(this.maxInput);
        const log10MaxInputFloor = Math.floor(log10MaxInput);
        // If the above 2 numbers are equal, then the max input is a power of 10.
        const numMaxInputDigits = log10MaxInputFloor + 1;
        const numMaxRandomInputDigits =
            log10MaxInputFloor == log10MaxInput ? numMaxInputDigits - 1 : numMaxInputDigits;
        const numRandomInputDigits = Section.#getRandomNum(1, numMaxRandomInputDigits + 1);
        const lowerBound =
            numRandomInputDigits === 1 ? this.minInput : Math.pow(10, numRandomInputDigits - 1);
        const upperBound =
            numRandomInputDigits === numMaxInputDigits ? this.maxInput + 1 : Math.pow(10, numRandomInputDigits);
        return Section.#getRandomNum(lowerBound, upperBound);
    }
}

getElementById('calculateBtn').onclick = () => {
class SingleInputSection extends Section {
    #getElements;

    /**
     * @typedef {(responseObj: any, inputString: string, inputNum: number) => HTMLElement[]} SingleInputGetElementsFunction
     */

    /**
     * @param {SectionParams} sectionParams 
     * @param {SingleInputGetElementsFunction} getElements 
     */
    constructor(sectionParams, getElements) {
        super(sectionParams);
        this.#getElements = getElements;
    }

    get getElements() {
        return this.#getElements;
    }
}

class GoldbachConjectureSection extends SingleInputSection {
    /**
     * @param {SectionParams} sectionParams 
     * @param {SingleInputGetElementsFunction} getElements 
     */
    constructor(sectionParams, getElements) {
        super(sectionParams, getElements);
    }

    /**
     * @param {number} num 
     * @returns {boolean}
     */
    isInvalidInput(num) {
        return super.isInvalidInput(num) || isOdd(num);
    }

    /**
     * @returns {number}
     */
    getRandomInput() {
        let randomInput;
        do {
            randomInput = super.getRandomInput();
        } while (isOdd(randomInput));
        return randomInput;
    }
}

class DoubleInputSection extends Section {
    #getElements;

    /**
     * @param {SectionParams} sectionParams 
     * @param {(responseObj: any, inputString1: string, inputString2: string) => HTMLElement[]} getElements 
     */
    constructor(sectionParams, getElements) {
        super(sectionParams);
        this.#getElements = getElements;
    }

    get getElements() {
        return this.#getElements;
    }
}


    const inputNum1 = getNum(inputField1);
    if (inputNum1 === null || curSection.isInvalidInput(inputNum1)) return;
    /**
     * @type {(responseObj: any) => HTMLElement[]}
     */
    let getElements;
    const inputString1 = getNumberStringWithCommas(inputNum1);
    const urlParams = new URLSearchParams();

    if (curSection instanceof SingleInputSection) {
        urlParams.append('input', inputNum1);
        getElements = (responseObj) => curSection.getElements(responseObj, inputString1, inputNum1);
    } else if (curSection instanceof DoubleInputSection) {
        const inputNum2 = getNum(inputField2);
        if (inputNum2 === null || curSection.isInvalidInput(inputNum2)) return;
        urlParams.append('input1', inputNum1);
        urlParams.append('input2', inputNum2);
        const inputString2 = getNumberStringWithCommas(inputNum2);
        getElements = (responseObj) => curSection.getElements(responseObj, inputString1, inputString2);
    }
    
    const errorMessage = 'Error with request.';

    fetch(`calculate/${curSection.apiEndpoint}?${urlParams}`)
    .then(response => response.ok ? response.json().then(getElements) : [errorMessage])
    .then(elementsOrErrorMessage => answerDiv.replaceChildren(...elementsOrErrorMessage))
    .catch(reason => {
        answerDiv.replaceChildren(errorMessage);
        console.error(errorMessage, reason);
    });
}


const primesInfoHtml =
    `A <i>prime number</i>, or a <i>prime</i>, is a whole number > 1 that isn't divisible by any whole numbers
    other than 1 and itself. A <i>composite number</i> is a whole number > 1 that is divisible by a whole number
    other than 1 and itself. The first 10 primes are 2, 3, 5, 7, 11, 13, 17, 19, 23, and 29. There are an
    infinite amount of primes. The largest known prime is 2<sup>136,279,841</sup> − 1. It has 41,024,320 digits!
    Primes are used in 7 of the 10 sections in this application.
    
    With the exception of 2 and 3, all primes are either 1 above or 1 below a multiple of 6. To show why this
    is the case, let's have a variable <var>i</var> and let it represent any whole number ≥ 6 that's a multiple
    of 6. We know that <var>i</var> is divisible by 2 and 3 so <var>i</var> + 2 and <var>i</var> + 4 are divisible
    by 2 and <var>i</var> + 3 is divisible by 3 but we don't have any guarantees about what <var>i</var> + 1 and
    <var>i</var> + 5 are divisible by. Therefore, that's where primes can be.
    
    A whole number can be determined to be prime if it's not divisible by any primes ≤ the square root of that
    number. This is called <i>trial division</i>. Let's determine if 29 and 33 are prime. 5<sup>2</sup> = 25
    and 6<sup>2</sup> = 36 so 5 < <math><msqrt><mn>29</mn></msqrt></math> < <math><msqrt><mn>33</mn></msqrt></math>
    < 6. We check if 29 and 33 are divisible by 2, 3, or 5; which are the primes ≤ 5. 29 isn't divisible by any
    of those and 33 is divisible by 3 so 29 is prime and 33 isn't.`;

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

new SingleInputSection(
    {
        btnIdStart: 'primeNums',
        infoHtml: primesInfoHtml,
        actionSentenceEnding: 'the first 30 prime numbers ≥ that number',
        minInput: 0,
        maxInput: ONE_HUNDRED_MILLION,
        apiEndpoint: 'primes'
    },
    getPrimesElements
);


const twinPrimePairsInfoHtml =
    `A <i>twin prime pair</i> is a pair of prime numbers that differ by 2. The first 5 twin prime pairs are
    3 & 5, 5 & 7, 11 & 13, 17 & 19, and 27 & 29. The largest known twin prime pair is
    an infinite amount of twin prime pairs. A <i>conjecture</i> is a statement that is believed to be true but
    has not been proven to be.
    (2,996,863,034,895 × 2<sup>1,290,000</sup>) ± 1. They have 388,342 digits! It's conjectured that there are
    
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

new SingleInputSection(
    {
        btnIdStart: 'twinPrimePairs',
        infoHtml: twinPrimePairsInfoHtml,
        actionSentenceEnding: 'the first 20 twin prime pairs ≥ that number',
        minInput: 0,
        maxInput: ONE_MILLION,
        apiEndpoint: 'twinPrimePairStarts'
    },
    getTwinPrimePairsElements
);


const pfInfoHtml =
    `The Fundamental Theorem of Arithmetic says that every whole number > 1 can be expressed as the product of
    prime numbers in 1 way if you ignore the order of those prime numbers. The <i>prime factorization</i> (PF)
    of a whole number > 1 is an expression of the prime numbers whose product is that number. For example; the
    PF of 5 is just 5, the PF of 25 is 5<sup>2</sup>, and the PF of 12,250 is 2 × 5<sup>3</sup> × 7<sup>2</sup>
    if the prime numbers are in ascending order. 12,250 could also be expressed as
    5<sup>3</sup> × 2 × 7<sup>2</sup> but that's the same expression as the previous one if you ignore the order
    of the prime numbers. The Number Theory Playground displays PFs with the prime numbers in ascending order.
    There are some interesting applications for PFs. See the info for the "Divisibility" or "GCD and LCM"
    sections for some applications.
    
    The input numbers with the highest amount of prime factors are 536,870,912 (2<sup>29</sup>) and 805,306,368
    (2<sup>28</sup> × 3). An input number with the highest amount of unique prime factors is 223,092,870. This
    number is the product of the first 9 prime numbers so it has 9 unique prime factors and its PF is
    2 × 3 × 5 × 7 × 11 × 13 × 17 × 19 × 23. You could also multiply that number by 2, 3, or 4 and those numbers
    are ≤ the max input and have the same amount of unique prime factors.`;

/**
 * @typedef FactorAndPower
 * @type {object}
 * @property {number} factor
 * @property {number} power
 * @typedef {{pfArr: ?PfArray, correspondingNumString: string}} PfArrayAndNumberString
 * @typedef {{ factor: number, power: number }} FactorAndPower
 * @typedef {{ fpArr: ?FactorAndPower[], correspondingNumString: string }} FactorAndPowerArrayAndNumberString
 * @param {FactorAndPower[]} fpArr
 * @returns {HTMLSpanElement}
 */
function getPfSpan(fpArr) {
    const span = createSpan();
    for (let i = 0; i < fpArr.length; i++) {

        const { factor, power } = fpArr[i];
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
 * @param {FactorAndPower[]} fpArr
 * @param {string} inputString
 * Array that represents the prime factorization of inputNumber.
 * @returns {HTMLElement[]}
 * An array with a heading and a span that displays the prime factorization from pfArray.
 */
    const heading = createH3(`The prime factorization of ${inputString} is:`);
function getPfElements(fpArr, inputString) {
    return [heading, getPfSpan(fpArr)];
}

const PF_MIN_INPUT = 2;

new SingleInputSection(
    {
        btnIdStart: 'pf',
        infoHtml: pfInfoHtml,
        minInput: PF_MIN_INPUT,
        maxInput: ONE_BILLION,
        actionSentenceEnding: 'the prime factorization of that number',
        apiEndpoint: 'primeFactorization'
    },
    getPfElements
);



const divisInfoStartHtml =
    `Say we have 2 whole numbers that we'll represent with the variables <var>a</var> and <var>b</var>. If we
    divide <var>a</var> by <var>b</var> and get no remainder, then <var>a</var> is said to be <i>divisible</i>
    by <var>b</var> and <var>b</var> is said to be a <i>factor</i> or <i>divisor</i> of <var>a</var>. If you
    want to find some whole number factors of a whole number, you could manually do some division but there are
    other ways to find them.`;

const divisPfInfoHtml = 
    `The factors of a whole number > 1 can be found by looking at its prime factorization (PF). Let's have a
    variable <var>n</var> and let it represent a whole number > 1. First, you can find how many factors
    and then multiplying all these together. For example, the PF of 36 is 2<sup>2</sup> × 3<sup>2</sup>. The
    powers are 2 and 2, so there are 3 × 3 = 9 factors. However, that count includes 1 and the number that the
    PF is for (36 in this case). If you want to exclude those, then subtract 2. That would give us 7 factors.
    You can find the factors of <var>n</var> by finding all the PFs within <var>n</var>'s PF, or the
    "sub-factorizations", as I like to call them. For 2<sup>2</sup> × 3<sup>2</sup>, the sub-factorizations are
    2, 3, 2<sup>2</sup> (4), 2 × 3 (6), 3<sup>2</sup> (9), 2<sup>2</sup> × 3 (12), and 2 × 3<sup>2</sup> (18).`;

const divisPfInfoDiv =
    createDiv(createH3('Prime Factorization'), ...createPsWithParagraphs(divisPfInfoHtml));
    
const divisRulesInfoHtml =
    rule for each whole number in the range of 3 to 12, excluding 5 and 10, though there are rules for more
    whole numbers and many whole numbers have multiple rules. I'll go over an example of using these rules to
    find the factors of a whole number in the next paragraph. Let's have a variable <var>n</var> and let it
    represent a whole number. If the last 2 digits of <var>n</var> is divisible by 4, then <var>n</var> is
    divisible by 4. If the last 3 digits of <var>n</var> is divisible by 8, then <var>n</var> is divisible by 8.
    If the sum of the digits of <var>n</var> is divisible by 3, then <var>n</var> is divisible by 3. If the sum
    of the digits of <var>n</var> is divisible by 9, then <var>n</var> is divisible by 9. If <var>n</var> is
    even and divisible by 3, then it's also divisible by 6. If <var>n</var> is divisible by both 3 and 4, then
    it's also divisible by 12. For 7, we split <var>n</var> into blocks of 3 from right to left. Coincidentally,
    these are the blocks separated by commas if we write <var>n</var> with commas. We do an alternating sum of the
    blocks from right to left. We start with 0, add the last block, subtract the 2<sup>nd</sup> to last block,
    add the 3<sup>rd</sup> to last block, and so on for all the blocks. If this alternating sum is divisible by
    7, then <var>n</var> is divisible by 7. For 11, we do an alternating sum of digits of <var>n</var> from left
    to right. We start with 0, add the 1<sup>st</sup> digit, subtract the 2<sup>nd</sup> digit, add the
    3<sup>rd</sup> digit, and so on for all digits. If this alternating sum is divisible by 11, then <var>n</var>
    is divisible by 11.
    
    Here's an example. Let <var>n</var> be 4,695,768. The PF of <var>n</var> is
    2<sup>3</sup> × 3<sup>2</sup> × 7<sup>2</sup> × 11<sup>3</sup>. We can tell from that PF that <var>n</var>
    is divisible by all the numbers that had rules mentioned about them above. Let's check if <var>n</var> is
    divisible by those numbers using those rules. The last 2 digits are 68, which is divisible by 4. The last 3
    digits are 768, which is divisible by 8. The sum of the digits is 4 + 6 + 9 + 5 + 7 + 6 + 8 = 45, which is
    divisible by 3 and 9. Since <var>n</var> is even and divisible by 3, it's also divisible by 6. Since
    <var>i</var> is divisible by both 3 and 4, it's also divisible by 12. The alternating sum of blocks of 3
    from right to left is 768 − 695 + 4 = 77 , which is divisible by 7. The alternating sum of digits from left
    to right is 4 − 6 + 9 − 5 + 7 − 6 + 8 = 11, which, of course, is divisible by 11.`;

const divisRulesInfoDiv =
    createDiv(createH3('Divisibility Rules'), ...createPsWithParagraphs(divisRulesInfoHtml));

const divisInfoElements =
    [createPWithInnerHtml(divisInfoStartHtml), divisPfInfoDiv, divisRulesInfoDiv];

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
 * @property {FactorAndPower[]} inputFpArr
 * @property {string} numFactorsExpression
 * @property {number} numFactors
 * @property {FactorAndPowerArrayAndNumberString[]} factorFpArrsAndNumStrings
 */

/** 
 * @param {{ rulesData: ?DivisibilityRulesData, pfAnswer: ?DivisibilityPfAnswer }}
 * @param {string} inputString
 * @param {number} inputNum 
 * @returns {HTMLElement[]}
 * An array that contains elements with divisibility info about the argument number based on the info in infoObject.
 */
function getDivisibilityInfoElements({ rulesData, pfAnswer }, inputString, inputNum) {
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
            getDivisibilitySentence(4, last2Digits, isDivisibleBy4)
            `The last 2 digits form the number ${last2Digits}.`,
        );

        if (isDivisibleBy4) {
            if (inputNum >= 1_000) {
                const isDivisibleBy8 = isDivisible(last3Digits, 8);
                sentences.push(
                    getDivisibilitySentence(8, last3Digits, isDivisibleBy8)
                    `The last 3 digits form the number ${last3Digits}`,
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
        const isDivisibleBy9 = isDivisible(sumOfDigits, 9);
        sentences.push(getDivisSentence(9, sumOfDigits, isDivisibleBy9));

        if (isEvenVar) {
            sentences.push(`${inputString} is even and divisible by 3 so it's also divisible by 6.`);

            if (isDivisibleBy4) {
                sentences.push(`${inputString} is divisible by both 3 and 4 so it's also divisible by 12.`);
            } else if (inputNum < 100) {
                sentences.push(`${inputString} isn't divisible by 4 so it isn't divisible by 12.`)
            }
        }
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

    const { inputFpArr, numFactorsExpression, numFactors, factorFpArrsAndNumStrings } = pfAnswer;
    pfInfoParagraph.append(getPfSpan(inputFpArr), '. ');

    const numFactorsInfoEnd =
        numFactors === 3
        ? `'s 1 factor`
        : ` are ${createNumStringWithCommas(numFactors - 2)} factors`;

    const numFactorsInfo =
        `By looking at the powers, we can see that there are ${numFactorsExpression} = \
        ${createNumStringWithCommas(numFactors)} factors. If 1 and ${inputString} are excluded then there${numFactorsInfoEnd}.`;

    pfInfoParagraph.append(numFactorsInfo, ' The factors and their PFs are:');

    /**
     * @param {FactorAndPowerArrayAndNumberString} 
     * @returns {HTMLLIElement}
     */
    function fpArrAndNumStringToLi({ fpArr, correspondingNumString }) {
        const numStringWithCommas = getNumberStringWithCommas(correspondingNumString);
        return fpArr ? createLi(getPfSpan(fpArr), ` (${numStringWithCommas})`) : createLi(numStringWithCommas);
    }

    pfDiv.appendChild(arrayToOl(factorFpArrsAndNumStrings, fpArrAndNumStringToLi));
    return pfDiv;
}

new SingleInputSection(
    {
        btnIdStart: 'divisibility',
        infoHtml: divisbilityInfoHtml,
        minInput: PF_MIN_INPUT,
        maxInput: ONE_BILLION,
        actionSentenceEnding: 'divisbility info for that number',
        apiEndpoint: 'divisibilityAnswer'
    },
    getDivisibilityInfoElements
);


const gcdAndLcmInfoStart =
    `GCD stands for greatest common divisor and is also known as greatest common factor, or GCF. LCM stands for
    least common multiple. To find the GCD and LCM of 2 whole numbers, you could manually do some division and
    multiplication but there are other ways to find them.`;

const euclideanInfoStartHtml =
    `The Euclidean algorithm can be used to find just the GCD of 2 whole numbers. This was named after the
    ancient Greek mathematician Euclid.
    
    A simple way of explaining this algorithm is that it starts with 2 whole numbers that we want to find the GCD
    of and if the max of those numbers is divisible by the min, then that min is the GCD. Otherwise, the GCD of
    the 2 numbers is the same as the GCD of the min and the remainder when the max is divided by the min. Repeat.

    Another way of explaining this algorithm is that it consists of iterations and each one consists of a
    max number, min number, and remainder when the max number is divided by the min number. These'll be referred
    to as just the max, min, and remainder. The 1<sup>st</sup> iteration has a max of the max of 2 whole numbers
    that you want to find the GCD of. The min of this iteration is the min of those 2 numbers. If the remainder
    is 0, then the algorithm is done and the GCD of the 2 numbers that we wanted to find the GCD of is the min
    of this iteration. Otherwise, we do another iteration and the max of the new iteration is the min of the
    last iteration and the min of the new iteration is the remainder of the last iteration. Again, we check if
    the remainder is 0 and if it is, then the min of this iteration is the GCD. Otherwise, we keep doing
    iterations until we get a remainder of 0.`;
    
/**
 * @typedef {{ max: number, min: number, remainder: number }} EuclideanIteration
 */

/**
 * @param {EuclideanIteration[]} iterations 
 * @returns {HTMLTableElement}
 */
function createEuclideanTable(iterations) {
    const tableHeadings = ['Max', 'Min', 'Remainder'];
    /**
     * @param {EuclideanIteration} ei 
     * @returns {number[]}
     */
    const getTableRowNums = (ei) => [ei.max, ei.min, ei.remainder];
    return createTable(tableHeadings, iterations, getTableRowNums);
}

/**
 * @param {EuclideanIteration[]} iterations 
 * @returns {number}
 */
const getGcd = (iterations) => iterations[iterations.length - 1].min;

/**
 * @param {EuclideanIteration[]} iterations 
 * @returns {HTMLDivElement}
 */
function createEuclideanExampleDiv(iterations) {
    const startText =
        `Let's find the GCD of ${iterations[0].min} and ${iterations[0].max} using the Euclidean algorithm. \
        Here are the iterations:`;
    const endText = `The GCD is ${getGcd(iterations)}.`;
    return createDiv(createP(startText), createEuclideanTable(iterations), createP(endText));
}
    
/**
 * @type {EuclideanIteration[]}
 */
const euclideanExample1Iterations =
    [
        { max: 35, min: 6, remainder: 5 },
        { max: 6, min: 5, remainder: 1 },
        { max: 5, min: 1, remainder: 0}
    ];

/**
 * @type {EuclideanIteration[]}
 */
const euclideanExample2Iterations =
    [
        { max: 99, min: 54, remainder: 45 },
        { max: 54, min: 45, remainder: 9 },
        { max: 45, min: 9, remainder: 0 }
    ];

const euclideanInfoDiv =
    createDiv(
        createH3('The Euclidean Algorithm'),
        ...createPsWithParagraphs(euclideanInfoStartHtml),
        createEuclideanExampleDiv(euclideanExample1Iterations),
        createEuclideanExampleDiv(euclideanExample2Iterations)
    );

const gcdAndLcmPfInfoHtml =
    `The GCD and LCM of 2 whole numbers > 1 can be found by looking at their prime factorizations (PFs). If those
    numbers don't have any common prime factors, then the GCD is 1. If they do have common prime factors, then
    the GCD PF consists of all the common prime factors and the power of each factor is the min of the powers of
    that factor in the 2 PFs. The LCM PF consists of all factors that are in either of the PFs of the 2 numbers.
    If a factor is in both PFs, then the power of that factor in the LCM PF is the max of the powers of that
    factor in the 2 PFs. If a factor is unique to one of the PFs, then that factor and its power are in the LCM PF.

    Let's find the GCD and LCM of 6 and 35 using their PFs. The PF of 6 is 2 × 3 and the PF of 35 is 5 × 7.
    There are no common prime factors so the GCD is 1. The LCM PF is 2 × 3 × 5 × 7, which is 210.
    
    Let's find the GCD and LCM of 54 and 99 using their PFs. The PF of 54 is 2 × 3<sup>3</sup> and the PF of 99
    is 3<sup>2</sup> × 11. 3 is the only common prime factor and the min power of it is 2 so the GCD PF is
    3<sup>2</sup>, which is 9. The max power of 3 is 3 so 3<sup>3</sup> is in the LCM PF. The LCM PF is
    2 × 3<sup>3</sup> × 11, which is 594.`;

const gcdAndLcmPfInfoDiv =
    createDiv(createH3('Prime Factorizations'), ...createPsWithParagraphs(gcdAndLcmPfInfoHtml));

const gcdAndLcmInfoEndHtml =
    `The input numbers whose LCM is the highest are 1 billion, the max input; and 1 billion − 1. The LCM is 
    999,999,999,000,000,000, or 999 quadrillion 999 trillion 999 billion. It has 18 digits.
    
    A pair of input numbers whose LCM might have the highest amount of unique prime factors is 223,092,870, the
    product of the first 9 prime numbers; and 64,097,801, the product of the next 5 prime numbers. The LCM is
    14,299,762,385,778,870, or 14 quadrillion ... It has 17 digits and 14 unique prime factors and its PF is
    2 × 3 × 5 × 7 × 11 × 13 × 17 × 19 × 23 × 29 × 31 × 37 × 41 × 47! Other pairs of input numbers have the same LCM,
    such as that 1<sup>st</sup> input number divided by 2 and the 2<sup>nd</sup> input number multiplied by 2.`;

const gcdAndLcmInfoEndDiv =
    createDiv(createH3('Other Info'), ...createPsWithParagraphs(gcdAndLcmInfoEndHtml));

const gcdAndLcmInfoElements =
    [createP(gcdAndLcmInfoStart), euclideanInfoDiv, gcdAndLcmPfInfoDiv, gcdAndLcmInfoEndDiv];

/**
 * @typedef GcdAndLcmPfAnswer
 * @type {object}
 * 
 * @param {{euclideanIterations: EuclideanIteration[], gcdAndLcmPfAnswer: GcdAndLcmPfAnswer}} infoObject 
 * @property {FactorAndPower[]} input1FpArr
 * @property {FactorAndPower[]} input2FpArr
 * @property {?FactorAndPowerArrayAndNumberString} gcdFpArrAndNumString
 * @property {FactorAndPowerArrayAndNumberString} lcmFpArrAndNumString
 * @param {string} inputString1
 * @param {string} inputString2
 * @returns {HTMLElement[]}
 */
function createGcdAndLcmAnswerElements({ euclideanIterations, pfAnswer }, inputString1, inputString2) {
    const mainHeading = createH3(`GCD and LCM Info for ${inputString1} and ${inputString2}`);
    const euclideanDiv = createEuclideanAnswerDiv(euclideanIterations, inputString1, inputString2);
    const pfDiv = createGcdAndLcmPfAnswerDiv(pfAnswer, inputString1, inputString2);
    return [mainHeading, euclideanDiv, pfDiv];
}

/**
 * @param {EuclideanIteration[]} iterations
 * @param {string} inputString1 
 * @param {string} inputString2 
 * @returns {HTMLDivElement}
 */
function createEuclideanAnswerDiv(iterations, inputString1, inputString2) {
    const heading = createH4('Euclidean Algorithm Iterations');
    const table = createEuclideanTable(iterations);
    const gcdString = createNumStringWithCommas(getGcd(iterations));
    const gcdMessageP =
        createP(`The GCD of ${inputString1} and ${inputString2} is ${gcdString}.`);

    return createDiv(heading, table, gcdMessageP);
}

/**
 * @param {GcdAndLcmPfAnswer} answer 
 * @param {string} inputString1 
 * @param {string} inputString2 
 * @returns {HTMLDivElement}
 */
function createGcdAndLcmPfAnswerDiv(answer, inputString1, inputString2) {

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
     * @param {FactorAndPowerArrayAndNumberString}
     */
        const div = createDiv();
        div.append(`The PF of the ${gcdOrLcmText} is `);
        if (factorsAndPowers) {
            div.append(getPfSpan(factorsAndPowers), `, which is `);
    function createInnerDiv2(gcdOrLcmText, { pfArr, correspondingNumString }) {
        }
        div.append(getNumberStringWithCommas(correspondingNumString));
        return div;
    }

    const { input1FpArr, input2FpArr, gcdFpArrAndNumString, lcmFpArrAndNumString } = answer;

    /**
     * @type {Appendable}
     */
    const gcdInfoAppendable =
        ? createInnerDiv2('GCD', gcdPfArrAndNumString)
        gcdFpArrAndNumString
        : 'There are no common prime factors so the GCD is 1.';

    return createDiv(
        createInnerDiv1(inputString1, input1PfArr),
        createInnerDiv1(inputString2, input2PfArr),
        gcdInfoAppendable,
        createInnerDiv2('LCM', lcmPfArrAndNumString)
    );
}

new DoubleInputSection(
    {
        btnIdStart: 'gcdAndLcm',
        infoHtml: gcdAndLcmInfoHtml,
        minInput: PF_MIN_INPUT,
        maxInput: ONE_BILLION,
        actionSentenceEnding: 'GCD and LCM info for those numbers',
        apiEndpoint: 'gcdAndLcmAnswer'
    },
    getGcdAndLcmInfoElements
);


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
function createGoldbachConjectureElements(primePairStarts, inputString, inputNum) {
    const thereIs1Pair = primePairStarts.length === 1;
    const headingText =
        `There${thereIs1Pair ? `'s 1 pair` : ` are ${createNumStringWithCommas(primePairStarts.length)} pairs`} \
        of prime numbers that sum to ${inputString}. ${thereIs1Pair ? 'It is' :  'They are'}:`;
    const pairsOl = arrayToOl(primePairStarts, (start) => numPairToString(start, inputNum - start));
    return [createNonBoldAnswerHeading(headingText), pairsOl];
}

new GoldbachConjectureSection(
    {
        btnIdStart: 'goldbachConjecture',
        infoHtml: goldbachConjectureInfoHtml,
        actionSentenceEnding: 'the pairs of prime numbers that sum to that number',
        minInput: 4,
        maxInput: 100_000,
        apiEndpoint: 'goldbachPrimePairStarts'
    },
    getGoldbachConjectureElements
);


const pythagoreanTriplesInfoHtml =
    `The Pythagorean Theorem says that for a right triangle, the sum of the squares of the lengths of the 2
    short sides equals the square of the long side (hypotenuse) length, or
    <var>a</var><sup>2</sup> + <var>b</var><sup>2</sup> = <var>c</var><sup>2</sup>. This theorem was named after
    the ancient Greek mathematician Pythagoras. A <i>Pythagorean triple</i> is a triple of whole numbers that
    <var>a</var>, <var>b</var>, and <var>c</var> can be. For example; 3, 4, and 5 is a Pythagorean triple since
    3<sup>2</sup> (9) + 4<sup>2</sup> (16) = 5<sup>2</sup> (25). 11, 60, and 61 is another one since
    11<sup>2</sup> (121) + 60<sup>2</sup> (3,600) = 61<sup>2</sup> (3,721).
    
    Once we know a Pythagorean triple, we can form another one by multiplying <var>a</var>, <var>b</var>, and
    <var>c</var> by the same whole number > 1, Because of this, there are an infinite amount of Pythagorean
    triples. A Pythagorean triple is considered to be <i>primitive</i> if the GCD of <var>a</var>, <var>b</var>,
    and <var>c</var> is 1. Therefore, a primitive triple can't be formed by taking another triple and multiplying
    <var>a</var>, <var>b</var>, and <var>c</var> by the same whole number. The triples mentioned above;
    3, 4, and 5, and 11, 60, and 61; are primitive. 6 (3 × 2), 8 (4 × 2), and 10 (5 × 2) is another triple.
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

new SingleInputSection(
    {
        btnIdStart: 'pythagoreanTriples',
        infoHtml: pythagoreanTriplesInfoHtml,
        actionSentenceEnding: 'the first 10 Pythagorean triples ≥ that number',
        minInput: 0,
        maxInput: 500,
        apiEndpoint: 'pythagoreanTriples'
    },
    getPythagoreanTriplesElements
);


const twoSquareTheoremInfoHtml =
    `The Two Square Theorem says that every prime number that's 1 above a multiple of 4 can be expressed as the
    sum of 2 square numbers. A <i>square number</i>, also known as a <i>perfect square</i>, is a number that can
    be formed by taking an integer and multiplying it by itself, or squaring it. The first 4 square numbers are
    0 (0<sup>2</sup>), 1 (1<sup>2</sup> or (-1)<sup>2</sup>), 4 (2<sup>2</sup> or (-2)<sup>2</sup>), and
    9 (3<sup>2</sup> or (-3)<sup>2</sup>). An example of a number that's prime and is 1 above a multiple of 4 is
    29 and it can be expressed as 2<sup>2</sup> (4) + 5<sup>2</sup> (25).`;

const twoSquareTheoremActionSentenceEnding =
    `the first number ≥ that number that's prime and is 1 above a multiple of 4, \
    as well as the whole numbers whose squares sum to that prime number`;

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

new SingleInputSection(
    {
        btnIdStart: 'twoSquareTheorem',
        infoHtml: twoSquareTheoremInfoHtml,
        actionSentenceEnding: twoSquareTheoremActionSentenceEnding,
        minInput: 0,
        maxInput: ONE_BILLION,
        apiEndpoint: 'twoSquareTheoremAnswer'
    },
    getTwoSquareTheoremInfoElements
);


const phiLetter = '𝚽';
/**
 * Phi is approximately this number.
 * 
 * The server must use BigDecimal division with MathContexts when calculating Fibonacci-like sequences, so one
 * of those MathContexts was also used to calculate this number, as well as 21 / 13 in the info HTML string below.
 */
const phiNumString = '1.618033988749895';

const fiboLikeSequencesInfoHtml = 
    `I consider a number sequence to be "Fibonacci-like" if it starts with 2 numbers and has every following
    number be the sum of the 2 previous numbers. The Fibonacci sequence does this and the first 8 numbers of it
    are 1, 1, 2, 3, 5, 8, 13, and 21. Fibonacci was a mathematician from the 1100s to 1200s from modern-day Italy.
    Another Fibonacci-like sequence is the Lucas sequence and the first 8 numbers of it are
    2, 1, 3, 4, 7, 11, 18, and 29. This sequence was named after 1800s French mathematician Francois Edouard Anatole Lucas.
    
    The <i>Golden Ratio</i> is an irrational number symbolized by the Greek letter ${phiLetter} (Phi).
    ${phiLetter} = (1 + <math><msqrt><mn>5</mn></msqrt></math>) / 2 ≈ ${phiNumString}. As we advance further
    and further into a Fibonacci-like sequence, the ratio between a number and the number before it gets closer
    and closer to ${phiLetter}. For example, recall that the first 8 numbers of the Fibonacci sequence are
    1, 1, 2, 3, 5, 8, 13, and 21. 2 / 1 = 2, 8 / 5 = 1.6, and 21 / 13 ≈ 1.615384615384615.`;

const fibonacciLikeSequencesActionSentenceEnding =
    'the first 20 numbers in the Fibonacci-like sequence that starts with those numbers, \
    as well as the ratios between some consecutive numbers in that sequence';

/**
 * @typedef {{ num1String: string, num2String: string, ratio: number, isRounded: boolean }} RatioData
 * 
 * @param {{ sequence: string[], ratioDataArray: RatioData[] }} infoObject
 * @param {string} inputString1
 * @param {string} inputString2
 * @returns {HTMLElement[]}
 */
function getFibonacciLikeSequencesInfoElements(infoObject, inputString1, inputString2) {
    const { sequence, ratioDataArray } = infoObject;

    const headingText =
        `The first ${sequence.length} numbers in the Fibonacci-like sequence that starts with \
        ${inputString1} and ${inputString2} are:`;

    const sequenceOl = arrayToOl(sequence, getNumberStringWithCommas);

    /**
     * @param {RatioData}
     * @returns {string}
     */
    function ratioDataToString({ num1String, num2String, ratio, isRounded }) {
        const num1StringWithCommas = getNumberStringWithCommas(num1String);
        const num2StringWithCommas = getNumberStringWithCommas(num2String);
        return `${num2StringWithCommas} / ${num1StringWithCommas} ${isRounded ? '≈' : '='} ${ratio}`;
    }

    const ratioDataOl = arrayToOl(ratioDataArray, ratioDataToString, false);
    ratioDataOl.append(createLi(`${phiLetter} ≈ ${phiNumString}`));

    return [createNonBoldAnswerHeading(headingText), sequenceOl, ratioDataOl];
}

new DoubleInputSection(
    {
        btnIdStart: 'fiboLikeSequences',
        infoHtml: fiboLikeSequencesInfoHtml,
        actionSentenceEnding: fiboLikeSequencesActionSentenceEnding,
        minInput: 1,
        maxInput: NINE_QUADRILLION,
        apiEndpoint: 'fibonacciLikeSequencesAnswer'
    },
    getFiboLikeSequencesInfoElements
);


const ancientMultInfoStart =
    'The ancient Egyptians had an interesting algorithm for multiplication of 2 whole numbers. My way of \
    explaining the algorithm goes like this:';

const ancientMultStepsArr = [
    'Let variable <var>a</var> represent one of the numbers and variable <var>b</var> represent the other one.',

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

const ancientMultResultSentence = 'This gives us the product of the 2 numbers.';

const ancientMultExampleParagraphs =
    `Let's find the product of 5 and 12. Let's first use 5 for the number represented by <var>a</var> in the
    algorithm above and 12 for <var>b</var>. The powers of 2 ≤ 5 are 1, 2, and 4. The products of 12 and these
    powers are 12, 24, and 48. The powers of 2 that sum to 5 are 1 and 4. The products of 12 and these powers
    are 12 and 48. 12 + 48 = (12 × 1) + (12 × 4) = 12 × (1 + 4) = 60.
    
    Now let's use 12 for <var>a</var> and 5 for <var>b</var>. The powers of 2 ≤ 12 are 1, 2, 4, and 8.
    The products of 5 and these powers are 5, 10, 20, and 40. The powers of 2 that sum to 12 are 4 and 8.
    The products of 5 and these powers are 20 and 40. 20 + 40 = (5 × 4) + (5 × 8) = 5 × (4 + 8) = 60.`;

const ancientMultInfoElements =
    [
        createP(ancientMultInfoStart),
        ancientMultStepsOl,
        createP(ancientMultResultSentence),
        ...createPsWithParagraphs(ancientMultExampleParagraphs)
    ];


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
    const table1Headings = [`Powers of 2 ≤ ${inputString1}`, correspondingMultiplesHeading];
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

new DoubleInputSection(
    {
        btnIdStart: 'ancientMult',
        infoHtml: ancientMultInfoHtmlElements,
        isSingleInputSection: false,
        actionSentenceEnding: 'ancient Egyptian multiplication info for those numbers',
        minInput: 2,
        maxInput: NINE_QUADRILLION,
        apiEndpoint: 'ancientMultiplicationAnswer'
    },
    getAncientMultInfoElements
);
