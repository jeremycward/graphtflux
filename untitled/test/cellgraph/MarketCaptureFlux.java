package cellgraph;

import cellgraph.mutations.MktDataCapture;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;

public class MarketCaptureFlux {

    private final Duration interval;

    private final Predicate<Pair<Duration, Integer>> stopCondidtion;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

    private final MarketCaptureGenerator marketCaptureGenerator;
    private final Flux<MktDataCapture> flux;
    private final Sinks.Many<MktDataCapture> mktCaptures = Sinks.many().unicast().onBackpressureBuffer();

    private volatile boolean completed = false;


    private MarketCaptureFlux(MarketCaptureGenerator marketCaptureGenerator, Predicate<Pair<Duration, Integer>> stopCondition,Duration interval) {
        this.interval = interval;
        this.marketCaptureGenerator = marketCaptureGenerator;
        this.flux = mktCaptures.asFlux();
        this.stopCondidtion = stopCondition;
    }

    public Flux<MktDataCapture> getFlux() {
        return flux;
    }

    public void start() {

        final Runnable producer = new Runnable() {
            @Override
            public void run() {
                if (!completed) {
                    final boolean localComplete = stopCondidtion.test(getCriteria(marketCaptureGenerator)) ;
                    if (localComplete) {
                        completed = true;
                        mktCaptures.tryEmitComplete();

                    } else {
                        List<MktDataCapture> capture = marketCaptureGenerator.next(1);
                        capture.stream().forEach(capt -> {
                            mktCaptures.tryEmitNext(capt);
                        });
                    }
                }
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(producer, interval.toMillis(), interval.toMillis(), TimeUnit.MILLISECONDS);
    }

    static Pair<Duration, Integer> getCriteria(MarketCaptureGenerator gen) {
        final Duration dur = Duration.between(gen.getStartTime(), LocalDateTime.now());
        return Pair.of(dur, gen.count());
    }

    public static FluxCaptureBuilder aBuilder() {
        return new FluxCaptureBuilder();
    }

    public static class FluxCaptureBuilder {
        private MktDataIdentifier id = null;
        private double initValue = 1.0;
        private Predicate<Pair<Duration, Integer>> stopCondition = (p) -> true;

        private Duration interval = Duration.ofMillis(100);


        public MarketCaptureFlux build() {
            MarketCaptureGenerator generator = new MarketCaptureGenerator(id, initValue);
            return new MarketCaptureFlux(generator, stopCondition,interval);
        }

        public FluxCaptureBuilder withMarketData(MktDataIdentifier id) {
            this.id = id;
            return this;
        }
        public FluxCaptureBuilder withInterval(Duration duration){
            this.interval = duration;
            return this;
        }


        public FluxCaptureBuilder stoppingAfterDurationOf(Duration limit) {
            this.stopCondition = (pair) -> pair.getLeft().compareTo(limit) >= 0;
            return this;
        }

        public FluxCaptureBuilder stoppingAfterCount(Integer max) {
            this.stopCondition = (pair) -> pair.getRight().compareTo(max) > 0;
            return this;
        }


    }
}
