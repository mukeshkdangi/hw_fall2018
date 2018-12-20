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
//app.use(cors());
app.set('json spaces', 40);
app.use(express.static(__dirname + '/public'));
let HashMap = require('hashmap');
let urlToHtmlMap = new HashMap();

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
        res.send(JSON.stringify(result));
    });
});
app.listen(3000,
    function () {
        console.log('Example app listening on port 3000!')
    });