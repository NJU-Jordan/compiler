import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Visitor extends  SysYParserBaseVisitor{
    private SysYLexer sysYLexer;
    private SysYParser sysYParser;
    public Visitor(SysYLexer sysYLexer, SysYParser sysYParser){
        this.sysYLexer=sysYLexer;
        this.sysYParser=sysYParser;
    }
    public static String indent_of_depth(int depth){
        String indent="";
        for(int i=0;i<depth;i++){
            indent+="  ";
        }
        return indent;
    }
    @Override
    public Object visitChildren(RuleNode node) {

        int index=node.getRuleContext().getRuleIndex();
        int depth=node.getRuleContext().depth();
        System.err.println(indent_of_depth(depth)+sysYParser.getRuleNames()[index]);
        return super.visitChildren(node);
    }

    @Override
    public Object visitTerminal(TerminalNode node) {

    //    int index=node.getSymbol().getTokenIndex();
  //      System.err.println(sysYLexer.getRuleNames()[index]);
        return super.visitTerminal(node);
    }
}
