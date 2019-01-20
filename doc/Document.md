# Document for Development
[TOC]
### Local development environment
1. Download and install the [Eclipse](https://www.eclipse.org/downloads/) IDE.

   How To Install Eclipse and Get Started with Java Programming (on Windows, Mac OS and Ubuntu)： http://www.ntu.edu.sg/home/ehchua/programming/howto/eclipsejava_howto.html

### SSH setting
1. Generate your ssh public key:

+ For Mac:

```
$ pushd ~/.ssh
$ ssh-keygen
(press enter many times)
$ cp id_rsa.pub ~/[your_name].pub
$ popd
```
+ For windows:

open git bash

`cd ~/.ssh`

find if there was id_rsa.pub: 

``` ls
ls
cat id_rsa.pub
cp id_rsa.pub ~/[your_name].pub
```

If there was not id_rsa.pub, generate the keys as follows: 

```
ssh-keygen
```

Enter the file to save the keys, and then the ssh public keys will be generated.

```
cp id_rsa.pub ~/[your_name].pub
```



1. Send [your_name].pub to TA (mituan@126.com)
2. Wait for TA to add the repository permissions for you.
### Git setting

1. Set your global git config, please set this truthfully, which is related to the final score.

   For Mac:

```
$ git config --global user.name "[your_name]"
$ git config --global user.email [your_email_address]
```
​	For windows:

open git bash

```
git config --global user.name "[your_name]"
git config --global user.email [your_email_address]
```

2. Read [gitmessage](https://github.com/thoughtbot/dotfiles/blob/master/gitmessage) and learn how to write git log.
3. Read [git flow](https://guides.github.com/introduction/flow/) and learn how git flow work.

### Git integration for the Eclipse IDE

​    English tutorial:   https://eclipsesource.com/blogs/tutorials/egit-tutorial/

​    Chinese tutorial: https://blog.csdn.net/hhhccckkk/article/details/10458159



### Code coverage in Eclipse

https://developers.redhat.com/blog/2017/10/06/java-code-coverage-eclipse/



###PMD configuration in Eclipse

1. install
   + In Eclipse go to **Help** > **Install new software**
   + Click **add**  Name: PMD Location: https://dl.bintray.com/pmd/pmd-eclipse-plugin/updates/ 
   + Select the plugin **Eclipse PMD 4**.
   + Finish the installation by accepting the license and restarting Eclipse.
2. Activate
   + Open the property dialog of your project.
   + Select the **PMD** property page.
   + Check the **Enable PMD for this project** checkbox.
   + Add one or more rule sets by clicking the **Add...** button.
   + Close the property dialog. Your project will now be analysed by PMD.
   + If you have another project where you want to activate eclipse-pmd, continue with step 2.


