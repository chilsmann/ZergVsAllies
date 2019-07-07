/**
 * Created by Cameron Hilsmann on 12/1/2018.
 *
 * The attempt of this program was to used a greedy like algorithm to determine the proper number of Protoss and Marines to kill
 * the given Drones and Zerglings. A tough part of this program was actually making it fair. When I first started, I stated the
 * army size would be the 10 or the (sqrt(drones+zerglings) + 5), which ever was bigger. I had to get rid of the + 5 because
 * it got to the point where the zerg would never win unless the minutes were really low. I also had to adjust the values on how much a protoss
 * and zealot could kill in a minute.
 *
 * My test samples include ranges (1, 9999) for all spots with different permutations. I tested have close values to each others, in order to see if different units were used.
 * I used combinations that would force the algorithm to favor Terran units or Protoss units to make sure that that the attack values on units worked properly.
 *
 */

import java.util.*;
import java.io.*;

public class ZergVAllies {
    public static void main(String [] args){
        boolean zergWon;
        //Grabbing File
        File inputFile = new File("input.txt");
        if(args.length > 0){
            inputFile = new File(args[0]);
        }
        //read file
        try {
            Scanner scan = new Scanner(inputFile);
            //d = # of Drones, z = # of zerglings, t = # of minutes
            int d, z, t, counter = 0, numCases;
            numCases = scan.nextInt();
            scan.nextLine();
            while(scan.hasNext()){
                //grab dzt
                d = scan.nextInt();
                z = scan.nextInt();
                t = scan.nextInt();
                //debug
                zergWon = calculateArmy(d,z,t);

                if(zergWon)
                    System.out.println("I am the Swarm. Armies will be shattered. Worlds will burn.");
                else {
                    System.out.println("All your base now belong to us.");
                }
                System.out.println();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }//end main

    private static boolean calculateArmy(int d, int z, int t){
        //calculate army size
        int armySize = 10;
        double zergArmy = d +z;
        if((Math.sqrt(zergArmy)) > armySize)
            armySize = (int)(Math.sqrt(zergArmy));
        // greedy algorithm
        char [] temp = new char[armySize];
        char [] strongestArmy = new char[armySize];
        int bestTime = 10000000 , tempTime;
        int numTerran;
        //this coming for loop is used to test widely different combinations of units to send to calculate the minutes.
        for(int i = 0; i < armySize + 1; i++){
            numTerran = i;
            for(int j = 0; j < armySize; j++) {
                if(numTerran > 0){
                    temp[j] = 'T';
                    numTerran--;
                }//end if
                else
                    temp[j] = 'P';
            }//end innerfor
            tempTime = timeToKill(temp, d,z);
            //Here I check to see if I got a better bestTime than before. Best time starts at 10000 minutes (out of range) to ensure that it will actually grab the best time.
            if(tempTime < bestTime ) {
                bestTime = tempTime;
                for(int x = 0; x < temp.length; x++){
                    strongestArmy[x] = temp[x];
                }

            }

        }//end for
        debugArray(strongestArmy);
        if(bestTime < t)//Protoss and terran win
            return false;
        else
            return true;
    }//end calculate

    public static void debugArray(char [] temp){
        int T=0, P=0;
        //this is used to get how many units there are in the bestArmy to report.
        for(int i = 0; i < temp.length; i++) {
            //System.out.print(temp[i]);
            if(temp[i] == 'T')
                T++;
            else
                P++;
        }
        System.out.println("The number of Protoss Zealots: "+P);
        System.out.println("The number of Terran Marines: "+T);
    }//end debug

    private static int timeToKill(char[]army, int d, int z){
        int armyTime, numP=0, numT=0;
        //One Terran Marine can kill 2 drones per minute, 1 Zerglings per minute.
        //One Protoss Zealot can kill 1 drone per minute, 2 Zerglings per minute.
        for(int i = 0; i < army.length; i++){
            if(army[i] == 'P')
                numP++;
            else
                numT++;
        }//end for
        //protoss army values
        int ProVZerglings = numP*2;
        int ProVDrones = numP*1;
        //Terran Army values
        int terranVZerglings = numT*1;
        int terranVDrones = numT*2;

        int zergDeathTime = z / (ProVZerglings+terranVZerglings);
        int droneDeathTime = d / (ProVDrones + terranVDrones);

        armyTime = zergDeathTime + droneDeathTime;

        return armyTime;

    }//end timeToKill
}
