package bank;

class TransferThread extends Thread {
    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;
    private final int reps;

    public TransferThread(Bank bank, int from, int max, int reps) {
        this.bank = bank;
        this.fromAccount = from;
        this.maxAmount = max;
        this.reps = reps;
    }

    @Override
    public void run() {
        for (int i = 0; i < reps; i++) {
            int toAccount = (int) (bank.getSize() * Math.random());
            int amount = (int) (maxAmount * Math.random() / reps);
            bank.transfer(fromAccount, toAccount, amount);
        }
    }
}
