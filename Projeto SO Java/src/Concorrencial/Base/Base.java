package Concorrencial.Base;

import java.util.ArrayList;

public class Base {

    private ArrayList<Thread> threads = new ArrayList<>();
    private int numberOfThreads;

    public Base(){}

    public void start(int numberThreads)
    {
        for(int i = 0; i < numberThreads; i++)
        {
            BaseThread thread = new BaseThread();
            threads.add(thread);
        }

        numberOfThreads = numberThreads;
    }

    private void threadExecution()
    {
        Storage storage = new Storage(numberOfThreads);


        for (Thread t : threads)
        {
           // int[] bestPath = t.start();
        System.out.print("Passei aqui: threadExecution ");
        }
    }

    static class Storage
    {
        int numOfThreads;
        int[] storage;
        int numberThreadUpdate = 0;

        public Storage(int numOfThreads)
        {
            this.numOfThreads = numOfThreads;
            storage = new int[numOfThreads];
        }

        synchronized void set(int index, int value)
        {
            this.storage[index] = value;
            numberThreadUpdate++;
            if(numberThreadUpdate == numOfThreads)
            {
                notify();
            }
        }

        synchronized int[] get()
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
