import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.util.List;

public class Playground {
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
        System.out.println(toDEC("0"));


        CharStream input = CharStreams.fromFileName("src/input.txt");
        SysYLexer sysYLexer = new SysYLexer(input);
        sysYLexer.removeErrorListeners();
        //   ErrorListener myErrorListener=new ErrorListener();
        //   sysYLexer.addErrorListener(myErrorListener);
        List<?extends Token> tokens =sysYLexer.getAllTokens();
        System.err.println(tokens);
        for(Token token:tokens){
            if(sysYLexer.getRuleNames()[token.getType()-1].equals("INTEGR_CONST")){
                int  num=toDEC(token.getText());
                System.err.println(sysYLexer.getRuleNames()[token.getType()-1]+" "+num+" at Line "+token.getLine()+".");
            }
            else System.err.println(sysYLexer.getRuleNames()[token.getType()-1]+" "+token.getText()+" at Line "+token.getLine()+".");
        }


    }
}
