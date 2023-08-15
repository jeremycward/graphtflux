package cellgraph;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.IntegerSequence;

public class InstrumentPriceGenerator {

    private final NormalDistribution normalDistribution = new NormalDistribution();

    public double next(){
        return normalDistribution.sample();
    }

    public static void main(String[] args) {
        InstrumentPriceGenerator priceGen = new InstrumentPriceGenerator();
        IntegerSequence.range(0,106).forEach((it)->{
            System.out.println(priceGen.next());
        });
    }

}
