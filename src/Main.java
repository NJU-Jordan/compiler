import org.antlr.runtime.tree.TreeWizard;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;


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
         //      System.out.println(toDEC("0"));
        if (args.length < 1) {
            System.err.println("input path is required");
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);
        //  CharStream input = CharStreams.fromFileName("src/input.txt");
        SysYLexer sysYLexer = new SysYLexer(input);



        CommonTokenStream tokens = new CommonTokenStream(sysYLexer);
        SysYParser sysYParser = new SysYParser(tokens);
        ErrorListener errorListener=new ErrorListener();
        sysYParser.removeErrorListeners();
        sysYParser.addErrorListener(errorListener);
        ParseTree tree = sysYParser.program();
        //Visitor extends SysYParserBaseVisitor<Void>
        Visitor visitor = new Visitor(sysYLexer,sysYParser);
        if(errorListener.hasErr==1){

        }
        else{

            visitor.visit(tree);
        }

    }


}

