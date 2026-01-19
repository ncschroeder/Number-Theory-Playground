'use strict';

/**
 * @param {string} id 
 * @returns {HTMLElement | null}
 */
const getElementById = (id) => document.getElementById(id);

/**
 * @typedef {string | Node} Appendable
 * These 2 types can be passed as args to the append, prepend, and replaceChildren methods on the Element class.
 */

/**
 * This is similar to creating an HTML element in the Elm language. Elm has functions for creating different
 * types of elements and one of the params of each function is a list of child elements for the new element.
 * 
 * @param {string} elementType 
 * @param {...Appendable} objectsToAppend 
 * @returns {HTMLElement}
 */
function createElement(elementType, ...objectsToAppend) {
    const element = document.createElement(elementType);
    element.append(...objectsToAppend);
    return element;
}

/**
 * @param {string} [text]
 * @returns {HTMLHeadingElement}
 */
const createH3 = (text) => createElement('h3', text);

/**
 * @param {string} [text] 
 * @returns {HTMLHeadingElement}
 */
function createNonBoldAnswerH3(text) {
    const h3 = createH3(text);
    h3.className = 'non-bold-answer-h3';
    return h3;
}

/**
 * @param {string} text
 * @returns {HTMLHeadingElement}
 */
const createH4 = (text) => createElement('h4', text);

/**
 * @param {...Appendable} objectsToAppend
 * @returns {HTMLDivElement}
 */
const createDiv = (...objectsToAppend) => createElement('div', ...objectsToAppend);

/**
 * @param {string} text 
 * @returns {HTMLDivElement}
 */
function createNarrowTextDiv(text) {
    const div = createDiv(text);
    div.className = 'narrow-text-div';
    return div;
}

/**
 * @param {...Appendable} objectsToAppend
 * @returns {HTMLParagraphElement}
 */
const createP = (...objectsToAppend) => createElement('p', ...objectsToAppend);

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
 * @param {string} infoString
 * A string with paragraphs of info HTML, each of which is separated by a blank line.
 * @returns {HTMLParagraphElement[]}
 * Each p element in here contains a paragraph in infoString.
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
 * @param  {...HTMLLIElement} lis 
 * @returns {HTMLOListElement}
 */
const createOl = (...lis) => createElement('ol', ...lis);

/**
 * @param  {...Appendable} objectsToAppend 
 * @returns {HTMLLIElement}
 */
const createLi = (...objectsToAppend) => createElement('li', ...objectsToAppend);

/**
 * @param {string} text
 * @returns {HTMLElement}
 */
const createSuperscript = (text) => createElement('sup', text);

/**
 * @returns {HTMLElement}
 */
const createSuperscriptWith2 = () => createSuperscript('2');

/**
 * @param {...Appendable} objectsToAppend
 * @returns {HTMLSpanElement}
 */
const createSpan = (...objectsToAppend) => createElement('span', ...objectsToAppend);

/**
 * @param {number} num
 * @returns {HTMLSpanElement}
 */
const createNumAndSquareSpan = (num) =>
    createSpan(createNumStringWithCommas(num), createSuperscriptWith2(), ` (${createNumStringWithCommas(num * num)})`);

/**
 * @typedef {(obj: any) => Appendable | HTMLLIElement} ArrayElementToListItem
 */

/**
 * @param {any[]} arr
 * @param {ArrayElementToListItem} arrElementTransform 
 * @param {string} [olClassName]
 * @returns {HTMLOListElement}
 */
function arrToOl(arr, arrElementTransform, olClassName) {
    const ol = createOl();
    if (olClassName) {
        ol.className = olClassName;
    }

    for (const element of arr) {
        const transformObj = arrElementTransform(element);
        const li = transformObj instanceof HTMLLIElement ? transformObj : createLi(transformObj);
        ol.appendChild(li);
    }

    return ol;
}

const answerNormalOlClassName = 'answer-normal-ol';

/**
 * @param {any[]} arr 
 * @param {ArrayElementToListItem} arrElementTransform 
 * @returns {HTMLOListElement}
 */
const arrToAnswerNormalOl = (arr, arrElementTransform) =>
    arrToOl(arr, arrElementTransform, answerNormalOlClassName);

/**
 * @param {any[]} arr 
 * @param {ArrayElementToListItem} arrElementTransform 
 * @returns {HTMLOListElement}
 */
const arrToAnswerFlexOl = (arr, arrElementTransform) =>
    arrToOl(arr, arrElementTransform, 'answer-flex-ol');

/**
 * @param {string[]} colHeadings 
 * @param {any[]} rowsDataSourceArr 
 * @param {(obj: any) => (number | string)[]} rowsDataSourceTransform
 * Return type is an array of numbers and/or unformatted number strings.
 * @returns {HTMLTableElement}
 */
function createTable(colHeadings, rowsDataSourceArr, rowsDataSourceTransform) {
    const table = createElement('table');
    const createTr = () => createElement('tr');
    
    const tHead = createElement('thead');
    let tr = createTr();
    for (const heading of colHeadings) {
        tr.appendChild(createElement('th', heading));
    }
    tHead.appendChild(tr);
    table.append(tHead);
    
    const tBody = createElement('tbody');
    for (const element of rowsDataSourceArr) {
        tr = createTr();
        for (const numOrNumString of rowsDataSourceTransform(element)) {
            tr.appendChild(createElement('td', createNumStringWithCommas(numOrNumString)));
        }
        tBody.appendChild(tr);
    }
    table.appendChild(tBody);
    
    return table;
}

/**
 * @param {number} a 
 * @param {number} b 
 * @returns {boolean}
 */
const isDivisible = (a, b) => a % b === 0;

/**
 * @param {number} num 
 * @returns {boolean}
 */
const isEven = (num) => isDivisible(num, 2);

/**
 * @param {number} num 
 * @returns {boolean}
 */
const isOdd = (num) => !isEven(num);

const commaAdder = new Intl.NumberFormat();

/**
 * @param {number | string} value
 * A number or an unformatted number string.
 * @returns {string}
 */
const createNumStringWithCommas = (value) => commaAdder.format(value);

/**
 * @param {number} a
 * @param {number} b
 * @returns {string}
 */
const numPairToString = (a, b) => `${createNumStringWithCommas(a)} & ${createNumStringWithCommas(b)}`;

/**
 * @type {Section}
 */
let curSection;

/**
 * @param {HTMLInputElement} textField 
 * @returns {?number}
 */
function getNum(textField) {
    const modifiedValue = textField.value.trim().replaceAll(',', '');
    if (modifiedValue.length === 0) return null;
    const containsNonDigit = /\D/.test(modifiedValue);
    if (containsNonDigit) return null;
    const num = Number(modifiedValue);
    return Number.isSafeInteger(num) ? num : null;
}

/**
 * @returns {{ inputDiv: HTMLDivElement, textField: HTMLInputElement }}
 * The text field is a child of the input div.
 */
