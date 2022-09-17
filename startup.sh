#!/bin/bash

apt-get update
apt-get install apt-transport-https ca-certificates curl gnupg lsb-release
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo \
  "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
apt-get update
apt-get install docker-ce docker-ce-cli containerd.io
systemctl start docker
systemctl enable docker
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

#cd hyperledger
./pullBinaries.sh
docker network create tools_network
./network.sh down
./network.sh up createChannel
./network.sh deployCC

cd ../server && rm -rf wallet && mvn package
cd .. && docker-compose down

cd hyperledger/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/
file=(*_sk)
cd ../../../../../../../../hyperledger
rm -rf connection.json
cat connection_template.json >> connection.json
sed -i "s/keyStoreFile/${file}/g" connection.json

cd .. && docker-compose up -d --build