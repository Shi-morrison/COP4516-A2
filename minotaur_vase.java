import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class minotaur_vase {

    private static final int NUM_GUESTS = 10;
    private static final Set<Long> guestsVisited = Collections.synchronizedSet(new HashSet<>());
    private static final Lock mutex = new ReentrantLock();
    private static volatile Status roomStatus = Status.AVAILABLE;

    enum Status {
        AVAILABLE,
        BUSY
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_GUESTS);
        for (int i = 0; i < NUM_GUESTS; i++) {
            executor.submit(new Guest(i));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All guests have viewed the vase.");
    }

    private static class Guest implements Runnable {
        private final int threadIndex;

        Guest(int threadIndex) {
            this.threadIndex = threadIndex;
        }

        @Override
        public void run() {
            long threadId = Thread.currentThread().getId();

            while (guestsVisited.size() < NUM_GUESTS) {
                mutex.lock();
                try {
                    if (roomStatus == Status.AVAILABLE && !guestsVisited.contains(threadId)) {
                        roomStatus = Status.BUSY;
                        System.out.println("Guest #" + threadIndex + " is admiring the vase");
                        try {
                            Thread.sleep(generateRandomNumber(10, 500));
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        roomStatus = Status.AVAILABLE;
                        guestsVisited.add(threadId);
                    }
                } finally {
                    mutex.unlock();
                }
            }
        }
    }

    private static long generateRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }
}
