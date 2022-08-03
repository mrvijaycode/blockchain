#!/bin/bash

#To build package
peer lifecycle chaincode package unigem.tar.gz --path ./chaincode/node/unigem --lang node --label unigem_1.0

#To install
peer lifecycle chaincode install unigem.tar.gz --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

#To approve package
peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA --channelID geminichannel --name unigem --version 1.0 --init-required --package-id unigem_1.0:53dd724416c0f7ec42c60061827217cc189189ae3bebfa37337733281ccc2268 Label: unigem_1.0 --sequence 1

#To check commitreadiness
peer lifecycle chaincode checkcommitreadiness --channelID geminichannel --name unigem --version 1.0 --sequence 1 --output json --init-required

#To commit the chaincode in to all orgs, Its mandatory to commit into all orgs. Here the below command commit in all peers
peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA --channelID geminichannel --name unigem --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 --version 1.0 --sequence 1 --init-required

#To invoke chaincode -- It's working 
peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C geminichannel -n unigem -c '{"Args":["RegisterFulfillment","{\"id\":\"test100\",\"version\":\"1.0.0\",\"description\":\"testcontract\"}"]}'



peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.blockchain.unigem.com --tls --cafile ${PWD}/organizations/ordererOrganizations/blockchain.unigem.com/orderers/orderer.blockchain.unigem.com/msp/tlscacerts/tlsca.blockchain.unigem.com-cert.pem -C channel1 -n unigem -c '{"Args":["CreateMerchant","1001","Dell"]}'