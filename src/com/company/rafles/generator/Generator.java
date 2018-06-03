package com.company.rafles.generator;

import com.company.rafles.elevator.Elevator;
import com.company.rafles.entity.Clock;

public class Generator implements IGenerator{

    private Elevator elevator;
    private Clock clock;

    public Generator(Elevator elevator, Clock clock) {
        this.elevator = elevator;
        this.clock = clock;
    }

    @Override
    public void generate() {

    }
}
