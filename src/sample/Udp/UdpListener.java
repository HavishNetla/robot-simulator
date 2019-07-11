package sample.Udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpListener implements Runnable {
  private final int port;
  private PacketParser packetParser;

  public UdpListener(int port) {
    this.port = port;
    packetParser = new PacketParser();
  }

  @Override
  public void run() {
    System.out.println("running");
    try (DatagramSocket clientSocket = new DatagramSocket(port)) {
      while (true) {
        byte[] buffer = new byte[65507];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
        clientSocket.receive(datagramPacket);

        String receivedMessage = new String(datagramPacket.getData());
        System.out.println("recived message: " + receivedMessage);

        packetParser.parseMessage(receivedMessage);
      }
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Timeout. Client is closing.");
    }
  }
}
