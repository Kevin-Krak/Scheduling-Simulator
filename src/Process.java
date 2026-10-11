import java.util.Random;

public class Process {
    //Private attributes
    private final int PID;
    private final double arrivalTime;
    private double serviceTime;
    private double executionTime;
    private final int priority;
    //Bits to implement for stat tracking
    private int contextSwitches;


    //Default constructor
    public Process(int id, double c, double a, double s) {
        this.PID = id;
        this.arrivalTime = generateArrival(a) + c;
        this.serviceTime = generateServicing(s);
        this.executionTime = 0;
        this.priority = generatePriority();

        contextSwitches = 0;
    }

    //Special constructor for first process. Preset ID and arrival time
    public Process(double s){
        this.PID = 0;
        this.arrivalTime = 0;
        this.serviceTime = generateServicing(s);
        this.executionTime = 0;
        this.priority = generatePriority();
    }

    //Getters
    public int getID() {return this.PID;}
    public double getArrivalTime() {return this.arrivalTime;}
    public double getServiceTime() {return this.serviceTime;}
    public double getExecutionTime() {return this.executionTime;}
    public int getPriority() {return this.priority;}
    public int getContextSwitches() {return this.contextSwitches;}

    //Setters
    public void setServiceTime(double serviceTime) {this.serviceTime = serviceTime;}
    public void setExecutionTime(double executionTime) {this.executionTime = executionTime;}
    public void setContextSwitches(int contextSwitches) {this.contextSwitches = contextSwitches;}

    //Helper functions
    //Generate random number 0-1, use with arrival rate...
    //to generate next arrival time via poisson distribution
    public double generateArrival(double aRate){
        //Random rand = new Random();
        //final double prob = rand.nextDouble();
        final double prob = Math.random();
        return ((-1.0/aRate) * Math.log(prob));
    }

    //Generate random number 0-1, use with arrival rate...
    //to generate service time of process via exponential distribution
    public double generateServicing(double sTime){
        Random rand = new Random();
        final double prob = rand.nextDouble();
        return -sTime * Math.log(prob);
    }

    //Generate random priority 1-10 with equal odds
    public int generatePriority(){
        Random rand = new Random();
        return rand.nextInt(10) + 1;
    }
}
