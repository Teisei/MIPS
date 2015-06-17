package edu.ecnu.teisei.mips.test;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Teisei on 2015/5/25.
 */
public class testQueue {

    public static void main(String args[]) {
        int a[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
        queue.add(a[0]);
        queue.add(a[1]);
        queue.add(a[2]);
        queue.add(a[3]);
        int t = queue.peek();
        int tt = queue.poll();
        int ttt = queue.poll();
        int ss = 1;
    }
}
