'use strict';

const express = require('express');
const router = express.Router();

const minInputNumbers = {
    primes: 0,
    twinPrimes: 0,
    primeFactorization: 2,
    divisibility: 2,
    gcdAndLcm: 2,
    goldbach: 4,
    pythagTriples: 0,
    twoSquare: 0,
    fiboSeq: 1,
    egyptMult: 2
}

const maxInputNumbers = {
    primes: 1_000_000_000,
    twinPrimes: 1_000_000_000,
    primeFactorization: 10_000,
    divisibility: 10_000,
    gcdAndLcm: 10_000,
    goldbach: 100_000,
    pythagTriples: 1_000,
    twoSquare: 1_000_000_000,
    fiboSeq: 1_000,
    egyptMult: 1_000
}

router.get('/', (req, res) => {
    const {section} = req.query;
    const minInputInt = minInputNumbers[section];
    const maxInputInt = maxInputNumbers[section];
    if (minInputInt === undefined || maxInputInt === undefined) {
        res.sendStatus(404);
        return;
    }

    // First check for sections that involve 2 input numbers
    if (['gcdAndLcm', 'fiboSeq', 'egyptMult'].includes(section)) {
        const firstNumber = Number(req.query.firstNumber);
        const secondNumber = Number(req.query.secondNumber);
        if (firstNumber < minInputInt || firstNumber > maxInputInt || !Number.isSafeInteger(firstNumber) ||
            secondNumber < minInputInt || secondNumber > maxInputInt || !Number.isSafeInteger(secondNumber)) {
                res.sendStatus(404);
        } else {
            switch (section) {
                case 'gcdAndLcm':
                    res.json({
                        euclideanInfo: getEuclideanInfo(firstNumber, secondNumber),
                        pfInfo: getGcdAndLcmInfoViaPf(firstNumber, secondNumber)
                    });
                    return;

                case 'fiboSeq':
                    res.json(getFiboSeqInfo(firstNumber, secondNumber));
                    return;

                case 'egyptMult':
                    res.json(getEgyptMultInfo(firstNumber, secondNumber));
                    return;
            }
        }
    } else {
        // Sections that involve 1 input number
        const number = Number(req.query.number);
        if (number < minInputInt || number > maxInputInt || !Number.isSafeInteger(number)) {
            res.sendStatus(404);
        } else {
            switch (section) {
                case 'primes':
                    res.json(getPrimesAfter(number));
                    return;

                case 'twinPrimes':
                    res.json(getTwinPrimesAfter(number));
                    return;

                case 'primeFactorization':
                    res.json(getPfArrayWithStringsAndCommas(number));
                    return;

                case 'divisibility':
                    res.json({
                        tricksInfo: getDivisInfoViaTricks(number),
                        pfInfo: getDivisInfoViaPf(number),
                    });
                    return;

                case 'goldbach':
                    res.json(getGoldbachPrimePairs(number));
                    return;

                case 'pythagTriples':
                    res.json(getPythagTriplesAfter(number));
                    return;

                case 'twoSquare':
                    res.json(getTwoSquareInfo(number));
                    return;

                default:
                    res.sendStatus(404);
            }
        }
    }
});

module.exports = router;

/**
 * 
 * @param {number} number 
 * @returns {boolean}
 */
function isPrime(number) {
    // All numbers less than 2 are not prime
    if (number < 2) {
        return false;
    }
    // 2 and 3 are prime
    if (number <= 3) {
        return true;
    }

    // Check if the argument number is divisible by any prime numbers less than or equal to the
    // floor of the square root of the argument number. If it is, then the argument number is not prime.
    // All prime numbers besides 2 and 3 are either 1 below or 1 above a multiple of 6.

    if (number % 2 === 0 || number % 3 === 0) {
        return false;
    }

    const highestNumberToCheck = Math.floor(Math.sqrt(number));
    // potentialPrime will iterate through numbers that are either 1 below or 1 above a multiple of 6
    for (let potentialPrime = 5; ; potentialPrime += (potentialPrime % 6 === 1 ? 4 : 2)) {
        if (potentialPrime > highestNumberToCheck) {
            return true;
        }
        if (number % potentialPrime === 0) {
            return false;
        }
    }
}

