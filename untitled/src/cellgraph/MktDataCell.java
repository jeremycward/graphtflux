package cellgraph;

import cellgraph.mutations.MktDataCapture;
import demo.Action;
import demo.BaseCellImpl;
import demo.Mutation;

import java.util.List;
import java.util.stream.Collectors;

public class MktDataCell extends BaseCellImpl<MktDataCapture> {
    private final MktDataIdentifier mktDataIdentifier;

    public MktDataCell( MktDataIdentifier mktDataIdentifier) {
        super(MktDataCapture.class, mktDataIdentifier.getId());
        this.mktDataIdentifier = mktDataIdentifier;
    }


    @Override
    public Action process(Action input) {
        List<Mutation> mutationsApplied =
        input.getMutations().stream()
                .filter(MktDataCapture.class::isInstance)
                .map(MktDataCapture.class::cast)
                .filter(capture->capture.getMktDataIdentifier().equals(mktDataIdentifier))
                .map(mutation->this.regenerate(mutation)).collect(Collectors.toUnmodifiableList());
        return new Action(mutationsApplied);
    }

    public MktDataIdentifier getMktDataIdentifier() {
        return mktDataIdentifier;
    }
}


