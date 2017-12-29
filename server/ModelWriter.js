const json2csv = require('json2csv');
var fs = require('fs');
exports.save= function(data,callback){
  var fields = ['VideoName', 'ValueExtraversion','ValueAgreeableness','ValueConscientiousness','ValueNeurotisicm','ValueOpenness','gender','maxage'];

try {
var result = json2csv({ data: data, fields: fields });
fs.writeFile(__dirname+'/final_test.csv', result, function(err) {
  if (err) throw err;
  callback(false)
});
} catch (err) {
// Errors are thrown for bad options, or if the data is empty and no fields are provided.
// Be sure to provide fields if it is possible that your data array will be empty.
console.error(err);
callback(true);
}
}
