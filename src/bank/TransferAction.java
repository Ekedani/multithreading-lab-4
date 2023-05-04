package bank;

import java.util.concurrent.RecursiveAction;

public class TransferAction extends RecursiveAction {
    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;
    private final int reps;

    public TransferAction(Bank bank, int fromAccount, int maxAmount, int reps) {
        this.bank = bank;
        this.fromAccount = fromAccount;
        this.maxAmount = maxAmount;
        this.reps = reps;
    }

    @Override
    protected void compute() {
        for (int i = 0; i < reps; i++) {
            int toAccount = (int) (bank.getSize() * Math.random());
            int amount = (int) (maxAmount * Math.random() / reps);
            bank.transfer(fromAccount, toAccount, amount);
        }
    }
}
