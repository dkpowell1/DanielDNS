package Message;

import Utilities.ParserUtility;
import java.nio.ByteBuffer;

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
    private long ttl;

    /**
     * This is the resource data
     */
    private String resourceData;


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
        this.resourceData = "";
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
        this.name = ParserUtility.parseName("",data);
    }

    /**
     * This method parses the Time To Live for the Resource Record
     * @param data  the ByteBuffer containing the data to parse
     */
    private void parseTTL(ByteBuffer data) {
        this.ttl = ((data.get() << 31) | (data.get() << 23) | (data.get() << 15)
                | (data.get() << 7));
    }

    /**
     * This method parses out the data associated with the resource records
     *
     * @param data the ByteBuffer containing the data to parse
     */
    private void parseData(ByteBuffer data) {
        switch(this.type) {
            case A:
                this.resourceData = ParserUtility.parseAData(data);
                break;
            case NS:
                this.resourceData = ParserUtility.parseNsData(data);
                break;
            case PTR:
                this.resourceData = ParserUtility.parsePtrData(data);
                break;
            case MX:
                this.resourceData = ParserUtility.parseMxData(data);
                break;
            case CNAME:
                this.resourceData = ParserUtility.parseCnameData(data);
                break;
        }
    }

    /**
     * This method returns the string representation of the Resource Record
     * in a way that is easy to read
     * @return the formatted string
     */
    public String toString() {
        return " Name: " + this.name + "\n Type: " + this.type +
                "\n Class: " + this.dnsClass + "\n TTL: " + this.ttl +
                "\n " + "Resource Data: " + this.resourceData + "\n";
    }
}
