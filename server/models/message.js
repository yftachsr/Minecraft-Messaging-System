const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const messageSchema = new Schema({
    playerName: {
        type: String,
        required: true
    },
    x: {
        type: Number,
        required: true
    },
    y: {
        type: Number,
        required: true
    },
    z: {
        type: Number,
        required: true
    },
    text: {
        type: String,
        required: false
    }
}, {timestamps: true});

const Message = mongoose.model('Message',messageSchema);
module.exports = Message;
