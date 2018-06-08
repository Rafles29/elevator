package generator;

import elevator.Elevator;
import entity.Clock;

public class Generator implements IGenerator{

    private Elevator elevator;
    private Clock clock;

    public Generator(Elevator elevator, Clock clock) {
        this.elevator = elevator;
        this.clock = clock;
    }
}
