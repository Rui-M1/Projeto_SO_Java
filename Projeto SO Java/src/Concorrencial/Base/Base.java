package Concorrencial.Base;

import Original.AJE;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class Base extends Thread {

    static Storage storage;
    int index;
    String fileName;
    int pathSize;
    int time;
    double chanceOfMutation;
    int[] bestPath;
    int bestPathSum;

    public static Semaphore mutex = new Semaphore(1);

    public Base(int index, String fileName,
                int pathSize, int time, double chanceOfMutation, int numThreads)
    {
        this.index = index;
        this.fileName = fileName;
        this.pathSize = pathSize;
        this.time = time;
        this.chanceOfMutation = chanceOfMutation;
        storage = new Storage(numThreads);
    }

    @Override
    public void run()
    {
        try
        {
            mutex.acquire();
            AJE aje = new AJE();
            aje.start(fileName, pathSize, time, chanceOfMutation);
            Thread.sleep(5000);
            bestPathSum = aje.getBestPathSum();
            System.out.println(bestPathSum);
            System.out.println(index);
            storage.set(index, bestPathSum);
            Thread.sleep(5000);
            mutex.release();
        }
        catch (InterruptedException e)
        {
            Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static class Storage
    {
        int numOfThreads;
        int[] storage;
        int numberThreadUpdate = 0;

        public Storage(int numOfThreads)
        {
            this.numOfThreads = numOfThreads;
            storage = new int[numOfThreads+1];
        }

        public synchronized void set(int index, int value)
        {
            this.storage[index] = value;
            numberThreadUpdate++;
            if(numberThreadUpdate == numOfThreads)
            {
                notify();
            }
        }

        public synchronized int[] get()
        {
            try
            {
                wait();
            }
            catch (InterruptedException e){ Thread.currentThread().interrupt(); }
            return this.storage;
        }
    }

}
