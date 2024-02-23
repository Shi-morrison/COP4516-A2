# Problem 1
## minotaur_birthday
The program uses Java's concurrency utilities to model each guest as a separate thread. A lock (ReentrantLock) ensures that only one guest can access the cupcake at any given time, simulating the rule that guests must enter the labyrinth one by one. An atomic boolean (AtomicBoolean) tracks the availability of the cupcake, and an atomic integer (AtomicInteger) counts the number of guests who have successfully eaten the cupcake. The program ensures that all guests have a chance to visit the labyrinth, with a special role designated for the first guest to replace the cupcake when needed.
- Compile the Java program:
- javac minotaur_birthday.java
- Run the compiled Java program:
- java minotaur_birthday

# Problem 2
## minotaur_vase
The code implements the second strategy, where a sign (simulated by the Status enum with AVAILABLE or BUSY states) indicates the showroom's availability. Each guest (thread) checks if the showroom is AVAILABLE before entering. Upon entering, the guest sets the status to BUSY, views the vase, and sets it back to AVAILABLE upon exiting. This mechanism ensures that only one guest can view the vase at a time, preventing crowding and potential damage to the vase.
ExecutorService: Manages a pool of guest threads, simulating guests attempting to view the vase.
Lock (ReentrantLock): Ensures mutual exclusion, allowing only one guest in the showroom at a time.
Status Enum: Represents the showroom's availability sign with AVAILABLE and BUSY states.
Synchronized Set: Tracks which guests (threads) have viewed the vase to prevent multiple viewings by the same guest.
- Compile the Java program:
- javac minotaur_vase.java
- Run the compiled Java program:
- java minotaur_vase
