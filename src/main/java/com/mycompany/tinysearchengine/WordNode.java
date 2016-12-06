package com.mycompany.tinysearchengine;

import java.util.ArrayList;
import se.kth.id1020.util.Attributes;

/**
 *
 * @author Anna Isacson
 */
public class WordNode {
    public final String word;
    public final ArrayList<Attributes> attributes = new ArrayList<>();

    public WordNode(String word, Attributes attr){
        this.word = word;
        this.attributes.add(attr);
    }
    
    public void insertAttributes(Attributes attr) {
        attributes.add(attr);
    }
}
