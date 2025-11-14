import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AutomatonImpl implements Automaton {

    class StateLabelPair {
        int state;
        char label;
        public StateLabelPair(int state_, char label_) { state = state_; label = label_; }

        @Override
        public int hashCode() {
            return Objects.hash((Integer) state, (Character) label);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof StateLabelPair)) return false;
            StateLabelPair o1 = (StateLabelPair) o;
            return (state == o1.state) && (label == o1.label);
        }
    }

    HashSet<Integer> start_states;
    HashSet<Integer> accept_states;
    HashSet<Integer> current_states;
    HashMap<StateLabelPair, HashSet<Integer>> transitions;

    public AutomatonImpl() {
        start_states = new HashSet<Integer>();
        accept_states = new HashSet<Integer>();
        transitions = new HashMap<StateLabelPair, HashSet<Integer>>();
        current_states = new HashSet<Integer>();
    }


    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
        // TODO Auto-generated method stub
         if (is_start) start_states.add(s);
        if (is_accept) accept_states.add(s);
    }

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
       StateLabelPair key = new StateLabelPair(s_initial, label);
        HashSet<Integer> set = transitions.get(key);
        if (set == null) {
            set = new HashSet<Integer>();
            transitions.put(key, set);
        }
        set.add(s_final);
    }
    

    @Override
    public void reset() {
        // TODO Auto-generated method stub
       current_states = new HashSet<Integer>(start_states);
    }

    @Override
    public void apply(char input) {
        HashSet<Integer> next = new HashSet<Integer>();
        for (Integer s : current_states) {
            StateLabelPair key = new StateLabelPair(s, input);
            HashSet<Integer> targets = transitions.get(key);
            if (targets != null) {
                next.addAll(targets);
            }
        }
        current_states = next;
    }

       
    

    @Override
    public boolean accepts() {
        for (Integer s : current_states) {
            if (accept_states.contains(s)) return true;
        }
        return false;
    }

    @Override
    public boolean hasTransitions(char label) {
       for (Integer s : current_states) {
            StateLabelPair key = new StateLabelPair(s, label);
            if (transitions.containsKey(key)) return true;
        }
        return false;
    }

}


