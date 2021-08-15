// Generated from Abc.g4 by ANTLR 4.5.1

package abc.parser;
// Do not edit this .java file! Edit the .g4 file and re-run Antlr.

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AbcParser}.
 */
public interface AbcListener extends ParseTreeListener {
  /**
   * Enter a parse tree produced by {@link AbcParser#root}.
   * @param ctx the parse tree
   */
  void enterRoot(AbcParser.RootContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#root}.
   * @param ctx the parse tree
   */
  void exitRoot(AbcParser.RootContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#music}.
   * @param ctx the parse tree
   */
  void enterMusic(AbcParser.MusicContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#music}.
   * @param ctx the parse tree
   */
  void exitMusic(AbcParser.MusicContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#repeat}.
   * @param ctx the parse tree
   */
  void enterRepeat(AbcParser.RepeatContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#repeat}.
   * @param ctx the parse tree
   */
  void exitRepeat(AbcParser.RepeatContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#varied}.
   * @param ctx the parse tree
   */
  void enterVaried(AbcParser.VariedContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#varied}.
   * @param ctx the parse tree
   */
  void exitVaried(AbcParser.VariedContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#section}.
   * @param ctx the parse tree
   */
  void enterSection(AbcParser.SectionContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#section}.
   * @param ctx the parse tree
   */
  void exitSection(AbcParser.SectionContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#bar}.
   * @param ctx the parse tree
   */
  void enterBar(AbcParser.BarContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#bar}.
   * @param ctx the parse tree
   */
  void exitBar(AbcParser.BarContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#note}.
   * @param ctx the parse tree
   */
  void enterNote(AbcParser.NoteContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#note}.
   * @param ctx the parse tree
   */
  void exitNote(AbcParser.NoteContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#rest}.
   * @param ctx the parse tree
   */
  void enterRest(AbcParser.RestContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#rest}.
   * @param ctx the parse tree
   */
  void exitRest(AbcParser.RestContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#chord}.
   * @param ctx the parse tree
   */
  void enterChord(AbcParser.ChordContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#chord}.
   * @param ctx the parse tree
   */
  void exitChord(AbcParser.ChordContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#tuplet}.
   * @param ctx the parse tree
   */
  void enterTuplet(AbcParser.TupletContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#tuplet}.
   * @param ctx the parse tree
   */
  void exitTuplet(AbcParser.TupletContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#pitch}.
   * @param ctx the parse tree
   */
  void enterPitch(AbcParser.PitchContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#pitch}.
   * @param ctx the parse tree
   */
  void exitPitch(AbcParser.PitchContext ctx);
  /**
   * Enter a parse tree produced by {@link AbcParser#duration}.
   * @param ctx the parse tree
   */
  void enterDuration(AbcParser.DurationContext ctx);
  /**
   * Exit a parse tree produced by {@link AbcParser#duration}.
   * @param ctx the parse tree
   */
  void exitDuration(AbcParser.DurationContext ctx);
}