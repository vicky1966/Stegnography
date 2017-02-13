package steganography;

public class ByteManager 
{
    String password;
    int counter, max;
    
    ByteManager(String x)
    {
        setPassword(x);
    }
    
    void setPassword(String x)
    {
        password = x;
        max =password.length();
        counter = -1;
    }
    
    int getEncrypted(int b)
    {
        counter = (counter +1) % max;
        return  b ^ password.charAt(counter);
    }
    
    int getDecrypted(int b)
    {
        counter = (counter +1) % max;
        return  b ^ password.charAt(counter);
    }
    
    
    
    int[] breakThebyte(int x)
    {//111 101 10
        int arr[] = new int[3];
        arr[2] = x&0x3;
        arr[1] = x>>2&0x7;
        arr[0] = x>>5&0x7;
        return arr;
    }
    
    int formTheByte(int arr[])
    {//111 101 10
        int q;
        q = arr[0]<<3;
        q = (q | arr[1])<<2;
        q = q | arr[2];
        return q;
    }
}
