lexer grammar SysYLexer;



IDENT : (LETTER | '_')(LETTER |DIGIT | '_')*;

INT : '0' | ([1-9] [0-9]*) ;

ASSIGN : '=' ;

L_PAREN : '(' ;

R_PAREN : ')' ;

L_BRACE : '{' ;

R_BRACE : '}' ;

SEMICOLON : ';' ;

WS : [ \t\r\n]+ -> skip ;



fragment LETTER: [a-zA-Z] ;
fragment DIGIT: [0-9] ;

