import Concorrencial.Base.Base;
import Original.AJE;

import java.util.Scanner;

public class AppStart {

    public static void main(String[] args)
    {
        String res;
        int continuarCorrer = 0;

        do
        {
            System.out.println("Que algoritmo deseja correr?\n 1- AJE++\n 2- Concorrencial Base\n 3- Concorrencial Avançado\n 0 - Terminar Programa");
            Scanner scan = new Scanner(System.in);
            int mode = scan.nextInt();

            switch(mode)
            {
                case 1:
                System.out.println("Introduza os parametros necessarios para correr o algoritmo:\n " +
                        "filename,numero de caminhos,tempo limite,chance de mutação");

                String[] parameters = scan.next().split(",");
                String filename = parameters[0];
                int pathSize = Integer.parseInt(parameters[1]);
                int time = Integer.parseInt(parameters[2]);
                double chance = Double.parseDouble(parameters[3]);

                AJE aje = new AJE();
                aje.start(filename, pathSize, time, chance);
                
                break;

                case 2:
            
                System.out.println("Introduza os parametros necessarios para correr o algoritmo:\n " +
                        "filename,numero de threads,numero de caminhos,tempo limite,chance de mutação");

                String[] parameters2 = scan.next().split(",");
                String filename2 = parameters2[0];
                int threadNum = Integer.parseInt(parameters2[1]);
                int pathSize2 = Integer.parseInt(parameters2[2]);
                int time2 = Integer.parseInt(parameters2[3]);
                double chance2 = Double.parseDouble(parameters2[4]);

                int[] resultList = new int[threadNum];

                Base.Storage storage = new Base.Storage(threadNum);

                for(int i = 1; i < threadNum+1; i++)
                {
                    Base thread = new Base(i, filename2, pathSize2, time2, chance2, threadNum);
                    thread.start();
                }

                resultList = storage.get();

                bestPathSolution(resultList);

                break;

                case 0:
                System.out.println("Programa Terminado!");
                System.exit(0);

                break;
            }

                do
                {System.out.print("Deseja continuar? (S/N)");
                res = scan.next();
            
                switch(res){
                    case "s":
                    case "S":
                    continuarCorrer = 0;
                    break;
                    case "N":
                    case "n":
                    System.out.println("Programa Terminado!");
                    continuarCorrer = 1;
                    System.exit(0);
                    break;
                }
                }
                while(res != "s" || res !="S" || res != "n" || res != "N") ;
                scan.close();
        }
        while(continuarCorrer == 0);

    }

    private static void bestPathSolution(int[] resultList) {
        int bestSolution = 0;

        for (int i = 0; i < resultList.length; i++) {
            int curr = i;
            if (resultList[curr] < resultList[curr + 1]) {
                bestSolution = resultList[i];
            }
        }

        System.out.println("The best solution found is: " + bestSolution);
    }
}
