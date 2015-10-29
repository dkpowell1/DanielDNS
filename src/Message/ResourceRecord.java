package Message;

import Utilities.ParserUtility;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * This is the portion of the message where the data resides
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class ResourceRecord {
    /**
     * This is the domain name for where the resource is
     */
    private String name;

    /**
     * This is the type of data in the resource record
     */
    private Type type;

    /**
     * This is the class of data in the resource record
     */
    private Class dnsClass;

    /**
     * This is the how long the record should be kept in the cache of the resource record
     */
    private int ttl;

    /**
     * This is the length of the resource data
     */
    private int resourceDataLength;

    /**
     * This is the resource data
     */
    private ArrayList<Byte> resourceData;


    /**
     * This constructor initializes all the fields in a Resource Record
     *
     * @param name               domain name searched for
     * @param type               the type of record being received
     * @param dnsClass           the class of the record
     * @param ttl                Time to live, or the hops this record has
     * @param resourceDataLength the length of the data in the record
     */
    public ResourceRecord(String name, Type type, Class dnsClass, int ttl, int resourceDataLength) {
        this.name = name;
        this.type = type;
        this.dnsClass = dnsClass;
        this.ttl = ttl;
        this.resourceDataLength = resourceDataLength;
    }

    /**
     * This method creates a new Resource Record using the bytes in a ByteBuffer
     * @param data the ByteBuffer to pull the data from
     */
    public ResourceRecord(ByteBuffer data) {
        parseName(data);
        this.type = ParserUtility.parseType(data);
        this.dnsClass = ParserUtility.parseClass(data);
        parseTTL(data);
        parseData(data);
    }

    /**
     * This method parses the bytes for the name of the resource record and
     * determines whether or not the byte represents a pointer, or a string
     * and then calls the utility method for parsing the name
     *
     * @param data the ByteBuffer with the bytes for the name
     */
    private void parseName(ByteBuffer data) {
        this.name = "";
        int currentLength = ((data.get() & 0xff) << 8) | (data.get() & 0xff);

        while (currentLength != 0) {
            if (((currentLength >> 15) & 0x1) == 1 && (((currentLength >> 14) & 0x1)
                    == 1)) {
                int oldPos = data.position();
                data.position(currentLength & 0x3FFF);
                this.name = ParserUtility.parseName(this.name, data);
                data.position(oldPos);
            } else {
                this.name = ParserUtility.parseName(this.name, data);
            }
            currentLength = data.get();
        }
    }

    private void parseTTL(ByteBuffer data) {
        this.ttl = ((data.get() << 31) | (data.get() << 23) | (data.get() << 15)
                | (data.get() << 7));
    }

    private void parseData(ByteBuffer data) {
        this.resourceDataLength = (data.get() << 8 | data.get()) & 0xFF;
        for(int i = 0; i < resourceDataLength; i++) {
            this.resourceData.add(data.get());
        }
    }
}
