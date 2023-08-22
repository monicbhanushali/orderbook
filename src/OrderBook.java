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
            matchAndExecuteMarketOrder(order, sellSide, OrderAction.BID);
        } else {
            sellSide.addOrder(order);
            matchAndExecuteMarketOrder(order, buySide, OrderAction.ASK);
        }
    }

    private void processLimitOrder(Order order) {
        OrderAction side = order.getSide();

        if (side == OrderAction.BID) {
            buySide.addOrder(order);
            matchAndExecuteLimitOrder(order, sellSide, OrderAction.BID);
        } else {
            sellSide.addOrder(order);
            matchAndExecuteLimitOrder(order, buySide, OrderAction.ASK);
        }
    }

    private void matchAndExecuteMarketOrder(Order order, OrderTree orderTree, OrderAction side) {
        int quantity = order.getQuantity();
        String orderId = order.getOrderId();
        if(side == OrderAction.BID) {
            while(quantity > 0) {
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
                            o.updateQuantity(o.getQuantity() - tradedQuantity);
                        }
                    } else {
                        isTraded = true;
                        tradedQuantity = o.getQuantity();
                        quantity -= o.getQuantity();
                        orderTree.deleteOrder(o.getOrderId());
                    }

                    if (isTraded) {
                        System.out.println("**************** Market Buy Trade Executed ****************");
                        System.out.println("OrderID: " + order.getOrderId() + " bought " + tradedQuantity);
                        System.out.println("OrderID: " + o.getOrderId() + " sold " + tradedQuantity);
                        System.out.println("**************** ENDS ****************");
                    }
                }
            }
        } else {
            while(quantity > 0) {
                List<Order> maxOrderList = orderTree.getMaxPriceList();
                if(maxOrderList == null || maxOrderList.isEmpty()) break;
                for(Order o : maxOrderList) {
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
                        orderTree.deleteOrder(order.getOrderId());
                        isTraded = true;
                        tradedQuantity = quantity;
                    } else {
                        quantity -= o.getQuantity();
                        orderTree.deleteOrder(o.getOrderId());
                        isTraded = true;
                        tradedQuantity = o.getQuantity();
                    }
                    if (isTraded) {
                        System.out.println("**************** Market Sell Trade Executed ****************");
                        System.out.println("OrderID: " + order.getOrderId() + " bought " + tradedQuantity);
                        System.out.println("OrderID: " + o.getOrderId() + " sold " + tradedQuantity);
                        System.out.println("**************** ENDS ****************");
                    }
                }
            }
        }
        if (quantity > 0 && quantity != order.getQuantity()) {
            order.updateQuantity(quantity);
        }
    }

    private void matchAndExecuteLimitOrder(Order order, OrderTree orderTree, OrderAction side) {
        int quantity = order.getQuantity();
        double price = order.getPrice();
        String orderId = order.getOrderId();
        if(side == OrderAction.BID) {
            while(quantity > 0 && orderTree.isNotEmpty() && price >= orderTree.getLowestPrice()) {
                List<Order> minOrderList = orderTree.getMinPriceList();
                if(minOrderList == null || minOrderList.isEmpty()) break;

                for(Order o : minOrderList) {
                    boolean isTraded = false;
                    int tradedQuantity = 0;
                    // on exact match reduce quantity to zero and delete the order from list
                    if(quantity == o.getQuantity()) {
                        quantity = 0;
                        orderTree.deleteOrder(o.getOrderId());
                        orderTree.deleteOrder(orderId);
                        isTraded = true;
                        tradedQuantity = quantity;
                    } else if (quantity < o.getQuantity()) {
                        o.updateQuantity(o.getQuantity() - quantity);
                        isTraded = true;
                        tradedQuantity = quantity;
                        quantity = 0;
                        orderTree.deleteOrder(orderId);
                    } else {
                        quantity -= o.getQuantity();
                        orderTree.deleteOrder(o.getOrderId());
                        isTraded = true;
                        tradedQuantity = o.getQuantity();
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
                    if(quantity == o.getQuantity()) {
                        quantity = 0;
                        orderTree.deleteOrder(o.getOrderId());
                        orderTree.deleteOrder(order.getOrderId());
                        isTraded = true;
                        tradedQuantity = quantity;
                    } else if (quantity < o.getQuantity()) {
                        o.updateQuantity(o.getQuantity() - quantity);
                        orderTree.deleteOrder(order.getOrderId());
                        isTraded = true;
                        tradedQuantity = quantity;
                    } else {
                        quantity -= o.getQuantity();
                        orderTree.deleteOrder(o.getOrderId());
                        isTraded = true;
                        tradedQuantity = o.getQuantity();
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
        if (quantity > 0 && quantity != order.getQuantity()) {
            order.updateQuantity(quantity);
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
