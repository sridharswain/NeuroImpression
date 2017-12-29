"use strict"
const predictor = require("./LSTMPredictor");
const cacheWriter = require("./ModelWriter");


exports.process = function(socket,model,name,callback){
  predictor.predict(model,name,(value)=>{
    socket.emit('data',value);
    cacheWriter.save(model,(isErr)=>{
      callback(value);
    });
  });
}
