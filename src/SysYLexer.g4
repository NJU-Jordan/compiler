lexer grammar SysYLexer;



ID : (LETTER | '_')(LETTER |DIGIT | '_')*;

INT : '0' | ([1-9] [0-9]*) ;

WS : [ \t\r\n]+ -> skip ;



fragment LETTER: [a-zA-Z] ;
fragment DIGIT: [0-9] ;

