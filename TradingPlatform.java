import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Represents a stock with a symbol, name, and current price.
 */
class Stock {
    private String symbol;
    private String name;
    private double currentPrice;

    /**
     * Constructor for the Stock class.
     * @param symbol The unique symbol of the stock (e.g., "AAPL").
     * @param name The full name of the company (e.g., "Apple Inc.").
     * @param initialPrice The initial price of the stock.
     */
    public Stock(String symbol, String name, double initialPrice) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = initialPrice;
    }

    /**
     * Gets the stock symbol.
     * @return The stock symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the stock name.
     * @return The stock name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current price of the stock.
     * @return The current price.
     */
    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Sets a new current price for the stock.
     * @param currentPrice The new current price.
     */
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * Returns a string representation of the stock.
     * @return A formatted string with stock information.
     */
    @Override
    public String toString() {
        return String.format("%-8s %-20s $%.2f", symbol, name, currentPrice);
    }
}

/**
 * Represents a financial transaction (buy or sell) for a stock.
 */
class Transaction {
    private String type; // "BUY" or "SELL"
    private Stock stock;
    private int quantity;
    private double pricePerShare;
    private LocalDateTime timestamp;

    /**
     * Constructor for the Transaction class.
     * @param type The type of transaction ("BUY" or "SELL").
     * @param stock The stock involved in the transaction.
     * @param quantity The number of shares.
     * @param pricePerShare The price per share at the time of transaction.
     */
    public Transaction(String type, Stock stock, int quantity, double pricePerShare) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Gets the type of transaction.
     * @return The transaction type.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the stock involved in the transaction.
     * @return The Stock object.
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * Gets the quantity of shares.
     * @return The quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the price per share at the time of transaction.
     * @return The price per share.
     */
    public double getPricePerShare() {
        return pricePerShare;
    }

    /**
     * Gets the timestamp of the transaction.
     * @return The LocalDateTime object.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a string representation of the transaction.
     * @return A formatted string with transaction details.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("%s %s %d shares of %s at $%.2f (Total: $%.2f) on %s",
                type, stock.getSymbol(), quantity, stock.getName(), pricePerShare,
                (quantity * pricePerShare), timestamp.format(formatter));
    }
}

/**
 * Represents a user of the trading platform with a balance, portfolio, and transaction history.
 */
class User {
    private String username;
    private String password; // Simplified for this simulation
    private double balance;
    // Portfolio: Map of Stock Symbol to Quantity
    private Map<String, Integer> portfolio;
    private List<Transaction> transactionHistory;

