"use strict"
const express = require("express");
const busboy = require("connect-busboy");
const connect = require("connect");
const fileUpload = require('express-fileupload');
var bb = require('express-busboy');
var isDashBoardReady = false;
var count=0;

var multer = require('multer');
var bodyParser = require('body-parser');

/*require('child_process').exec('python3 personality.py ['+0.5+','+0.2+','+0.5+','+0.7+','+0.8+'] & return 0', function (error, stdout, stderr) {
  //socket.emit('personality',stdout);
  console.log(stdout);
  if (error !== null) {
      console.log('exec error: ' + error);
    }
  });*/

var fs = require("fs"),
    http = require("http"),
    url = require("url"),
    path = require("path");

var Storage = multer.diskStorage({
     destination: function(req, file, callback) {
         callback(null, __dirname+"/uploads/");
     },
     filename: function(req, file, callback) {
         callback(null, file.fieldname + "_" + Date.now() + "_" + file.originalname);
     }
 });
 var upload = multer({
     storage: Storage
 }).array("imgUploader", 3);

//DASHBOARD
var dashboard = express();
var http = http.createServer(dashboard)
var io = require('socket.io').listen(http);

/*dashboard.get('/socket.io/socket.io.js',(req,res)=>{
  res.sendFile(__dirname+'/node_modules/socket.io-client/dist/socket.io.js');
});*/


dashboard.get('/video',(req,res)=>{
  setTimeout(function(){
    var file = __dirname + '/uploads/'+req.query.name;//path.resolve(__dirname,"/uploads/video.mp4");
    fs.stat(file, function(err, stats) {
      if (err) {
        if (err.code === 'ENOENT') {
          // 404 Error if file not found
          return res.sendStatus(404);
        }
      res.end(err);
      }
      var range = req.headers.range;
      if (!range) {
       // 416 Wrong range
       return res.sendStatus(416);
      }
      var positions = range.replace(/bytes=/, "").split("-");
      var start = parseInt(positions[0], 10);
      var total = stats.size;
      var end = positions[1] ? parseInt(positions[1], 10) : total - 1;
      var chunksize = (end - start) + 1;

      res.writeHead(206, {
        "Content-Range": "bytes " + start + "-" + end + "/" + total,
        "Accept-Ranges": "bytes",
        "Content-Length": chunksize,
        "Content-Type": "video/webm"
      });

      var stream = fs.createReadStream(file, { start: start, end: end })
        .on("open", function() {
          stream.pipe(res);
        }).on("error", function(err) {
          res.end(err);
        });
    });
  },2000);
});

dashboard.get('/',(req,res)=>{
  //res.writeHead(200, { "Content-Type": "text/html" });
  res.sendFile(__dirname+'/showVid.html');
});

dashboard.get('/frame',(req,res)=>{
  res.sendFile(__dirname+'/frame.html');
});

dashboard.get('/chart',(req,res)=>{
  res.sendFile(__dirname+'/charts.html');
});

dashboard.get('/socket.io.js',(req,res)=>{
  res.sendFile(__dirname+'/node_modules/socket.io-client/dist/socket.io.js');
});

var sockets ={};
io.on('connection', function(socket){
  console.log('Connected to Dashboard');
  socket.on('disconnect', function(){
    console.log('Dashboard disconnected');
    isDashBoardReady = false;
  });
  //socket.emit('data','hello');

  var socketVid,socketChart;
  socket.on('vid',(msg)=>{
    socketVid = socket;
    sockets['1'] = socket;
    console.log(sockets);
  });

  socket.on('chart',(msg)=>{
    socketChart = socket;
    sockets['2']=socket;
    initClientHandler(sockets['1'],sockets['2']);
  });

  socket.on('exec',(msg)=>{
    require('child_process').exec('python3 personality.py ['+msg.ValueExtraversion+','+msg.ValueOpenness+','+msg.ValueAgreeableness+','+msg.ValueConscientiousness+','+msg.ValueNeurotisicm+'] & return 0', function (error, stdout, stderr) {
      socket.emit('personality',stdout);
      if (error !== null) {
          console.log('exec error: ' + error);
        }
      });
    });
});


http.listen(8080,()=>{
  console.log("Dashboard available at localhost:8080/");
});




var initClientHandler = function(socketVid,socketChart){
  //MOBILE CLIENTS

  const app = express();

  const port = process.env.PORT || 3030;
  app.use(busboy());
  app.use(bodyParser.urlencoded({
    extended: true
}));

/**bodyParser.json(options)
 * Parses the text as JSON and exposes the resulting object on req.body.
 */
app.use(bodyParser.json());
  //app.use(fileUpload());
  app.use(express.static(__dirname + '/public'));
  require("./routes")(upload,socketVid,socketChart,app);
  app.listen(port,()=>{
    console.log("Server started at "+port);
  });
}

//initClientHandler()
