AngryTweet Fuse Service Works Demo
==================================

      easy step by step instructions may be found in 2014.02.AngryClaimInstall.pdf


Installation pre-requisite (RHEL 6.4 server)
--------------------------------------------

1) Install MAVEN

     sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
     sudo yum install apache-maven
     sudo mkdir /usr/share/apache-maven/conf/logging

create a file in /etc/profile.d/maven.sh containing : 

     if ! echo ${PATH} | /bin/grep -q /usr/share/apache-maven/bin ; 
     then
          PATH=/usr/share/apache-maven/bin:${PATH}
     fi 

2) Install MYSQL

     sudo yum install mysql-server
     sudo yum install mysql-connector-java
     sudo chkconfig mysqld on

3) Install GIT

     sudo yum install git

4) clone the demo from gitHub

     cd ~
     git clone https://github.com/lucpierson/AngryClaim.git

5) Download required softwares and place them in ~/AngryClaim/installs

     from Redhat downloads       
        ==> jboss-fsw-installer-6.0.0.GA-redhat-4.jar 
        ==> jboss-bpms-6.0.0.GA-redhat-1-deployable-eap6.x.zip
        ==> jboss-eap-6.1.1.zip
     from search.maven.org 
        ==> twitter4j-core-3.0.5.jar
        ==> twitter4j-stream-3.0.5.jar
6) Reboot

7) create a twitter app (see below) and a gmail account

      update the init.sh with the correct tokens


8) launch ~/AngryClaim/init.sh

9) final demo instructions are generated in readme.txt





Installation Notes
------------------

### Camel-twitter configuration

To use the camel-twitter component on FSW, it needs to be installed as a module in the underlying EAP server and referenced in the SwitchYard configuration.
Moreover, the camel-twitter component shipped with the version of camel used by FSW (version 2.10.0.redhat-60024-1) does not longer work, due to API changes introduced by Twitter.
The etc/lib folder contains a modified and working version of the camel-twitter component.

#### Installation of the camel-twitter module:
* Create a directory twitter/main under $FSW_HOME/modules/system/layers/soa/org/apache/camel.
* Copy module.xml and camel-twitter-2.10.0.redhat-60024-1.jar from etc/modules/camel-twitter to the twitter/main directory

####Installation of the twitter4j module
* Create a directory twitter4j/main under $FSW_HOME/modules/system/layers/soa/org/
* Copy module xml from etc/modules/twitter4j to the twitter4j/main directory
* Copy twitter4j-core-3.0.5.jar and twitter4j-stream-3.0.5.jar (download these from maven central - http://search.maven.org) into the twitter4j/main directory

To be able to poll a twitter account and send updates, the application needs to be registered with that account. 
This can be done from the http://dev.twitter.com/apps page. 
Be sure to grant read and write rights to the application.
Make note of the customer key and secret, and the access token key and secret.  

### Configuration of switchyard

In $FSW_HOME/standalone/configuration/standalone.xml, add the following line to the <extensions> section of the switchyard subsystem configuration:

    <extension identifier="org.apache.camel.twitter"/>

### crm webservice configuration
The application calls a webservice which emulates a crm application. 
Data for this mock are configured in a crm.properties file, to be added to $FSW_HOME/standalone/configuration/

The format of the properties is:

    <customer code>=<first name>,<last name>,<twitter username>,<postal code>,<email address>

For an example, see etc/crm/crm.properties

### csv file (input for batch example)

The application can take as input a csv file.
The format of the csv records is:

    <id>;<timestamp>;<customer code>;<service>;<comments>;<urgent>

For an example, see etc/csv/csv.txt
directory scanned by Fuse : etc/csv/demo

### datasource configuration
In $FSW_HOME/standalone/configuration/standalone.xml, add a datasource definition, with JNDI name java:jboss/datasources/AngryTweetDS.

    <datasource jndi-name="java:jboss/datasources/AngryTweetDS" pool-name="angrytweetDS" enabled="true" use-java-context="true">
      <connection-url>jdbc:mysql://localhost:3306/angrytweet</connection-url>
      <driver>mysql</driver>
      <security>
        <user-name>jboss</user-name>
        <password>jboss</password>
      </security>
    </datasource>

If needed, add a module for the database driver to the server, and add the driver definition to standalone.xml:

    <drivers>
    [...]
      <driver name="mysql" module="com.mysql">
        <xa-datasource-class>com.mysql.jdbc.jbdc2.optional.MysqlXAConnection</xa-datasource-class>
      </driver>
    </drivers>

### System properties

The FSW server needs to be started up with a number of system properties:

* consumerKey : twitter account consumer key
* consumerSecret : twitter account consumer secret
* accessToken : twitter account access token
* accessTokenSecret : twitter account access token secret
* sinceId : tweet ID to use as start value for timeline polling (only tweets with an id > sinceId will be returned)
* csvInputDir : directory to poll for csv files
* email.server.host : email server url (example smtps://smtp.gmail.com:465 for gmail)
* email.server.username : email server user name
* email.server.password : email server password (note: should not contain characters as ?,&,+ ...) 
* crm.host : host name/ip address of the crm application
* crm.port : port of the crm application


