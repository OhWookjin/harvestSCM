package kr.co.hanulit.harvest.util;

import java.applet.Applet;

public final class ScmDesApplet extends Applet
{
	//protected static LogWriter logger = LoggingService.getLogger(ScmDesApplet.class);
	
    public ScmDesApplet()
    {
        m_EncryptKn1 = new int[32];
        m_DecryptKn1 = new int[32];
        m_EncryptKn2 = new int[32];
        m_DecryptKn2 = new int[32];
        m_EncryptKn3 = new int[32];
        m_DecryptKn3 = new int[32];
        m_TableStatus = false;
    }

    static void main(String args[])
    {
//        ScmDesApplet ScmDesApplet = new ScmDesApplet();
        //String s = ScmDesApplet.enc("harvest", "109406992835873053");
    }

    public boolean setKey(byte abyte0[])
        throws Exception
    {
        if(abyte0.length != 24)
        {
            throw new Exception("Des.setKey(): key length must be 24 bytes");
        } else
        {
            resetKey(abyte0);
            m_TableStatus = true;
            return true;
        }
    }

    public boolean setKey(String s, String s1)
    {
        boolean flag = true;
        try
        {
            byte abyte0[] = ScmByteArray.stringToBinaryArray(s);
            byte abyte1[] = ScmByteArray.stringToBinaryArray(s1);
            byte abyte2[] = passphraseToKey(abyte0, abyte1);
            flag = setKey(abyte2);
        }
        catch(Exception exception)
        {
            flag = false;
        }
        return flag;
    }

    public String enc(String s, String s1)
    {
        setKey("a;sd9-98()*;lkKAS9:LKjS(n(*_", s1);
        return encrypt(s);
    }

    public String encrypt(String s)
    {
        String s1 = null;
        try
        {
            byte abyte0[] = ScmByteArray.stringToBinaryArray(s);
            abyte0 = ScmByteArray.paddingToMultipleOf8(abyte0);
            encrypt(abyte0);
            trace("c3:" + ScmByteArray.toString(abyte0));
            s1 = ScmByteArray.toString(abyte0);
        }
        catch(Exception exception)
        {
            s1 = null;
        }
        return s1;
    }

    private boolean encrypt(byte abyte0[])
        throws Exception
    {
        int i = 0;
        int j = abyte0.length;
        if(m_TableStatus)
        {
            byte byte0 = 8;
            if(j % byte0 != 0)
                throw new Exception("Des.encrypt(): length of Data must be in multiple of 8");
            DES3(D_ENCRYPT, abyte0, i, j);
        } else
        {
            throw new Exception("Des.encrypt(): please set key before call the encrypt");
        }
        return true;
    }

    byte[] passphraseToKey(byte abyte0[], byte abyte1[])
    {
        byte abyte2[] = new byte[24];
        int ai[] = {
            0
        };
        CrunchPassphrase(abyte0, abyte0.length, abyte1, abyte1.length, abyte2, abyte2.length, ai);
        return abyte2;
    }

    private int CrunchPassphrase(byte abyte0[], int i, byte abyte1[], int j, byte abyte2[], int k, int ai[])
    {
        ScmCASHA1 ScmCASHA1 = new ScmCASHA1();
        //byte abyte3[] = new byte[4];
        byte abyte4[] = new byte[20];
        ScmCASHA1.Update(abyte0, 0, i);
        ScmCASHA1.Update(abyte1, 0, j);
        ScmCASHA1.Final(abyte4, 0);
        ScmCAUtils.CRDWORDReverse(abyte4, 20);
        int l;
        for(l = 0; l + 20 <= k; l += 20)
            System.arraycopy(abyte4, 0, abyte2, l, 20);

        if(k % 20 != 0)
            System.arraycopy(abyte4, 0, abyte2, l, k % 20);
        ai[0] = (i * 100) / (k * 8);
        return 0;
    }

