#!/bin/bash

#To build package-- but if already done package no need to package again
peer lifecycle chaincode package unigem.tar.gz --path ./chaincode/node/unigem --lang node --label unigem_1.0

#To install
peer lifecycle chaincode install unigem.tar.gz --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

#To queryinstalled chaincode 
peer lifecycle chaincode queryinstalled --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

#To approve package
peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA --channelID geminichannel --name unigem --version 1.0 --init-required --package-id unigem_1.0:53dd724416c0f7ec42c60061827217cc189189ae3bebfa37337733281ccc2268 Label: unigem_1.0 --sequence 1


#commit chaincode
peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA --channelID geminichannel --name unigem --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 --version 1.0 --sequence 1 --init-required