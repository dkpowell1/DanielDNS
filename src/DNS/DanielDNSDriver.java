package DNS;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This class runs the DNSLookup
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class DanielDNSDriver {
    /**
     * This method takes in User input to try and resolve a DNS query
     * @param args this takes in the IP for the DNS Server, the lookup name
     *             or domain name to resolve, and the type of DNS query to send
     */
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("usage: DNS.DanielDNSDriver <dnsIP> <lookupName> <lookupType>");
        } else {
            try {
                DNSLookup dnsLookup = new DNSLookup(args[0],args[1],args[2]);
                dnsLookup.queryDNS();
            } catch (UnknownHostException e) {
                System.out.println("Unknown Host Exception: Invalid DNS IP");
            } catch (SocketException e) {
                System.out.println("Socket Exception: Couldn't create Socket");
            }
        }
    }
}
