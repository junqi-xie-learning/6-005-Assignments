/*
 * Compile all your grammars using
 *       java -jar ../../../lib/antlr.jar *.g4
 * then Refresh in Eclipse.
 */
grammar Abc;
import Configuration;

root : (music | repeat | varied)+;
music : section BARLINE;
repeat : section ':' BARLINE;
varied : section '|' '[1' section ':' '|' '[2' section BARLINE;
section : bar ('|' bar?)*;

bar : (note | rest | chord | tuplet)+;
note : pitch duration;
rest : 'z' duration;
chord : '[' note+ ']';
tuplet : '(' NUMBER bar;

pitch : ACCIDENTAL* LETTER OCTAVE*;
duration : NUMBER? '/'? NUMBER?;

LETTER : [A-Ga-g];
NUMBER : [0-9]+;

ACCIDENTAL : '_' | '^' | '=';
OCTAVE : '\'' | ',';
BARLINE : '|:' | '[|' | '||' | '|]';

/* Tell Antlr to ignore spaces around tokens. */
SPACES : [ ]+ -> skip;
