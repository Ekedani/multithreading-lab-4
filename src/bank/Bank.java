package bank;

public interface Bank {
    void transfer(int from, int to, int amount);
    void printSystemSum();
    int getSize();
}