function createInputDiv() {
    const textField = createElement('input');
    textField.className = 'text-field';

    const randomizeBtn = createBtn('Randomize');
    randomizeBtn.className = 'randomize-btn';
    randomizeBtn.onclick = () => textField.value = curSection.getRandomInput();
    
    /**
     * @returns {boolean}
     */
    const textFieldIsBlank = () => !/\S/.test(textField.value);

    const plusBtn = createBtn('+');
    plusBtn.className = 'plus-btn';
    plusBtn.onclick = () => {
        const { minInput, maxInput, needsEvenInput } = curSection;
        
        if (textFieldIsBlank()) {
            textField.value = minInput;
            return;
        }
        
        let inputNum = getNum(textField);
        if (inputNum === null) return;

        inputNum =
            inputNum < minInput || inputNum >= maxInput
            ? minInput
            : inputNum + (needsEvenInput && isEven(inputNum) ? 2 : 1);
        textField.value = inputNum;
    };

    const minusBtn = createBtn('−');
    minusBtn.className = 'minus-btn';
    minusBtn.onclick = () => {
        const { minInput, maxInput, needsEvenInput } = curSection;
        
        if (textFieldIsBlank()) {
            textField.value = maxInput;
            return;
        }

        let inputNum = getNum(textField);
        if (inputNum === null) return;
        inputNum =
            inputNum <= minInput || inputNum > maxInput
            ? maxInput
            : inputNum - (needsEvenInput && isEven(inputNum) ? 2 : 1);
        textField.value = inputNum;
    };

    const inputDiv = createDiv(textField, randomizeBtn, plusBtn, minusBtn);
    inputDiv.className = 'input-div';

    return { inputDiv, textField };
}

const sectionHeading = getElementById('section-heading');
const homeHeadingText = sectionHeading.textContent;
const homeContentDiv = getElementById('home-content-div');

const sectionInfoDetailsSummary = createElement('summary', 'Info');
const sectionInfoDiv = createDiv();
sectionInfoDiv.id = 'section-info-div';
sectionInfoDiv.className = 'non-answer-info-div';
/**
 * @type {HTMLDetailsElement}
 */
const sectionInfoDetails = createElement('details', sectionInfoDetailsSummary, sectionInfoDiv);

const { inputDiv: inputDiv1, textField: textField1 } = createInputDiv();
const { inputDiv: inputDiv2, textField: textField2 } = createInputDiv();
const inputDivDiv = createDiv(inputDiv1, inputDiv2);
inputDivDiv.id = 'input-div-div';

const sectionDirectionsDiv = createNarrowTextDiv();
const calculateBtn = createBtn('Calculate');
calculateBtn.id = 'calculate-btn';
const answerDiv = createDiv();
answerDiv.id = 'answer-div';

const sectionContentDiv =
    createDiv(sectionInfoDetails, sectionDirectionsDiv, inputDivDiv, calculateBtn, answerDiv);
sectionContentDiv.id = 'section-content-div';

getElementById('home-btn').onclick = () => {
    if (document.body.lastElementChild === sectionContentDiv) {
        sectionHeading.textContent = homeHeadingText;
        document.body.replaceChild(homeContentDiv, sectionContentDiv);
    }
};


// Max input constants
const tenThousand = 10_000;
const oneQuadrillion = 1_000_000_000_000_000;


class Section {
    /*
    For this class and its subclasses, use private fields with
    public getters to allow for read-only access outside the class.
     */

    #minInput;
    #maxInput;
    #apiEndpointEnd;

    /** 
     * @typedef {Object} SectionParams
     * @property {string} btnIdStart
     * The ID of the button for this section in section-btns-div.
     * @property {string | HTMLElement[]} infoHtmlStringOrArr
     * If this is a string, it'll contain paragraphs of info with each one separated by a blank line.
     * @property {string} actionSentenceEnding
     * @property {number} minInput
     * @property {number} maxInput
     * @property {string} apiEndpointEnd
     * The end of the endpoint to make an HTTP request to for the server to do the calculation(s) for this
     * section. The full endpoint is the website URL followed by "/calculate/" followed by this endpoint end.
     */