/**
 * Intended to be used to insert commas into numbers. Will not throw an exception if a string that contains non-number characters
 * is provided as an argument.
 * @param {number | string} number 
 * @returns {string} A string of the argument number that has commas where appropriate.
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
 * @param {number} number
 *  @returns An array of the first 30 prime numbers that are greater than or equal to the argument number.
 * Each prime number is a string that has commas where appropriate.
 */
function getPrimesAfter(number) {
    if (typeof number !== 'number' || !Number.isSafeInteger(number) || number < 0 || number > 1_000_000_000) {
        console.error('invalid argument:', number);
        return;
    }

    const primes = [];

    // All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6. This algorithm takes
    // advantage of this.

    if (number <= 2) primes.push('2');
    if (number <= 3) primes.push('3');

    let potentialPrime;
    if (number <= 5) {
        // set potentialPrime to the first positive number that is 1 less than a multiple of 6
        potentialPrime = 5;
    } else {
        // set potentialPrime to the first number greater than or equal to the argument number that is either 1 below or 1 above a multiple of 6
        potentialPrime = number;
        while (potentialPrime % 6 !== 1 && potentialPrime % 6 !== 5) {
            potentialPrime++;
        }
    }

    // potentialPrime will iterate between numbers that are either 1 above or 1 below a multiple of 6
    for (;; potentialPrime += (potentialPrime % 6 === 1 ? 4 : 2)) {
        if (isPrime(potentialPrime)) {
            primes.push(getNumberStringWithCommas(potentialPrime));
            if (primes.length === 30) {
                return primes;
            }
        }
    }
}

/**
 * @param {number} number 
 * @returns An array of strings that say the first 20 pairs of twin prime numbers that appear after the argument number.
 * Each string consists of the 2 numbers with commas where appropriate and each number is separated by " and ".
 */
function getTwinPrimesAfter(number) {
    if (typeof number !== 'number' || !Number.isSafeInteger(number) || number < 0 || number > 1_000_000_000) {
        console.error('invalid argument:', number);
        return;
    }

    const twinPrimes = [];

    // All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6. This means
    // that all prime number pairs besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and the other
    // number is 1 above that same multiple of 6. This algorithm takes advantage of this.

    if (number <= 3) {
        twinPrimes.push('3 and 5');
    }
    let potentialPrime1;
    if (number <= 5) {
        // Set potentialPrime1 to the first positive number that is 1 below a multiple of 6
        potentialPrime1 = 5;
    } else {
        // Set potentialPrime1 to the first number greater than or equal to the argument number that is 1 below
        // a multiple of 6
        potentialPrime1 = number;
        while (potentialPrime1 % 6 !== 5) {
            potentialPrime1++;
        }
    }

    // Check pairs of numbers that are potentially twin prime pairs until 20 twin prime pairs are found
    for (;; potentialPrime1 += 6) {
        const potentialPrime2 = potentialPrime1 + 2;
        if (isPrime(potentialPrime1) && isPrime(potentialPrime2)) {
            twinPrimes.push(
                `${getNumberStringWithCommas(potentialPrime1)} and ` +
                getNumberStringWithCommas(potentialPrime2)
            );
            if (twinPrimes.length === 20) {
                return twinPrimes;
            }
        }
    }
}

class PrimeFactorization {
    #number;
    #pfMap;

