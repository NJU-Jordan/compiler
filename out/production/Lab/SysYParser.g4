parser grammar SysYParser;

options {
    tokenVocab = SysYLexer;
}

program
   : compUnit
   ;
compUnit
   : (funcDef | decl)+ EOF
   ;

decl : constDecl | varDecl ;


constDecl : CONST bType constDef (COMMA constDef ) * SEMICOLON ;

bType : INT ;

constDef : id ( L_BRACKT constExp R_BRACKT )* ASSIGN constInitVal ;

constInitVal : constExp
             | L_BRACE ( constInitVal (COMMA constInitVal )* )? R_BRACE
             ;

varDecl : bType varDef ( COMMA varDef ) * SEMICOLON
        ;

varDef  : id ( L_BRACKT constExp R_BRACKT ) *
        | id ( L_BRACKT  constExp R_BRACKT  )* ASSIGN initVal
        ;


initVal : exp | L_BRACE ( initVal ( COMMA initVal ) * )?  R_BRACE ;

funcDef : funcType id L_PAREN (funcFParams)? R_PAREN block ;

funcType :  VOID  | INT;

funcFParams : funcFParam (COMMA funcFParam )* ;

funcFParam : bType id (L_BRACKT  R_BRACKT (L_BRACKT  exp R_BRACKT )* )? ;


block : L_BRACE ( blockItem )*  R_BRACE ;

blockItem : decl | stmt ;

stmt
    : lhs=lVal ASSIGN rhs=exp SEMICOLON  # AssignStmt
    | (exp)? SEMICOLON      # ExpStmt
    | block                 # BlockStmt
    | IF L_PAREN cond R_PAREN stmt ( ELSE stmt )?  #IfStmt
    | WHILE L_PAREN cond R_PAREN stmt    # WhileStmt
    | BREAK SEMICOLON      # BreakStmt
    | CONTINUE SEMICOLON   # ContinueStmt
    | RETURN (exp)? SEMICOLON   #ReturnStmt
    ;

exp
   : L_PAREN exp R_PAREN     #Parens
   | lVal                    #ExpLVal
   | number                  #ExpNumber
   | IDENT L_PAREN funcRParams? R_PAREN   # Call
   | unaryOp exp                  #Unary
   | lhs=exp (MUL | DIV | MOD) rhs=exp    # MulDivMod
   | lhs=exp (PLUS | MINUS) rhs=exp       # PlusMinus
   ;

cond
   : exp                        # CondExp
   | lhs=cond (LT | GT | LE | GE) rhs=cond  # LG
   | lhs=cond (EQ | NEQ) rhs=cond    # ENQ
   | lhs=cond AND rhs=cond           # And
   | lhs=cond OR rhs=cond            # Or
   ;

lVal
   : id (L_BRACKT exp R_BRACKT)*
   ;

id:
    IDENT
    ;

number
   : INTEGR_CONST
   ;

unaryOp
   : PLUS
   | MINUS
   | NOT
   ;

funcRParams
   : param (COMMA param)*
   ;

param
   : exp
   ;

constExp
   : exp
   ;