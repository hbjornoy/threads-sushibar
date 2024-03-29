import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;


public class SushiBar {

    //SushiBar settings
    private static int waitingAreaCapacity = 15;
    private static int waitressCount = 8;
    private static int duration = 4;
    public static int maxOrder = 10;
    public static int waitressWait = 50; // Used to calculate the time the waitress spends before taking the order
    public static int customerWait = 2000; // Used to calculate the time the customer spends eating
    public static int doorWait = 100; // Used to calculate the interval at which the door tries to create a customer 100
    public static boolean isOpen = true; 

    //Creating log file
    private static File log;
    private static String path = "./";

    //Variables related to statistics
    public static SynchronizedInteger customerCounter;
    public static SynchronizedInteger servedOrders;
    public static SynchronizedInteger takeawayOrders;
    public static SynchronizedInteger totalOrders;

    // public static ThreadGroup waitress_group = new ThreadGroup("parent waitress thread group"); 

    public static void main(String[] args) {
        log = new File(path + "log.txt");

        //Initializing shared variables for counting number of orders
        customerCounter = new SynchronizedInteger(0);
        totalOrders = new SynchronizedInteger(0);
        servedOrders = new SynchronizedInteger(0);
        takeawayOrders = new SynchronizedInteger(0);

        // Create a new clock
        Clock clock = new Clock(duration);
        SushiBar.write(Thread.currentThread().getName() + ": Clock is created");

        // Shared resource
        WaitingArea waitingArea = new WaitingArea(waitingAreaCapacity);
        SushiBar.write(Thread.currentThread().getName() + ": WaitingArea is CREATED");

        for(int i = 0; i < waitressCount; i++) {
          Thread thread = new Thread(new Waitress(waitingArea));
          // waitingArea.connectWaitress(waitress);

          thread.start();
          waitingArea.connectWaitress(thread);
        }

        // Create a door
        Thread door = new Thread(new Door(waitingArea));
        waitingArea.connectDoor(door);
        (new Thread(door)).start();

        ArrayList<Thread> waitressList = waitingArea.getWaitresses();
        for(Thread waitress: waitressList) {
          try {
            // forces the waitresses to finish tasks and join threads
            waitress.join();
          } catch (InterruptedException e) {}
        }

        SushiBar.write(Thread.currentThread().getName() + ": Total orders:" + totalOrders.get());
        SushiBar.write(Thread.currentThread().getName() + ": Takeaway orders: " + takeawayOrders.get());
        SushiBar.write(Thread.currentThread().getName() + ": Served orders:" + servedOrders.get());
        SushiBar.write(Thread.currentThread().getName() + ": Customers: " + customerCounter.get());
    }

    //Writes actions in the log file and console
    public static void write(String str) {
        try {
            FileWriter fw = new FileWriter(log.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(Clock.getTime() + ", " + str + "\n");
            bw.close();
            System.out.println(Clock.getTime() + ", " + str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
