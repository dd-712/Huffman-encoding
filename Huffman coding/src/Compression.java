import java.io.*;
import java.util.*;

public class Compression {

    String address;
    public Compression(String add){
        address=add;
    }
    
    static class Node{
        char ch;
        int freq;
        Node left, right;
        Node(char ch,int freq,Node l,Node r)
        {
            this.ch=ch;
            this.freq = freq;
            left = l;
            right = r;
        }
    }

    static class Sortbyfreq implements Comparator<Node> 
    { 
        @Override
        public int compare(Node a,Node b)
        {
            return a.freq-b.freq;
        }
    }

    static class PQsort implements Comparator<Node> 
    { 
        @Override
        public int compare(Node a,Node b)
        {
            return a.freq-b.freq;
        }
    }

    static void Inorder(Node t,String s,Map<Character,String> map)
    {
        if(t!=null)
        {
            Inorder(t.left,s+'0',map);
            if(t.left==null && t.right==null)
            {
                map.put(t.ch, s);
            }
            Inorder(t.right,s+'1',map);
        }
    }
   
    public void compress() {
        try {
            File f = new File(address);
            FileReader fr;
            fr = new FileReader(f);
            BufferedReader br=new BufferedReader(fr);
            int freq[]=new int[500];
            String dataString="";
            int sum=0,numberOfCharactersInMap=0;
            int data=br.read();
            dataString+=(char)data;
            while(data!=-1)
            {
                freq[data]++;
                data=br.read();
                if(data!=-1)
                    dataString+=(char)data;
            }
            br.close();
            ArrayList<Node> arr=new ArrayList<Node>();
            PriorityQueue<Node> pq=new PriorityQueue<Node>(500,new PQsort());
            for(int i=0;i<500;i++)
            {
                if(freq[i]!=0)
                {
                    numberOfCharactersInMap++;
                    sum += freq[i];
                    Node node=new Node((char)i,freq[i],null,null);
                    arr.add(node);
                    pq.add(node);
                }
            }
            
            Collections.sort(arr,new Sortbyfreq());
            Node root=null;
            while(pq.peek().freq!=sum)
            {
                Node n1 = pq.peek();
                pq.poll();
                Node n2 = pq.peek();
                pq.poll();
                Node parent=new Node('a',(n1.freq+n2.freq),n1,n2);
                pq.add(parent);
                root=parent;
            }
            Map<Character,String> map=new HashMap<Character,String>();
            Inorder(root,"",map);
            String bitString="";
            for(int i=0;i<dataString.length();i++)
            {
                bitString+=map.get(dataString.charAt(i));
            }
            int zeros_to_add=15-(bitString.length()%15);
            for(int i=0;i<zeros_to_add;i++)
            {
                bitString+="0";
            }
//            File fp=new File("Compressed.txt");
            String fileSavePath = address.substring(0, address.lastIndexOf('\\') + 1) + "Compress.txt";
            OutputStreamWriter out=new OutputStreamWriter(new FileOutputStream(fileSavePath), "UTF-16");
            PrintWriter pw=new PrintWriter(out);
            pw.print(zeros_to_add+" ");
            pw.println(numberOfCharactersInMap);
            for (Map.Entry<Character,String> entry : map.entrySet())  
                pw.println(entry.getKey()+entry.getValue());
            for(int i=0;i<bitString.length();i+=15)
            {
                int code=0;
                int k=14;
                for(int j=i;j<i+15;j++)
                {
                    if(bitString.charAt(j)=='1')
                        code+=(int)Math.pow(2,k);
                    k--;
                }
                pw.print((char) code);
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
