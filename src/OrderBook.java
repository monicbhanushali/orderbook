import enums.OrderAction;
import enums.OrderType;

import java.util.List;

public class OrderBook {

    private OrderTree buySide;
    private OrderTree sellSide;

    public OrderBook() {
        this.buySide = new OrderTree();
        this.sellSide = new OrderTree();
    }

    public void processOrder(Order order) {
        int quantity = order.getQuantity();
        if(quantity <= 0) throw new ArithmeticException("Quantity of Order cannot be less than 1");
        if(order.getType() == OrderType.LIMIT) {
            processLimitOrder(order);
        } else {
            processMarketOrder(order);
        }
    }

    private void processMarketOrder(Order order) {
        OrderAction side = order.getSide();

        if (side == OrderAction.BID) {
            buySide.addOrder(order);
            matchAndExecuteOrder(order, sellSide, OrderAction.BID);
        } else {
            sellSide.addOrder(order);
            matchAndExecuteOrder(order, buySide, OrderAction.ASK);
        }
    }

    private void processLimitOrder(Order order) {
        OrderAction side = order.getSide();

        if (side == OrderAction.BID) {
            buySide.addOrder(order);
            matchAndExecuteOrder(order, sellSide, OrderAction.BID);
        } else {
            sellSide.addOrder(order);
            matchAndExecuteOrder(order, buySide, OrderAction.ASK);
        }
    }

    private void matchAndExecuteOrder(Order order, OrderTree orderTree, OrderAction side) {
        int quantity = order.getQuantity();
        String orderId = order.getOrderId();
        double price = order.getPrice();
        if(side == OrderAction.BID) {
            while(quantity > 0 && orderTree.isNotEmpty() && price >= orderTree.getLowestPrice()) {
                List<Order> minOrderList = orderTree.getMinPriceList();
                if(minOrderList == null || minOrderList.isEmpty()) break;

                for(Order o : minOrderList) {
                    boolean isTraded = false;
                    int tradedQuantity = 0;
                    // on exact match reduce quantity to zero and delete the order from list
                    if(quantity <= o.getQuantity()) {
                        tradedQuantity = quantity;
                        isTraded = true;
                        this.buySide.deleteOrder(orderId);
                        quantity = 0;
                        if(o.getQuantity() - tradedQuantity == 0) {
                            orderTree.deleteOrder(o.getOrderId());
                        } else {
                            this.sellSide.updateVolume(tradedQuantity);
                            o.updateQuantity(o.getQuantity() - tradedQuantity);
                        }
                    } else {
                        isTraded = true;
                        tradedQuantity = o.getQuantity();
                        this.buySide.updateVolume(tradedQuantity);
                        quantity -= o.getQuantity();
                        orderTree.deleteOrder(o.getOrderId());
                        order.updateQuantity(quantity);
                    }

                    if (isTraded) {
                        System.out.println("**************** Limit Buy Trade Executed ****************");
                        System.out.println("OrderID: " + order.getOrderId() + " bought " + tradedQuantity);
                        System.out.println("OrderID: " + o.getOrderId() + " sold " + tradedQuantity);
                        System.out.println("**************** ENDS ****************");
                    }
                }
            }
        } else {
            while(quantity > 0 && orderTree.isNotEmpty() && price <= orderTree.getHighestPrice()) {
                List<Order> maxOrderList = orderTree.getMaxPriceList();
                if(maxOrderList == null || maxOrderList.isEmpty()) break;
                for(Order o : maxOrderList) {
                    boolean isTraded = false;
                    int tradedQuantity = 0;
                    // on exact match reduce quantity to zero and delete the order from list
                    if(quantity <= o.getQuantity()) {
                        tradedQuantity = quantity;
                        isTraded = true;
                        this.sellSide.deleteOrder(orderId);
                        quantity = 0;
                        if(o.getQuantity() - tradedQuantity == 0) {
                            orderTree.deleteOrder(o.getOrderId());
                        } else {
                            this.buySide.updateVolume(tradedQuantity);
                            o.updateQuantity(o.getQuantity() - tradedQuantity);
                        }
                    } else {
                        isTraded = true;
                        tradedQuantity = o.getQuantity();
                        this.sellSide.updateVolume(tradedQuantity);
                        quantity -= o.getQuantity();
                        orderTree.deleteOrder(o.getOrderId());
                        order.updateQuantity(quantity);
                    }
                    if (isTraded) {
                        System.out.println("**************** Limit Sell Trade Executed ****************");
                        System.out.println("OrderID: " + order.getOrderId() + " bought " + tradedQuantity);
                        System.out.println("OrderID: " + o.getOrderId() + " sold " + tradedQuantity);
                        System.out.println("**************** ENDS ****************");
                    }
                }
            }
        }
    }

    /**
     * Print Order Book
     * @return string version of order book for printing
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("######################################################");
        builder.append("\n" + "Symbol: TEMP" + "\n");
        builder.append(" -------------- BUY SIDE --------------");
        builder.append(this.buySide.toString());
        builder.append("\n");
        builder.append(" -------------- SELL SIDE --------------");
        builder.append(this.sellSide.toString());
        builder.append("\n");
        builder.append("######################################################");

        return builder.toString();
    }
}
