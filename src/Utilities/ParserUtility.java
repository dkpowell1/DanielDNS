package Utilities;

import Message.Class;
import Message.Type;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * This class holds my utility methods for parsing bytes
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class ParserUtility {

    /**
     * This is a helper method that parses a ByteBuffer to get the name in a
     * query
     *
     * @param name the name to append this name to
     * @param data the ByteBuffer to pull the bytes from
     */
    public static String parseName(String name, ByteBuffer data) {
        int currentLength = data.get();
        while (currentLength != 0) {
            if (((currentLength >> 15) & 0x1) == 1 && (((currentLength >> 14) & 0x1)
                    == 1)) {
                int newPosition = (((currentLength << 8) & 0x3F) | (data.get() &
                        0xFF));
                int oldPos = data.position();
                data.position(newPosition);
                name = parseName(name, data);
                data.position(oldPos);
                currentLength = 0;
            } else {
                name = nameParseLength(name, data,currentLength);
                currentLength = data.get();
            }
        }
        return name;
    }

    /**
     * This method parses bytes from a byteBuffer for a specified length
     * @param name the name to append to
     * @param data the ByteBuffer to parse from
     * @param currentLength the number of bytes to parse out
     * @return the String from the bytes read in
     */
    private static String nameParseLength(String name, ByteBuffer data,int
            currentLength) {
        byte[] bytes = new byte[currentLength];
        for (int i = 0; i < currentLength; i++) {
            bytes[i] = data.get();
        }
        name = name + new String(bytes) + ".";
        return name;
    }

    /**
     * This method parses the ByteBuffer into an integer that the Type class
     * can parse
     *
     * @param data the ByteBuffer to parse
     */
    public static Type parseType(ByteBuffer data) {
        return Type.parseType((data.get() << 8) | data.get());
    }

    /**
     * This method parses the ByteBuffer to get an integer that can be
     * compared to find the class
     *
     * @param data the ByteBuffer to parse
     */
    public static Class parseClass(ByteBuffer data) {
        Class dnsClass;
        if (((data.get() << 8) | data.get()) == 1) {
            dnsClass = Class.IN;
        } else {
            dnsClass = Class.CH;
        }
        return dnsClass;
    }

    /**
     * This method takes in the ByteBuffer containing data to parse, the
     * length of the data and then returns a String with the values contained
     *
     * @param data   the ByteBuffer containing the data
     * @return the String representation of the data
     */
    public static String parseAData(ByteBuffer data) {
        byte[] aData = {data.get(), data.get(), data.get(), data.get()};
        String address;
        try {
            address = InetAddress.getByAddress(aData).getHostAddress();
        } catch (UnknownHostException ignore) {
            address = "Unkonwn Host";
        }
        return address;
    }

    /**
     * This method takes in the ByteBuffer containing data to parse, the
     * length of the data and then returns a String with the values contained
     *
     * @param data   the ByteBuffer containing the data
     * @return the String representation of the data
     */
    public static String parseCnameData(ByteBuffer data) {
        return parseName("",data);
    }

    /**
     * This method takes in the ByteBuffer containing data to parse, the
     * length of the data and then returns a String with the values contained
     *
     * @param data   the ByteBuffer containing the data
     * @return the String representation of the data
     */
    public static String parsePtrData(ByteBuffer data) {
        return parseName("",data);
    }

    /**
     * This method takes in the ByteBuffer containing data to parse, the
     * length of the data and then returns a String with the values contained
     *
     * @param data   the ByteBuffer containing the data
     * @return the String representation of the data
     */
    public static String parseMxData(ByteBuffer data) {
        int preference = ((data.get() << 8) | data.get());
        return preference + " " + parseName("", data);
    }

    /**
     * This method takes in the ByteBuffer containing data to parse, the
     * length of the data and then returns a String with the values contained
     *
     * @param data   the ByteBuffer containing the data
     * @return the String representation of the data
     */
    public static String parseNsData(ByteBuffer data) {
        return parseName("",data);
    }

}
