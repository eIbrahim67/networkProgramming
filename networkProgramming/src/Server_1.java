import java.io.*;
import java.net.*;
import java.util.*;

public class Server_1 {
    private static final List<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(555);
            System.out.println("Server is running and waiting for clients...");
            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                clientHandler.start();
                sendHtmlToClient(clientHandler);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }













    private static void sendHtmlToClient(ClientHandler clientHandler) {
        try {
            File htmlFile = new File("src/cv.html");
            BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append("\n");
            }
            reader.close();
            clientHandler.sendMessage("HTML_CONTENT:" + htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
                clientHandler.sendMessage(sender.getClientName() + ":" + message);
            }
        }
    }
}








