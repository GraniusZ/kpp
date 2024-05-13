import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class MulticastChat extends JFrame {
    private JTextArea chatArea = new JTextArea(10, 30);
    private JTextField inputField = new JTextField(30);
    private JButton sendButton = new JButton("Send");
    private MulticastSocket socket;
    private InetAddress group;
    private int port = 4446;
    private String userId = Long.toString(System.currentTimeMillis()); // Генерація унікального ідентифікатора

    public MulticastChat() {
        super("Multicast Chat");
        try {
            group = InetAddress.getByName("230.0.0.0");
            socket = new MulticastSocket(port);
            socket.joinGroup(group);
            startReceiver();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        setLayout(new BorderLayout());
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        JPanel inputPanel = new JPanel();
        inputPanel.add(inputField);
        inputPanel.add(sendButton);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String messageToSend = inputField.getText();
                String message = userId + ":" + messageToSend;  // Включення ID користувача у повідомлення
                sendMessage(message);
                chatArea.append("You: " + messageToSend + "\n"); // Відображення повідомлення в GUI
                inputField.setText(""); // Очищення поля вводу після відправлення повідомлення
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void startReceiver() {
        Thread receiverThread = new Thread(() -> {
            try {
                byte[] buffer = new byte[1000];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    if (!received.startsWith(userId + ":")) { // Перевірка на власні повідомлення
                        chatArea.append("Received: " + received.substring(received.indexOf(':') + 1) + "\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("Socket closed!");
            }
        });
        receiverThread.start();
    }

    private void sendMessage(String message) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error sending message", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new MulticastChat();
    }
}
