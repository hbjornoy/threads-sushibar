/**
 * This class implements a customer, which is used for holding data and update the statistics
 *
 */
public class Customer {

    /**
     *  Creates a new Customer.
     *  Each customer should be given a unique ID
     */

    private int id;

    public Customer() {
        // TODO Implement required functionality
        this.id = SushiBar.customerCounter.get();
    }

    public synchronized void order(){
        int total = (int)(SushiBar.maxOrder*Math.random() + 1);
        int served = (int)(total*Math.random() + 1);

        SushiBar.totalOrders.add(total);
        SushiBar.servedOrders.add(served);
        SushiBar.takeawayOrders.add(total - served);
    }

    public String getCustomerID() {
        // TODO Implement required functionality
        return String.valueOf(this.id);
    }

    public String toString() {
        return String.valueOf(this.id);
    }
}
