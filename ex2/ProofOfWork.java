/*
 * Concurrency Exercise 2 Problem 1
 * */

package ex2;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ProofOfWork {
    private int currentNonce;
    private BigInteger winnerNonce;
    private MessageDigest digest;
    private BigInteger loopValue = BigInteger.valueOf(-1);

    public void proveWork(String block, BigInteger target) throws NoSuchAlgorithmException {
        this.currentNonce = 0;
        this.winnerNonce = BigInteger.valueOf(-1);
        while (winnerNonce.compareTo(loopValue) == 0) {
            if(checkHash(block, currentNonce, target)) break;
            currentNonce++;
        }
        System.out.printf("New block! Computations: %d. Winner nonce: %s %n", currentNonce, currentNonce);
    }

    private boolean checkHash(String block, int nonce, BigInteger target) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(digest.digest(block.concat(Integer.toString(nonce)).getBytes(StandardCharsets.UTF_8)));
        BigInteger value = new BigInteger(1, hash);
        return value.compareTo(target) == -1;
    }
}
