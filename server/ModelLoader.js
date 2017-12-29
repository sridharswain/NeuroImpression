"use strict"

exports.load = function(path,callback){
const csv=require('csvtojson')
var json = [];
csv()
.fromFile(path)
.on('json',(jsonObj)=>{
    json.push(jsonObj);
})
.on('done',(error)=>{
    callback(json);
    console.log('Model Loaded');
})
}
