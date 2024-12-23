import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class OurUtils {
    static String saveHtmlToFile(String htmlContent) {
        try {
            File tempFile = File.createTempFile("clientPage", ".html");
            String name = tempFile.getName();
            System.out.println("Temporary file created at: " + tempFile.getAbsolutePath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            writer.write(htmlContent);
            writer.close();
            System.out.println("HTML content saved to " + tempFile.getAbsolutePath());

            return name;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; }
    static void openInBrowser(String name) {
        try {
            File tempFile = new File("C:\\Users\\ibrah\\AppData\\Local\\Temp\\" + name);
            if (!tempFile.exists()) {
                System.out.println("Temporary file not found.");
                return;
            }
            System.out.println("Opening file in browser: " + tempFile.getAbsolutePath());
            String command = "rundll32 url.dll,FileProtocolHandler " + tempFile.getAbsolutePath();
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        } } }





