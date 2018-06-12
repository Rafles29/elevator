package policy;

import elevator.Elevator;
import elevator.Floor;
import elevator.Passenger;
import listeners.IListener;

import java.util.ArrayDeque;
import java.util.Queue;

public class FCFS implements IPolicy, IListener {

    protected Elevator elevator;
    protected Queue<Integer> floors_queue;

    public FCFS(Elevator elevator) {
        this.elevator = elevator;
        this.floors_queue = new ArrayDeque<>();
        elevator.getListeners().add(this);  // TODO create addListener in elevator
    }

    @Override
    public void decide() {
        int current_floor = elevator.getCurrentFloor();

        if (floors_queue.contains(current_floor)) {
            this.openUp();
        } else this.choose_next(current_floor);
    }

    protected void choose_next(int current_floor) {
        Integer next_floor = floors_queue.peek();

        if (next_floor == null)
            this.idle();
        else {
            if (next_floor > current_floor)
                this.goUp();
            else
                this.goDown();
        }
    }

    protected void idle() {
        Floor floor = elevator.getFloors().get(elevator.getCurrentFloor());
        if (floor.isButtonPressed(Floor.Direction.DOWN))
            this.goDown();
        else if (floor.isButtonPressed(Floor.Direction.UP))
            this.goUp();
        else
            this.stay();
    }

    protected void openUp() {
        int current_floor = elevator.getCurrentFloor();
        floors_queue.remove(current_floor);
        elevator.openUp();
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

    private void push_floor(int floor) {
        if (!this.floors_queue.contains(floor))
            this.floors_queue.add(floor);
    }

    @Override
    public void personLeftFloor(int floor, Passenger passenger) {
        this.push_floor(passenger.getDestination());
    }

    @Override
    public void pressUpButton(int floor) {
        this.push_floor(floor);
    }

    @Override
    public void pressDownButton(int floor) {
        this.push_floor(floor);
    }

}