    private void cookey(int ai[], int ai1[])
    {
        int i = 0;
        //boolean flag = false;
        //boolean flag1 = false;
        int l = 0;
        for(int j = 0; j < 16; j++)
        {
            int k = l++;
            ai1[i] = (ai[k] & 0xfc0000) << 6;
            ai1[i] |= (ai[k] & 0xfc0) << 10;
            ai1[i] |= (ai[l] & 0xfc0000) >> 10;
            ai1[i++] |= (ai[l] & 0xfc0) >> 6;
            ai1[i] = (ai[k] & 0x3f000) << 12;
            ai1[i] |= (ai[k] & 0x3f) << 16;
            ai1[i] |= (ai[l] & 0x3f000) >> 4;
            ai1[i++] |= ai[l] & 0x3f;
            l++;
        }

    }

    private void deskey(byte abyte0[], int i, byte byte0, int ai[])
    {
        byte abyte1[] = new byte[56];
        byte abyte2[] = new byte[56];
        int ai1[] = new int[32];
        for(int k = 0; k < 56; k++)
        {
            byte byte1 = pc1[k];
            short word1 = (short)(byte1 & 7);
            abyte1[k] = (byte)((abyte0[(byte1 >>> 3) + i] & 0xff & bytebit[word1]) == 0 ? 0 : 1);
        }

        for(int j = 0; j < 16; j++)
        {
            int l1;
            if(byte0 == D_DECRYPT)
                l1 = 15 - j << 1 & 0xffff;
            else
                l1 = j << 1 & 0xffff;
            int i2 = l1 + 1 & 0xffff;
            ai1[l1] = ai1[i2] = 0;
            for(int l = 0; l < 28; l++)
            {
                int k1 = l + totrot[j] & 0xffff;
                if(k1 < 28)
                    abyte2[l] = abyte1[k1];
                else
                    abyte2[l] = abyte1[k1 - 28];
            }

            for(int i1 = 28; i1 < 56; i1++)
            {
                short word0 = (short)(i1 + totrot[j]);
                if(word0 < 56)
                    abyte2[i1] = abyte1[word0];
                else
                    abyte2[i1] = abyte1[word0 - 28];
            }

            for(int j1 = 0; j1 < 24; j1++)
            {
                if(abyte2[pc2[j1]] != 0)
                    ai1[l1] |= bigbyte[j1];
                if(abyte2[pc2[j1 + 24]] != 0)
                    ai1[i2] |= bigbyte[j1];
            }

        }

        cookey(ai1, ai);
    }

    private void scrunch(byte abyte0[], int i, int ai[])
    {
        int j = i;
        ai[0] = (abyte0[j++] & 0xff) << 24;
        ai[0] |= (abyte0[j++] & 0xff) << 16;
        ai[0] |= (abyte0[j++] & 0xff) << 8;
        ai[0] |= abyte0[j++] & 0xff;
        ai[1] = (abyte0[j++] & 0xff) << 24;
        ai[1] |= (abyte0[j++] & 0xff) << 16;
        ai[1] |= (abyte0[j++] & 0xff) << 8;
        ai[1] |= abyte0[j] & 0xff;
    }

    private void unscrun(int ai[], byte abyte0[], int i)
    {
        int j = i;
        abyte0[j++] = (byte)(ai[0] >>> 24 & 0xff);
        abyte0[j++] = (byte)(ai[0] >>> 16 & 0xff);
        abyte0[j++] = (byte)(ai[0] >>> 8 & 0xff);
        abyte0[j++] = (byte)(ai[0] & 0xff);
        abyte0[j++] = (byte)(ai[1] >>> 24 & 0xff);
        abyte0[j++] = (byte)(ai[1] >>> 16 & 0xff);
        abyte0[j++] = (byte)(ai[1] >>> 8 & 0xff);
        abyte0[j] = (byte)(ai[1] & 0xff);
    }

