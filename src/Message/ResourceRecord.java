package Message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * This is the portion of the message where the data resides
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class ResourceRecord {
    /** This is the domain name for where the resource is */
    private String name;

    /** This is the type of data in the resource record */
    private Type type;

    /** This is the class of data in the resource record */
    private Class dnsClass;

    /** This is the how long the record should be kept in the cache of the resource record */
    private int ttl;

    /** This is the length of the resource data */
    private int resourceDataLength;

    /** This is the resource data */
    private ArrayList<Byte> resourceData;


    /**
     * This constructor initializes all the fields in a Resource Record
     *
     * @param name domain name searched for
     * @param type the type of record being received
     * @param dnsClass the class of the record
     * @param ttl Time to live, or the hops this record has
     * @param resourceDataLength the length of the data in the record
     */
    public ResourceRecord(String name, Type type, Class dnsClass, int ttl, int resourceDataLength) {
        this.name = name;
        this.type = type;
        this.dnsClass = dnsClass;
        this.ttl = ttl;
        this.resourceDataLength = resourceDataLength;
    }

    public ResourceRecord(ByteBuffer data) {
        parseName(data);
    }

    private void parseName(ByteBuffer data) {
        this.name = "";
        int currentLength = data.get() & 0xFF;
        while(currentLength != 0) {
            byte[] bytes = new byte[currentLength];
            for (int i = 0; i < currentLength; i++) {
                bytes[i] = data.get();
            }
            this.name = this.name + new String(bytes, StandardCharsets.UTF_8);
            currentLength = data.get();
        }
    }
}
