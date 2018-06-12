package policy;

import elevator.Elevator;

public class MomentumPolicy extends FCFSPolicy {

    private enum Momentum {NONE, UP, DOWN}

    private Momentum momentum;

    public MomentumPolicy(Elevator elevator) {
        super(elevator);
        this.momentum = Momentum.NONE;
    }

    @Override
    protected int choose_next() {
        int current_floor = this.elevator.getCurrentFloor();

        switch (this.momentum) {
            case UP:
                for (int floor : this.floors_queue)
                    if (floor > current_floor)
                        return current_floor + 1;
                break;
            case DOWN:
                for (int floor : this.floors_queue)
                    if (floor < current_floor)
                        return current_floor - 1;
                break;
        }

        return super.choose_next();
    }

    @Override
    protected void openUp() {
        super.openUp();
    }

    @Override
    protected void goDown() {
        this.momentum = Momentum.DOWN;
        super.goDown();
    }

    @Override
    protected void goUp() {
        this.momentum = Momentum.UP;
        super.goUp();
    }

    @Override
    protected void stay() {
        this.momentum = Momentum.NONE;
        super.stay();
    }

}
