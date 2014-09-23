#!/bin/bash

__run_supervisor() {
echo "Running the run_supervisor function."
supervisord 
}

__run_script() {
echo "Running scripts"
echo MYSQL_HOST=${SERVICE_HOST} >> /env.sh
}

# Call all functions
__run_script
__run_supervisor
