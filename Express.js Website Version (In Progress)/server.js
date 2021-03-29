'use strict';

const express = require('express');
const app = express();
const port = 3000;

app.set('views', 'views');
app.set('view engine', 'pug');
app.use(express.static('resources'));

const cf = require('./calculationFunctions');

// cf.getPfArrayWithStringsAndCommas(10);
// cf.getGcdAndLcmInfoViaPf(4, 8);

const minInputNumbers = {
    primes: 0,
    twinPrimes: 0,
    primeFactorization: 2,
    divisibility: 2,
    gcdAndLcm: 2,
    goldbach: 4,
    pythagTriples: 0
}

const maxInputNumbers = {
    primes: 1_000_000_000,
    twinPrimes: 1_000_000_000,
    primeFactorization: 10_000,
    divisibility: 10_000,
    gcdAndLcm: 10_000,
    goldbach: 100_000,
    pythagTriples: 1_000
}

app.get('/', (req, res) => {
    res.render('ntp');
});

app.get('/calculations', (req, res) => {
    const {section} = req.query;
    const minInputInt = minInputNumbers[section];
    const maxInputInt = maxInputNumbers[section];
    if (minInputInt === undefined || maxInputInt === undefined) {
        res.sendStatus(404);
        return;
    }

    if (section === 'gcdAndLcm') {
        const firstNumber = Number(req.query.firstNumber);
        const secondNumber = Number(req.query.secondNumber);
        if (firstNumber < minInputInt || firstNumber > maxInputInt || !Number.isSafeInteger(firstNumber) ||
            secondNumber < minInputInt || secondNumber > maxInputInt || !Number.isSafeInteger(secondNumber)) {
                res.sendStatus(404);
        } else {
            res.json({
                euclideanInfo: cf.getEuclideanInfo(firstNumber, secondNumber),
                pfInfo: cf.getGcdAndLcmInfoViaPf(firstNumber, secondNumber)
            });
        }
    } else {
        const number = Number(req.query.number);
        if (number < minInputInt || number > maxInputInt || !Number.isSafeInteger(number)) {
            res.sendStatus(404);
        } else {
            switch (section) {
                case 'primes':
                    res.json(cf.getPrimesAfter(number));
                    return;

                case 'twinPrimes':
                    res.json(cf.getTwinPrimesAfter(number));
                    return;

                case 'primeFactorization':
                    res.json(cf.getPfArrayWithStringsAndCommas(number));
                    return;

                case 'divisibility':
                    res.json({
                        tricksInfo: cf.getDivisInfoViaTricks(number),
                        pfInfo: cf.getDivisInfoViaPf(number),
                    });
                    return;

                case 'goldbach':
                    res.json(cf.getGoldbachPrimePairs(number));
                    return;

                case 'pythagTriples':
                    res.json(cf.getPythagTriplesAfter(number));
                    return;

                default:
                    res.sendStatus(404);
            }
        }
    }
});

// app.get('/calculations/:sectionWithOneInput/number/:number([0-9]+)', (req, res) => {
//     const section = req.params.sectionWithOneInput;
//     const number = Number(req.params.number);
//     const minInputInt = minInputNumbers[section];
//     const maxInputInt = maxInputNumbers[section];

//     if (number < minInputInt || number > maxInputInt) {
//         res.sendStatus(404);
//         return;
//     }

//     switch (section) {
//         case 'primes':
//             res.json(cf.getPrimesAfter(number));
//             // res.json({primes: cf.getPrimesAfter(number)});
//             return;

//         case 'twinPrimes':
//             res.json(cf.getTwinPrimesAfter(number));
//             // res.json({twinPrimePairs: cf.getTwinPrimesAfter(number)});
//             return;

//         case 'primeFactorization':
//             res.json(cf.getPfObject(number));
//             return;

//         case 'divisibility':
//             res.json({
//                 tricksInfo: cf.getDivisInfoViaTricks(number),
//                 pfInfo: cf.getDivisInfoViaPf(number),
//             });
//             return;

//         case 'goldbach':
//             res.json(cf.getGoldbachPrimePairs(number));
//             // res.json({pairs: cf.getGoldbachPrimePairs(number)});
//             return;

//         case 'pythagTriples':
//             res.json(cf.getPythagTriplesAfter(number));
//             // res.json({pythagTriples: cf.getPythagTriplesAfter(number)});
//             return;

//         default:
//             res.sendStatus(404);
//     }
// });

// app.get('/calculations/:sectionWithTwoInputs/numbers/:firstNumber([0-9]+)/:secondNumber([0-9]+)', (req, res) => {
//     const section = req.params.sectionWithTwoInputs;
//     const firstNumber = Number(req.params.firstNumber);
//     const secondNumber = Number(req.params.secondNumber);
//     const minInputInt = minInputNumbers.gcdAndLcm;
//     const maxInputInt = maxInputNumbers.gcdAndLcm;
//     // Only section is gcd and lcm section
//     if (section !== 'gcdAndLcm' || firstNumber < minInputInt || firstNumber > maxInputInt || secondNumber < minInputInt || secondNumber > maxInputInt) {
//         res.sendStatus(404);
//     } else {
//         res.json({
//             euclideanInfo: cf.getEuclideanInfo(firstNumber, secondNumber),
//             pfInfo: cf.getGcdAndLcmInfoViaPf(firstNumber, secondNumber)    
//         });
//     }
// });

// app.get('/randomNumber/section/:section', (req, res) => {
//     const {section} = req.params;
//     // Have min random number be at least 0
//     const minRandomNumber = minInputNumbers[section];
//     const maxRandomNumber = maxInputNumbers[section];

//     if (minRandomNumber === undefined || maxRandomNumber === undefined) {
//         // invalid section since maxInputNumbers has keys for all sections
//         console.log('invalid section: ' + section);
//         res.sendStatus(404);
//         return;
//     }

//     // Send a string of a random int that is greater than or equal to minRandomNumber and less than or equal to maxRandomNumber.
//     // Based on function from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
//     res.send((Math.floor(Math.random() * (maxRandomNumber - minRandomNumber + 1) + minRandomNumber)).toString());
// });

app.listen(port, () => {
    console.log('App listening on port', port)
});