    private void desfunc(int ai[], int ai1[])
    {
        int j1 = 0;
        int k1 = 0;
        int l = ai[0];
        int k = ai[1];
        int j = (l >>> 4 ^ k) & 0xf0f0f0f;
        k ^= j;
        l ^= j << 4;
        j = (l >>> 16 ^ k) & 0xffff;
        k ^= j;
        l ^= j << 16;
        j = (k >>> 2 ^ l) & 0x33333333;
        l ^= j;
        k ^= j << 2;
        j = (k >>> 8 ^ l) & 0xff00ff;
        l ^= j;
        k ^= j << 8;
        k = (k << 1 | k >>> 31 & 1) & -1;
        j = (l ^ k) & 0xaaaaaaaa;
        l ^= j;
        k ^= j;
        l = (l << 1 | l >>> 31 & 1) & -1;
        for(int i1 = 0; i1 < 8; i1++)
        {
            j = k << 28 | k >>> 4;
            j ^= ai1[j1++];
            int i = SP7[j & 0x3f];
            i |= SP5[j >>> 8 & 0x3f];
            i |= SP3[j >>> 16 & 0x3f];
            i |= SP1[j >>> 24 & 0x3f];
            j = k ^ ai1[j1++];
            i |= SP8[j & 0x3f];
            i |= SP6[j >>> 8 & 0x3f];
            i |= SP4[j >>> 16 & 0x3f];
            i |= SP2[j >>> 24 & 0x3f];
            l ^= i;
            j = l << 28 | l >>> 4;
            j ^= ai1[j1++];
            i = SP7[j & 0x3f];
            i |= SP5[j >>> 8 & 0x3f];
            i |= SP3[j >>> 16 & 0x3f];
            i |= SP1[j >>> 24 & 0x3f];
            j = l ^ ai1[j1++];
            i |= SP8[j & 0x3f];
            i |= SP6[j >>> 8 & 0x3f];
            i |= SP4[j >>> 16 & 0x3f];
            i |= SP2[j >>> 24 & 0x3f];
            k ^= i;
        }

        k = k << 31 | k >>> 1;
        j = (l ^ k) & 0xaaaaaaaa;
        l ^= j;
        k ^= j;
        l = l << 31 | l >>> 1;
        j = (l >>> 8 ^ k) & 0xff00ff;
        k ^= j;
        l ^= j << 8;
        j = (l >>> 2 ^ k) & 0x33333333;
        k ^= j;
        l ^= j << 2;
        j = (k >>> 16 ^ l) & 0xffff;
        l ^= j;
        k ^= j << 16;
        j = (k >>> 4 ^ l) & 0xf0f0f0f;
        l ^= j;
        k ^= j << 4;
        ai[k1++] = k;
        ai[k1] = l;
    }

    private void DES3(byte byte0, byte abyte0[], int i, int j)
    {
        int ai[] = new int[2];
        if(byte0 == D_ENCRYPT)
        {
            for(int k = 0; k < j; k += 8)
            {
                scrunch(abyte0, i + k, ai);
                desfunc(ai, m_EncryptKn1);
                desfunc(ai, m_EncryptKn2);
                desfunc(ai, m_EncryptKn3);
                unscrun(ai, abyte0, i + k);
            }

        } else
        {
            for(int l = 0; l < j; l += 8)
            {
                scrunch(abyte0, i + l, ai);
                desfunc(ai, m_DecryptKn1);
                desfunc(ai, m_DecryptKn2);
                desfunc(ai, m_DecryptKn3);
                unscrun(ai, abyte0, i + l);
            }

        }
    }

    private void resetKey(byte abyte0[])
    {
        m_TableStatus = false;
        fill(m_EncryptKn1, 0);
        fill(m_DecryptKn1, 0);
        fill(m_EncryptKn2, 0);
        fill(m_DecryptKn2, 0);
        fill(m_EncryptKn3, 0);
        fill(m_DecryptKn3, 0);
        deskey(abyte0, 0, D_ENCRYPT, m_EncryptKn1);
        deskey(abyte0, 16, D_DECRYPT, m_DecryptKn1);
        deskey(abyte0, 8, D_DECRYPT, m_EncryptKn2);
        deskey(abyte0, 8, D_ENCRYPT, m_DecryptKn2);
        deskey(abyte0, 16, D_ENCRYPT, m_EncryptKn3);
        deskey(abyte0, 0, D_DECRYPT, m_DecryptKn3);
    }
    
