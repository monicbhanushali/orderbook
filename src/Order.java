/**
 * Order is the basic unit in stock exchange.
 * Order can be either a buy/sell order
 */

public class Order {

    private String timestamp;
    private int quantity;
    private double price;
    private String orderId;
    private String tradeId;
    private OrderAction side;
    private String stockName;
    private OrderType type;

    public Order(String timestamp, int quantity, double price, String orderId, String tradeId, OrderAction side, String stockName, OrderType type) {
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
        this.tradeId = tradeId;
        this.side = side;
        this.stockName = stockName;
        this.type = type;
    }

    public void updateQuantity(int newQuantity) {
        if(newQuantity < 1) {
            throw new ArithmeticException("Order quantity cannot be less than 1");
        }
        this.quantity = newQuantity;
    }

    public void updatePrice(double newPrice) {
        if(newPrice < 0) {
            throw new ArithmeticException("Order price cannot be less than 1");
        }
        this.price = newPrice;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTradeId() {
        return tradeId;
    }

    public OrderAction getSide() {
        return side;
    }

    public String getStockName() {
        return stockName;
    }

    public String toString() {
        return "Order ID" + this.orderId + " quantity " + this.quantity + " side " + this.side;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }
}
