package Concorrencial.Base;

import Original.AJE;

public class BaseThread extends Thread{

    public static int threadID = -1;
    private AJE aje;

    @Override
    public void run()
    {
        aje = new AJE();
        aje.start();
        aje.getBestPath();
    }

    public BaseThread()
    {
        threadID++;
    }
    
    //public return Best
    

}
