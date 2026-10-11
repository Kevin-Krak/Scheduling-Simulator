import java.util.ArrayDeque;
import java.util.PriorityQueue;

public class EventQueue extends PriorityQueue<Event> {
    //private double lastEventTime;
    private final Scheduler scheduler;

    //Default constructor
    //Probably breaks a best practice to have nested instantiations like this,
    //but it's faster and not likely to be expanded
    public EventQueue(double s, double base, double a, double b) {
        super();
        //this.lastEventTime = 0;
        this.add(new Event(Event.EventType.ARRIVAL, 0.0, new Process(s)));
        this.scheduler = new Scheduler(base, a , b);
    }

    //Methods
    public double handleNextEvent(Simulator sim, ArrayDeque<Process> ready){
        Event current;
        if (this.isEmpty()){
            throw new IllegalStateException("Event queue is empty");
        }
        else{current = this.poll();}
        System.out.println(current.getType());
        switch (current.getType()){
            case ARRIVAL -> handleArrival(sim, ready, current);
            case DEPARTURE -> handleDeparture(sim, current);
            case EXPIRATION -> handleExpiration(sim, ready, current);
        };
        return current.getEventTime();
    }

    //Process reports to scheduler. If CPU is not busy, start process immediately. Otherwise, add it to the queue
    public void handleArrival(Simulator s, ArrayDeque<Process> ready, Event c){
        System.out.println("Arrived: " + c.getProc().getID());
        //System.out.println("Servicing time: " + c.getProc().getServiceTime());
        //If this process is brand-new, set the flag for the simulator to generate another new process
        if(c.getProc().getExecutionTime() == 0){s.setMakeNew(true);}
        if (!s.isCPUBusy()){
            s.setCPUBusy(true);
            //Schedule departure event for current process at this event's time + proc's allotted quantum
            this.add(new Event(Event.EventType.EXPIRATION, planExpirationTime(c), c.getProc()));
        }
        else{ready.add(c.getProc());} //Add process linked to current event to ready queue
    }

    //Process is complete. Update statistics
    public void handleDeparture(Simulator s, Event cur){
        //Statistics update
        s.setCompletions(s.getCompletions() + 1);
        System.out.println("Departed: " + cur.getProc().getID());
        //Add turnaround time to total TaT. Can be derived from existing numbers, no need for it to be it's own attribute
        double turnAround = cur.getEventTime() - cur.getProc().getArrivalTime();
        s.setAvgTurnaroundTime(s.getAvgTurnaroundTime() + turnAround);
        //Add the departing process' context switch count to the current total.
        double contextSwitches = cur.getProc().getContextSwitches();
        s.setAvgSwitches(s.getAvgSwitches() + contextSwitches);
        //Add the current ready queue to the ready queue total
        double queueSize = s.getReadyQueue().size();
        s.setAvgQueueSize(s.getAvgQueueSize() + queueSize);
    }

    public void handleExpiration(Simulator s, ArrayDeque<Process> ready, Event c){
        Process proc = c.getProc();
        proc.setContextSwitches(proc.getContextSwitches() + 1);
        System.out.println("Expired: " + proc.getID());
        double runTime = scheduler.findQuantum(proc);
        //Subtract time just on CPU from remaining service time, add it to execution time
        proc.setServiceTime(proc.getServiceTime() - runTime);
        proc.setExecutionTime(proc.getExecutionTime() + runTime);

        //Schedule expired process for appropriate follow-up event
        //If process is done, immediate departure event
        if (proc.getServiceTime() <= 0){
            Event addDep = new Event(Event.EventType.DEPARTURE, c.getEventTime(), proc);
            this.add(addDep);
        }
        //Otherwise, immediate arrival event
        else{
            Event addArr = new Event(Event.EventType.ARRIVAL, c.getEventTime(), proc);
            this.add(addArr);
        }

        //Handle next item in ready queue or set CPU to idle
        if (ready.isEmpty()){s.setCPUBusy(false);}
        else{
            Process nextProc =  ready.removeFirst();
            c.setProc(nextProc);
            //Pull next proc but sends in time of current event which still has current proc's expire time
            Event addExp = new Event(Event.EventType.EXPIRATION, planExpirationTime(c), nextProc);
            this.add(addExp);
        }
    }

    //Helper method to set event time in a more readable format
    public double planExpirationTime(Event c){
        double currentTime = c.getEventTime();
        Process proc = c.getProc();
        return currentTime + scheduler.findQuantum(proc);
    }
}