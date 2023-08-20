import java.util.*;

public class OrderTree {
    TreeMap<Double, LinkedList<Order>> orderTree = new TreeMap<>();
    HashMap<String, Order> orderMap = new HashMap<>();
    int volume = 0;

    public void addOrder(Order order) {
        String orderId = order.getOrderId();
        double price = order.getPrice();

        // first add the order in map and then insert it into the tree
        orderMap.put(orderId, order);
        if(orderTree.containsKey(price)) {
            orderTree.get(price).add(order);
        } else {
            LinkedList<Order> priceList = new LinkedList<>();
            priceList.add(order);
            orderTree.put(price, priceList);
        }
        volume += order.getQuantity();
    }

    public void deleteOrder(String orderId) {
        // If orderId is present and the price level is present in
        // the tree then delete order else do nothing
        if(orderMap.containsKey(orderId)) {
            Order order = orderMap.get(orderId);
            double price = order.getPrice();
            if(orderTree.containsKey(price)) {
                LinkedList<Order> priceList = orderTree.get(price);
                priceList.remove(order);
                if(priceList.isEmpty()) {
                    orderTree.remove(price);
                }
                volume -= order.getQuantity();
            }
            orderMap.remove(orderId);
        }
    }

    /**
     * Returns an unmodifiable copy of best ask/sell price of orders
     * @return NavigableMap
     */
    public List<Order> getMinPriceList() {
        if(orderTree.isEmpty()) return Collections.unmodifiableList(new LinkedList<>());
        return Collections.unmodifiableList(orderTree.firstEntry().getValue());
    }

    public List<Order> getMaxPriceList() {
        if(orderTree.isEmpty()) return Collections.unmodifiableList(new LinkedList<>());
        return Collections.unmodifiableList(orderTree.lastEntry().getValue());
    }
}
