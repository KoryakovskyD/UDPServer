import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class UDPServer implements AutoCloseable {

    public static String DEFAULT_HOST = "localhost";
    public static final int SERVER_PORT = 12345;
    public static final int DATA_LENGTH = 32;
    private Map<String, InetAddress> users;
    private DatagramSocket socket;
    private DatagramPacket packet;

    protected UDPServer() throws SocketException, UnknownHostException {
        users = new HashMap<>();
        socket = new DatagramSocket(SERVER_PORT);
        packet = new DatagramPacket(new byte[DATA_LENGTH], DATA_LENGTH);
    }

    public static void main(String[] args) {
        System.out.println("UDPServer started...");
        try (UDPServer s = new UDPServer()) {
            s.run();
        } catch (SocketException e) {
            System.out.println("Error #1: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error #2: " + e.getMessage());
        }
        System.out.println("UDPServer stopped.");
    }

    private void run() throws IOException {
        String login = null;
        InetAddress address = null;
        while (true) {
            socket.receive(packet);
            login = new String(packet.getData(), 0, packet.getLength());
            if (login.isEmpty()) {
                break;
            }
            address = packet.getAddress();
            users.put(login, address);
            System.out.println(login + ": " + address);
        }
    }

    @Override
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
