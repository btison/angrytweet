FROM mattdm/fedora:f20
# FROM rhel7

# Perform updates
RUN yum -y update; yum clean all

# Install supervisor
RUN yum -y install supervisor; yum clean all

# Create directories
RUN mkdir -p /var/run/supervisord


#Install java
RUN yum -y install java-1.7.0-openjdk-devel; yum clean all

# Install mysql driver
RUN yum -y install mysql-connector-java; yum clean all

# Create user jboss
RUN groupadd -r jboss -g 400 && useradd -u 400 -r -g jboss -d /opt/jboss -s /bin/bash -c "JBoss user" jboss

# Create installation directory and set permissions
RUN mkdir -p /opt/jboss
RUN chown -R jboss:jboss /opt/jboss

# Scripts and resources
ADD ./env.sh /env.sh
ADD ./install-fsw.sh /install-fsw.sh
ADD ./configuration /configuration
ADD ./resources /resources
ADD ./start-container.sh /start-container.sh
ADD ./supervisord.conf /etc/supervisord.conf
ADD ./start-fsw.sh /start-fsw.sh
ADD ./stop-fsw.sh /stop-fsw.sh
ADD ./copy-environment-variables.sh /copy-environment-variables.sh
ADD ./configure-fsw.sh /configure-fsw.sh

# Modify script permissions
RUN chmod 755 /start-container.sh /install-fsw.sh /env.sh /start-fsw.sh /stop-fsw.sh /copy-environment-variables.sh /configure-fsw.sh

# Install FSW script
RUN /install-fsw.sh

# Configure FSW script
RUN /configure-fsw.sh

# Expose ports 8080,9990,9999
EXPOSE 8080 9990 9999

# Entrypoint
ENTRYPOINT ["/bin/bash", "/start-container.sh"]
