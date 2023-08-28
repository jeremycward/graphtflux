package cellgraph;

import cellgraph.mutations.MktDataCapture;
import org.apache.commons.math3.distribution.NormalDistribution;


import java.util.*;
import java.util.stream.Collectors;

public class MarketSegGenerator {





    private final Map<MktDataIdentifier,MarketCaptureGenerator> captureGeneratorMap;
    private final NormalDistribution normalDistribution = new NormalDistribution();



    public MarketSegGenerator(Currency ccy, List<String> tenors) {

        Stack<Double> seedValues = new Stack<>();
        seedValues.push(Double.valueOf("3.14"));
        this.captureGeneratorMap = new HashMap<>();
        tenors.forEach(it->{
            MktDataIdentifier id = new MktDataIdentifier(it,ccy);
            Double nextVal = seedValues.peek() + normalDistribution.sample() ;
            seedValues.push(nextVal);
            MarketCaptureGenerator generator = new MarketCaptureGenerator(id,seedValues.peek());
            captureGeneratorMap.put(id,generator);

        });

    }

    List<MktDataCapture> next(final int count){

        return captureGeneratorMap.values().stream()
                .map(it->it.next(count)).flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableList());
    }

    public Map<MktDataIdentifier, MarketCaptureGenerator> getCaptures() {
        return captureGeneratorMap;
    }
}
