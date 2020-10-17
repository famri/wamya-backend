# Wamya backend application and messaging gateway (sms and email) application 

This is the source code for wamya application and the messaging gateway (sms & email)


## Official website

* [Excentira IT](https://excentria-it.com)

## Getting started: How To ?

* clone the source code 
* download and install VirtualBox from [virtualbox.org](https://www.virtualbox.org/wiki/Downloads)
* download and install Vagrant on your development machine from [vagrantup.com](https://www.vagrantup.com/)
* open command line terminal in Wamya-backend folder 
* run "./gradlew build" to build the source code
* run "vagrant up" from command line terminal and wait for development environment set up
* if vagrant up succeded, run "vagrant ssh" into command line terminal while in Wamya-backend folder to log into the virtual machine that holds the development environment 
* once in the vagrant machine, go to /vagrant/Wamya-backend-configuration folder
* run "docker-compose stop <service name>" to stop any service from those defined in docker-compose.yml
* run "docker-compose build" to rebuild the services docker-compose.yml if code changes
* run "docker-compose start <service name>" to start any service from those defined in docker-compose.yml
* use [http://192.168.50.4:8080](http://192.168.50.4:8080) to query the Wamya-backend application
* use [http://localhost:15672/](http://localhost:15672/) to configure RabbitMQ broker
* use [http://192.168.50.4:13000/status?password=admin](http://192.168.50.4:13000/status?password=admin) to check the kannel sms gateway status

