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
        const li = createElement('li');
        li.append(transform(element));
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
 * @param {number} n 
 * @returns {boolean}
 */
const isEven = (n) => n % 2 === 0;

/**
 * @param {HTMLInputElement} inputField 
 * @returns {number}
 */
function getNum(inputField) {
    const trimmedValue = inputField.value.trim().replaceAll(',', '');
    if (trimmedValue.length === 0) return NaN;
    const containsNonDigit = /\D/.test(trimmedValue);
    return containsNonDigit ? NaN : Number(trimmedValue);
}

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
     * @typedef {Object} SectionParams
     * @property {string} btnIdStart
     * @property {string | HTMLElement[]} infoHtml
     * @property {boolean} [isSingleInputSection=true]
     * @property {string} actionSentenceEnding
     * @property {number} minInput
     * @property {number} maxInput
     * @property {boolean} [needsEvenInput=false]
     * @property {string} apiEndpoint
     * @property {(responseObj: any, input1String: string, input2String: ?string) => HTMLElement[]} getElements
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
            sectionInfo.replaceChildren(infoHtmlElements);
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
        ? curSection.getElements(await response.json(), inputString1, inputString2)
        : [errorMessage]
    )
    .then(elementsOrErrorMessage => answerArea.replaceChildren(...elementsOrErrorMessage))
    .catch(reason => {
        answerArea.replaceChildren(errorMessage);
        console.error(errorMessage, reason);
    });
}


const primesInfoHtml = ``;

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


const twinPrimePairsInfoHtml = ``;

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


const pfInfoHtml = ``;

/**
 * @typedef FactorAndPower
 * @type {object}
 * @property {number} factor
 * @property {number} power
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


const divisbilityInfoHtml = ``;

/**
 * @typedef {{factorsAndPowers: ?FactorAndPower[], number: number}} FactorsAndPowersWithNumber
 * 
 * @param {{rulesInfo: string, pfInfo: ?{inputPf: FactorAndPower[], numFactorsInfo: string, factorPfs: FactorsAndPowersWithNumber[]}}} infoObject
 * @param {string} inputString
 * @returns {HTMLElement[]}
 * An array that contains elements with divisibility info about the argument number based on the info in infoObject.
 */
function getDivisibilityInfoElements(infoObject, inputString) {
    const { rulesInfo, pfInfo } = infoObject;
    const rulesInfoDiv = createDiv();
    rulesInfoDiv.append(createH4('Rules Info'), createP(rulesInfo));

    const pfDiv = createDiv();
    pfDiv.appendChild(createH4('Prime Factorization Info'));
    const pfInfoParagraph = createP(`The prime factorization for ${inputString} is `);
    
    if (!pfInfo) {
        pfInfoParagraph.append(
            `${inputString}. ${inputString} is prime and doesn't have any factors other than itself and 1.`
        );
        pfDiv.appendChild(pfInfoParagraph);
    } else {
        const { inputPf, numFactorsInfo, factorPfs } = pfInfo;
        const subfactorizationsSentence =
            'By looking at all the "sub-factorizations", we can see the factors are:';
        pfInfoParagraph.append(getPfSpan(inputPf), '. ', numFactorsInfo, ' ', subfactorizationsSentence);
        pfDiv.appendChild(pfInfoParagraph);

        /**
         * @param {FactorsAndPowersWithNumber} pf 
         * @returns {HTMLSpanElement}
         */
        function pfToSpan(pf) {
            const { factorsAndPowers, number } = pf;
            const numString = getNumberStringWithCommas(number);
            const span = createSpan();
            if (factorsAndPowers) {
                span.append(getPfSpan(factorsAndPowers), ` (${numString})`);
            } else {
                span.append(numString);
            }
            return span;
        }

        pfDiv.appendChild(arrayToOl(factorPfs, pfToSpan));
    }

    const mainHeading = createH3(`Divisibility Info for ${inputString}`);
    return [mainHeading, rulesInfoDiv, pfDiv];
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


const gcdAndLcmInfoHtml = ``;

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


const goldbachConjectureInfoHtml = ``;

/**
 * @param {number[][]} primePairs
 * @param {string} inputString
 * @returns {HTMLElement[]}
 */
function getGoldbachConjectureElements(primePairs, inputString) {
    const heading = createH3();
    heading.textContent =
        `There${primePairs.length === 1 ? '\'s 1 pair' : ` are ${getNumberStringWithCommas(primePairs.length)} pairs`} \
        of prime numbers that sum to ${inputString}. They are:`;
    const pairsOl = arrayToOl(primePairs, nums => numPairToString(nums[0], nums[1]));
    return [heading, pairsOl];
}

new Section({
    btnIdStart: 'goldbachConjecture',
    infoHtml: goldbachConjectureInfoHtml,
    actionSentenceEnding: 'pairs of prime numbers that sum to that integer',
    minInput: 4,
    maxInput: 10,
    needsEvenInput: true,
    apiEndpoint: 'goldbachPrimePairs',
    getElements: getGoldbachConjectureElements
});


const pythagoreanTriplesInfoHtml = '';

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


const twoSquareTheoremInfoHtml = '';

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


const phiNum = (1 + Math.sqrt(5)) / 2;
const capitalPhi = '\u03A6';

const fibonacciLikeSequencesInfoHtml = '';

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


const ancientMultiplicationInfoHtmlElements = [];

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
