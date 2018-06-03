package com.company.rafles.elevator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;

public class Floor {

    public enum Direction {
        UP,DOWN
    }
    private Deque<Passenger> goingUp = new ArrayDeque<Passenger>();
    private Deque<Passenger> goingDown = new ArrayDeque<Passenger>();

    public boolean isButtonPressed(Direction direction) {
        boolean status = false;
        switch (direction) {
            case UP: {
                if(this.goingUp.peek() != null){
                    status = true;
                }
                break;
            }

            case DOWN:{
                if(this.goingDown.peek() != null){
                    status = true;
                }
                break;
            }
        }
        return status;
    }

    public void pushPassenger(Passenger passenger) {
        int diff = passenger.getDestination() - passenger.getFrom();
        if (diff > 0) {
            this.goingUp.push(passenger);
        } else {
            this.goingDown.push(passenger);
        }
    }

    private Passenger popPassenger(Direction direction) {
        Passenger passenger = new Passenger(0,0,0);
        switch (direction) {
            case UP: {
                passenger = this.goingUp.pop();
                break;
            }

            case DOWN: {
                passenger = this.goingDown.pop();
                break;
            }
        }
        return passenger;
    }

    public ArrayList<Passenger> popPassengers(Direction direction, int maxNumber) {
        ArrayList<Passenger> passengers = new ArrayList<>();
        Passenger passenger;
        int i = 0;
        while(i<maxNumber){
            try {
                passenger = this.popPassenger(direction);
                passengers.add(passenger);
                i++;
            } catch (NoSuchElementException e) {
                return passengers;
            }
        }
        return passengers;
    }
}