    /**
     * @param {SectionParams} params 
     */
    constructor(params) {
        const { 
            btnIdStart, infoHtmlStringOrArr, actionSentenceEnding, minInput, maxInput, apiEndpointEnd
        } = params;
        
        this.#minInput = minInput;
        this.#maxInput = maxInput;
        this.#apiEndpointEnd = apiEndpointEnd;
        
        const sectionBtn = getElementById(btnIdStart + '-btn');
        const heading = sectionBtn.textContent;

        /**
         * @type {HTMLElement[]}
         */
        const infoHtmlElements =
            Array.isArray(infoHtmlStringOrArr)
            ? Array.from(infoHtmlStringOrArr)
            : createPsWithParagraphs(infoHtmlStringOrArr);

        const maxInputString = createNumStringWithCommas(maxInput);
        const maxInputSentencePart =
            maxInput === oneQuadrillion ? `1 quadrillion (${maxInputString})` : maxInputString;

        const directions =
            `Enter or generate ${this.isSingleInputSection ? 'a whole number' : '2 whole numbers'} and click
            the "Calculate" button to get ${actionSentenceEnding}.
            Have ${this.isSingleInputSection ? 'this number' : 'these numbers'} be ${
                this.needsEvenInput
                ? `even, ≥ ${minInput}, and ≤ ${maxInputSentencePart}`
                : `≥ ${minInput} and ≤ ${maxInputSentencePart}`
            }. Commas are optional.`;


        // Use arrow function so that "this" refers to the Section instance.
        const goToThisSection = () => {
            curSection = this;
            sectionHeading.textContent = heading;
            sectionInfoDetails.open = false;
            sectionInfoDiv.replaceChildren(...infoHtmlElements);
            sectionDirectionsDiv.textContent = directions;
            textField1.value = '';
            textField2.value = '';
            answerDiv.innerHTML = '';

            if (this.isSingleInputSection) {
                if (inputDivDiv.lastElementChild === inputDiv2) {
                    inputDivDiv.removeChild(inputDiv2);
                }
            } else if (inputDivDiv.lastElementChild === inputDiv1) {
                inputDivDiv.appendChild(inputDiv2);
            }

            if (document.body.lastElementChild === homeContentDiv) {
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

    get apiEndpointEnd() {
        return this.#apiEndpointEnd;
    }

    /**
     * @returns {boolean}
     */
    get isSingleInputSection() {
        return this instanceof SingleInputSection;
    }

    /**
     * @returns {boolean}
     */
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
     * 
     * @param {number} upperBound
     * This is an exclusive bound.
     */
    static #getRandomNum = (lowerBound, upperBound) =>
        Math.floor(Math.random() * (upperBound - lowerBound)) + lowerBound;

    /**
     * First, a random number of digits will be generated for a random number. If the max input is a power of
     * 10, then the max number of digits for the random number is the number of digits of the max input - 1.
     * Otherwise, the max number of random digits is the number of digits of the max input.
     * 
     * Then, a random number with the generated random number of digits will be generated and returned. The
     * random number will be ≥ the min input and ≤ the max input of this section.
     * 
     * @returns {number}
     */
    getRandomInput() {
        const numMinInputDigits = this.minInput === 0 ? 1 : Math.floor(Math.log10(this.minInput)) + 1;
        const log10MaxInput = Math.log10(this.maxInput);
        const log10MaxInputFloor = Math.floor(log10MaxInput);
        // If the above 2 numbers are equal, then the max input is a power of 10.
        const numMaxInputDigits = log10MaxInputFloor + 1;
        const numMaxRandomInputDigits =
            log10MaxInputFloor === log10MaxInput ? numMaxInputDigits - 1 : numMaxInputDigits;
        const numRandomInputDigits = Section.#getRandomNum(numMinInputDigits, numMaxRandomInputDigits + 1);
        const lowerBound =
            numRandomInputDigits === numMinInputDigits ? this.minInput : Math.pow(10, numRandomInputDigits - 1);
        const upperBound = Math.pow(10, numRandomInputDigits);
        return Section.#getRandomNum(lowerBound, upperBound);
    }
}

class SingleInputSection extends Section {
    #createAnswerElements;

    /**
     * @callback SingleInputAnswerElementsCreator
     * @param {any} responseObj
     * Object from the response of an HTTP request that does the calculation(s) for this section.
     * @param {string} inputString
     * Contains the input number with commas.
     * @param {number} inputNum
     * The createAnswerElements functions for the Divisibility and Goldbach Conjecture Sections use this number.
     * The other createAnswerElements functions omit this param.
     * @returns {HTMLElement[]}
     */

    /**
     * @param {SectionParams} sectionParams 
     * @param {SingleInputAnswerElementsCreator} createAnswerElements 
     */
    constructor(sectionParams, createAnswerElements) {
        super(sectionParams);
        this.#createAnswerElements = createAnswerElements;
    }

    get createAnswerElements() {
        return this.#createAnswerElements;
    }
}

class GoldbachConjectureSection extends SingleInputSection {
    /**
     * @param {SectionParams} sectionParams 
     * @param {SingleInputAnswerElementsCreator} createAnswerElements 
     */
    constructor(sectionParams, createAnswerElements) {
        super(sectionParams, createAnswerElements);
    }

    /**
     * @param {number} num 
     * @returns {boolean}
     * @override
     */
    isInvalidInput(num) {
        return super.isInvalidInput(num) || isOdd(num);
    }

    /**
     * @returns {number}
     * @override
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
    #createAnswerElements;

    /**
     * @param {SectionParams} sectionParams 
     * @param {(responseObj: any, input1String: string, input2String: string) => HTMLElement[]} createAnswerElements 
     * responseObj is the object from the response of an HTTP request that does the calculation(s) for this
     * section. The input strings contain the input numbers with commas.
     */
    constructor(sectionParams, createAnswerElements) {
        super(sectionParams);
        this.#createAnswerElements = createAnswerElements;
    }

    get createAnswerElements() {
        return this.#createAnswerElements;
    }
}


const invalidInputDiv = createNarrowTextDiv('Invalid input');
invalidInputDiv.id = 'invalid-input-div';

function showInvalidInputMessage() {
    answerDiv.replaceChildren(invalidInputDiv);
}

calculateBtn.onclick = () => {
    const input1Num = getNum(textField1);
    if (input1Num === null || curSection.isInvalidInput(input1Num)) {
        showInvalidInputMessage();
        return;
    }
    const input1String = createNumStringWithCommas(input1Num);
    const urlParams = new URLSearchParams();
    /**
     * @type {(responseObj: any) => HTMLElement[]}
     * responseObj is the object from a response from an HTTP request made below.
     */
    let createAnswerElements;

    if (curSection.isSingleInputSection) {
        urlParams.append('input', input1Num);
        createAnswerElements = (responseObj) =>
            curSection.createAnswerElements(responseObj, input1String, input1Num);
    } else {
        const input2Num = getNum(textField2);
        if (input2Num === null || curSection.isInvalidInput(input2Num)) {
            showInvalidInputMessage();
            return;
        }
        urlParams.append('input1', input1Num);
        urlParams.append('input2', input2Num);
        const input2String = createNumStringWithCommas(input2Num);
        createAnswerElements = (responseObj) =>
            curSection.createAnswerElements(responseObj, input1String, input2String);
    }
    
    const errorMessage = 'Error with request.';

    fetch(`calculate/${curSection.apiEndpointEnd}?${urlParams}`)
    .then(response => response.ok ? response.json().then(createAnswerElements) : [errorMessage])
    .then(elementsOrErrorMessageArr => answerDiv.replaceChildren(...elementsOrErrorMessageArr))
    .catch(reason => {
        answerDiv.replaceChildren(errorMessage);
        console.error(errorMessage, reason);
    });
};


const primesInfoHtml =
    `A <i>prime number</i>, or a <i>prime</i>, is a whole number > 1 that isn't divisible by any whole numbers
    other than 1 and itself. A <i>composite number</i> is a whole number > 1 that is divisible by a whole number
    other than 1 and itself. The first 10 primes are 2, 3, 5, 7, 11, 13, 17, 19, 23, and 29. There are an
    infinite amount of primes. The largest known one is 2<sup>136,279,841</sup> − 1. It has 41,024,320 digits!
    Primes are used in 7 of the 10 sections in the Number Theory Playground.
    
    With the exception of 2 and 3, all primes are either 1 above or 1 below a multiple of 6. To show why this
    is the case, let's have a variable <var>n</var> and let it represent a whole number ≥ 6 that's a multiple of
    6. We know that <var>n</var> is divisible by 2 and 3 so <var>n</var> + 2 and <var>n</var> + 4 are divisible
    by 2 and <var>n</var> + 3 is divisible by 3 but we don't have any guarantees about what <var>n</var> + 1 and
    <var>n</var> + 5 are divisible by. Therefore, that's where primes can be.
    
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
function createPrimesAnswerElements(primes, inputString) {
    const headingText = `The first ${primes.length} primes ≥ ${inputString} are:`;
    const primesOl = arrToAnswerFlexOl(primes, createNumStringWithCommas);
    return [createNonBoldAnswerH3(headingText), primesOl];
}

new SingleInputSection(
    {
        btnIdStart: 'prime-nums',
        infoHtmlStringOrArr: primesInfoHtml,
        actionSentenceEnding: 'the first 30 prime numbers ≥ that number',
        minInput: 0,
        maxInput: tenThousand,
        apiEndpointEnd: 'primes'
    },
    createPrimesAnswerElements
);


const semiprimesInfoHtml =
    ``;

/**
 * @typedef {{ semiprime: number, factor1: number, factor2: number }} SemiprimeData
 */

/**
 * @param {SemiprimeData[]} semiprimeDatas
 * @param {string} inputString
 */
function createSemiprimesAnswerElements(semiprimeDatas, inputString) {
    const headingText = `The first ${semiprimeDatas.length} ≥ ${inputString} are:`;
    
    /**
     * @param {SemiprimeData} sd
     * @returns {string}
     */
    function semiprimeDataToString(sd) {
        const [semiprimeString, factor1String, factor2String] =
            [sd.semiprime, sd.factor1, sd.factor2]
            .map(createNumStringWithCommas);
            
        return `${semiprimeString} (${factor1String} × ${factor2String})`;
    }
    
    const semiprimesOl = arrToAnswerFlexOl(semiprimeDatas, semiprimeDataToString);
    return [createNonBoldAnswerH3(headingText), semiprimesOl]
}

new SingleInputSection(
    {
        btnIdStart: 'semiprimes',
        infoHtmlStringOrArr: semiprimesInfoHtml,
        actionSentenceEnding: 'the first 20 semiprimes ≥ that number',
        minInput: 0,
        maxInput: tenThousand,
        apiEndpointEnd: 'semiprimes-answer'
    },
    createSemiprimesAnswerElements
);


const conjectureDefinitionHtml =
    `A <i>conjecture</i> is a statement that's believed to be true but hasn't been proven to be`;


const twinPrimePairsInfoHtml =
    `A <i>twin prime pair</i> is a pair of prime numbers that differ by 2. The first 5 twin prime pairs are
    3 & 5, 5 & 7, 11 & 13, 17 & 19, and 27 & 29. The largest known twin prime pair is
    (2,996,863,034,895 × 2<sup>1,290,000</sup>) ± 1. They have 388,342 digits! The <i>twin prime conjecture</i>
    says that there are an infinite amount of twin prime pairs. ${conjectureDefinitionHtml}.
    
    All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so this means that all twin
    prime pairs besides 3 and 5 consist of 1 number that's 1 below a multiple of 6 and another number that's 1
    above that same multiple of 6. 5 is the only number to be in 2 twin prime pairs, the first 2 mentioned above.`;

/**
 * @param {number[]} pairStarts
 * @param {string} inputString
 * @returns {HTMLElement[]}
 * An array with a heading and an ordered list that shows twin prime pairs.
 */
function createTwinPrimePairsAnswerElements(pairStarts, inputString) {
    const headingText = `The first ${pairStarts.length} twin prime pairs ≥ ${inputString} are:`;
    const pairsOl = arrToAnswerFlexOl(pairStarts, (start) => numPairToString(start, start + 2));
    return [createNonBoldAnswerH3(headingText), pairsOl];
}

new SingleInputSection(
    {
        btnIdStart: 'twin-prime-pairs',
        infoHtmlStringOrArr: twinPrimePairsInfoHtml,
        actionSentenceEnding: 'the first 20 twin prime pairs ≥ that number',
        minInput: 0,
        maxInput: tenThousand,
        apiEndpointEnd: 'twin-prime-pair-starts'
    },
    createTwinPrimePairsAnswerElements
);


const pfInfoHtml =
    `The Fundamental Theorem of Arithmetic says that every whole number > 1 can be expressed as the product of
    prime numbers in 1 way if you ignore the order of those prime numbers. The <i>prime factorization</i> (PF)
    of a whole number > 1 is an expression of the prime numbers whose product is that number. For example; the
    PF of 5 is just 5, the PF of 25 is 5<sup>2</sup>, and the PF of 4,725 is 3<sup>3</sup> × 5<sup>2</sup> × 7
    if the prime numbers are in ascending order. 4,725 could also be expressed as
    5<sup>2</sup> × 3<sup>3</sup> × 7 but that's the same expression as the previous one if you ignore the order
    of the prime numbers. The Number Theory Playground displays PFs with the prime numbers in ascending order.
    There are some interesting applications for PFs. See the info for the "Divisibility" or "GCD and LCM"
    sections for some applications.
    
    The input number with the highest amount of prime factors is 8,192 (2<sup>13</sup>). An input number with
    the highest amount of <em>unique</em> prime factors is 2,310. This number has a PF of 2 × 3 × 5 × 7 × 11.
    You could also multiply that number by 2, 3, or 4 and those numbers are ≤ the max input and have the same
    amount of unique prime factors.`;

/** 
 * @typedef {{ factor: number, power: number }} FactorAndPower
 * 
 * @typedef {Object} PrimeFactorization
 * @property {?FactorAndPower[]} fps
 * If this is null, then that means the corresponding number is prime and therefore the PF just consists of 1
 * factor with 1 as its power.
 * @property {number} correspondingNum
 */

/**
 * @param {FactorAndPower[]} fps
 * @returns {HTMLSpanElement}
 * A span that shows the prime factorization (PF) of a number. This PF consists of the factors and powers in
 * fps. Each factor and power group is separated by " × ." A span is returned so it can be placed in the ol for
 * GCD and LCM PF answers without any problems.
 */
function createPfSpan(fps) {
    const pfSpan = createSpan();
    
    for (let i = 0; i < fps.length; i++) {
        if (i !== 0) {
            pfSpan.append(' × ');
        }
        
        const { factor, power } = fps[i];
        const fpSpan = createSpan(createNumStringWithCommas(factor));
        if (power > 1) {
            fpSpan.appendChild(createSuperscript(power));
        }
        
        pfSpan.appendChild(fpSpan);
    }
    
    return pfSpan;
}

/**
 * @param {FactorAndPower[]} fps
 * @param {string} inputString
 * @returns {HTMLElement[]}
 */
function createPfAnswerElements(fps, inputString) {
    const headingText = `The PF of ${inputString} is:`;
    const pfSpan = createPfSpan(fps);
    pfSpan.id = 'pf-section-pf-span';
    return [createNonBoldAnswerH3(headingText), pfSpan];
}

const pfMinInput = 2;

new SingleInputSection(
    {
        btnIdStart: 'pf',
        infoHtmlStringOrArr: pfInfoHtml,
        actionSentenceEnding: 'the prime factorization of that number',
        minInput: pfMinInput,
        maxInput: tenThousand,
        apiEndpointEnd: 'prime-factorization'
    },
    createPfAnswerElements
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
    <var>n</var> has by looking at <var>n</var>'s PF, taking all the powers of the factors, adding 1 to each,
    and then multiplying all these together. For example, the PF of 36 is 2<sup>2</sup> × 3<sup>2</sup>. The
    powers are 2 and 2, so there are 3 × 3 = 9 factors. However, that count includes 1 and the number that the
    PF is for (36 in this case). If you want to exclude those, then subtract 2. That would give us 7 factors.
    You can find the factors of <var>n</var> by finding all the PFs within <var>n</var>'s PF, or the
    "sub-factorizations", as I like to call them. For 2<sup>2</sup> × 3<sup>2</sup>, the sub-factorizations are
    2, 3, 2<sup>2</sup> (4), 2 × 3 (6), 3<sup>2</sup> (9), 2<sup>2</sup> × 3 (12), and 2 × 3<sup>2</sup> (18).
    
    Whole numbers that are ≤ the max input of this section generally have a pretty small amount of factors, like
    < 20. An example of an input number with a high number of factors is 9,240. This number has a PF of
    2<sup>3</sup> × 3 × 5 × 7 × 11. It has 4 × 2<sup>4</sup> = 2<sup>6</sup> = 64 total factors!`;

const divisPfInfoDiv =
    createDiv(createH3('Prime Factorization'), ...createPsWithParagraphs(divisPfInfoHtml));
    
const divisRulesInfoHtml =
    `Some rules can be used to determine if a whole number is divisible by another whole number. I'll go over 1
    rule for each number in the range of 3 to 12, excluding 5 and 10, though there are rules for more numbers
    and many numbers have multiple rules. I'll go over an example of using these rules to find the factors of a
    number in the "Example" section below. Let's have a variable <var>n</var> and let it represent a whole
    number. If the last 2 digits of <var>n</var> is divisible by 4, then <var>n</var> is divisible by 4. If the
    last 3 digits of <var>n</var> is divisible by 8, then <var>n</var> is divisible by 8. If the sum of the
    digits of <var>n</var> is divisible by 3, then <var>n</var> is divisible by 3. If the sum of the digits of
    <var>n</var> is divisible by 9, then <var>n</var> is divisible by 9. If <var>n</var> is even and divisible
    by 3, then it's also divisible by 6. If <var>n</var> is divisible by both 3 and 4, then it's also divisible
    by 12.
    
    For 7, we split <var>n</var> into 3-digit blocks from right to left, though the leftmost block can contain
    1 or 2 digits. Coincidentally, these are the blocks separated by commas if we write <var>n</var> with
    commas. We do an alternating sum of the blocks from right to left. We start with 0, add the rightmost block,
    subtract the block to the left of that, add the block to the left of that, and so on for all the blocks. If
    this sum is divisible by 7, then <var>n</var> is divisible by 7.
    
    For 11, we do an alternating sum of the digits of <var>n</var> from left to right. We start with 0, add the
    1<sup>st</sup> digit, subtract the 2<sup>nd</sup> digit, add the 3<sup>rd</sup> digit, and so on for all
    the digits. If this sum is divisible by 11, then <var>n</var> is divisible by 11. These alternating sums
    might involve negative integers or 0, so that makes them some of the few calculations done by the Number
    Theory Playground that involve numbers other than natural numbers.`;

const divisRulesExampleHtml =
    `Let <var>n</var> be 5,544. Its PF is 2<sup>3</sup> × 3<sup>2</sup> × 7 × 11. We can tell from that PF that
    <var>n</var> is divisible by all the numbers that had rules mentioned about them above. Let's check if
    <var>n</var> is divisible by those numbers using those rules. The last 2 digits are 44, which is divisible
    by 4. The last 3 digits are 544, which is divisible by 8. The sum of the digits is 5 + 5 + 4 + 4 = 18, which
    is divisible by both 3 and 9. Since <var>n</var> is even and divisible by 3, it's also divisible by 6. Since
    <var>n</var> is divisible by both 3 and 4, it's also divisible by 12. The alternating sum of 3-digit blocks
    from right to left is 544 − 5 = 539, which is divisible by 7. The alternating sum of digits from left to
    right is 8 − 7 + 1 − 2 = 0, which is divisible by 11.`;
    
const divisRulesExampleP = createPWithInnerHtml(divisRulesExampleHtml);
divisRulesExampleP.id = 'divis-rules-example-p';

const divisRulesInfoDiv =
    createDiv(
        createH3('Divisibility Rules'),
        ...createPsWithParagraphs(divisRulesInfoHtml),
        createDiv(createH4('Example'), divisRulesExampleP)
    );

const divisInfoElements =
    [createPWithInnerHtml(divisInfoStartHtml), divisPfInfoDiv, divisRulesInfoDiv];

/**
 * @typedef {{ sum: number, expression: string }} AlternatingSumAndExpression
 * 
 * @typedef {Object} DivisibilityRulesData
 * For the input number 5,544, blocksAltSumAndExpression would have an expression of "544 - 5" and a sum of 539.
 * digitsAltSumAndExpression would have an expression of "5 - 5 + 4 - 5" and a sum of 0.
 * @property {number} last2Digits
 * @property {number} last3Digits
 * @property {number} sumOfDigits
 * @property {?AlternatingSumAndExpression} blocksAltSumAndExpression
 * @property {AlternatingSumAndExpression} digitsAltSumAndExpression
 * 
 * @typedef {Object} DivisibilityPrimeFactorizationAnswer
 * For the input number with a PF of 5^2 × 7^3, numFactorsExpression would be "(2 + 1) × (3 + 1)" and numFactors
 * would be 12.
 * @property {FactorAndPower[]} inputFps
 * @property {string} numFactorsExpression
 * @property {number} numFactors
 * @property {PrimeFactorization[]} factorPfs
 */

/** 
 * @param {{ rulesData: DivisibilityRulesData, pfAnswer: ?DivisibilityPrimeFactorizationAnswer }}
 * If pfAnswer is null, then that means the input number is prime.
 * @param {string} inputString
 * @param {number} inputNum 
 * @returns {HTMLElement[]}
 */
const createDivisAnswerElements = ({ rulesData, pfAnswer }, inputString, inputNum) =>
    [
        createH3(`Divisibility Info for ${inputString}`),
        createDivisRulesAnswerDiv(rulesData, inputString, inputNum),
        createDivisPfAnswerDiv(pfAnswer, inputString)
    ];

/**
 * This function does the only non-trivial calculations that are done on the front end.
 * 
 * @param {DivisibilityRulesData} rulesData 
 * @param {string} inputString 
 * @param {number} inputNum 
 * @returns {HTMLDivElement}
 * A div with a heading and a paragraph element with info about factors of the input number that are found
 * using divisibility rules.
 */
function createDivisRulesAnswerDiv(rulesData, inputString, inputNum) {
    const heading = createH4('Rules Info');

    /**
     * For all the rules besides the ones for 6 and 12, we do a calculation with the input num and if the result of
     * that calculation is divisible by a certain number, then the input number is also divisible by that number.
     * 
     * @param {number} possibleFactor 
     * @param {number} numFromCalculation
     * Since the max input is 10,000, this number will be at most 3 digits so it'll never require a comma.
     * @param {boolean} isDivisible 
     */
    function getDivisSentence(possibleFactor, numFromCalculation, isDivisible) {
        const isOrIsnt = isDivisible ? 'is' : `isn't`;
        return `${numFromCalculation} ${isOrIsnt} divisible by ${possibleFactor} so ${inputString} \
            ${isOrIsnt} divisible by ${possibleFactor}.`;
    }

    const { last2Digits, last3Digits, sumOfDigits } = rulesData;
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
            `The last 2 digits form the number ${last2Digits}.`,
            getDivisSentence(4, last2Digits, isDivisibleBy4)
        );

        if (isDivisibleBy4) {
            if (inputNum >= 1_000) {
                const isDivisibleBy8 = isDivisible(last3Digits, 8);
                sentences.push(
                    `The last 3 digits form the number ${last3Digits}.`,
                    getDivisSentence(8, last3Digits, isDivisibleBy8)
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
        getDivisSentence(3, sumOfDigits, isDivisibleBy3)
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
    
    const { blocksAltSumAndExpression, digitsAltSumAndExpression } = rulesData;
    
    if (blocksAltSumAndExpression) {
        // Since the max input is 10,000, this sum will be at most 3 digits long so it'll never require a comma.
        const { sum, expression } = blocksAltSumAndExpression;
        const isDivisibleBy7 = isDivisible(sum, 7);
        sentences.push(
            `The alternating sum of 3-digit blocks from right to left is ${expression} = ${sum}.`,
            getDivisSentence(7, sum, isDivisibleBy7)
        );
    }

    const { sum, expression } = digitsAltSumAndExpression;
    const isDivisibleBy11 = isDivisible(sum, 11);
    sentences.push(
        `The alternating sum of digits from left to right is ${expression} = ${sum}.`,
        getDivisSentence(11, sum, isDivisibleBy11)
    );

    return createDiv(heading, createP(sentences.join(' ')));
}

/**
 * @param {?DivisibilityPrimeFactorizationAnswer} pfAnswer
 * If this is null, then that means the input number is prime.
 * @param {string} inputString 
 * @returns {HTMLDivElement}
 * A div with at least a heading and an inner div with text that shows the PF of the input number. If pfAnswer
 * is null, then the inner div will only contain a little more info. Otherwise, the inner div will contain more
 * info about the number of factors the input number has. Also, the returned div will also contain an ordered
 * list that shows the factors of the input number and the PFs of those factors.
 */
function createDivisPfAnswerDiv(pfAnswer, inputString) {
    const heading = createH4('Prime Factorization Info');
    const pfInfoTextDiv = createNarrowTextDiv(`The PF of ${inputString} is `);
    const pfDiv = createDiv(heading, pfInfoTextDiv);

    if (!pfAnswer) {
        pfInfoTextDiv.append(
            `${inputString}. ${inputString} is prime and doesn't have any whole number factors other than 1 and itself.`
        );
        return pfDiv;
    }

    const { inputFps, numFactorsExpression, numFactors, factorPfs } = pfAnswer;
    pfInfoTextDiv.append(createPfSpan(inputFps), '. ');

    const numFactorsInfoEnd =
        'there' + (
            numFactors === 3
            ? `'s 1 factor`
            : ` are ${createNumStringWithCommas(numFactors - 2)} factors`
        );

    const numFactorsInfo =
        `By looking at the powers, we can see that there are ${numFactorsExpression} = ${numFactors} factors. \
        If 1 and ${inputString} are excluded, then ${numFactorsInfoEnd}.`;

    pfInfoTextDiv.append(numFactorsInfo, ' The factors and their PFs are:');

    /**
     * @param {PrimeFactorization} 
     * @returns {HTMLLIElement}
     */
    function pfToLi({ fps, correspondingNum }) {
        const numStringWithCommas = createNumStringWithCommas(correspondingNum);
        return fps ? createLi(createPfSpan(fps), ` (${numStringWithCommas})`) : createLi(numStringWithCommas);
    }

    const factorsOl = arrToAnswerFlexOl(factorPfs, pfToLi);
    factorsOl.id = 'divis-answer-factors-ol';
    pfDiv.appendChild(factorsOl);
    return pfDiv;
}

new SingleInputSection(
    {
        btnIdStart: 'divis',
        infoHtmlStringOrArr: divisInfoElements,
        actionSentenceEnding: 'divisbility info for that number',
        minInput: 10,
        maxInput: tenThousand,
        apiEndpointEnd: 'divisibility-answer'
    },
    createDivisAnswerElements
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
 * @param {HTMLDivElement | HTMLHeadingElement} firstChild
 * For examples in the GCD and LCM section info, this is a div and for answers, this is an h4.
 * @param {EuclideanIteration[]} iterations 
 * @returns {HTMLDivElement}
 * A div with firstChild; a table that shows the max, min, and remainder of each iteration of the Euclidean
 * algorithm performed on 2 input numbers; and a div with a message about the GCD of the input numbers.
 */
function createEuclideanTableDiv(firstChild, iterations) {
    const tableColHeadings = ['Max', 'Min', 'Remainder'];
    /**
     * @param {EuclideanIteration} ei 
     * @returns {number[]}
     */
    const getTableRowNums = (ei) => [ei.max, ei.min, ei.remainder];
    const table = createTable(tableColHeadings, iterations, getTableRowNums);
    const gcd = iterations[iterations.length - 1].min;
    const gcdMessageDiv = createDiv(`The GCD is ${createNumStringWithCommas(gcd)}.`);
    gcdMessageDiv.className = 'gcd-message-div';
    return createDiv(firstChild, table, gcdMessageDiv);
}

/**
 * @param {EuclideanIteration[]} iterations 
 * @returns {HTMLDivElement}
 */
function createEuclideanExampleDiv(iterations) {
    const startText =
        `Let's find the GCD of ${iterations[0].min} and ${iterations[0].max} using the Euclidean algorithm. \
        Here are the iterations:`;
    const tableDiv = createEuclideanTableDiv(createNarrowTextDiv(startText), iterations);
    tableDiv.querySelector('table').className = 'euclidean-example-table';
    return tableDiv;
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

const gcdAndLcmOtherInfo =
    `2 whole numbers are said to be <i>coprime</i> if their GCD is 1. Therefore, coprime numbers don't have any
    common factors in their PFs. The input numbers whose LCM is the highest are 10,000, the max input, and 9,999.
    Their LCM is 99,990,000. A pair of input numbers whose LCM has the highest amount of prime factors is
    8,192 (2<sup>13</sup>) and 6,561 (3<sup>8</sup>). Their LCM is 53,747,712. A pair of input numbers whose LCM
    might have the highest amount of <em>unique</em> prime factors is 2,310, the product of the first 5 prime
    numbers; and 4,199, the product of the next 3 prime numbers. Their LCM is 9,699,690 and its PF is
    2 × 3 × 5 × 7 × 11 × 13 × 17 × 19.`

const gcdAndLcmOtherInfoDiv =
    createDiv(createH3('Other Info'), createPWithInnerHtml(gcdAndLcmOtherInfo));

const gcdAndLcmInfoElements =
    [createP(gcdAndLcmInfoStart), euclideanInfoDiv, gcdAndLcmPfInfoDiv, gcdAndLcmOtherInfoDiv];

/**
 * @typedef GcdAndLcmPrimeFactorizationAnswer
 * @type {Object}
 * @property {FactorAndPower[]} input1Fps
 * @property {FactorAndPower[]} input2Fps
 * @property {?PrimeFactorization} gcdPf
 * If this is null, then that means the GCD is 1.
 * @property {PrimeFactorization} lcmPf
 */

/**
 * @param {{ euclideanIterations: EuclideanIteration[], pfAnswer: GcdAndLcmPrimeFactorizationAnswer }}
 * @param {string} input1String
 * @param {string} input2String
 * @returns {HTMLElement[]}
 */
function createGcdAndLcmAnswerElements({ euclideanIterations, pfAnswer }, input1String, input2String) {
    const mainHeading = createH3(`GCD and LCM Info for ${input1String} and ${input2String}`);
    const euclideanDiv =
        createEuclideanTableDiv(
            createH4('Euclidean Algorithm Iterations'),
            euclideanIterations
        );
    euclideanDiv.querySelector('table').id = 'euclidean-answer-table';
    const pfDiv = createGcdAndLcmPfAnswerDiv(pfAnswer, input1String, input2String);
    return [mainHeading, euclideanDiv, pfDiv];
}

/**
 * @param {GcdAndLcmPrimeFactorizationAnswer} answer 
 * @param {string} input1String 
 * @param {string} input2String 
 * @returns {HTMLDivElement}
 * A div with a heading and an ordered list that shows the PFs of the input numbers, the PF of the GCD if it's 
 * not 1, and the PF of the LCM.
 */
function createGcdAndLcmPfAnswerDiv(answer, input1String, input2String) {
    const heading = createH4('Prime Factorizations Info');

    /**
     * @param {string} inputString 
     * @param {FactorAndPower[]} fps
     * @returns {HTMLLIElement}
     */
    const createInputPfLi = (inputString, fps) =>
        createLi(`The PF of ${inputString} is `, createPfSpan(fps), '.');

    /**
     * @param {string} gcdOrLcmText 
     * @param {PrimeFactorization}
     * @returns {HTMLLIElement}
     */
    function createGcdOrLcmPfLi(gcdOrLcmText, { fps, correspondingNum }) {
        const li = createLi(`The PF of the ${gcdOrLcmText} is `);
        if (fps) {
            li.append(createPfSpan(fps), ', which is ');
        }
        li.append(createNumStringWithCommas(correspondingNum), '.');
        return li;
    }

    const { input1Fps, input2Fps, gcdPf, lcmPf } = answer;

    /**
     * @type {Appendable}
     */
    const gcdInfoAppendable =
        gcdPf
        ? createGcdOrLcmPfLi('GCD', gcdPf)
        : 'There are no common prime factors so the GCD is 1.';

    const pfsOl =
        createOl(
            createInputPfLi(input1String, input1Fps),
            createInputPfLi(input2String, input2Fps),
            gcdInfoAppendable,
            createGcdOrLcmPfLi('LCM', lcmPf)
        );
    pfsOl.id = 'gcd-and-lcm-pf-answer-ol';
    pfsOl.className = answerNormalOlClassName;

    return createDiv(heading, pfsOl);
}

new DoubleInputSection(
    {
        btnIdStart: 'gcd-and-lcm',
        infoHtmlStringOrArr: gcdAndLcmInfoElements,
        actionSentenceEnding: 'GCD and LCM info for those numbers',
        minInput: pfMinInput,
        maxInput: tenThousand,
        apiEndpointEnd: 'gcd-and-lcm-answer'
    },
    createGcdAndLcmAnswerElements
);


const goldbachConjectureInfoHtml =
    `The <i>Goldbach conjecture</i> says that every even number ≥ 4 can be expressed as the sum of 2 prime
    numbers. This was named after 1700s Prussian mathematician Christian Goldbach. ${conjectureDefinitionHtml}.
    The Goldbach conjecture has been verified to be true for all even numbers ≥ 4 and ≤ 4 × 10<sup>18</sup>.`;

/**
 * @param {number[]} primePairStarts
 * @param {string} inputString
 * @param {number} inputNum 
 * @returns {HTMLElement[]}
 * An array with a heading and an ordered list that shows the pairs of prime numbers that sum to the input num.
 */
function createGoldbachConjectureAnswerElements(primePairStarts, inputString, inputNum) {
    const thereIs1Pair = primePairStarts.length === 1;
    const headingText =
        `There${thereIs1Pair ? `'s 1 pair` : ` are ${createNumStringWithCommas(primePairStarts.length)} pairs`} \
        of prime numbers that sum to ${inputString}. ${thereIs1Pair ? 'It is' :  'They are'}:`;
    const pairsOl = arrToAnswerFlexOl(primePairStarts, (start) => numPairToString(start, inputNum - start));
    return [createNonBoldAnswerH3(headingText), pairsOl];
}

new GoldbachConjectureSection(
    {
        btnIdStart: 'goldbach-conjecture',
        infoHtmlStringOrArr: goldbachConjectureInfoHtml,
        actionSentenceEnding: 'the pairs of prime numbers that sum to that number',
        minInput: 4,
        maxInput: 1_000,
        apiEndpointEnd: 'goldbach-prime-pair-starts'
    },
    createGoldbachConjectureAnswerElements
);


const pythagTriplesInfoHtml =
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
 * @typedef {{ a: number, b: number, c: number, isPrimitive: boolean }} PythagoreanTriple
 */

/**
 * @param {PythagoreanTriple[]} triples
 * @param {string} inputString
 * @returns {HTMLElement[]}
 * An array with a heading and an ordered list. There's an item in this list for each triple. Each item shows
 * the numbers and squares of the triple. If the triple is primitive, then that'll be mentioned.
 */
function createPythagTriplesAnswerElements(triples, inputString) {
    const headingText = `The first ${triples.length} Pythagorean triples ≥ ${inputString} are:`;

    /**
     * @param {PythagoreanTriple}
     * @returns {HTMLLIElement}
     */
    function tripleToLi({ a, b, c, isPrimitive }) {
        const maybePrimitiveString = isPrimitive ? ' (primitive)' : '';
        return createLi(createNumAndSquareSpan(a), ' + ', createNumAndSquareSpan(b), ' = ', createNumAndSquareSpan(c), maybePrimitiveString);
    }

    const triplesOl = arrToAnswerNormalOl(triples, tripleToLi);
    triplesOl.id = 'pythag-triples-ol';

    return [createNonBoldAnswerH3(headingText), triplesOl];
}

new SingleInputSection(
    {
        btnIdStart: 'pythag-triples',
        infoHtmlStringOrArr: pythagTriplesInfoHtml,
        actionSentenceEnding: 'the first 10 Pythagorean triples ≥ that number',
        minInput: 0,
        maxInput: 100,
        apiEndpointEnd: 'pythagorean-triples'
    },
    createPythagTriplesAnswerElements
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
 * @param {{ primeNum: number, a: number, b: number }}
 * @param {string} inputString
 * @returns {HTMLElement[]}
 */
function createTwoSquareTheoremAnswerElements({ primeNum, a, b }, inputString) {
    const headingText =
        `The first number ≥ ${inputString} that's prime and is 1 above a multiple of 4 is:`;
    const answerDiv =
        createDiv(createNumStringWithCommas(primeNum), ', which is ', createNumAndSquareSpan(a), ' + ', createNumAndSquareSpan(b));
    answerDiv.id = 'two-square-theorem-answer-div';
    return [createNonBoldAnswerH3(headingText), answerDiv];
}

new SingleInputSection(
    {
        btnIdStart: 'two-square-theorem',
        infoHtmlStringOrArr: twoSquareTheoremInfoHtml,
        actionSentenceEnding: twoSquareTheoremActionSentenceEnding,
        minInput: 0,
        maxInput: tenThousand,
        apiEndpointEnd: 'two-square-theorem-answer'
    },
    createTwoSquareTheoremAnswerElements
);


// There are many Unicode chars for Phi. I'll pick this one and use this constant for it.
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
    1, 1, 2, 3, 5, 8, 13, and 21. 2 / 1 = 2, 8 / 5 = 1.6, and 21 / 13 ≈ 1.615384615384615.
    
    The ratios that get calculated by this section are floating-point numbers most of the time, so these
    calculations are some of the few calculations done by the Number Theory Playground that involve numbers other
    than natural numbers.`;

const fiboLikeSequencesActionSentenceEnding =
    'the first 20 numbers in the Fibonacci-like sequence that starts with those numbers, \
    as well as the ratios between some consecutive numbers in that sequence';

/**
 * @typedef {{ num1String: string, num2String: string, ratio: number, isRounded: boolean }} RatioData
 */

/**
 * @param {{ fiboLikeSequence: string[], ratioDataArr: RatioData[] }}
 * @param {string} input1String
 * @param {string} input2String
 * @returns {HTMLElement[]}
 * An array with a div that shows the Fibonacci-like sequence and another div that shows ratios between some
 * consecutive numbers in the sequence.
 */
function createFiboLikeSequencesAnswerElements({ fiboLikeSequence, ratioDataArr }, input1String, input2String) {
    const sequenceHeadingText =
        `The first ${fiboLikeSequence.length} numbers in the Fibonacci-like sequence that starts with \
        ${input1String} and ${input2String} are:`;
    const sequenceOl = arrToAnswerFlexOl(fiboLikeSequence, createNumStringWithCommas);
    const sequenceDiv = createDiv(createNonBoldAnswerH3(sequenceHeadingText), sequenceOl);

    const ratiosHeading = createNonBoldAnswerH3();
    ratiosHeading.innerHTML =
        'The ratios between the 5<sup>th</sup> and 4<sup>th</sup>, 10<sup>th</sup> and 9<sup>th</sup>, \
        15<sup>th</sup> and 14<sup>th</sup>, and 20<sup>th</sup> and 19<sup>th</sup> numbers are:';

    /**
     * @param {RatioData}
     * @returns {string}
     */
    function ratioDataToString({ num1String, num2String, ratio, isRounded }) {
        const num1StringWithCommas = createNumStringWithCommas(num1String);
        const num2StringWithCommas = createNumStringWithCommas(num2String);
        return `${num2StringWithCommas} / ${num1StringWithCommas} ${isRounded ? '≈' : '='} ${ratio}`;
    }

    const ratiosOl = arrToAnswerNormalOl(ratioDataArr, ratioDataToString);
    ratiosOl.append(createLi(`${phiLetter} ≈ ${phiNumString}`));
    const ratiosDiv = createDiv(ratiosHeading, ratiosOl);
    ratiosDiv.id = 'fibo-like-sequence-ratios-div';

    return [sequenceDiv, ratiosDiv];
}

new DoubleInputSection(
    {
        btnIdStart: 'fibo-like-sequences',
        infoHtmlStringOrArr: fiboLikeSequencesInfoHtml,
        actionSentenceEnding: fiboLikeSequencesActionSentenceEnding,
        minInput: 1,
        maxInput: oneQuadrillion,
        apiEndpointEnd: 'fibonacci-like-sequences-answer'
    },
    createFiboLikeSequencesAnswerElements
);


const ancientMultAlgorithmInfoStart =
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

const ancientMultStepsOl = arrToOl(ancientMultStepsArr, createLiWithInnerHtml);

const ancientMultResultSentence = 'This gives us the product of the 2 numbers.';

const ancientMultAlgorithmInfoDiv =
    createDiv(
        createP(ancientMultAlgorithmInfoStart),
        ancientMultStepsOl,
        createNarrowTextDiv(ancientMultResultSentence)
    );
ancientMultAlgorithmInfoDiv.className = 'div-with-p-and-ol';

const ancientMultExampleParagraphs =
    `Let's find the product of 5 and 12. Let's first use 5 for the number represented by <var>a</var> in the
    algorithm above and 12 for <var>b</var>. The powers of 2 ≤ 5 are 1, 2, and 4. The products of 12 and these
    powers are 12, 24, and 48. The powers of 2 that sum to 5 are 1 and 4. The products of 12 and these powers
    are 12 and 48. 12 + 48 = (12 × 1) + (12 × 4) = 12 × (1 + 4) = 60.
    
    Now let's use 12 for <var>a</var> and 5 for <var>b</var>. The powers of 2 ≤ 12 are 1, 2, 4, and 8.
    The products of 5 and these powers are 5, 10, 20, and 40. The powers of 2 that sum to 12 are 4 and 8.
    The products of 5 and these powers are 20 and 40. 20 + 40 = (5 × 4) + (5 × 8) = 5 × (4 + 8) = 60.`;

const ancientMultInfoElements =
    [ancientMultAlgorithmInfoDiv, ...createPsWithParagraphs(ancientMultExampleParagraphs)];

/**
 * @typedef {{ powerOf2String: string, correspondingMultipleString: string }} AncientMultiplicationTableRow
 */

/**
 * @param {{ table1Rows: AncientMultiplicationTableRow[], table2Rows: AncientMultiplicationTableRow[], productString: string }}
 * @param {string} input1String
 * @param {string} input2String
 * @returns {HTMLElement[]}
 * An array with a heading, a div with tables made using the data from the arrays in the param object, and a div
 * with text about the product of the 2 input numbers.
 */
function createAncientMultAnswerElements({ table1Rows, table2Rows, productString }, input1String, input2String) {
    const mainHeadingText = `Ancient Egyptian Multiplication Info for ${input1String} and ${input2String}`;
    
    const correspondingMultiplesColHeading = `Corresponding Multiples of ${input2String}`;
    const table1ColHeadings = [`Powers of 2 ≤ ${input1String}`, correspondingMultiplesColHeading];
    const table2ColHeadings = [`Powers of 2 That Sum to ${input1String}`, correspondingMultiplesColHeading];
    /**
     * @param {AncientMultiplicationTableRow} row 
     * @returns {string[]}
     */
    const getTableRowStrings = (row) => [row.powerOf2String, row.correspondingMultipleString];
    const tableDiv =
        createDiv(
            createTable(table1ColHeadings, table1Rows, getTableRowStrings),
            createTable(table2ColHeadings, table2Rows, getTableRowStrings)
        );
    tableDiv.id = 'ancient-mult-table-div';
    
    const productSentenceDiv = createNarrowTextDiv();
    productSentenceDiv.innerHTML =
        `The sum of the right column of the 2<sup>nd</sup> table is ${createNumStringWithCommas(productString)}, \
        which is the product of ${input1String} and ${input2String}.`;

    return [createH3(mainHeadingText), tableDiv, productSentenceDiv];
}

new DoubleInputSection(
    {
        btnIdStart: 'ancient-mult',
        infoHtmlStringOrArr: ancientMultInfoElements,
        actionSentenceEnding: 'ancient Egyptian multiplication info for those numbers',
        minInput: 2,
        maxInput: oneQuadrillion,
        apiEndpointEnd: 'ancient-multiplication-answer'
    },
    createAncientMultAnswerElements
);

