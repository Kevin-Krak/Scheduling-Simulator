import java.util.ArrayDeque;

public class Simulator {
    //Private attributes
    //Timer and process counters
    private double clock;
    private int generations;
    private int completions;

    //Performance stats
    private double avgTurnaroundTime;
    private double avgSwitches;
    private double avgQueueSize;

    //Process generation parameters
    private final double arrivalRate;
    private final double serviceTime;
    private Boolean makeNew;

    //System state elements
    private Boolean CPUBusy;
    private EventQueue events;
    private ArrayDeque<Process> readyQueue;

    //private Scheduler scheduler;

    //Default constructor
    public Simulator(double arrivals, double service, double base, double a, double b) {
        this.clock = 0;
        this.generations = 1;
        this.completions = 0;
        this.avgTurnaroundTime = 0;
        this.avgSwitches = 0;
        this.avgQueueSize = 0;
        this.arrivalRate = arrivals;
        this.serviceTime = service;
        this.CPUBusy = false;
        this.events = new EventQueue(service, base, a, b);
        this.readyQueue = new ArrayDeque<>();
        //this.scheduler = new Scheduler(base, a, b);
        this.makeNew = true;
    }
    //Getters
    public int getCompletions() {return this.completions;}
    public boolean isCPUBusy() {return this.CPUBusy;}
    public ArrayDeque<Process> getReadyQueue() {return this.readyQueue;}
    public double getAvgTurnaroundTime() {return this.avgTurnaroundTime;}
    public double getAvgSwitches() {return this.avgSwitches;}
    public double getAvgQueueSize() {return this.avgQueueSize;}



    //Setters
    public void setCompletions(int completions) {this.completions = completions;}
    public void setCPUBusy(boolean flag) {this.CPUBusy = flag;}
    public void setMakeNew(boolean flag) {this.makeNew = flag;}
    public void setAvgTurnaroundTime(double avgTurnaroundTime) {this.avgTurnaroundTime = avgTurnaroundTime;}
    public void setAvgSwitches(double avgSwitches) {this.avgSwitches = avgSwitches;}
    public void setAvgQueueSize(double avgQueueSize) {this.avgQueueSize = avgQueueSize;}

    //Methods
    public void run(){
        //If new process arrival needs to be scheduled, do so and reset the flag.
        clock = events.handleNextEvent(this, readyQueue);
        if(makeNew){
            Process newProc = new Process(this.generations, this.clock, this.arrivalRate, this.serviceTime);
            this.events.add(new Event(Event.EventType.ARRIVAL, newProc.getArrivalTime(), newProc));
            generations++;
            makeNew = false;
        }
        //Logging section
        System.out.println("Current clock: " + clock);
        System.out.println("Current completions: " + completions);
        System.out.println();
    }

    public void results(){
        //Calculate final averages
        avgTurnaroundTime = avgTurnaroundTime / completions;
        avgSwitches = avgSwitches / completions;
        avgQueueSize = avgQueueSize / completions;

        System.out.println("Simulation complete. Statistics:");
        System.out.println();
        System.out.println("Processes completed: " + completions);
        System.out.println("Simulated running time: " + clock);
        System.out.println("Size of event queue at end of simulation: " + events.size());
        System.out.println("Size of ready queue at end of simulation: " + readyQueue.size());

        System.out.println();
        System.out.println("Average ready queue size by completed process: " + avgQueueSize);
        System.out.println("Average turnaround time: " + avgTurnaroundTime);
        System.out.println("Average switches: " + avgSwitches);
    }
}
