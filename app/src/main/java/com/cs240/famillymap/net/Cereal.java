package com.cs240.famillymap.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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



    /*
     * The writeString method shows how to write a String to an OutputStream.
     * (Courtesy of class notes)
     */
    public static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
