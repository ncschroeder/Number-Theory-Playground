'use strict';

const express = require('express');
const app = express();

app.set('views', 'views');
app.set('view engine', 'pug');
app.use(express.static('resources'));



app.get('/', (req, res) => {
    res.render('ntp');
});

const cf = require('./calculationFunctions');
app.use('/calculations', cf);



const port = 3000;
app.listen(port, () => {
    console.log('App listening on port', port)
});