require('dotenv').config()
const mongoose = require('mongoose')
const Message = require('./models/message')
const express = require('express')

const server = express()
const PORT = 3000
const uri = "mongodb+srv://" + process.env.DATABASE_USERNAME + ":" + process.env.DATABASE_PASSWORD + "@cluster0.02ntz09.mongodb.net/DB0"
const messages_route = "/messages"
server.use(express.json())

mongoose.connect(uri, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(result => {
    console.log("Server is connected to DB and listening for requests")
    server.listen(PORT)
  })
  .catch(err => {
    console.log(err)
  })

server.get(messages_route, (req,res) => {
  console.log("GET request received")
  Message.find()
    .then(result => {
      //console.log(result);
      res.send(result)
    })
    .catch((err) => {
      console.log(err)
    })
})

server.post(messages_route,(req,res) => {
  console.log("POST Request Received")
  const message = new Message(req.body)
  message.save()
    .then((result) => {
        res.send(result);
       //console.log(result);
    })
    .catch((err) => {
      console.log(err)
    })
})

server.put(messages_route, (req, res) => {
  console.log("PUT Request Received")
  Message.findByIdAndUpdate(JSON.parse(JSON.stringify(req.body))['_id'], req.body, {new: true})
    .then((result) => {
      res.send(result);
      console.log(result);
    })
    .catch((err) => {
      console.log(err);
    })
})

server.delete(messages_route + "/:id", (req, res) => {
  console.log("DELETE Request Received")
  const id = req.params.id
  Message.deleteOne({ _id: id}).then((result) => {
    res.send(result)
  })
  .catch((err) => {
    console.log(err)
  })
})
