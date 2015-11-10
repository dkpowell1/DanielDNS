package Message;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * This lets the recipient of the message know what to expect from the message its received
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class Header {
    /**
     * This is the identifier that generates any kind of query
     */
    private int id;

    /**
     * This a bit that specifies whether this message is a query or
     * response, true if response
     */
    private boolean qR;

    /**
     * This is a field that specifies the query in the message
     */
    private int opCode;

    /**
     * This is for responses, and it basically says the DNS server you hit knows about the name you're looking up
     */
    private boolean aA;

    /**
     * This is an indicator that only the first 512 bytes were returned
     */
    private boolean truncated;

    /**
     * This is used to tell the server to use recursion to search
     */
    private boolean recursionDesired;

    /**
     * This is used to say whether or not recursion is even available on the server
     */
    private boolean recursionAvailable;

    /**
     * This is the value Z
     */
    private int z;

    /**
     * This is the code returned by the server
     */
    private int returnCode;

    /**
     * These are the bytes for an unsigned integer of the number of questions
     */
    private int totalQuestions;

    /**
     * These are the bytes for an unsigned integer representing the number of resource records returned
     */
    private int totalAnswerResourceRecords;

    /**
     * These are the bytes for an unsigned integer representing the total authority resource records
     */
    private int totalAuthorityResourceRecords;

    /**
     * These are the bytes for an unsigned integer representing the number of additional resource records
     */
    private int totalAdditionalResourceRecords;

    /**
     * This method creates a header object with all the necessary components of the header being passed in
     *
     * @param qR                             the flag for query/response
     * @param opCode                         the field specifying the query
     * @param aA                             the flag for authoritative answer
     * @param truncated                      the flag for a truncated message
     * @param recursionDesired               the flag for recursive searching desired
     * @param recursionAvailable             the flag for a servers ability to search with recursion
     * @param z                              a flag, unclear of its use
     * @param returnCode                     the return code from the server
     * @param totalQuestions                 the total number of questions in the message
     * @param totalAnswerResourceRecords     the total number of answers in the message
     * @param totalAuthorityResourceRecords  the total number of authority records in the message
     * @param totalAdditionalResourceRecords the total number of additional resource records in the message
     */
    public Header(boolean qR, int opCode, boolean aA, boolean truncated, boolean recursionDesired, boolean recursionAvailable, int z, int returnCode, int totalQuestions, int totalAnswerResourceRecords, int totalAuthorityResourceRecords, int totalAdditionalResourceRecords) {
        //65536 is the highest 16 bit value for the id, so I randomly generate a number between 0 and 65536
        Random r = new Random();
        this.id = r.nextInt(65536);
        this.qR = qR;
        this.opCode = opCode;
        this.aA = aA;
        this.truncated = truncated;
        this.recursionDesired = recursionDesired;
        this.recursionAvailable = recursionAvailable;
        this.z = z;
        this.returnCode = returnCode;
        this.totalQuestions = totalQuestions;
        this.totalAnswerResourceRecords = totalAnswerResourceRecords;
        this.totalAuthorityResourceRecords = totalAuthorityResourceRecords;
        this.totalAdditionalResourceRecords = totalAdditionalResourceRecords;
    }

    /**
     * The constructor that parses a ByteBuffer to fill its values
     *
     * @param data the ByteBuffer containing the bytes to fill the head with
     */
    public Header(ByteBuffer data) {
        parseID(data);
        parseFlags(data);
        parseCounts(data);
    }

    /**
     * This method uses the helper methods below to package up the header and return it in a ByteBuffer
     *
     * @return the ByteBuffer of the header
     */
    public ByteBuffer getByteBuffer() {
        byte[] newByteArray = new byte[12];
        ByteBuffer byteBuffer = ByteBuffer.wrap(newByteArray);
        byteBuffer.put(getIDBytes());
        byteBuffer.put(getFlags());
        byteBuffer.put(getCounts());
        return byteBuffer;
    }

    /**
     * This method takes the id and pulls it into two bytes and stores them in a byte[]
     *
     * @return the byte array with the two bytes that comprise the id
     */
    private byte[] getIDBytes() {
        byte[] IDArray = new byte[2];
        //This pulls out the first part of the hexidecimal representation of the id
        IDArray[0] = (byte) ((id >> 8) & 0xFF);
        //This pulls out the second part of the hexidecimal representation of the id
        IDArray[1] = (byte) (id & 0xFF);
        return IDArray;
    }

    /**
     * This method takes the flags for the header, converts them to 1's or 0's and combines them into an integer
     * then it separates the ints into two separate bytes and stores them into a byte[]
     *
     * @return This method returns a byte[] that will contain the bits representing the flags
     */
    private byte[] getFlags() {
        byte[] returnBytes = new byte[2];
        //Using tertiary operations to convert booleans to numbers and bitwise operations to shift and combine values
        //I create an integer representation of all the values
        int flags = (qR ? 1 : 0) << 15 | opCode << 14 | (aA ? 1 : 0) << 10 | (truncated ? 1 : 0) << 9 | (recursionDesired ? 1 : 0) << 8
                | (recursionAvailable ? 1 : 0) << 7 | z << 6 | returnCode;
        //This section does the same as getIDBytes where I pull out the first byte of the hex and the second byte
        //of the hex
        returnBytes[0] = (byte) ((flags >> 8) & 0xFF);
        returnBytes[1] = (byte) (flags & 0xFF);
        return returnBytes;
    }

    /**
     * This method takes the counts of all the queries and resource records
     * and converts them to byte[] representation
     *
     * @return the byte[] of the counts
     */
    private byte[] getCounts() {
        byte[] returnBytes = new byte[8];
        //The following is the bitwise operations used to separate the first
        //and second bytes from the hex of the counts
        returnBytes[0] = (byte) ((totalQuestions >> 8) & 0xFF);
        returnBytes[1] = (byte) (totalQuestions & 0xFF);
        returnBytes[2] = (byte) ((totalAnswerResourceRecords >> 8) & 0xFF);
        returnBytes[3] = (byte) (totalAnswerResourceRecords & 0xFF);
        returnBytes[4] = (byte) ((totalAuthorityResourceRecords >> 8) & 0xFF);
        returnBytes[5] = (byte) (totalAuthorityResourceRecords & 0xFF);
        returnBytes[6] = (byte) ((totalAdditionalResourceRecords >> 8) & 0xFF);
        returnBytes[7] = (byte) (totalAdditionalResourceRecords & 0xFF);
        return returnBytes;
    }

    /**
     * This method combines two bytes into a 16 bit ID
     *
     * @param data the ByteBuffer with the bytes for the ID
     */
    private void parseID(ByteBuffer data) {
        this.id = (((data.get() << 8)&0xFF) | (data.get() & 0xFF)) & 0xffff;
    }

    /**
     * This method parses the two bytes containing all the flags and returns
     * an array containing integers for the values for each flag
     *
     * @param data the ByteBuffer with the bytes for parsing
     */
    private void parseFlags(ByteBuffer data) {
        int oneTwo = (data.get() << 8) | data.get();
        this.qR = ((oneTwo >> 15) & 0x1) == 1;
        this.opCode = ((oneTwo >> 14) & 0xF);
        this.aA = ((oneTwo >> 10) & 0x1) == 1;
        this.truncated = ((oneTwo >> 9) & 0x1) == 1;
        this.recursionDesired = ((oneTwo >> 8) & 0x1) == 1;
        this.recursionAvailable = ((oneTwo >> 7) & 0x1) == 1;
        this.z = ((oneTwo >> 6) & 0x7);
        this.returnCode = ((oneTwo) & 0xF);
    }

    /**
     * This function parses the bytes containing the total counts for the
     * Questions and ResourceRecords in the message
     *
     * @param data the ByteBuffer with the bytes for total counts
     */
    private void parseCounts(ByteBuffer data) {
        this.totalQuestions = (data.get() << 8) | data.get();
        this.totalAnswerResourceRecords = (data.get() << 8) | data.get();
        this.totalAuthorityResourceRecords = (data.get() << 8) | data.get();
        this.totalAdditionalResourceRecords = (data.get() << 8) | data.get();
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getTotalAnswerResourceRecords() {
        return totalAnswerResourceRecords;
    }

    public int getTotalAuthorityResourceRecords() {
        return totalAuthorityResourceRecords;
    }

    public int getTotalAdditionalResourceRecords() {
        return totalAdditionalResourceRecords;
    }

    /**
     * This method formats the values of the header into a readable String
     * @return the readable string
     */
    public String toString() {
        return " ID: " + this.id + "\n QR: " + this.qR + "\n OpCode: " + this
                .opCode + "\n Authoritative Answer: " +
                this.aA + "\n Truncated: " + this.truncated + "\n Recursion Desired: " +
                this.recursionDesired + "\n Recursion Available: " +
                this.recursionAvailable + "\n Z: " +
                this.z + "\n Return Code: " +
                this.returnCode + "\n Total Questions: " +
                this.totalQuestions + "\n Total Answers: " +
                this.totalAnswerResourceRecords + "\n Total Authority " +
                "Records: " +
                this.totalAuthorityResourceRecords + "\n Total Additional " +
                "Records: " +
                this.totalAdditionalResourceRecords + "\n";
    }
}
