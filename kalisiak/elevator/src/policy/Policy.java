package policy;

import elevator.Elevator;

public class Policy implements IPolicy {

    private Elevator elevator;

    public Policy(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void decide() {

    }
}
