import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * TODO Descriptions
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class DanielDNSDriver {
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("usage: DanielDNSDriver <dnsIP> <lookupName> <lookupType>");
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
