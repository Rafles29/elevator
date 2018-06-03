package com.company.rafles.policy;

import com.company.rafles.elevator.Elevator;

public class Policy implements IPolicy {

    private Elevator elevator;

    public Policy(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void decide() {

    }
}
