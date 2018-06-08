package listeners;

public interface IListener {


    // TODO: 02.06.2018 only prototype not a final version
    void newPersonOnFloor(int floor);
    void personLeftFloor(int floor);
    void peopleExited(int floor, int newCount);
    void peopleEntered(int floor, int newCount);
    void elevatorToFrom(int to, int from, int passengersCount);
    void openDoor(int floor);
    void closeDoor(int floor);
    void pressUpButton(int floor);
    void pressDownButton(int floor);
    void turnOffButtons(int floor);
}
