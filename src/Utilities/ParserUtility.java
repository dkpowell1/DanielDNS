package Utilities;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * This class holds my utility methods for parsing bytes
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class ParserUtility {

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
}
