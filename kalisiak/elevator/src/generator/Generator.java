package generator;

import elevator.Elevator;
import entity.Clock;
import entity.Entity;

public abstract class Generator extends Entity{

    private Elevator elevator;
    private Clock clock;

    public Generator(Elevator elevator, Clock clock) {
        this.elevator = elevator;
        this.clock = clock;
    }
}
