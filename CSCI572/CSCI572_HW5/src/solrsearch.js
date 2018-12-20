var SolrNode = require('solr-node');
var client = new SolrNode({
    host: 'localhost',
    port: '8983',
    core: 'nypost',
    protocol: 'http'
});
const express = require('express');
const csv = require('csvtojson')
const app = express();
const request = require('request');
//app.use(cors());
app.set('json spaces', 40);
app.use(express.static(__dirname + '/public'));
let HashMap = require('hashmap');
let urlToHtmlMap = new HashMap();
var fs = require('fs');

var SpellCorrector = require('spelling-corrector');
var spellCorrector = new SpellCorrector();
// you need to do this step only one time to load the Dictionary
spellCorrector.loadDictionary('/Users/mukesh/Office/Tools/Solr/solr-7.5.0/big.txt');

app.use('/api/snippet/', (req, res) => {
    let fileName = '/Users/mukesh/Office/Tools/Solr/solr-7.5.0/DOM/' + req.query.file;
    console.log('getting ..file ', fileName);
    try {
        var data = fs.readFileSync(fileName, 'utf8');
        console.log(data.toString());
        res.send({ data: data.toString() });
    } catch (e) {
        console.log('Error:', e.stack);
        data.toString(e.stack);
    }

});

app.use('/api/spellcorrect/', (req, res) => {
    let allWords = req.query.keyword.split(" ");
    let finalWordc;
    for (let x = 0; x < allWords.length; x++) {
        let sple = spellCorrector.correct(allWords[x]);
        if (sple)
            finalWordc = finalWordc ? finalWordc + ' ' + sple : sple;
    }
    res.send({ word: finalWordc })
});

app.use('/api/csvjson/', (req, res) => {

    let csvFilePath = '/Users/mukesh/Office/Tools/Solr/solr-7.5.0/nypost/URLtoHTML_nypost.csv';
    const csv = require('csvtojson')
    csv({ delimiter: [","] })
        .fromFile(csvFilePath)
        .then((jsonObj) => {
            console.log(jsonObj);
            res.send((jsonObj));
        })
});

app.use('/api/suggest/', (req, res) => {
    let url = 'http://localhost:8983/solr/nypost/suggest?q=' + req.query.keyword;
    console.log(url);
    request({ uri: url }, function (error, response, body) {
        if (!error && response.statusCode === 200) {
            res.json(JSON.parse(body));
        } else {
            res.json(error);
        }
    });
});

app.use('/api/getresult/', (req, res) => {

    let strQuery = client.query().q(req.query.keyword);
    console.log('rreq.query.sort', req.query.sort)
    if (req.query.sort && req.query.sort.length > 0) {
        strQuery.sort({ pageRankFile: 'desc' })
    }
    console.log('req.query.rows', req.query.rows)
    if (req.query.rows > 0) {
        strQuery.rows(req.query.rows)
    }

    client.search(strQuery, function (err, result) {
        if (err) {
            console.log(err);
            return;
        }
        console.log('Response:', result);
        let prefix = '/Users/mukesh/Office/Tools/Solr/solr-7.5.0/DOM/'
        try {
            result.response.docs.forEach(element => {
                let fileName = element.id.replace("/Users/mukesh/Office/Tools/Solr/solr-7.5.0/nypost/nypost/", "");
                fileName = prefix + fileName;
                console.log(fileName);
                var data = fs.readFileSync(fileName, 'utf8');
                let suggestResponse = data.toString();
                let matchedSubstring;
                let matced = suggestResponse.indexOf(req.query.keyword)
                if (matced && matced > 0) {
                    let min = suggestResponse.length > matced - 80 ? matced - 80 : 0;
                    let max = suggestResponse.length > matced + 80 ? matced + 80 : suggestResponse.length;
                    matced = suggestResponse.indexOf(req.query.keyword)
                    //suggestResponse = suggestResponse.substring(0, matced) + " " + req.query.keyword + " " + suggestResponse.substring(matced + req.query.keyword + 1);
                    matchedSubstring = '...' + suggestResponse.substring(min, max) + '...';
                    console.log('matchedSubstring 1', matchedSubstring);
                } else {
                    let allWords = req.query.keyword.split(" ");
                    for (let idx = 0; idx < allWords.length; idx++) {
                        let len = 160 / allWords.length
                        matced = suggestResponse.indexOf(allWords[idx]);
                        if (matced && matced > 0) {
                            let min = suggestResponse.length > matced - len ? matced - len : 0;
                            let max = suggestResponse.length > matced + len ? matced + len : suggestResponse.length;
                            matced = suggestResponse.indexOf(allWords[idx])
                          //  suggestResponse = suggestResponse.substring(0, matced) + " " + req.query.keyword + " " + suggestResponse.substring(matced + req.query.keyword.length + 1);
                            matchedSubstring += '...' + suggestResponse.substring(min, max) + '...';
                            if (matchedSubstring)
                            console.log('matchedSubstring 2', matchedSubstring);
                        }
                    }
                }
                element.stream_content_type[0] = matchedSubstring;
                console.log('matchedSubstring 2', element.stream_content_type[0]);
            })
        } catch (e) {
            console.log('Error:', e.stack);
            data.toString(e.stack);
        }
        res.send(JSON.stringify(result));
    });
});


app.listen(3000,
    function () {
        console.log('Example app listening on port 3000!')
    });