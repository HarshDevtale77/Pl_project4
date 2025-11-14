public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
       LexerImpl mylex = new LexerImpl();

        // NUM: [0-9]*\.[0-9]+  (must have '.' and >=1 digit after it)
        Automaton a_num = new AutomatonImpl();
        // states: 0 = start, 1 = seen digit(s) before dot (loop on digits), 2 = seen dot, 3 = seen at least one digit after dot (accept, loop on digits)
        a_num.addState(0, true, false);
        a_num.addState(1, false, false);
        a_num.addState(2, false, false);
        a_num.addState(3, false, true);

        // digits from start -> state1 (to allow digits before dot)
        for (char d = '0'; d <= '9'; d++) {
            a_num.addTransition(0, d, 1);
            a_num.addTransition(1, d, 1); // stay in state1 while reading digits
            // after dot we will go to state3 on digits (below)
            a_num.addTransition(3, d, 3); // state3 loops on digits
        }
        // dot transitions: from start (to allow no digits before dot) and from state1
        a_num.addTransition(0, '.', 2);
        a_num.addTransition(1, '.', 2);
        // after dot, need at least one digit to accept: 2 --digit--> 3
        for (char d = '0'; d <= '9'; d++) {
            a_num.addTransition(2, d, 3);
        }

        // PLUS
        Automaton a_plus = new AutomatonImpl();
        a_plus.addState(0, true, false);
        a_plus.addState(1, false, true);
        a_plus.addTransition(0, '+', 1);

        // MINUS
        Automaton a_minus = new AutomatonImpl();
        a_minus.addState(0, true, false);
        a_minus.addState(1, false, true);
        a_minus.addTransition(0, '-', 1);

        // TIMES
        Automaton a_times = new AutomatonImpl();
        a_times.addState(0, true, false);
        a_times.addState(1, false, true);
        a_times.addTransition(0, '*', 1);

        // DIV
        Automaton a_div = new AutomatonImpl();
        a_div.addState(0, true, false);
        a_div.addState(1, false, true);
        a_div.addTransition(0, '/', 1);

        // LPAREN
        Automaton a_lparen = new AutomatonImpl();
        a_lparen.addState(0, true, false);
        a_lparen.addState(1, false, true);
        a_lparen.addTransition(0, '(', 1);

        // RPAREN
        Automaton a_rparen = new AutomatonImpl();
        a_rparen.addState(0, true, false);
        a_rparen.addState(1, false, true);
        a_rparen.addTransition(0, ')', 1);

        // WHITE_SPACE: one or more of ' ', '\n', '\r', '\t'
        Automaton a_ws = new AutomatonImpl();
        a_ws.addState(0, true, false);
        a_ws.addState(1, false, true);
        char[] wsChars = new char[] { ' ', '\n', '\r', '\t' };
        for (char c : wsChars) {
            a_ws.addTransition(0, c, 1);
            a_ws.addTransition(1, c, 1);
        }

        // Register automata in the lexer
        mylex.add_automaton(TokenType.NUM, a_num);
        mylex.add_automaton(TokenType.PLUS, a_plus);
        mylex.add_automaton(TokenType.MINUS, a_minus);
        mylex.add_automaton(TokenType.TIMES, a_times);
        mylex.add_automaton(TokenType.DIV, a_div);
        mylex.add_automaton(TokenType.LPAREN, a_lparen);
        mylex.add_automaton(TokenType.RPAREN, a_rparen);
        mylex.add_automaton(TokenType.WHITE_SPACE, a_ws);

        
        this.lex = mylex;
    }

}