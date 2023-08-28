package flux.box;


import org.junit.Before;
import org.junit.Test;


import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.function.Consumer;

import static org.mockito.Matchers.any;


@RunWith(MockitoJUnitRunner.class)
public class FluxBoxTest {
    InputGate inGate;
    @Mock
    public Consumer<FluxEvent> inputChannel1;
    @Mock
    public OutputGate outputGate;

    @Mock
    public InternalProcessor internalProcessor;
    @Before
    public void setup() {
        inGate = new InputGate(Map.of("channel1",inputChannel1));

    }

    @Test
    public void testEmptyBox(){
        FluxBox fb = new FluxBox(inGate,outputGate,internalProcessor);
        Envelope envelope = new Envelope("channel1",new FluxEvent());
        fb.getInputGate().post(envelope);
        Mockito.verify(inputChannel1,Mockito.times(1)).accept(any());
    }

}