const express = require('express');
const app = express();
const port = 3000;

const Eureka = require('eureka-js-client').Eureka;
const client = new Eureka({
    instance: {
        app: 'node-service',
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        statusPageUrl: 'http://localhost:3000/info',
        healthCheckUrl: 'http://localhost:3000/healthcheck',
        homePageUrl: 'http://localhost:3000',
        port: {
          '$': 3000,
          '@enabled': 'true',
        },
        vipAddress: 'node-service',
        dataCenterInfo: {
          '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
          name: 'MyOwn',
        },
      },
      eureka: {
        host: '192.168.143.44',
        port: 8761,
        servicePath: '/eureka/apps/'
      },
  });

client.on('started', () => console.log('Eureka Client started'));
client.on('registered', () => console.log('Eureka Client registered'));
client.on('deregistered', () => console.log('Eureka Client deregistered'));
client.on('heartbeat', () => console.log('Eureka Client heartbeat'));
client.on('registryUpdated', () => console.log('Eureka Client registryUpdated'));

app.get('/', (req, res) => res.send('Hello World!'));
app.get('/info', (req, res) => res.send('Info'));
app.get('/healthcheck', (req, res) => res.send('Alive!'));
app.get('/stop', (req, res) => {
  res.send('Stopping'); 
  client.stop();
});

app.listen(port, () => {
    console.log(`Example app listening on port ${port}!`);
    client.start();
});