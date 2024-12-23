import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI {
    static boolean isOn;
    static String clientName;
    private static JTextArea chatArea;
    private static JTextField messageField;
    private static JButton sendButton;
    private static DataOutputStream output;
    private static DataInputStream input;
    private static Socket socket;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(new Color(255, 255, 255));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(new Color(245, 245, 245));
        chatArea.setForeground(new Color(50, 50, 50));  // Dark text for better readability
        chatArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(new Color(255, 255, 255));

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.setPreferredSize(new Dimension(350, 40));
        messageField.setBackground(new Color(245, 245, 245));
        messageField.setForeground(new Color(50, 50, 50));
        messageField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setPreferredSize(new Dimension(100, 40));
        sendButton.setBackground(new Color(70, 130, 180));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        sendButton.setFocusPainted(false);

        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.getContentPane().add(chatPanel, BorderLayout.CENTER);
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("Connecting to server at " + ip);
            socket = new Socket(ip, 555);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            String name = JOptionPane.showInputDialog(frame, "Enter your name:");
            clientName = name != null ? name : "Anonymous";
            output.writeUTF(clientName);
            isOn = true;

            Thread receiveThread = new Thread(() -> {
                try {
                    String str;
                    while (isOn) {
                        str = input.readUTF();
                        if (str.startsWith("HTML_CONTENT:")) {
                            String htmlContent = str.substring(13);
                            displayMessage("Received HTML content from server.");
                            String fileName = OurUtils.saveHtmlToFile(htmlContent);
                            OurUtils.openInBrowser(fileName);
                        }
                        displayMessage(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();
            sendButton.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { sendMessage();}});
            messageField.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { sendMessage();} });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private static void sendMessage() {
        String message = messageField.getText();
        if (!message.trim().isEmpty()) {
            try {
                output.writeUTF(message);
                displayMessage(message);
                messageField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void displayMessage(String message) {
        chatArea.append(message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