    /**
     * 
     * @param {number | Map} arg
     */
    constructor(arg) {
        if (typeof arg === 'number') {
            // constructor for if number is provided
            if (arg < 2 || !Number.isSafeInteger(arg)) {
                console.error('number argument must be an integer greater than or equal to 2');
                return;
            }
            this.#number = arg;
            let numberToChange = arg;
            const pfMap = new Map();

            // Find all the prime factors and their powers and put these in pfObject. Divide the numberToChange variable by each
            // prime factor that is found. When the numberToChange variable becomes 1, the entire prime factorization
            // has been found. All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so first
            // 2 and 3 will be checked to see if they're prime factors and then numbers that are either 1 above or 1 below
            // a multiple of 6 will be checked.

            /**
             * Checks if potentialPrimeFactor is a prime factor and if it is, finds it's power in the prime factorization and modifies appropriate
             * variables.
             * @param {number} potentialPrimeFactor
             */
            function checkIfPrimeFactor(potentialPrimeFactor) {
                if (numberToChange % potentialPrimeFactor === 0) {
                    let power = 0;
                    do {
                        power++;
                        numberToChange /= potentialPrimeFactor;
                    } while (numberToChange % potentialPrimeFactor === 0);
                    pfMap.set(potentialPrimeFactor, power);
                }
            }

            checkIfPrimeFactor(2);
            checkIfPrimeFactor(3);
            for (let potentialPrimeFactor = 5; numberToChange > 1; potentialPrimeFactor += (potentialPrimeFactor % 6 === 1 ? 4 : 2)) {
                checkIfPrimeFactor(potentialPrimeFactor);
            }

            this.#pfMap = pfMap;
    
        } else if (arg instanceof Map) {
            // constructor for if map is provided
            this.#pfMap = arg;
            // this.#number = Array.from(this.#pfMap).reduce((product, primeFactorAndPower) => {
            //     const primeFactor = primeFactorAndPower[0];
            //     const power = primeFactorAndPower[1];
            //     return product * (primeFactor ** power);
            //     },
            //     0
            //     )
            this.#number = 1;
            for (const [primeFactor, power] of this.#pfMap) {
                this.#number *= primeFactor ** power;
            }
        } else {
            console.error('Invalid argument:', arg);
        }
    }

    /**
     * 
     * @param {PrimeFactorization} pf1 
     * @param {PrimeFactorization} pf2 
     */
    static getGcdAndLcmPfs(pf1, pf2) {
        // The prime factorization of the GCD of 2 numbers contains all the prime factors that are in both of the prime
        // factorizations of the 2 numbers. The power of each prime factor is the minimum of the 2 powers in the 2
        // prime factorizations. If there are no common prime factors then the GCD is 1.

        // The prime factorization of the LCM of 2 numbers contains all prime factors that are in either of the prime
        // factorizations for the 2 numbers. If a prime factor is in both prime factorizations then the power of that
        // prime factor in the LCM prime factorization is the max of the 2 powers in the 2 prime factorizations. If a
        // prime factor is unique to 1 of the prime factorizations of the 2 numbers, then the power of that prime factor
        // in the LCM prime factorization is the same as in the prime factorization for that 1 number.

        // const gcdPfMap = new Map(Array.from(pf1.#pfMap).filter(primeFactorAndPower => !pf2.#pfMap.has(primeFactorAndPower[0])))
        const gcdPfMap = new Map();
        let lcmPfMap = new Map();
        for (const [primeFactor, power1] of pf1.#pfMap) {
            const power2 = pf2.#pfMap.get(primeFactor);
            if (power2) {
                // common prime factors
                gcdPfMap.set(primeFactor, Math.min(power1, power2));
                lcmPfMap.set(primeFactor, Math.max(power1, power2));
            } else {
                // prime factor is unique to pf1
                lcmPfMap.set(primeFactor, power1);
            }
        }

        // find prime factors unique to pf2
        for (const [primeFactor, power2] of pf2.#pfMap) {
            if (!pf1.#pfMap.has(primeFactor)) {
                lcmPfMap.set(primeFactor, power2);
            }
        }

        // The keys of lcmPfMap might be out of order so let's fix that

        /**
         * This is a function to be used for the argument of a sort function. Both arguments for this function are an array of 2 elements 
         * where the first element is a prime factor and the second element is the power of that prime factor in a prime factorization.
         */
        function primeFactorsAscendingSortFn(primeFactorAndPower1, primeFactorAndPower2) {
            return primeFactorAndPower1[0] - primeFactorAndPower2[0];
        }

        lcmPfMap = new Map(Array.from(lcmPfMap).sort(primeFactorsAscendingSortFn));
        // const lcmPrimeFactors = Array.from(lcmPfMap.keys());
        // const lcmPrimeFactors = [];
        // for (const primeFactor of lcmPfMap.keys()) {
        //     lcmPrimeFactors.push(primeFactor);
        // }
        // lcmPrimeFactors.sort((a, b) => a - b);
        // const orderedLcmPfMap = new Map(lcmPrimeFactors.map(factor => [factor, lcmPfMap.get(factor)]));
        // for (const primeFactor of lcmPrimeFactors) {
        //     orderedLcmPfMap.set(primeFactor, lcmPfMap.get(primeFactor));
        // }

        return {
            gcdPf: gcdPfMap.size === 0 ? null : new PrimeFactorization(gcdPfMap), 
            lcmPf: new PrimeFactorization(lcmPfMap)
        };
    }

