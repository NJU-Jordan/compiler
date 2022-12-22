import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;

public class SymbolTableListener extends SysYParserBaseListener{
    private GlobalScope globalScope = null;
    private Scope currentScope = null;
    int localScopeCounter= 0;

    private final ParseTreeProperty<Type> typeProperty=new ParseTreeProperty<>();

  //  boolean detectErr=false;
    //开启新的Scope
    @Override
    public void enterProgram(SysYParser.ProgramContext ctx) {
        globalScope=new GlobalScope(null);
        //进入作用域
        currentScope=globalScope;

    }
    //funcDef : funcType IDENT L_PAREN (funcFParams)? R_PAREN block ;
    public void enterFuncDef(SysYParser.FuncDefContext ctx){
        String typeName=ctx.funcType().getText();

        globalScope.resolve(typeName);
        String funName=ctx.IDENT().getText();

        //暂时不放入functionType
        FunctionSymbol fun=new FunctionSymbol(funName,null,currentScope);

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
    //varDef  : IDENT ( L_BRACKT constExp R_BRACKT ) *
    //        | IDENT ( L_BRACKT  constExp R_BRACKT  )* ASSIGN initVal
    //        ;
    public void exitVarDef(SysYParser.VarDefContext ctx) {
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
        if(currentScope.getSymbols().get(varName)!=null)
        {   typeProperty.put(ctx,new NoneType());
            System.err.println("Error type 3 at Line "+ctx.start.getLine()+": Redefined variable: " + varName);
        }
        else currentScope.define(var);

    }

  //  @Override
    public void exitFuncType(SysYParser.FuncTypeContext ctx) {
       // ctx.
    }

    //funcDef : funcType IDENT L_PAREN (funcFParams)? R_PAREN block ;
    public void exitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        SysYParser.FuncDefContext parent_ctx=(SysYParser.FuncDefContext)ctx.parent;
        String funcName=parent_ctx.IDENT().getText();
        Type retTy= (Type) currentScope.resolve(funcName);
        ArrayList<Type> paramsType=new ArrayList<>();
       for(SysYParser.FuncFParamContext funcFParamContext:ctx.funcFParam()){
            Type paramType=typeProperty.get(funcFParamContext);
           if(!(paramType instanceof NoneType)){
              paramsType.add(paramType);
          }
       }

       FunctionType functionType=new FunctionType(retTy,paramsType);
       currentScope.getSymbols().get(funcName).setType(functionType);
    }

    //定义函数形参中的变量
    // funcFParam : bType IDENT (L_BRACKT  R_BRACKT (L_BRACKT  exp R_BRACKT )* )?
    // int [][exp];
    public void exitFuncFParam(SysYParser.FuncFParamContext ctx) {

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
        if(currentScope.getSymbols().get(varName)!=null)
        {   typeProperty.put(ctx,new NoneType());
            System.err.println("Error type 3 at Line "+ctx.start.getLine()+": Redefined variable: " + varName);
        }
        currentScope.define(var);


    }


    //什么时候解析变量


    //IDENT (L_BRACKT exp R_BRACKT)*  id[ ][ ]
    @Override
    public void exitLVal(SysYParser.LValContext ctx) {
        String varName=ctx.IDENT().getText();
        //在lval节点上附上其类型信息，处理ID
        if( currentScope.resolve(varName)==null)
        //如果是未定义的变量，报错
            System.err.println("Error type 1 at Line "+ctx.start.getLine()+": Undefined variable: " + varName);

        else{

            Type var_type=currentScope.resolve(varName).getType();  //原始变量的类型
            Type target_type;
            if(var_type instanceof ArrayType){
                ArrayType arrayType=(ArrayType) var_type;
                int dimen=arrayType.dimen-ctx.exp().size();
                Type basictype= arrayType.basicType;
                 //加中括号后表达式的类型
                if(dimen> 0) {
                    target_type=new ArrayType(dimen,basictype);
                    //   System.err.println(dimen);
                }
                else target_type=basictype;



            }
            else target_type=var_type;
            typeProperty.put(ctx,target_type);

        }


    }

    //函数调用时检查是否使用没有声明和定义的函数
    public void enterCall(SysYParser.CallContext ctx) {
        String funcName=ctx.IDENT().getText();
       if( currentScope.resolve(funcName)==null)
           System.err.println("Error type 2 at Line "+ctx.start.getLine()+": Undefined function: " + funcName);
    }


    @Override public void exitExpLVal(SysYParser.ExpLValContext ctx) {

        typeProperty.put(ctx, typeProperty.get(ctx.lVal()));
    }
        //处理等号右边的整数
    public void exitExpNumber(SysYParser.ExpNumberContext ctx) {
        typeProperty.put(ctx, new BasicTypeSymbol("int"));

    }

    @Override
    public void exitMulDivMod(SysYParser.MulDivModContext ctx) {
       Type lhs=typeProperty.get(ctx.lhs);
       Type rhs=typeProperty.get(ctx.lhs);

    }

    public void enterPlusMinus(SysYParser.PlusMinusContext ctx){

    }

    public void enterAssignStmt(SysYParser.AssignStmtContext ctx) {
     //   System.out.println("enter assignstmt!");;
    }
    //检查stmt中赋值号两侧类型是否匹配
    //lVal ASSIGN exp SEMICOLON  # AssignStmt
    public void exitAssignStmt(SysYParser.AssignStmtContext ctx) {
        Type lhs=typeProperty.get(ctx.lhs);
        Type rhs=typeProperty.get(ctx.rhs);
        boolean ismatch=false;
        if(lhs instanceof ArrayType && rhs instanceof ArrayType){
            if(((ArrayType)lhs).dimen==((ArrayType)rhs).dimen) ismatch=true;

        }
        else if( lhs instanceof BasicTypeSymbol && rhs instanceof  ArrayType){
            if(((ArrayType)rhs).dimen==0) ismatch=true;

        }
        else if (lhs instanceof ArrayType && rhs instanceof  BasicTypeSymbol){
            if(((ArrayType)lhs).dimen==0) ismatch=true;
        }
        else if(lhs instanceof BasicTypeSymbol && rhs instanceof  BasicTypeSymbol){
            ismatch=true;
        }
        if(!ismatch)  System.err.println("Error type 5 at Line "+ctx.start.getLine()+": Type mismatched for assignment.");

    }
    public void exitReturnStmt(SysYParser.ReturnStmtContext ctx) {
     //  String returnName=ctx.exp().getText();
    }



}
