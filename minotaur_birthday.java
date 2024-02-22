import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class minotaur_birthday {

    private static final int NUM_GUESTS = 10;
    private static final AtomicBoolean isCupcakeAvailable = new AtomicBoolean(true);
    private static final AtomicInteger currentCount = new AtomicInteger(0);
    private static final Lock mazeLock = new ReentrantLock();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_GUESTS + 1);
        executor.execute(new CupcakeChecker());

        for (int i = 0; i < NUM_GUESTS; i++) {
            executor.execute(new Guest(i));
        }
        executor.shutdown();
    }

    static class CupcakeChecker implements Runnable {
        @Override
        public void run() {
            while (currentCount.get() < NUM_GUESTS) {
                mazeLock.lock();
                try {
                    if (!isCupcakeAvailable.get()) {
                        currentCount.incrementAndGet();
                        isCupcakeAvailable.set(true);
                    }
                } finally {
                    mazeLock.unlock();
                }
            }
        }
    }

    static class Guest implements Runnable {
        private final int guestID;

        public Guest(int guestID) {
            this.guestID = guestID;
        }

        @Override
        public void run() {
            while (currentCount.get() < NUM_GUESTS) {
                mazeLock.lock();
                try {
                    if (isCupcakeAvailable.get()) {
                        isCupcakeAvailable.set(false);
                        System.out.println("Guest " + guestID + " ate the cupcake.");

                        if (guestID == 0) {
                            currentCount.incrementAndGet();
                            isCupcakeAvailable.set(true);
                            System.out.println("Guest 0 replaced the cupcake.");
                        }
                    }
                } finally {
                    mazeLock.unlock();
                }
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
