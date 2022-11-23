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
    @Override
    public Object visitChildren(RuleNode node) {

        int index=node.getRuleContext().getRuleIndex();
        cur_depth=node.getRuleContext().depth();
        System.err.println(indent_of_depth(cur_depth)+sysYParser.getRuleNames()[index]);
        return super.visitChildren(node);
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
       String text=node.getText();
       int type=node.getSymbol().getType();
       cur_depth++;
        System.err.println(indent_of_depth(cur_depth)+text+" "+vocabulary.getSymbolicName(type));
      //  System.err.println(index);
        cur_depth--;
        return super.visitTerminal(node);
    }
}