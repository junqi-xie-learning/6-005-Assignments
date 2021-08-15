// Generated from Abc.g4 by ANTLR 4.5.1

package abc.parser;
// Do not edit this .java file! Edit the .g4 file and re-run Antlr.

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AbcParser extends Parser {
  static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache =
    new PredictionContextCache();
  public static final int
    T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
    LETTER=10, NUMBER=11, ACCIDENTAL=12, OCTAVE=13, BARLINE=14, SPACES=15;
  public static final int
    RULE_root = 0, RULE_music = 1, RULE_repeat = 2, RULE_varied = 3, RULE_section = 4, 
    RULE_bar = 5, RULE_note = 6, RULE_rest = 7, RULE_chord = 8, RULE_tuplet = 9, 
    RULE_pitch = 10, RULE_duration = 11;
  public static final String[] ruleNames = {
    "root", "music", "repeat", "varied", "section", "bar", "note", "rest", 
    "chord", "tuplet", "pitch", "duration"
  };

  private static final String[] _LITERAL_NAMES = {
    null, "':'", "'|'", "'[1'", "'[2'", "'z'", "'['", "']'", "'('", "'/'"
  };
  private static final String[] _SYMBOLIC_NAMES = {
    null, null, null, null, null, null, null, null, null, null, "LETTER", 
    "NUMBER", "ACCIDENTAL", "OCTAVE", "BARLINE", "SPACES"
  };
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated
  public static final String[] tokenNames;
  static {
    tokenNames = new String[_SYMBOLIC_NAMES.length];
    for (int i = 0; i < tokenNames.length; i++) {
      tokenNames[i] = VOCABULARY.getLiteralName(i);
      if (tokenNames[i] == null) {
        tokenNames[i] = VOCABULARY.getSymbolicName(i);
      }

      if (tokenNames[i] == null) {
        tokenNames[i] = "<INVALID>";
      }
    }
  }

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override

  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }

  @Override
  public String getGrammarFileName() { return "Abc.g4"; }

  @Override
  public String[] getRuleNames() { return ruleNames; }

  @Override
  public String getSerializedATN() { return _serializedATN; }

  @Override
  public ATN getATN() { return _ATN; }


      // This method makes the parser stop running if it encounters
      // invalid input and throw a RuntimeException.
      public void reportErrorsAsExceptions() {
          // To prevent any reports to standard error, add this line:
          //removeErrorListeners();
          
          addErrorListener(new BaseErrorListener() {
              public void syntaxError(Recognizer<?, ?> recognizer,
                                      Object offendingSymbol, 
                                      int line, int charPositionInLine,
                                      String msg, RecognitionException e) {
                  throw new ParseCancellationException(msg, e);
              }
          });
      }

  public AbcParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
  }
  public static class RootContext extends ParserRuleContext {
    public List<MusicContext> music() {
      return getRuleContexts(MusicContext.class);
    }
    public MusicContext music(int i) {
      return getRuleContext(MusicContext.class,i);
    }
    public List<RepeatContext> repeat() {
      return getRuleContexts(RepeatContext.class);
    }
    public RepeatContext repeat(int i) {
      return getRuleContext(RepeatContext.class,i);
    }
    public List<VariedContext> varied() {
      return getRuleContexts(VariedContext.class);
    }
    public VariedContext varied(int i) {
      return getRuleContext(VariedContext.class,i);
    }
    public RootContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_root; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterRoot(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitRoot(this);
    }
  }

  public final RootContext root() throws RecognitionException {
    RootContext _localctx = new RootContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_root);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(27); 
      _errHandler.sync(this);
      _la = _input.LA(1);
      do {
        {
        setState(27);
        switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
        case 1:
          {
          setState(24);
          music();
          }
          break;
        case 2:
          {
          setState(25);
          repeat();
          }
          break;
        case 3:
          {
          setState(26);
          varied();
          }
          break;
        }
        }
        setState(29); 
        _errHandler.sync(this);
        _la = _input.LA(1);
      } while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__7) | (1L << LETTER) | (1L << ACCIDENTAL))) != 0) );
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class MusicContext extends ParserRuleContext {
    public SectionContext section() {
      return getRuleContext(SectionContext.class,0);
    }
    public TerminalNode BARLINE() { return getToken(AbcParser.BARLINE, 0); }
    public MusicContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_music; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterMusic(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitMusic(this);
    }
  }

  public final MusicContext music() throws RecognitionException {
    MusicContext _localctx = new MusicContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_music);
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(31);
      section();
      setState(32);
      match(BARLINE);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RepeatContext extends ParserRuleContext {
    public SectionContext section() {
      return getRuleContext(SectionContext.class,0);
    }
    public TerminalNode BARLINE() { return getToken(AbcParser.BARLINE, 0); }
    public RepeatContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_repeat; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterRepeat(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitRepeat(this);
    }
  }

  public final RepeatContext repeat() throws RecognitionException {
    RepeatContext _localctx = new RepeatContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_repeat);
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(34);
      section();
      setState(35);
      match(T__0);
      setState(36);
      match(BARLINE);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class VariedContext extends ParserRuleContext {
    public List<SectionContext> section() {
      return getRuleContexts(SectionContext.class);
    }
    public SectionContext section(int i) {
      return getRuleContext(SectionContext.class,i);
    }
    public TerminalNode BARLINE() { return getToken(AbcParser.BARLINE, 0); }
    public VariedContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_varied; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterVaried(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitVaried(this);
    }
  }

  public final VariedContext varied() throws RecognitionException {
    VariedContext _localctx = new VariedContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_varied);
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(38);
      section();
      setState(39);
      match(T__1);
      setState(40);
      match(T__2);
      setState(41);
      section();
      setState(42);
      match(T__0);
      setState(43);
      match(T__1);
      setState(44);
      match(T__3);
      setState(45);
      section();
      setState(46);
      match(BARLINE);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SectionContext extends ParserRuleContext {
    public List<BarContext> bar() {
      return getRuleContexts(BarContext.class);
    }
    public BarContext bar(int i) {
      return getRuleContext(BarContext.class,i);
    }
    public SectionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_section; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterSection(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitSection(this);
    }
  }

  public final SectionContext section() throws RecognitionException {
    SectionContext _localctx = new SectionContext(_ctx, getState());
    enterRule(_localctx, 8, RULE_section);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
      setState(48);
      bar();
      setState(55);
      _errHandler.sync(this);
      _alt = getInterpreter().adaptivePredict(_input,3,_ctx);
      while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
        if ( _alt==1 ) {
          {
          {
          setState(49);
          match(T__1);
          setState(51);
          _la = _input.LA(1);
          if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__7) | (1L << LETTER) | (1L << ACCIDENTAL))) != 0)) {
            {
            setState(50);
            bar();
            }
          }

          }
          } 
        }
        setState(57);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input,3,_ctx);
      }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class BarContext extends ParserRuleContext {
    public List<NoteContext> note() {
      return getRuleContexts(NoteContext.class);
    }
    public NoteContext note(int i) {
      return getRuleContext(NoteContext.class,i);
    }
    public List<RestContext> rest() {
      return getRuleContexts(RestContext.class);
    }
    public RestContext rest(int i) {
      return getRuleContext(RestContext.class,i);
    }
    public List<ChordContext> chord() {
      return getRuleContexts(ChordContext.class);
    }
    public ChordContext chord(int i) {
      return getRuleContext(ChordContext.class,i);
    }
    public List<TupletContext> tuplet() {
      return getRuleContexts(TupletContext.class);
    }
    public TupletContext tuplet(int i) {
      return getRuleContext(TupletContext.class,i);
    }
    public BarContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_bar; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterBar(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitBar(this);
    }
  }

  public final BarContext bar() throws RecognitionException {
    BarContext _localctx = new BarContext(_ctx, getState());
    enterRule(_localctx, 10, RULE_bar);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
      setState(62); 
      _errHandler.sync(this);
      _alt = 1;
      do {
        switch (_alt) {
        case 1:
          {
          setState(62);
          switch (_input.LA(1)) {
          case LETTER:
          case ACCIDENTAL:
            {
            setState(58);
            note();
            }
            break;
          case T__4:
            {
            setState(59);
            rest();
            }
            break;
          case T__5:
            {
            setState(60);
            chord();
            }
            break;
          case T__7:
            {
            setState(61);
            tuplet();
            }
            break;
          default:
            throw new NoViableAltException(this);
          }
          }
          break;
        default:
          throw new NoViableAltException(this);
        }
        setState(64); 
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input,5,_ctx);
      } while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class NoteContext extends ParserRuleContext {
    public PitchContext pitch() {
      return getRuleContext(PitchContext.class,0);
    }
    public DurationContext duration() {
      return getRuleContext(DurationContext.class,0);
    }
    public NoteContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_note; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterNote(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitNote(this);
    }
  }

  public final NoteContext note() throws RecognitionException {
    NoteContext _localctx = new NoteContext(_ctx, getState());
    enterRule(_localctx, 12, RULE_note);
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(66);
      pitch();
      setState(67);
      duration();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RestContext extends ParserRuleContext {
    public DurationContext duration() {
      return getRuleContext(DurationContext.class,0);
    }
    public RestContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_rest; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterRest(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitRest(this);
    }
  }

  public final RestContext rest() throws RecognitionException {
    RestContext _localctx = new RestContext(_ctx, getState());
    enterRule(_localctx, 14, RULE_rest);
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(69);
      match(T__4);
      setState(70);
      duration();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ChordContext extends ParserRuleContext {
    public List<NoteContext> note() {
      return getRuleContexts(NoteContext.class);
    }
    public NoteContext note(int i) {
      return getRuleContext(NoteContext.class,i);
    }
    public ChordContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_chord; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterChord(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitChord(this);
    }
  }

  public final ChordContext chord() throws RecognitionException {
    ChordContext _localctx = new ChordContext(_ctx, getState());
    enterRule(_localctx, 16, RULE_chord);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(72);
      match(T__5);
      setState(74); 
      _errHandler.sync(this);
      _la = _input.LA(1);
      do {
        {
        {
        setState(73);
        note();
        }
        }
        setState(76); 
        _errHandler.sync(this);
        _la = _input.LA(1);
      } while ( _la==LETTER || _la==ACCIDENTAL );
      setState(78);
      match(T__6);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TupletContext extends ParserRuleContext {
    public TerminalNode NUMBER() { return getToken(AbcParser.NUMBER, 0); }
    public BarContext bar() {
      return getRuleContext(BarContext.class,0);
    }
    public TupletContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_tuplet; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterTuplet(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitTuplet(this);
    }
  }

  public final TupletContext tuplet() throws RecognitionException {
    TupletContext _localctx = new TupletContext(_ctx, getState());
    enterRule(_localctx, 18, RULE_tuplet);
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(80);
      match(T__7);
      setState(81);
      match(NUMBER);
      setState(82);
      bar();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class PitchContext extends ParserRuleContext {
    public TerminalNode LETTER() { return getToken(AbcParser.LETTER, 0); }
    public List<TerminalNode> ACCIDENTAL() { return getTokens(AbcParser.ACCIDENTAL); }
    public TerminalNode ACCIDENTAL(int i) {
      return getToken(AbcParser.ACCIDENTAL, i);
    }
    public List<TerminalNode> OCTAVE() { return getTokens(AbcParser.OCTAVE); }
    public TerminalNode OCTAVE(int i) {
      return getToken(AbcParser.OCTAVE, i);
    }
    public PitchContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_pitch; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterPitch(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitPitch(this);
    }
  }

  public final PitchContext pitch() throws RecognitionException {
    PitchContext _localctx = new PitchContext(_ctx, getState());
    enterRule(_localctx, 20, RULE_pitch);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(87);
      _errHandler.sync(this);
      _la = _input.LA(1);
      while (_la==ACCIDENTAL) {
        {
        {
        setState(84);
        match(ACCIDENTAL);
        }
        }
        setState(89);
        _errHandler.sync(this);
        _la = _input.LA(1);
      }
      setState(90);
      match(LETTER);
      setState(94);
      _errHandler.sync(this);
      _la = _input.LA(1);
      while (_la==OCTAVE) {
        {
        {
        setState(91);
        match(OCTAVE);
        }
        }
        setState(96);
        _errHandler.sync(this);
        _la = _input.LA(1);
      }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class DurationContext extends ParserRuleContext {
    public List<TerminalNode> NUMBER() { return getTokens(AbcParser.NUMBER); }
    public TerminalNode NUMBER(int i) {
      return getToken(AbcParser.NUMBER, i);
    }
    public DurationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_duration; }
    @Override
    public void enterRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).enterDuration(this);
    }
    @Override
    public void exitRule(ParseTreeListener listener) {
      if ( listener instanceof AbcListener ) ((AbcListener)listener).exitDuration(this);
    }
  }

  public final DurationContext duration() throws RecognitionException {
    DurationContext _localctx = new DurationContext(_ctx, getState());
    enterRule(_localctx, 22, RULE_duration);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
      setState(98);
      switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
      case 1:
        {
        setState(97);
        match(NUMBER);
        }
        break;
      }
      setState(101);
      _la = _input.LA(1);
      if (_la==T__8) {
        {
        setState(100);
        match(T__8);
        }
      }

      setState(104);
      _la = _input.LA(1);
      if (_la==NUMBER) {
        {
        setState(103);
        match(NUMBER);
        }
      }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static final String _serializedATN =
    "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\21m\4\2\t\2\4\3"+
      "\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
      "\t\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\6\2\36\n\2\r\2\16\2\37\3\3\3\3\3"+
      "\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6"+
      "\3\6\5\6\66\n\6\7\68\n\6\f\6\16\6;\13\6\3\7\3\7\3\7\3\7\6\7A\n\7\r"+
      "\7\16\7B\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\6\nM\n\n\r\n\16\nN\3\n\3"+
      "\n\3\13\3\13\3\13\3\13\3\f\7\fX\n\f\f\f\16\f[\13\f\3\f\3\f\7\f_\n"+
      "\f\f\f\16\fb\13\f\3\r\5\re\n\r\3\r\5\rh\n\r\3\r\5\rk\n\r\3\r\2\2\16"+
      "\2\4\6\b\n\f\16\20\22\24\26\30\2\2o\2\35\3\2\2\2\4!\3\2\2\2\6$\3\2"+
      "\2\2\b(\3\2\2\2\n\62\3\2\2\2\f@\3\2\2\2\16D\3\2\2\2\20G\3\2\2\2\22"+
      "J\3\2\2\2\24R\3\2\2\2\26Y\3\2\2\2\30d\3\2\2\2\32\36\5\4\3\2\33\36"+
      "\5\6\4\2\34\36\5\b\5\2\35\32\3\2\2\2\35\33\3\2\2\2\35\34\3\2\2\2\36"+
      "\37\3\2\2\2\37\35\3\2\2\2\37 \3\2\2\2 \3\3\2\2\2!\"\5\n\6\2\"#\7\20"+
      "\2\2#\5\3\2\2\2$%\5\n\6\2%&\7\3\2\2&\'\7\20\2\2\'\7\3\2\2\2()\5\n"+
      "\6\2)*\7\4\2\2*+\7\5\2\2+,\5\n\6\2,-\7\3\2\2-.\7\4\2\2./\7\6\2\2/"+
      "\60\5\n\6\2\60\61\7\20\2\2\61\t\3\2\2\2\629\5\f\7\2\63\65\7\4\2\2"+
      "\64\66\5\f\7\2\65\64\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\67\63\3\2\2"+
      "\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:\13\3\2\2\2;9\3\2\2\2<A\5\16\b"+
      "\2=A\5\20\t\2>A\5\22\n\2?A\5\24\13\2@<\3\2\2\2@=\3\2\2\2@>\3\2\2\2"+
      "@?\3\2\2\2AB\3\2\2\2B@\3\2\2\2BC\3\2\2\2C\r\3\2\2\2DE\5\26\f\2EF\5"+
      "\30\r\2F\17\3\2\2\2GH\7\7\2\2HI\5\30\r\2I\21\3\2\2\2JL\7\b\2\2KM\5"+
      "\16\b\2LK\3\2\2\2MN\3\2\2\2NL\3\2\2\2NO\3\2\2\2OP\3\2\2\2PQ\7\t\2"+
      "\2Q\23\3\2\2\2RS\7\n\2\2ST\7\r\2\2TU\5\f\7\2U\25\3\2\2\2VX\7\16\2"+
      "\2WV\3\2\2\2X[\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z\\\3\2\2\2[Y\3\2\2\2\\"+
      "`\7\f\2\2]_\7\17\2\2^]\3\2\2\2_b\3\2\2\2`^\3\2\2\2`a\3\2\2\2a\27\3"+
      "\2\2\2b`\3\2\2\2ce\7\r\2\2dc\3\2\2\2de\3\2\2\2eg\3\2\2\2fh\7\13\2"+
      "\2gf\3\2\2\2gh\3\2\2\2hj\3\2\2\2ik\7\r\2\2ji\3\2\2\2jk\3\2\2\2k\31"+
      "\3\2\2\2\16\35\37\659@BNY`dgj";
  public static final ATN _ATN =
    new ATNDeserializer().deserialize(_serializedATN.toCharArray());
  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}