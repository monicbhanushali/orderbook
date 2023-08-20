// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        Order order = new Order(
                "2023-08-20",
                35,
                22.5,
                "O123456-S",
                "T12345-S",
                OrderAction.ASK,
                "Reliance",
                OrderType.MARKET
        );

        Order order1 = new Order(
                "2023-08-20",
                80,
                22.5,
                "O123456-S1",
                "T12345-S",
                OrderAction.ASK,
                "Reliance",
                OrderType.MARKET

        );

        Order order2 = new Order(
                "2023-08-20",
                99,
                23.5,
                "O123456",
                "T12345",
                OrderAction.BID,
                "Reliance",
                OrderType.MARKET
        );

        OrderBook book = new OrderBook();
        book.processOrder(order);
        book.processOrder(order1);
        book.processOrder(order2);
    }
}