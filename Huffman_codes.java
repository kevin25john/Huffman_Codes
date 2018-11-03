import java.util.*;
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
    
    public static HashMap<String, Integer> sortedMap = new HashMap<>();
    public static HashMap<String, Integer> AscSortedMap = new HashMap<>();
    public static HashMap<String, Integer> map = new HashMap<>();
    public static HashMap<String, Double> percentFreqMap = new HashMap<>();
    public static HashMap<String, String> HuffmanCodeValue = new HashMap<>();
    public static HashMap<String, Integer> codeLengthMap = new HashMap<>();
    ArrayList<Character> charac = new ArrayList<>();
    ArrayList<Integer> charValues = new ArrayList<>();
    int totalBits;
    
    public static void main(String[] args)throws Exception{

        Huffman_codes a = new Huffman_codes();
        String message = ""; 
        a.readFile(message);
    }

    public void readFile(String message) throws Exception{
        String msg="";
        
        msg = new String(Files.readAllBytes(Paths.get("infile.dat")));   //https://www.geeksforgeeks.org/different-ways-reading-text-file-java/

            
        for(int i = 0; i<msg.length(); i++){
            char c = msg.charAt(i);
            if(Character.isDigit(c) || Character.isLetter(c)){
                message += c;
                
            }      
        } 
        frequency(message);
  
    }

    public void frequency(String message) throws Exception{

        message = message.toUpperCase();

        int count=0;
        double countPercent = 0;
        double len = message.length();
        for(Character c = 'A'; c<='Z'; c++){                 //http://www.vinaysingh.info/frequency-table/
            count = 0;
            String ch = "";
            String s = c.toString();
            for(int i=0; i<message.length(); i++){
                if(c==message.charAt(i)){
                    ch = s;
                    count++;
                }
            }
            if(count>0){

                map.put(ch,count);

            }

        }

        for(int i =0; i<10;i++){
            count = 0;
            String x = "";
            String s = Integer.toString(i);
            for(int j=0; j<message.length(); j++){
                String t = Character.toString(message.charAt(j));
                if(s.equals(t)){
                    x = s;
                    count++;
                    //System.out.println(s);
                    
                }
            }
            if(count>0){

                map.put(x.toString(),count);

            }

        }

        

        sortedMap = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new)); //https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html


        AscSortedMap = map.entrySet().stream().sorted(comparingByValue())
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));
        

        Iterator it = AscSortedMap.entrySet().iterator();
      
        while(it.hasNext()){                                        //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
            Map.Entry val = (Map.Entry)it.next();
            charac.add(val.getKey().toString().charAt(0));
            String temp = val.getValue().toString();
            charValues.add(Integer.parseInt(temp));
            
            //it.remove();
        }
        //System.out.println(charac);
       // System.out.println(charValues);

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
        writer.write("\t Frequency Table \n \n");
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
            double percent = (x*100.00)/len;
            writer.write(percent+ "%");
            writer.write("\n");
            
            //it.remove();
        }

        //System.out.println(HuffmanCodeValueSorted);
        huffmanBits();
        System.out.println(totalBits);
        //System.out.println(codeLengthMap);

        writer.write("\n \n");
        writer.write("\t Huffman Code table \n \n");
        writer.write("Symbols \t Huffman Code \t Code Length \n");
        Iterator itSort = sortedMap.entrySet().iterator();
        while(itSort.hasNext()){

            Map.Entry vals = (Map.Entry)itSort.next();
            //Character temp = vals.getKey();
            //HuffmanCodeValueSorted.put(temp, HuffmanCodeValue.get(temp).toString());
            writer.write("\t");
            writer.write(vals.getKey().toString());
            writer.write("\t \t \t");
            writer.write(HuffmanCodeValue.get((vals.getKey().toString())));
            int codeKey = codeLengthMap.get((vals.getKey()));
            if(codeKey<=3){
                writer.write("\t \t \t \t");
            }
            else if(codeKey>3 && codeKey<8){
                writer.write("\t \t \t");
            }
            else{
                writer.write("\t \t");
            }
            //System.out.println(codeKey);
            writer.write(Integer.toString(codeKey));
            writer.write("\n");

        }

        writer.write("\n");
        writer.write("Total number of bits: "+Integer.toString(totalBits));


        //System.out.println(sortedMap);
        // Iterator itNew = HuffmanCodeValueSorted.entrySet().iterator();
        
        // while(itNew.hasNext()){
        //     Map.Entry huffVal = (Map.Entry)itNew.next();
        //     writer.write("\t");
        //     writer.write(huffVal.getKey().toString());
        //     writer.write("\t \t \t");
        //     writer.write(huffVal.getValue().toString());
        //     writer.write("\n");

        // }




        //System.out.println(len);
        writer.close();

    }
    public static void printHuffmanCode(HuffmanNode root, String s){
        if(root.leftChild  == null && root.rightChild ==null && (Character.isLetter(root.c) || Character.isDigit(root.c))){

            HuffmanCodeValue.put((root.c).toString(), s);
            return;
            

        }

        printHuffmanCode(root.leftChild, s + "0");
        printHuffmanCode(root.rightChild, s + "1");

    }
    

    public void huffmanBits(){


        Iterator itSort = HuffmanCodeValue.entrySet().iterator();
        while(itSort.hasNext()){

            Map.Entry vals = (Map.Entry)itSort.next();
            String huffKey = vals.getKey().toString();
            String huffVal = vals.getValue().toString();
            int codeLength = huffVal.length();
            codeLengthMap.put(huffKey, codeLength);

        }
        totalBits = 0;
        int adder =0;
        Iterator itBits = sortedMap.entrySet().iterator();
        while(itBits.hasNext()){
            Map.Entry vals = (Map.Entry)itBits.next();
            String keyNew = vals.getKey().toString();
            int temp =0;
            int codeLengthTemp = codeLengthMap.get(keyNew);
            int freq = Integer.parseInt(vals.getValue().toString());
            adder = adder + (freq*codeLengthTemp);
        }
        totalBits = adder;



    }





    
}
class HuffmanNode{
    int data;
    Character c;

    HuffmanNode leftChild;
    HuffmanNode rightChild;
}

class myComparator implements Comparator<HuffmanNode>{
    public int compare(HuffmanNode x, HuffmanNode y){
        return x.data - y.data;
    }
}

