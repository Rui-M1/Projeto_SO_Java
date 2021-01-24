import Concorrencial.Base.Base;
import Original.AJE;

import java.util.Scanner;

public class AppStart {

    public static void main(String[] args)
    {
        String res;

        do
        {
            System.out.println("Que algoritmo deseja correr?\n 1- AJE++\n 2- Concorrencial Base\n 3- Concorrencial Avançado");
            Scanner scan = new Scanner(System.in);
            int mode = scan.nextInt();

            if(mode == 1)
            {
                System.out.println("Introduza os parametros necessarios para correr o algoritmo:\n " +
                        "filename,numero de caminhos,tempo limite,chance de mutação");

                String[] parameters = scan.next().split(",");
                String filename = parameters[0];
                int pathSize = Integer.parseInt(parameters[1]);
                int time = Integer.parseInt(parameters[2]);
                double chance = Double.parseDouble(parameters[3]);

                AJE aje = new AJE();
                aje.start(filename, pathSize, time, chance);
            }
            else if(mode == 2)
            {
                System.out.println("Introduza os parametros necessarios para correr o algoritmo:\n " +
                        "filename,numero de threads,numero de caminhos,tempo limite,chance de mutação");

                String[] parameters = scan.next().split(",");
                String filename = parameters[0];
                int threadNum = Integer.parseInt(parameters[1]);
                int pathSize = Integer.parseInt(parameters[2]);
                int time = Integer.parseInt(parameters[3]);
                double chance = Double.parseDouble(parameters[4]);

                int[] resultList = new int[threadNum];

                Base.Storage storage = new Base.Storage(threadNum);

                for(int i = 1; i < threadNum+1; i++)
                {
                    Base thread = new Base(i, filename, pathSize, time, chance, threadNum);
                    thread.start();
                }

                resultList = storage.get();

                bestPathSolution(resultList);

                /*
                System.out.print("Quantas threads deseja criar? ");
                int numThreads = scan.nextInt();
                Base b = new Base();
                b.start(numThreads);
                 */
            }

            System.out.print("Deseja continuar? ");
            res = scan.next();
        }
        while(res == "n" || res == "nao" || res == "Nao");
    }

    private static void bestPathSolution(int[] resultList)
    {
        int bestSolution = 0;

        for(int i = 0; i < resultList.length; i++)
        {
            int curr = i;
            if(resultList[curr] < resultList[curr+1])
            {
                bestSolution = resultList[i];
            }
        }

        System.out.println("The best solution found is: " + bestSolution);
    }
}
