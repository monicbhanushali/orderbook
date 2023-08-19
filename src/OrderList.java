import java.util.LinkedList;
import java.util.List;

/**
 * A doubly linked list of orders having same price. This list is useful for iterating over
 * orders when a price match is found.
 * The orders in the list are arranged by time, that is, new orders are always appended at tail
 * An update in quantity of the order wouldn't re-arrange its position in the list
 * An update in the price would however remove the order from this list and add it in other
 * order list depending on the price
 *
 * With Order List it should be possible to perform add, delete (cancel), etc
 */
public class OrderList {
    /**
     * List of orders which have same price
     * LinkedList is internally implemented as doubly-linked list
     * https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html
     */
    private List<Order> linkedOrders = new LinkedList<>();
    /**
     * Total quantity of shares at the price (volume)
     */
    private int volume;

    /**
     * Appends order to the end of the list
     * @param o incoming Order
     */
    public void addOrder(Order o) {
        linkedOrders.add(o);
        this.volume += o.getQuantity();
    }

    public void deleteOrder(Order o) {
        linkedOrders.remove(o);
        this.volume -= o.getQuantity();
    }
}
