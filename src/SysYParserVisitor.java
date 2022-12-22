// Generated from ./src/SysYParser.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SysYParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SysYParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SysYParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(SysYParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#compUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompUnit(SysYParser.CompUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(SysYParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#constDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDecl(SysYParser.ConstDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#bType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBType(SysYParser.BTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#constDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDef(SysYParser.ConstDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#constInitVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstInitVal(SysYParser.ConstInitValContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(SysYParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#varDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDef(SysYParser.VarDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#initVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitVal(SysYParser.InitValContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#funcDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDef(SysYParser.FuncDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#funcType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncType(SysYParser.FuncTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#funcFParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncFParams(SysYParser.FuncFParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#funcFParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncFParam(SysYParser.FuncFParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(SysYParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#blockItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockItem(SysYParser.BlockItemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStmt(SysYParser.AssignStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpStmt(SysYParser.ExpStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmt(SysYParser.BlockStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(SysYParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(SysYParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BreakStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(SysYParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ContinueStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(SysYParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReturnStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(SysYParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpNumber}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpNumber(SysYParser.ExpNumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlusMinus}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusMinus(SysYParser.PlusMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Call}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall(SysYParser.CallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDivMod}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivMod(SysYParser.MulDivModContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpLVal}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpLVal(SysYParser.ExpLValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(SysYParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unary}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary(SysYParser.UnaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(SysYParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#lVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLVal(SysYParser.LValContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(SysYParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#unaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(SysYParser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#funcRParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncRParams(SysYParser.FuncRParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(SysYParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#constExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstExp(SysYParser.ConstExpContext ctx);
}