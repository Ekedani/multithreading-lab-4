package bank;

import java.util.Arrays;

class OriginalBank implements Bank {
    private final int[] accounts;

    public OriginalBank(int size, int initialBalance) {
        accounts = new int[size];
        Arrays.fill(accounts, initialBalance);
    }

    public synchronized void transfer(int from, int to, int amount) {
        while (accounts[from] < amount) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        accounts[from] -= amount;
        accounts[to] += amount;
        notifyAll();
    }

    public void printSystemSum() {
        double sum = 0;
        for (int account : accounts) {
            sum += account;
        }
        System.out.println("System sum: " + sum);
    }

    public int getSize() {
        return accounts.length;
    }
}

