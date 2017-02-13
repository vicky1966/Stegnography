package steganography;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import javax.imageio.ImageIO;

public class Embed 
{
    File fImage;
    File fFile;
    public Embed(String imgName, String fileName) throws Exception
    {
        fImage = new File(imgName);
        fFile = new File(fileName);
        
        if(!fImage.exists() )
            throw new Exception("Image " + imgName +"Not Found");
        if(!fFile.exists() )
            throw new Exception("File "+ fileName +"Not Found ");
    }//Embed
    
    String embedIt(String password) throws Exception
    {
        BufferedImage bi;
        bi = ImageIO.read(fImage);

        int w, h;
        w = bi.getWidth();//w
        h = bi.getHeight();//h

        //size check
        long capacity = (long)w * h ;
        long need = fFile.length() + HeaderManager.HEADER_LENGTH;

        if(capacity < need)
            throw new Exception("File Too Large, Embedding Not Possible");

        WritableRaster wrstr = bi.getRaster();
        int x, y;
        int r,g,b;
        
        FileInputStream fin = new FileInputStream(fFile);
        int data= 0;
        int arr[];
        
        ByteManager bm = new ByteManager(password);
        
        String header = HeaderManager.getHeader(fFile.getAbsolutePath());
        int i = 0;
        
        for(y =0 ; y< h && data != -1; y++)
        {
            for(x =0 ; x < w; x++)
            {//row wise
                
                //header to be embedded yet
                if(i < HeaderManager.HEADER_LENGTH)
                {
                    data = header.charAt(i++);
                }
                else
                {
                    data = fin.read();//byte to embed
                    if(data == -1)
                    {//eof
                        break;
                    }
               }
                
             
                //pixel
                r = wrstr.getSample(x, y, 0);//red
                g = wrstr.getSample(x, y, 1);//green
                b = wrstr.getSample(x, y, 2);//blue
                
                data = bm.getEncrypted(data);
                arr = bm.breakThebyte(data);
                
                r=(r&0xF8)|arr[0];
                g=(g&0xF8)|arr[1];
                b=(b&0xFC)|arr[2];
                
                wrstr.setSample(x, y, 0, r);//red
                wrstr.setSample(x, y, 1, g);//green
                wrstr.setSample(x, y, 2, b);//blue
                
            }
        }
        
        fin.close();
        
        String q = System.getProperty("user.home") + "/downloads/" + fImage.getName();
        bi.setData(wrstr);
        ImageIO.write(bi, "PNG", new File(q));
        
        return q;
    }
    
}
