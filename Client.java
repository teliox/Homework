package hw1_202135752;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static String serverIp;
    private static int port;

//--------------------------- Variables ---------------------------------------

    public static void main(String[] args) {

        //Read server IP and port from server_info.dat file
        try (BufferedReader reader = new BufferedReader(new FileReader("server_info.dat"))) {
            serverIp = reader.readLine();                //Read IP address
            port = Integer.parseInt(reader.readLine());  //Read Port number
            System.out.println("Successfully read data from 'server_info.dat'.");
        } catch (IOException e) {
            System.out.println("Cannot find or read 'server_info.dat': " + e.getMessage() + ". Try connecting to local host ...");

            //If cannot find server_info.dat, then connecting with local host
            serverIp = "127.0.0.1";
            port = 7498;
            System.out.println("Local host connected: " + serverIp + ":" + port);
        }

        //Try to connect to server
        try (Socket socket = new Socket(serverIp, port)) {
            System.out.println("Successfully connected to server: " + serverIp + ":" + port);

            BufferedReader msgFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter msgToServer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            //Use end signal to avoid confusion
            String message;
            while ((message = msgFromServer.readLine()) != null) {
                if (message.equals("END_OF_QUESTION")) {
                    //Answer when receive a Quit signal: Output
                    System.out.print("Answer: ");
                    String answer = scanner.nextLine();
                    msgToServer.println(answer);
                    System.out.println("Sent answer to server: " + answer);
                } else {
                    System.out.println("Server: " + message);
                }
            }

//--------------------------- Exception handeling -----------------------------

        } catch (IOException e) {
            System.out.println("Cannot connect to server: " + e.getMessage());
        }
    }
}