const http = require('http');
const mongoose = require('mongoose');
const Message = require('./models/message');
const express = require('express');

const server = express();

// TO DO make environment variables for the username and password
const uri = "mongodb+srv://yftach:y0504537324@cluster0.02ntz09.mongodb.net/DB0";

server.use(express.json());

mongoose.connect(uri, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(result => {
    console.log("Server is connected to DB and listening for requests");
    server.listen(3000);
  })
  .catch(err => console.log(err));

server.get("/", (req,res) => {
  
  message.save()
    .then((result) => {
      res.send(result);
    })
    .catch((err) => {
      console.log(err);
    });
  console.log("Request Received");
  console.log(req.body);
  res.send("gsrghbswrbsdbsebredrsfbedbedrfhbedrhb");

});

server.post('/',(req,res) => {
  console.log("Request Received");
  const message = new Message(req.body);
  message.save()
    .then((result) => {
        res.send(result);
        console.log(result);
    })
    .catch((err) => {
      console.log(err);
    })
});
