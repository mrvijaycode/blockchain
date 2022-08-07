#!/bin/bash

#To build package
peer lifecycle chaincode package stud.tar.gz --path ./chaincode/node/stud --lang node --label stud_1.0

#To install
peer lifecycle chaincode install stud.tar.gz --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE
peer lifecycle chaincode install stud.tar.gz --peerAddresses $CORE_PEER_ADDRESS --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

#To queryinstalled chaincode 
peer lifecycle chaincode queryinstalled --peerAddresses $CORE_PEER_ADDRESS --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE


#To approve package
peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA --channelID vijaychannel --name stud --version 1.0 --init-required --package-id stud_1.0:52923f9be7ea10746ea2f8d469c672d299ccbf522e6189a549af2ad14eae537a --sequence 1


#Package ID: stud_1.0:52923f9be7ea10746ea2f8d469c672d299ccbf522e6189a549af2ad14eae537a, Label: stud_1.0

#To check commitreadiness
peer lifecycle chaincode checkcommitreadiness --channelID vijaychannel --name stud --version 1.0 --sequence 1 --output json --init-required

#To commit the chaincode in to all orgs, Its mandatory to commit into all orgs. Here the below command commit in all peers
peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA --channelID vijaychannel --name stud --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 --version 1.0 --sequence 1 --init-required

#To check querycommited
peer lifecycle chaincode querycommitted --channelID vijaychannel --name stud

#To initiate the chaincode
peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C vijaychannel -n stud --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 --isInit -c '{"Args":[]}'


#To invoke the data
peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C vijaychannel -n stud --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 -c '{"Args":["addMarks", "103", "50","52","53"]}'

#To Query chaincode
peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C vijaychannel -n stud --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 -c '{"Args":["queryMarks", "101"]}'

peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C vijaychannel -n stud -c '{"Args":["addMarks", "104", "60","62","63"]}'
