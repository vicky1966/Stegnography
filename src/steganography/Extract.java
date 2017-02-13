package steganography;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import javax.imageio.ImageIO;

public class Extract 
{
    File sImage;
    
    public Extract(String imgName) throws Exception
    {
        sImage = new File(imgName);
        if(!sImage.exists() )
            throw new Exception("Image " + imgName +"Not Found");
    }//Extract
    
    String extractIt(String password) throws Exception
    {
        BufferedImage bi;
        bi = ImageIO.read(sImage);
        
        Raster rstr = bi.getData();
        int w, h;
        int x, y;
        
        w = bi.getWidth();
        h = bi.getHeight();
        
        ByteManager bm = new ByteManager(password);
        
        int i =0;
        int r, g, b;
        int arr[] = new int[3];
        int data=0;
        long fileSize = 1;
        String fileName = "";
        String header = "";
        FileOutputStream fout = null;
        
        //pixel reading
        for(y = 0; y < h && fileSize > 0; y++)
        {
            for(x = 0; x < w && fileSize >0; x++)
            {//rowwise
               
                //read the pixel bands
                r = rstr.getSample(x, y, 0);
                g = rstr.getSample(x, y, 1);
                b = rstr.getSample(x, y, 2);
                
                //fetch the bits
                arr[0] = r&0x7;
                arr[1] = g&0x7;
                arr[2] = b&0x3;

                data = bm.formTheByte(arr);
                data = bm.getDecrypted(data);
                
                if(i < HeaderManager.HEADER_LENGTH)
                {
                    header = header + (char) data;
                    i++;
                    if(i == HeaderManager.HEADER_LENGTH)
                    {
                        try
                        {
                            fileSize = HeaderManager.getFileSize(header);
                            fileName = HeaderManager.getFileName(header);
                            fileName = System.getProperty("user.home") + "/downloads/" + fileName;
                            fout = new FileOutputStream(fileName);
                        }   
                        catch(Exception ex)
                        {//header is corrupt
                            fileSize = -1;
                            break;
                        }
                    }//nested if
                }//if
                else 
                {//file Writing
                    fout.write(data);
                    fileSize--;
                }
            }//for(x
        }//for(y
        
        fout.flush();
        fout.close();
        
        if(fileSize== -1)
            return null; //failure in extract
        else
            return fileName;
    }//extractIt
}
