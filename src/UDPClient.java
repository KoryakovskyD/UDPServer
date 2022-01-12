import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient implements AutoCloseable {

    private static String default_login = "Test";
    private DatagramSocket socket;
    private DatagramPacket packet;

    public UDPClient() throws SocketException {
        socket = new DatagramSocket();
        packet = new DatagramPacket(new byte[UDPServer.DATA_LENGTH],
                UDPServer.DATA_LENGTH);
    }
    
    public static void main(String[] args) {
        try (UDPClient client = new UDPClient ()) {
            InetAddress server 
                    = InetAddress.getByName(UDPServer.DEFAULT_HOST);
            client.run(server);
        } catch (SocketException e) {
            System.out.println("Error #1: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error #2: " + e.getMessage());
        }
    }
    
    public void run (InetAddress server) throws IOException {
        byte[] tmp = default_login.getBytes();
        System.arraycopy(tmp, 0, packet.getData(), 0, tmp.length);
        packet.setAddress(server);
        packet.setPort (UDPServer.SERVER_PORT);
        packet.setLength(tmp.length);
        String s = new String (packet.getData());
        socket.send (packet);
        // Stop server
        tmp = new byte[0];
        packet.setData(tmp);
        packet.setLength(0);
        socket.send(packet);
    }

    @Override
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
