package policy;

import elevator.Elevator;
import elevator.Passenger;

import java.util.*;

public class FCFSPolicy extends Policy {

    protected Queue<Integer> floors_queue;
    protected Set<Integer> targets;

    public FCFSPolicy(Elevator elevator) {
        super(elevator);
        this.floors_queue = new ArrayDeque<>();
        this.targets = new HashSet<>();
    }

    @Override
    public void decide() {
        int current_floor = this.elevator.getCurrentFloor();
        int next_floor = this.choose_next();
   
        if (this.elevator.isDoorsOpen() && current_floor == next_floor) {
            this.floors_queue.remove(current_floor);
            next_floor = this.choose_next();
        }
        
        if (this.elevator.isDoorsOpen()) 
            this.goToFloor(next_floor);
        else if (current_floor == next_floor)
            this.openUp();
        else if (this.isUpPresed() && next_floor > current_floor)
            this.openUp();
        else if (this.isDownPresed() && next_floor < current_floor)
            this.openUp();
        else if (targets.contains(current_floor))
            this.openUp();
        else
            this.goToFloor(next_floor);
    }

    @Override
    public void peopleEntered(int floor, int newCount, List<Passenger> passengers) {
        if (this.isFloorEmpty(floor))
            this.floors_queue.remove(floor);
        else 
            this.push_floor(floor);
    }

    protected int choose_next() {
        int current_floor = this.elevator.getCurrentFloor();
        Integer next_floor = floors_queue.peek();

        if (next_floor != null)
            return next_floor;

        if (this.isUpPresed())
            return current_floor + 1;

        if (this.isDownPresed())
            return current_floor - 1;

        return current_floor;
    }

    @Override
    protected void openUp() {
        int current_floor = this.elevator.getCurrentFloor();
        this.targets.remove(current_floor);
        this.floors_queue.remove(current_floor);
        super.openUp();
    }

    private void push_floor(int floor) {
        if (!this.floors_queue.contains(floor))
            this.floors_queue.add(floor);
    }

    @Override
    public void personLeftFloor(int floor, Passenger passenger) {
        int target_floor = passenger.getDestination();
        this.targets.add(target_floor);
        this.push_floor(target_floor);
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
