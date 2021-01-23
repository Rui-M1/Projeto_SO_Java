package Original;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import Resources.PMXCrossover;

public class AJE{

    private Random rand = new Random();;
    private Scanner scan = new Scanner(System.in);;

    //para efeito de teste inicial, depois é imported
    /*
    private int[][] matrix = {
        {0, 23, 10, 4, 1},
        {23, 0, 9, 5, 4},
        {10, 9, 0, 8, 2},
        {4, 5, 8, 0, 11},
        {1, 4, 2, 11, 0}
    };
     */

    private int pathSize;
    private int n;
    private int[][] matrix;
    private int[] bestPath;

    public AJE(){}

    //isto é para testar se o algoritmo funciona sem a populacao
    //de dados com os valores que o stor da
    /*private void Test()
    {

    }*/

    private void importValues()
    {
        System.out.print("Indique o nome do ficheiro que pretende importar:  //indicar aoenas o nome do ficheiro sem tipo ");
        String filename = scan.nextLine();

        String path = "tsp_testes\\";

        File file = new File(path + filename + ".txt");

        try
        {
            Scanner scanFile = new Scanner(file);

            if(scanFile.hasNextLine())
                n = Integer.parseInt(scanFile.nextLine());

            matrix = new int[n][n];

            int i = 0;
            while (scanFile.hasNextLine() && i < n)
            {
                for(int j = 0; j < n; j++)
                {
                    int dist = scanFile.nextInt();
                    matrix[i][j] = dist;
                }
                i++;
            }
            scanFile.close();
        }
        catch (FileNotFoundException e){ e.printStackTrace(); }

    }

    public void start()
    {
        importValues();

        //n = 5; // isto é temporario porque depois é imported

        System.out.print("Quantos caminhos deseja criar? ");
        pathSize = scan.nextInt();

        System.out.print("Durante quantas interações deseja que o programa corra: ");
        int interactions = scan.nextInt();

        int currentInteraction = 0;

        ArrayList<int[]> paths = new ArrayList<>();
        int[] goodPath1 = new int[n];
        int[] goodPath2 = new int[n];

        populatePaths(paths);
        printPathsArray(paths);

        while(currentInteraction != interactions)
        {
            goodPath1 = evaluatePaths(paths);
            paths.remove(goodPath1);
            goodPath2 = evaluatePaths(paths);
            paths.add(goodPath1);
            printGoodPaths(goodPath1, goodPath2);

            int[] goodPathChild1 = new int[n];
            int[] goodPathChild2 = new int[n];

            PMXCrossover pmx = new PMXCrossover();
            pmx.pmxCrossover(goodPath1, goodPath2, goodPathChild1, goodPathChild2, n, rand);


            int prob = rand.nextInt(100) + 1;
            if(prob <= 5)
            {
                applyMutation(goodPathChild1, goodPathChild2);
                postMutationExchange(goodPathChild1, goodPathChild2, paths);
            }

            //TODO: ver o porque de isto demorar bueeee tempo a chegar aqui e resolver
            // printPathsArray(paths);

            currentInteraction++;
        }

        bestPath = goodPath1;

    }

    private void populatePaths(ArrayList<int[]> paths)
    {
        for(int i = 0; i < pathSize; i++)
        {
            int path[] = new int[n];

            for (int j = 0; j < n; j++)
            {
                int curr = j;

                int dist = rand.nextInt(n);

                //TODO: fazer de maneira a que nao existam randoms repetidos
                if(j > 0 && dist == path[curr-1]) {
                    do
                    {
                        dist = rand.nextInt(5);
                    }while (path[curr-1] == dist);
                }
                path[j] = dist;
            }
            paths.add(path);
        }
    }

