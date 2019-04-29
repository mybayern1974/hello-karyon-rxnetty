#!/bin/bash
sudo sh -c 'java -jar /opt/hello-karyon-rxnetty/hello-karyon-rxnetty*.jar > /var/log/hello-karyon-rxnetty/hello1.log 2>&1 &'
sleep 1m
sudo sh -c 'java -jar /opt/hello-karyon-rxnetty/hello-karyon-rxnetty*.jar > /var/log/hello-karyon-rxnetty/hello2.log 2>&1 &'
sleep 1m
sudo sh -c 'java -jar /opt/hello-karyon-rxnetty/hello-karyon-rxnetty*.jar > /var/log/hello-karyon-rxnetty/hello3.log 2>&1 &'
sleep 1m
sudo sh -c 'java -jar /opt/hello-karyon-rxnetty/hello-karyon-rxnetty*.jar > /var/log/hello-karyon-rxnetty/hello4.log 2>&1 &'