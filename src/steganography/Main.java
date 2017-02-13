package steganography;

public class Main 
{
    public static void main(String[] args) 
    {
        try
        {
            Embed eb = new Embed("d:/Flowers.png", "d:/s.pdf");
            String f1 = eb.embedIt("tiger");
            Extract ex = new Extract(f1);
            String f2 = ex.extractIt("tiger");
            System.out.println("File extracted : " + f2);
                    
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}
