sudo apt-get update
sudo apt-get install -y git
sudo apt-get install -y vim
sudo apt-get install -y openjdk-6-jdk
sudo apt-get install -y maven
git clone https://github.com/gcuisinier/jenv.git /home/vagrant/.jenv
echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> /home/vagrant/.profile
echo 'eval "$(jenv init -)"' >> /home/vagrant/.profile
# reload bash (log in or out or something) or "source ~/profile" to apply settings
# for now, just do the eval and use the full path to jenv
eval "$(/home/vagrant/.jenv/bin/jenv init -)"
/home/vagrant/.jenv/bin/jenv add /usr/lib/jvm/java-1.6.0-openjdk-amd64/
