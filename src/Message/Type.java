package Message;

/**
 * This enumeration is all of the types that need to be implemented
 *
 * @author Daniel Powell
 * @version 1.0
 */
public enum Type {
    A,
    CNAME,
    MX,
    PTR;

    /**
     * This method parses the string for the type and returns a Type
     * @param type the string representation of the type
     * @return the corresponding type
     */
    public static Type parseType(String type) {
        Type returnType = A;
        if(type.toLowerCase() == "cname") {
            returnType = CNAME;
        }
        else if(type.toLowerCase() == "mx") {
            returnType = MX;
        }
        else if(type.toLowerCase() == "ptr") {
            returnType = PTR;
        }
        return returnType;
    }

    /**
     * This method takes in an integer and matches it to the corresponding
     * Type value
     *
     * @param typeNum the number to match to a Type
     * @return the Type the number matches to
     */
    public static Type parseType(int typeNum) {
        Type type = null;
        switch (typeNum) {
            case 1:
                type = A;
                break;
            case 5:
                type = CNAME;
                break;
            case 15:
                type = MX;
                break;
            case 12:
                type = PTR;
                break;
        }
        return type;
    }
}
