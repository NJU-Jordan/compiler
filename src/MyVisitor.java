import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;

import java.util.List;

import static org.bytedeco.llvm.global.LLVM.*;


public class MyVisitor extends SysYParserBaseVisitor<LLVMValueRef> {
    ParseTreeProperty<LLVMValueRef> valueProperty=new ParseTreeProperty<>();
    ParseTreeProperty<Boolean>  isLoadedValue=new ParseTreeProperty<>();
    // pass down to identify if the value needed to be loaded   beginning state is null , if true ,set it true
    LLVMModuleRef module;
    LLVMBuilderRef builder;

    LLVMTypeRef i32Type;
    LLVMTypeRef voidType;


    LLVMValueRef zero ;

    private GlobalScope globalScope = null;
    private Scope currentScope = null;

    private int localScopeCounter = 0;


    public MyVisitor(){
        //初始化LLVM
        LLVMInitializeCore(LLVMGetGlobalPassRegistry());
        LLVMLinkInMCJIT();
        LLVMInitializeNativeAsmPrinter();
        LLVMInitializeNativeAsmParser();
        LLVMInitializeNativeTarget();

        //创建module
         module = LLVMModuleCreateWithName("moudle");

        //初始化IRBuilder，后续将使用这个builder去生成LLVM IR
        builder = LLVMCreateBuilder();

        //考虑到我们的语言中仅存在int一个基本类型，可以通过下面的语句为LLVM的int型重命名方便以后使用
        i32Type = LLVMInt32Type();
        voidType=LLVMVoidType();

        zero= LLVMConstInt(i32Type, 0, /* signExtend */ 0);

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
    public LLVMValueRef visit(ParseTree tree) {
        return super.visit(tree);
    }


    @Override
    public LLVMValueRef visitTerminal(TerminalNode node) {
        return   super.visitTerminal(node);
    }
    @Override
    public LLVMValueRef visitProgram(SysYParser.ProgramContext ctx) {
        globalScope = new GlobalScope(null);
        currentScope = globalScope;

        super.visitProgram(ctx);

        currentScope = currentScope.getEnclosingScope();
        return null;

    }

    @Override
    public LLVMValueRef visitCompUnit(SysYParser.CompUnitContext ctx) {
        return super.visitCompUnit(ctx);
    }


    @Override
    public LLVMValueRef visitFuncDef(SysYParser.FuncDefContext ctx) {
        //生成返回值类型



        String funName=ctx.IDENT().getText();
        int paramSize;
        if(ctx.funcFParams()!=null)
            paramSize=ctx.funcFParams().funcFParam().size();
        else paramSize=0;
        LLVMTypeRef returnType = ctx.funcType().INT()!=null?i32Type:voidType;

      //  生成函数参数类型
       PointerPointer<Pointer> argumentTypes = new PointerPointer<>(paramSize);
       for(int i=0;i<paramSize;i++) argumentTypes.put(i,i32Type);

        //生成函数类型
        LLVMTypeRef ft = LLVMFunctionType(returnType, argumentTypes, /* argumentCount */ paramSize, /* isVariadic */ 0);
        //若仅需一个参数也可以使用如下方式直接生成函数类型
     //   LLVMTypeRef ft = LLVMFunctionType(returnType, i32Type, /* argumentCount */ 0, /* isVariadic */ 0);

        //生成函数，即向之前创建的module中添加函数
        LLVMValueRef function = LLVMAddFunction(module, /*functionName:String*/funName, ft);
        currentScope.define(funName,function);
        //lab4  we have only one block
        //通过如下语句在函数中加入基本块，一个函数可以加入多个基本块
        LLVMBasicBlockRef block1 = LLVMAppendBasicBlock(function, /*blockName:String*/funName+"Entry");

        LLVMPositionBuilderAtEnd(builder, block1);//后续生成的指令将追加在block1的后面

        //

        FunctionScope functionScope=new FunctionScope(funName,currentScope);
        currentScope=functionScope;

        visitChildren(ctx);

        currentScope=currentScope.getEnclosingScope();


        return null;


    }
    @Override public LLVMValueRef visitBlock(SysYParser.BlockContext ctx) {

        LocalScope localScope = new LocalScope(currentScope);
        String localScopeName =  localScope.getName()+localScopeCounter;
        localScope.setName(localScopeName);
        localScopeCounter++;
        currentScope = localScope;

        visitChildren(ctx);


        currentScope = currentScope.getEnclosingScope();
        return null;

    }
    @Override
    public LLVMValueRef visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        visitChildren(ctx);
        SysYParser.FuncDefContext funcDefContext=(SysYParser.FuncDefContext)ctx.parent;
        String funName=funcDefContext.IDENT().getText();
        LLVMValueRef function=currentScope.resolve(funName); //correct currentScope is FunctionScope

        for(int i=0;i<ctx.funcFParam().size();i++){
           LLVMValueRef param= LLVMGetParam(function,i);
            LLVMValueRef pointer=valueProperty.get(ctx.funcFParam(i));
        //    将数值存入该内存
             LLVMBuildStore(builder, param, pointer);
        }

        return null;
    }
  //  funcFParam : bType IDENT (L_BRACKT  R_BRACKT (L_BRACKT  exp R_BRACKT )* )? ;
    @Override public LLVMValueRef visitFuncFParam(SysYParser.FuncFParamContext ctx) {
        visitChildren(ctx);
        String varName=ctx.IDENT().getText();
        if(ctx.L_BRACKT().size()==0){

            //申请一块能存放int型的内存
            LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, /*pointerName:String*/varName);

            valueProperty.put(ctx,pointer);
            currentScope.define(varName,pointer);
        }
        return null;
    }
    //varDef  : IDENT ( L_BRACKT constExp R_BRACKT ) *
    //        | IDENT ( L_BRACKT  constExp R_BRACKT  )* ASSIGN initVal
    //        ;
    @Override
    public LLVMValueRef visitVarDef(SysYParser.VarDefContext ctx) {
        visitChildren(ctx);
        String varName=ctx.IDENT().getText();
        if(ctx.L_BRACKT().size()==0){
            LLVMValueRef initval=ctx.initVal()==null?zero: valueProperty.get(ctx.initVal());
            //申请一块能存放int型的内存
            LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, /*pointerName:String*/varName);

            //将数值存入该内存
            LLVMBuildStore(builder, initval, pointer);
            currentScope.define(varName,pointer);
        }
        else{
            List<SysYParser.InitValContext > initValContexts=ctx.initVal().initVal();

            // one dimension
            int capacity= (int) LLVMConstIntGetSExtValue(valueProperty.get(ctx.constExp(0)));
            LLVMTypeRef vectorType = LLVMVectorType(i32Type, capacity);

            //申请一个可存放该vector类型的内存
            LLVMValueRef vectorPointer = LLVMBuildAlloca(builder, vectorType, varName);
            currentScope.define(varName,vectorPointer);
            for(int i=0;i<capacity;i++){

                LLVMValueRef initval;
                if(i<initValContexts.size()){
                    SysYParser.InitValContext initValContext=initValContexts.get(i);
                    initval=valueProperty.get(initValContext);
                }

                else initval=zero;
                PointerPointer valuePointer= new PointerPointer<>(zero,LLVMConstInt(i32Type,i,0));

                LLVMValueRef res=LLVMBuildGEP(builder,vectorPointer,valuePointer,2,"pointer");
                LLVMBuildStore(builder, initval, res);
            }

        }
        //1245
