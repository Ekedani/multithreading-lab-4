package bank;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinTask;

public class Main {
    public static final int BANK_SIZE = 16;
    public static final int REPS = 1000000;
    public static final int INITIAL_BALANCE = BANK_SIZE * REPS;
    public static final boolean TESTING_FORK_JOIN = false;

    public static void main(String[] args) {
        Bank bank = new Bank(BANK_SIZE, INITIAL_BALANCE);
        if (TESTING_FORK_JOIN) {
            final long forkJoinExecutionTime = measureForkJoinApproachTime(bank);
            System.out.println("ForkJoin: " + forkJoinExecutionTime + " ns");
        } else {
            final long threadedExecutionTime = measureThreadedApproachTime(bank);
            System.out.println("Threaded: " + threadedExecutionTime + " ns");
        }
        bank.printSystemSum();
    }

    public static long measureThreadedApproachTime(Bank bank) {
        ArrayList<TransferThread> transferThreads = new ArrayList<>();
        for (int i = 0; i < BANK_SIZE; i++) {
            transferThreads.add(new TransferThread(bank, i, INITIAL_BALANCE, REPS));
        }
        try {
            long start = System.nanoTime();
            for (var thread : transferThreads) {
                thread.start();
            }
            for (var thread : transferThreads) {
                thread.join();
            }
            long finish = System.nanoTime();
            return finish - start;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static long measureForkJoinApproachTime(Bank bank) {
        ArrayList<TransferAction> transferActions = new ArrayList<>();
        for (int i = 0; i < BANK_SIZE; i++) {
            transferActions.add(new TransferAction(bank, i, INITIAL_BALANCE, REPS));
        }
        try {
            long start = System.nanoTime();
            ForkJoinTask.invokeAll(transferActions);
            long finish = System.nanoTime();
            return finish - start;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
