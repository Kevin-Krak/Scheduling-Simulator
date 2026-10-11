public class Event implements Comparable<Event> {
    //Define event types
    public enum EventType {ARRIVAL, DEPARTURE, EXPIRATION}

    //Private attributes
    private final EventType type;
    private final Double eventTime;
    private Process proc;

    //Default constructor
    public Event(EventType type, Double eventTime, Process proc) {
        this.type = type;
        this.eventTime = eventTime;
        this.proc = proc;
    }


    //Getters
    public EventType getType() {return this.type;}
    public Double getEventTime() {return this.eventTime;}
    public Process getProc() {return this.proc;}
    //Setters
    public void setProc(Process proc){this.proc = proc;}

    //Methods
    @Override
    public int compareTo(Event second) {
        return Double.compare(this.getEventTime(), second.getEventTime());
    }
}