    /*
    private void ZapTable()
    {
        for(int i = 0; i < 32; i++)
        {
            m_EncryptKn1[i] = 0;
            m_DecryptKn1[i] = 0;
            m_EncryptKn2[i] = 0;
            m_DecryptKn2[i] = 0;
            m_EncryptKn3[i] = 0;
            m_DecryptKn3[i] = 0;
        }

    }
	private static void dumpScmByteArray(byte abyte0[])
    {
        dumpScmByteArray(abyte0, "");
    }

    private static void dumpScmByteArray(byte abyte0[], String s)
    {
        System.out.print(s + "{");
        for(int i = 0; i < abyte0.length; i++)
            System.out.print(abyte0[i] + " ");

        System.out.println("}");
    }

    private static String ScmByteArrayToString(byte abyte0[])
        throws NullPointerException
    {
        if(abyte0 == null)
            throw new NullPointerException("Des.ScmByteArrayToString(): parameter 'abyte' cannot be null");
        StringBuffer stringbuffer = new StringBuffer(abyte0.length * 4);
        for(int i = 0; i < abyte0.length; i++)
        {
            stringbuffer.append(Byte.toString(abyte0[i]));
            stringbuffer.append(" ");
        }

        return stringbuffer.toString();
    }
	*/
    private static void fill(byte abyte0[], byte byte0)
    {
        for(int i = 0; i < abyte0.length; i++)
            abyte0[i] = byte0;

    }

    private static void fill(int ai[], int i)
    {
        for(int j = 0; j < ai.length; j++)
            ai[j] = i;

    }

    public void Start()
    {
        try
        {
            enc("start", "start");
        }
        catch(Exception e) {
        	//logger.errorLog("", e);
        	trace(e.getMessage());
        }
    }

    void trace(String s)
    {
        //logger.systemLog(s);
    }

