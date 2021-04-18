# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"
VAGRANT_USER = "vagrant"

#ENV['VAGRANT_EXPERIMENTAL'] = 'disks'

#A shell script to install docker and docker-compose 
$bootstrap = <<-'INSTALL_DOCKER'

 apt-get remove docker docker-engine docker.io containerd runc

 apt-get update -qq -y

 apt-get install -qq -y --allow-unauthenticated \
 apt-transport-https \
 ca-certificates \
 curl \
 gnupg \
 lsb-release

 curl -fsSL https://download.docker.com/linux/ubuntu/gpg |  gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
 
 echo \
  "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
  
 apt-get update -qq -y

 apt-get install -qq -y docker-ce docker-ce-cli containerd.io

 gpasswd -a "$1" docker 

 sleep 5

 curl -fsSL "https://github.com/docker/compose/releases/download/1.28.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

 chmod +x /usr/local/bin/docker-compose

 curl -fsSL "https://raw.githubusercontent.com/docker/compose/$(docker-compose version --short)/contrib/completion/bash/docker-compose" -o /etc/bash_completion.d/docker-compose 

 service docker restart
 
INSTALL_DOCKER

#A shell script to bring spring boot app and database containers up in Vagrant VM
$docker_compose = <<-'RUN_DOCKER_COMPOSE'

 cd /vagrant/Messaging-gateway && docker-compose up --build -d && cd /vagrant/Wamya-backend && docker-compose up --build -d

RUN_DOCKER_COMPOSE

#Create the host VM volume to backup database
$create_database_volumes = <<-'DATABASE_VOLUME'

mkdir -m 700 -p /postgres/database-data

mkdir -m 700 -p /postgres/auth-database-data

DATABASE_VOLUME


Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.disksize.size = '50GB'
  config.vm.define "dev_env" do |dev|
		dev.vm.box = "ubuntu/groovy64"
		dev.vm.boot_timeout = 600
	  	dev.vm.provider "virtualbox" do |vb|
	  	  vb.memory = "5120"
	  	  vb.customize ["modifyvm", :id, "--natdnsproxy1", "on"]
	  	end
	  	
	  	dev.vm.provision "shell", inline: $bootstrap, args: VAGRANT_USER
		dev.vm.provision "shell", inline: $create_database_volumes
#		dev.vm.provision "shell", inline: $docker_compose, run: 'always'
  
#		dev.vm.synced_folder ".", "/vagrant", disabled:true
#		dev.vm.synced_folder "Wamya-backend-configuration", "/vagrant/Wamya-backend-configuration"		
#		dev.vm.synced_folder "Messaging-gateway", "/vagrant/Messaging-gateway"
#		dev.vm.synced_folder "Wamya-spring-cloud", "/vagrant/Wamya-spring-cloud"


		dev.vm.network "private_network", ip: "192.168.50.4"
		dev.vm.network "forwarded_port", guest: 22, host: 2222
		
# POSTGRES WAMYA DB	PORT	
		dev.vm.network "forwarded_port", guest: 5432, host: 5432
		
# POSTGRES AUTH DB PORT	
		dev.vm.network "forwarded_port", guest: 5433, host: 5433
		
# GATEWAY PORT
		dev.vm.network "forwarded_port", guest: 8443, host: 8443
# WAMYA WEB PORT
		dev.vm.network "forwarded_port", guest: 8080, host: 8080
		
# WAMYA WEB DEBUG PORT		
		dev.vm.network "forwarded_port", guest: 9090, host: 9090

# MESSAGING-GATEWAY WEB PORT
		dev.vm.network "forwarded_port", guest: 8585, host: 8585

# MESSAGING-GATEWAY WEB DEBUG PORT
		dev.vm.network "forwarded_port", guest: 9595, host: 9595
		
# RABBIT MQ MANAGER PORT		
		dev.vm.network "forwarded_port", guest: 15672, host: 15672
		
# KANNEL ADMIN PORT
		dev.vm.network "forwarded_port", guest: 13000, host: 13000
		
# KANNEL SMS BOX PORT
		dev.vm.network "forwarded_port", guest: 13013, host: 13013
		
# MAILHOG WEB PORT
		dev.vm.network "forwarded_port", guest: 8025, host: 8025
		

	end
end
