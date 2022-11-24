const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const messageSchema = new Schema({
    playerUUID: {
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
        required: true
    },
    dir: {
        type: Number,
        required: true
    },
    likes: {
        type: Number,
        required: true
    }
}, {timestamps: true});

const Message = mongoose.model('Message',messageSchema);
module.exports = Message;
