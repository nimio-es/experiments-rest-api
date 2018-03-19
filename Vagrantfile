Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"

  config.vm.provider "virtualbox" do |vb|
    vb.memory = "1250"  # this is sufficient in this moment
  end

  # if you want to export the server to the host machine
  config.vm.network "forwarded_port", guest: 8080, host: 8080, auto_correct: true

  config.vm.provision "shell", path: "vagrant-provision.sh", privileged: false
end
