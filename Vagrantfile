# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"
VAGRANT_USER = "vagrant"

#A shell script to install docker and docker-compose 
$bootstrap = <<-'INSTALL_DOCKER'

apt-get remove -qq -y docker docker-engine docker.io containerd runc

 apt-get update -qq -y

 apt-get install -qq -y --force-yes \
 apt-transport-https=1.9.4ubuntu0.1 \
 ca-certificates=20190110ubuntu0.19.10.1 \
 curl=7.65.3-1ubuntu3.1 \
 gnupg-agent=2.2.12-1ubuntu3 \
 software-properties-common=0.98.5

 curl -fsSL https://download.docker.com/linux/ubuntu/gpg |  apt-key add -

 add-apt-repository \
 "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
 $(lsb_release -cs) \
 stable"
 
 apt-get update -qq -y

 apt-get install -qq -y docker-ce=5:19.03.12~3-0~ubuntu-eoan docker-ce-cli=5:19.03.12~3-0~ubuntu-eoan containerd.io

 gpasswd -a "$1" docker 

 sleep 5

 curl -fsSL "https://github.com/docker/compose/releases/download/1.26.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

 chmod +x /usr/local/bin/docker-compose

 curl -fsSL "https://raw.githubusercontent.com/docker/compose/$(docker-compose version --short)/contrib/completion/bash/docker-compose" -o /etc/bash_completion.d/docker-compose 

 service docker restart
 
INSTALL_DOCKER

#A shell script to bring spring boot app and database containers up in Vagrant VM
$docker_compose = <<-'RUN_DOCKER_COMPOSE'

 cd /vagrant/Wamya-backend-configuration && docker-compose up --build -d

RUN_DOCKER_COMPOSE

#Create the host VM volume to backup database
$create_database_volume = <<-'DATABASE_VOLUME'

mkdir -m 700 -p /postgres/database-data

DATABASE_VOLUME


Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  
  config.vm.box = "ubuntu/eoan64" 
  
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"
  end
  
  config.vm.provision "shell", inline: $bootstrap, args: VAGRANT_USER
  config.vm.provision "shell", inline: $create_database_volume
  config.vm.provision "shell", inline: $docker_compose, run: 'always'
  
  config.vm.synced_folder ".", "/vagrant", disabled: true
  config.vm.synced_folder "Wamya-backend-configuration", "/vagrant/Wamya-backend-configuration"
  
  config.vm.network "private_network", ip: "192.168.50.4"
  config.vm.network "forwarded_port", guest: 22, host: 2222
  config.vm.network "forwarded_port", guest: 5432, host: 5432
  config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.vm.network "forwarded_port", guest: 9090, host: 9090
  config.vm.network "forwarded_port", guest: 15672, host: 15672

end
