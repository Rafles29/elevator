package com.company.rafles.elevator;

import com.company.rafles.entity.Clock;
import com.company.rafles.entity.Entity;
import com.company.rafles.listeners.IListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Elevator extends Entity {

    public enum Doors {
        OPEN,CLOSED
    }

    // TODO: 02.06.2018 add other methods (passengers,time)
    private ArrayList<IListener> listeners;
    private ArrayList<Boolean> buttons;
    private ArrayList<Floor> floors;
    private int currentFloor;
    private Doors doors;
    private ArrayList<Passenger> passengers;

    public Elevator(int numbFloors, Clock clock) {
        this.clock = clock;
        this.listeners = new ArrayList<IListener>();
        this.buttons = new ArrayList<Boolean>();
        this.floors = new ArrayList<Floor>();
        this.currentFloor = 0;
        this.doors = Doors.CLOSED;
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

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Doors getDoors() {
        return doors;
    }

    public void Open() {
        this.doors = Doors.OPEN;
    }

    public void Close() {
        this.doors = Doors.CLOSED;
    }

    public boolean isOpen() {
        if (this.doors == Doors.OPEN)
            return true;
        else
            return false;
    }

    public void moveUp() {
        if(this.isOpen()) {
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
        if(this.isOpen()) {
            throw new Error("The doors are opened. It's impossible to move with opened doors");
        }
        else if (this.currentFloor == 0) {
            throw new Error("You are already on the ground floor");
        }
        else {
            this.currentFloor = currentFloor - 1;
        }
    }

    public State getState() {
        // TODO: 02.06.2018 change this when State class will be implemented
        return new State();
    }



}
