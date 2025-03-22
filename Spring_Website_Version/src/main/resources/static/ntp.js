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
 */
const createBtn = (textContent) => createElement('button', textContent);

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
    /**
     * @param {string} heading 
     * @param {string} infoHtmlString 
     * @param {number} minInput 
     * @param {number} maxInput 
     * @param {boolean} isSingleInputSection 
     * @param {string} actionSentenceEnding 
     * @param {string} apiEndpoint 
     * @param {(responseObj: any, input1String: string, input2String: ?string) => HTMLElement[]} getElements
     * @param {boolean} needsEvenInput 
     */
    constructor(
        btnIdStart, infoHtmlString, minInput, maxInput, isSingleInputSection,
        actionSentenceEnding, apiEndpoint, getElements, needsEvenInput = false
    ) {
        this.minInput = minInput;
        this.maxInput = maxInput;
        this.isSingleInputSection = isSingleInputSection;
        this.apiEndpoint = apiEndpoint;
        this.getElements = getElements;
        this.needsEvenInput = needsEvenInput;

        const sectionBtn = getElementById(btnIdStart + 'Btn');
        const heading = sectionBtn.textContent;

        /**
         * @type {HTMLParagraphElement[]}
         */
        const infoPs =
            infoHtmlString
            .split('\n\n')
            .map(s => {
                const p = createElement('p');
                p.innerHTML = s;
                return p;
            });

        const directions =
            `Enter or generate ${isSingleInputSection ? 'an integer' : '2 integers'} and click the \
            Calculate button to ${actionSentenceEnding}. Have \
            ${isSingleInputSection ? 'this integer' : 'these integers'} be \
            ${needsEvenInput ? 'even && ' : ''}>= ${getNumberStringWithCommas(minInput)} && \
            <= ${getNumberStringWithCommas(maxInput)}. Commas are optional.`;

        function show() {
            curSection = this;
            sectionHeading.textContent = heading;
            sectionInfo.replaceChildren(infoPs);
            sectionDirections.textContent = directions;
            clearInputBoxesAndAnswerArea();
        }

        sectionBtn.onclick = show;
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
