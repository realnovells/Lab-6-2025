package functions.threads;

public class Semaphore {
    private boolean available = false;

    public synchronized void acquireWrite() throws InterruptedException {
        while (available) {
            wait();
        }
    }

    public synchronized void releaseWrite() {
        available = true;
        notifyAll();
    }

    public synchronized void acquireRead() throws InterruptedException {
        while (!available) {
            wait();
        }
    }

    public synchronized void releaseRead() {
        available = false;
        notifyAll();
    }
}
