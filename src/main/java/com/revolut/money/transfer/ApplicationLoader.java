package com.revolut.money.transfer;

import com.revolut.money.transfer.server.Server;

public class ApplicationLoader {
    public static void main(String[] args) {
        Server.startServer();
    }
}
