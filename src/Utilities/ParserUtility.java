package Utilities;

import Message.Class;
import Message.Type;

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
}
