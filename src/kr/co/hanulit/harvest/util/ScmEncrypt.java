package kr.co.hanulit.harvest.util;
public class ScmEncrypt
{

    public ScmEncrypt()
    {
    }

    public static String encrypt(String s)
    {
        String s2 = null;
        try
        {
        	ScmDes des = new ScmDes();
            des.setKey("a;sd9-98()*;lkKAS9:LKjS(n(*_", HAR_ENCRYPT_KEY);
            s2 = des.encrypt(s);
        }
        catch(Exception exception)
        {
            System.err.println("harweb.Password.encrypt(): get exception --" + exception.getMessage());
        }
        return s2;
    }

    public static String decrypt(String s)
    {
        String s2 = null;
        try
        {
            ScmDes des = new ScmDes();
            des.setKey("a;sd9-98()*;lkKAS9:LKjS(n(*_", HAR_ENCRYPT_KEY);
            s2 = des.decrypt(s);
        }
        catch(Exception exception)
        {
            System.err.println("harweb.Passowrd.decrypt(): get exception --" + exception.getMessage());
        }
        return s2;
    }
    /*
    private static void trace(String s)
    {
//        System.out.println("harweb.ScmEncrypt: " + s);
    }
	*/
    private static final String CRYPT_PASSPHRASE = "a;sd9-98()*;lkKAS9:LKjS(n(*_";
	private static final String HAR_ENCRYPT_KEY = "-90q2w;kjsadf-903r;lkjjsdv-0-zscx0v"; 
}

