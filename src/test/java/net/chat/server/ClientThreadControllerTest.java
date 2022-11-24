package net.chat.server;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientThreadControllerTest {

    private final String testClientMessage = "Ура я смог написать тест";
    Socket clientSocket = mock(Socket.class);
    ByteArrayInputStream inContent = new ByteArrayInputStream(testClientMessage.getBytes());
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    public void capturePrintLnOn() {
        System.setOut(new PrintStream(outContent));
    }

    public void capturePrintLnOff() {
        System.setOut(System.out);
    }

    @Test
    void sentMessageInStream() throws IOException {
        when(clientSocket.getOutputStream()).thenReturn(outContent);
        PrintWriterMock printWriter = new PrintWriterMock(
                ClientThreadController.createMessageSender(clientSocket)
        );
        ClientThreadController.sentMessageToClients(printWriter, testClientMessage);
        when(clientSocket.getInputStream()).thenReturn(inContent);
        BufferedReader bufferedReader = ClientThreadController.createMessageReceiver(clientSocket);
        assertEquals("Ура я смог написать тест", bufferedReader.readLine());
    }


    @Test
    void printMessageFromClient() throws IOException {
        capturePrintLnOn();
        ClientThreadController.printMessageFromClient(testClientMessage);
        assertEquals("Сообщение от Клиента: Ура я смог написать тест", outContent.toString()
                .trim());
        capturePrintLnOff();
    }

    @Test
    void isHaveNewMessage() throws IOException {
        when(clientSocket.getInputStream()).thenReturn(inContent);
        BufferedReader bufferedReader = ClientThreadController.createMessageReceiver(clientSocket);
        assertNotNull(bufferedReader);
        assertEquals(bufferedReader.readLine(), testClientMessage);
    }

    @Test
    void createMessageSender() throws IOException {
        when(clientSocket.getOutputStream()).thenReturn(outContent);
        assertNotNull(ClientThreadController.createMessageSender(clientSocket));
    }

    @Test
    void createMessageReceiver() throws IOException {
        when(clientSocket.getInputStream()).thenReturn(inContent);
        assertNotNull(ClientThreadController.createMessageReceiver(clientSocket));
    }

    @Test
    void closeOutputStream() throws IOException {
        when(clientSocket.getOutputStream()).thenReturn(outContent);
        PrintWriterMock printWriter = new PrintWriterMock(
                ClientThreadController.createMessageSender(clientSocket)
        );
        assertNotNull(printWriter);
        assertTrue(printWriter.isOpen());
        ClientThreadController.closeOutputStream(printWriter);
        assertFalse(printWriter.isOpen());
    }

    @Test
    void closeInputStream() throws IOException {
        when(clientSocket.getInputStream()).thenReturn(inContent);
        BufferedReader bufferedReader = ClientThreadController.createMessageReceiver(clientSocket);
        assertNotNull(bufferedReader);
        assertTrue(bufferedReader.ready());
        ClientThreadController.closeInputStream(bufferedReader);
        assertThrows(IOException.class, bufferedReader::ready);
    }

    @Test
    void closeClientSocket() throws IOException {
        Socket clientSocket = new Socket();
        assertNotNull(clientSocket);
        assertFalse(clientSocket.isClosed());
        ClientThreadController.closeClientSocket(clientSocket);
        clientSocket.close();
        assertTrue(clientSocket.isClosed());
    }
}