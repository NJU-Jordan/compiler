import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

public class Visitor extends  SysYParserBaseVisitor{
    private SysYLexer sysYLexer;
    private SysYParser sysYParser;

    private Vocabulary vocabulary;
    private RuleNode cur_node;  //记录当前访问的节点
    private int cur_depth;
    private SymbolTableListener symbolTableListener;

    private GlobalScope globalScope = null;
    private Scope currentScope;
    int localScopeCounter= 0;

    int lineNo;
    int column;
    String rename;

    String replacedName;   //
    Scope effectReplacedScope;  //
    private String[] rule_with_colors= new String[] {
         "","CONST[orange]", "INT[orange]", "VOID[orange]", "IF[orange]", "ELSE[orange]", "WHILE[orange]", "BREAK[orange]", "CONTINUE[orange]",
                "RETURN[orange]", "PLUS[blue]", "MINUS[blue]", "MUL[blue]", "DIV[blue]", "MOD[blue]", "ASSIGN[blue]", "EQ[blue]", "NEQ[blue]",
                "LT[blue]", "GT[blue]", "LE[blue]", "GE[blue]", "NOT[blue]", "AND[blue]", "OR[blue]", "", "", "",
                "", "", "", "", "", "IDENT[red]", "INTEGR_CONST[green]",
                "","","","", "", ""
    };

    public Visitor(SysYLexer sysYLexer, SysYParser sysYParser,SymbolTableListener symbolTableListener){
        this.sysYLexer=sysYLexer;
        this.sysYParser=sysYParser;
        this.vocabulary=sysYLexer.getVocabulary();
        this.symbolTableListener=symbolTableListener;
    
    }

    public void setRenameInfo(int lineNo,int column, String rename){
        this.lineNo=lineNo;
        this.column=column;
        this.rename=rename;
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
        //访问每个节点的子节点前调用调用

        int index=node.getRuleContext().getRuleIndex();
       // System.err.println(sysYParser.getRuleContext());
        cur_depth++;
        String rulename=sysYParser.getRuleNames()[index];
        String pro_rulename=rulename.substring(0,1).toUpperCase()+rulename.substring(1);
        System.err.println(indent_of_depth(cur_depth)+pro_rulename);

        super.visitChildren(node);
        cur_depth--;
        return null;
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




    public Object visitProgram(SysYParser.ProgramContext ctx){
        globalScope=new GlobalScope(null);
        //进入作用域
        currentScope=globalScope;

        visitChildren(ctx);

        currentScope=currentScope.getEnclosingScope();

        return null;
    }
    public Object visitFuncDef(SysYParser.FuncDefContext ctx) {
        String typeName=ctx.funcType().getText();

        Type retTy=(Type) globalScope.resolve(typeName);
        String funName=ctx.IDENT().getText();

        //暂时不放入paramsType
        FunctionType functionType=new FunctionType( retTy,null);
        FunctionSymbol fun=new FunctionSymbol(funName,functionType,currentScope);

        //函数本身还是符号;需要在全局作用域定义

        currentScope.define(fun);
        currentScope=fun;

        visitChildren(ctx);

       // System.err.println(indent_of_depth(cur_depth)+"fuuuuuuunc!!!!!");
        currentScope=currentScope.getEnclosingScope();
        return null;
    }

    public Object visitBlock(SysYParser.BlockContext ctx){

        LocalScope localScope=new LocalScope(currentScope);
        String localScopeName=localScope.getName() + localScopeCounter;
        localScope.setName(localScopeName);
        localScopeCounter++;
        currentScope=localScope;

        visitChildren(ctx);

        currentScope=currentScope.getEnclosingScope();
        return null;
    }

    public Object visitVarDef(SysYParser.VarDefContext ctx) {

        SysYParser.VarDeclContext parent_ctx =parent_ctx= (SysYParser.VarDeclContext) ctx.parent;
        String typeName= parent_ctx.bType().getText();


        int dimen=ctx.constExp().size();
        Type basictype=(Type) currentScope.resolve(typeName);
        Type type;
        if(dimen> 0) {
            type=new ArrayType(dimen,basictype);
            //   System.err.println(dimen);
        }
        else type=basictype;

        String varName=ctx.IDENT().getText();
        VariableSymbol var=new VariableSymbol(varName,type);

        currentScope.define(var);

        return super.visitVarDef(ctx);
    }

    public Object visitFuncFParam(SysYParser.FuncFParamContext ctx) {

        String typeName= ctx.bType().getText();


        int dimen=ctx.exp().size();
        Type basictype=(Type) currentScope.resolve(typeName);
        Type type;
        if(dimen> 0) {
            type=new ArrayType(dimen,basictype);
            //   System.err.println(dimen);
        }
        else type=basictype;

        String varName=ctx.IDENT().getText();
        VariableSymbol var=new VariableSymbol(varName,type);




        //if(currentScope.getSymbols().get(varName)!=null)

        currentScope.define(var);

        return super.visitFuncFParam(ctx);
    }

}
