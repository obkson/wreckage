# How to compile whiteoak projects on Mac OSX

### Download Whiteoak

Whiteoak is not published to an online repository and must be downloaded manually from

[http://whiteoak.sourceforge.net/](http://whiteoak.sourceforge.net/)
, or
[https://sourceforge.net/projects/whiteoak/files/Whiteoak%20compiler](https://sourceforge.net/projects/whiteoak/files/Whiteoak%20compiler)

Unzip and put ```whiteoak-2.1.jar``` (comes pre-build with the download) in the ```lib``` folder


### Compile

The Whiteoak compiler requires OpenJDK 1.6, which is currently not available for Mac OSX.

The ```build.sh``` script thus sets up a vagrant box running linux, installs Open JDK 1.6 and then compiles the project in the virtual machine. Lastly, the class files are packaged into a JAR.

Thus, to compile ```whiteoaknative```, simply run the script

    $ ./build.sh

...and go get some coffee. ☕️

This should produce ```target/whiteoak-2.1/whiteoaknative_2.1-0.1.jar```.