import java.util.*;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
import static java.util.stream.Collectors.*;
//import static java.util.Map.Entry.*;





class Huffman_codes{
    
    public static HashMap<Character, Integer> sortedMap = new HashMap<>();
    public static HashMap<Character, Integer> map = new HashMap<>();
    
    public static void main(String[] args)throws Exception{

        Huffman_codes a = new Huffman_codes();
        //HashMap<Character, Integer> map = new HashMap<>();
        String message = ""; 
        a.readFile(message);

        System.out.println(map);
        System.out.println(sortedMap);
    }

    public void readFile(String message) throws Exception{
        //String text ="";
        String msg="";
        //int counter =0;
        //int outCOunter = 0;
        
        msg = new String(Files.readAllBytes(Paths.get("infile.dat")));   //https://www.geeksforgeeks.org/different-ways-reading-text-file-java/

        System.out.println(msg.length());
            
        for(int i = 0; i<msg.length(); i++){
            char c = msg.charAt(i);
            if(Character.isDigit(c) || Character.isLetter(c)){
                message += c;
                
                //counter ++;
            }      
            //outCOunter++;
            //System.out.println();
        } 
        System.out.println(message);
        frequency(message);
        //System.out.println(counter);
       // System.out.println(outCOunter);
  
    }

    public void frequency(String message){

        message = message.toUpperCase();

        int count=0;
        double countPercent = 0;
        //System.out.println(message.length());
        for(char c = 'A'; c<='Z'; c++){                 //http://www.vinaysingh.info/frequency-table/
            count = 0;
            char ch = c;
            for(int i=0; i<message.length(); i++){
                //System.out.println("abcs");
                if(c==message.charAt(i)){
                    count++;
                    //System.out.println("123");
                }
            }
            if(count>0){

                //System.out.println(c+" : "+count);
                map.put(c,count);

            }

        }

        sortedMap = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new)); //https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html




    }
    
}