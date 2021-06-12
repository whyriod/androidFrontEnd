package com.cs240.shared.cereal;

import java.io.*;

/***
 * Reads an input stream of Json into a string.
 * From: Class notes
 */
public class Cereal {

    /***
     * Takes an inputStream and creates an InputStream read. Reads input Json
     * to a string.
     * @param is - Input input stream.
     * @return - The stringified Json.
     * @throws IOException
     */
    public String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
