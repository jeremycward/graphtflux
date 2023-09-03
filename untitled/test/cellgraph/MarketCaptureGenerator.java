package cellgraph;

import cellgraph.mutations.MktDataCapture;
import org.apache.commons.lang3.time.DurationUtils;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarketCaptureGenerator {
    private final MktDataIdentifier mktDataIdentifier;

    private final Stack<Double> values= new Stack<>();
    private final NormalDistribution normalDistribution = new NormalDistribution();
    private final Double initValue;

    private final LocalDateTime startTime = LocalDateTime.now();

    public MarketCaptureGenerator(MktDataIdentifier mktDataIdentifier, Double initValue) {
        this.mktDataIdentifier = mktDataIdentifier;
        this.initValue = initValue;
        values.push(initValue);
    }
    public List<MktDataCapture> next(int count){
        return
        IntStream.rangeClosed(0,count-1).boxed()
                .peek(it->values.push(normalDistribution.sample()* 0.01 + values.peek()))
                .map(this::getCapture).collect(Collectors.toUnmodifiableList());

    }

    public int count() {
        return values.size();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    private MktDataCapture getCapture(int count){
        return new MktDataCapture(mktDataIdentifier,values.peek(),LocalDateTime.now());
    }

}
