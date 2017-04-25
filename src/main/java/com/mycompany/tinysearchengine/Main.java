package com.mycompany.tinysearchengine;

import se.kth.id1020.Driver;
import se.kth.id1020.TinySearchEngineBase;

/**
 *
 * @author annaisacson
 */
public class Main {
    public static void main (String [] args) throws Exception {
        TinySearchEngineBase searchEngine = new TinySearchEngine();
        Driver.run(searchEngine);
    }
}
