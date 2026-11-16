import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class InvalidAmountException extends Exception {
    InvalidAmountException(String message) { super(message); }
}

class InsufficientBalanceException extends Exception {
    InsufficientBalanceException(String message) { super(message); }
}

public class AtmDemo {
    static void withdraw(int amount, int balance) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        if (amount > balance) throw new InsufficientBalanceException("Not enough balance");
        System.out.println("Please collect your cash: Rs." + amount);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter account number: ");
            String account = sc.nextLine();
            System.out.print("Enter withdrawal amount: ");
            int amount = Integer.parseInt(sc.nextLine());
            if (!new File("card.txt").exists()) throw new FileNotFoundException("Card reader file is missing");
            withdraw(amount, 5000);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: enter a number");
        } catch (InvalidAmountException | InsufficientBalanceException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Device error: " + e.getMessage());
        } finally {
            System.out.println("ATM session closed");
            sc.close();
        }
    }
}
