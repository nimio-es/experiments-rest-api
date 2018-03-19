#!/bin/bash

# Adapted from https://github.com/ssledz/vagrant-boxes/blob/master/java-dev-environment/provision.sh

VAGRANT_DIR=/vagrant
HOME_DIR=~/

installPackage()
{
  local packages=$*
  echo "Installing $packages"
  sudo apt-get install -y $packages >/dev/null 2>&1
}

indent()
{
  echo -n '    '
}

installDockerCe() {
  indent; echo "Installing Docker-CE"
  indent; indent; installPackage apt-transport-https
  indent; indent; installPackage ca-certificates
  indent; indent; installPackage curl
  indent; indent; installPackage software-properties-common
  indent; indent; echo 'Aggregate apt source list'
  sudo touch /etc/apt/sources.list.d/docker.list >/dev/null 2>&1
  echo "deb [arch=amd64] https://download.docker.com/linux/ubuntu artful stable" | sudo tee -a /etc/apt/sources.list.d/docker.list
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add
  indent; indent; echo 'apt-get update'
  sudo apt-get update >/dev/null 2>&1
  indent; indent; installPackage docker-ce
  indent; indent; echo "Doing easy to use"
  sudo groupadd docker >/dev/null 2>&1
  sudo usermod -aG docker $USER >/dev/null 2>&1

  indent; echo "Install Docker-Compose"
  indent; indent; echo "TO BE DONE"

}

installZsh() {
  indent; echo "Install Zsh"
  indent; indent; installPackage zsh
  indent; indent; installPackage git-core
  indent; indent; echo 'Oh My Zsh'
  git clone git://github.com/robbyrussell/oh-my-zsh.git ~/.oh-my-zsh >/dev/null 2>&1
  cp ~/.oh-my-zsh/templates/zshrc.zsh-template ~/.zshrc >/dev/null 2>&1
  indent; indent; echo 'Changing shell'
  sudo chsh -s /bin/zsh vagrant > /dev/null 2>&1
  sed -i 's/ZSH_THEME="robbyrussell"/ZSH_THEME="half-life"/g' ~/.zshrc > /dev/null 2>&1
}

installOracle8Jdk() {
  indent; echo "Install JDK 8"
  indent; indent; installPackage python-software-properties
  indent; indent; installPackage debconf-utils
  indent; indent; echo 'Aggregate Oracle repository'
  sudo add-apt-repository ppa:webupd8team/java >/dev/null 2>&1
  sudo apt-get update >/dev/null 2>&1
  echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | sudo debconf-set-selections
  indent; indent; installPackage oracle-java8-installer
  indent; indent; installPackage oracle-java8-set-default
}

installTmux() {
  indent; echo 'Install and configure tmux'
  indent; indent; installPackage tmux
}

installUtils() {
  indent; echo "Install other utils"
  indent; indent; installPackage httpie
  indent; indent; installPackage python-pip
  indent; indent; echo Install http-jwt-auth
  indent; indent; pip install -U httpie-jwt-auth >/dev/null 2>&1
}

installPackages() {
  echo "Installing packages"
  indent; echo 'apt-get update'
  sudo apt-get update >/dev/null 2>&1
  installZsh
  installDockerCe
  installOracle8Jdk
  installTmux
  installUtils
}

run() {
  installPackages
  cd $VAGRANT_DIR
}


if [ ! -f "/var/vagrant_provision" ]; then
  sudo touch /var/vagrant_provision
  run
else
  echo "Nothing to do"
fi