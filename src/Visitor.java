import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Visitor extends  SysYParserBaseVisitor{
    private SysYParser sysYParser;
    public Visitor(SysYParser sysYParser){
        this.sysYParser=sysYParser;
    }
    @Override
    public Object visitChildren(RuleNode node) {
        int index=node.getRuleContext().getRuleIndex();
        System.err.println(sysYParser.getRuleNames()[index]);
        return super.visitChildren(node);
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
        return super.visitTerminal(node);
    }
}
