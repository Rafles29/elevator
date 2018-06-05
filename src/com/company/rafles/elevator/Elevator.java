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
        STATIONARY,MOVING_UP,MOVING_DOWN,DOORS_OPENNING,DOORS_CLOSING,GETTING_IN_PEOPLE,GETTING_OUT_PEOPLE
    }
    public enum Decision {
        MOVEUP,MOVEDOWN,OPENDOORS
    }
    //enums

    private ArrayList<IListener> listeners;
    private ArrayList<Floor> floors;
    private int currentFloor;
    private Doors doors;
    private State state;
    private Decision decision;
    private int numbFloors;
    private int size;
    private ArrayList<Passenger> passengers;

    public Elevator(int numbFloors, Clock clock, int size) {
        this.numbFloors = numbFloors;
        this.clock = clock;
        this.clock.pushUpdate(this,0);
        this.listeners = new ArrayList<IListener>();
        this.floors = new ArrayList<>();
        for (int i=0;i<numbFloors;i++) {
            this.floors.add(new Floor());
        }
        this.currentFloor = 0;
        this.doors = Doors.CLOSED;
        this.size = size;
        this.state = State.STATIONARY;
        this.decision = null;
        this.passengers = new ArrayList<Passenger>();
        this.block();
    }


    //Internal methods
    public void openDoors() {
        this.doors = Doors.OPEN;
    }

    public void closeDoors() {
        this.doors = Doors.CLOSED;
    }

    public boolean isDoorsOpen() {
        if (this.getDoors() == Doors.OPEN)
            return true;
        else
            return false;
    }

    public int getNumberOfPassengers() {
        return this.passengers.size();
    }

    public int calculateSpaceLeft() {
        return this.getSize() - this.getNumberOfPassengers();
    }

    public void moveUp() {
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

    public void moveDown() {
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

    public void releasePassengers() {
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

    public void takePassengers(Floor.Direction direction) {
        if (!this.isDoorsOpen()) {
            throw new Error("You have to open the doors first!");
        }

        Floor currentFloor = this.getFloors().get(this.currentFloor);
        ArrayList<Passenger> passengers;

        passengers = currentFloor.popPassengers(direction,this.calculateSpaceLeft());
        this.passengers.addAll(passengers);
    }
    //Internal methods

    //For Artur
    public int getNumbFloors() {
        return numbFloors;
    }

    public Decision getDecision() {
        return decision;
    }

    public ArrayList<IListener> getListeners() {
        return listeners;
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

    public ArrayList<Boolean> getButtons(){
        ArrayList<Boolean> buttons = new ArrayList<>();
        for (int i=0; i<this.floors.size();i++) {
            buttons.add(new Boolean(false));
        }

        for(int i=0;i<this.passengers.size();i++){
            buttons.set(this.passengers.get(i).getDestination(),true);
        }
        return buttons;
    }

    public void goUp() {
        this.unblock();
        this.decision = Decision.MOVEUP;
    }

    public void goDown() {
        this.unblock();
        this.decision = Decision.MOVEDOWN;
    }

    public void openUp() {
        this.unblock();
        this.decision = Decision.OPENDOORS;
    }

    public void addPassengerToFloor(int floor, Passenger passenger) {
        this.floors.get(floor).pushPassenger(passenger);
    }
    //For Artur


    //Machine State
    @Override
    public void update() {

        if(this.state == State.STATIONARY) {

            if (this.decision == Decision.OPENDOORS) {

                this.state = State.DOORS_OPENNING;
                this.clock.pushUpdate(this,1);

            }else if(this.decision == Decision.MOVEUP) {

                if(this.isDoorsOpen()) {

                    this.state = State.DOORS_CLOSING;
                    this.clock.pushUpdate(this,1);

                } else {

                    this.state = State.MOVING_UP;
                    this.clock.pushUpdate(this,1);

                }
            }else if(this.decision == Decision.MOVEDOWN) {

                if(this.isDoorsOpen()) {

                    this.state = State.DOORS_CLOSING;
                    this.clock.pushUpdate(this,1);

                } else {

                    this.state = State.MOVING_DOWN;
                    this.clock.pushUpdate(this,1);
                }

            }
        } else if(this.state == State.DOORS_CLOSING) {

            this.closeDoors();
            this.state = State.STATIONARY;
            this.clock.pushUpdate(this,1);

        } else if(this.state == State.DOORS_OPENNING) {

            this.openDoors();
            this.state = State.GETTING_OUT_PEOPLE;
            this.clock.pushUpdate(this,1);

        } else if(this.state == State.GETTING_IN_PEOPLE) {
            if (this.decision == Decision.OPENDOORS) {

                this.block();
                //nie wiem czy wtedy pushować?
                this.clock.pushUpdate(this,1);

            }else if(this.decision == Decision.MOVEUP) {

                this.takePassengers(Floor.Direction.UP);
                this.state = State.DOORS_CLOSING;
                this.clock.pushUpdate(this,1);

            }else if(this.decision == Decision.MOVEDOWN) {

                this.takePassengers(Floor.Direction.DOWN);
                this.state = State.DOORS_CLOSING;
                this.clock.pushUpdate(this,1);
            }

        } else if(this.state == State.GETTING_OUT_PEOPLE) {

            this.releasePassengers();
            this.state = State.GETTING_IN_PEOPLE;
            this.clock.pushUpdate(this,1);

        } else if(this.state == State.MOVING_UP) {

            this.moveUp();
            this.state = State.STATIONARY;
            this.clock.pushUpdate(this,1);

        } else if(this.state == State.MOVING_DOWN) {

            this.moveDown();
            this.state = State.STATIONARY;
            this.clock.pushUpdate(this,1);
        }
    }
    //Machine State

    //For Goliat
    public FullState getFullState() {
        // TODO: 02.06.2018 change this when State class will be implemented
        return new FullState();
    }
    //For Goliat



}
