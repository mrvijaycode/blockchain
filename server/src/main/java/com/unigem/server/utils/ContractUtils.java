package com.unigem.server.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.sdk.Channel;

final public class ContractUtils {
    public static Contract getContract() throws IOException {
        Path walletPath = Paths.get("wallet");

        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("hyperledger", "organizations", "peerOrganizations",
                "org1.blockchain.unigem.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        Gateway gateway = builder.connect();
        Network network = gateway.getNetwork("geminichannel");
        return network.getContract("unigem");
    }

    public static Channel getChannel() throws IOException {
        Path walletPath = Paths.get("wallet");

        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("hyperledger", "organizations", "peerOrganizations",
                "org1.blockchain.unige.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        Gateway gateway = builder.connect();
        Network network = gateway.getNetwork("geminichannel");
        return network.getChannel();
    }

    public static Contract getQSCCContract() throws IOException {
        Path walletPath = Paths.get("wallet");

        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("hyperledger", "organizations", "peerOrganizations",
                "org1.blockchain.unigem.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        Gateway gateway = builder.connect();
        Network network = gateway.getNetwork("geminichannel");

        return network.getContract("qscc");
    }
}
