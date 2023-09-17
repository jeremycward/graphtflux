package cellgraph;

import demo.Cell;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

public class MajorCcyGraph {

    private final CellGraph cellGraph;
    private final List<MarketSegGenerator> mktSetGenerators;

    private MajorCcyGraph(String[] shortTenors, String[] longTenors, Currency[] ccys){
        Pair<CellGraph, List<MarketSegGenerator>> results = buildGraph(shortTenors,longTenors,ccys);
        cellGraph = results.getLeft();
        mktSetGenerators = results.getRight();
    }



    private static Pair<CellGraph, List<MarketSegGenerator>> buildGraph(String[] shortTenors,String[] longTenors,Currency[] ccys) {
        final List<MarketSegGenerator> marketSegGenerators = new ArrayList<>();
        CellGraph gr = new CellGraph();
        List<Cell> curveCells = new ArrayList<>();
        InputCell inputCell = new InputCell();
        gr.addCell(inputCell, List.of());
        Arrays.stream(ccys).forEach(ccy -> {
            CurrencyCell ccyCell = new CurrencyCell(ccy);
            gr.addCell(ccyCell, List.of(inputCell));
            List<Cell> shortTenorCells = Arrays.stream(shortTenors)
                    .map(tenor->tenorToMktDataCells(tenor,ccy,ccyCell,gr))
                    .collect(Collectors.toUnmodifiableList());


            List<Cell> longTenorCells = Arrays.stream(longTenors)
                            .map(tenor->tenorToMktDataCells(tenor,ccy,ccyCell,gr))
                                    .collect(Collectors.toUnmodifiableList());

            SegmentCell shortEndSeg = new SegmentCell(String.format("%s_SHORT_END_SEG", ccy.getCurrencyCode()));
            gr.addCell(shortEndSeg, shortTenorCells);
            marketSegGenerators.add(segGenerators(shortTenorCells, ccy));
            SegmentCell longEndSeg = new SegmentCell(String.format("%s_LONG_END_SEG", ccy.getCurrencyCode()));
            gr.addCell(longEndSeg, longTenorCells);
            marketSegGenerators.add(segGenerators(longTenorCells, ccy));
            CurveCell ccyCurve = new CurveCell
                    (String.format("%s_PRICING_CURVE", ccy.getCurrencyCode()));
            curveCells.add(ccyCurve);
            gr.addCell(ccyCurve, List.of(shortEndSeg, longEndSeg));
        });
        gr.addCell(new OutputCell(), curveCells);
        return Pair.of(gr, marketSegGenerators);
    }
    public static Builder aCcyGraph(){
        return new Builder();
    }
    private static MktDataCell tenorToMktDataCells(String tenor,Currency ccy,CurrencyCell ccyCell,CellGraph cellGraph){
        List<MktDataCell> mktDataCellList = new ArrayList<>();
        MktDataIdentifier identifier = new MktDataIdentifier(tenor, ccy);
        MktDataCell mktDataCell = new MktDataCell(identifier);
        cellGraph.addCell(mktDataCell, List.of(ccyCell));
        return mktDataCell;
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

    public static class Builder{
        private    Currency[] majors = {
                Currency.getInstance("EUR"),
                Currency.getInstance("USD"),
                Currency.getInstance("GBP"),
                Currency.getInstance("CAD"),
                Currency.getInstance("JPY"),


        };
        private   String[] shortTenors = {
                "1D",
                "1M",
                "2M",
                "6M",
        };
        private   String[] longTenors = {
                "1Y",
                "2Y",
                "5Y",
                "25Y"

        };
        public MajorCcyGraph build(){
            return new MajorCcyGraph(shortTenors,longTenors,majors);
        }
        public Builder withCcys(String... ccys){
            majors = Arrays.stream(ccys).map(it->Currency.getInstance(it)).collect(Collectors.toUnmodifiableList()).toArray(new Currency[]{});
            return this;
        }
        public Builder withShortTenors(String... shortTenors){
            this.shortTenors = shortTenors;
            return this;
        }
        public Builder withLongTenors(String... longTenors){
            this.longTenors = longTenors;
            return this;
        }

    }
}