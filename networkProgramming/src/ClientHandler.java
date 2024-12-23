import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private final DataInputStream input;
    private final DataOutputStream output;
    private String clientName;

    public ClientHandler(Socket socket) {
        DataInputStream tempInput = null;
        DataOutputStream tempOutput = null;
        try {
            tempInput = new DataInputStream(socket.getInputStream());
            tempOutput = new DataOutputStream(socket.getOutputStream());
            clientName = tempInput.readUTF();
            System.out.println(clientName + " has joined the chat.");

        } catch (IOException e) {
            e.printStackTrace();
        }
        input = tempInput;
        output = tempOutput;
    }















    @Override
    public void run() {
        try {
            String message;
            while (true) {
                message = input.readUTF();
                System.out.println(clientName + ":" + message);
                Server_1.broadcast(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            output.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientName() {
        return clientName;
    }
}