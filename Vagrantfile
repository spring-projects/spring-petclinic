Vagrant.configure("2") do |config|
  # Use the ubuntu/bionic64 box
  config.vm.box = "ubuntu/bionic64"
  config.vm.boot_timeout = 600000

  # Configuring VirtualBox provider settings
  config.vm.provider "virtualbox" do |vb|
    vb.name = "MyVB"
    vb.cpus = 2
    vb.memory = "2048"
  end

  # Configuring VirtualBox network settings
  config.vm.network "forwarded_port", guest: 8080, host: 8087

  # Configuring VirtualBox folder settings
  config.vm.synced_folder ".", "/home/vagrant/petclinic"

  # Provisioning with a shell script to run the JAR file
  config.vm.provision "shell", inline: <<-SHELL
    apt update
    apt install -y openjdk-17-jdk git unzip

    APP_DIR="/home/vagrant/petclinic"
    REPO_URL="https://github.com/yiting68/spring-petclinic.git"
    JAR_NAME="spring-petclinic-3.4.0.jar"
    SERVICE_NAME="petclinic"

    # Clone Spring PetClinic project if does not exist
    if [ ! -d "$APP_DIR/.git" ]; then
      git clone "$REPO_URL" "$APP_DIR"
    # Pull latest changes if project already exists
    else 
      cd "$APP_DIR"
      git pull
    fi  

    # Navigate to the project directory
    cd "$APP_DIR"

    # Build the JAR file
    ./gradlew build

    # Run the JAR file
    nohup java -jar build/libs/$JAR_NAME > $APP_DIR/app.log 2>&1 &
  SHELL
end
