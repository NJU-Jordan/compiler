import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.stringtemplate.v4.NoIndentWriter;

import java.util.ArrayList;

public class SymbolTableListener extends SysYParserBaseListener{
    public GlobalScope globalScope = null;
    public Scope currentScope = null;
    int localScopeCounter= 0;

    public boolean hasErr=false;
    private final ParseTreeProperty<Type> typeProperty=new ParseTreeProperty<>();
    private final ParseTreeProperty<String> idProperty=new ParseTreeProperty<>();

    private boolean isValidFuc=true;  //判断是否
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

        Type retTy=(Type) globalScope.resolve(typeName);
        String funName=ctx.IDENT().getText();

        //暂时不放入paramsType
        FunctionType functionType=new FunctionType( retTy,null);
        FunctionSymbol fun=new FunctionSymbol(funName,functionType,currentScope);

        //函数本身还是符号;需要在全局作用域定义
        if(currentScope.getSymbols().get(funName)!=null)
        {   isValidFuc=false;
            hasErr=true;
            System.err.println("Error type 4 at Line "+ctx.start.getLine()+": Redefined function: " + funName);
        }
        else  currentScope.define(fun);
        currentScope=fun;
        //Todo
        //如果重定义的函数，无需进入函数体



    }




    public void enterBlock(SysYParser.BlockContext ctx) {
        if(!isValidFuc) {
           return ;
        }
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
        isValidFuc = true;
        currentScope=currentScope.getEnclosingScope();
    }
    public void exitBlock(SysYParser.BlockContext ctx){
        if(!isValidFuc) {
            return ;
        }
        currentScope=currentScope.getEnclosingScope();
    }

    //varDecl : bType varDef ( COMMA varDef ) * SEMICOLON
    //        ;
    //
    //varDef  : IDENT ( L_BRACKT constExp R_BRACKT ) *
    //        | IDENT ( L_BRACKT  constExp R_BRACKT  )* ASSIGN initVal
    //        ;
    public void exitConstDef(SysYParser.ConstDefContext ctx) {
        if(!isValidFuc) {
            return ;
        }
        SysYParser.ConstDeclContext parent_ctx =parent_ctx= (SysYParser.ConstDeclContext) ctx.parent;
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
            hasErr=true;
            System.err.println("Error type 3 at Line "+ctx.start.getLine()+": Redefined variable: " + varName);
        }
        else currentScope.define(var);

    }

    //什么时候定义Symbol

    //普通情况下定义变量
    //varDef  : IDENT ( L_BRACKT constExp R_BRACKT ) *
    //        | IDENT ( L_BRACKT  constExp R_BRACKT  )* ASSIGN initVal
    //        ;
    public void exitVarDef(SysYParser.VarDefContext ctx) {
        if(!isValidFuc) {
            return ;
        }
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
            hasErr=true;
            System.err.println("Error type 3 at Line "+ctx.start.getLine()+": Redefined variable: " + varName);
        }
        else currentScope.define(var);

    }



    //funcDef : funcType IDENT L_PAREN (funcFParams)? R_PAREN block ;

    public void exitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        if(!isValidFuc) {
            return ;
        }
        SysYParser.FuncDefContext parent_ctx=(SysYParser.FuncDefContext)ctx.parent;
        String funcName=parent_ctx.IDENT().getText();
       //
        ArrayList<Type> paramsType=new ArrayList<>();
       for(SysYParser.FuncFParamContext funcFParamContext:ctx.funcFParam()){
            Type paramType=typeProperty.get(funcFParamContext);
           if(!(paramType instanceof NoneType)){
              paramsType.add(paramType);
          }
       }

        //Todo: find functionSymbol
       Symbol symbol= globalScope.resolve(funcName);
       FunctionType functionType=(FunctionType) symbol.getType();
       functionType.setParamsType(paramsType);
    }

    //定义函数形参中的变量
    // funcFParam : bType IDENT (L_BRACKT  R_BRACKT (L_BRACKT  exp R_BRACKT )* )?
    // int [][exp];
    public void exitFuncFParam(SysYParser.FuncFParamContext ctx) {
        if(!isValidFuc) {
            return ;
        }
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
            hasErr=true;
            System.err.println("Error type 3 at Line "+ctx.start.getLine()+": Redefined variable: " + varName);
        }
        currentScope.define(var);


    }


    //什么时候解析变量


    //IDENT (L_BRACKT exp R_BRACKT)*  id[ ][ ]
    @Override
    public void exitLVal(SysYParser.LValContext ctx) {
        if(!isValidFuc) {
            return ;
        }
        String varName=ctx.IDENT().getText();
        //在lval节点上附上其类型信息，处理ID
        if( currentScope.resolve(varName)==null){
            //如果是未定义的变量，报错
            typeProperty.put(ctx,new NoneType());
            hasErr=true;
            System.err.println("Error type 1 at Line "+ctx.start.getLine()+": Undefined variable: " + varName);
        }


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
                else if(dimen<0){
                    hasErr=true;
                    System.err.println("Error type 9 at Line "+ctx.start.getLine()+": Not an array: "+varName);
                    target_type=new NoneType();
                }else target_type=basictype;



            }
            else {

                //对非数组使用下标
                if(ctx.exp().size()>0) {
                    target_type=new NoneType();
                    hasErr=true;
                    System.err.println("Error type 9 at Line "+ctx.start.getLine()+": Not an array: "+varName);
                }
                else target_type=var_type;
            }

            typeProperty.put(ctx,target_type);

        }


    }

    //函数调用时检查是否使用没有声明和定义的函数
    public void enterCall(SysYParser.CallContext ctx) {
        if(!isValidFuc) {
            return ;
        }
        String name=ctx.IDENT().getText();
        Symbol symbol=globalScope.resolve(name);
       if( symbol==null){
           typeProperty.put(ctx,new NoneType());
           hasErr=true;
           System.err.println("Error type 2 at Line "+ctx.start.getLine()+": Undefined function: " + name);
       }
       else  {
           Type type;
           if(symbol instanceof VariableSymbol){
               type=new NoneType();
               hasErr=true;
               System.err.println("Error type 10 at Line "+ctx.start.getLine()+": Not a function: "+name);
           }
           else{
               type= symbol.getType();

           }
           typeProperty.put(ctx,type);

       }

    }
   //  IDENT L_PAREN funcRParams? R_PAREN
    public void exitCall(SysYParser.CallContext ctx){
        if(!isValidFuc) {
            return ;
        }
        Type type=typeProperty.get(ctx);
        if(!(type instanceof NoneType)){
            ArrayList<Type> rparams=new ArrayList<>();
            int rparams_cnt=0;
            int fparams_cnt=0;
            boolean paramfault=false;
            if(ctx.funcRParams()!=null){
                rparams_cnt=ctx.funcRParams().param().size();
                for(SysYParser.ParamContext paramContext:ctx.funcRParams().param()){
                    Type paramtype=typeProperty.get(paramContext);
                    if(paramtype instanceof NoneType) paramfault=true;
                }

            }

            if(!paramfault){
                String funcName=ctx.IDENT().getText();
                FunctionType functionType=(FunctionType) typeProperty.get(ctx);
                if(functionType.paramsType!=null) fparams_cnt=functionType.paramsType.size();
                if(fparams_cnt!= rparams_cnt){
                    typeProperty.put(ctx,new NoneType());
                    hasErr=true;
                    System.err.println("Error type 8 at Line "+ctx.start.getLine()+": Function is not applicable for arguments.");
                }
                else typeProperty.put(ctx,functionType.retTy);
            }
            else typeProperty.put(ctx,new NoneType());




        }
    }
    //param (COMMA param)*
    public void exitFuncRParams(SysYParser.FuncRParamsContext ctx){


//       boolean paramfault=false;
//        ArrayList<Type> paramsType=new ArrayList<>();
//       for(SysYParser.ParamContext paramContext :ctx.param()){
//            Type paramType=typeProperty.get(paramContext);
//           if(paramType instanceof NoneType){
//                paramfault=true;
//          }
//       }
//      if(paramfault) typeProperty.put(ctx,new NoneType());
    }

    public void exitParam(SysYParser.ParamContext ctx){
        if(!isValidFuc) {
            return ;
        }
        typeProperty.put(ctx,typeProperty.get(ctx.exp()));
    }

    @Override public void exitExpLVal(SysYParser.ExpLValContext ctx) {
        if(!isValidFuc) {
            return ;
        }

        typeProperty.put(ctx, typeProperty.get(ctx.lVal()));
    }
        //处理等号右边的整数
    public void exitExpNumber(SysYParser.ExpNumberContext ctx) {
        if(!isValidFuc) {
            return ;
        }
        typeProperty.put(ctx, new BasicTypeSymbol("int"));

    }
    public void exitUnary(SysYParser.UnaryContext ctx){
        if(!isValidFuc) {
            return ;
        }
        Type type=typeProperty.get(ctx.exp());
        if(!(type instanceof BasicTypeSymbol)) {
            if(!(type instanceof NoneType)){
                hasErr=true;
                System.err.println("Error type 6 at Line "+ctx.start.getLine()+": Type mismatched for operands.");
            }
            typeProperty.put(ctx,new NoneType());
        }
        else typeProperty.put(ctx,new BasicTypeSymbol("int"));
    }

    @Override
    public void exitMulDivMod(SysYParser.MulDivModContext ctx) {
        if(!isValidFuc) {
            return ;
        }
       Type lhs=typeProperty.get(ctx.lhs);
       Type rhs=typeProperty.get(ctx.rhs);
       if(!(lhs instanceof BasicTypeSymbol) || !(rhs instanceof BasicTypeSymbol)){
           if(!(lhs instanceof NoneType) && !(rhs instanceof NoneType)){
               hasErr=true;
               System.err.println("Error type 6 at Line "+ctx.start.getLine()+": Type mismatched for operands.");
           }

           typeProperty.put(ctx,new NoneType());
       }
       else typeProperty.put(ctx,new BasicTypeSymbol("int"));
    }

    public void exitPlusMinus(SysYParser.PlusMinusContext ctx){
        if(!isValidFuc) {
            return ;
        }
        Type lhs=typeProperty.get(ctx.lhs);
        Type rhs=typeProperty.get(ctx.rhs);
        if(!(lhs instanceof BasicTypeSymbol) || !(rhs instanceof BasicTypeSymbol)){
            if(!(lhs instanceof NoneType)&&!(rhs instanceof NoneType)){
                //只有左操作数和右操作数均不报错时，才报错
                hasErr=true;
                System.err.println("Error type 6 at Line "+ctx.start.getLine()+": Type mismatched for operands.");
            }
            typeProperty.put(ctx,new NoneType());

        }
        else typeProperty.put(ctx,new BasicTypeSymbol("int"));
    }

    //处理cond

    public void exitCondExp(SysYParser.CondExpContext ctx){
        if(!isValidFuc) {
            return ;
        }

        typeProperty.put(ctx, typeProperty.get(ctx.exp()));
    }
    public void exitLG(SysYParser.LGContext ctx){
        if(!isValidFuc) {
            return ;
        }
        Type lhs=typeProperty.get(ctx.lhs);
        Type rhs=typeProperty.get(ctx.rhs);
        if(!(lhs instanceof BasicTypeSymbol) || !(rhs instanceof BasicTypeSymbol)){
            if(!(lhs instanceof NoneType)&&!(rhs instanceof NoneType)){
                //只有左操作数和右操作数均不报错时，才报错
                hasErr=true;
                System.err.println("Error type 6 at Line "+ctx.start.getLine()+": Type mismatched for operands.");
            }
            typeProperty.put(ctx,new NoneType());

        }
        else typeProperty.put(ctx,new BasicTypeSymbol("int"));
    }
    public void exitENQ(SysYParser.ENQContext ctx) {

        if(!isValidFuc) {
            return ;
        }
        Type lhs=typeProperty.get(ctx.lhs);
        Type rhs=typeProperty.get(ctx.rhs);
        if(!(lhs instanceof BasicTypeSymbol) || !(rhs instanceof BasicTypeSymbol)){
            if(!(lhs instanceof NoneType)&&!(rhs instanceof NoneType)){
                //只有左操作数和右操作数均不报错时，才报错
                hasErr=true;
                System.err.println("Error type 6 at Line "+ctx.start.getLine()+": Type mismatched for operands.");
            }
            typeProperty.put(ctx,new NoneType());

        }
        else typeProperty.put(ctx,new BasicTypeSymbol("int"));
    }
    public void exitAnd(SysYParser.AndContext ctx) {
        if(!isValidFuc) {
            return ;
        }
        Type lhs=typeProperty.get(ctx.lhs);
        Type rhs=typeProperty.get(ctx.rhs);
        if(!(lhs instanceof BasicTypeSymbol) || !(rhs instanceof BasicTypeSymbol)){
            if(!(lhs instanceof NoneType)&&!(rhs instanceof NoneType)){
                //只有左操作数和右操作数均不报错时，才报错
                hasErr=true;
                System.err.println("Error type 6 at Line "+ctx.start.getLine()+": Type mismatched for operands.");
            }
            typeProperty.put(ctx,new NoneType());

        }
        else typeProperty.put(ctx,new BasicTypeSymbol("int"));
    }
    public void exitOr(SysYParser.OrContext ctx) {
        if(!isValidFuc) {
            return ;
        }
        Type lhs=typeProperty.get(ctx.lhs);
        Type rhs=typeProperty.get(ctx.rhs);
        if(!(lhs instanceof BasicTypeSymbol) || !(rhs instanceof BasicTypeSymbol)){
            if(!(lhs instanceof NoneType)&&!(rhs instanceof NoneType)){
                //只有左操作数和右操作数均不报错时，才报错
                hasErr=true;
                System.err.println("Error type 6 at Line "+ctx.start.getLine()+": Type mismatched for operands.");
            }
            typeProperty.put(ctx,new NoneType());

        }
        else typeProperty.put(ctx,new BasicTypeSymbol("int"));
    }
    public void enterAssignStmt(SysYParser.AssignStmtContext ctx) {
     //   System.out.println("enter assignstmt!");;
    }
    //检查stmt中赋值号两侧类型是否匹配
    //lVal ASSIGN exp SEMICOLON  # AssignStmt
    public void exitAssignStmt(SysYParser.AssignStmtContext ctx) {
        if(!isValidFuc) {
            return ;
        }
        Type lhs=typeProperty.get(ctx.lhs);
        Type rhs=typeProperty.get(ctx.rhs);
        boolean ismatch=false;
        if(lhs instanceof FunctionType){
            hasErr=true;
            System.err.println("Error type 11 at Line "+ctx.start.getLine()+": The left-hand side of an assignment must be a variable.");
        }
        else {
            if (lhs instanceof ArrayType && rhs instanceof ArrayType) {
                if (((ArrayType) lhs).dimen == ((ArrayType) rhs).dimen) ismatch = true;

            } else if (lhs instanceof BasicTypeSymbol && rhs instanceof ArrayType) {
                if (((ArrayType) rhs).dimen == 0) ismatch = true;

            } else if (lhs instanceof ArrayType && rhs instanceof BasicTypeSymbol) {
                if (((ArrayType) lhs).dimen == 0) ismatch = true;
            } else if (lhs instanceof BasicTypeSymbol && rhs instanceof BasicTypeSymbol) {
                ismatch = true;
            }
            //若等号右边有报操作符错误，则无需继续报错
            if (!ismatch && !(rhs instanceof NoneType)){
                hasErr=true;
                System.err.println("Error type 5 at Line " + ctx.start.getLine() + ": Type mismatched for assignment.");
            }

        }
    }
    //RETURN (exp)? SEMICOLON  return a+2;
    public void exitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        if(!isValidFuc) {
            return ;
        }

       Type type=typeProperty.get(ctx.exp());

       if(currentScope instanceof LocalScope){
           Scope scope=currentScope;
           while(!(scope instanceof FunctionSymbol)){
               scope=scope.getEnclosingScope();
               //一直往上找父作用域名，直到找到函数作用域为止；
           }
           FunctionSymbol functionSymbol= ((FunctionSymbol) scope);
           Type retTy= ((FunctionType)functionSymbol.getType()).retTy;
           boolean ismatch=false;
            if(retTy instanceof BasicTypeSymbol && type instanceof BasicTypeSymbol){
                if(((BasicTypeSymbol) retTy).name.equals(((BasicTypeSymbol) type).name)) ismatch=true;


            }
            if(!ismatch && !(type instanceof NoneType)){
                hasErr=true;
                System.err.println("Error type 7 at Line " + ctx.start.getLine() + ": type.Type mismatched for return.");
            }

       }
    }




}
