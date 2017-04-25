    package com.mycompany.tinysearchengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.PartOfSpeech;
import se.kth.id1020.util.Word;

/**
 *
 * @author Anna Isacson
 */
public class TinySearchEngine implements TinySearchEngineBase {
    ArrayList<WordNode> wordNodeList = new ArrayList<>();
    WordNode wordNode;
    ArrayList<Attributes> attributeList = new ArrayList<>();
    List<Document> documentList = new ArrayList<>();
    
    @Override
    public void insert(Word word, Attributes attr) {
        if (word.pos == PartOfSpeech.PUNCTUATION){
            return;
        }
        if(word.pos == PartOfSpeech.NUM) {
            return;
        }
        // with !(wordNodeList.contains(wordNode)) it takes longer time.
        if(wordNodeList.isEmpty()){
            wordNode = new WordNode(word.word, attr);
            wordNodeList.add(0, wordNode);
        }
        int index = sort(word.word, wordNodeList);
        
        if(wordNodeList.get(index).word.equals(word.word)){
            wordNodeList.get(index).insertAttributes(attr);
        }
        else {
            index = sort(word.word, wordNodeList);
                wordNodeList.add(index, new WordNode(word.word, attr));
        }
    }

    @Override
    public List<Document> search(String query) {
        String [] querywords = query.split(" ");
        boolean hasOrderby = Arrays.asList(querywords).contains("orderby");
        StringBuilder builder = new StringBuilder();
        String extractedSearchTerms;
        
        if(hasOrderby){
            int orderbyIndex = Arrays.asList(querywords).indexOf("orderby");
            boolean hasProDir = Arrays.asList(querywords).size()-1 == orderbyIndex+2;
                
            if(hasProDir) {
                String property = Arrays.asList(querywords).get(orderbyIndex+1);
                String direction = Arrays.asList(querywords).get(orderbyIndex+2);
                boolean hasDir = direction.contains("asc") || direction.contains("desc");
                
                for(int i = 0; i < querywords.length-3; i++){
                    if (builder.length() > 0) {
                        builder.append(" ");
                    }   
                    builder.append(querywords[i]);
                }
                extractedSearchTerms = builder.toString();

                switch(property){
                    case "count":
                        if(hasDir){System.out.println("orders by count and " + direction);} // not implemented yet
                        else {System.out.println("Invalid query request");}
                        break;
                        
                    case "popularity":
                        if(hasDir) {documentList = searchByPop(extractedSearchTerms, direction);}
                        else {System.out.println("Invalid query request");}
                        break;
                        
                    case "occurence":
                        if(hasDir){System.out.println("orders by occurence and " + direction);} // not implemented yet
                        else {System.out.println("Invalid query request");}
                        break;
                        
                    default: {System.out.println("Invalid query request");}
                        break;
                }
            }
            if(hasProDir == false){
                System.out.println("Invalid query request");
            }
        }
        if(hasOrderby == false) {
            documentList = searchWithoutOrdering(query);
        }
        return documentList;
    }
    
    /**
     * input from insert, which will send to the binary search which will sort
     * and return the index of the sorted word.
     * @param word the word that should get sorted.
     * @param wordList the list which the words should be stored in.
     * @return the index of the sorted word.
    */ 
    public static int sort(String word, ArrayList<WordNode> wordList){
        return binarysearchsort(word, wordList, 0, wordList.size()-1);
    }
    
    public static int binarysearchsort(String word, ArrayList<WordNode> wordList, int lo, int hi){
        if(hi <= lo) return lo;
        int mid = lo + (hi - lo)/2;
        if (mid > wordList.size()-1) return mid;
        int compare = wordList.get(mid).word.compareTo(word);
        if(compare > 0){
            return binarysearchsort(word, wordList, lo, mid);
        }
        else if(compare < 0) {
            return binarysearchsort(word, wordList, mid+1, hi);
        }
        else {
            return mid;
        }
    }
    
    public static int search(String query, ArrayList<WordNode> wordList) {
        return binarysearch(query, wordList, 0, wordList.size()-1);
    }
    
    public static int binarysearch(String query, ArrayList<WordNode> wordList, int lo, int hi) {
        // possible key indices in [lo, hi)
        if (hi <= lo) return -1;
        int mid = lo + (hi - lo) / 2;
        if (mid > wordList.size()-1) return mid;
        int cmp = wordList.get(mid).word.compareTo(query);
        if      (cmp > 0) return binarysearch(query, wordList, lo, mid);
        else if (cmp < 0) return binarysearch(query, wordList, mid+1, hi);
        else              return mid;
    }
    
    public List<Document> searchWithoutOrdering(String query){
        String [] queries = query.split(" ");
        Document document;
        documentList = new ArrayList<>();
        int wordIndex;
        
        for (String querie : queries) {
            wordIndex = search(querie, wordNodeList);
            if(wordIndex == -1){
                System.out.println("Your requested word: " + query + " could not be found.");
            }
            for(int i = 0; i < wordNodeList.get(wordIndex).attributes.size(); i++){
                document = wordNodeList.get(wordIndex).attributes.get(i).document;
           
                if(documentList.isEmpty()){
                    documentList.add(0,document);
                }
                else if(!(documentList.contains(document))) {
                    documentList.add(document);
                }
            }
        }
        return documentList;
    }
    
    public List<Document> searchByPop(String query, String dir) {
        Bubblesort bubblesort = new Bubblesort();
        String [] direction = {"asc", "desc"};
        ArrayList<Integer> popList = new ArrayList<>();
        String [] searchTerms = query.split(" ");
        int pop;
        int wordIndex;
        
        for (String searchTerm : searchTerms) {
            wordIndex = search(searchTerm, wordNodeList);
            if(wordIndex == -1){
                System.out.print("Your requested word: " + searchTerm + "could no be found.");
            }
            for(int i = 0; i < wordNodeList.get(wordIndex).attributes.size(); i++){
                pop = wordNodeList.get(wordIndex).attributes.get(i).document.popularity;
                
                if(popList.isEmpty()){
                    popList.add(0, pop);
                }
                else if(!(popList.contains(pop))){
                    popList.add(pop);
                }
            }
            boolean dirSort = dir.equals(direction[0]);
            bubblesort.bubblesort(popList, dirSort);
            for(int i = 0; i < popList.size(); i++){
                pop = wordNodeList.get(wordIndex).attributes.get(i).document.popularity;
                
                if(popList.isEmpty() && popList.contains(pop)){
                    documentList.add(0, wordNodeList.get(wordIndex).attributes.get(i).document);
                }
                if(popList.contains(pop) && !(documentList.contains(wordNodeList.get(wordIndex).attributes.get(i).document))){
                    documentList.add(i, wordNodeList.get(wordIndex).attributes.get(i).document);
                }
            }
        }
        return documentList;
    }
}