    private static final String CRYPT_PASSPHRASE = "a;sd9-98()*;lkKAS9:LKjS(n(*_";
    private static final String ms_strEncode = "ISO-8859-1";
    private static final int KEYLENGTH = 168;
    private static final int KEYBYTES = 21;
    private static final int BLOCK_LEN = 64;
    private static final String TITLE = "CA 3-DES 168 bit EDE";
    private static final int KEY_TABLE_SIZE = 24;
    private int m_EncryptKn1[];
    private int m_DecryptKn1[];
    private int m_EncryptKn2[];
    private int m_DecryptKn2[];
    private int m_EncryptKn3[];
    private int m_DecryptKn3[];
    private boolean m_TableStatus;
    private static byte D_ENCRYPT = 0;
    private static byte D_DECRYPT = 1;
    private static final byte bytebit[] = {
        -128, 64, 32, 16, 8, 4, 2, 1
    };
    private static final int bigbyte[] = {
        0x800000, 0x400000, 0x200000, 0x100000, 0x80000, 0x40000, 0x20000, 0x10000, 32768, 16384, 
        8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 
        8, 4, 2, 1
    };
    private static final byte pc1[] = {
        56, 48, 40, 32, 24, 16, 8, 0, 57, 49, 
        41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 
        26, 18, 10, 2, 59, 51, 43, 35, 62, 54, 
        46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 
        29, 21, 13, 5, 60, 52, 44, 36, 28, 20, 
        12, 4, 27, 19, 11, 3
    };
    private static final byte totrot[] = {
        1, 2, 4, 6, 8, 10, 12, 14, 15, 17, 
        19, 21, 23, 25, 27, 28
    };
    private static final byte pc2[] = {
        13, 16, 10, 23, 0, 4, 2, 27, 14, 5, 
        20, 9, 22, 18, 11, 3, 25, 7, 15, 6, 
        26, 19, 12, 1, 40, 51, 30, 36, 46, 54, 
        29, 39, 50, 44, 32, 47, 43, 48, 38, 55, 
        33, 52, 45, 41, 49, 35, 28, 31
    };
    private static final int SP1[] = {
        0x1010400, 0, 0x10000, 0x1010404, 0x1010004, 0x10404, 4, 0x10000, 1024, 0x1010400, 
        0x1010404, 1024, 0x1000404, 0x1010004, 0x1000000, 4, 1028, 0x1000400, 0x1000400, 0x10400, 
        0x10400, 0x1010000, 0x1010000, 0x1000404, 0x10004, 0x1000004, 0x1000004, 0x10004, 0, 1028, 
        0x10404, 0x1000000, 0x10000, 0x1010404, 4, 0x1010000, 0x1010400, 0x1000000, 0x1000000, 1024, 
        0x1010004, 0x10000, 0x10400, 0x1000004, 1024, 4, 0x1000404, 0x10404, 0x1010404, 0x10004, 
        0x1010000, 0x1000404, 0x1000004, 1028, 0x10404, 0x1010400, 1028, 0x1000400, 0x1000400, 0, 
        0x10004, 0x10400, 0, 0x1010004
    };
    private static final int SP2[] = {
        0x80108020, 0x80008000, 32768, 0x108020, 0x100000, 32, 0x80100020, 0x80008020, 0x80000020, 0x80108020, 
        0x80108000, 0x80000000, 0x80008000, 0x100000, 32, 0x80100020, 0x108000, 0x100020, 0x80008020, 0, 
        0x80000000, 32768, 0x108020, 0x80100000, 0x100020, 0x80000020, 0, 0x108000, 32800, 0x80108000, 
        0x80100000, 32800, 0, 0x108020, 0x80100020, 0x100000, 0x80008020, 0x80100000, 0x80108000, 32768, 
        0x80100000, 0x80008000, 32, 0x80108020, 0x108020, 32, 32768, 0x80000000, 32800, 0x80108000, 
        0x100000, 0x80000020, 0x100020, 0x80008020, 0x80000020, 0x100020, 0x108000, 0, 0x80008000, 32800, 
        0x80000000, 0x80100020, 0x80108020, 0x108000
    };
    private static final int SP3[] = {
        520, 0x8020200, 0, 0x8020008, 0x8000200, 0, 0x20208, 0x8000200, 0x20008, 0x8000008, 
        0x8000008, 0x20000, 0x8020208, 0x20008, 0x8020000, 520, 0x8000000, 8, 0x8020200, 512, 
        0x20200, 0x8020000, 0x8020008, 0x20208, 0x8000208, 0x20200, 0x20000, 0x8000208, 8, 0x8020208, 
        512, 0x8000000, 0x8020200, 0x8000000, 0x20008, 520, 0x20000, 0x8020200, 0x8000200, 0, 
        512, 0x20008, 0x8020208, 0x8000200, 0x8000008, 512, 0, 0x8020008, 0x8000208, 0x20000, 
        0x8000000, 0x8020208, 8, 0x20208, 0x20200, 0x8000008, 0x8020000, 0x8000208, 520, 0x8020000, 
        0x20208, 8, 0x8020008, 0x20200
    };
    private static final int SP4[] = {
        0x802001, 8321, 8321, 128, 0x802080, 0x800081, 0x800001, 8193, 0, 0x802000, 
        0x802000, 0x802081, 129, 0, 0x800080, 0x800001, 1, 8192, 0x800000, 0x802001, 
        128, 0x800000, 8193, 8320, 0x800081, 1, 8320, 0x800080, 8192, 0x802080, 
        0x802081, 129, 0x800080, 0x800001, 0x802000, 0x802081, 129, 0, 0, 0x802000, 
        8320, 0x800080, 0x800081, 1, 0x802001, 8321, 8321, 128, 0x802081, 129, 
        1, 8192, 0x800001, 8193, 0x802080, 0x800081, 8193, 8320, 0x800000, 0x802001, 
        128, 0x800000, 8192, 0x802080
    };
    private static final int SP5[] = {
        256, 0x2080100, 0x2080000, 0x42000100, 0x80000, 256, 0x40000000, 0x2080000, 0x40080100, 0x80000, 
        0x2000100, 0x40080100, 0x42000100, 0x42080000, 0x80100, 0x40000000, 0x2000000, 0x40080000, 0x40080000, 0, 
        0x40000100, 0x42080100, 0x42080100, 0x2000100, 0x42080000, 0x40000100, 0, 0x42000000, 0x2080100, 0x2000000, 
        0x42000000, 0x80100, 0x80000, 0x42000100, 256, 0x2000000, 0x40000000, 0x2080000, 0x42000100, 0x40080100, 
        0x2000100, 0x40000000, 0x42080000, 0x2080100, 0x40080100, 256, 0x2000000, 0x42080000, 0x42080100, 0x80100, 
        0x42000000, 0x42080100, 0x2080000, 0, 0x40080000, 0x42000000, 0x80100, 0x2000100, 0x40000100, 0x80000, 
        0, 0x40080000, 0x2080100, 0x40000100
    };
    private static final int SP6[] = {
        0x20000010, 0x20400000, 16384, 0x20404010, 0x20400000, 16, 0x20404010, 0x400000, 0x20004000, 0x404010, 
        0x400000, 0x20000010, 0x400010, 0x20004000, 0x20000000, 16400, 0, 0x400010, 0x20004010, 16384, 
        0x404000, 0x20004010, 16, 0x20400010, 0x20400010, 0, 0x404010, 0x20404000, 16400, 0x404000, 
        0x20404000, 0x20000000, 0x20004000, 16, 0x20400010, 0x404000, 0x20404010, 0x400000, 16400, 0x20000010, 
        0x400000, 0x20004000, 0x20000000, 16400, 0x20000010, 0x20404010, 0x404000, 0x20400000, 0x404010, 0x20404000, 
        0, 0x20400010, 16, 16384, 0x20400000, 0x404010, 16384, 0x400010, 0x20004010, 0, 
        0x20404000, 0x20000000, 0x400010, 0x20004010
    };
    private static final int SP7[] = {
        0x200000, 0x4200002, 0x4000802, 0, 2048, 0x4000802, 0x200802, 0x4200800, 0x4200802, 0x200000, 
        0, 0x4000002, 2, 0x4000000, 0x4200002, 2050, 0x4000800, 0x200802, 0x200002, 0x4000800, 
        0x4000002, 0x4200000, 0x4200800, 0x200002, 0x4200000, 2048, 2050, 0x4200802, 0x200800, 2, 
        0x4000000, 0x200800, 0x4000000, 0x200800, 0x200000, 0x4000802, 0x4000802, 0x4200002, 0x4200002, 2, 
        0x200002, 0x4000000, 0x4000800, 0x200000, 0x4200800, 2050, 0x200802, 0x4200800, 2050, 0x4000002, 
        0x4200802, 0x4200000, 0x200800, 0, 2, 0x4200802, 0, 0x200802, 0x4200000, 2048, 
        0x4000002, 0x4000800, 2048, 0x200002
    };
    private static final int SP8[] = {
        0x10001040, 4096, 0x40000, 0x10041040, 0x10000000, 0x10001040, 64, 0x10000000, 0x40040, 0x10040000, 
        0x10041040, 0x41000, 0x10041000, 0x41040, 4096, 64, 0x10040000, 0x10000040, 0x10001000, 4160, 
        0x41000, 0x40040, 0x10040040, 0x10041000, 4160, 0, 0, 0x10040040, 0x10000040, 0x10001000, 
        0x41040, 0x40000, 0x41040, 0x40000, 0x10041000, 4096, 64, 0x10040040, 4096, 0x41040, 
        0x10001000, 64, 0x10000040, 0x10040000, 0x10040040, 0x10000000, 0x40000, 0x10001040, 0, 0x10041040, 
        0x40040, 0x10000040, 0x10040000, 0x10001000, 0x10001040, 0, 0x10041040, 0x41000, 0x41000, 4160, 
        4160, 0x40040, 0x10000000, 0x10041000
    };

