package DNS;

import Message.Message;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * This is the main part of the program.  This handles all the other classes that are needed for the Lookup
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class DNSLookup {
    /** This is the name of the host to search for */
    private String hostname;

    /** This is the type of Messages to look for */
    private String messageType;

    /** This is the IP of the DNS Server to lookup with */
    private InetAddress dnsIP;

    /** This is the datagram socket for the DNS lookup */
    private DatagramSocket datagramSocket;

    /**
     * This is the constructor that sets an object up to hold the values needed for a DNS Lookup
     *
     * @param hostname the name of the host to lookup
     * @param messageType the type of message to look for
     * @param dnsIP the IP of the DNS server to look with
     *
     * @throws UnknownHostException when the dnsIP isn't valid for an InetAddress
     */
    public DNSLookup(String dnsIP, String hostname, String messageType)
            throws UnknownHostException, SocketException {
        this.hostname = hostname;
        this.messageType = messageType;
        this.dnsIP = InetAddress.getByName(dnsIP);
        this.datagramSocket = new DatagramSocket(5053);
        this.datagramSocket.setSoTimeout(500);
    }

    /**
     * This method sends a datagram to the specified DNS Server and then
     * Parses and prints the response to the user
     */
    public void queryDNS() {
        Message message = new Message(hostname,messageType,false);
        DatagramPacket datagramPacket = new DatagramPacket(message
                    .getByteBuffer().array(),message
                    .getByteBuffer().array().length, dnsIP,53);
            try {
                this.datagramSocket.send(datagramPacket);
                this.datagramSocket.receive(datagramPacket);
                Message response = new Message(ByteBuffer.wrap(datagramPacket
                        .getData()));
                System.out.println(response.toString());
            } catch (IOException e) {

            }

    }
}
