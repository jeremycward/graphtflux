package cellgraph;

import cellgraph.mutations.MktDataCapture;
import demo.Action;
import demo.BaseCellImpl;
import demo.Mutation;

import java.util.Currency;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CurrencyCell extends BaseCellImpl<Currency> {
    private final Currency ccy;
    private static final Logger logger = Logger.getLogger(CurrencyCell.class.getName());

    public CurrencyCell(Currency ccy) {

        super(Currency.class, ccy.getCurrencyCode());
        this.ccy = ccy;
    }

    @Override
    public Action process(Action input) {
        List<Mutation> mutationstoApply =
        input.getMutations().stream()
                .filter(MktDataCapture.class::isInstance)
                .map(MktDataCapture.class::cast)
                .filter(mutation->mutation.getMktDataIdentifier().getCurrency()==ccy).collect(Collectors.toUnmodifiableList());



        return mutationstoApply.isEmpty() ? Action.EMPTY_ACTION : new Action(mutationstoApply);
    }
}
