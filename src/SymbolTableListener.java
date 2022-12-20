public class SymbolTableListener extends SysYParserBaseListener{
    private GlobalScope globalScope = null;
    private Scope currentScope = null;
    int localScopeCounter= 0;

  //  boolean detectErr=false;
    //开启新的Scope
    @Override
    public void enterProgram(SysYParser.ProgramContext ctx) {
        globalScope=new GlobalScope(null);
        //进入作用域
        currentScope=globalScope;

    }

    public void enterFuncDef(SysYParser.FuncDefContext ctx){
        String typeName=ctx.funcType().getText();

        globalScope.resolve(typeName);
        String funName=ctx.IDENT().getText();
        FunctionSymbol fun=new FunctionSymbol(funName,currentScope);

        //函数本身还是符号;需要在全局作用域定义
        if(currentScope.getSymbols().get(funName)!=null) System.err.println("Error type 4 at Line "+ctx.start.getLine()+": Redefined function: " + funName);
        currentScope.define(fun);
        currentScope=fun;



    }

    public void enterBlock(SysYParser.BlockContext ctx) {

        LocalScope localScope=new LocalScope(currentScope);
        String localScopeName=localScope.getName() + localScopeCounter;
        localScope.setName(localScopeName);
        localScopeCounter++;
        currentScope=localScope;

    }


    public void exitProgram(SysYParser.ProgramContext ctx){
        currentScope=currentScope.getEnclosingScope();
    }

    public void exitFuncDef(SysYParser.FuncDefContext ctx){
        currentScope=currentScope.getEnclosingScope();
    }
    public void exitBlock(SysYParser.BlockContext ctx){
        currentScope=currentScope.getEnclosingScope();
    }


    //什么时候定义Symbol

    //普通情况下定义变量
    public void exitVarDef(SysYParser.VarDefContext ctx) {
        SysYParser.VarDeclContext parent_ctx =parent_ctx= (SysYParser.VarDeclContext) ctx.parent;
        String typeName= parent_ctx.bType().getText();
        Type type=(Type) currentScope.resolve(typeName);

        String varName=ctx.IDENT().getText();
        VariableSymbol var=new VariableSymbol(varName,type);
        if(currentScope.getSymbols().get(varName)!=null) System.err.println("Error type 3 at Line "+ctx.start.getLine()+": Redefined variable: " + varName);
        currentScope.define(var);

    }


    //定义函数形参中的变量
    public void exitFuncFParam(SysYParser.FuncFParamContext ctx) {

        String typeName=ctx.IDENT().getText();
        Type type=(Type) globalScope.resolve(typeName);

        String varName=ctx.IDENT().getText();
        VariableSymbol var=new VariableSymbol(varName,type);
        //if(currentScope.getSymbols().get(varName)!=null)
        currentScope.define(var);


    }


    //什么时候解析变量


    //IDENT (L_BRACKT exp R_BRACKT)*  id[ ][ ]
    @Override
    public void exitLVal(SysYParser.LValContext ctx) {
        String varName=ctx.IDENT().getText();
       if( currentScope.resolve(varName)==null)
           System.err.println("Error type 1 at Line "+ctx.start.getLine()+": Undefined variable: " + varName);
    }

    //函数调用时检查是否使用没有声明和定义的函数
    public void enterCall(SysYParser.CallContext ctx) {
        String funcName=ctx.IDENT().getText();
       if( currentScope.resolve(funcName)==null)
           System.err.println("Error type 2 at Line "+ctx.start.getLine()+": Undefined function: " + funcName);
    }
}
