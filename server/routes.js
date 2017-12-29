const videoHtml = require("./showVid");
const clientProcessor =  require("./Client");
const fs = require("fs");

const modelLoader = require("./ModelLoader");


module.exports = function(upload,socketVid,socketChart,app){

  let model;
  modelLoader.load("./final_test.csv",(data)=>{
    model=data;
  });

  app.get('/index.html',(req,res)=>{
    //console.log(req);
    res.sendFile('upload.html', {root: __dirname })
  });

  app.post("/upload", function(req, res) {
    if(req.busboy) {
        req.busboy.on("file", function(fieldName, fileStream, fileName, encoding, mimeType) {
            res.json(fileName);
        });
        return req.pipe(req.busboy);
    }
    res.json("Failed");
  });

  app.get("/file",(req,res)=>{
    res.send(videoHtml(req.query.link));
    //console.log(req);
  });

  app.get('/video',(req,res)=>{

  });

  app.post('/fileFromMobile',(req,res)=>{
    //console.log(req);
    var fstream;
    req.pipe(req.busboy);
    req.busboy.on('file', function (fieldname, file, filename) {
        console.log("Uploading: " + filename);
        fstream = fs.createWriteStream(__dirname + '/uploads/' + filename);
        file.pipe(fstream);
        fstream.on('close', function () {
          console.log(" fs Done")
          socketVid.emit("video",filename);
          clientProcessor.process(socketChart,model,filename.split('.')[0],(result)=>{
            console.log(result);
            res.send(result);
          });
        });
    });
  });
}
