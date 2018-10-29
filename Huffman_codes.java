import java.util.*;

//import com.sun.org.apache.xpath.internal.operations.String;

import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;





class Huffman_codes{
    
    public static HashMap<Character, Integer> sortedMap = new HashMap<>();
    public static HashMap<Character, Integer> AscSortedMap = new HashMap<>();
    public static HashMap<Character, Integer> map = new HashMap<>();
    public static HashMap<Character, Double> percentFreqMap = new HashMap<>();
    public static HashMap<Character, Integer> HuffmanCodeValue = new HashMap<>();
    ArrayList<Character> charac = new ArrayList<>();
    //String[] charac = new String[sortedMap.size()];
    ArrayList<Integer> charValues = new ArrayList<>();
    //int[] charValues = new int[sortedMap.size()];
    
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

    public void frequency(String message) throws Exception{

        message = message.toUpperCase();

        int count=0;
        double countPercent = 0;
        double len = message.length();
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


        AscSortedMap = map.entrySet().stream().sorted(comparingByValue())
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));
        // for(int i =0; i<sortedMap.size();i++){
        //     charac[i] = sortedMap.get(key);
        //     charValues[i] = sortedMap.get(value);
        // }

        Iterator it = AscSortedMap.entrySet().iterator();
        //int i =0;
        //System.out.println(charValues.size);
        while(it.hasNext()){                                        //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
            Map.Entry val = (Map.Entry)it.next();
            charac.add(val.getKey().toString().charAt(0));
            String temp = val.getValue().toString();
            charValues.add(Integer.parseInt(temp));
            //double valPercent = (Integer.parseInt(temp)/message.length())*100;
            //percentFreqMap.put(val.getKey().toString().charAt(0), valPercent);
           // i++;
            //it.remove();
        }
        System.out.println(charac);
        System.out.println(charValues);

        Scanner s = new Scanner(System.in);

        int num = charac.size();

        PriorityQueue <HuffmanNode> q = new PriorityQueue<HuffmanNode>(num, new myComparator());

        for(int i =0; i<num; i++){

            HuffmanNode hn = new HuffmanNode();
            hn.c = charac.get(i);
            hn.data = charValues.get(i);

            hn.leftChild = null;
            hn.rightChild = null;

            q.add(hn);
        }

        HuffmanNode root = null;


        while(q.size() >1){
            HuffmanNode x = q.peek();
            q.poll();

            HuffmanNode y = q.peek();
            q.poll();

            HuffmanNode f = new HuffmanNode();

            f.data = x.data + y.data;
            f.c = '-';

            f.leftChild =x;

            f.rightChild = y;

            root = f;

            q.add(f);
        }

        printHuffmanCode(root, "");


        printInFile(len);

    }


    public void printInFile(Double len) throws Exception{


        BufferedWriter writer = new BufferedWriter(new FileWriter("outfile.dat"));
        Iterator it = sortedMap.entrySet().iterator();
        writer.write("Symbols \t Frequency \t Frequence Percent");
        writer.write("\n");
        while(it.hasNext()){                                        //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
            Map.Entry val = (Map.Entry)it.next();
            writer.write("\t");
            writer.write(val.getKey().toString());
            writer.write("\t \t \t");
            writer.write(val.getValue().toString());
            writer.write("\t \t \t");
            double x = Double.parseDouble(val.getValue().toString());
            //System.out.println(x);
            double percent = (x*100.00)/len;
            //System.out.println(percent);
            writer.write("%.4f"+percent+ "%");
            writer.write("\n");
            //charac.add(val.getKey().toString().charAt(0));
            //String temp = val.getValue().toString();
            //charValues.add(Integer.parseInt(temp));
            //i++;
            //it.remove();
        }
        writer.write("\n \n");
        Iterator itNew = HuffmanCodeValue.entrySet().iterator();
        writer.write("Code table \n");
        writer.write("Symbols \t Huffman Code \n");
        while(itNew.hasNext()){
            Map.Entry huffVal = (Map.Entry)itNew.next();
            writer.write("\t");
            writer.write(huffVal.getKey().toString());
            writer.write("\t \t \t");
            writer.write(huffVal.getValue().toString());
            writer.write("\n");

        }



        //System.out.println(len);
        writer.close();

    }
    public static void printHuffmanCode(HuffmanNode root, String s){
        if(root.leftChild  == null && root.rightChild ==null && Character.isLetter(root.c)){

            HuffmanCodeValue.put(root.c, Integer.parseInt(s));
            return;

        }

        printHuffmanCode(root.leftChild, s + "0");
        printHuffmanCode(root.rightChild, s + "1");

    }





    
}
class HuffmanNode{
    int data;
    char c;

    HuffmanNode leftChild;
    HuffmanNode rightChild;
}

class myComparator implements Comparator<HuffmanNode>{
    public int compare(HuffmanNode x, HuffmanNode y){
        return x.data = y.data;
    }
}

