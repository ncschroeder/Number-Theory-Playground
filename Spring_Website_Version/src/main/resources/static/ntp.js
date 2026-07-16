'use strict';

/**
 * @param {string} id
 * @returns {?Element}
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
 * @param {string} elementName
 * @param {...Appendable} objectsToAppend
 * @returns {HTMLElement}
 */
function createHtmlElement(elementName, ...objectsToAppend) {
    const element = document.createElement(elementName);
    element.append(...objectsToAppend);
    return element;
}

/**
 * @param {string} text
 * @returns {HTMLHeadingElement}
 */
const createH3 = (text) => createHtmlElement('h3', text);

/**
 * @param {string} text
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
const createH4 = (text) => createHtmlElement('h4', text);

/**
 * @param {...Appendable} objectsToAppend
 * @returns {HTMLDivElement}
 */
const createDiv = (...objectsToAppend) => createHtmlElement('div', ...objectsToAppend);

/**
 * @param {...Appendable} objectsToAppend
 * @returns {HTMLDivElement}
 */
function createNarrowTextDiv(...objectsToAppend) {
    const div = createDiv(...objectsToAppend);
    div.className = 'narrow-text-div';
    return div;
}

/**
 * @param {...Appendable} objectsToAppend
 * @returns {HTMLParagraphElement}
 */
const createP = (...objectsToAppend) => createHtmlElement('p', ...objectsToAppend);

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
 * 
 * @returns {HTMLParagraphElement[]}
 * Each p element in here contains a paragraph in infoString.
 */
const createPsWithParagraphs = (infoString) => infoString.split(/\n\s*\n/).map(createPWithInnerHtml);

/**
 * @param {string} text
 * @returns {HTMLButtonElement}
 */
function createBtn(text) {
    /** @type {HTMLButtonElement} */
    const btn = createHtmlElement('button', text);
    btn.type = 'button';
    return btn;
}

/**
 * @param  {...HTMLLIElement} lis
 * @returns {HTMLOListElement}
 */
const createOl = (...lis) => createHtmlElement('ol', ...lis);

/**
 * @param  {...Appendable} objectsToAppend
 * @returns {HTMLLIElement}
 */
const createLi = (...objectsToAppend) => createHtmlElement('li', ...objectsToAppend);

/**
 * @param {any[]} arr
 * @param {(obj: any) => Appendable} arrElementTransform
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
 * @param {(obj: any) => Appendable} arrElementTransform
 * @returns {HTMLOListElement}
 */
const arrToAnswerNormalOl = (arr, arrElementTransform) =>
    arrToOl(arr, arrElementTransform, answerNormalOlClassName);

/**
 * @param {any[]} arr
 * @param {(obj: any) => Appendable} arrElementTransform
 * @returns {HTMLOListElement}
 */
const arrToAnswerFlexOl = (arr, arrElementTransform) =>
    arrToOl(arr, arrElementTransform, 'answer-flex-ol');

/**
 * @param {string[]} colHeadings
 * @param {any[]} rowsDataSourceArr
 * 
 * @param {(obj: any) => (number | string)[]} rowsDataSourceTransform
 * Return type is an array of numbers and/or unformatted number strings.
 * 
 * @returns {HTMLTableElement}
 */
function createTable(colHeadings, rowsDataSourceArr, rowsDataSourceTransform) {
    const table = createHtmlElement('table');
    const createTr = () => createHtmlElement('tr');
    
    const tHead = createHtmlElement('thead');
    let tr = createTr();
    for (const heading of colHeadings) {
        tr.appendChild(createHtmlElement('th', heading));
    }
    tHead.appendChild(tr);
    table.append(tHead);
    
    const tBody = createHtmlElement('tbody');
    for (const element of rowsDataSourceArr) {
        tr = createTr();
        for (const numOrNumString of rowsDataSourceTransform(element)) {
            tr.appendChild(createHtmlElement('td', createNumStringWithCommas(numOrNumString)));
        }
        tBody.appendChild(tr);
    }
    table.appendChild(tBody);
    
    return table;
}


/**
 * Having the locale be "en-US" ensures that numbers will be formatted with commas. In the section info HTML
 * strings, I'm manually formatting numbers with commas so I'll have other numbers be formatted with commas as
 * well, even if this website is accessed from a country where numbers are formatted differently.
 * 
 * I'll have the maximum fraction digits be the max it can be. It would be 3 by default, but the ratios for
 * the Fibonacci-like sequences section answers need more than 3 fraction digits.
 */
const commaAdder = new Intl.NumberFormat('en-US', { maximumFractionDigits: 100 });

/**
 * @param {number | string} value
 * A number or unformatted number string.
 * 
 * @returns {string}
 */
const createNumStringWithCommas = (value) => commaAdder.format(value);

/**
 * @param {number} a
 * @param {number} b
 * @returns {string}
 */
const numPairToString = (a, b) => `${createNumStringWithCommas(a)} & ${createNumStringWithCommas(b)}`;


const mathMlNamespaceUri = 'http://www.w3.org/1998/Math/MathML';

/**
 * @param {string} elementName
 * @param {...Appendable} objectsToAppend
 * @returns {MathMLElement}
 */
function createMathMlElement(elementName, ...objectsToAppend) {
    const element = document.createElementNS(mathMlNamespaceUri, elementName);
    element.append(...objectsToAppend);
    return element;
}

/**
 * @param  {...Appendable} objectsToAppend
 * @returns {MathMLElement}
 */
const createMathElement = (...objectsToAppend) => createMathMlElement('math', ...objectsToAppend);

/**
 * Creates a math row element, which is used to group related elements together, like a div.
 * @param  {...Appendable} objectsToAppend
 * @returns {MathMLElement}
 */
const createMrow = (...objectsToAppend) => createMathMlElement('mrow', ...objectsToAppend);

/**
 * Creates a math number element.
 * 
 * @param {number | string} value
 * A number or unformatted number string.
 * 
 * @returns {MathMLElement}
 */
const createMn = (value) => createMathMlElement('mn', createNumStringWithCommas(value));

/**
 * Creates a math superscript element.
 * @param {number} base
 * @param {number} power
 */
const createMsup = (base, power) => createMathMlElement('msup', createMn(base), createMn(power));

/**
 * Creates a math operator (e.g. +) element.
 * @param {string} operator
 * @returns {MathMLElement}
 */
const createMo = (operator) => createMathMlElement('mo', operator);

/**
 * Creates a math text element.
 * @param {string} text
 * @param {boolean} [putSpaceAtStart]
 * @returns {MathMLElement}
 */
function createMtext(text, putSpaceAtStart = false) {
    const mtext = createMathMlElement('mtext', text);
    if (putSpaceAtStart) {
        mtext.insertAdjacentHTML('afterbegin', '&nbsp;');
    }
    return mtext;
}

/**
 * @param {string} text
 * @param {boolean} [putSpaceAtStart]
 * @returns {MathMLElement}
 */
function createMtextWithMathFont(text, putSpaceAtStart = false) {
    const mtext = createMtext(text, putSpaceAtStart);
    mtext.className = 'math-font';
    return mtext;
}

/**
 * Creates a math identifier (e.g. letter for variable) element.
 * @param {string} identifier
 * @returns {MathMLElement}
 */
const createMi = (identifier) => createMathMlElement('mi', identifier);

/**
 * @param {string} identifier
 * @param {string} [endText]
 * @returns {string} A Math ML string.
 */
const createMiMl = (identifier, endText) =>
    `<math><mi>${identifier}</mi>${endText ? `<mtext>${endText}</mtext>` : ''}</math>`;

const [aVarMl, bVarMl, cVarMl, nVarMl] = ['a', 'b', 'c', 'n'].map(letter => createMiMl(letter));
const aVarAndPeriodMl = createMiMl('a', '.');
const aVarAndCommaMl = createMiMl('a', ',');
const bVarAndPeriodMl = createMiMl('b', '.');
const bVarAndCommaMl = createMiMl('b', ',');

/**
 * @param {number} num
 * @returns {MathMLElement}
 */
const createNumAndSquareMrow = (num) =>
    createMrow(
        createMsup(num, 2),
        createMtextWithMathFont('(', true),
        createMn(num * num),
        createMtextWithMathFont(')')
    );

/**
 * @param {number} num
 * @returns {string} A Math ML string.
 */
const createNumAndSquareMl = (num) =>
    `<mrow>
        <msup><mn>${num}</mn><mn>2</mn></msup>
        <mrow>
            <mtext class="math-font">&nbsp;(</mtext>
            <mn>${createNumStringWithCommas(num * num)}</mn>
            <mtext class="math-font">)</mtext>
        </mrow>
    </mrow>`;


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

