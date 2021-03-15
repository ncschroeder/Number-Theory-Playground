'use strict';

// const express = require('express');
// const router = express.Router();

// router.get('/:sectionWithOneInput/number/:number', (req, res) => {
//     const section = req.params.sectionWithOneInput;
//     let {number} = req.params;
//     number = Number(number);
//     if (!Number.isInteger(number)) {
//         throw 'Invalid request';
//     }
//     switch (section) {
//         case 'primes':
//             // res.send(cf.getPrimesAfter(number));
//             res.json({primes: getPrimesAfter(number)});
//             return;
//         case 'twinPrimes':
//             res.json({twinPrimePairs: getTwinPrimesAfter(number)});
//             return;
//         case 'primeFactorization':
//             res.send(getPfString(number));
//             return;
//         case 'divisibility':
//             res.json({divisInfo: getDivisInfoViaTricks(number)});
//             return;
//         case 'goldbach':
//             res.json({pairs: getGoldbachPairs(number)});
//             return;
//         case 'pythagTriples':
//             res.json({pythagTriples: getPythagTriples(number)});
//     }
// });

// module.exports = router;


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
    for (let potentialPrime = 5; ; potentialPrime += 4) {
        for (let i = 0; i < 2; i++) {
            if (potentialPrime > highestNumberToCheck) {
                return true;
            }
            if (number % potentialPrime === 0) {
                return false;
            }
            if (i === 0) {
                potentialPrime += 2;
            }
        }
    }
}

/**
 * 
 * @param {(number | Map)} intOrMap 
 */
// function PrimeFactorization(intOrMap) {
//     // console.log(numberOrMap instanceof Map);
//     // if (number < 2) {
//     //     throw '\'number\' must be at least 2';
//     // }
//     let map, number;

//     this.getNumber = function() {
//         return number;
//     }

//     this.toMap = function() {
//         return map;
//     }

//     this.toString = function() {
//         let string = '';
//         for (let [primeFactor, power] of map) {
//             string += primeFactor;
//             if (power != 1) {
//                 string += '^' + power;
//             }
//             string += ' * ';
//         }
//         return string.substring(0, string.length - 3);
//     }

//     this.isForAPrimeNumber = function() {
//         if (map.size !== 1) {
//             return false;
//         }
//         for (let value of map.values()) {
//             return value === 1;
//         }
//     }

//     if (Number.isInteger(intOrMap)) {
//         // This block of code acts as the constructor for if an integer is passed in
//         map = new Map();
//         number = intOrMap;
//         // let doneCalculating = false;
//         for (let potentialPrimeFactor = 2; potentialPrimeFactor <= 3; potentialPrimeFactor++) {
//             if (intOrMap % potentialPrimeFactor === 0) {
//                 let power = 0;
//                 do {
//                     power++;
//                     intOrMap /= potentialPrimeFactor;
//                 } while (intOrMap % potentialPrimeFactor === 0);
//                 map.set(potentialPrimeFactor, power);
//                 if (intOrMap === 1) {
//                     return;
//                     // doneCalculating = true;
//                     // break;
//                 }
//             }
//         }

//         for (let potentialPrimeFactor = 5; ; potentialPrimeFactor += 4) {
//             // if (doneCalculating) {
//             //     break;
//             // }

//             for (let i = 0; i < 2; i++) {
//                 if (intOrMap % potentialPrimeFactor === 0) {
//                     let power = 0;
//                     do {
//                         power++;
//                         intOrMap /= potentialPrimeFactor;
//                     } while (intOrMap % potentialPrimeFactor === 0);
//                     map.set(potentialPrimeFactor, power);
//                     if (intOrMap === 1) {
//                         return;
//                         // doneCalculating = true;
//                         // break;
//                     }
//                 }

//                 if (i === 0) {
//                     potentialPrimeFactor += 2;
//                 }
//             }
//         }
//     } else if (intOrMap instanceof Map) {
//         // This block of code acts as the constructor for if a map is passed in
//         map = intOrMap;
//         number = 1;
//         for (let [primeFactor, power] of map) {
//             number *= Math.pow(primeFactor, power);
//         }
//     } else {
//         throw '\'numberOrMap\' is not a integer nor map';
//     }
// }

