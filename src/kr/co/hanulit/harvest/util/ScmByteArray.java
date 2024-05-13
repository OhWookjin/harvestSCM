package kr.co.hanulit.harvest.util;

public class ScmByteArray
{
	//protected static LogWriter logger = LoggingService.getLogger(ScmByteArray.class);

    public static void main(String args[])
    {
        byte abyte0[] = genScmByteArray();
        toString(abyte0);
        String s = toString(abyte0);
        //System.out.println(s);
        byte abyte1[] = fromString(s);
        toString(abyte1);
    }

    private static byte[] genScmByteArray()
    {
        return (new byte[] {
            1, 0, 2, 0, 127, 0, 1, -1, -128, -55
        });
    }

    public static String toString(byte abyte0[])
    {
        return toString(abyte0, abyte0.length);
    }

    public static String toString(byte abyte0[], int i)
    {
        StringBuffer stringbuffer = new StringBuffer(100);
        stringbuffer.append(i).append(",");
        for(int j = 0; j < i; j++)
        {
            stringbuffer.append(Byte.toString(abyte0[j]));
            stringbuffer.append(",");
        }

        return stringbuffer.toString();
    }

    public static byte[] fromString(String s)
        throws NumberFormatException
    {
        int i = s.indexOf(",");
        int j = Integer.parseInt(s.substring(0, i++));
        byte abyte0[] = new byte[j];
        for(int k = 0; k < j; k++)
        {
            int l = s.indexOf(",", i);
            String s1 = s.substring(i, l);
            i = ++l;
            abyte0[k] = Byte.parseByte(s1);
        }

        return abyte0;
    }

    public static byte[] charToBytes(char c)
    {
        byte abyte0[] = new byte[2];
        abyte0[0] = (byte)(c & 0xff);
        abyte0[1] = (byte)(c >> 8);
        if(abyte0[1] == 0)
            abyte0[1] = -1;
        return abyte0;
    }

    public static char bytesToChar(byte byte0, byte byte1)
    {
        byte byte2 = byte0 != -1 ? byte0 : 0;
        return (char)(byte2 << 8 | byte1);
    }

    public static byte[] stringToBinaryArray(String s)
    {
        byte abyte0[] = new byte[s.length() * 2];
        for(int i = 0; i < s.length(); i++)
        {
            byte abyte1[] = charToBytes(s.charAt(i));
            int j = i << 1;
            abyte0[j] = abyte1[1];
            abyte0[j + 1] = abyte1[0];
        }

        return abyte0;
    }

    public static String binaryArrayToString(byte abyte0[])
    {
        String s = null;
        int i = abyte0.length / 2;
        if(i > 0)
        {
            StringBuffer stringbuffer = new StringBuffer(i);
            for(int j = 0; j < i; j++)
            {
                char c = bytesToChar(abyte0[j << 1], abyte0[(j << 1) + 1]);
                if(c != 0)
                    stringbuffer.append(c);
            }

            s = stringbuffer.toString();
        } else
        {
            s = "";
        }
        return s;
    }

    public static byte[] paddingToMultipleOf8(byte abyte0[])
    {
        int i = abyte0.length;
        byte byte0 = 8;
        int j = i % byte0;
        int k = i + (j != 0 ? byte0 - j : 0);
        byte abyte1[] = new byte[k];
        for(int l = 0; l < k; l++)
            abyte1[l] = l >= i ? 0 : abyte0[l];

        return abyte1;
    }

    public static void dump(byte abyte0[])
    {
        //System.out.print("Array[" + abyte0.length + "]:");
        for(int i = 0; i < abyte0.length; i++)
        	trace(abyte0[i] + ",");

        //System.out.println();
    }

    static void trace(String s)
    {
        //logger.systemLog(s);
    }
}