package com.purdue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable, ClientHandlerService {
    private Socket clientSocket;
    private MessageManager messageManager;

    public ClientHandler(Socket socket, MessageManager messageManager) {
        this.clientSocket = socket;
        this.messageManager = messageManager;
    }

    @Override
    public void run() {
        try (BufferedReader bfr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputFromClient;
            while ((inputFromClient = bfr.readLine()) != null) {
                if (inputFromClient.equals("exit")) {
                    break;
                }

                String outputToClient = MainServer.processCommand(inputFromClient, messageManager);
                pw.println(outputToClient);
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
