import java.io.*;
import java.net.*;

public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    Server(){
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection.");
            System.out.println("Waiting ...");
            socket = server.accept();

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // receiving data
            writer = new PrintWriter(socket.getOutputStream()); // sending data

            read(); // Reading Message
            write(); // Sending Message
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read Message
    public void read(){
        Runnable readThread = ()->{
            System.out.println("Reader started.");
            try {
            while(true)
            {
                String message = null;

                    message = reader.readLine();
                    if (message.equals("[Client] : exit")){
                        System.out.println("User left the chat.");
                        socket.close();
                        break;
                    }

                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        };

        // Starting Thread
        new Thread(readThread).start();
    }

    // Write Message
    public void write(){
        Runnable writeThread = ()->{
            System.out.println("Writer started.");
            try {
            while (true){
                // Taking new message
                BufferedReader data = new BufferedReader(new InputStreamReader(System.in));
                String message = null;

                    message = data.readLine();
                if (message.equals("exit")){
                    System.out.println("Terminating chat.");
                    socket.close();
                    break;
                }
                    writer.println("[Server] : " + message);
                    // Flushing message
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        // Starting Thread
        new Thread(writeThread).start();
    }

    public static void main(String[] args)
    {
        System.out.println("Starting server ...");
        // Initializing Server
        new Server();
    }
}
