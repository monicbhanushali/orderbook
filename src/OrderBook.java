import java.util.List;

public class OrderBook {

    private OrderTree buySide;
    private OrderTree sellSide;

    public OrderBook() {
        this.buySide = new OrderTree();
        this.sellSide = new OrderTree();
    }

    public void processOrder(Order order) {
        double price = order.getPrice();
        processMarketOrder(order);
    }

    private void processMarketOrder(Order order) {
        int quantity = order.getQuantity();
        String side = order.getSide();

        if(quantity <= 0) throw new ArithmeticException("Quantity of Order cannot be less than 1");

        if (side == "buy") {
            buySide.addOrder(order);
            matchAndExecuteOrder(order, sellSide, "buy");
        } else {
            sellSide.addOrder(order);
            matchAndExecuteOrder(order, buySide, "sell");
        }
    }

    private void matchAndExecuteOrder(Order order, OrderTree orderTree, String side) {
        int quantity = order.getQuantity();
        if(side == "buy") {
            while(quantity > 0) {
                List<Order> minOrderList = orderTree.getMinPriceList();
                if(minOrderList == null || minOrderList.isEmpty()) break;

                for(Order o : minOrderList) {
                    System.out.println(o);

                    boolean isTraded = false;
                    int tradedQuantity = 0;
                    // on exact match reduce quantity to zero and delete the order from list
                    if(quantity == o.getQuantity()) {
                        quantity = 0;
                        orderTree.deleteOrder(o.getOrderId());
                        orderTree.deleteOrder(order.getOrderId());
                        isTraded = true;
                        tradedQuantity = quantity;
                    } else if (quantity < o.getQuantity()) {
                        o.updateQuantity(o.getQuantity() - quantity);
                        isTraded = true;
                        tradedQuantity = quantity;
                        quantity = 0;
                        orderTree.deleteOrder(order.getOrderId());
                    } else {
                        quantity -= o.getQuantity();
                        orderTree.deleteOrder(o.getOrderId());
                        isTraded = true;
                        tradedQuantity = o.getQuantity();
                    }

                    System.out.println("**************** Trade Executed ****************");
                    System.out.println("OrderID: " + order.getOrderId() + " bought " + tradedQuantity);
                    System.out.println("OrderID: " + o.getOrderId() + " sold " + tradedQuantity);
                    System.out.println("**************** ENDS ****************");
                }
            }
        } else {
            while(quantity > 0) {
                List<Order> maxOrderList = orderTree.getMaxPriceList();
                if(maxOrderList == null || maxOrderList.isEmpty()) break;
                for(Order o : maxOrderList) {
                    // on exact match reduce quantity to zero and delete the order from list
                    if(quantity == o.getQuantity()) {
                        quantity = 0;
                        orderTree.deleteOrder(o.getOrderId());
                    } else if (quantity < o.getQuantity()) {
                        o.updateQuantity(o.getQuantity() - quantity);
                    } else {
                        quantity -= o.getQuantity();
                        orderTree.deleteOrder(o.getOrderId());
                    }
                }
            }
        }
        if (quantity == 0) {
            orderTree.deleteOrder(order.getOrderId());
        } else {
            order.updateQuantity(quantity);
        }
    }
}
