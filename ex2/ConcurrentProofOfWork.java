/*
* Concurrency Exercise 2 Problem 2
* */

package ex2;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentProofOfWork implements Runnable {
    private long currentNonce;
    private AtomicLong winnerNonce = new AtomicLong(-1);
    private long loopValue = -1;
    private long startNonce;

    public String block;
    public BigInteger target;

    public ConcurrentProofOfWork(String block, BigInteger target, long startNonce) {
        this.block = block;
        this.target = target;
        this.startNonce = startNonce;
    }

    public AtomicLong proveWork(String block, BigInteger target) throws NoSuchAlgorithmException {
        this.currentNonce = startNonce;
        while (winnerNonce.compareAndSet(loopValue, loopValue)) {
            try {
                checkHash(block, currentNonce, target);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            currentNonce+=8;
        }
        System.out.printf("New block! Computations: %d. Winner nonce: %s %n", currentNonce, winnerNonce);
        return winnerNonce;
    }

    public AtomicLong checkHash(String block, long nonce, BigInteger target) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(digest.digest(block.concat(Long.toString(nonce)).getBytes(StandardCharsets.UTF_8)));
        BigInteger value = new BigInteger(1, hash);

        if (value.compareTo(target) == -1) {
            winnerNonce.set(nonce);
            return winnerNonce;
        }
        return winnerNonce;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        try {
            proveWork(block, target);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.printf("Thread time: %d seconds %n", (endTime-startTime) / 1.0e9);
    }
}