    /*private static int MAX_WEAK_KEY = 64;
    private static byte WeakKeys[][] = {
        {
            1, 1, 1, 1, 1, 1, 1, 1
        }, {
            -2, -2, -2, -2, -2, -2, -2, -2
        }, {
            31, 31, 31, 31, 31, 31, 31, 31
        }, {
            -32, -32, -32, -32, -32, -32, -32, -32
        }, {
            1, -2, 1, -2, 1, -2, 1, -2
        }, {
            31, -32, 31, -32, 14, -15, 14, -15
        }, {
            1, -32, 1, -32, 1, -15, 1, -15
        }, {
            31, -2, 31, -2, 14, -2, 14, -2
        }, {
            1, 31, 1, 31, 1, 14, 1, 14
        }, {
            -32, -2, -32, -2, -15, -2, -15, -2
        }, {
            -2, 1, -2, 1, -2, 1, -2, 1
        }, {
            -32, 31, -32, 31, -15, 14, -15, 14
        }, {
            -32, 1, -32, 1, -15, 1, -15, 1
        }, {
            -2, 31, -2, 31, -2, 14, -2, 14
        }, {
            31, 1, 31, 1, 14, 1, 14, 1
        }, {
            -2, -32, -2, -32, -2, -15, -2, -15
        }, {
            31, 31, 1, 1, 14, 14, 1, 1
        }, {
            1, 31, 31, 1, 1, 14, 14, 1
        }, {
            31, 1, 1, 31, 14, 1, 1, 14
        }, {
            1, 1, 31, 31, 1, 1, 14, 14
        }, {
            -32, -32, 1, 1, -15, -15, 1, 1
        }, {
            -2, -2, 1, 1, -2, -2, 1, 1
        }, {
            -2, -32, 31, 1, -2, -15, 14, 1
        }, {
            -32, -2, 31, 1, -15, -2, 14, 1
        }, {
            -2, -32, 1, 31, -2, -15, 1, 14
        }, {
            -32, -2, 1, 31, -15, -2, 1, 14
        }, {
            -32, -32, 31, 31, -15, -15, 14, 14
        }, {
            -2, -2, 31, 31, -2, -2, 14, 14
        }, {
            -2, 31, -32, 1, -2, 14, -15, 1
        }, {
            -32, 31, -2, 1, -15, 14, -2, 1
        }, {
            -2, 1, -32, 31, -2, 1, -15, 14
        }, {
            -32, 1, -2, 31, -15, 1, -2, 14
        }, {
            1, -32, -32, 1, 1, -15, -15, 1
        }, {
            31, -2, -32, 1, 14, -2, -16, 1
        }, {
            31, -32, -2, 1, 14, -15, -2, 1
        }, {
            1, -2, -2, 1, 1, -2, -2, 1
        }, {
            31, -32, -32, 31, 14, -15, -15, 14
        }, {
            1, -2, -32, 31, 1, -2, -15, 14
        }, {
            1, -32, -2, 31, 1, -15, -2, 14
        }, {
            31, -2, -2, 31, 14, -2, -2, 14
        }, {
            -32, 1, 1, -32, -15, 1, 1, -15
        }, {
            -2, 31, 1, -32, -2, 14, 1, -15
        }, {
            -2, 1, 31, -32, -2, 1, 14, -15
        }, {
            -32, 31, 31, -32, -15, 14, 14, -15
        }, {
            -2, 1, 1, -2, -2, 1, 1, -2
        }, {
            -32, 31, 1, -2, -15, 14, 1, -2
        }, {
            -32, 1, 31, -2, -15, 1, 14, -2
        }, {
            -2, 31, 31, -2, -2, 14, 14, -2
        }, {
            31, -2, 1, -32, 14, -2, 1, -15
        }, {
            1, -2, 31, -32, 1, -2, 14, -15
        }, {
            31, -32, 1, -2, 14, -15, 1, -2
        }, {
            1, -32, 31, -2, 1, -15, 14, -2
        }, {
            1, 1, -32, -32, 1, 1, -15, -15
        }, {
            31, 31, -32, -32, 14, 14, -15, -15
        }, {
            31, 1, -2, -32, 14, 1, -2, -15
        }, {
            1, 31, -2, -32, 1, 14, -2, -15
        }, {
            31, 1, -32, -2, 14, 1, -15, -2
        }, {
            1, 31, -32, -2, 1, 14, -15, -2
        }, {
            1, 1, -2, -2, 1, 1, -2, -2
        }, {
            31, 31, -2, -2, 14, 14, -2, -2
        }, {
            -2, -2, -32, -32, -2, -2, -15, -15
        }, {
            -32, -2, -2, -32, -15, -2, -2, -15
        }, {
            -2, -32, -32, -2, -2, -15, -15, -2
        }, {
            -32, -32, -2, -2, -15, -15, -2, -2
        }
    };*/

}