/** @type {Section} */
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
    const textField = createHtmlElement('input');
    textField.className = 'text-field';
    /** @returns {boolean} */
    const textFieldIsBlank = () => !/\S/.test(textField.value);
    
    const randomizeBtn = createBtn('Randomize');
    randomizeBtn.className = 'randomize-btn';
    randomizeBtn.onclick = () => textField.value = curSection.getRandomInput();
    
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

const sectionInfoDetailsSummary = createHtmlElement('summary', 'Info');
const sectionInfoDiv = createDiv();
sectionInfoDiv.id = 'section-info-div';
sectionInfoDiv.className = 'non-answer-info-div';
/** @type {HTMLDetailsElement} */
const sectionInfoDetails = createHtmlElement('details', sectionInfoDetailsSummary, sectionInfoDiv);

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
const oneMillion = 1_000_000;
const oneBillion = 1_000_000_000;


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
     * 
     * @property {string} btnIdStart
     * The ID of the button for this section in section-btns-div.
     * 
     * @property {string | HTMLElement[]} infoHtmlStringOrArr
     * If this is a string, then it'll contain paragraphs of info with each one separated by a blank line.
     * 
     * @property {string} actionSentenceEndingHtml
     * @property {number} minInput
     * @property {number} maxInput
     * 
     * @property {string} apiEndpointEnd
     * The end of the endpoint to make an HTTP request to for the server to do the calculation(s) for this
     * section. The full endpoint is the website URL followed by "/calculate/" followed by this endpoint end.
     */
    
    /** @param {SectionParams} params */
    constructor(params) {
        const {
            btnIdStart, infoHtmlStringOrArr, actionSentenceEndingHtml, minInput, maxInput, apiEndpointEnd
        } = params;
        
        this.#minInput = minInput;
        this.#maxInput = maxInput;
        this.#apiEndpointEnd = apiEndpointEnd;
        
        const sectionBtn = getElementById(btnIdStart + '-btn');
        const heading = sectionBtn.textContent;
        
        /** @type {HTMLElement[]} */
        const infoHtmlElements =
            Array.isArray(infoHtmlStringOrArr)
            ? Array.from(infoHtmlStringOrArr)
            : createPsWithParagraphs(infoHtmlStringOrArr);
        
        const maxInputString = createNumStringWithCommas(maxInput);
        const numWithWord =
            maxInput === oneMillion ? '1 million' : maxInput === oneBillion ? '1 billion' : null;
        const maxInputSentencePart =
            numWithWord ? `${numWithWord} (${maxInputString})` : maxInputString;
        
        const directionsHtml =
            `Enter or generate ${this.isSingleInputSection ? 'a whole number' : '2 whole numbers'} and click
            the "Calculate" button to get ${actionSentenceEndingHtml}.
            Have ${this.isSingleInputSection ? 'this number' : 'these numbers'} be ${
                this.needsEvenInput
                ? `even, ≥ ${minInput}, and ≤ ${maxInputSentencePart}`
                : `≥ ${minInput} and ≤ ${maxInputSentencePart}`
            }. Commas are optional.`;
        
        sectionBtn.onclick = () => {
            // An arrow function is used so that "this" refers to the Section instance.
            curSection = this;
            sectionHeading.textContent = heading;
            sectionInfoDetails.open = false;
            sectionInfoDiv.replaceChildren(...infoHtmlElements);
            sectionDirectionsDiv.innerHTML = directionsHtml;
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
    
    /** @returns {boolean} */
    get isSingleInputSection() {
        return this instanceof SingleInputSection;
    }
    
    /** @returns {boolean} */
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
     * 
     * @param {any} responseObj
     * An object or null from the response of an HTTP request that does the calculation(s) for this section.
     * 
     * @param {string} inputString
     * Contains the input number with commas.
     * 
     * @param {number} inputNum
     * The createAnswerElements functions for the Divisibility and Goldbach Conjecture Sections use this number.
     * The other createAnswerElements functions omit this param.
     * 
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
     * 
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


const invalidInputDiv = createDiv('Invalid input');
function showInvalidInputMessage() {
    answerDiv.replaceChildren(invalidInputDiv);
}
const requestErrorDiv = createDiv('Error with request');
requestErrorDiv.className = invalidInputDiv.className = 'answer-error-div';

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
     * responseObj is an object or null from a response from an HTTP request made below.
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
    
    fetch(`calculate/${curSection.apiEndpointEnd}?${urlParams}`)
    .then(response =>
        response.ok
        ? response.text().then(text => createAnswerElements(text.length === 0 ? null : JSON.parse(text)))
        : [requestErrorDiv]
    )
    .then(elements => answerDiv.replaceChildren(...elements))
    .catch(reason => {
        answerDiv.replaceChildren(requestErrorDiv);
        console.error(requestErrorDiv.textContent, reason);
    });
};


/** A Math ML string. */
const largestKnownPrimeAndPeriodMl =
    `<math>
        <msup><mn>2</mn><mn>136,279,841</mn></msup>
        <mo>−</mo>
        <mn>1</mn>
        <mtext>.</mtext>
    </math>`;

const primesInfoHtml =
    `A <i>prime number</i>, or a <i>prime</i>, is a whole number > 1 that isn't divisible by any whole numbers
    other than 1 and itself. A <i>composite number</i> is a whole number > 1 that is divisible by a whole number
    other than 1 and itself. The first 10 primes are 2, 3, 5, 7, 11, 13, 17, 19, 23, and 29. There are an
    infinite amount of primes. The largest known one is ${largestKnownPrimeAndPeriodMl} It has 41,024,320 digits!
    Primes are used in 8 of the 11 sections in the Number Theory Playground.
    
    With the exception of 2 and 3, all primes are either 1 above or 1 below a multiple of 6. To show why this
    is the case, let's have a variable ${nVarMl} and let it represent a whole number ≥ 6 that's a multiple of
    6. We know that ${nVarMl} is divisible by 2 and 3 so ${nVarMl} + 2 and ${nVarMl} + 4 are divisible
    by 2 and ${nVarMl} + 3 is divisible by 3 but we don't have any guarantees about what ${nVarMl} + 1 and
    ${nVarMl} + 5 are divisible by. Therefore, that's where primes can be.
    
    A whole number can be determined to be prime if it's not divisible by any primes ≤ the square root of that
    number. This is called <i>trial division</i>. Let's determine if 29 and 33 are prime.
    <math>
        <msup><mn>5</mn><mn>2</mn></msup>
        <mo>=</mo>
        <mn>25</mn>
    </math>
    and
    <math>
        <msup><mn>6</mn><mn>2</mn></msup>
        <mo>=</mo>
        <mn>36</mn>
    </math>
    so
    <math>
        <mn>5</mn>
        <mo><</mo>
        <msqrt><mn>29</mn></msqrt>
        <mo><</mo>
        <msqrt><mn>33</mn></msqrt>
        <mo><</mo>
        <mn>6</mn>
        <mtext>.</mtext>
    </math>
    We check if 29 and 33 are divisible by 2, 3, or 5; which are the primes ≤ 5. 29 isn't divisible by any
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
        actionSentenceEndingHtml: 'the first 30 primes ≥ that number',
        minInput: 0,
        maxInput: 100_000,
        apiEndpointEnd: 'primes'
    },
    createPrimesAnswerElements
);


const semiprimesInfoHtml =
    `A <i>semiprime</i>, also known as a <i>biprime</i>, is a number made by multiplying 2, possibly equal,
    prime numbers. The first 5 semiprimes and their prime number factors are 4 (2 × 2), 6 (2 × 3), 9 (3 × 3),
    10 (2 × 5), and 14 (2 × 7). Since there are an infinite amount of prime numbers, there are also an infinite
    amount of semiprimes. The largest known semiprime is the square of the largest known prime number, which is
    ${largestKnownPrimeAndPeriodMl}`;

/** @typedef {{ semiprime: number, primeFactor1: number, primeFactor2: number }} SemiprimeData */

/**
 * @param {SemiprimeData[]} semiprimesData
 * @param {string} inputString
 * @returns {HTMLElement[]}
 * An array with a heading and an ordered list that shows semiprimes and their prime number factors.
 */
function createSemiprimesAnswerElements(semiprimesData, inputString) {
    const headingText = `The first ${semiprimesData.length} semiprimes ≥ ${inputString} are:`;
    
    /**
     * @param {SemiprimeData} sd
     * @returns {string}
     */
    function semiprimeDataToString(sd) {
        const [semiprimeString, factor1String, factor2String] =
            [sd.semiprime, sd.primeFactor1, sd.primeFactor2]
            .map(createNumStringWithCommas);
        
        return `${semiprimeString} (${factor1String} × ${factor2String})`;
    }
    
    const semiprimesOl = arrToAnswerFlexOl(semiprimesData, semiprimeDataToString);
    return [createNonBoldAnswerH3(headingText), semiprimesOl]
}

new SingleInputSection(
    {
        btnIdStart: 'semiprimes',
        infoHtmlStringOrArr: semiprimesInfoHtml,
        actionSentenceEndingHtml: 'the first 20 semiprimes ≥ that number, as well as their prime number factors',
        minInput: 0,
        maxInput: 100_000,
        apiEndpointEnd: 'semiprimes-data'
    },
    createSemiprimesAnswerElements
);


const conjectureDefinitionHtml =
    `A <i>conjecture</i> is a statement that's believed to be true but hasn't been proven to be`;

const twinPrimePairsInfoHtml =
    `A <i>twin prime pair</i> is a pair of prime numbers that differ by 2. The first 5 twin prime pairs are
    3 & 5, 5 & 7, 11 & 13, 17 & 19, and 27 & 29. The largest known twin prime pair is
    <math>
        <mrow>
            <mo>(</mo>
            <mn>2,996,863,034,895</mn>
            <mo>×</mo>
            <msup><mn>2</mn><mn>1,290,000</mn></msup>
            <mo>)</mo>
        </mrow>
        <mo>±</mo>
        <mn>1</mn>
        <mtext>.</mtext>
    </math>
    They have 388,342 digits! The <i>twin prime conjecture</i> says that there are an infinite amount of twin
    prime pairs. ${conjectureDefinitionHtml}.
    
    All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so this means that all twin
    prime pairs besides 3 and 5 consist of 1 number that's 1 below a multiple of 6 and another number that's 1
    above that same multiple of 6. 5 is the only number to be in 2 twin prime pairs, the first 2 mentioned above.`;

const twinPrimePairsHeadingStart = 'The first 20 twin prime pairs where the lowest number in the pair is ≥';
const twinPrimePairsActionSentenceEnding = twinPrimePairsHeadingStart.toLowerCase() + ' that number';

/**
 * @param {number[]} pairStarts
 * @param {string} inputString
 * @returns {HTMLElement[]}
 * An array with a heading and an ordered list that shows twin prime pairs.
 */
function createTwinPrimePairsAnswerElements(pairStarts, inputString) {
    const headingText = `${twinPrimePairsHeadingStart} ${inputString} are:`;
    const pairsOl = arrToAnswerFlexOl(pairStarts, (start) => numPairToString(start, start + 2));
    return [createNonBoldAnswerH3(headingText), pairsOl];
}

new SingleInputSection(
    {
        btnIdStart: 'twin-prime-pairs',
        infoHtmlStringOrArr: twinPrimePairsInfoHtml,
        actionSentenceEndingHtml: twinPrimePairsActionSentenceEnding,
        minInput: 0,
        maxInput: 100_000,
        apiEndpointEnd: 'twin-prime-pair-starts'
    },
    createTwinPrimePairsAnswerElements
);


/**
 * Creates a Math ML string for the prime factorization whose factors and powers are in factorsAndPowers.
 * 
 * @param {number[][]} factorsAndPowers
 * For each inner array, the first number is the factor and the second is the power.
 * 
 * @returns {string}
 */
const createPfMl = (factorsAndPowers) =>
    factorsAndPowers
    .map(([factor, power]) => power === 1 ? `<mn>${factor}</mn>` : `<msup><mn>${factor}</mn><mn>${power}</mn></msup>`)
    .join('<mo>×</mo>');

/**
 * @param {number[][]} factorsAndPowers
 * @param {string} [endText]
 * @returns {string}
 */
const createPfMathElementMl = (factorsAndPowers, endText) =>
    `<math>${createPfMl(factorsAndPowers)}${endText ? `<mtext>${endText}</mtext>`: ''}</math>`;

const pfInfoHtml =
    `The fundamental theorem of arithmetic says that every whole number > 1 can be expressed as the product of
    prime numbers in 1 way if you ignore the order of those prime numbers. The <i>prime factorization</i> (PF)
    of a whole number > 1 is an expression of the prime numbers whose product is that number. For example; the
    PF of 5 is just <math><mn>5</mn><mtext>,</mtext></math> the PF of 25 is ${createPfMathElementMl([[5, 2]], ',')}
    and the PF of 4,725 is ${createPfMathElementMl([[3, 3], [5, 2], [7, 1]])} if the prime numbers are in
    ascending order. 4,725 could also be expressed as ${createPfMathElementMl([[5, 2], [3, 3], [7, 1]])} but
    that's the same expression as the previous one if you ignore the order of the prime numbers. The Number
    Theory Playground displays PFs with the prime numbers in ascending order. There are some interesting
    applications for PFs. See the info for the "Divisibility" or "GCD and LCM" sections for some applications.
    
    The input number with the highest amount of prime factors is 8,192 (2<sup>13</sup>). An input number with
    the highest amount of <em>unique</em> prime factors is 2,310. This number has a PF of
    ${createPfMathElementMl([[2, 1], [3, 1], [5, 1], [7, 1], [11, 1]], '.')} You could also multiply that number
    by 2, 3, or 4 and those numbers are ≤ the max input and have the same amount of unique prime factors.`;

/**
 * @typedef {Object} PrimeFactorization
 * 
 * @property {number} correspondingNum
 * 
 * @property {?(FactorAndPower[])} fps
 * If this is null, then that means the corresponding number is prime and therefore the PF just consists of 1
 * factor with 1 as its power.
 */

/**
 * @param {FactorAndPower[]} fps
 * @param {string} [endText]
 * @returns {MathMLElement}
 * A math element for the prime factorization whose factors and powers are in fps.
 */
function createPfMathElement(fps, endText) {
    const pfEl = createMathElement();
    
    for (const { factor, power} of fps) {
        pfEl.append(
            pfEl.firstChild ? createMo('×') : '',
            power === 1 ? createMn(factor) : createMsup(factor, power)
        )
    }
    
    if (endText) {
        pfEl.appendChild(createMtext(endText))
    }
    
    return pfEl;
}

/**
 * @param {FactorAndPower[]} fps
 * @param {string} inputString
 * @returns {HTMLElement[]}
 */
function createPfAnswerElements(fps, inputString) {
    const headingText = `The PF of ${inputString} is:`;
    /*
    Put math element in a div since math elements are displayed inline by default and manually
    giving them a block display causes their children to get a block display as well.
     */
    const pfDiv = createDiv(createPfMathElement(fps));
    pfDiv.id = 'pf-section-answer-div';
    return [createNonBoldAnswerH3(headingText), pfDiv];
}

const pfMinInput = 2;

new SingleInputSection(
    {
        btnIdStart: 'pf',
        infoHtmlStringOrArr: pfInfoHtml,
        actionSentenceEndingHtml: 'the PF of that number',
        minInput: pfMinInput,
        maxInput: pfMaxInput,
        apiEndpointEnd: 'prime-factorization'
    },
    createPfAnswerElements
);


const divisInfoStartHtml =
    `Say we have 2 whole numbers that we'll represent with the variables ${aVarMl} and ${bVarAndPeriodMl}
    If we divide ${aVarMl} by ${bVarMl} and get no remainder, then ${aVarMl} is said to be <i>divisible</i> by
    ${bVarMl} and ${bVarMl} is said to be a <i>factor</i> or <i>divisor</i> of ${aVarMl}. If you want to find
    some whole number factors of a whole number, you could manually do some division but there are other ways to
    find them.`;

/**
 * @param {number[][]} factorsAndPowers
 * For each inner array, the first number is the factor and the second is the power.
 * 
 * @param {number} num
 * @param {string} [endText]
 * @returns {string} A Math ML string.
 */
const createSubfactorizationMl = (factorsAndPowers, num, endText = ',') =>
    `<math>
        <mrow>${createPfMl(factorsAndPowers)}</mrow>
        <mrow>
            <mtext class="math-font">&nbsp;(</mtext>
            <mn>${num}</mn>
            <mtext class="math-font">)</mtext>
        </mrow>
        <mtext>${endText}</mtext>
    </math>`;

const nVarAndSMl = createMiMl('n', `'s`);
const pfOf36FactorsAndPowers = [[2, 2], [3, 2]];

const divisPfInfoHtml =
    `The factors of a whole number > 1 can be found by looking at its prime factorization (PF). Let's have a
    variable ${nVarMl} and let it represent a whole number > 1. First, you can find how many factors ${nVarMl}
    has by looking at ${nVarAndSMl} PF, taking the powers of the factors, adding 1 to each, and multiplying them.
    For example, the PF of 36 is ${createPfMathElementMl(pfOf36FactorsAndPowers, '.')} The powers are 2 and 2,
    so there are <math><mn>3</mn><mo>×</mo><mn>3</mn><mo>=</mo><mn>9</mn></math> factors. This amount includes 1
    and the number that the PF is for (36 in this case). You can find the factors of ${nVarMl} by finding the PFs
    within ${nVarAndSMl} PF, or the <i>subfactorizations</i>, as I like to call them.
    For ${createPfMathElementMl(pfOf36FactorsAndPowers, ',')} the subfactorizations are
    <math><mn>2</mn><mtext>,</mtext></math>
    <math><mn>3</mn><mtext>,</mtext></math>
    ${createSubfactorizationMl([[2, 2]], 4)}
    ${createSubfactorizationMl([[2, 1], [3, 1]], 6)}
    ${createSubfactorizationMl([[3, 2]], 9)}
    ${createSubfactorizationMl([[2, 2], [3, 1]], 12)}
    and
    ${createSubfactorizationMl([[2, 1], [3, 2]], 18, '.')}
    
    Whole numbers that are ≤ 1 million, the max input of this section, generally have a small amount of factors,
    like < 50. An example of an input number with a high amount of factors is 510,510. This number has a PF of
    ${createPfMathElementMl([[2, 1], [3, 1], [5, 1], [7, 1], [11, 1], [13, 1], [17, 1]], ',')} so it has
    <math><msup><mn>2</mn><mn>7</mn></msup><mo>=</mo><mn>128</mn></math> factors!`;

const divisPfInfoDiv =
    createDiv(createH3('Prime Factorization'), ...createPsWithParagraphs(divisPfInfoHtml));

const divisRulesInfoHtml =
    `Some rules can be used to determine if a whole number is divisible by another whole number. I'll go over 1
    rule for each number in the range of 3 to 15, excluding 5 and 10, though there are rules for more numbers and
    many numbers have multiple rules. I'll go over an example of using these rules to find the factors of a number
    in the "Example" section below. Let's have a variable ${nVarMl} and let it represent a whole number. If the
    number formed from the last 2 digits of ${nVarMl} is divisible by 4, then ${nVarMl} is divisible by 4. If the
    number formed from the last 3 digits is divisible by 8, then ${nVarMl} is divisible by 8. If the sum of the
    digits of ${nVarMl} is divisible by 3, then ${nVarMl} is divisible by 3. If the sum of the digits is divisible
    by 9, then ${nVarMl} is divisible by 9. If ${nVarMl} is even and divisible by 3,then it's also divisible by 6.
    If ${nVarMl} is divisible by both 3 and 4, then it's also divisible by 12. If ${nVarMl} is even and divisible
    by 7 (see rule below), then it's also divisible by 14. If ${nVarMl} is divisible by both 3 and 5, then it's
    also divisible by 15.
    
    For 11, we do an alternating sum of the digits of ${nVarMl} from left to right. We start with 0, add the
    1<sup>st</sup> digit, subtract the 2<sup>nd</sup> digit, add the 3<sup>rd</sup> digit, and so on for all the
    digits. If this sum is divisible by 11, then ${nVarMl} is divisible by 11.
    
    For 7 and 13, we split ${nVarMl} into 3-digit blocks from right to left, though the leftmost block can
    contain 1 or 2 digits. Coincidentally, these are the blocks separated by commas if we write ${nVarMl} with
    commas. We do an alternating sum of the blocks from right to left. We start with 0, add the rightmost block,
    subtract the block to the left of that, add the block to the left of that, and so on for all the blocks. If
    this sum is divisible by 7, then ${nVarMl} is divisible by 7. If this sum is divisible by 13, then ${nVarMl}
    is divisible by 13. These alternating sums might involve negative integers or 0, so that makes them some of
    the few calculations done by the Number Theory Playground that involve numbers other than natural numbers.`;

const divisRulesExampleHtml =
    `Let ${nVarMl} be 720,720. Its PF is ${createPfMathElementMl([[2, 4], [3, 2], [5, 1], [7, 1], [11, 1], [13, 1]], '.')}
    We can tell from that PF that ${nVarMl} is divisible by all the numbers that had rules mentioned about them
    above. Let's check using those rules. The last 2 digits form the number 20, which is divisible by 4. The last
    3 digits form the number 720, which is divisible by 8. The sum of the digits is
    <math>
        <mn>7</mn>
        <mo>+</mo>
        <mn>2</mn>
        <mo>+</mo>
        <mn>0</mn>
        <mo>+</mo>
        <mn>7</mn>
        <mo>+</mo>
        <mn>2</mn>
        <mo>+</mo>
        <mn>0</mn>
        <mo>=</mo>
        <mn>18</mn>
        <mtext>,</mtext>
    </math>
    which is divisible by both 3 and 9. Since ${nVarMl} is even and divisible by 3, it's also divisible by 6.
    Since ${nVarMl} is divisible by both 3 and 4, it's also divisible by 12. Since ${nVarMl} is divisible by
    both 3 and 5, it's also divisible by 15. The alternating sum of the digits from left to right is
    <math>
        <mn>7</mn>
        <mo>−</mo>
        <mn>2</mn>
        <mo>+</mo>
        <mn>0</mn>
        <mo>−</mo>
        <mn>7</mn>
        <mo>+</mo>
        <mn>2</mn>
        <mo>−</mo>
        <mn>0</mn>
        <mo>=</mo>
        <mn>0</mn>
        <mtext>,</mtext>
    </math>
    which is divisible by 11. The alternating sum of 3-digit blocks from right to left is
    <math><mn>720</mn><mo>−</mo><mn>720</mn><mo>=</mo><mn>0</mn><mtext>,</mtext></math> which is divisible by
    both 7 and 13. Since ${nVarMl} is even and divisible by 7, it's also divisible by 14.`;

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

/** @typedef {{ inputFps: FactorAndPower[], factorPfs: PrimeFactorization[] }} DivisibilityPrimeFactorizationAnswer */

/**
 * @param {?DivisibilityPrimeFactorizationAnswer} pfAnswer
 * @param {string} inputString
 * @param {number} inputNum
 * @returns {HTMLElement[]}
 */
const createDivisAnswerElements = (pfAnswer, inputString, inputNum) =>
    [
        createH3(`Divisibility Info for ${inputString}`),
        createDivisRulesAnswerDiv(inputString, inputNum),
        createDivisPfAnswerDiv(pfAnswer, inputString, inputNum)
    ];

/**
 * This function does the only non-trivial calculations that are done on the front end.
 * 
 * @param {string} inputString
 * @param {number} inputNum
 * @returns {HTMLDivElement}
 * A div with a heading and a paragraph element with info about factors of the input number that are found
 * using divisibility rules.
 */
function createDivisRulesAnswerDiv(inputString, inputNum) {
    const heading = createH4('Rules Info');
    
    /**
     * For all the rules besides the ones for 6 and 12, we do a calculation with the input num and if the result of
     * that calculation is divisible by a certain number, then the input number is also divisible by that number.
     * 
     * @param {number} possibleFactor
     * @param {boolean} isDivisible
     * @param {boolean} [startWithWhich]
     * @returns {string}
     * This has a space at the end to separate the sentence in this string from the next sentence in the created paragraph.
     */
    function getDivisSentenceEnd(possibleFactor, isDivisible, startWithWhich = true) {
        const isOrIsnt = isDivisible ? 'is' : `isn't`;
        return ` ${startWithWhich ? 'which ' : ''}${isOrIsnt} divisible by ${possibleFactor} so
            ${inputString} ${isOrIsnt} divisible by ${possibleFactor}. `;
    }
    
    const answerP = createP();
    const isEvenVar = isEven(inputNum);
    const last2Digits = inputNum % 100;
    const isDivisibleBy4 = isDivisible(last2Digits, 4);
    
    if (!isEvenVar) {
        answerP.append(`${inputString} isn't even so it isn't divisible by any even numbers. `);
    } else if (inputNum >= 100) {
        answerP.append(
            `The last 2 digits form the number ${last2Digits},`,
            getDivisSentenceEnd(4, isDivisibleBy4)
        );
        
        if (!isDivisibleBy4) {
            answerP.append(
                `Since ${inputString} isn't divisible by 4, it's also not divisible by 8, 12, and any
                other multiples of 4. `
            );
        } else if (inputNum >= 1_000) {
            const last3Digits = inputNum % 1_000;
            const isDivisibleBy8 = isDivisible(last3Digits, 8);
            answerP.append(
                `The last 3 digits form the number ${last3Digits},`,
                getDivisSentenceEnd(8, isDivisibleBy8)
            );
        }
    }
    
    const sumOfDigitsEl = createMathElement();
    let sumOfDigits = 0;
    const altSumOfDigitsEl = createMathElement();
    let altSumOfDigits = 0;
    let addForAltSum = true;
    
    for (const digitString of inputNum.toString()) {
        if (sumOfDigitsEl.firstChild) {
            sumOfDigitsEl.appendChild(createMo('+'));
            altSumOfDigitsEl.appendChild(createMo(addForAltSum ? '+' : '−'));
        }
        const digitNum = Number(digitString);
        sumOfDigitsEl.appendChild(createMn(digitString));
        sumOfDigits += digitNum;
        altSumOfDigitsEl.appendChild(createMn(digitString));
        altSumOfDigits += addForAltSum ? digitNum : -digitNum;
        addForAltSum = !addForAltSum;
    }
    
    sumOfDigitsEl.append(createMo('='), createMn(sumOfDigits), createMtext(','));
    altSumOfDigitsEl.append(createMo('='), createMn(altSumOfDigits), createMtext(','));
    
    const isDivisibleBy3 = isDivisible(sumOfDigits, 3);
    answerP.append(
        'The sum of the digits is ',
        sumOfDigitsEl,
        getDivisSentenceEnd(3, isDivisibleBy3)
    );
    
    if (!isDivisibleBy3) {
        answerP.append(
            `Since ${inputString} isn't divisible by 3, it's also not divisible by 6, 9, 12, and any
            other multiples of 3. `
        );
    } else {
        const isDivisibleBy9 = isDivisible(sumOfDigits, 9);
        answerP.append(sumOfDigits, getDivisSentenceEnd(9, isDivisibleBy9, false));
        
        if (isEvenVar) {
            answerP.append(`${inputString} is even and divisible by 3 so it's also divisible by 6. `);
            
            if (isDivisibleBy4) {
                answerP.append(`${inputString} is divisible by both 3 and 4 so it's also divisible by 12. `);
            } else if (inputNum < 100) {
                answerP.append(`${inputString} isn't divisible by 4 so it isn't divisible by 12. `)
            }
        }
        
        if (isDivisible(inputNum, 5)) {
            answerP.append(`${inputString} is divisible by both 3 and 5 so it's also divisible by 15. `);
        }
    }
    
    const isDivisibleBy11 = isDivisible(altSumOfDigits, 11);
    answerP.append(
        'The alternating sum of the digits from left to right is ',
        altSumOfDigitsEl,
        getDivisSentenceEnd(11, isDivisibleBy11)
    );
    
    if (inputNum >= 1_000) {
        const altSumOfBlocksEl = createMathElement();
        let altSumOfBlocks = 0;
        addForAltSum = true;
        const blocksOf3 = inputString.split(',');
        
        for (let i = blocksOf3.length - 1; i >= 0; i--) {
            if (altSumOfBlocksEl.firstChild) {
                altSumOfBlocksEl.append(createMo(addForAltSum ? '+' : '−'));
            }
            const blockString = blocksOf3[i];
            altSumOfBlocksEl.append(createMn(blockString));
            const blockNum = Number(blockString);
            altSumOfBlocks += addForAltSum ? blockNum : -blockNum;
            addForAltSum = !addForAltSum;
        }
        
        altSumOfBlocksEl.append(createMo('='), createMn(altSumOfBlocks), createMtext(','));
        const isDivisibleBy7 = isDivisible(altSumOfBlocks, 7);
        answerP.append(
            'The alternating sum of 3-digit blocks from right to left is ',
            altSumOfBlocksEl,
            getDivisSentenceEnd(7, isDivisibleBy7)
        );
        
        const isDivisibleBy13 = isDivisible(altSumOfBlocks, 13);
        answerP.append(altSumOfBlocks, getDivisSentenceEnd(13, isDivisibleBy13, false));
        
        if (isEvenVar && isDivisibleBy7) {
            answerP.append(`${inputString} is even and divisible by 7 so it's also divisible by 14. `);
        }
    }
    
    const div = createDiv(heading, answerP);
    div.id = 'divis-rules-answer-div';
    return div;
}

/**
 * @param {?DivisibilityPrimeFactorizationAnswer} pfAnswer
 * @param {string} inputString
 * @param {number} inputNum
 * @returns {HTMLDivElement}
 * A div with at least a heading and an inner div with text that shows the PF of the input number. If pfAnswer
 * is null, then the inner div will only contain a little more info. Otherwise, the inner div will contain more
 * info about the number of factors the input number has. Also, the returned div will also contain an ordered
 * list that shows the factors of the input number and the PFs of those factors.
 */
function createDivisPfAnswerDiv(pfAnswer, inputString, inputNum) {
    const heading = createH4('Prime Factorization Info');
    const pfInfoTextDiv = createNarrowTextDiv(`The PF of ${inputString} is `);
    const pfDiv = createDiv(heading, pfInfoTextDiv);
    pfDiv.id = 'divis-pf-answer-div';
    
    if (!pfAnswer) {
        pfInfoTextDiv.append(
            createMathElement(createMn(inputNum), createMtext('.')),
            ` ${inputString} is prime and doesn't have any whole number factors other than 1 and itself.`
        );
        return pfDiv;
    }
    
    const { inputFps, factorPfs } = pfAnswer;
    pfInfoTextDiv.append(createPfMathElement(inputFps, '.'), ' ');
    
    const numFactorsEl = createMathElement();
    for (const { power } of inputFps) {
        numFactorsEl.append(
            numFactorsEl.firstChild ? createMo('×') : '',
            createMrow(createMo('('), createMn(power), createMo('+'), createMn(1), createMo(')'))
        );
    }
    // factorPfs doesn't include 1 and the input number.
    numFactorsEl.append(createMo('='), createMn(factorPfs.length + 2));
    
    pfInfoTextDiv.append(
        `By looking at the power${inputFps.length === 1 ? '' : 's'}, we can see that there are `,
        numFactorsEl,
        ` factors. The factors, excluding 1 and ${inputString}, and their PFs are:`
    );
    
    /**
     * @param {PrimeFactorization}
     * @returns {MathMLElement}
     */
    function createPfEl({ fps, correspondingNum }) {
        const corNumMn = createMn(correspondingNum);
        if (!fps) return createMathElement(corNumMn);
        const pfEl = createPfMathElement(fps);
        pfEl.appendChild(
            createMrow(createMtextWithMathFont('(', true), corNumMn, createMtextWithMathFont(')'))
        );
        return pfEl;
    }
    
    pfDiv.appendChild(arrToAnswerFlexOl(factorPfs, createPfEl));
    return pfDiv;
}

new SingleInputSection(
    {
        btnIdStart: 'divis',
        infoHtmlStringOrArr: divisInfoElements,
        actionSentenceEndingHtml: 'divisbility info for that number',
        minInput: 10,
        maxInput: pfMaxInput,
        apiEndpointEnd: 'divisibility-pf-answer'
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

/** @typedef {{ max: number, min: number, remainder: number }} EuclideanIteration */

/**
 * @param {string} className
 * 
 * @param {HTMLDivElement | HTMLHeadingElement} firstChild
 * For examples in the GCD and LCM section info, this is a div and for answers, this is an h4.
 * 
 * @param {EuclideanIteration[]} iterations
 * 
 * @returns {HTMLDivElement}
 * A div with a class of className and whose children are firstChild; a table that shows the max, min, and
 * remainder of each iteration of the Euclidean algorithm performed on 2 input numbers; and a div with a message
 * about the GCD of the input numbers.
 */
function createEuclideanTableDiv(className, firstChild, iterations) {
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
    
    const tableDiv = createDiv(firstChild, table, gcdMessageDiv);
    tableDiv.className = className;
    return tableDiv;
}

/**
 * @param {EuclideanIteration[]} iterations
 * @returns {HTMLDivElement}
 */
function createEuclideanExampleDiv(iterations) {
    const startText =
        `Let's find the GCD of ${iterations[0].min} and ${iterations[0].max} using the Euclidean algorithm.
        Here are the iterations:`;
    return createEuclideanTableDiv('euclidean-example-table-div', createDiv(startText), iterations);
}

/** @type {EuclideanIteration[]} */
const euclideanExample1Iterations =
    [
        { max: 35, min: 6, remainder: 5 },
        { max: 6, min: 5, remainder: 1 },
        { max: 5, min: 1, remainder: 0}
    ];

/** @type {EuclideanIteration[]} */
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
    numbers don't have any common prime factors, then the GCD is 1. If they do have common prime factors, then the
    GCD PF consists of all the common prime factors and the power of each factor is the min of the powers of that
    factor in the 2 PFs. The LCM PF consists of all prime factors that are in either of the PFs of the 2 numbers.
    If a factor is in both PFs, then the power of that factor in the LCM PF is the max of the powers of that
    factor in the 2 PFs. If a factor is unique to one of the PFs, then that factor and its power are in the LCM PF.
    
    Let's find the GCD and LCM of 6 and 35 using their PFs. The PF of 6 is ${createPfMathElementMl([[2, 1], [3, 1]])}
    and the PF of 35 is ${createPfMathElementMl([[5, 1], [7, 1]], '.')} There are no common prime factors so the
    GCD is 1. The LCM PF is ${createPfMathElementMl([[2, 1], [3, 1], [5, 1], [7, 1]], ',')} which is 210.
    
    Let's find the GCD and LCM of 54 and 99 using their PFs. The PF of 54 is ${createPfMathElementMl([[2, 1], [3, 3]])}
    and the PF of 99 is ${createPfMathElementMl([[3, 2], [11, 1]], '.')} 3 is the only common prime factor and
    the min power of it is 2 so the GCD PF is ${createPfMathElementMl([[3, 2]], ',')} which is 9. The max power
    of 3 is 3 so ${createPfMathElementMl([[3, 3]])} is in the LCM PF. The LCM PF is
    ${createPfMathElementMl([[2, 1], [3, 3], [11, 1]], ',')} which is 594.`;

const gcdAndLcmPfInfoDiv =
    createDiv(createH3('Prime Factorizations'), ...createPsWithParagraphs(gcdAndLcmPfInfoHtml));

const gcdAndLcmOtherInfo =
    `2 whole numbers are said to be <i>coprime</i> if their GCD is 1. Therefore, coprime numbers don't have any
    common factors in their PFs. The input numbers whose LCM is the highest are 10,000, the max input, and 9,999.
    Their LCM is 99,990,000. A pair of input numbers whose LCM has the highest amount of prime factors is
    8,192 (2<sup>13</sup>) and 6,561 (3<sup>8</sup>). Their LCM is 53,747,712. A pair of input numbers whose LCM
    might have the highest amount of <em>unique</em> prime factors is 2,310, the product of the first 5 prime
    numbers; and 4,199, the product of the next 3 prime numbers. Their LCM is 9,699,690 and its PF is
    ${createPfMathElementMl([[2, 1], [3, 1], [5, 1], [7, 1], [11, 1], [13, 1], [17, 1], [19, 1]], '.')}`;

const gcdAndLcmOtherInfoDiv =
    createDiv(createH3('Other Info'), createPWithInnerHtml(gcdAndLcmOtherInfo));

const gcdAndLcmInfoElements =
    [createP(gcdAndLcmInfoStart), euclideanInfoDiv, gcdAndLcmPfInfoDiv, gcdAndLcmOtherInfoDiv];

/** @typedef {{ input1Fps: FactorAndPower[], input2Fps: FactorAndPower[], gcdPf: PrimeFactorization?, lcmPf: PrimeFactorization }} GcdAndLcmPrimeFactorizationAnswer */

/**
 * @param {{ euclideanIterations: EuclideanIteration[], pfAnswer: GcdAndLcmPrimeFactorizationAnswer }}
 * @param {string} input1String
 * @param {string} input2String
 * @returns {HTMLElement[]}
 */
const createGcdAndLcmAnswerElements = ({ euclideanIterations, pfAnswer }, input1String, input2String) =>
    [
        createH3(`GCD and LCM Info for ${input1String} and ${input2String}`),
        createEuclideanTableDiv('euclidean-answer-table-div', createH4('Euclidean Algorithm Iterations'), euclideanIterations),
        createGcdAndLcmPfAnswerDiv(pfAnswer, input1String, input2String)
    ];

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
        createLi(`The PF of ${inputString} is `, createPfMathElement(fps, '.'));
    
    /**
     * @param {string} gcdOrLcmText
     * @param {PrimeFactorization}
     * @returns {HTMLLIElement}
     */
    function createGcdOrLcmPfLi(gcdOrLcmText, { fps, correspondingNum }) {
        const li = createLi(`The PF of the ${gcdOrLcmText} is `);
        if (fps) {
            li.append(createPfMathElement(fps, ','), ' which is ', createNumStringWithCommas(correspondingNum), '.');
        } else {
            li.append(createMathElement(createMn(correspondingNum), createMtext('.')))
        }
        return li;
    }
    
    const { input1Fps, input2Fps, gcdPf, lcmPf } = answer;
    
    const gcdLi =
        gcdPf
        ? createGcdOrLcmPfLi('GCD', gcdPf)
        : createLi('There are no common prime factors so the GCD is 1.');
    
    const pfsOl =
        createOl(
            createInputPfLi(input1String, input1Fps),
            createInputPfLi(input2String, input2Fps),
            gcdLi,
            createGcdOrLcmPfLi('LCM', lcmPf)
        );
    pfsOl.id = 'gcd-and-lcm-pf-answer-ol';
    pfsOl.className = answerNormalOlClassName;
    
    const div = createDiv(heading, pfsOl);
    div.id = 'gcd-and-lcm-pf-answer-div';
    return div;
}

new DoubleInputSection(
    {
        btnIdStart: 'gcd-and-lcm',
        infoHtmlStringOrArr: gcdAndLcmInfoElements,
        actionSentenceEndingHtml: 'GCD and LCM info for those numbers',
        minInput: pfMinInput,
        maxInput: pfMaxInput,
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
        `There${thereIs1Pair ? `'s 1 pair` : ` are ${createNumStringWithCommas(primePairStarts.length)} pairs`}
        of prime numbers that sum to ${inputString}. ${thereIs1Pair ? 'It is' :  'They are'}:`;
    const pairsOl = arrToAnswerFlexOl(primePairStarts, (start) => numPairToString(start, inputNum - start));
    return [createNonBoldAnswerH3(headingText), pairsOl];
}

new GoldbachConjectureSection(
    {
        btnIdStart: 'goldbach-conjecture',
        infoHtmlStringOrArr: goldbachConjectureInfoHtml,
        actionSentenceEndingHtml: 'the pairs of prime numbers that sum to that number',
        minInput: 4,
        maxInput: 10_000,
        apiEndpointEnd: 'goldbach-prime-pair-starts'
    },
    createGoldbachConjectureAnswerElements
);


/**
 * @param {number} a
 * @param {number} b
 * @param {number} c
 * @returns {string} A Math ML string. This is used at the ends of sentences so a period is included.
 */
const createPythagTripleMl = (a, b, c) =>
    `<math>
        ${createNumAndSquareMl(a)}
        <mo>+</mo>
        ${createNumAndSquareMl(b)}
        <mo>=</mo>
        ${createNumAndSquareMl(c)}
        <mtext>.</mtext>
    </math>`;

const pythagTriplesInfoHtml =
    `The Pythagorean theorem says that for a right triangle, the sum of the squares of the lengths of the 2
    shortest sides (legs) equals the square of the longest side (hypotenuse) length, or
    <math>
        <msup><mi>a</mi><mn>2</mn></msup>
        <mo>+</mo>
        <msup><mi>b</mi><mn>2</mn></msup>
        <mo>=</mo>
        <msup><mi>c</mi><mn>2</mn></msup>
        <mtext>.</mtext>
    </math>
    This was named after the ancient Greek mathematician Pythagoras. A <i>Pythagorean triple</i> is a triple of
    whole numbers that ${aVarAndCommaMl} ${bVarAndCommaMl} and ${cVarMl} can be. For example; 3, 4, and 5 is a
    Pythagorean triple since ${createPythagTripleMl(3, 4, 5)} 11, 60, and 61 is another one since
    ${createPythagTripleMl(11, 60, 61)}
    
    Once we know a Pythagorean triple, we can form another one by multiplying ${aVarAndCommaMl} ${bVarAndCommaMl}
    and ${cVarMl} by the same whole number > 1, Because of this, there are an infinite amount of Pythagorean
    triples. A Pythagorean triple is considered to be <i>primitive</i> if the GCD of ${aVarAndCommaMl}
    ${bVarAndCommaMl} and ${cVarMl} is 1. Therefore, a primitive triple can't be formed by taking another triple
    and multiplying ${aVarAndCommaMl} ${bVarAndCommaMl} and ${cVarMl} by the same whole number. The triples
    mentioned above; 3, 4, and 5, and 11, 60, and 61; are primitive. 6 (3 × 2), 8 (4 × 2), and 10 (5 × 2) is
    another triple. ${createPythagTripleMl(6, 8, 10)} 55 (11 × 5), 300 (60 × 5), and 305 (61 × 5) is another one.
    ${createPythagTripleMl(55, 300, 305)}
    
    The algorithm I came up with for calculating triples first tries to find triples where the short leg length
    equals the input number and then tries to find triples where the short leg equals the input number + 1, and
    so on until 10 are found.`;

const pythagTriplesHeadingStart = 'The first 10 Pythagorean triples where the lowest number in the triple is ≥';
const pythagTriplesActionSentenceEnding = 't' + pythagTriplesHeadingStart.substring(1) + ' that number';

/** @typedef {{ a: number, b: number, c: number, isPrimitive: boolean }} PythagoreanTriple */

/**
 * @param {PythagoreanTriple[]} triples
 * @param {string} inputString
 * @returns {HTMLElement[]}
 * An array with a heading and an ordered list. There's an item in this list for each triple. Each item shows
 * the numbers and squares of the triple. If the triple is primitive, then that'll be mentioned.
 */
function createPythagTriplesAnswerElements(triples, inputString) {
    const headingText = `${pythagTriplesHeadingStart} ${inputString} are:`;
    
    /**
     * @param {PythagoreanTriple}
     * @returns {MathMLElement}
     */
    const createTripleEl = ({ a, b, c, isPrimitive }) =>
        createMathElement(
            createNumAndSquareMrow(a),
            createMo('+'),
            createNumAndSquareMrow(b),
            createMo('='),
            createNumAndSquareMrow(c),
            isPrimitive ? createMtextWithMathFont('(primitive)', true) : ''
        );
    
    const triplesOl = arrToAnswerNormalOl(triples, createTripleEl);
    triplesOl.id = 'pythag-triples-ol';
    
    return [createNonBoldAnswerH3(headingText), triplesOl];
}

new SingleInputSection(
    {
        btnIdStart: 'pythag-triples',
        infoHtmlStringOrArr: pythagTriplesInfoHtml,
        actionSentenceEndingHtml: pythagTriplesActionSentenceEnding,
        minInput: 0,
        maxInput: 100,
        apiEndpointEnd: 'pythagorean-triples'
    },
    createPythagTriplesAnswerElements
);


const twoSquareTheoremInfoHtml =
    `Fermat's two square theorem says that every prime number that's 1 above a multiple of 4 can be expressed as
    the sum of 2 squares in 1 way. This was named after 1600s French mathematician Pierre de Fermat. In the
    context of this theorem, <i>square</i> is a shortening of <i>square number</i> or <i>perfect square</i> and
    is a number that can be formed by taking an integer and multiplying it by itself, or <i>squaring</i> it. The
    first 4 squares are 0 (0<sup>2</sup>), 1 (1<sup>2</sup> or (-1)<sup>2</sup>),
    4 (2<sup>2</sup> or (-2)<sup>2</sup>), and 9 (3<sup>2</sup> or (-3)<sup>2</sup>). Because of this theorem, a
    prime number that's 1 above a multiple of 4 is known as a <i>Pythagorean prime</i>. An example of a
    Pythagorean prime is 29 and it can be expressed as
    <math>
        ${createNumAndSquareMl(2)}
        <mo>+</mo>
        ${createNumAndSquareMl(5)}
        <mtext>.</mtext>
    </math>`;

/**
 * @param {{ pythagPrime: number, a: number, b: number }}
 * @param {string} inputString
 * @returns {HTMLElement[]}
 */
function createTwoSquareTheoremAnswerElements({ pythagPrime, a, b }, inputString) {
    const headingText = `The first Pythagorean prime ≥ ${inputString} is:`;
    const aAndBEl = createMathElement(createNumAndSquareMrow(a), createMo('+'), createNumAndSquareMrow(b));
    const answerDiv = createDiv(createNumStringWithCommas(pythagPrime), ', which is ', aAndBEl);
    answerDiv.id = 'two-square-theorem-answer-div';
    return [createNonBoldAnswerH3(headingText), answerDiv];
}

new SingleInputSection(
    {
        btnIdStart: 'two-square-theorem',
        infoHtmlStringOrArr: twoSquareTheoremInfoHtml,
        actionSentenceEndingHtml: 'the first Pythagorean prime ≥ that number, as well as the whole numbers whose squares sum to that prime',
        minInput: 0,
        maxInput: oneMillion,
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
    2, 1, 3, 4, 7, 11, 18, and 29. This sequence was named after 1800s French mathematician
    François Édouard Anatole Lucas.
    
    The <i>Golden Ratio</i> is an irrational number symbolized by the Greek letter ${phiLetter} (Phi).
    <math>
        <mi>${phiLetter}</mi>
        <mo>=</mo>
        <mfrac>
            <mrow><mn>1</mn><mo>+</mo><msqrt><mn>5</mn></msqrt></mrow>
            <mn>2</mn>
        </mfrac>
        <mo>≈</mo>
        <mn>${phiNumString}</mn>
        <mtext>.</mtext>
    </math>
    As we advance further and further into a Fibonacci-like sequence, the ratio between a number and the number
    before it gets closer and closer to ${phiLetter}. For example, recall that the first 8 numbers of the
    Fibonacci sequence are 1, 1, 2, 3, 5, 8, 13, and 21.
    <math>
        <mfrac><mn>2</mn><mn>1</mn></mfrac>
        <mo>=</mo>
        <mn>2</mn>
        <mtext>,</mtext>
    </math>
    <math>
        <mfrac><mn>8</mn><mn>5</mn></mfrac>
        <mo>=</mo>
        <mn>1.6</mn>
        <mtext>, and</mtext>
    </math>
    <math>
        <mfrac><mn>21</mn><mn>13</mn></mfrac>
        <mo>≈</mo>
        <mn>1.615384615384615</mn>
        <mtext>.</mtext>
    </math>
    
    The ratios that get calculated by this section are floating-point numbers most of the time, so these
    calculations are some of the few calculations done by the Number Theory Playground that involve numbers other
    than natural numbers.`;

const fiboLikeSequencesActionSentenceEndingHtml =
    `the first 20 numbers in the Fibonacci-like sequence that starts with those numbers, as well as the ratios
    between the 5<sup>th</sup> and 4<sup>th</sup>, 10<sup>th</sup> and 9<sup>th</sup>,
    15<sup>th</sup> and 14<sup>th</sup>, and 20<sup>th</sup> and 19<sup>th</sup> numbers in that sequence`;

/** @typedef {{ num1: number, num2: number, ratio: string, isRounded: boolean }} RatioData */

/**
 * @param {{ fiboLikeSequence: number[], ratiosData: RatioData[] }}
 * @param {string} input1String
 * @param {string} input2String
 * @returns {HTMLElement[]}
 * An array with a div that shows the Fibonacci-like sequence and another div that shows ratios between some
 * consecutive numbers in the sequence.
 */
function createFiboLikeSequencesAnswerElements({ fiboLikeSequence, ratiosData }, input1String, input2String) {
    const sequenceHeadingText =
        `The first ${fiboLikeSequence.length} numbers in the Fibonacci-like sequence that starts with
        ${input1String} and ${input2String} are:`;
    const sequenceOl = arrToAnswerFlexOl(fiboLikeSequence, createNumStringWithCommas);
    const sequenceDiv = createDiv(createNonBoldAnswerH3(sequenceHeadingText), sequenceOl);
    
    /**
     * @param {RatioData}
     * @returns {MathMLElement}
     */
    const createRatioEl = ({ num1, num2, ratio, isRounded }) =>
        createMathElement(
            createMathMlElement('mfrac', createMn(num1), createMn(num2)),
            createMo(isRounded ? '≈' : '='),
            createMn(ratio)
        );
    
    const ratiosHeading = createNonBoldAnswerH3('The ratios are:');
    const ratiosOl = arrToAnswerNormalOl(ratiosData, createRatioEl);
    const phiEl = createMathElement(createMi(phiLetter), createMo('≈'), createMn(phiNumString));
    ratiosOl.appendChild(createLi(phiEl));
    const ratiosDiv = createDiv(ratiosHeading, ratiosOl);
    ratiosDiv.id = 'fibo-like-sequence-ratios-div';
    
    return [sequenceDiv, ratiosDiv];
}

new DoubleInputSection(
    {
        btnIdStart: 'fibo-like-sequences',
        infoHtmlStringOrArr: fiboLikeSequencesInfoHtml,
        actionSentenceEndingHtml: fiboLikeSequencesActionSentenceEndingHtml,
        minInput: 1,
        maxInput: oneBillion,
        apiEndpointEnd: 'fibonacci-like-sequences-answer'
    },
    createFiboLikeSequencesAnswerElements
);


const ancientMultAlgorithmInfoStart =
    `The ancient Egyptians had an interesting algorithm for multiplication of 2 whole numbers. My way of
    explaining the algorithm goes like this:`;

const ancientMultStepsArr = [
    `Let variable ${aVarMl} represent one of the numbers and variable ${bVarMl} represent the other one.`,
    
    `Find all powers of 2 that are ≤ ${aVarAndPeriodMl} This could be done without modern multiplication by
    starting with 1, the 1<sup>st</sup> power of 2 or 2<sup>0</sup>, and finding the next power by adding the
    previous power to itself. This process will look like:
    <math>
        <mrow><mn>1</mn><mo>+</mo><mn>1</mn></mrow>
        <mo>=</mo>
        <mrow>
            <mn>2</mn>
            <mtext class="math-font">&nbsp;(</mtext>
            <msup><mn>2</mn><mn>1</mn></msup>
            <mtext class="math-font">)</mtext>
        </mrow>
        <mtext>,</mtext>
    </math>
    <math>
        <mrow><mn>2</mn><mo>+</mo><mn>2</mn></mrow>
        <mo>=</mo>
        <mrow>
            <mn>4</mn>
            <mtext class="math-font">&nbsp;(</mtext>
            <msup><mn>2</mn><mn>2</mn></msup>
            <mtext class="math-font">)</mtext>
        </mrow>
        <mtext>,</mtext>
    </math>
    <math>
        <mrow><mn>4</mn><mo>+</mo><mn>4</mn></mrow>
        <mo>=</mo>
        <mrow>
            <mn>8</mn>
            <mtext class="math-font">&nbsp;(</mtext>
            <msup><mn>2</mn><mn>3</mn></msup>
            <mtext class="math-font">)</mtext>
        </mrow>
        <mtext>,</mtext>
    </math>
    and so on until we find a power that's > ${aVarAndCommaMl} which we won't use.`,
    
    `Find the products of ${bVarMl} and these powers of 2. Like with the powers of 2, this could be done by
    starting with ${bVarMl} and finding the next product by adding the previous product to itself. If we let
    ${bVarMl} be 5, this process will look like:
    <math>
        <mrow><mn>5</mn><mo>+</mo><mn>5</mn></mrow>
        <mo>=</mo>
        <mrow>
            <mn>10</mn>
            <mtext class="math-font">&nbsp;(</mtext>
            <mn>5</mn>
            <mo>×</mo>
            <mn>2</mn>
            <mtext class="math-font">)</mtext>
        </mrow>
        <mtext>,</mtext>
    </math>
    <math>
        <mrow><mn>10</mn><mo>+</mo><mn>10</mn></mrow>
        <mo>=</mo>
        <mrow>
            <mn>20</mn>
            <mtext class="math-font">&nbsp;(</mtext>
            <mn>5</mn>
            <mo>×</mo>
            <mn>4</mn>
            <mtext class="math-font">)</mtext>
        </mrow>
        <mtext>,</mtext>
    </math>
    <math>
        <mrow><mn>20</mn><mo>+</mo><mn>20</mn></mrow>
        <mo>=</mo>
        <mrow>
            <mn>40</mn>
            <mtext class="math-font">&nbsp;(</mtext>
            <mn>5</mn>
            <mo>×</mo>
            <mn>8</mn>
            <mtext class="math-font">)</mtext>
        </mrow>
        <mtext>,</mtext>
    </math>
    and so on.`,
    
    `Find the powers of 2 that sum to ${aVarAndPeriodMl}`,
    
    `Add up the products of ${bVarMl} and these powers.`
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
        createDiv(ancientMultAlgorithmInfoStart),
        ancientMultStepsOl,
        createDiv(ancientMultResultSentence)
    );
ancientMultAlgorithmInfoDiv.className = 'div-with-info-and-ol';

const ancientMultExampleParagraphs =
    `Let's find the product of 5 and 12. Let's first use 5 for the number represented by ${aVarMl} in the
    algorithm above and 12 for ${bVarAndPeriodMl} The powers of 2 ≤ 5 are 1, 2, and 4. The products of 12 and
    these powers are 12, 24, and 48. The powers of 2 that sum to 5 are 1 and 4. The products of 12 and these
    powers are 12 and 48.
    <math>
        <mrow><mn>12</mn><mo>+</mo><mn>48</mn></mrow>
        <mo>=</mo>
        <mrow>
            <mrow><mo>(</mo><mn>12</mn><mo>×</mo><mn>1</mn><mo>)</mo></mrow>
            <mo>+</mo>
            <mrow><mo>(</mo><mn>12</mn><mo>×</mo><mn>4</mn><mo>)</mo></mrow>
        </mrow>
        <mo>=</mo>
        <mrow>
            <mn>12</mn>
            <mo>×</mo>
            <mrow><mo>(</mo><mn>1</mn><mo>+</mo><mn>4</mn><mo>)</mo></mrow>
        </mrow>
        <mo>=</mo>
        <mn>60</mn>
        <mtext>.</mtext>
    </math>
    
    Now let's use 12 for ${aVarMl} and 5 for ${bVarAndPeriodMl} The powers of 2 ≤ 12 are 1, 2, 4, and 8. The
    products of 5 and these powers are 5, 10, 20, and 40. The powers of 2 that sum to 12 are 4 and 8. The
    products of 5 and these powers are 20 and 40.
    <math>
        <mrow><mn>20</mn><mo>+</mo><mn>40</mn></mrow>
        <mo>=</mo>
        <mrow>
            <mrow><mo>(</mo><mn>5</mn><mo>×</mo><mn>4</mn><mo>)</mo></mrow>
            <mo>+</mo>
            <mrow><mo>(</mo><mn>5</mn><mo>×</mo><mn>8</mn><mo>)</mo></mrow>
        </mrow>
        <mo>=</mo>
        <mrow>
            <mn>5</mn>
            <mo>×</mo>
            <mrow><mo>(</mo><mn>4</mn><mo>+</mo><mn>8</mn><mo>)</mo></mrow>
        </mrow>
        <mo>=</mo>
        <mn>60</mn>
        <mtext>.</mtext>
    </math>`;

const ancientMultInfoElements =
    [ancientMultAlgorithmInfoDiv, ...createPsWithParagraphs(ancientMultExampleParagraphs)];

/** @typedef {{ powerOf2: string, correspondingMultiple: string }} AncientMultiplicationTableRow */

/**
 * @param {{ table1Rows: AncientMultiplicationTableRow[], table2Rows: AncientMultiplicationTableRow[], product: string }}
 * @param {string} input1String
 * @param {string} input2String
 * @returns {HTMLElement[]}
 * An array with a heading, a div with tables made using the data from the arrays in the param object, and a div
 * with text about the product of the 2 input numbers.
 */
function createAncientMultAnswerElements({ table1Rows, table2Rows, product }, input1String, input2String) {
    const mainHeadingText = `Ancient Egyptian Multiplication Info for ${input1String} and ${input2String}`;
    const correspondingMultiplesColHeading = `Corresponding Multiples of ${input2String}`;
    const table1ColHeadings = [`Powers of 2 ≤ ${input1String}`, correspondingMultiplesColHeading];
    const table2ColHeadings = [`Powers of 2 That Sum to ${input1String}`, correspondingMultiplesColHeading];
    
    /**
     * @param {AncientMultiplicationTableRow} row
     * @returns {string[]}
     */
    const getTableRowStrings = (row) => [row.powerOf2, row.correspondingMultiple];
    
    const tableDiv =
        createDiv(
            createTable(table1ColHeadings, table1Rows, getTableRowStrings),
            createTable(table2ColHeadings, table2Rows, getTableRowStrings)
        );
    tableDiv.id = 'ancient-mult-tables-div';
    
    const productSentenceDiv =
        createNarrowTextDiv(
            'The sum of the right column of the 2',
            createHtmlElement('sup', 'nd'),
            ` table is ${createNumStringWithCommas(product)}, which is the product of ${input1String} and ${input2String}.`
        );
    
    return [createH3(mainHeadingText), tableDiv, productSentenceDiv];
}

new DoubleInputSection(
    {
        btnIdStart: 'ancient-mult',
        infoHtmlStringOrArr: ancientMultInfoElements,
        actionSentenceEndingHtml: 'ancient Egyptian multiplication info for those numbers',
        minInput: 2,
        maxInput: oneBillion,
        apiEndpointEnd: 'ancient-multiplication-answer'
    },
    createAncientMultAnswerElements
);

