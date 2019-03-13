//var storage = require('node-persist');
var uuid = require('../').uuid;
var Accessory = require('../').Accessory;
var Camera = require('../').Camera;
var Service = require('../').Service;

console.log("HAP-NodeJS starting...");

// Initialize our storage system
storage.initSync();

// Start by creating our Bridge which will host all loaded Accessories
var cameraAccessory = new Accessory('Cat Camera', uuid.generate("Cat Camera"));

var cameraSource = new Camera();

cameraAccessory.configureCameraSource(cameraSource);

cameraAccessory.on('identify', function(paired, callback) {
  console.log("Node Camera identify");
  callback(); // success
});

// Publish the camera on the local network.
cameraAccessory.publish({
  username: "EC:22:3D:D3:CE:8E",
  //port: 51062,
  pincode: "031-45-154",
  category: Accessory.Categories.CAMERA
}, true);