    /**
     * @returns true if the number that this prime factorization represents is prime and false otherwise. If this is true,
     * then that means that the prime factorization contains 1 prime factor whose power is 1.
     */
    get isForAPrimeNumber() {
        return this.#pfMap.size === 1 && this.#pfMap.values().next().value === 1;
    }

    /**
     * @returns An array. Each element of this array is another array with 2 elements each. The first element is a prime factor
     * of this prime factorization. The second element is the power of that prime factor in this prime factorization. Both are
     * represented as strings with commas where appropriate.
     */
    get arrayWithStringsAndCommas() {
        // const array = [];
        // for (const primeFactorAndPower of this.#pfMap) {
        //     array.push(primeFactorAndPower.map(getNumberStringWithCommas));
        // }
        // return array;
        return Array.from(this.#pfMap).map(primeFactorAndPower => primeFactorAndPower.map(getNumberStringWithCommas));
    }

    /**
     * @returns The number that this prime factorization is for. This number is represented as a string with commas.
     */
    get numberAsStringWithCommas() {
        return getNumberStringWithCommas(this.#number);
    }

    get numberOfFactorsInfo() {
        let numberOfFactors = 1;
        let info = 'By looking at all the powers of the prime factors, we can see there are ';
        
        // The number of factors can be determined by taking all the powers of each prime factor, add 1 to each
        // of them, and then multiply them all together. The following code calculates this and makes a string
        // that shows how it was calculated.
        for (const power of this.#pfMap.values()) {
            numberOfFactors *= (power + 1);
            info += `(${power} + 1) x `;
        }
        // Remove last ' x ' of numberOfFactorsInfo
        info = info.substring(0, info.length - 3);
    
        info += ` = ${numberOfFactors} total factors. If 1 and ${this.numberAsStringWithCommas} are excluded, then there are ` +
            `${numberOfFactors - 2} factors.`;

        return info;
    }
}

function getPfArrayWithStringsAndCommas(number) {
    return new PrimeFactorization(number).arrayWithStringsAndCommas;
}

/**
 * @param {number} number 
 * @returns A string that contains divisibility info about the argument number.
 * @throws Something if the argument number is negative.
 */
function getDivisInfoViaTricks(number) {
    if (number < 0) {
        throw 'Argument number can\'t be negative';
    }

    const numberString = getNumberStringWithCommas(number);
    let info = '';
    const isEven = number % 2 === 0;
    if (!isEven) {
        info += `${numberString} is not even so it cannot be divisible by any even numbers. `;
    }

    // function getSumOfDigits(number) {
    //     let sum = 0;
    //     for (const digitStr of number.toString()) {
    //         sum += Number(digitStr);
    //     }
    //     return sum;
    // }

    const sumOfDigits = Array.from(number.toString()).reduce((sum, digitString) => sum + Number(digitString), 0);
    info += `The sum of the digits is ${sumOfDigits}. `;

    // A number is divisible by 3 if and only if the sum of it's digits is divisible by 3
    const isDivisibleBy3 = sumOfDigits % 3 === 0;
    if (isDivisibleBy3) {
        info += `${sumOfDigits} is divisible by 3 so ${numberString} is divisible by 3. `;

        // A number is divisible by 9 if and only if the sum of it's digits is divisible by 9
        if (sumOfDigits % 9 === 0) {
            info += `${sumOfDigits} is divisible by 9 so ${numberString} is divisible by 9. `;
        } else {
            info += `${sumOfDigits} is not divisible by 9 so ${numberString} is not divisible by 9. `;
        }
    } else {
        info += `${sumOfDigits} is not divisible by 3 so ${numberString} is not divisible by 3. This means that ${numberString} ` +
            'cannot be divisible by any multiples of 3. ';
    }

    if (isEven) {
        // A number is divisible by 6 if it's divisible by both 2 and 3
        if (isDivisibleBy3) {
            info += `${numberString} is even and divisible by 3 so it's also divisible by 6. `;
        }
        const last2Digits = number % 100;
        info += `The last 2 digits are ${last2Digits}. `;

        // A number is divisible by 4 if and only if the last 2 digits are divisible by 4
        if (last2Digits % 4 === 0) {
            info += `${last2Digits} is divisible by 4 so ${numberString} is divisible by 4. `;
            const last3Digits = number % 1000;
            info += `The last 3 digits are ${last3Digits}. `;

            // A number is divisible by 8 if and only if the last 3 digits are divisible by 8
            if (last3Digits % 8 === 0) {
                info += `${last3Digits} is divisible by 8 so ${numberString} is divisible by 8. `;
            } else {
                info += `${last3Digits} is not divisible by 8 so ${numberString} is not divisible by 8. `;
            }

            // A number is divisible by 12 if it's divisible by both 3 and 4
            if (isDivisibleBy3) {
                info += `${numberString} is divisible by 3 and 4 so it's also divisible by 12. `;
            }
        } else {
            info += `${last2Digits} is not divisible by 4 so ${numberString} is not divisible by 4. This means that ${numberString} ` +
                'cannot be divisible by any multiples of 4. ';
        }
    }
    return info.trimEnd();
}

/**
 * 
 * @param {number} number
 * @returns {object} An object. One entry has the key 'isPrime' and the value is a boolean value. The next entry has the value
 * 'numberOfFactorsInfo' and the value is a string that has this info. If the argument number is prime, then the object only has
 * these 2 entries. Otherwise, there is a property with the key 'pf' and the value for this is an object that represents the
 * the argument number's prime factorization. The next property has the key 'factors' and the value for this is an object. The
 * properties of this object have keys that are the factors of the argument number. The values for these are another object.
 * The first property has the key 'isPrime' and the value is a boolean value. If a factor is prime, then the 'isPrime' property 
 * is the only property. Otherwise, there is another property with the key 'pf' and the value for this is an object that
 * that represents the prime factorization of that factor.
 */
 function getDivisInfoViaPf(number) {
    const returnObject = {};
    const argNumberPf = new PrimeFactorization(number);

    returnObject.isPrime = argNumberPf.isForAPrimeNumber;
    if (argNumberPf.isForAPrimeNumber) {
        return returnObject;
    }
    
    returnObject.pf = argNumberPf.arrayWithStringsAndCommas;
    returnObject.numberOfFactorsInfo = argNumberPf.numberOfFactorsInfo;

    // Find all the factors and add their prime factorizations to info to show that they are
    // "sub-factorizations". Some examples of "sub-factorizations" are 2 and 2 * 3 in the prime factorization
    // 2 * 3 * 5.
    const highestPossibleFactor = number / 2;
    const factorsArray = [];
    for (let potentialFactor = 2; potentialFactor <= highestPossibleFactor; potentialFactor++) {
        if (number % potentialFactor === 0) {
            const factorPf = new PrimeFactorization(potentialFactor);
            const factorObject = {number: factorPf.numberAsStringWithCommas, isPrime: factorPf.isForAPrimeNumber};
            if (!factorPf.isForAPrimeNumber) {
                factorObject.pf = factorPf.arrayWithStringsAndCommas;
            }
            factorsArray.push(factorObject);
        }
    }
    returnObject.factors = factorsArray;
    return returnObject;
}

/**
 * 
 * @param {number} firstNumber 
 * @param {number} secondNumber
 * @returns An array containing objects about the Euclidean algorithm done on firstNumber and secondNumber. One of the entries for each object
 * has the key 'maxNumber' and the value is the max number for this iteration of the algorithm. Another entry has the key 'minNumber' and the 
 * value is the min number for this iteration. The last entry has the key 'remainder' and the value is the remainder for this iteration. The 
 * GCD is the 'minNumber' value of the last object in the array.
 */
function getEuclideanInfo(firstNumber, secondNumber) {
    if (firstNumber < 1 || secondNumber < 1) {
        throw 'Cannot do the Euclidean algorithm on a number less than 1';
    }
    
    const info = [];
    let maxNumber = Math.max(firstNumber, secondNumber);
    let maxNumberString = getNumberStringWithCommas(maxNumber);
    let minNumber = Math.min(firstNumber, secondNumber);
    let minNumberString = getNumberStringWithCommas(minNumber);
    let remainder = maxNumber % minNumber;
    let remainderString = getNumberStringWithCommas(remainder);

    while (true) {
        info.push({maxNumber: maxNumberString, minNumber: minNumberString, remainder: remainderString});
        if (remainder === 0) {
            return info;
        }
        maxNumber = minNumber;
        maxNumberString = minNumberString;
        minNumber = remainder;
        minNumberString = remainderString;
        remainder = maxNumber % minNumber;
        remainderString = getNumberStringWithCommas(remainder);
    }
}

/**
 * @param {number} firstNumber
 * @param {number} secondNumber
 * @return {object} An object. The first entry has the key 'firstNumberPf' and the value for this is an object that contains
 * the prime factorization of firstNumber. The second entry has the key 'secondNumberPf' and the value for this is an object
 * that contains the prime factorization of secondNumber. 
 * 
 * The third entry has the key 'gcd' and the value for this is an object.
 * The first entry of this object has the key 'number' and the value for this is what number the gcd is. If the gcd is 1, then
 * firstNumber and secondNumber have no common prime factors. For this situation, this will be the only entry in the gcd object.
 * If the gcd is not 1, the second entry of the gcd object will have the key 'isPrime' and the value for this will be a boolean 
 * value for whether or not the gcd is prime. If it's prime, then that means the prime factorization contains 1 prime factor whose
 * power is 1. For this situation, the gcd object will only have the entries whose keys are 'number' and 'isPrime'. Otherwise,
 * the gcd object will have a third entry with the key 'pf' and the value for this is an object that contains the prime factorization
 * of the gcd.
 * 
 * The fourth entry has the key 'lcm' and the value for this an object. The first entry for this object has the key 'number' and
 * the value is what number the lcm is. The second entry has the key 'isPrime' and the value for this is a boolean value for whether
 * or not the lcm is prime. If the lcm is prime (very unlikely), then that means that the prime factorization contains 1 prime factor
 * whose power is 1. For this situation, this object will only have the entries whose keys are 'number' and 'isPrime. Otherwise, 
 * there is a third entry with the key 'pf' and the value for this is an object that contains the prime factorization of the lcm.
 * 
 * @throws something if firstNumber is less than 2 or secondNumber is less than 2.
 */
function getGcdAndLcmInfoViaPf(firstNumber, secondNumber) {
    const returnObject = {};

    const pf1 = new PrimeFactorization(firstNumber);
    returnObject.firstNumberPf = pf1.arrayWithStringsAndCommas;

    const pf2 = new PrimeFactorization(secondNumber);
    returnObject.secondNumberPf = pf2.arrayWithStringsAndCommas;

    const {gcdPf, lcmPf} = PrimeFactorization.getGcdAndLcmPfs(pf1, pf2);

    if (gcdPf === null) {
        returnObject.gcd = {number: '1'};
    } else {
        returnObject.gcd = {number: gcdPf.numberAsStringWithCommas, isPrime: gcdPf.isForAPrimeNumber};
        if (!gcdPf.isForAPrimeNumber) {
            returnObject.gcd.pf = gcdPf.arrayWithStringsAndCommas;
        }
    }

    returnObject.lcm = {number: lcmPf.numberAsStringWithCommas, isPrime: lcmPf.isForAPrimeNumber};
    if (!lcmPf.isForAPrimeNumber) {
        returnObject.lcm.pf = lcmPf.arrayWithStringsAndCommas;
    }

    return returnObject;
}

/**
 *
 * @param {number} number 
 * @returns An array of strings that say the prime number pairs that sum up to the argument number. Each string contains
 * the 2 numbers separated by " and ".
 * @throws Something if the argument number is not even or is less than 4.
 */
function getGoldbachPrimePairs(number) {
    if (number % 2 !== 0 || number < 4) {
        throw 'Cannot find Goldbach pairs of a number that is not even or is less than 4';
    }
    
    const pairs = [];

    // All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6. First, check if the argument
    // number is 4 since 4 is the only even number greater than or equal to 4 that has 2 in a pair of prime numbers
    // that sum to it.
    if (number === 4) {
        pairs.push('2 and 2');
        return pairs;
    }

    // Check if 3 and a prime number sum to the number
    if (isPrime(number - 3)) {
        pairs.push(`3 and ${getNumberStringWithCommas(number - 3)}`);
    }

    // Check if a potential prime number and the difference between that potential prime number and the argument
    // number are both prime.

    // The max potential prime number that needs to be checked is equal to the floor of half of the argument number.
    // This is because after that point, calculations for primality will be done on numbers that have already been
    // determined to be pairs of prime numbers that sum to the argument number.

    const upperBound = number / 2;
    for (let potentialPrime1 = 5; potentialPrime1 <= upperBound; potentialPrime1 += (potentialPrime1 % 6 === 1 ? 4 : 2)) {
        const potentialPrime2 = number - potentialPrime1;
        if (isPrime(potentialPrime1) && isPrime(potentialPrime2)) {
            pairs.push([potentialPrime1, potentialPrime2].map(getNumberStringWithCommas).join(' and '));
        }
    }
    return pairs;
}

/**
 * 
 * @param {number} number 
 * @returns An array of strings that say the first 10 Pythagorean triples that appear after the argument number. 
 * Each string consists of what the 3 numbers are along with their squares in parentheses.
 */
function getPythagTriplesAfter(number) {
    const triples = [];

    // sideLength1 represents one of the lengths of one of the two short sides of a right triangle.
    // 3 is the lowest number to be in a Pythagorean triple so make sideLength be at least that.
    let sideLength1 = Math.max(number, 3);
    // sidelength2 represents the length of the other short side. It will always be greater than sideLength1.
    let sideLength2 = sideLength1 + 1;

    while (true) {
        const hypotLength = Math.hypot(sideLength1, sideLength2);
        if (hypotLength < sideLength2 + 1) {
            // sideLength2 + 1 is the minimum possible integer value for the hypotenuse length.
            // If the hypotenuse length is less than this, then the max value for otherSideLength for the current
            // value of sideLength1 has been exceeded.
            sideLength1++;
            sideLength2 = sideLength1 + 1;
            // sideLength2 = ++sideLength1;
        } else {
            if (Number.isInteger(hypotLength)) {
                // A Pythagorean triple has been found
                const numbersInTriple = [sideLength1, sideLength2, hypotLength];
                triples.push(
                    {
                        numbers: numbersInTriple.map(getNumberStringWithCommas), 
                        squares: numbersInTriple.map(number => number * number).map(getNumberStringWithCommas)
                    }
                );
                if (triples.length === 10) {
                    return triples;
                }                
            }
            sideLength2++;
        }
    }
}

/**
 * 
 * @param {number} number
 * @returns {object} An object and 1 entry of this object has the key 'primeNumber', which is the first prime number
 * greater than or equal to the argument number that is 1 above a multiple of 4. Another entry of this object has the key
 * 'numbers' and the value for this is an array of 2 numbers and the sum of the squares of these 2 numbers equals 'primeNumber'. 
 */
function getTwoSquareInfo(number) {
    let primeNumber = number;
    while (primeNumber % 4 !== 1) {
        primeNumber++;
    }
    while (!isPrime(primeNumber)) {
        primeNumber += 4;
    }
    // primeNumber is equal to the first prime number greater than or equal to the argument number that is 1 above a multiple of 4
    for (let firstNumber = 1; firstNumber < primeNumber; firstNumber++) {
        const firstNumberSquare = firstNumber * firstNumber;
        const secondNumberSquare = primeNumber - firstNumberSquare;
        const secondNumber = Math.sqrt(secondNumberSquare);
        if (Number.isInteger(secondNumber)) {
            return {
                primeNumber: getNumberStringWithCommas(primeNumber), 
                numbers: [firstNumber, secondNumber].map(getNumberStringWithCommas),
                squares: [firstNumberSquare, secondNumberSquare].map(getNumberStringWithCommas)
            };
        }
    }

    // This code shouldn't be reached. If it has then it means I made an error.
    throw 'no number found';
}

/**
 * 
 * @param {number} firstNumber 
 * @param {number} secondNumber 
 * @returns An object that consists of 2 entries. One of the entries has the key 'sequence' and the value is an array with 20 numbers. 
 * The first 2 numbers are firstNumber and secondNumber (each is 1 if no argument is provided) and each following term is the sum of the 
 * 2 previous terms. Each number is represented as a string with commas where appropriate. 
 * 
 * The second entry has the key 'endRatio' and the value for it is the ratio of the last number in the sequence to the second to last number
 * in the sequence. This should be close to the golden ratio (~1.618).
 */
function getFiboSeqInfo(firstNumber, secondNumber) {
    const fiboSeqArray = [firstNumber, secondNumber];
    for (let i = 0; i < 18; i++) {
        fiboSeqArray.push(fiboSeqArray[i] + fiboSeqArray[i + 1]);
    }

    return {
        sequence: fiboSeqArray.map(getNumberStringWithCommas),
        endRatio: fiboSeqArray[fiboSeqArray.length - 1] / fiboSeqArray[fiboSeqArray.length - 2]
    };
}

/**
 * Does the algorithm for multiplication used by ancient Egyptians and returns info obtained from the algorithm. The product obtained 
 * by using this algorithm is asserted to be correct.
 * 
 * @param {number} firstNumber 
 * @param {number} secondNumber 
 * @returns An object. Two of the entries have the keys 'minNumber' and 'maxNumber' and the values are the min and max of firstNumber and 
 * secondNumber respectively. Another entry has the key 'allPowersOf2LessThanMinNumber' and the value is an array that contains those powers
 * of 2. Another entry has the key 'correspondingMaxNumberMultiples' and the value is an array that contains the multiples of max number for 
 * those powers of 2. Another entry has the key 'powersOf2ThatSum' and the value is an array that contains the powers of 2 that sum to minNumber.
 * Another entry has the key 'maxNumberMultiplesThatSumToProduct' and the value is an array that contains the elements of 
 * 'correspondingMaxNumberMultiples' that sum to form the product. The final entry has the key 'product' and the value is that.
 * 
 */
function getEgyptMultInfo(firstNumber, secondNumber) {
    const minNumber = Math.min(firstNumber, secondNumber);
    const maxNumber = Math.max(firstNumber, secondNumber);

    const powersOf2 = {all: [1], sumToMinNumber: []};

    // make allPowersOf2 an array that contains all the powers of 2 less than or equal to minNumber
    // const allPowersOf2LessThanMinNumber = [1];
    while (true) {
        const nextPowerOf2 = powersOf2.all[powersOf2.all.length - 1] * 2;
        if (nextPowerOf2 > minNumber) break;
        powersOf2.all.push(nextPowerOf2);
    }

    const maxNumberMultiples = {all: powersOf2.all.map(power => power * maxNumber)};

    // const maxNumberMultiplesForAllPowersOf2 = allPowersOf2LessThanMinNumber.map(power => power * maxNumber);

    // make powersOf2ThatSum an array that contains the powers of 2 that sum to minNumber in ascending order
    // const powersOf2ThatSum = [];
    let changingMinNumber = minNumber;
    for (let i = powersOf2.all.length - 1; i >= 0; i--) {
        const powerOf2 = powersOf2.all[i];
        if (powerOf2 <= changingMinNumber) {
            powersOf2.sumToMinNumber.unshift(powerOf2);
            changingMinNumber -= powerOf2;
        }
    }

    maxNumberMultiples.sumToProduct = powersOf2.sumToMinNumber.map(power => power * maxNumber);

    // validate that algorithm is correct
    const correctProduct = firstNumber * secondNumber;
    const productFromAlgorithm = maxNumberMultiples.sumToProduct.reduce((sum, currentNumber) => sum + currentNumber);

    console.assert(
        productFromAlgorithm === correctProduct, 
        `Egyptian multiplication for ${minNumber} and ${maxNumber} resulted in product of ${productFromAlgorithm} \
        but should have been ${correctProduct}`
    );
    // if (correctProduct !== productFromAlgorithm) {
    //     throw 'algorithm produced incorrect product';
    // }

    // Convert numbers to strings with commas
    powersOf2.all = powersOf2.all.map(getNumberStringWithCommas);
    powersOf2.sumToMinNumber = powersOf2.sumToMinNumber.map(getNumberStringWithCommas);

    maxNumberMultiples.all = maxNumberMultiples.all.map(getNumberStringWithCommas);
    maxNumberMultiples.sumToProduct = maxNumberMultiples.sumToProduct.map(getNumberStringWithCommas);
    
    return {
        minNumber: getNumberStringWithCommas(minNumber),
        maxNumber: getNumberStringWithCommas(maxNumber),
        powersOf2,
        maxNumberMultiples,
        product: getNumberStringWithCommas(correctProduct)
    };
}
