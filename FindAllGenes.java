/**
 * This Java program takes a DNA string and prints the number of genes found
 * 
 * @author Joanne Hsu 
 * @version 1.0
 */
import edu.duke.*;
import java.io.*;

public class FindAllGenes {
    
    // find stop positions in a dna string
     public int findStopIndex(String dna, int startIndex) {
        //set position of the stop codon (tga, taa, tag)
        //if the stop codon is not found, set position to dna length
        int tgaIndex = dna.indexOf("tga", startIndex);
        if (tgaIndex == -1 || (tgaIndex - startIndex) % 3 != 0) {
            tgaIndex = dna.length();
        }
        int taaIndex = dna.indexOf("taa", startIndex);
        if (taaIndex == -1 || (taaIndex - startIndex) % 3 != 0) {
            taaIndex = dna.length();
        }
        int tagIndex = dna.indexOf("tag", startIndex);
        if (tagIndex == -1 || (tagIndex - startIndex) % 3 != 0) {
            tagIndex = dna.length();
        }
        return Math.min(tgaIndex, Math.min(taaIndex, tagIndex));
    }
    
    // print all genes using a dna string
    public void testFindGenes() {
        String dna = "atggggtgaatggggtgaatggggtgaatggggtga";        
        String dnaLower = dna.toLowerCase();
        int index = 0;
        while (true) {
            int startIndex = dnaLower.indexOf("atg", index);
            if (startIndex == -1) {
                break;
            }
            int stopIndex = findStopIndex(dnaLower, startIndex + 3);
            if (stopIndex != dna.length()) {
                System.out.println("Genes found: " + dna.substring(startIndex, stopIndex + 3));
                index = stopIndex + 3;
            } else {
                index++;
            }
        }
    }
    
    // store all genes
    public StorageResource getAllGenes(String dna) {
        String dnaLower = dna.toLowerCase();
        int index = 0;
        StorageResource genes = new StorageResource();
        // while there are genes downstream, add them to the storage object
        while (true) {
            int startIndex = dnaLower.indexOf("atg", index);
            if (startIndex == -1) {
                break;
            }
            int stopIndex = findStopIndex(dnaLower, startIndex + 3);
            if (stopIndex != dna.length()) {
                genes.add(dna.substring(startIndex, stopIndex + 3));
                index = stopIndex + 3;
            } else {
                index++;
            }
        }
        return genes;
    }
    
    // count bases in a dna string
    public int howManyBases(String base, String dna){
        int count = 0;
        int startIndex = dna.indexOf(base);
        while(true){
            int currentIndex = dna.indexOf(base,startIndex);
            if (currentIndex == -1){
             break;   
            }
            count++;
            startIndex = currentIndex + base.length();
        }
        return count;
    }

    // calculate CG ratio in a string
    public float cgRatio(String dna) {
        dna = dna.toLowerCase();
        float cg = (float)(howManyBases("c", dna) + howManyBases("g",dna));
        float ratio = cg/dna.length();
        return ratio;
    }
    
    public void printCG(){
        float result = cgRatio("ATGCCCGGGATAG");
        System.out.println("GC ratio " + result);
    }

    // count number of CTGs in a string
    public int countCTG(String dna){
        dna = dna.toLowerCase();
        String stringa = "ctg";
        int startIndex = dna.indexOf(stringa);
        int max = 0;
        int count = 0;
        while(true){
             int currentIndex = dna.indexOf(stringa,startIndex);
            if (currentIndex == -1){
             break;   
            }
            count++;
            startIndex = currentIndex + stringa.length();
        }
        return count;
    }
    
    // get gene data
    public void processGenes(StorageResource sr){
        int count = 0;
        int geneNumber = 0;
        int cgCount = 0;
        int maxLength = 0;
        int gene_count = 0;
        
        for(String gene: sr.data()){
            geneNumber++;
            float cg = cgRatio(gene);
            if(gene.length() > 60){
                count++;
            }
            if (cg > 0.35){
                cgCount++;
                }
            if (gene.length() > maxLength) {
                maxLength = gene.length();
            }
        }
        System.out.println("Total number of genes found in the DNA string: " + geneNumber);
        System.out.println("Total number of genes with longer than 60 bases: " + count);
        System.out.println("Total number of genes whose C-G-ratio is higher than 0.35: " + cgCount);
        System.out.println("The longest gene length found: " + maxLength);
       
    }
    
    // print gene data
    public void testProcessGenes(){
        //get file object
        FileResource file = new FileResource("brca1line.fa");
        //convert file object to string object
        String dna = file.asString();
        dna = dna.toLowerCase();
        StorageResource results = getAllGenes(dna);
        processGenes(results);
        int ctg = countCTG(dna);
        System.out.println("Total number of CTGs found in the DNA string: " + ctg);
    }
}
