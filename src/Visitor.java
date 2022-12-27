import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Visitor extends  SysYParserBaseVisitor{
    int mode;
    private SysYLexer sysYLexer;
    private SysYParser sysYParser;

    private Vocabulary vocabulary;
    private RuleNode cur_node;  //记录当前访问的节点
    private int cur_depth;
    private SymbolTableListener symbolTableListener;

    private GlobalScope globalScope = new GlobalScope(null);
    private Scope currentScope;
    int localScopeCounter= 0;

    int lineNo;
    int column;


    int cur_lineNo;
    int cur_column;
    String rename;

    String replacedName;   //
    Symbol replacedSymbol;  //

    boolean is_call_name=false;
    private final ParseTreeProperty<String> idProperty=new ParseTreeProperty<>();
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
    public void setMode(int mode){
        this.mode= mode;
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
        //访问每个节点的子节点前调用
        if(node instanceof ParserRuleContext){
            ParserRuleContext ctx=(ParserRuleContext) node;
            cur_lineNo=ctx.start.getLine();
            cur_column=ctx.start.getCharPositionInLine();

        }


        int index=node.getRuleContext().getRuleIndex();
       // System.err.println(sysYParser.getRuleContext());
        cur_depth++;
        String rulename=sysYParser.getRuleNames()[index];
        String pro_rulename=rulename.substring(0,1).toUpperCase()+rulename.substring(1);
        if(mode==2) System.err.println(indent_of_depth(cur_depth)+pro_rulename);

        super.visitChildren(node);
        cur_depth--;
        return null;
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
        cur_lineNo=node.getSymbol().getLine();
        cur_column=node.getSymbol().getCharPositionInLine();
        String text=node.getText();
       if(mode==1&&findReplacedName()) {
           replacedName=text;

           replacedSymbol=currentScope.resolve(replacedName);
       }
       if(mode==2 &&isReplacedTarget(text)){

            if(cur_column==10) {
                cur_column=cur_column;
                isReplacedTarget(text);
            }
               text=rename;
       }


       int type=node.getSymbol().getType();

       cur_depth++;
       if(type!=-1&&rule_with_colors[type].length()!=0){
           if(type==SysYLexer.INTEGR_CONST){
               //进制转换
               text=String.valueOf(toDEC(text));
           }

        if(mode==2)   System.err.println(indent_of_depth(cur_depth)+text+" "+rule_with_colors[type]);
       }

      //  System.err.println(index);
        cur_depth--;


       //refresh lineno && column
       // cur_column=cur_column+text.length();
        return super.visitTerminal(node);
    }




    public Object visitProgram(SysYParser.ProgramContext ctx){

            //进入作用域
            currentScope = globalScope;

            visitChildren(ctx);

            currentScope = globalScope;
         //   currentScope = currentScope.getEnclosingScope();

        return null;

    }
    public Object visitFuncDef(SysYParser.FuncDefContext ctx) {
        String funName = ctx.IDENT().getText();
        if (mode == 0) {

            String typeName = ctx.funcType().getText();

            Type retTy = (Type) globalScope.resolve(typeName);


            //暂时不放入paramsType
            FunctionType functionType = new FunctionType(retTy, null);
            FunctionSymbol fun = new FunctionSymbol(funName, functionType, currentScope);

            //函数本身还是符号;需要在全局作用域定义

            currentScope.define(fun);
            currentScope.addDerivedScope(fun);
            currentScope = fun;
        }
        else {
            FunctionSymbol functionSymbol=(FunctionSymbol) currentScope.resolve(funName);
            FunctionType functionType=(FunctionType) functionSymbol.functionType;
            if(functionType.paramsType==null)
                currentScope=currentScope.nextDerivedScope();
        }

            visitChildren(ctx);

            // System.err.println(indent_of_depth(cur_depth)+"fuuuuuuunc!!!!!");
            currentScope = currentScope.getEnclosingScope();

        return null;
    }

    // if no param no enter
    public Object visitFuncFParams(SysYParser.FuncFParamsContext ctx){
        if(mode==0) {

        }
        else
            currentScope=currentScope.nextDerivedScope(); // enter function scope



        super.visitFuncFParams(ctx);
        if(ctx.funcFParam().size()!=0) {
            ArrayList<Type> paramsType=new ArrayList<>();
            paramsType.add(new BasicTypeSymbol("int"));
            SysYParser.FuncDefContext parent_ctx=(SysYParser.FuncDefContext)ctx.parent;
            String funcName=parent_ctx.IDENT().getText();
            Symbol symbol= globalScope.resolve(funcName);
            FunctionType functionType=(FunctionType) symbol.getType();
            functionType.setParamsType(paramsType);
        }
        return null;
    }
    public Object visitBlock(SysYParser.BlockContext ctx){

        if(mode==0) {


            LocalScope localScope = new LocalScope(currentScope);
            String localScopeName = localScope.getName() + localScopeCounter;
            localScope.setName(localScopeName);
            localScopeCounter++;
            currentScope.addDerivedScope(localScope);
            currentScope = localScope;

        }
        else  currentScope=currentScope.nextDerivedScope();
            visitChildren(ctx);

            currentScope = currentScope.getEnclosingScope();
            return null;

    }
   // constDef : IDENT ( L_BRACKT constExp R_BRACKT )* ASSIGN constInitVal ;
    public Object visitConstDef(SysYParser.ConstDefContext ctx) {



        SysYParser.ConstDeclContext parent_ctx = parent_ctx = (SysYParser.ConstDeclContext) ctx.parent;
        String typeName = parent_ctx.bType().getText();


        int dimen = ctx.constExp().size();
        Type basictype = (Type) currentScope.resolve(typeName);
        Type type;
        if (dimen > 0) {
            type = new ArrayType(dimen, basictype);
            //   System.err.println(dimen);
        } else type = basictype;

        String varName = ctx.IDENT().getText();
        VariableSymbol var = new VariableSymbol(varName, type);

        if(mode==0)    currentScope.define(var);


        return super.visitConstDef(ctx);
    }
    public Object visitVarDef(SysYParser.VarDefContext ctx) {



            SysYParser.VarDeclContext parent_ctx  = (SysYParser.VarDeclContext) ctx.parent;
            String typeName = parent_ctx.bType().getText();


            int dimen = ctx.constExp().size();
            Type basictype = (Type) currentScope.resolve(typeName);
            Type type;
            if (dimen > 0) {
                type = new ArrayType(dimen, basictype);
                //   System.err.println(dimen);
            } else type = basictype;

            String varName = ctx.IDENT().getText();
            VariableSymbol var = new VariableSymbol(varName, type);

        if(mode==0)    currentScope.define(var);


      return super.visitVarDef(ctx);
    }

    public Object visitFuncFParam(SysYParser.FuncFParamContext ctx) {



            String typeName = ctx.bType().getText();


            int dimen = ctx.exp().size();
            Type basictype = (Type) currentScope.resolve(typeName);
            Type type;
            if (dimen > 0) {
                type = new ArrayType(dimen, basictype);
                //   System.err.println(dimen);
            } else type = basictype;

            String varName = ctx.IDENT().getText();
            VariableSymbol var = new VariableSymbol(varName, type);


            //if(currentScope.getSymbols().get(varName)!=null)

         if(mode==0)   currentScope.define(var);
       return super.visitFuncFParam(ctx);
    }
    public Object visitLVal(SysYParser.LValContext ctx) {


        return visitChildren(ctx);
    }

    public Object visitCall(SysYParser.CallContext ctx) {
        is_call_name=true;
        return visitChildren(ctx);
    }
    public boolean isReplacedTarget(String name){
        boolean result=false;
        if(is_call_name) {
            result=globalScope.resolve(name)==replacedSymbol;
            is_call_name=false;
        }
        else result=currentScope.resolve(name)==replacedSymbol;
        return result;
    }

    public boolean findReplacedName(){
        return   lineNo==cur_lineNo&&column==cur_column;
    }
}
