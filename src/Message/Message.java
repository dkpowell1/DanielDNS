package Message;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * This is what is going to be sent to the DNS server
 *
 * @author Daniel Powell
 * @version 1.0
 */
public class Message {
    /**
     * This is the header to the message that is described in RFC 1035
     */
    private Header header;

    /**
     * This is a list of questions
     */
    private ArrayList<Query> questions;

    /**
     * This is a list of answer resource records
     */
    private ArrayList<ResourceRecord> answerResourceRecords;

    /**
     * This is the list of authority resource records
     */
    private ArrayList<ResourceRecord> authorityResourceRecords;

    /**
     * This is the list of additional resource records
     */
    private ArrayList<ResourceRecord> additionalResourceRecords;

    /**
     * This is the constructor that initializes every field in the class
     *
     * @param header the Header class holding the information for the header
     * @param queries the ArrayList holding all queries sent to the server
     * @param answerResourceRecords the ArrayList holding all Answer
     *                              Resource Records
     * @param authorityResourceRecords the ArrayList holding all Authority
     *                                 Resource Records
     * @param additionalResourceRecords the ArrayList holding all Additional
     *                                  Resource Records
     */
    public Message(Header header, ArrayList<Query> queries, ArrayList<ResourceRecord> answerResourceRecords, ArrayList<ResourceRecord> authorityResourceRecords, ArrayList<ResourceRecord> additionalResourceRecords) {
        this.header = header;
        this.questions = queries;
        this.answerResourceRecords = answerResourceRecords;
        this.authorityResourceRecords = authorityResourceRecords;
        this.additionalResourceRecords = additionalResourceRecords;
    }

    /**
     * This constructor initializes a message that is a query so many fields
     * are not required
     *
     * @param name the hostname to search
     * @param type the String representing the message type
     * @param recursionDesired a boolean that indicates whether or not
     *                         recursion is desired
     */
    public Message(String name, String type, boolean recursionDesired) {
        this.header = new Header(false, 0, false, false, recursionDesired,
                false, 0,
                0, 1, 0, 0, 0);
        this.questions = new ArrayList<Query>();
        this.questions.add(new Query(name, Type.parseType(type), Class.IN));
    }

    /**
     * This is the constructor used to build a message from bytes in a
     * ByteBuffer
     *
     * @param data the ByteBuffer to parse
     */
    public Message(ByteBuffer data) {
        this.header = new Header(data);
        this.questions = new ArrayList<Query>();
        this.answerResourceRecords = new ArrayList<ResourceRecord>();
        this.authorityResourceRecords = new ArrayList<ResourceRecord>();
        this.additionalResourceRecords = new ArrayList<ResourceRecord>();
        for(int i = 0; i < header.getTotalQuestions();i++) {
            this.questions.add(new Query(data));
        }
        for(int i = 0; i < header.getTotalAnswerResourceRecords();i++) {
            this.answerResourceRecords.add(new ResourceRecord(data));
        }
        for(int i = 0; i < header.getTotalAuthorityResourceRecords();i++) {
            this.authorityResourceRecords.add(new ResourceRecord(data));
        }
        for(int i = 0; i < header.getTotalAdditionalResourceRecords();i++) {
            this.additionalResourceRecords.add(new ResourceRecord(data));
        }
    }

    /**
     * This method returns a ByteBuffer with all the byte representations of
     * the Messages information
     *
     * @return the ByteBuffer representing the Message
     */
    public ByteBuffer getByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byteBuffer.put(header.getByteBuffer().array());
        for (Query query : questions) {
            byteBuffer.put(query.getByteBuffer().array());
        }
        return byteBuffer;
    }

    /**
     * This method returns a readable string with the values of the Message
     *
     * @return the readable String
     */
    public String toString() {
        String message = "Header: \n";
        String lines = "-------------------------------------------\n";
        message += this.header.toString();
        message += lines;
        message += "Queries: \n";
        for(int i = 0; i < header.getTotalQuestions();i++) {
            message += this.questions.get(i).toString();
            message += lines;
        }
        message += "Answers: \n";
        for(int i = 0; i < header.getTotalAnswerResourceRecords();i++) {
            message += this.answerResourceRecords.get(i).toString();
            message += lines;
        }
        message += "Authority: \n";
        for(int i = 0; i < header.getTotalAuthorityResourceRecords();i++) {
            message += this.authorityResourceRecords.get(i).toString();
            message += lines;
        }
        message += "Additional: \n";
        for(int i = 0; i < header.getTotalAdditionalResourceRecords();i++) {
            message += this.additionalResourceRecords.get(i).toString();
            message += lines;
        }
        return message;
    }
}
