package ub.cse.algo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * For use in CSE 331 HW1.
 * This is the class you will be editing and turning in. It will be timed against our implementation
 * NOTE that if you declare this file to be in a package, it will not compile in Autolab
 */
public class Solution {

    private int numberOfMenAndWomen;


    // The following represent the preference list for the men and women.
    // The KEY represents the integer representation of a given man or woman.
    // The VALUE is a list, from most preferred to least, of the member of the opposite gender.
    private HashMap<Integer, ArrayList<Integer>> men;
    private HashMap<Integer, ArrayList<Integer>> women;
    private ArrayList<Matching> stableMatchings = new ArrayList<>();

    /**
     * The constructor simply sets up the necessary data structures.
     * The grader for the homework will first call this class and pass the necessary variables.
     * There is no need to edit this constructor.
     *
     * @param n The number of men/women.
     * @param men A map linking each man (integer value) with their preference list.
     * @param women A map linking each woman (integer value) with their preference list.
     */
    public Solution(int n, HashMap<Integer, ArrayList<Integer>> men, HashMap<Integer, ArrayList<Integer>> women){
        this.numberOfMenAndWomen = n;
        this.men = men;
        this.women = women;
    }

    /**
     * This method must be filled in by you. You may add other methods and subclasses as you see fit,
     * but they must remain within the Solution class.
     * @return Your set of stable matches. Order does not matter.
     */
    public ArrayList<Matching> outputStableMatchings() {

            /* The code below just calls the allPermutations function, and thenjust prints all permutattions*/
            /* To compare your code's output with the sample outpout you need to comment out the part about printing the permutations*/

            ArrayList<ArrayList<Integer>> listOfAllPermutations = new ArrayList<>();
            listOfAllPermutations = allPermutations(numberOfMenAndWomen);

//            System.out.println("----------------------------");
//            System.out.println("Printing all possible permutations of [1,2,...n] for n ="+numberOfMenAndWomen);
//            System.out.println("Total number of permutation generated ="+listOfAllPermutations.size());
//            System.out.println("----------------------------");
            for (ArrayList<Integer> set : listOfAllPermutations) {
                // Generates all possible marriages
                Matching aMatch = new Matching();
                for (int i=0; i<set.size(); i++) {
                    Marriage pair = new Marriage(set.get(i), i+1);  // match a man with a woman
                    aMatch.add(pair);
                }

                int outOftheLoop = 0;

                for (Marriage aPair : aMatch) {
                    Integer man = aPair.man;         // m in (m,w)
                    Integer woman = aPair.woman;     // w in (m,w)
                    ArrayList<Integer> ManPreferenceList = men.get(man);        // the preference list of the man
                    int priorityOfWoman = ManPreferenceList.indexOf(woman);     // the rank of the woman in the man's list
                    boolean IsTheBestWoman = priorityOfWoman == 0;    // whether the woman is the favorite of the man

                    /* When there is at least one woman before the current one in the preference list */
                    if (!IsTheBestWoman) {
                        for (int preference = 0; preference < priorityOfWoman; preference++) {
                            Integer otherWoman = ManPreferenceList.get(preference);  // the preference-th (pth) most preferred woman
                            Integer initialPartner = aMatch.get(otherWoman - 1).man; // the partner of the other woman
                            ArrayList<Integer> otherWomanPreferenceList = women.get(otherWoman);
                            int rankOfInitialOne = otherWomanPreferenceList.indexOf(initialPartner);
                            int rankOfCurrentOne = otherWomanPreferenceList.indexOf(man);

                            // When the potential partner is listed before the initial one,
                            // there is a instability in the matching.
                            if (rankOfCurrentOne < rankOfInitialOne ) {
                                outOftheLoop = 1;
                                break;
                            }
                        }
                    }
                    if (outOftheLoop == 1) {
                        break;
                    }
                }
                if (outOftheLoop == 0) {
                    stableMatchings.add(aMatch);
                }
//                System.out.println(set);
            }
//            System.out.println("----------------------------");
            /*allPermutations call done*/

        return stableMatchings;
    }

    /**
     * Generates all permutations.
     * Just a wrapper function to call permutate
     */
     private ArrayList<ArrayList<Integer>> allPermutations(int n){
            ArrayList<Integer> start = new ArrayList<Integer>();
            for(int k = 1; k<=numberOfMenAndWomen; ++k) {
                start.add(k);
            }
            ArrayList<ArrayList<Integer>> allPermuts= new ArrayList<>();
            permutate(start,allPermuts,n); // Once this call returns the list of all permutations will be in "allPermuts"

            return allPermuts;
     }

    /**
     * This method generates all of the permutations of the input for you.
     * Implements Jeap's algorithm.
     * @param set A complete matching set, not necesarrily stable
     * @param listOfPermut Current of of all opermutations that have been generated so far. This would be updated by ref
     * @param length length of the set
     */
    private void permutate(ArrayList<Integer> set, ArrayList<ArrayList<Integer>> listOfPermut, int length){
        if(length == 1){
            //System.out.println(set);
            //Have to deep copy the current matching so that next call of Heap's does not over-write the current matching
            ArrayList<Integer> cloneSet = new ArrayList<>();
            for(int i = 0; i < set.size(); i++){
                cloneSet.add(set.get(i));
            }
            listOfPermut.add(cloneSet); 
        }
        else{
            for(int i = 0; i < length; i++){
                permutate(set, listOfPermut, length - 1);
                int j = (length % 2 == 0 ) ? i : 0;
                Integer t = set.get(length-1);
                set.set(length-1, set.get(j));
                set.set(j, t);
            }
        }
    }

}
