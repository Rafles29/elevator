package policy;

import elevator.Elevator;
import elevator.Floor;
import listeners.IListener;

public abstract class Policy implements IPolicy, IListener {

    protected Elevator elevator;

    public Policy(Elevator elevator) {
        this.elevator = elevator;
        elevator.getListeners().add(this);  // TODO create addListener in elevator
    }

    protected void goToFloor(int floor) {
        if (floor == elevator.getCurrentFloor())
            this.stay();
        else if (floor > elevator.getCurrentFloor())
            this.goUp();
        else
            this.goDown();
    }

    protected boolean isUpPresed() {
        Floor floor = elevator.getFloors().get(elevator.getCurrentFloor());
        return floor.isButtonPressed(Floor.Direction.UP);
    }

    protected boolean isDownPresed() {
        Floor floor = elevator.getFloors().get(elevator.getCurrentFloor());
        return floor.isButtonPressed(Floor.Direction.DOWN);
    }

    protected boolean isFloorEmpty(int floor_nr) {
        Floor floor = elevator.getFloors().get(floor_nr);
        return floor.getGoingDown().size() + floor.getGoingUp().size() == 0;
    }

    protected void goDown() {
        elevator.goDown();
    }

    protected void goUp() {
        elevator.goUp();
    }

    protected void stay() {
        elevator.stay();
    }

    protected void openUp() { elevator.openUp(); }

}
