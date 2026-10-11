public class Scheduler {
    //Private attributes
    private final double base;
    private final double A;
    private final double B;

    //Default constructor
    public Scheduler(double base, double a, double b) {
        this.base = base;
        this.A = a;
        this.B = b;
    }

    //Methods
    //Determine quantum according to formula provided.
    //Assure every process at least .1 ms of processing time per visit to CPU
    //If a process finishes in less than the assigned time, reduce assignment
    public double findQuantum(Process p){
        //Assign the proc properties needed for readability
        int priority = p.getPriority();
        double execution = p.getExecutionTime();
        double service =  p.getServiceTime();
        double quantum = (base + (10 - (double) priority)/A - B * execution);
        final double MIN_QUANTUM = .0001;
        if (quantum <= MIN_QUANTUM){quantum = MIN_QUANTUM;}
        else if(quantum > service){quantum = service;}
        return quantum;
    }
}
