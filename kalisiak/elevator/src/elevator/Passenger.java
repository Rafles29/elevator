package elevator;

public class Passenger {
    private static int idCounter = 0;
    
    private int id;
    private int from;
    private int destination;
    private long timeStamp;

    public Passenger(int from, int destination, long timeStamp) {
        this.id = idCounter;
        idCounter++;
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

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Passenger other = (Passenger) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
