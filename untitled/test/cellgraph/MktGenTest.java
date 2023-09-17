package cellgraph;

import cellgraph.mutations.MktDataCapture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Currency;


@RunWith(MockitoJUnitRunner.class)
public class MktGenTest {
    @Test
    public void testWithCountLimit(){
        MarketCaptureFluxGenerator fl =
        MarketCaptureFluxGenerator.aBuilder()
                .withMarketData(new MktDataIdentifier("3M",Currency.getInstance("EUR")))
                .withInterval(Duration.ofMillis(5))
                .stoppingAfterCount(22).build();
        fl.start();
        StepVerifier.create(fl.getFlux())
                .expectNextCount(22)
                .verifyComplete();

    }
    @Test
    public void testWithDurationLimit(){
        MarketCaptureFluxGenerator fl =
                MarketCaptureFluxGenerator.aBuilder()
                        .withMarketData(new MktDataIdentifier("3M",Currency.getInstance("EUR")))
                        .withInterval(Duration.ofMillis(100))
                          .stoppingAfterDurationOf(Duration.ofSeconds(1)).build();

        fl.start();
        Flux<MktDataCapture> captureFlux = fl.getFlux();

        StepVerifier.create(captureFlux)
                .expectNextCount(9)
                .thenAwait(Duration.ofMillis(500))
                .verifyComplete();

    }
}
