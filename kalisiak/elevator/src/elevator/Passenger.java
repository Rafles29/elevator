package elevator;

public class Passenger {
    private int from;
    private int destination;
    private long timeStamp;

    public Passenger(int from, int destination, long timeStamp) {
        this.from = from;
        this.destination = destination;
        this.timeStamp = timeStamp;
    }

    public int getFrom() {
        return this.from;
    }

    public int getDestination() {
        return destination;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
