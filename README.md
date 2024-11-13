# Computer Network & Programming HW1_202135752
### Protocol Definition Document for Quiz over the cloud
##### 1. Overview
+ ###### This document defines the quiz application protocol between the client and the server. The server passes the quiz question to the client, who sends the answer to the server. The server checks the correct answer and provides the final score to the client.

*** 
#### 2. Message Format and Flow
+ ###### Communication method: Clients and servers send and receive text-based messages, each of which is an open letter \n It ends with that.
+ ###### Ports: The default port number is 7498.

***
#### 3. Protocol Details
+ ##### 3.1 Connection Initialization
  + ###### The client reads the server's IP address and port through the server_info.dat file and attempts to connect to the server.
  + ###### When connected to the server, the server increases the number of connected clients and outputs a message that the client is connected.

+ ##### 3.2 Quiz Flow
+ ##### Question Message
  + ###### Formats: Question: (Question Contents)
  + ###### Example: Question: What is the name of our university?
  + ###### The server sends questions that begin with Question: to the client.
  + ###### If it is a multi-line question, send each line separately and notify the end of the question with the END_OF_QUESTION message.

+ ##### Multiline Question Option
  + ###### Examples:
  ###### (1). Our Planet is Mars.
  ###### (2). The color of Apple is RAINBOW.
  ###### (3). This is Algorithm lecture.
  ###### (4). The professor of Comp network & programming is Choi Jaehyuk.
  + ###### For a multi-line question, send each option individually and notify the END_OF_QUESTION message that the choice is over.

+ ##### End of Question
  + ###### Formats: END_OF_QUESTION
  + ###### Describe: After the server sends all the multilinear questions, send END_OF_QUESTION to allow the client to enter the answers.

+ ##### Answer Message
  + ###### Formats: (Client answer)
  + ###### Example: Gachon university
  + ###### After receiving END_OF_QUESTION, the client enters and sends the answer.
  + ###### The server reviews the answers and sends the results to the client.

+ ##### Result Message
  + ###### Formats: Correct! or Incorrect! The correct answer was : (answer)
  + ###### Example: Incorrect! The correct answer was: Gachon university
  + ###### The server sends a Correct! message if the client's answer is correct, or Incorrect! if it is incorrect.
  
+ ##### 3.3 Quiz Completion
+ ##### Final Score Message
  + ###### Formats: Your score is: (score)/(total score)
  + ###### Example: Your score is: 30/50
  + ###### When all questions are finished, the server calculates the final score and sends it to the client.
 
+ ##### 3.4 Connection Termination
  + ###### The server terminates the connection with the client after sending the final score.
  + ###### When the connection with the client is terminated, the server reduces the number of clients that are connecting.

***
#### 4. Error Handling
+ ##### Invalid Answer: If the client sends an empty answer, the server sends an Invalid answer! Please provide a valid answer.
+ ##### Connection Issues: If the server experiences a problem while connecting to the client, it terminates the connection and outputs an error message.

***
#### 5. Example Interaction
+ ##### The actual message flow between the server and the client is as follows:
  + ###### 1. Server -> Client: Question: What is 10 + 7?
  + ###### 2. Client -> Server: 17
  + ###### 3. Server -> Client: Correct!
 
+ ##### Examples of multi-line questions:
  + ###### 1. Server -> Client:
  + ###### Question: Type number which is correct:
  + ###### (1). Our Planet is Mars.
  + ###### (2). The color of Apple is RAINBOW.
  + ###### (3). This is Algorithm lecture.
  + ###### (4). The professor of Comp network & programming is Choi Jaehyuk.
  + ###### END_OF_QUESTION
 
  + ###### 2. Client -> Server: 4
  + ###### 3. Server -> Client: Correct!

***
#### 6. Protocol Version
+ ##### Version: 1.0
+ ##### Last Updated: 2024.11.14





