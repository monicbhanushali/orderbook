import java.util.Map;
import java.util.TreeMap;

/**
 * A TreeMap implementation to store OrderList sorted by price key
 * One TreeMap for each side, i.e, Bid & Ask
 * Bid == Buy
 * Ask == Sell
 * TreeMap is internally implemented as a red-black tree
 * https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html
 */

public class OrderTree {

    /**
     * Map to store price, orderList in key value pair
     */
    private Map<Float, OrderList> orderTree;
    /**
     * Map to find orders by orderID in O(1)
     */
    private Map<String, Order> orderMap;

    public OrderTree() {
        this.orderTree = new TreeMap<>();
    }

    public void addOrder(Order order) {
        // if the order id is not present in the map only then add the order
        if(!orderMap.containsKey(order.getOrderId())) {
            orderTree.get(order.getPrice()).addOrder(order);
        }
    }

    public boolean deleteOrder(String orderId) {
        if(!orderMap.containsKey(orderId)) {
            return false;
            // TODO: throw order not present exception
        }

        Order order = orderMap.get(orderId);
        orderTree.get(order.getPrice()).deleteOrder(order);
        orderMap.remove(orderId);

        return true;
    }
}
