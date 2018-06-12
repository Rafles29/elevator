package policy;

import elevator.Elevator;

public class FCFSMomentum extends FCFS {

    private enum Momentum {NONE, UP, DOWN}

    private Momentum momentum;

    public FCFSMomentum(Elevator elevator) {
        super(elevator);
        this.momentum = Momentum.NONE;
    }

    @Override
    protected void choose_next(int current_floor) {
        switch (this.momentum) {
            case UP:
                for (int floor : this.floors_queue)
                    if (floor > current_floor) {
                        this.goUp();
                        return;
                    }
                break;
            case DOWN:
                for (int floor : this.floors_queue)
                    if (floor < current_floor) {
                        this.goDown();
                        return;
                    }
                break;
        }

        super.choose_next(current_floor);
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
