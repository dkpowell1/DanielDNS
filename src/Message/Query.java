package Message;

import Utilities.ParserUtility;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This is the data for the query that is sent to the DNS server
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class Query {
    /** This is the name of the query */
    private String name;

    /** This is the type of the query */
    private Type type;

    /** This is the class of the query */
    private Class dnsClass;

    /**
     * This is a constructor for a query that has all the fields initialized
     * @param name the domain name being queried
     * @param type the type of query
     * @param dnsClass the class for the query
     */
    public Query(String name, Type type, Class dnsClass) {
        this.name = name;
        this.type = type;
        this.dnsClass = dnsClass;
    }

    /**
     * This method takes in a ByteBuffer containing the data to parse into
     * the class
     *
     * @param data the ByteBuffer to parse through
     */
    public Query(ByteBuffer data) {
        this.name = "";
        parseName(data);
        parseType(data);
        parseClass(data);
    }

    /**
     * This turns all the values stored in this class into the bytes
     * representing them
     *
     * @return the ByteBuffer holding all the bytes for this class
     */
    public ByteBuffer getByteBuffer() {
        byte[] newByteArray = new byte[(512-32)/8];
        ByteBuffer byteBuffer = ByteBuffer.wrap(newByteArray);
        byteBuffer.put(getNameBytes());
        switch (type) {
            case A:
                byteBuffer.putShort((short) 1);
                break;
            case CNAME:
                byteBuffer.putShort((short) 5);
                break;
            case MX:
                byteBuffer.putShort((short) 15);
                break;
            case PTR:
                byteBuffer.putShort((short) 12);
                break;
        }
        if(dnsClass == Class.IN) {
            byteBuffer.putShort((short)1);
        }
        return byteBuffer;
    }

    /**
     * This method assists in separating the host name into labels and
     * returning their byte values
     *
     * @return the byte array that holds the bytes for the hostname labels
     */
    private byte[] getNameBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String[] nameArray = this.name.split("\\.");
        for(int i = 0; i  < nameArray.length; i++) {
            outputStream.write(nameArray[i].length());
            try {
                outputStream.write(nameArray[i].getBytes());
            } catch (IOException ignored) {

            }
        }
        outputStream.write(0);
        return outputStream.toByteArray();
    }

    /**
     * This is a helper method that parses a ByteBuffer to get the name in a
     * query
     *
     * @param data the ByteBuffer to pull the bytes from
     */
    private void parseName(ByteBuffer data) {
        this.name = ParserUtility.parseName(this.name, data);
    }

    /**
     * This method parses the ByteBuffer into an integer that the Type class
     * can parse
     *
     * @param data the ByteBuffer to parse
     */
    private void parseType(ByteBuffer data) {
        this.type = Type.parseType((data.get() << 8) | data.get());
    }

    /**
     * This method parses the ByteBuffer to get an integer that can be
     * compared to find the class
     *
     * @param data the ByteBuffer to parse
     */
    private void parseClass(ByteBuffer data) {
        if(((data.get() << 8) | data.get()) == 1) {
            this.dnsClass = Class.IN;
        }
    }
}
