import org.antlr.runtime.tree.TreeWizard;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


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
//         //      System.out.println(toDEC("0"));
//        if (args.length < 4) {
//            System.err.println("input path is required");
//        }
//        String source = args[0];
//        int lineNo = Integer.parseInt(args[1]);
//        int column = Integer.parseInt(args[2]);
//        String name = args[3];

        int lineNo = 8;
        int column = 4;
        String name = "d";
        String source = "src/input.txt";
        CharStream input = CharStreams.fromFileName(source);
        //  CharStream input = CharStreams.fromFileName("src/input.txt");
        SysYLexer sysYLexer = new SysYLexer(input);



        CommonTokenStream tokens = new CommonTokenStream(sysYLexer);
        SysYParser sysYParser = new SysYParser(tokens);
        ErrorListener errorListener=new ErrorListener();
        sysYParser.removeErrorListeners();
        sysYParser.addErrorListener(errorListener);
        ParseTree tree = sysYParser.program();

        ParseTreeWalker walker=new ParseTreeWalker();
        SymbolTableListener symbolTableListener=new SymbolTableListener();
        walker.walk(symbolTableListener,tree);



//        Visitor extends SysYParserBaseVisitor<Void>
        Visitor visitor = new Visitor(sysYLexer,sysYParser,symbolTableListener);
        visitor.setRenameInfo(lineNo,column,name);
        if(symbolTableListener.hasErr==false){


            visitor.visit(tree); //create symbol  table
        }

   }


}

