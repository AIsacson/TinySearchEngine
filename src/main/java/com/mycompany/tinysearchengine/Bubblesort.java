package com.mycompany.tinysearchengine;

import java.util.ArrayList;

/**
 *
 * @author Anna Isacson
 */
public class Bubblesort {

    public ArrayList<Integer> bubblesort(ArrayList<Integer> list, boolean direction){
        ArrayList<Integer> sortedlist = new ArrayList<>();
        boolean swapped = true;
        int r = list.size() - 1;
            
        while(swapped && r >= 0){
            swapped = false;
            for(int i = 0; i < r; i++) {
                //ascending order
                if(direction){
                    if(list.get(i).compareTo(list.get(i+1)) > 0){
                        swap(list, i, i+1);
                        swapped = true;
                    }
                }
                //descending order
                if(direction == false){
                    if(list.get(i).compareTo(list.get(i+1)) < 0){
                        swap(list, i, i+1);
                        swapped = true;
                    }
                }
            }
            --r;
        }
        return sortedlist;
    }
    
    void swap (ArrayList<Integer> list, int first, int second) {
        int temp = list.get(first);
        list.add(first, list.get(second));
        list.add(second, temp);
    }
}