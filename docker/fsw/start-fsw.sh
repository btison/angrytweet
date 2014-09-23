#!/bin/bash

. /environment
. /env.sh

IPADDR=$(ip a s | sed -ne '/127.0.0.1/!{s/^[ \t]*inet[ \t]*\([0-9.]\+\)\/.*$/\1/p}')

echo "IPADDR = $IPADDR"

#MYSQL_HOST_IP=$ANGRYTWEETDB_PORT_3306_TCP_ADDR
#MYSQL_HOST_PORT=$ANGRYTWEETDB_SERVICE_PORT

MYSQL_HOST_IP=$SERVICE_HOST
MYSQL_HOST_PORT=$ANGRYTWEETDB_SERVICE_PORT

ANGRYTWEET_CONSUMERKEY=replace 
ANGRYTWEET_CONSUMERSECRET=replace 
ANGRYTWEET_ACCESSTOKEN=replace 
ANGRYTWEET_CONSUMERSECRET=replace 
ANGRYTWEET_EMAIL_USERNAME=replace 
ANGRYTWEET_EMAIL_PASSWORD=replace 
ANGRYTWEET_EMAIL_HOST="smtps://smtp.gmail.com:465" 



echo "MySQL host = $MYSQL_HOST_IP"
echo "MySQL port = $MYSQL_HOST_PORT"

# Sanity checks
if [ ! -d $SERVER_INSTALL_DIR/$SERVER_NAME ]
then
  echo "FSW not installed."
  exit 0
fi

CLEAN=false

SERVER_OPTS=""

while [ "$1" != "" ]; do
    case $1 in
        --clean )               CLEAN=true
                                ;;
        --admin-only )          ADMIN_ONLY=--admin-only
                                ;;
        * )                     SERVER_OPTS="$SERVER_OPTS \"$1\""
                                ;;
    esac
    shift
done

# Clean data, log and temp directories
if [ "$CLEAN" = "true" ]
then
    rm -rf $SERVER_INSTALL_DIR/$SERVER_NAME/standalone/data $SERVER_INSTALL_DIR/$SERVER_NAME/standalone/log $SERVER_INSTALL_DIR/$SERVER_NAME/standalone/tmp
fi

# start fsw
su jboss <<EOF
nohup ${SERVER_INSTALL_DIR}/${SERVER_NAME}/bin/standalone.sh -Djboss.bind.address=$IPADDR -Djboss.bind.address.management=$IPADDR -Djboss.bind.address.insecure=$IPADDR -Djboss.node.name=server-$IPADDR -Dmysql.host.ip=$MYSQL_HOST_IP -Dmysql.host.port=$MYSQL_HOST_PORT -DconsumerKey=$ANGRYTWEET_CONSUMERKEY -DconsumerSecret=$ANGRYTWEET_CONSUMERSECRET -DaccessToken=$ANGRYTWEET_ACCESSTOKEN -DaccessTokenSecret=$ANGRYTWEET_ACCESSTOKENSECRET -DsinceId=$ANGRYTWEET_SINCEID -DcsvInputDir=$ANGRYTWEET_CSVINPUTDIR -Demail.server.username=$ANGRYTWEET_EMAIL_USERNAME -Demail.server.password=$ANGRYTWEET_EMAIL_PASSWORD -Demail.server.host=$ANGRYTWEET_EMAIL_HOST -Dcrm.host=$IPADDR -Dcrm.port=8080 --server-config=$JBOSS_CONFIG $ADMIN_ONLY $SERVER_OPTS 0<&- &>/dev/null &




EOF
echo "FSW started"
