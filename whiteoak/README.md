# How to Build Whiteoak Benchmarks on Mac OSX

The Whiteoak compiler requires OpenJDK 1.6, which is currently not available for MacOS X

Furthermore, whiteoak is not published to an online repository and need to be deployed locally.

This guide shows how to download and deploy whiteoak on mac osx, as well as compile the benchmarks using a virtual machine running linux with OpenJDK 1.6 installed.

## Prerequisites

* Maven
* Vagrant

## Deploy Whiteoak

Download Whiteoak from

[http://whiteoak.sourceforge.net/](http://whiteoak.sourceforge.net/)
, or rather
[https://sourceforge.net/projects/whiteoak/files/Whiteoak%20compiler](https://sourceforge.net/projects/whiteoak/files/Whiteoak%20compiler)

and unzip to get ```whiteoak-2.1.jar``` (comes pre-build with the download).

Create a folder that will be the root of whiteoak benchmarks

    $ mkdir /path/to/whiteoak

Place ```whiteoak-2.1.jar```, ```Vagrantfile``` and ```provision.sh``` in it
and ```cd``` into the folder.

    $ cd /path/to/whiteoak
    $ ls
    Vagrantfile
    provision.sh
    whiteoak-2.1.jar

Create a local maven repository.

    $ mkdir repo

Deploy Whiteoak to the local repository.

    $ mvn deploy:deploy-file -Durl=file://repo \
    						  -Dfile=whiteoak-2.1.jar \
    						  -DgroupId=net.sourceforge \
    						  -DartifactId=whiteoak \
    						  -Dpackaging=jar \
    						  -Dversion=2.1

## Generate benchmarks

Generate whiteoak benchmarks by running the benchmarking suite.
When creating the benchmarks, you will have to give the path to the local repository containing whiteoak.

##

vagrant ssh -c "cd /vagrant/lib && java -cp ../whiteoak-2.1.jar whiteoak.tools.openjdk.compiler.WocMain whiteoaknative.wo"


## Vagrant up

Start up the vagrant box

    $ vagrant up
    
...and drink some coffee while it runs the provisioning script... ☕️

Check that the virtual machine runs OpenJDK version 1.6

	$ vagrant ssh -c "java -version"
   
and optionally force the right version by creating a ```.java-version``` file in ```/path/to/whiteoak```

    $ echo "1.6" > .java-version
   
(This folder is mounted at ```/vagrant``` on the virtual machine, so the above command will create a file ```/vagrant/.java-version``` on the virtual machine.)


## Installing Benchmarks

Now, let's say you have generated a benchmark folder ```native_whiteoak_2_1``` in ```/path/to/whiteoak```. Instead of running

    $ cd native_whiteoak_2_1
    $ mvn clean install
    
the benchmark should be installed from the virtual machine, like so:

    $ vagrant ssh -c "cd /vagrant/native_whiteoak_2_1 && jenv exec mvn clean install"

Thereafter, the benchmarks can be run from your Mac OSX host with any compatible Java version

    $ cd native_whiteoak_2_1/target
    $ java -jar target/benchmarks.jar
    
 