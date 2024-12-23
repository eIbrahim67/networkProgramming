import java.net.*;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

//      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//      while (interfaces.hasMoreElements()) {
//          NetworkInterface network = interfaces.nextElement();
//          System.out.println(getMACIdentifier(network));
//      }

//      httpGETRequest();

        clintPOSTRequestToServer();
    }

    // Method to get the MAC address of a network interface as a formatted string
    public static String getMACIdentifier(NetworkInterface network) {
        // A StringBuilder to construct the MAC address string
        StringBuilder identifier = new StringBuilder();
        try {
            // Get the MAC address as a byte array from the network interface
            byte[] macBuffer = network.getHardwareAddress();
            // Check if the MAC address is available (not null)
            if (macBuffer != null) {
                // Iterate over the MAC address byte array
                for (int i = 0; i < macBuffer.length; i++) {
                    // Append each byte to the identifier as a two-digit hexadecimal string
                    // Use ":" as a separator between bytes, except for the last byte
                    identifier.append(
                            String.format("%02X%s", macBuffer[i],
                                    (i < macBuffer.length - 1) ? ":" : ""));
                }
            } else {
                // If the MAC address is not available, return a placeholder string
                return "---";
            }
        } catch (SocketException ex) {
            // Handle any exception that occurs while retrieving the MAC address
            ex.printStackTrace();
        }
        // Return the formatted MAC address string
        return identifier.toString();
    }


    // Method that throws IOException if any I/O exception occurs
    public static void httpGETRequest() throws IOException {

        // The host and port of the HTTP server to connect to
        String host = "127.0.0.1";   // Localhost
        int port = 5500;             // Port number of the server

        // Create a socket to connect to the server at the specified host and port
        Socket socket = new Socket(host, port);

        // Create BufferedReader to read input from the server via the socket's input stream
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        // Create PrintWriter to send output to the server via the socket's output stream
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        // Send an HTTP GET request to the server for the "/index.html" page
        out.println("GET /www/test.html HTTP/1.0");

        // A blank line separating headers and body in the HTTP request
        out.println();

        // Ensure that the request is sent immediately by flushing the output stream
        out.flush();

        // Variable to hold each line of the response from the server
        String line;

        // Read the response from the server line by line and print each line to the console
        // readLine() will return null if the server closes the connection
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }

        // Close the input and output streams once the communication is done
        in.close();
        out.close();
    }


    public static void clintPOSTRequestToServer() {
        try {
            // Step 1: Create a URL object with the server's address and port
            URL url = new URL("http://localhost:12001");

            // Step 2: Open an HTTP connection to the specified URL
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Step 3: Configure the connection properties
            con.setDoOutput(true); // Enable output to send data to the server
            con.setRequestMethod("POST"); // Set HTTP request method to POST
            con.setRequestProperty("connection", "keep-alive"); // Keep connection alive for potential reuse
            con.setUseCaches(false); // Disable caching to ensure fresh request and response

            // Step 4: Create the request body (HTML content in this case)
            String test = "<html><body><h1>Hello</h1></body></html>";
            byte[] bytes = test.getBytes(); // Convert string to byte array for transmission

            // Step 5: Set HTTP headers for the request
            con.setRequestProperty("Content-length", String.valueOf(bytes.length)); // Set content length
            con.setRequestProperty("Content-type", "text/html"); // Specify the MIME type as HTML
            con.setRequestProperty("User-Agent", " Java1.3.1_04 "); // Provide information about the client
            con.setRequestProperty("Accept", "text/html"); // Specify that the client accepts HTML responses

            // Step 6: Send the HTTP request
            OutputStream out = con.getOutputStream(); // Open output stream to send data
            //out.write(); // Send a blank line to separate headers and body
            out.write(bytes); // Write the HTML content to the output stream
            out.flush(); // Ensure all data is sent to the server immediately

            // Step 7: Read the server's response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String temp;
            while ((temp = in.readLine()) != null) { // Read the response line by line
                System.out.println(temp); // Print each line of the response to the console
            }

            // Step 8: Close resources and disconnect
            out.close(); // Close the output stream
            in.close(); // Close the input stream
            con.disconnect(); // Disconnect the HTTP connection to release resources
        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions that occur for debugging purposes
            System.exit(1); // Exit the program with an error code
        }
    }


}
