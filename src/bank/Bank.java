package bank;

import java.util.Arrays;

class Bank {
    private final int[] accounts;

    public Bank(int size, int initialBalance) {
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

    public void printAccounts() {
        double sum = 0;
        for (int account : accounts) {
            System.out.print(account + " ");
            sum += account;
        }
        System.out.println("Total: " + sum);
    }

    public int getSize() {
        return accounts.length;
    }
}

