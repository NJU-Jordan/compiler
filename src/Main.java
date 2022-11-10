import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;


import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main
{
    public static Integer toDEC(String INTEGR_CONST){

        Integer result;
        if(INTEGR_CONST.startsWith("0x")){
            result =Integer.parseInt(INTEGR_CONST.substring(2),16);
        }else if(INTEGR_CONST.startsWith("0")&&INTEGR_CONST.length()>=2){
            result = Integer.parseInt(INTEGR_CONST.substring(1),8);
        }
        else{
            result=Integer.parseInt(INTEGR_CONST);
        }
        return  result;
    }
    public static void main(String[] args) throws IOException {
 //       System.out.println(toDEC("0"));
// 命令行参数部分
        if (args.length < 1) {
            System.err.println("input path is required");
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);
//        CharStream input = CharStreams.fromFileName("src/input.txt");
        SysYLexer sysYLexer = new SysYLexer(input);
       sysYLexer.removeErrorListeners();
       ErrorListener myErrorListener=new ErrorListener();
       sysYLexer.addErrorListener(myErrorListener);
      List<?extends Token> tokens =sysYLexer.getAllTokens();
      Vocabulary vocabulary=sysYLexer.getVocabulary();
      //词汇表中的SysmbolicName不包含fragment
        if(myErrorListener.hasErr==1){
            //只输出错误信息
        }
        else{
            for(Token token:tokens){
                if(sysYLexer.getRuleNames()[token.getType()-1].equals("INTEGR_CONST")){
                    int  num=toDEC(token.getText());
                    System.err.println(vocabulary.getSymbolicName(token.getType())+" "+num+" at Line "+token.getLine()+".");
                }
                else System.err.println(vocabulary.getSymbolicName(token.getType())+" "+token.getText()+" at Line "+token.getLine()+".");
            }
        }


    }


}

