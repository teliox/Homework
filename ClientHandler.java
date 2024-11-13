package hw1_202135752;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private static final String[][] Questions = {
        {"What is 10 + 7?", "17"},
        {"What is the name of our university?", "Gachon university"},
        {"Type number which is correct:", "4"},
        {"This quiz is provided by S_ _ _ _ _ (Type 5 letters).", "erver"},
        {"Elon Musk is CEO of _ _ _ _ _.", "tesla"}
    };

    private static final String[] MultiLineOptions = {
        "(1). Our Planet is Mars.",
        "(2). The color of Apple is RAINBOW.",
        "(3). This is Algorithm lecture.",
        "(4). The professor of Comp network & programming is Choi Jaehyuk."
    };

//------------------------- Variables -----------------------------------------

    @Override
    public void run() {
        //Set buffers
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            int score = 0;
            
            //Sending questions
            for (int i = 0; i < Questions.length; i++) {
                String question = Questions[i][0];
                String answer = Questions[i][1];

                out.println("Question: " + question);
                out.flush(); //clearing buffer
                
                //MultiLineOption : when multiple choice questions are
                //presented,there are some problems like Printing only one 
                //line, or an infinite waiting are occured

                //So, Exceptions are made only for multiple-choice questions,
                //and after the problem is output, 
                //MultiLine Options sets are each printed and flush repeatedly.
                if (question.equals("Type number which is correct:")) {
                    for (String line : MultiLineOptions) {
                        out.println(line);
                        out.flush();
                    }
                }

                //Send Multiline Question End Signaling
                out.println("END_OF_QUESTION");
                out.flush();

                //Receive answers and handle exceptions
                String clientAnswer = in.readLine();
                if (clientAnswer == null || clientAnswer.trim().isEmpty()) {
                    out.println("Invalid answer! Please provide a valid answer.");
                    out.flush();
                    continue;
                }

                //Answer Check - Case-free comparison after space removal

                //For example, in question number 2, GachonUniversity,
                //gachonuniversity,GACHON UNIVERSITY are all correct
                String normalizedClientAnswer = clientAnswer.replaceAll("\\s+", "").toLowerCase();
                String normalizedAnswer = answer.replaceAll("\\s+", "").toLowerCase();

                System.out.println("Received answer from client: " + clientAnswer);

                if (normalizedClientAnswer.equals(normalizedAnswer)) {
                    out.println("Correct!");
                    score += 10; //Scoreing
                } else {
                    out.println("Incorrect! The correct answer was: " + answer);
                }
                out.flush();
            }

            //Send final score
            out.println("Your score is: " + score + "/" + (Questions.length * 10));
            out.flush();
            System.out.println("Sent final score to client.");

//------------------------- Exception handeling -------------------------------

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Closed client connection.");
            } catch (IOException e) {
                System.out.println("Failed to close client socket: " + e.getMessage());
            }
        }
    }
}