module.exports = {

    /**
     * 
     * @param {number} number
     * @returns An array of the first 30 prime numbers that are greater than or equal to the argument number.
     */
    getPrimesAfter: function(number) {
        const primes = [];

        // All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6. This algorithm takes
        // advantage of this.

        let potentialPrime;
        if (number <= 5) {
            // Set potentialPrime to the first positive number that is 1 below a multiple of 6.
            potentialPrime = 5;
            if (number <= 2) {
                primes.push(2);
            }
            if (number <= 3) {
                primes.push(3);
            }
        } else {
            switch (number % 6) {
                case 0:
                    // If the argument number is divisible by 6, check if the next number is prime and set potentialPrime
                    // to the next number that is 1 below a multiple of 6.
                    if (isPrime(number + 1)) {
                        primes.push(number + 1);
                    }
                    potentialPrime = number + 5;
                    break;

                case 1:
                    // If the argument number is 1 above a multiple of 6, check if this number is prime and set
                    // potentialPrime to the next number that is 1 below a multiple of 6.
                    if (isPrime(number)) {
                        primes.push(number);
                    }
                    potentialPrime = number + 4;
                    break;

                default:
                    // Set potentialPrime be the next number that is 1 below a multiple of 6
                    potentialPrime = number;
                    while ((potentialPrime + 1) % 6 !== 0) {
                        potentialPrime++;
                    }
                    break;
            }
        }

        // Check potential prime numbers for primality until 30 prime numbers are found
        for (;; potentialPrime += 4) {
            for (let i = 0; i < 2; i++) {
                if (isPrime(potentialPrime)) {
                    primes.push(potentialPrime);
                    if (primes.length === 30) {
                        return primes;
                    }
                }
                if (i === 0) {
                    potentialPrime += 2;
                }
            }
        }
    },

    /**
     * 
     * @param {number} number 
     * @returns An array of strings that say the first 20 pairs of twin prime numbers that appear after the argument number.
     * Each string consists of the 2 numbers separated by " and ".
     */
    getTwinPrimesAfter: function(number) {
        const twinPrimes = [];

        // All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6. This means
        // that all prime number pairs besides 3 and 5 consist of 1 number that is 1 below a multiple of 6 and the other
        // number is 1 above that same multiple of 6. This algorithm takes advantage of this.

        if (number <= 3) {
            twinPrimes.push('3 and 5');
        }
        let potentialPrime;
        if (number <= 5) {
            // Set potentialPrime to the first positive number that is 1 below a multiple of 6
            potentialPrime = 5;
        } else {
            // Set potentialPrime to the first number greater than or equal to the argument number that is 1 below
            // a multiple of 6
            potentialPrime = number;
            while ((potentialPrime + 1) % 6 != 0) {
                potentialPrime++;
            }
        }

        // Check pairs of numbers that are potentially twin prime pairs until 20 twin prime pairs are found
        for (;; potentialPrime += 6) {
            if (isPrime(potentialPrime) && isPrime(potentialPrime + 2)) {
                twinPrimes.push(`${potentialPrime} and ${potentialPrime + 2}`);
                if (twinPrimes.length === 20) {
                    return twinPrimes;
                }
            }
        }
    },

    /**
     * 
     * @param {number} number 
     * @returns An object of the prime factorization of the argument number. The keys are the prime factors and the values are the 
     * powers of those prime factors.
     */
    getPfObject: function(number) {
        const pfObject = {};

        // Find all the prime factors and their powers and put these in pfObject. Divide the number variable by each
        // prime factor that is found. When the number variable becomes 1, the entire prime factorization
        // has been found. All prime numbers besides 2 and 3 are either 1 above or 1 below a multiple of 6 so first
        // 2 and 3 will be checked to see if they're prime factors and then numbers that are either 1 above or 1 below
        // a multiple of 6 will be checked.

        for (let potentialPrimeFactor = 2; potentialPrimeFactor <= 3; potentialPrimeFactor++) {
            if (number % potentialPrimeFactor === 0) {
                let power = 0;
                do {
                    power++;
                    number /= potentialPrimeFactor;
                } while (number % potentialPrimeFactor === 0);
                pfObject[potentialPrimeFactor] = power;
                if (number === 1) {
                    return pfObject;
                }
            }
        }

        for (let potentialPrimeFactor = 5; ; potentialPrimeFactor += 4) {
            for (let i = 0; i < 2; i++) {
                if (number % potentialPrimeFactor === 0) {
                    let power = 0;
                    do {
                        power++;
                        number /= potentialPrimeFactor;
                    } while (number % potentialPrimeFactor === 0);
                    pfObject[potentialPrimeFactor] = power;
                    if (number === 1) {
                        return pfObject;
                    }
                }
                if (i === 0) {
                    potentialPrimeFactor += 2;
                }
            }
        }
    },

    /**
     * 
     * @param {number} number 
     * @returns A string that contains divisibility info about the argument number.
     * @throws Something if the argument number is negative.
     */
    getDivisInfoViaTricks: function(number) {
        if (number < 0) {
            throw 'Argument number can\'t be negative';
        }

        let info = '';
        const isEven = number % 2 === 0;
        if (!isEven) {
            info += `${number} is not even so it cannot be divisible by any even numbers. `;
        }

        function getSumOfDigits(number) {
            let sum = 0;
            for (const digitStr of number.toString()) {
                sum += Number(digitStr);
            }
            return sum;
        }

        const sumOfDigits = getSumOfDigits(number);
        info += `The sum of the digits is ${sumOfDigits}. `;

        // A number is divisible by 3 if and only if the sum of it's digits is divisible by 3
        const isDivisibleBy3 = sumOfDigits % 3 === 0;
        if (isDivisibleBy3) {
            info += `${sumOfDigits} is divisible by 3 so ${number} is divisible by 3. `;

            // A number is divisible by 9 if and only if the sum of it's digits is divisible by 9
            if (sumOfDigits % 9 === 0) {
                info += `${sumOfDigits} is divisible by 9 so ${number} is divisible by 9. `;
            } else {
                info += `${sumOfDigits} is not divisible by 9 so ${number} is not divisible by 9. `;
            }
        } else {
            info += `${sumOfDigits} is not divisible by 3 so ${number} is not divisible by 3. This means that ${number} ` +
                'cannot be divisible by any multiples of 3. ';
        }

        if (isEven) {
            // A number is divisible by 6 if it's divisible by both 2 and 3
            if (isDivisibleBy3) {
                info += `${number} is even and divisible by 3 so it's also divisible by 6. `;
            }
            const last2Digits = number % 100;
            info += `The last 2 digits are ${last2Digits}. `;

            // A number is divisible by 4 if and only if the last 2 digits are divisible by 4
            if (last2Digits % 4 === 0) {
                info += `${last2Digits} is divisible by 4 so ${number} is divisible by 4. `;
                const last3Digits = number % 1000;
                info += `The last 3 digits are ${last3Digits}. `;

                // A number is divisible by 8 if and only if the last 3 digits are divisible by 8
                if (last3Digits % 8 === 0) {
                    info += `${last3Digits} is divisible by 8 so ${number} is divisible by 8. `;
                } else {
                    info += `${last3Digits} is not divisible by 8 so ${number} is not divisible by 8. `;
                }

                // A number is divisible by 12 if it's divisible by both 3 and 4
                if (isDivisibleBy3) {
                    info += `${number} is divisible by 3 and 4 so it's also divisible by 12. `;
                }
            } else {
                info += `${last2Digits} is not divisible by 4 so ${number} is not divisible by 4. This means that ${number} ` +
                    'cannot be divisible by any multiples of 4. ';
            }
        }
        return info;
    },

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
    getDivisInfoViaPf: function(number) {
        const returnObject = {};
        // returnObject[number] = {};
        const inputNumberPfObject = this.getPfObject(number);
        // returnObject[number] = inputNumberPfObject;
        const pfPrimeFactors = Object.keys(inputNumberPfObject);

        if (pfPrimeFactors.length === 1 && inputNumberPfObject[pfPrimeFactors[0]] === 1) {
            // Input number is prime
            returnObject.isPrime = true;
            returnObject.numberOfFactorsInfo = `${number} is prime and doesn't have any factors other than itself and 1.`;
            return returnObject;
        }

        returnObject.isPrime = false;
        returnObject.pf = inputNumberPfObject;

        // The number of factors can be determined by taking all the powers of each prime factor, add 1 to each
        // of them, and then multiply them all together. The following code calculates this and makes a string
        // that shows how it was calculated.
        
        let numberOfFactorsInfo = 'By looking at all the powers of the prime factors, we can see there are ';
        let numberOfFactors = 1;
        for (const primeFactor in inputNumberPfObject) {
            const power = inputNumberPfObject[primeFactor];
            numberOfFactors *= (power + 1);
            numberOfFactorsInfo += `(${power} + 1) x `;
        }
        // Remove last ' x ' of numberOfFactorsInfo
        numberOfFactorsInfo = numberOfFactorsInfo.substring(0, numberOfFactorsInfo.length - 3);

        numberOfFactorsInfo += ` = ${numberOfFactors} total factors. If 1 and ${number} are excluded, then there are ` +
            `${numberOfFactors - 2} factors. By looking at all the "sub-factorizations, we can see the factors are:`;

        returnObject.numberOfFactorsInfo = numberOfFactorsInfo;

        // Find all the factors and add their prime factorizations to info to show that they are
        // "sub-factorizations". Some examples of "sub-factorizations" are 2 and 2 * 3 in the prime factorization
        // 2 * 3 * 5.
        const highestPossibleFactor = number / 2;
        const factorsObject = {};
        for (let potentialFactor = 2; potentialFactor <= highestPossibleFactor; potentialFactor++) {
            if (number % potentialFactor === 0) {
                const factorPfObject = this.getPfObject(potentialFactor);
                const factorPrimeFactors = Object.keys(factorPfObject);
                if (factorPrimeFactors.length === 1 && factorPfObject[factorPrimeFactors[0]] === 1) {
                    // potentialFactor is prime
                    factorsObject[potentialFactor] = {isPrime: true};
                } else {
                    factorsObject[potentialFactor] = {isPrime: false, pf: factorPfObject};
                }
            }
        }
        returnObject.factors = factorsObject;
        return returnObject;
    },

    /**
     * 
     * @param {number} firstNumber 
     * @param {number} secondNumber
     * @returns An array containing info about the Euclidean algorithm done on firstNumber and secondNumber. There are strings in
     * this list that contain info about every iteration and there is another string at the end about which number is the GCD.
     */
    getEuclideanInfo: function(firstNumber, secondNumber) {
        if (firstNumber < 1 || secondNumber < 1) {
            throw 'Cannot do the Euclidean algorithm on a number less than 1';
        }
        
        const info = [];
        let maxNumber = Math.max(firstNumber, secondNumber);
        let minNumber = Math.min(firstNumber, secondNumber);
        let remainder = maxNumber % minNumber;
        while (remainder !== 0) {
            info.push(`${maxNumber} is not divisible by ${minNumber} so we now find the GCD of ${minNumber} and ${remainder}`);
            maxNumber = minNumber;
            minNumber = remainder;
            remainder = maxNumber % minNumber;
        }
        info.push(`${maxNumber} is divisible by ${minNumber} so ${minNumber} is the GCD of ${minNumber} and ${maxNumber}`);
        if (maxNumber !== Math.max(firstNumber, secondNumber)) {
            info.push(`As a result, ${minNumber} is the GCD of ${firstNumber} and ${secondNumber}`);
        }
        return info;
    },

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
    getGcdAndLcmInfoViaPf: function(firstNumber, secondNumber) {
        const returnObject = {};

        const pfObject1 = this.getPfObject(firstNumber);
        returnObject.firstNumberPf = pfObject1;

        const pfObject2 = this.getPfObject(secondNumber);
        returnObject.secondNumberPf = pfObject2;

        const pf1PrimeFactors = Object.keys(pfObject1);
        const pf2PrimeFactors = Object.keys(pfObject2);
        const gcdPfObject = {};
        const lcmPfObject = {};

        // The prime factorization of the GCD of 2 numbers contains all the prime factors that are in both of the prime
        // factorizations of the 2 numbers. The power of each prime factor is the minimum of the 2 powers in the 2
        // prime factorizations. If there are no common prime factors then the GCD is 1.

        // The prime factorization of the LCM of 2 numbers contains all prime factors that are in either of the prime
        // factorizations for the 2 numbers. If a prime factor is in both prime factorizations then the power of that
        // prime factor in the LCM prime factorization is the max of the 2 powers in the 2 prime factorizations. If a
        // prime factor is unique to 1 of the prime factorizations of the 2 numbers, then the power of that prime factor
        // in the LCM prime factorization is the same as in the prime factorization for that 1 number.

        for (const primeFactor in pfObject1) {
            const power1 = pfObject1[primeFactor];
            if (pf2PrimeFactors.includes(primeFactor)) {
                const power2 = pfObject2[primeFactor];
                gcdPfObject[primeFactor] = Math.min(power1, power2);
                lcmPfObject[primeFactor] = Math.max(power1, power2);
            } else {
                lcmPfObject[primeFactor] = power1;
            }
        }

        // Find prime factors that are unique to the second prime factorization
        for (const primeFactor in pfObject2) {
            if (!pf1PrimeFactors.includes(primeFactor)) {
                const power = pfObject2[primeFactor];
                lcmPfObject[primeFactor] = power;
            }
        }

        /**
         * 
         * @param {object} pfObject An object representing a number's prime factorization. The keys are the prime factors and the
         * values are the powers those prime factors are raised to.
         * @returns The product of all prime factors raised to their powers, or the number whose prime factorization is in pfObject.
         */
        function getPfObjectProduct(pfObject) {
            let product = 1;
            for (let primeFactor in pfObject) {
                // primeFactor is a string so convert it to number
                primeFactor = Number(primeFactor);
                const power = pfObject[primeFactor];
                product *= primeFactor ** power;
            }
            return product;
        }

        const gcdPrimeFactors = Object.keys(gcdPfObject);
        if (gcdPrimeFactors.length === 0) {
            // no common prime factors
            returnObject.gcd = {number: 1};
        } else if (gcdPrimeFactors.length === 1 && gcdPfObject[gcdPrimeFactors[0]] === 1) {
            // gcd is prime
            returnObject.gcd = {number: gcdPrimeFactors[0], isPrime: true};
        } else {
            returnObject.gcd = {number: getPfObjectProduct(gcdPfObject), isPrime: false, pf: gcdPfObject};
        }

        const lcmPrimeFactors = Object.keys(lcmPfObject);
        if (lcmPrimeFactors.length === 1 && lcmPfObject[lcmPrimeFactors[0]] === 1) {
            // lcm is prime
            returnObject.lcm = {number: lcmPfObject[lcmPrimeFactors[0]], isPrime: true};
        } else {
            returnObject.lcm = {number: getPfObjectProduct(lcmPfObject), isPrime: false, pf: lcmPfObject};
        }

        return returnObject;
    },

    /**
     * 
     * @param {number} number 
     * @returns An array of strings that say the prime number pairs that sum up to the argument number. Each string contains
     * the 2 numbers separated by " and ".
     * @throws Something if the argument number is not even or is less than 4.
     */
    getGoldbachPrimePairs: function(number) {
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
            pairs.push('3 and ' + (number - 3));
        }

        // Check if a potential prime number and the difference between that potential prime number and the argument
        // number are both prime.

        // The max potential prime number that needs to be checked is equal to the floor of half of the argument number.
        // This is because after that point, calculations for primality will be done on numbers that have already been
        // determined to be pairs of prime numbers that sum to the argument number.

        const upperBound = number / 2;
        for (let potentialPrime = 5; ; potentialPrime += 4) {
            for (let i = 0; i < 2; i++) {
                if (potentialPrime > upperBound) {
                    return pairs;
                }
                const otherPotentialPrime = number - potentialPrime;
                if (isPrime(potentialPrime) && isPrime(otherPotentialPrime)) {
                    pairs.push(`${potentialPrime} and ${otherPotentialPrime}`);
                }

                if (i === 0) {
                    potentialPrime += 2;
                }
            }
        }
    },

    /**
     * 
     * @param {number} number 
     * @returns An array of strings that say the first 10 Pythagorean triples that appear after the argument number. 
     * Each string consists of what the 3 numbers are along with their squares in parentheses.
     */
    getPythagTriplesAfter: function(number) {
        const triples = [];

        // sideLength1 represents one of the lengths of one of the two short sides of a right triangle.
        // 3 is the lowest number to be in a Pythagorean triple so make sideLength be at least that.
        let sideLength1 = Math.max(number, 3);
        // sidelength2 represents the length of the other short side. It will always be greater than sideLength1.
        let sideLength2 = sideLength1 + 1;

        // /**
        //  * 
        //  * @param {number} firstSideLength 
        //  * @param {number} secondSideLength 
        //  * @returns The square root of firstLegLength^2 + secondLegLength^2
        //  */
        // const getHypotLength = (firstSideLength, secondSideLength) => 
        //     Math.sqrt(firstSideLength * firstSideLength + secondSideLength * secondSideLength);

        while (true) {
            const hypotLength = Math.hypot(sideLength1, sideLength2);
            if (hypotLength < sideLength2 + 1) {
                // sideLength2 + 1 is the minimum possible integer value for the hypotenuse length.
                // If the hypotenuse length is less than this, then the max value for otherSideLength for the current
                // value of sideLength1 has been exceeded.
                sideLength1++;
                sideLength2 = sideLength1 + 1;
            } else {
                if (Number.isInteger(hypotLength)) {
                    // A Pythagorean triple has been found. Add a string to the triples list. This string
                    // consists of what the 3 numbers are and what their squares are in parentheses.
                    // triples.push(
                    //     `${sideLength1}^2 (${sideLength1 * sideLength1}) + ${sideLength2}^2 (${sideLength2 * sideLength2}) = ${hypotLength}^2 (${hypotLength * hypotLength})`
                    // );
                    function square(number) {
                        return number * number;
                    }
                    const numbersInTriple = [sideLength1, sideLength2, hypotLength];
                    triples.push(
                        {numbers: numbersInTriple, squares: numbersInTriple.map(number => number * number)}
                        // [
                        //     [sideLength1, square(sideLength1)], 
                        //     [sideLength2, square(sideLength2)], 
                        //     [hypotLength, square(hypotLength)]
                        // ]
                    );
                    if (triples.length === 10) {
                        return triples;
                    }                
                }
                sideLength2++;
            }
        }
    },

    /**
     * 
     * @param {number} number
     * @returns {object} An object and 1 entry of this object has the key 'primeNumber', which is the first prime number
     * greater than or equal to the argument number that is 1 above a multiple of 4. Another entry of this object has the key
     * 'numbers' and the value for this is an array of 2 numbers and the sum of the squares of these 2 numbers equals 'primeNumber'. 
     */
    getTwoSquareInfo: function(number) {
        let primeNumber = number;
        while (primeNumber % 4 !== 1) {
            primeNumber++;
        }
        while (!isPrime(primeNumber)) {
            primeNumber += 4;
        }
        // primeNumber is equal to the first prime number greater than or equal to the argument number that is 1 above a multiple of 4
        let firstNumber = 1;
        while (firstNumber < primeNumber) {
            const firstNumberSquare = firstNumber * firstNumber;
            const secondNumber = Math.sqrt(primeNumber - firstNumberSquare);
            if (Number.isInteger(secondNumber)) {
                return {'primeNumber': primeNumber, numbers: [firstNumber, secondNumber]};
            }
            firstNumber++;
        }
        throw 'no number found';
    }
};