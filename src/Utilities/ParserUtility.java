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
        while(currentLength != 0) {
            byte[] bytes = new byte[currentLength];
            for (int i = 0; i < currentLength; i++) {
                bytes[i] = data.get();
            }
            name = name + new String(bytes, StandardCharsets.UTF_8) + ".";
            currentLength = data.get();
        }
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
        if(((data.get() << 8) | data.get()) == 1) {
            dnsClass = Class.IN;
        } else {
            dnsClass = Class.CH;
        }
        return dnsClass;
    }

    public static String parseAData(ByteBuffer data, int length) {
        byte[] aData = {data.get(),data.get(),data.get(),data.get()};
        String address;
        try {
            address = InetAddress.getByAddress(aData).toString();
        } catch (UnknownHostException ignore) {
            address = "Unkonwn Host";
        }
        return address;
    }

    public static String parseCnameData(ByteBuffer data, int length) {
        return parseDomainNames(data,length);
    }

    public static String parsePtrData(ByteBuffer data, int length) {
        return parseDomainNames(data,length);
    }

    public static String parseMxData(ByteBuffer data, int length) {
        int preference = ((data.get() << 8) | data.get());
        return preference + " " + parseDomainNames(data, length);
    }

    public static String parseNsData(ByteBuffer data, int length) {
        return parseDomainNames(data,length);
    }

    public static String parseDomainNames(ByteBuffer data, int length) {
        String name = "";
        while(length != 0) {
            byte[] bytes = new byte[length];
            for (int i = 0; i < length; i++) {
                bytes[i] = data.get();
            }
            name = name + new String(bytes, StandardCharsets.UTF_8) + ".";
            length = data.get();
        }
        return name;
    }
}
