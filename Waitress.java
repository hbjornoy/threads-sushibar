/**
 * This class implements the consumer part of the producer/consumer problem.
 * One waitress instance corresponds to one consumer.
 */
public class Waitress implements Runnable {

    private WaitingArea waitingArea;
    private int orderWait = SushiBar.waitressWait;
    private int eatWait = SushiBar.customerWait;

    public Waitress(WaitingArea waitingArea){
        this.waitingArea = waitingArea;
    }

    @Override
    public void run(){
        SushiBar.write(Thread.currentThread().getName() + ": Waitress is CREATED");

        while (SushiBar.isOpen || !waitingArea.isEmpty()) {

            // Only use syncronized her because if you incapsulate any waiting for order etc, it will artificially slow the process.
            Customer currentCustomer;
            synchronized(waitingArea) {
                currentCustomer = waitingArea.next();
                SushiBar.write(Thread.currentThread().getName() + ": Waitress FETCHED customer " + currentCustomer);
            }

            // take order
            SushiBar.write(Thread.currentThread().getName() + ": Waitress takes ORDER from customer " + currentCustomer);
            stop(orderWait);
            currentCustomer.order();

            // Eating time || Question: does the waitress need to assist while eating?
            SushiBar.write(Thread.currentThread().getName() + ": Customer " + currentCustomer.getCustomerID() + " is EATING.");
            stop(eatWait);
            SushiBar.write(Thread.currentThread().getName() + ": Customer " + currentCustomer.getCustomerID() + " just LEFT.");
        }
        SushiBar.write(Thread.currentThread().getName() + ": Waitress is DECEASED");
    }


    private void stop(int waitTime){
        try {
            Thread.sleep(waitTime);
        } catch(InterruptedException e) {}
    }


}