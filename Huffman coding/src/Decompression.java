import java.util.*;
import java.io.*;

public class Decompression {
    
    String address;
    public Decompression(String add){
        address=add;
    }
    public  void decompress() {
    	try{
            Map<String,Character> map=new HashMap<String,Character>();
            InputStreamReader reader =
                   new InputStreamReader(
                      new FileInputStream(address),"UTF-16");
            BufferedReader br = new BufferedReader(reader);
            String s1=br.readLine();
            String[] s2=s1.split(" ");
            int zeroes_to_delete=Integer.parseInt(s2[0]);
            int numberOfCharactersInMap=Integer.parseInt(s2[1]);
            while(numberOfCharactersInMap>0)
            {
                char ch=(char)br.read();
                s1 = br.readLine();
                map.put(s1, ch);
                numberOfCharactersInMap--;
            }
            String data="";
            int storage=br.read();
            while(storage!=-1)
            {
                data += (char)storage;
                storage=br.read();
            }
            String bitString="";
            String code = "";
            for(int i=0;i<data.length();i++)
            {
                String s=Integer.toBinaryString((int)data.charAt(i));
                if(s.length()!=15)
                {
                    String zeros="";
                    for(int j=0;j<15-s.length();j++)
                    {
                        zeros+="0";
                    }
                    s=zeros+s;
                }
                bitString+=s;
            }
            String fileSavePath = address.substring(0, address.lastIndexOf('\\') + 1) + "Decompress.txt"; 
            bitString=bitString.substring(0,bitString.length()-zeroes_to_delete);
            Writer out = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(fileSavePath), "UTF-8"));
            try {
                for(int i=0;i<bitString.length();i++)
                {
                    code += bitString.charAt(i);
                    if (map.get(code) != null) {
                        out.write(map.get(code));
                        code = "";
                    }
                }
            } 
           finally {
            out.close();
            }
            br.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}