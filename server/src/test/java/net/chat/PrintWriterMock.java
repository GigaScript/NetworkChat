package net.chat;

import java.io.*;
import java.nio.charset.Charset;

public class PrintWriterMock extends PrintWriter {

    public PrintWriterMock(PrintWriter printWriter) {
        super(printWriter);
    }

    public PrintWriterMock(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrintWriterMock(OutputStream out) {
        super(out);
    }

    public PrintWriterMock(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrintWriterMock(OutputStream out, boolean autoFlush, Charset charset) {
        super(out, autoFlush, charset);
    }

    public PrintWriterMock(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public PrintWriterMock(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public PrintWriterMock(String fileName, Charset charset) throws IOException {
        super(fileName, charset);
    }

    public PrintWriterMock(File file) throws FileNotFoundException {
        super(file);
    }

    public PrintWriterMock(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    public PrintWriterMock(File file, Charset charset) throws IOException {
        super(file, charset);
    }


    public boolean isOpen() {
        return out != null;
    }

}
