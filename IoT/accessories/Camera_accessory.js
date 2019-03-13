//var storage = require('node-persist');
var uuid = require('../').uuid;
var Service = require('../').Service;
var Accessory = require('../').Accessory;
var Camera = require('../').Camera;

console.log("HAP-NodeJS starting...");

// Initialize our storage system
//storage.initSync();

var CameraUUID = uuid.generate('hap-nodejs:accessories:Camera');

// Start by creating our Bridge which will host all loaded Accessories
var cameraAccessory = exports.accessory = new Accessory('Cat Camera', CameraUUID);

cameraAccessory.username = "1A:2B:3C:6D:5B:FF";
cameraAccessory.pincode = "031-45-155";

var cameraSource = new Camera();

cameraAccessory.configureCameraSource(cameraSource);


cameraAccessory.on('identify', function (paired, callback) {
    console.log("Cat Camera identify");
    callback(); // success
});

// Publish the camera on the local network.
cameraAccessory.publish({
    username: "E4:22:3D:R3:CE:CE",
    //port: 51062,
    pincode: "031-45-154",
    category: Accessory.Categories.CAMERA
}, true);