    //TODO: verificar se ha melhor solução | resolver array error
    private int[] evaluatePaths(ArrayList<int[]> paths)
    {
        int[] goodPath = new int[n];
        int goodPathSum = 0;

        for (int[] path : paths)
        {
            int arraySum = 0;
            for(int i = 0; i < path.length; i++)
            {
                int curr = i;
                if(curr+1 != n-1)
                    //TODO: ex4 arrebenta aqui tentar perceber porque e resolver
                    arraySum += matrix[path[curr]][path[curr++]];
                else
                    arraySum += matrix[path[curr]][path[0]];
            }
            if(goodPathSum == 0)
            {
                goodPathSum = arraySum;
                goodPath = path;
            }
            else if(goodPathSum > arraySum)
            {
                goodPathSum = arraySum;
                goodPath = path;
            }
        }
        return goodPath;
    }

    private void applyMutation(int[] goodPathChild, int[] goodPathChild2)
    {
        int tempPos = 0;

        int randomPos1 = rand.nextInt(5);
        int randomPos2 = 0;
        int tempRand = rand.nextInt(5);

        while(tempRand == randomPos1)
            tempRand = rand.nextInt();

        randomPos2 = tempRand;

        tempPos = goodPathChild[randomPos1];
        goodPathChild[randomPos1] = goodPathChild2[randomPos1];
        goodPathChild2[randomPos1] = tempPos;

        tempPos = goodPathChild[randomPos2];
        goodPathChild[randomPos2] = goodPathChild2[randomPos2];
        goodPathChild2[randomPos2] = tempPos;
    }

    private void postMutationExchange(int[] goodPathChild, int[] goodPathChild2, ArrayList<int[]> paths)
    {
        int goodPathChildSum = 0;
        int goodPathChildSum2 = 0;
        
        for(int i = 0; i < goodPathChild.length; i++)
        {
            int curr = i;
            int curr2 = i;
            if(curr+1 != n-1)
            {
                goodPathChildSum += matrix[goodPathChild[curr]][goodPathChild[curr++]];
                goodPathChildSum2 += matrix[goodPathChild2[curr2]][goodPathChild2[curr2++]];
            }
            else
                {
                    goodPathChildSum += matrix[goodPathChild[curr]][goodPathChild[0]]; 
                    goodPathChildSum2 += matrix[goodPathChild2[curr2]][goodPathChild2[0]];
                }
        }

        for (int i = 0; i < n; i++)
            System.out.printf("%2d, ", goodPathChild[i]);
        System.out.println();

        for (int i = 0; i < n; i++)
            System.out.printf("%2d, ", goodPathChild2[i]);
        System.out.println();

        getBiggestPath(paths);
        getBiggestPath(paths);

        paths.add(goodPathChild);
        paths.add(goodPathChild2);

    }

    private void getBiggestPath(ArrayList<int[]> paths)
    {
        int[] bigPath = new int[n];
        int bigPathSum = 0;

        for (int[] path : paths)
        {
            int arraySum = 0;
            for(int i = 0; i < path.length; i++)
            {
                int curr = i;
                if(curr+1 != n-1)
                    arraySum += matrix[path[curr]][path[curr++]];
                else
                    arraySum += matrix[path[curr]][path[0]];
            }
            if(bigPathSum == 0)
            {
                bigPathSum = arraySum;
                bigPath = path;
            }
            else if(bigPathSum < arraySum)
            {
                bigPathSum = arraySum;
                bigPath = path;
            }
        }
        paths.remove(bigPath);
    }

    private void printGoodPaths(int[] goodPath1, int[] goodPath2)
    {
        System.out.println("\nGood Path 1:");
        for (int i = 0; i < n; i++)
            System.out.printf("%2d, ", goodPath1[i]);
        System.out.println();

        System.out.println("\nGood Path 2:");
        for (int i = 0; i < n; i++)
            System.out.printf("%2d, ", goodPath2[i]);
        System.out.println("\n\n");
    }

    private void printPathsArray(ArrayList<int[]> paths)
    {
        int currentArray = 1;
        for (int[] path : paths) {
            System.out.println("Array " + currentArray + ":");
            for (int i = 0; i < n; i++)
                System.out.printf("%2d, ", path[i]);
            System.out.println();
            currentArray++;
        }
        System.out.println();
    }

    public int[] getBestPath()
    {
        return bestPath;
    }
}