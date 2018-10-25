const express = require('express');
const app = express();
const port = 3000;

const http = require('http');

const Eureka = require('eureka-js-client').Eureka;
const client = new Eureka({
    instance: {
        app: 'service-b',
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        statusPageUrl: 'http://localhost:3000/info',
        healthCheckUrl: 'http://localhost:3000/healthcheck',
        homePageUrl: 'http://localhost:3000',
        port: {
          '$': 3000,
          '@enabled': 'true',
        },
        vipAddress: 'service-b',
        dataCenterInfo: {
          '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
          name: 'MyOwn',
        },
      },
      eureka: {
        host: 'localhost',
        port: 8761,
        servicePath: '/eureka/apps/'
      },
  });

client.on('started', () => console.log('Eureka Client started'));
client.on('registered', () => console.log('Eureka Client registered'));
client.on('deregistered', () => console.log('Eureka Client deregistered'));
client.on('heartbeat', () => console.log('Eureka Client heartbeat'));
client.on('registryUpdated', () => console.log('Eureka Client registryUpdated'));

app.get('/', (req, res) => res.send('Handled by Node service'));
app.get('/info', (req, res) => res.send('Information'));
app.get('/healthcheck', (req, res) => res.send('Alive!'));
app.get('/stop', (req, res) => {
  res.send('Stopping'); 
  client.stop();
});

app.listen(port, () => {
    console.log(`Listening on port ${port}!`);
    // Make service discoverable
    client.start();
});

/* 
 * Using discovery to call another service 
 */
app.get('/call-a', (req,res) => {
  const instances = client.getInstancesByAppId('service-a');
  console.log('Instances: ' + instances.length);

  http.get(instances[0].homePageUrl, (resp) => {
    let data = '';
  
    // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
      data += chunk;
    });
  
    // The whole response has been received. Print out the result.
    resp.on('end', () => {
      console.log(data);
      res.send("From Java service: " + data);
    });
  
  }).on("error", (err) => {
    console.log("Error: " + err.message);
  });
});
