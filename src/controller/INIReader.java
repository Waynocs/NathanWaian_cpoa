package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to parse an INI file or stream and extract its data
 */
public class INIReader {
    /**
     * Exception thrown when an error ocurred while parsing an INI file
     */
    public class ReadException extends Exception {
        private static final long serialVersionUID = 1L;

        public ReadException(String message) {
            super(message);
        }
    }

    private INIReader() {
    }

    /**
     * Parse an INI formatted stream
     * 
     * @param stream INI formatted stream to parse
     * @return the structure of the INI formatted stream
     */
    public static Map<String, Map<String, String>> ReadFromStream(InputStream stream) {
        return ReadFromReader(new InputStreamReader(stream));
    }

    /**
     * Parse an INI file
     * 
     * @param file INI file to parse
     * @return the structure of the file
     */
    public static Map<String, Map<String, String>> ReadFromFile(File file) {
        try {
            return ReadFromReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Parse an INI structure from a Reader
     * 
     * @param reader INI structure to parse
     * @return the structure of the Reader
     */
    public static Map<String, Map<String, String>> ReadFromReader(Reader reader) {
        var object = new HashMap<String, Map<String, String>>();

        try {
            int number;
            int state = 0;
            boolean comment = false;
            String currSectionName = null;
            Map<String, String> values = null;
            String currKey = null;
            String currVal = null;
            while ((number = reader.read()) != -1) {
                char c = (char) number;
                if (!comment)
                    switch (state) {
                        case 0: {
                            if (c == ';')
                                comment = true;
                            else if (Character.isWhitespace(c))
                                ;
                            else if (c == '[') {
                                state = 1;
                                currSectionName = "";
                            } else
                                return null;
                        }
                            break;
                        case 1: {
                            if (c == ']') {
                                values = new HashMap<String, String>();
                                currKey = "";
                                state = 2;
                            } else if (c == '\n')
                                return null;
                            else
                                currSectionName += c;
                        }
                            break;
                        case 2: {
                            if (c == ';')
                                comment = true;
                            else if (Character.isWhitespace(c))
                                ;
                            else if (c == '[') {
                                state = 1;
                                object.put(currSectionName, values);
                                currSectionName = "";
                            } else {
                                currKey += c;
                                state = 3;
                            }
                        }
                            break;
                        case 3: {
                            if (c == '=') {
                                state = 4;
                                currVal = "";
                            } else if (c == '\n')
                                return null;
                            else
                                currKey += c;
                        }
                            break;
                        case 4: {
                            if (c == ';') {
                                comment = true;
                                state = 2;
                                currVal = currVal.trim();
                                if (currVal.charAt(0) == '"' && currVal.charAt(currVal.length() - 1) == '"')
                                    currVal = currVal.substring(1, currVal.length() - 1);
                                values.put(currKey.trim(), currVal);
                                currKey = "";
                            } else if (c == '\n') {
                                state = 2;
                                currVal = currVal.trim();
                                if (currVal.charAt(0) == '"' && currVal.charAt(currVal.length() - 1) == '"')
                                    currVal = currVal.substring(1, currVal.length() - 1);
                                values.put(currKey.trim(), currVal);
                                currKey = "";
                            } else
                                currVal += c;
                        }
                            break;
                    }
                else if (c == '\n')
                    comment = false;
            }
            if (currSectionName != null && values != null) {
                if (currVal != null) {
                    currVal = currVal.trim();
                    if (currVal.charAt(0) == '"' && currVal.charAt(currVal.length() - 1) == '"')
                        currVal = currVal.substring(1, currVal.length() - 1);
                    values.put(currKey.trim(), currVal);
                }
                object.put(currSectionName, values);
            }
        } catch (IOException e) {
            return null;
        }

        return object;
    }
}