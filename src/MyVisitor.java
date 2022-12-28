import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;

import static org.bytedeco.llvm.global.LLVM.*;

public class MyVisitor extends SysYParserBaseVisitor<LLVMValueRef> {

    LLVMModuleRef module;
    LLVMBuilderRef builder;

    LLVMTypeRef i32Type;


    LLVMValueRef zero ;
    final int NOT=0;
    final int U_PLUS=1;
    final int U_MINUS=2;
    int type=-1;
    final int MUL =  3;
    final int DIV = 4;

    final int MOD = 5;
    final int PLUS= 6;
    final int MINUS=7;
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
        return super.visitProgram(ctx);
    }

    @Override
    public LLVMValueRef visitCompUnit(SysYParser.CompUnitContext ctx) {
        return super.visitCompUnit(ctx);
    }


    @Override
    public LLVMValueRef visitFuncDef(SysYParser.FuncDefContext ctx) {
        //生成返回值类型
        LLVMTypeRef returnType = i32Type;

        //生成函数参数类型
      // PointerPointer<Pointer> argumentTypes = new PointerPointer<>(2)
       //         .put(0, i32Type)
        //        .put(1, i32Type);

        //生成函数类型
      //  LLVMTypeRef ft = LLVMFunctionType(returnType, argumentTypes, /* argumentCount */ 2, /* isVariadic */ 0);
        //若仅需一个参数也可以使用如下方式直接生成函数类型
        LLVMTypeRef ft = LLVMFunctionType(returnType, i32Type, /* argumentCount */ 0, /* isVariadic */ 0);

        //生成函数，即向之前创建的module中添加函数
        LLVMValueRef function = LLVMAddFunction(module, /*functionName:String*/"main", ft);

        //lab4  we have only one block
        //通过如下语句在函数中加入基本块，一个函数可以加入多个基本块
        LLVMBasicBlockRef block1 = LLVMAppendBasicBlock(function, /*blockName:String*/"mainEntry");

        LLVMPositionBuilderAtEnd(builder, block1);//后续生成的指令将追加在block1的后面

        return visitChildren(ctx);


    }
    @Override
    public LLVMValueRef visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        //函数返回指令

         //TODO

         LLVMValueRef result=visit(ctx.exp());
        LLVMBuildRet(builder, /*result:LLVMValueRef*/result);
        return super.visitReturnStmt(ctx);
    }

    @Override
    public LLVMValueRef visitPlusMinus(SysYParser.PlusMinusContext ctx) {
        visitChildren(ctx);
        LLVMValueRef lhs=visit(ctx.lhs);
        LLVMValueRef rhs=visit(ctx.rhs);

        LLVMValueRef result;
        if(ctx.PLUS()!=null){

            result=LLVMBuildAdd(builder,lhs,rhs,"result");

        }
        else {

            result=LLVMBuildSub(builder,lhs,rhs,"result");
        }
        return result;

    }
    @Override
    public LLVMValueRef visitMulDivMod(SysYParser.MulDivModContext ctx) {
        visitChildren(ctx);
        LLVMValueRef lhs=visit(ctx.lhs);
        LLVMValueRef rhs=visit(ctx.rhs);

        LLVMValueRef result;
        if(ctx.MUL()!=null){

            result=LLVMBuildMul(builder,lhs,rhs,"result");

        }
        else if(ctx.DIV()!=null){

            result=LLVMBuildSDiv(builder,lhs,rhs,"result");
        }
        else result=LLVMBuildSRem(builder,lhs,rhs,"result");
        return result;
    }
    @Override
    public LLVMValueRef visitNumber(SysYParser.NumberContext ctx) {

        visitChildren(ctx);
        int num=toDEC(ctx.INTEGR_CONST().getText());
        return LLVMConstInt(i32Type, num, /* signExtend */ 0);
    }
    @Override
    public LLVMValueRef visitExpNumber(SysYParser.ExpNumberContext ctx) {

        visitChildren(ctx);
        LLVMValueRef result= visit(ctx.number());
        return result;
    }
    @Override
    public LLVMValueRef visitParens(SysYParser.ParensContext ctx) {
        visitChildren(ctx);
        return visit(ctx.exp());
    }
    @Override
    public LLVMValueRef visitUnary(SysYParser.UnaryContext ctx) {
        //创建一个常量,这里是常数0
       // LLVMValueRef zero = LLVMConstInt(i32Type, 0, /* signExtend */ 0);
        //int型变量
        //申请一块能存放int型的内存
      //  LLVMValueRef pointer = LLVMBuildAlloca(builder, i32Type, /*pointerName:String*/"pointer");

        //将数值存入该内存
      //  LLVMBuildStore(builder, zero, pointer);

        //从内存中将值取出
       //LLVMValueRef value = LLVMBuildLoad(builder, pointer, /*varName:String*/"value");


        LLVMValueRef tmp_=visit(ctx.exp());
        // 生成icmp

        LLVMValueRef unary_op=visit(ctx.unaryOp());
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

        visitChildren(ctx);
        return tmp_;
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
        LLVMPrintModuleToFile(module,dest,error);

    }
}