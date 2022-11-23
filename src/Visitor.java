import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

public class Visitor extends  SysYParserBaseVisitor{
    private SysYLexer sysYLexer;
    private SysYParser sysYParser;

    private Vocabulary vocabulary;
    private int cur_depth;  //记录当前访问树的深度
    private String[] rule_with_colors= new String[] {
         "","CONST[orange]", "INT[orange]", "VOID[orange]", "IF[orange]", "ELSE[orange]", "WHILE[orange]", "BREAK[orange]", "CONTINUE[orange]",
                "RETURN[orange]", "PLUS[blue]", "MINUS[blue]", "MUL[blue]", "DIV[blue]", "MOD[blue]", "ASSIGN[blue]", "EQ[blue]", "NEQ[blue]",
                "LT[blue]", "GT[blue]", "LE[blue]", "GE[blue]", "NOT[blue]", "AND[blue]", "OR[blue]", "", "", "",
                "", "", "", "", "", "IDENT[red]", "INTEGR_CONST[green]",
                "","","","", "", ""
    };

    public Visitor(SysYLexer sysYLexer, SysYParser sysYParser){
        this.sysYLexer=sysYLexer;
        this.sysYParser=sysYParser;
        this.vocabulary=sysYLexer.getVocabulary();
    }
    public static String indent_of_depth(int depth){
        String indent="";
        for(int i=0;i<depth-1;i++){
            indent+="  ";
        }
        return indent;
    }
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
    @Override
    public Object visitChildren(RuleNode node) {

        int index=node.getRuleContext().getRuleIndex();

        cur_depth=node.getRuleContext().depth();
        String rulename=sysYParser.getRuleNames()[index];
        String pro_rulename=rulename.substring(0,1).toUpperCase()+rulename.substring(1);
        System.err.println(indent_of_depth(cur_depth)+pro_rulename);

        return super.visitChildren(node);
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
       String text=node.getText();
       int type=node.getSymbol().getType();
       cur_depth++;

       if(type!=-1&&rule_with_colors[type].length()!=0){
           if(type==SysYLexer.INTEGR_CONST){
               //进制转换
               text=String.valueOf(toDEC(text));
           }

           System.err.println(indent_of_depth(cur_depth)+text+" "+rule_with_colors[type]);
       }

      //  System.err.println(index);
        cur_depth--;
        return super.visitTerminal(node);
    }
}
