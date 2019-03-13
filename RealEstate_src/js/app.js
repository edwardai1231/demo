App = {
  web3Provider: null,
  contracts: {},

  init: function() {
    // Load pets.
    $.getJSON('../house.json', function(data) {
      var Row = $('#Row');
      var Template = $('#Template');

      for (i = 0; i < data.length; i ++) {
        Template.find('.panel-title').text(data[i].name);
        Template.find('img').attr('src', data[i].picture);
        Template.find('.FloorPlan').text(data[i].FloorPlan);
        Template.find('.Price').text(data[i].Price);
        Template.find('.location').text(data[i].location);
        Template.find('.btn-buy').attr('data-id', data[i].id);

        Row.append(Template.html());
      }
    });

    return App.initWeb3();
  },

  initWeb3: function() {
  // Is there an injected web3 instance?
    if (typeof web3 !== 'undefined') {
      App.web3Provider = web3.currentProvider;
    } else {
  // If no injected web3 instance is detected, fall back to Ganache
      App.web3Provider = new Web3.providers.HttpProvider('http://localhost:7545');
    }
      web3 = new Web3(App.web3Provider);

        return App.initContract();
  },

  initContract: function() {
    $.getJSON('Adoption.json', function(data) {
  // Get the necessary contract artifact file and instantiate it with truffle-contract
    var Artifact = data;
    App.contracts.Adoption = TruffleContract(Artifact);

  // Set the provider for our contract
    App.contracts.Adoption.setProvider(App.web3Provider);

  // Use our contract to retrieve and mark the adopted pets
    return App.markbought();
});


    return App.bindEvents();
  },

  bindEvents: function() {
    $(document).on('click', '.btn-buy', App.handlebuy);
  },

  markbought: function(adopters, account) {
    var buyInstance;

  App.contracts.Adoption.deployed().then(function(instance) {
    buyInstance = instance;

    return buyInstance.getAdopters.call();
  }).then(function(adopters) {
    for (i = 0; i < adopters.length; i++) {
        if (adopters[i] !== '0x0000000000000000000000000000000000000000') {
        $('.panel-pet').eq(i).find('button').text('Success').attr('disabled', true);
      }
    }
}).catch(function(err) {
  console.log(err.message);
});

  },

  handlebuy: function(event) {
    event.preventDefault();

    var ID = parseInt($(event.target).data('id'));

    var buyInstance;

    web3.eth.getAccounts(function(error, accounts) {
      if (error) {
          console.log(error);
      }

    var account = accounts[0];

    App.contracts.Adoption.deployed().then(function(instance) {
     buyInstance = instance;

    // Execute adopt as a transaction by sending account
      return buyInstance.adopt(ID, {from: account});
    }).then(function(result) {
      return App.markAdopted();
    }).catch(function(err) {
      console.log(err.message);
  });
});
  }

};

$(function() {
  $(window).load(function() {
    App.init();
  });
});
