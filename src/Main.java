import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;


import java.io.*;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("input path is required");
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);
        SysYLexer sysYLexer = new SysYLexer(input);
        sysYLexer.removeErrorListeners();
     //   ErrorListener myErrorListener=new ErrorListener();
     //   sysYLexer.addErrorListener(myErrorListener);
        System.out.println(sysYLexer.getAllTokens());

    }


}

