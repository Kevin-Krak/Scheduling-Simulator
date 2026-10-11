import java.util.Random;

public class Main {
    public static void main(String[] args) {
        double arrivalRate;
        double serviceTime;
        double base;
        double A;
        double B;

        if (args.length != 5) {
            System.out.println("Error, 5 arguments expected. Please provide:");
            System.out.println("1. Average process arrival rate in processes per second.");
            System.out.println("2. Average process service time in milliseconds.");
            System.out.println("3. Base quantum.");
            System.out.println("4. Scaling factor A.");
            System.out.println("5. Scaling factor B.");
            return;
        } else {
            arrivalRate = Double.parseDouble(args[0]);
            serviceTime = Double.parseDouble(args[1]);
            base = Double.parseDouble(args[2]);
            A = Double.parseDouble(args[3]);
            B = Double.parseDouble(args[4]);
        }
        //Initialization step
        for(int i = 0; i < 5; i++) {
            Simulator simulator = new Simulator(arrivalRate, serviceTime, base, A, B);
            while (simulator.getCompletions() < 10000) {
                simulator.run();
            }
            simulator.results();
        }
    }
}
