package ex2;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BigInteger target = new BigInteger("5748793675521088535753956442023997614447480418173898744039477227881985");

        /* Use smth. like 'h' for longer compute time */
        String block = "new block";

        /* Exercise 2 Problem 1 - Not concurrent */
/*        ProofOfWork bl = new ProofOfWork();
        long startTime = System.nanoTime();
        bl.proveWork(block, target);
        long endTime = System.nanoTime();
        System.out.printf("Time: %s", (endTime-startTime) / 1.0e9);*/


        /* Exercise 2 Problem 2 - Concurrent */

        long startTime = System.nanoTime();

        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < 8; i++) {

            Thread newThread = new Thread(new ConcurrentProofOfWork(block, target, i));
            newThread.start();
            threads.add(newThread);
        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        long endTime = System.nanoTime();
        System.out.printf("Total time: %d seconds", (endTime - startTime) / 1.0e9);
    }
}