//1472 1479 1486 1493



        return  null;
    }
//initVal : exp | L_BRACE ( initVal ( COMMA initVal ) * )?  R_BRACE ;
    @Override
    public LLVMValueRef visitInitVal(SysYParser.InitValContext ctx) {


        visitChildren(ctx);

        if(ctx.exp()!=null){
            valueProperty.put(ctx,valueProperty.get(ctx.exp()));
        }
        else if(ctx.initVal()!=null){



        }
        return null;
    }
    @Override public LLVMValueRef visitConstDef(SysYParser.ConstDefContext ctx) {
        visitChildren(ctx);
        String varName=ctx.IDENT().getText();
        if(ctx.L_BRACKT().size()==0){
            LLVMValueRef initval= ctx.constInitVal()==null?zero:valueProperty.get(ctx.constInitVal());
            //申请一块能存放int型的内存
            LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, /*pointerName:String*/varName);

            //将数值存入该内存
            LLVMBuildStore(builder, initval, pointer);
            currentScope.define(varName,pointer);
        }
        else {
            List<SysYParser.ConstInitValContext> initValContexts = ctx.constInitVal().constInitVal();

            // one dimension
            int capacity = (int) LLVMConstIntGetSExtValue(valueProperty.get(ctx.constExp(0)));
            LLVMTypeRef vectorType = LLVMVectorType(i32Type, capacity);

            //申请一个可存放该vector类型的内存
            LLVMValueRef vectorPointer = LLVMBuildAlloca(builder, vectorType, varName);
            currentScope.define(varName, vectorPointer);
            for (int i = 0; i < capacity; i++) {

                LLVMValueRef initval;
                if (i < initValContexts.size()) {
                    SysYParser.ConstInitValContext initValContext = initValContexts.get(i);
                    initval = valueProperty.get(initValContext);
                } else initval = zero;
                PointerPointer valuePointer = new PointerPointer<>(zero, LLVMConstInt(i32Type, i, 0));

                LLVMValueRef res = LLVMBuildGEP(builder, vectorPointer, valuePointer, 2, "pointer");
                LLVMBuildStore(builder, initval, res);
            }
        }

            return null;
    }
    @Override public LLVMValueRef visitConstInitVal(SysYParser.ConstInitValContext ctx) {

        visitChildren(ctx);

        if(ctx.constExp()!=null){
            valueProperty.put(ctx,valueProperty.get(ctx.constExp()));
        }
        else if(ctx.constInitVal()!=null){



        }
        return null;
    }
        @Override public LLVMValueRef visitConstExp(SysYParser.ConstExpContext ctx) {
        visitChildren(ctx);
        valueProperty.put(ctx,valueProperty.get(ctx.exp()));
        return  null;
    }
    @Override public LLVMValueRef visitAssignStmt(SysYParser.AssignStmtContext ctx) {
        isLoadedValue.put(ctx,true);
    //    isLoadedValue.put(ctx.lhs,false);

        visitChildren(ctx);
        LLVMValueRef lhs_pointer=valueProperty.get(ctx.lhs);
        LLVMValueRef rhs_value=valueProperty.get(ctx.rhs);
        LLVMBuildStore(builder,rhs_value , lhs_pointer);
         return null;
    }
    @Override
    public LLVMValueRef visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        //函数返回指令
        isLoadedValue.put(ctx,true);
         //TODO
        visitChildren(ctx);



        LLVMValueRef result = valueProperty.get(ctx.exp());
        LLVMBuildRet(builder, /*result:LLVMValueRef*/result);
        return null;
    }
    @Override public LLVMValueRef visitExpStmt(SysYParser.ExpStmtContext ctx) {
        isLoadedValue.put(ctx,true);
        visitChildren(ctx);

        return null;
    }
    //lVal
    //   : IDENT (L_BRACKT exp R_BRACKT)*
    //   ;
    public LLVMValueRef getPointer(SysYParser.LValContext ctx){
        String id=ctx.IDENT().getText();
        LLVMValueRef pointer=currentScope.resolve(id);
        LLVMValueRef result;
        if(ctx.L_BRACKT().size()==0){
           result=pointer;

        }
        else{
            int index=(int) LLVMConstIntGetSExtValue(valueProperty.get(ctx.exp(0)));
            PointerPointer valuePointer= new PointerPointer<>(zero,LLVMConstInt(i32Type,index,0));

            result=LLVMBuildGEP(builder,pointer,valuePointer,2,"pointer"); //2 for dereference

            //  System.out.println(  (int) LLVMConstIntGetSExtValue(result));
            //  LLVMValueRef cmp= LLVMBuildICmp(builder, LLVMIntNE, LLVMConstInt(i32Type, 0, 2), result, "tmp_");
            //  System.out.println(  (int) LLVMConstIntGetSExtValue(cmp));
        }
        return result;
    }
    @Override public LLVMValueRef visitLVal(SysYParser.LValContext ctx) {

        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));
        visitChildren(ctx);
        String id=ctx.IDENT().getText();

        LLVMValueRef pointer=getPointer(ctx);
        //
        if(isLoadedValue.get(ctx)==null|| ctx.parent instanceof SysYParser.AssignStmtContext){

            valueProperty.put(ctx,pointer);


        }
        //loaded situation
        else {
            LLVMValueRef result;

            result = LLVMBuildLoad(builder, pointer, /*varName:String*/id);

            valueProperty.put(ctx,result);
        }

        return null;
    }
    public LLVMValueRef visitExpLVal(SysYParser.ExpLValContext ctx) {
        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));
        visitChildren(ctx);
        valueProperty.put(ctx,valueProperty.get(ctx.lVal()));
        return null;
    }
    @Override public LLVMValueRef visitCall(SysYParser.CallContext ctx) {
        isLoadedValue.put(ctx,true);
        visitChildren(ctx);
        String funName=ctx.IDENT().getText();
        LLVMValueRef fun=currentScope.resolve(funName);
        int paramsSize=ctx.funcRParams()==null? 0:ctx.funcRParams().param().size();
        PointerPointer params=new PointerPointer(paramsSize);
        if(paramsSize!=0){
            List<SysYParser.ParamContext> paramContexts=ctx.funcRParams().param();
            for(int i=0;i<paramsSize;i++){
                LLVMValueRef value=valueProperty.get(paramContexts.get(i));
                params.put(i,value);
            }
        }
       LLVMValueRef callVal=LLVMBuildCall(builder,fun,params,paramsSize,funName);
        valueProperty.put(ctx,callVal);



        return null;
    }
    @Override public LLVMValueRef visitFuncRParams(SysYParser.FuncRParamsContext ctx) {
        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));
        visitChildren(ctx);
        return null;
    }
    @Override public LLVMValueRef visitParam(SysYParser.ParamContext ctx) {
        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));
        visitChildren(ctx);
        valueProperty.put(ctx,valueProperty.get(ctx.exp()));
        return  null;
    }
    @Override
    public LLVMValueRef visitPlusMinus(SysYParser.PlusMinusContext ctx) {
        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));
        visitChildren(ctx);
        LLVMValueRef lhs=valueProperty.get(ctx.lhs);
        LLVMValueRef rhs=valueProperty.get(ctx.rhs);

        LLVMValueRef result;
        if(ctx.PLUS()!=null){

            result=LLVMBuildAdd(builder,lhs,rhs,"result");

        }
        else {

            result=LLVMBuildSub(builder,lhs,rhs,"result");
        }
        valueProperty.put(ctx,result);
        return null;

    }
    @Override
    public LLVMValueRef visitMulDivMod(SysYParser.MulDivModContext ctx) {
        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));
        visitChildren(ctx);
        LLVMValueRef lhs=valueProperty.get(ctx.lhs);
        LLVMValueRef rhs=valueProperty.get(ctx.rhs);


        LLVMValueRef result;
        if(ctx.MUL()!=null){

            result=LLVMBuildMul(builder,lhs,rhs,"result");

        }
        else if(ctx.DIV()!=null){

            result=LLVMBuildSDiv(builder,lhs,rhs,"result");
        }
        else result=LLVMBuildSRem(builder,lhs,rhs,"result");
        valueProperty.put(ctx,result);
        return null;
    }
    @Override
    public LLVMValueRef visitNumber(SysYParser.NumberContext ctx) {
        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));
        visitChildren(ctx);
        int num=toDEC(ctx.INTEGR_CONST().getText());
        valueProperty.put(ctx,LLVMConstInt(i32Type, num, /* signExtend */ 0));
        return null;
    }
    @Override
    public LLVMValueRef visitExpNumber(SysYParser.ExpNumberContext ctx) {
        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));
        visitChildren(ctx);
        valueProperty.put(ctx,valueProperty.get(ctx.number()));
        return null;
    }
    @Override
    public LLVMValueRef visitParens(SysYParser.ParensContext ctx) {
        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));
        visitChildren(ctx);
        valueProperty.put(ctx,valueProperty.get(ctx.exp()));
        return null;
    }
    @Override
    public LLVMValueRef visitUnary(SysYParser.UnaryContext ctx) {
        isLoadedValue.put(ctx,isLoadedValue.get(ctx.parent));


        visitChildren(ctx);
        LLVMValueRef tmp_=valueProperty.get(ctx.exp());
        // 生成icmp


        if(ctx.unaryOp().NOT()!=null) {
            tmp_ = LLVMBuildICmp(builder, LLVMIntNE, LLVMConstInt(i32Type, 0, 0), tmp_, "tmp_");
// 生成xor
            tmp_ = LLVMBuildXor(builder, tmp_, LLVMConstInt(LLVMInt1Type(), 1, 0), "tmp_");
// 生成zext
            tmp_ = LLVMBuildZExt(builder, tmp_, i32Type, "tmp_");
        }
        else if(ctx.unaryOp().PLUS()!=null){

        }
        else if(ctx.unaryOp().MINUS()!=null){
            tmp_=LLVMBuildSub(builder,zero,tmp_,"tmp_");
        }

       valueProperty.put(ctx,tmp_);
        return null;
    }


    @Override public LLVMValueRef visitUnaryOp(SysYParser.UnaryOpContext ctx) {

//        if(ctx.NOT()!=null){
//            type=NOT;
//        }
//        else if(ctx.PLUS()!=null){
//            type=U_PLUS;
//        }else if(ctx.MINUS()!=null){
//            type=U_MINUS;
//        }


        return visitChildren(ctx);

    }
    public void OutPutConsole(){
        LLVMDumpModule(module);

    }

    public void OutputFile(String dest){
        final BytePointer error = new BytePointer();
        if (LLVMPrintModuleToFile(module, dest, error) != 0) {    // moudle是你自定义的LLVMModuleRef对象
            LLVMDisposeMessage(error);
        }
    }
}