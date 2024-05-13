package kr.co.hanulit.harvest.util;

final class ScmCAUtils
{
    static final void ClearByteArray(byte abyte0[], int i, int j)
    {
        for(int k = 0; k < j; k++)
            abyte0[i + k] = 0;

    }

    static final void EncodeBig_int(int i, byte abyte0[], int j)
    {
        for(int k = 0; k < 4; k++)
        {
            abyte0[(j + 3) - k] = (byte)(i & 0xff);
            i >>>= 8;
        }

    }

    static final int DecodeBig_int(byte abyte0[], int i)
    {
        int j = 0;
        for(int k = 0; k < 4; k++)
            j = j << 8 | abyte0[i++] & 0xff;

        return j;
    }

    static final void EncodeLittle_int(int i, byte abyte0[], int j)
    {
        for(int k = 0; k < 4; k++)
        {
            abyte0[j + k] = (byte)(i & 0xff);
            i >>>= 8;
        }

    }

    static int DecodeLittle_int(byte abyte0[], int i)
    {
        int j = 0;
        for(int k = 0; k < 4; k++)
            j = j << 8 | abyte0[3 - i++] & 0xff;

        return j;
    }

    static final void CRDWORDReverse(byte abyte0[], int i)
    {
        byte abyte1[] = new byte[4];
        for(int j = 0; j < i;)
        {
            int k = j;
            for(int l = 0; l < 4; l++)
                abyte1[3 - l] = abyte0[j++];

            System.arraycopy(abyte1, 0, abyte0, k, 4);
        }

    }

    private static char NibbleToASCII(byte byte0)
    {
        char c;
        if(byte0 <= 9)
            c = (char)(byte0 + 48);
        else
            c = (char)((byte0 + 65) - 10);
        return c;
    }

    static final String HexDump(byte abyte0[])
    {
        String s = new String("");
        for(int i = 0; i < abyte0.length; i++)
        {
            s = s + NibbleToASCII((byte)((abyte0[i] & 0xff) >>> 4));
            s = s + NibbleToASCII((byte)(abyte0[i] & 0xf));
            s = s + ' ';
        }

        return s;
    }

    static final boolean ByteArraysIdentical(byte abyte0[], byte abyte1[], int i)
    {
        boolean flag = true;
        int j = i != 0 ? i : abyte0.length;
        int k = i != 0 ? i : abyte1.length;
        if(j == k)
        {
            for(int l = 0; l < j && flag; l++)
                if((abyte0[l] & 0xff) != (abyte1[l] & 0xff))
                    flag = false;

        } else
        {
            flag = false;
        }
        return flag;
    }

    static final boolean IS_FLAG_ON(int i, int j)
    {
        return (i & j) == j;
    }
}

