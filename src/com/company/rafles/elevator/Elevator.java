package com.company.rafles.elevator;

import com.company.rafles.entity.Clock;
import com.company.rafles.entity.Entity;
import com.company.rafles.listeners.IListener;

import java.util.ArrayList;

public class Elevator extends Entity {

    public enum Doors {
        OPEN,CLOSED
    }

    public enum State {
        MOVEUP,MOVEDOWN,OPENDOORS,BLOCK
    }

    // TODO: 02.06.2018 add other methods (passengers,time)
    private ArrayList<IListener> listeners;
    private ArrayList<Boolean> buttons;
    private ArrayList<Floor> floors;
    private int currentFloor;
    private Doors doors;
    private State state;
    private int size;
    private ArrayList<Passenger> passengers;

    public Elevator(int numbFloors, Clock clock, int size) {
        this.clock = clock;
        this.listeners = new ArrayList<IListener>();
        this.buttons = new ArrayList<Boolean>();
        this.floors = new ArrayList<Floor>();
        this.currentFloor = 0;
        this.doors = Doors.CLOSED;
        this.size = size;
        this.state = State.BLOCK;
        this.passengers = new ArrayList<Passenger>();
    }

    public ArrayList<IListener> getListeners() {
        return listeners;
    }

    public ArrayList<Boolean> getButtons() {
        return buttons;
    }

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Doors getDoors() {
        return doors;
    }

    public int getSize() {
        return size;
    }

    public State getState() {
        return state;
    }

    private void openDoors() {
        this.doors = Doors.OPEN;
    }

    private void closeDoors() {
        this.doors = Doors.CLOSED;
    }

    private boolean isDoorsOpen() {
        if (this.getDoors() == Doors.OPEN)
            return true;
        else
            return false;
    }

    private int getNumberOfPassengers() {
        return this.passengers.size();
    }

    private int calculateSpaceLeft() {
        return this.getSize() - this.getNumberOfPassengers();
    }

    private void moveUp() {
        if(this.isDoorsOpen()) {
            throw new Error("The doors are opened. It's impossible to move with opened doors");
        }
        else if (this.currentFloor == this.floors.size()) {
            throw new Error("You are already on the last floor");
        }
        else {
            this.currentFloor = currentFloor + 1;
        }
    }

    private void moveDown() {
        if(this.isDoorsOpen()) {
            throw new Error("The doors are opened. It's impossible to move with opened doors");
        }
        else if (this.currentFloor == 0) {
            throw new Error("You are already on the ground floor");
        }
        else {
            this.currentFloor = currentFloor - 1;
        }
    }

    private void releasePassengers() {
        if (!this.isDoorsOpen()) {
            throw new Error("You have to open the doors first!");
        }

        int counter = 0;

        for (Passenger pass: this.getPassengers()) {
            if(pass.getDestination() == this.getCurrentFloor()) {
                counter++;
                this.passengers.remove(pass);
            }
        }
    }

    private void takePassengers(Floor.Direction direction) {
        if (!this.isDoorsOpen()) {
            throw new Error("You have to open the doors first!");
        }

        Floor currentFloor = this.getFloors().get(this.currentFloor);
        ArrayList<Passenger> passengers;

        passengers = currentFloor.popPassengers(direction,this.calculateSpaceLeft());
    }

    public void goUp() {
        this.state = State.MOVEUP;
    }

    public void goDown() {
        this.state = State.MOVEDOWN;
    }

    public void openUp() {
        this.state = State.OPENDOORS;
    }

    public void blockElevator() {
        this.state = State.BLOCK;
    }

    @Override
    public void update() {
        //patrz na stan i wykonaj odpiwedniaczynność

        switch (this.getState()) {
            case BLOCK: {
                this.block();
                break;
            }
            case MOVEUP: {
                this.unblock();
                if (this.isDoorsOpen()) {
                    this.takePassengers(Floor.Direction.UP);
                    this.closeDoors();
                }
                this.moveUp();
                this.block();
                break;
            }
            case MOVEDOWN: {
                this.unblock();
                if (this.isDoorsOpen()) {
                    this.takePassengers(Floor.Direction.DOWN);
                    this.closeDoors();
                }
                this.moveDown();
                this.block();
                break;
            }
            case OPENDOORS: {
                this.unblock();
                this.openDoors();
                this.releasePassengers();
                this.block();
                break;
            }
        }
    }

    public FullState getFullState() {
        // TODO: 02.06.2018 change this when State class will be implemented
        return new FullState();
    }



}
