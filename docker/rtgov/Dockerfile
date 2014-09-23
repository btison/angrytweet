FROM mattdm/fedora:f20

# Perform updates
RUN yum -y update; yum clean all

# Install supervisor
RUN yum -y install supervisor; yum clean all

# Create directories
RUN mkdir -p /var/run/supervisord

# Install java
RUN yum -y install java-1.7.0-openjdk-devel; yum clean all

# Install mysql driver
RUN yum -y install mysql-connector-java; yum clean all

# Create user jboss
RUN groupadd -r jboss -g 400 && useradd -u 400 -r -g jboss -d /opt/jboss -s /bin/bash -c "JBoss user" jboss

# Create installation directory and set permissions
RUN mkdir -p /opt/jboss
RUN chown -R jboss:jboss /opt/jboss

# Scripts and resources
ADD ./install-rtgov.sh /install-rtgov.sh
ADD ./env.sh /env.sh
ADD ./resources /resources
ADD ./configuration /configuration
ADD ./start-container.sh /start-container.sh
ADD ./supervisord.conf /etc/supervisord.conf
ADD ./start-rtgov.sh /start-rtgov.sh
ADD ./stop-rtgov.sh /stop-rtgov.sh
ADD ./copy-environment-variables.sh /copy-environment-variables.sh
ADD ./deploy-angrytweet.sh /deploy-angrytweet.sh

# Modify script permissions
RUN chmod 755 /start-container.sh /install-rtgov.sh /env.sh /start-rtgov.sh /stop-rtgov.sh /copy-environment-variables.sh /deploy-angrytweet.sh

# Install FSW script
RUN /install-rtgov.sh

# Deploy Angrytweet application
RUN /deploy-angrytweet.sh

# Expose ports 18080,19990,19999
EXPOSE 18080 19990 19999

# Entrypoint
ENTRYPOINT ["/bin/bash", "/start-container.sh"]
