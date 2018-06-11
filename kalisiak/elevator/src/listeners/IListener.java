package listeners;

import elevator.Passenger;
import java.util.List;

public interface IListener {


    // TODO: 02.06.2018 only prototype not a final version
    void newPersonOnFloor(int floor, Passenger passenger);
    void personLeftFloor(int floor, Passenger passenger);
    void peopleExited(int floor, int newCount, List<Passenger> passengers);
    void peopleEntered(int floor, int newCount, List<Passenger> passengers);
    void elevatorToFrom(int to, int from, int passengersCount);
    void openDoor(int floor);
    void closeDoor(int floor);
    void pressUpButton(int floor);
    void pressDownButton(int floor);
    void turnOffButtons(int floor);
    void initElevator(int floor, int passengersCount);
    void updateGui(long currentTime);
}
