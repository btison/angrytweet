FROM mattdm/fedora:f20

# Perform updates
RUN yum -y update; yum clean all

# Install supervisor
RUN yum -y install supervisor; yum clean all

# Create directories
RUN mkdir -p /var/run/supervisord

# Install mysql
RUN yum -y install mariadb-server; yum clean all

# Init mysql
RUN mysql_install_db
RUN chown -R mysql:mysql /var/lib/mysql

# Configure supervisor
ADD supervisord.conf /etc/supervisord.conf
ADD start-container.sh /start-container.sh

# Add scripts and resources
ADD ./sql /sql
ADD /create-schema.sh /create-schema.sh

# Change permissions
RUN chmod 755 /create-schema.sh

# Execute script
RUN /create-schema.sh

#Expose port 3306
EXPOSE 3306

# Entrypoint
ENTRYPOINT ["/bin/bash", "/start-container.sh"]
