package demo;

import java.util.List;
import java.util.stream.Collectors;

public class Action {
    public static final Action EMPTY_ACTION = new Action(List.of());

    private final  List<Mutation> mutations;

    public Action(List<Mutation> mutations) {
        this.mutations = mutations;
    }

    public List<Mutation> getMutations() {
        return mutations;
    }
    public static Action gather(List<Action> peers){
        return new Action (peers.stream().flatMap(it->it.getMutations().stream())
                .collect(Collectors.toUnmodifiableList()));

    }

}
