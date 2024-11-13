package hw1_202135752;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final int PORT = 7498;
    private static final int MAX_CLIENTS = 8; //Maximum concurrent connecting clients
    private static ExecutorService threadPool = Executors.newFixedThreadPool(MAX_CLIENTS);
    private static AtomicInteger clientCount = new AtomicInteger(0); //Number of clients being connected

//--------------------------------- Variables ---------------------------------

    public static void main(String[] args) {

        //Write IP and port information in the server_info.dat file
        String serverIp = "172.30.1.55";
        try (PrintWriter writer = new PrintWriter(new FileWriter("server_info.dat"))) {
            writer.println(serverIp);
            writer.println(PORT);
            System.out.println("The IP and Port number are successfully written in 'server_info.dat'.");
        } catch (IOException e) {
            System.out.println("Error occurs! End process: " + e.getMessage());
            return;
        }

        //Create a server socket and wait for the client
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, waiting for clients...");

            //Setting up the 'Scheduled Executor Service' for status Verification (For Multi-client suppport)
            ScheduledExecutorService statusExecutor = Executors.newSingleThreadScheduledExecutor();
            statusExecutor.scheduleAtFixedRate(() -> {
                System.out.println("Current connected clients: " + clientCount.get());
                System.out.println("ThreadPool active: " + (!threadPool.isShutdown()));
            }, 0, 10, TimeUnit.SECONDS); //Check status every 10seconds

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                //Increase number of client
                clientCount.incrementAndGet();

                //Create a Runable object for client processing and submit it to thread pool
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                threadPool.execute(() -> {
                    try {
                        clientHandler.run();
                    } finally {
                        //Reduced number of clients in connection at client termination
                        clientCount.decrementAndGet();
                    }
                });
            }

//----------------------------- Exception handeling ---------------------------

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            System.out.println("Shutting down server...");
            threadPool.shutdown(); //Thread Pool Shutdown When Server Shutdown
            try {
                if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.out.println("Forcing thread pool shutdown...");
                    threadPool.shutdownNow(); //Forced termination
                }
                System.out.println("Server shutdown complete.");
            } catch (InterruptedException e) {
                System.out.println("Error during thread pool shutdown: " + e.getMessage());
                threadPool.shutdownNow();
            }
        }
    }
}