import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;


import java.io.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

public class Main
{
    public static Integer toDEC(String INTEGR_CONST){

        Integer result=0;
        if(INTEGR_CONST.startsWith("0")){

        }else if(INTEGR_CONST.startsWith("0x")){
            result =Integer.parseInt(INTEGR_CONST.substring(2),16);
        }
        return  result;
    }
    public static void main(String[] args) throws IOException {
        System.out.println(toDEC("0xff"));
        String hex = "fff";
        Integer x = Integer.parseInt("0xff".substring(2),16);
        System.out.println(x);
        if (args.length < 1) {
            System.err.println("input path is required");
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);
        SysYLexer sysYLexer = new SysYLexer(input);
        sysYLexer.removeErrorListeners();
     //   ErrorListener myErrorListener=new ErrorListener();
     //   sysYLexer.addErrorListener(myErrorListener);
      List<?extends Token> tokens =sysYLexer.getAllTokens();
        System.err.println(tokens);
        for(Token token:tokens){
            System.err.println(sysYLexer.getRuleNames()[token.getType()-1]+" "+token.getText()+" at Line "+token.getLine()+".");
        }


    }


}

