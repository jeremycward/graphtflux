package cellgraph;

import demo.Action;
import demo.BaseCellImpl;
import demo.Cell;
import demo.Mutation;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

public class MajorCcyBuilder {


    private final CellGraph cellGraph;
    private final List<MarketSegGenerator> mktSetGenerators;


    private static final Currency[] majors = {
            Currency.getInstance("EUR"),
            Currency.getInstance("USD"),
            Currency.getInstance("GBP"),
            Currency.getInstance("CAD"),
            Currency.getInstance("JPY"),


    };
    private static final String[] tenors = {
            "1D",
            "1M",
            "2M",
            "6M",
            "1Y",
            "2Y",
            "5Y",
            "25Y"
    };

    public MajorCcyBuilder() {
        Pair<CellGraph, List<MarketSegGenerator>> results = buildGraph();
        cellGraph = results.getLeft();
        mktSetGenerators = results.getRight();
    }

    private static Pair<CellGraph, List<MarketSegGenerator>> buildGraph() {
        final List<MarketSegGenerator> marketSegGenerators = new ArrayList<>();
        CellGraph gr = new CellGraph();
        List<Cell> curveCells = new ArrayList<>();

        InputCell inputCell = new InputCell();

        gr.addCell(inputCell, List.of());
        Arrays.stream(majors).forEach(ccy -> {

            CurrencyCell ccyCell = new CurrencyCell(ccy);
            gr.addCell(ccyCell, List.of(inputCell));
            List<Cell> shortTenors = new ArrayList<>();
            List<Cell> longTenors = new ArrayList<>();
            Arrays.stream(tenors).forEach(tenor -> {
                MktDataIdentifier identifier = new MktDataIdentifier(tenor, ccy);
                MktDataCell mktDataCell = new MktDataCell(identifier);
                gr.addCell(mktDataCell, List.of(ccyCell));
                if (tenor.endsWith("Y")) {
                    longTenors.add(mktDataCell);
                } else {
                    shortTenors.add(mktDataCell);
                }
            });
            SegmentCell shortEndSeg = new SegmentCell(String.format("%s_SHORT_END_SEG", ccy.getCurrencyCode()));
            gr.addCell(shortEndSeg, shortTenors);
            marketSegGenerators.add(segGenerators(shortTenors, ccy));
            SegmentCell longEndSeg = new SegmentCell(String.format("%s_LONG_END_SEG", ccy.getCurrencyCode()));
            gr.addCell(longEndSeg, longTenors);
            marketSegGenerators.add(segGenerators(longTenors, ccy));
            CurveCell ccyCurve = new CurveCell
                    (String.format("%s_PRICING_CURVE", ccy.getCurrencyCode()));
            curveCells.add(ccyCurve);
            gr.addCell(ccyCurve, List.of(shortEndSeg, longEndSeg));
        });
        gr.addCell(new OutputCell(), curveCells);
        return Pair.of(gr, marketSegGenerators);
    }

    private static final MarketSegGenerator segGenerators(List<Cell> tenors, Currency ccy) {
        List<String> tenorsStr = tenors.stream().map(MktDataCell.class::cast)
                .map(MktDataCell::getMktDataIdentifier)
                .map(MktDataIdentifier::getTenor).collect(Collectors.toUnmodifiableList());


        return new MarketSegGenerator(ccy, tenorsStr);

    }

    public CellGraph getCellGraph() {
        return cellGraph;
    }

    public List<MarketSegGenerator> getMktSetGenerators() {
        return mktSetGenerators;
    }
}