package bank;

import java.util.Arrays;

public class CelledBank implements Bank {
    private final Cell[] accounts;

    public CelledBank(int size, int initialBalance) {
        accounts = new Cell[size];
        Arrays.fill(accounts, new Cell(initialBalance));
    }

    public void transfer(int from, int to, int amount) {
        synchronized (accounts[from]) {
            while (accounts[from].value < amount) {
                try {
                    accounts[from].wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            accounts[from].value -= amount;
        }
        synchronized (accounts[to]) {
            accounts[to].value += amount;
            accounts[to].notifyAll();
        }
    }

    public void printSystemSum() {
        double sum = 0;
        for (var account : accounts) {
            sum += account.value;
        }
        System.out.println("System sum: " + sum);
    }

    public int getSize() {
        return accounts.length;
    }
}
