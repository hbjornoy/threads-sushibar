import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
/**
 * This class implements a waiting area used as the bounded buffer, in the producer/consumer problem.
 */
public class WaitingArea {

    /**
     * Creates a new waiting area.
     */

    private int maxCapasity;

    // Threads
    public Thread door;
    private ArrayList<Customer> queue;
    // Question: not sure why I did not manage too use list as ArrayList≤Waitress>..
    private ArrayList<Thread> waitresses;

    public WaitingArea(int maxCapasity) {
        // TODO Implement required functionality
        this.maxCapasity = maxCapasity;

        // Init thread lists
        queue = new ArrayList<Customer>();
        waitresses = new ArrayList<Thread>();
    }

    public synchronized void enter(Customer customer){
        while (isFull()){
            try{
                SushiBar.write(Thread.currentThread().getName() + ": WaitingArea full, Door is now waiting");
                wait();
                SushiBar.write(Thread.currentThread().getName() + ": Door got notified");
            } catch (InterruptedException ex) {}
        }

        if (SushiBar.isOpen){
            // Add customer to the end of the queue and add to statistics
            SushiBar.customerCounter.increment();
            this.queue.add(customer);
            SushiBar.write(Thread.currentThread().getName() + ": Customer " + customer.getCustomerID() + " is waiting.");
            SushiBar.write("Waiting area: " + Arrays.toString(queue.toArray()));
        }
        // Wake up all waitresses
        notifyAll();
    }

    public synchronized Customer next(){
        // make waitresses wait if the waitingArea is Empty
        while (isEmpty()) {
            try {
                SushiBar.write(Thread.currentThread().getName() + ": Waitress is waiting");
                wait();
                SushiBar.write(Thread.currentThread().getName() + ": Waitress got notified");
            } catch (InterruptedException ex) {}
        }

        Customer nextCustomer = this.queue.remove(0);
        
        // notify door that it is room in the WaitingArea || Question: why cant I use this.door.notify()
        SushiBar.write("Waiting area: " + "notify door");
        notify();

        return nextCustomer;
    }

    // Add more methods as you see fit

    public void connectDoor(Thread door){
        this.door = door;
    }

    public void connectWaitress(Thread w){
        waitresses.add(w);
    }

    public ArrayList<Thread> getWaitresses(){
        return waitresses;
    }

    public boolean isFull(){
        assert this.queue.size() > this.maxCapasity : "WaitingArea Overflow";
        return this.queue.size() == this.maxCapasity;
    }

    public boolean isEmpty() {
        assert this.queue.size() < 0 : "WaitingArea Underflow";
        return this.queue.size() == 0;
    }

}
