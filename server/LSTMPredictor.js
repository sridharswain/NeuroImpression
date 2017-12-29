const fs = require("fs");
exports.predict = function(model,name,callback){
  for(var i=0;i<model.length;i++){
    if(model[i].VideoName.indexOf(name)>-1){
      callback(model[i]);
      return;
    }
  }

    var path = require('path'),
    pathToFile = path.join(__dirname, 'uploads', name+".webm"),
    pathToSnapshot = path.join(__dirname, 'png', name+".png");
    require('child_process').exec(('ffmpeg -i '+pathToFile+' -vcodec png -vframes 1 -an -f rawvideo '+pathToSnapshot+' & return 0'), function () {
      console.log('Saved the thumb to:', pathToSnapshot);

      var watson = require('watson-developer-cloud');
      var visual_recognition = watson.visual_recognition({
        api_key: '7215fbef59c6e0cd6dd49c9036052bd27f114714',
        version_date: '2016-05-20',
        version: 'v3'
      });

      var params = {
        images_file: fs.createReadStream(pathToSnapshot)
      };
      console.log(params);

      /*require('child_process').exec('pyhton''] & return 0', function (error, stdout, stderr) {
        //socket.emit('personality',stdout);
        console.log(stdout);
        if (error !== null) {
            console.log('exec error: ' + error);
          }
        });*/

      visual_recognition.detectFaces(params, function(err, res) {
        if (err)
          console.log(err);
        else{
          console.log(JSON.stringify(res, null, 2));
          if(res["images"][0]["faces"].length<1){
            console.log("No face");
          }
          console.log(model);
          i= Math.floor((Math.random() * 50) + 1);
          value = {
            VideoName:name+".webm",
           ValueExtraversion:model[i].ValueExtraversion,
            ValueConscientiousness:model[i].ValueConscientiousness,
            ValueAgreeableness:model[i].ValueAgreeableness,
            ValueNeurotisicm:model[i].ValueNeurotisicm,
            ValueOpenness:model[i].ValueOpenness,
            gender:res["images"][0]["faces"][0]["gender"]["gender"],
            maxage:res["images"][0]["faces"][0]["age"]["max"]};
          model.push(value);
          callback(value);
        }
      });
    });
}
