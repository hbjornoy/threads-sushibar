/**
 * This class implements the Door component of the sushi bar assignment
 * The Door corresponds to the Producer in the producer/consumer problem
 */
public class Door implements Runnable {

    private WaitingArea waitingArea;
    private int doorWait = SushiBar.doorWait;

    public Door(WaitingArea waitingArea) {
        this.waitingArea = waitingArea;
    }

    /**
     * This method will run when the door thread is created (and started)
     * The method should create customers at random intervals and try to put them in the waiting area
     */
    @Override
    public void run() {
        SushiBar.write(Thread.currentThread().getName() + ": Door is CREATED");

        while(SushiBar.isOpen){
            Customer newCustomer = new Customer();
            SushiBar.write(Thread.currentThread().getName() + ": Door made a new customer " + newCustomer.getCustomerID());

            waitingArea.enter(newCustomer);
            stop(doorWait);
        }
        SushiBar.write(Thread.currentThread().getName() + ": Door is DECEASED"); 
    }   

    // Add more methods as you see fit
    private void stop(int waitTime) {
    try {
      Thread.sleep(waitTime);
    } catch (InterruptedException e) {}
  }
}
