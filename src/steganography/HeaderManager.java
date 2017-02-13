package steganography;

import java.io.File;

public class HeaderManager 
{ 
    //first 5 bytes for extension 
    //next 8 bytes for size 
    //last 17 bytes for file name 

    public static final int HEADER_LENGTH= 30;
    
    static String getHeader(String fname) throws Exception
    {
        String name, extension, size;
        int pos, i;
        
        File f = new File(fname);
        long len = f.length();
        if(len == 0)
            throw new Exception ("FileNotFound");
    
        size = String.valueOf(len);
        
        //right pad to length 8
        while(size.length() < 8)
            size = size + " ";
        
        //d:/my pictures/flower.jpg
        //converts to
        //flower.jpg
        fname = f.getName();
        
        pos = fname.lastIndexOf(".");
        if(pos == -1)
        {//no extension
            extension = "";
            name = fname;
        }
        else
        {
            extension = fname.substring(pos);
            name = fname.substring(0,pos);
        }
        
        //right pad to length 5
        while(extension.length() < 5)
            extension = extension + " ";
        
        //right pad to length 17
        while(name.length() < 17)
            name = name + " ";
        
        return extension + size + name;
    }

    static long getFileSize(String header) throws Exception
    {//*****########&&&&&&&&&&&&&&&&&
        return Long.parseLong(header.substring(5,13).trim());
    }
    
    static String getFileName(String header)throws Exception
    {//*****########&&&&&&&&&&&&&&&&&
        return header.substring(13).trim() + header.substring(0,5).trim();
    }
}
