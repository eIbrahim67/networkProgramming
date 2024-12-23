import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client_2 {
    static boolean isOn;
    static String clientName;

    public static void main(String[] args) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("Connecting to server at " + ip);
            Socket s = new Socket(ip, 555);
            DataInputStream input = new DataInputStream(s.getInputStream());
            DataOutputStream output = new DataOutputStream(s.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            clientName = scanner.nextLine();
            output.writeUTF(clientName);
            isOn = true;
            Thread receiveThread = new Thread(() -> {
                try {
                    String str;
                    while (isOn) {
                        str = input.readUTF();
                        if (str.startsWith("HTML_CONTENT:")) {
                            String htmlContent = str.substring(13);
                            System.out.println("Received HTML content from server.");
                            String fileName = OurUtils.saveHtmlToFile(htmlContent);
                            OurUtils.openInBrowser(fileName);
                        }
                        System.out.println(str);






                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();
            String str;
            while (true) {
                str = scanner.nextLine();
                output.writeUTF(str);
                if (str.equalsIgnoreCase("exit")) {
                    break;
                }
            }
            isOn = false;
            input.close();
            output.close();
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(Client_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
