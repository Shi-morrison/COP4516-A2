import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class minotaur_birthday {

    private static final int NUM_GUESTS = 10;
    private static final AtomicBoolean cupcakeAvailable = new AtomicBoolean(true);
    private static final AtomicInteger cupcakesEaten = new AtomicInteger(0);
    private static final boolean[] guestsWhoAteCupcake = new boolean[NUM_GUESTS];
    private static final Lock mazeLock = new ReentrantLock(); // Reintroduced lock

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_GUESTS);
        for (int i = 0; i < NUM_GUESTS; i++) {
            executor.execute(new Guest(i));
        }
        executor.shutdown();

    }

    static class Guest implements Runnable {
        private final int guestID;

        Guest(int guestID) {
            this.guestID = guestID;
        }

        @Override
        public void run() {
            while (cupcakesEaten.get() < NUM_GUESTS) {
                enterMazeAndAttemptToEatCupcake();
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void enterMazeAndAttemptToEatCupcake() {
            mazeLock.lock();
            try {
                if (!guestsWhoAteCupcake[guestID] && cupcakeAvailable.get()) {
                    cupcakeAvailable.set(false);
                    guestsWhoAteCupcake[guestID] = true;
                    System.out.println("Guest " + guestID + " ate the cupcake.");
                }
                if (guestID == 0 && !cupcakeAvailable.get()) {
                    cupcakesEaten.incrementAndGet();
                    cupcakeAvailable.set(true);
                    System.out.println("Guest 0 replaced the cupcake.");
                }
            } finally {
                mazeLock.unlock();
            }
        }
    }
}
