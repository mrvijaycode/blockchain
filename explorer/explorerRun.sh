#!/bin/bash

#apt-get update

export EXPLORER_CONFIG_FILE_PATH=./config.json
export EXPLORER_PROFILE_DIR_PATH=./connection-profile
export FABRIC_CRYPTO_PATH=./organizations

docker-compose down -v

cd ../hyperledger/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/
file=(*_sk)

cd ../../../../../../../../explorer

rm -rf organizations

mkdir organizations
cd organizations
mkdir peerOrganizations

cp -r ../../hyperledger/organizations/peerOrganizations .

chmod -R 777 peerOrganizations

cd ../connection-profile

rm -rf test-network.json
cat test-network_template.json >> test-network.json
sed -i "s/keyStoreFile/${file}/g" test-network.json

cd ..

#docker-compose up
docker-compose up -d
echo "###... Explorer started ... ###"