    /**
     * Constructor for the User class.
     * @param username The user's chosen username.
     * @param password The user's chosen password.
     * @param initialBalance The initial balance for the user.
     */
    public User(String username, String password, double initialBalance) {
        this.username = username;
        this.password = password;
        this.balance = initialBalance;
        this.portfolio = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * Gets the user's username.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Checks if the provided password matches the user's password.
     * @param password The password to check.
     * @return True if passwords match, false otherwise.
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Gets the user's current balance.
     * @return The balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Deposits money into the user's account.
     * @param amount The amount to deposit.
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Deposited $%.2f. New balance: $%.2f%n", amount, balance);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    /**
     * Withdraws money from the user's account.
     * @param amount The amount to withdraw.
     * @return True if withdrawal is successful, false otherwise.
     */
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.printf("Withdrew $%.2f. New balance: $%.2f%n", amount, balance);
            return true;
        } else if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
        } else {
            System.out.println("Insufficient balance.");
        }
        return false;
    }

    /**
     * Adds shares of a stock to the user's portfolio.
     * @param stockSymbol The symbol of the stock.
     * @param quantity The number of shares to add.
     */
    public void addShares(String stockSymbol, int quantity) {
        portfolio.put(stockSymbol, portfolio.getOrDefault(stockSymbol, 0) + quantity);
    }

    /**
     * Removes shares of a stock from the user's portfolio.
     * @param stockSymbol The symbol of the stock.
     * @param quantity The number of shares to remove.
     * @return True if shares were successfully removed, false otherwise.
     */
    public boolean removeShares(String stockSymbol, int quantity) {
        if (portfolio.containsKey(stockSymbol)) {
            int currentQuantity = portfolio.get(stockSymbol);
            if (currentQuantity >= quantity) {
                portfolio.put(stockSymbol, currentQuantity - quantity);
                if (portfolio.get(stockSymbol) == 0) {
                    portfolio.remove(stockSymbol); // Remove entry if quantity becomes 0
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the quantity of a specific stock held by the user.
     * @param stockSymbol The symbol of the stock.
     * @return The quantity of shares, or 0 if not held.
     */
    public int getStockQuantity(String stockSymbol) {
        return portfolio.getOrDefault(stockSymbol, 0);
    }

    /**
     * Adds a transaction to the user's history.
     * @param transaction The Transaction object to add.
     */
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    /**
     * Gets the user's transaction history.
     * @return A list of Transaction objects.
     */
    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Displays the user's current portfolio and its performance.
     * @param market The StockMarket instance to get current prices.
     */
    public void displayPortfolio(StockMarket market) {
        System.out.println("\n--- " + username + "'s Portfolio ---");
        System.out.printf("Current Balance: $%.2f%n", balance);

        if (portfolio.isEmpty()) {
            System.out.println("You currently hold no stocks.");
            return;
        }

        System.out.println("\nYour Holdings:");
        System.out.printf("%-8s %-10s %-15s %-15s%n", "Symbol", "Quantity", "Current Price", "Total Value");
        System.out.println("--------------------------------------------------");

        double totalPortfolioValue = 0;
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            Stock stockInMarket = market.getStock(symbol);

            if (stockInMarket != null) {
                double currentPrice = stockInMarket.getCurrentPrice();
                double value = quantity * currentPrice;
                totalPortfolioValue += value;
                System.out.printf("%-8s %-10d $%-14.2f $%-14.2f%n", symbol, quantity, currentPrice, value);
            } else {
                System.out.printf("%-8s %-10d (Stock not found in market)%n", symbol, quantity);
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Total Portfolio Value: $%.2f%n", totalPortfolioValue);
        System.out.println("--------------------------------------------------");
    }

    /**
     * Displays the user's transaction history.
     */
    public void displayTransactionHistory() {
        System.out.println("\n--- " + username + "'s Transaction History ---");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction t : transactionHistory) {
            System.out.println(t);
        }
        System.out.println("------------------------------------------");
    }
}

/**
 * Simulates a stock market, holding a list of available stocks and updating their prices.
 */
class StockMarket {
    private List<Stock> availableStocks;
    private Random random;

    /**
     * Constructor for the StockMarket class. Initializes with some sample stocks.
     */
    public StockMarket() {
        this.availableStocks = new ArrayList<>();
        this.random = new Random();
        initializeStocks();
    }

    /**
     * Initializes a predefined list of stocks.
     */
    private void initializeStocks() {
        availableStocks.add(new Stock("AAPL", "Apple Inc.", 170.00));
        availableStocks.add(new Stock("GOOG", "Alphabet Inc.", 1500.00));
        availableStocks.add(new Stock("MSFT", "Microsoft Corp.", 280.00));
        availableStocks.add(new Stock("AMZN", "Amazon.com Inc.", 120.00));
        availableStocks.add(new Stock("TSLA", "Tesla Inc.", 250.00));
        availableStocks.add(new Stock("NVDA", "NVIDIA Corp.", 450.00));
    }

    /**
     * Simulates price fluctuations for all available stocks.
     * Prices change by a small random percentage (up to +/- 2%).
     */
    public void updateStockPrices() {
        for (Stock stock : availableStocks) {
            double change = (random.nextDouble() * 0.04) - 0.02; // Random change between -2% and +2%
            double newPrice = stock.getCurrentPrice() * (1 + change);
            // Ensure price doesn't go below a reasonable minimum
            if (newPrice < 1.0) {
                newPrice = 1.0;
            }
            stock.setCurrentPrice(newPrice);
        }
    }

    /**
     * Gets a list of all available stocks.
     * @return A list of Stock objects.
     */
    public List<Stock> getAvailableStocks() {
        return availableStocks;
    }

    /**
     * Finds a stock by its symbol.
     * @param symbol The symbol of the stock to find.
     * @return The Stock object if found, otherwise null.
     */
    public Stock getStock(String symbol) {
        for (Stock stock : availableStocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                return stock;
            }
        }
        return null;
    }

    /**
     * Displays the current market data for all available stocks.
     */
    public void displayMarketData() {
        System.out.println("\n--- Current Market Data ---");
        System.out.printf("%-8s %-20s %-10s%n", "Symbol", "Company Name", "Price");
        System.out.println("------------------------------------------");
        for (Stock stock : availableStocks) {
            System.out.println(stock);
        }
        System.out.println("------------------------------------------");
    }
}

/**
 * The main class for the Stock Trading Platform application.
 * Manages users, interacts with the stock market, and handles trading operations.
 */
public class TradingPlatform {
    private Map<String, User> users; // Stores users by username
    private User currentUser;
    private StockMarket stockMarket;
    private Scanner scanner;

    /**
     * Constructor for the TradingPlatform class.
     */
    public TradingPlatform() {
        this.users = new HashMap<>();
        this.stockMarket = new StockMarket();
        this.scanner = new Scanner(System.in);
        // Add a default user for quick testing
        users.put("testuser", new User("testuser", "password", 10000.00));
        System.out.println("Default user 'testuser' with password 'password' created with $10,000 balance.");
    }

    /**
     * Handles user registration.
     */
    public void registerUser() {
        System.out.println("\n--- Register New Account ---");
        System.out.print("Enter desired username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username.toLowerCase())) {
            System.out.println("Username already exists. Please choose a different one.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter initial balance: ");
        double initialBalance;
        try {
            initialBalance = scanner.nextDouble();
            if (initialBalance < 0) {
                System.out.println("Initial balance cannot be negative. Setting to 0.");
                initialBalance = 0;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Initial balance set to 0.");
            initialBalance = 0;
        } finally {
            scanner.nextLine(); // Consume newline
        }

        User newUser = new User(username, password, initialBalance);
        users.put(username.toLowerCase(), newUser);
        System.out.println("Account created successfully for " + username + "!");
    }

    /**
     * Handles user login.
     * @return True if login is successful, false otherwise.
     */
    public boolean loginUser() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username.toLowerCase());
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            System.out.println("Welcome, " + currentUser.getUsername() + "!");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    /**
     * Allows the current user to buy stocks.
     */
    public void buyStock() {
        if (currentUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        stockMarket.displayMarketData(); // Show current market data
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock stockToBuy = stockMarket.getStock(symbol);

        if (stockToBuy == null) {
            System.out.println("Stock not found. Please enter a valid symbol.");
            return;
        }

        System.out.print("Enter quantity to buy: ");
        int quantity;
        try {
            quantity = scanner.nextInt();
            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for quantity.");
            return;
        } finally {
            scanner.nextLine(); // Consume newline
        }

        double totalCost = quantity * stockToBuy.getCurrentPrice();
        if (currentUser.getBalance() >= totalCost) {
            currentUser.withdraw(totalCost);
            currentUser.addShares(stockToBuy.getSymbol(), quantity);
            currentUser.addTransaction(new Transaction("BUY", stockToBuy, quantity, stockToBuy.getCurrentPrice()));
            System.out.printf("Successfully bought %d shares of %s for $%.2f.%n", quantity, stockToBuy.getName(), totalCost);
        } else {
            System.out.println("Insufficient balance to complete this purchase.");
        }
    }

    /**
     * Allows the current user to sell stocks.
     */
    public void sellStock() {
        if (currentUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        currentUser.displayPortfolio(stockMarket); // Show current holdings
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock stockToSell = stockMarket.getStock(symbol);

        if (stockToSell == null) {
            System.out.println("Stock not found in market. Cannot sell.");
            return;
        }

        int ownedQuantity = currentUser.getStockQuantity(symbol);
        if (ownedQuantity == 0) {
            System.out.println("You do not own any shares of " + symbol + ".");
            return;
        }

        System.out.print("Enter quantity to sell (you own " + ownedQuantity + "): ");
        int quantity;
        try {
            quantity = scanner.nextInt();
            if (quantity <= 0 || quantity > ownedQuantity) {
                System.out.println("Invalid quantity. You can only sell between 1 and " + ownedQuantity + " shares.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for quantity.");
            return;
        } finally {
            scanner.nextLine(); // Consume newline
        }

        double totalRevenue = quantity * stockToSell.getCurrentPrice();
        if (currentUser.removeShares(stockToSell.getSymbol(), quantity)) {
            currentUser.deposit(totalRevenue); // Add revenue to balance
            currentUser.addTransaction(new Transaction("SELL", stockToSell, quantity, stockToSell.getCurrentPrice()));
            System.out.printf("Successfully sold %d shares of %s for $%.2f.%n", quantity, stockToSell.getName(), totalRevenue);
        } else {
            System.out.println("Failed to sell shares. Please check your holdings.");
        }
    }

    /**
     * Displays the main trading menu.
     */
    public void displayTradingMenu() {
        System.out.println("\n--- Trading Menu for " + currentUser.getUsername() + " ---");
        System.out.println("1. View Market Data");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View My Portfolio");
        System.out.println("5. View Transaction History");
        System.out.println("6. Logout");
        System.out.print("Enter your choice: ");
    }

    /**
     * Runs the main application loop for the trading platform.
     */
    public void run() {
        System.out.println("Welcome to the Stock Trading Platform Simulation!");
        int choice;

        while (true) {
            if (currentUser == null) {
                System.out.println("\n--- Main Menu ---");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            loginUser();
                            break;
                        case 2:
                            registerUser();
                            break;
                        case 3:
                            System.out.println("Thank you for using the platform. Goodbye!");
                            scanner.close();
                            return; // Exit the program
                        default:
                            System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Consume invalid input
                }
            } else {
                // Update stock prices periodically (e.g., every time the menu is shown)
                stockMarket.updateStockPrices();
                displayTradingMenu();
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            stockMarket.displayMarketData();
                            break;
                        case 2:
                            buyStock();
                            break;
                        case 3:
                            sellStock();
                            break;
                        case 4:
                            currentUser.displayPortfolio(stockMarket);
                            break;
                        case 5:
                            currentUser.displayTransactionHistory();
                            break;
                        case 6:
                            System.out.println("Logging out " + currentUser.getUsername() + ".");
                            currentUser = null; // Logout
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Consume invalid input
                }
            }
        }
    }

    /**
     * Main method to start the Stock Trading Platform application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        TradingPlatform platform = new TradingPlatform();
        platform.run();
    }
}
