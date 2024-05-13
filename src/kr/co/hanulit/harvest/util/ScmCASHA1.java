package kr.co.hanulit.harvest.util;

final class ScmCASHA1
{

    private static final int f1(int i, int j, int k)
    {
        return k ^ i & (j ^ k);
    }

    private static final int f2(int i, int j, int k)
    {
        return i ^ j ^ k;
    }

    private static final int f3(int i, int j, int k)
    {
        return (i & j) + (k & (i ^ j));
    }

    private static final int f4(int i, int j, int k)
    {
        return i ^ j ^ k;
    }

    private static final int ROTL(int i, int j)
    {
        return j << i | j >> 32 - i & (1 << i) - 1;
    }

    private static final int Expand(int ai[], int i)
    {
        int j = ai[i & 0xf] = ROTL(1, ai[i & 0xf] ^ ai[i - 14 & 0xf] ^ ai[i - 8 & 0xf] ^ ai[i - 3 & 0xf]);
        return j;
    }

    private static final void SubRound1(int ai[], int ai1[], int ai2[], int ai3[], int ai4[], int i)
    {
        ai4[0] = ai4[0] + ROTL(5, ai[0]) + f1(ai1[0], ai2[0], ai3[0]) + 0x5a827999 + i;
        ai1[0] = ROTL(30, ai1[0]);
    }

    private static final void SubRound2(int ai[], int ai1[], int ai2[], int ai3[], int ai4[], int i)
    {
        ai4[0] = ai4[0] + ROTL(5, ai[0]) + f2(ai1[0], ai2[0], ai3[0]) + 0x6ed9eba1 + i;
        ai1[0] = ROTL(30, ai1[0]);
    }

    private static final void SubRound3(int ai[], int ai1[], int ai2[], int ai3[], int ai4[], int i)
    {
        ai4[0] = ai4[0] + ROTL(5, ai[0]) + f3(ai1[0], ai2[0], ai3[0]) + 0x8f1bbcdc + i;
        ai1[0] = ROTL(30, ai1[0]);
    }

    private static final void SubRound4(int ai[], int ai1[], int ai2[], int ai3[], int ai4[], int i)
    {
        ai4[0] = ai4[0] + ROTL(5, ai[0]) + f4(ai1[0], ai2[0], ai3[0]) + 0xca62c1d6 + i;
        ai1[0] = ROTL(30, ai1[0]);
    }

    private void AdjustInputByteOrder(int i)
    {
        int j = 0;
        for(int k = 0; k < i / 4; k++)
        {
            m_data[k] = m_dataBytes[j++] & 0xff;
            m_data[k] = m_data[k] << 8 | m_dataBytes[j++] & 0xff;
            m_data[k] = m_data[k] << 8 | m_dataBytes[j++] & 0xff;
            m_data[k] = m_data[k] << 8 | m_dataBytes[j++] & 0xff;
        }

    }

    private void Transform()
    {
        A[0] = m_digest[0];
        B[0] = m_digest[1];
        C[0] = m_digest[2];
        D[0] = m_digest[3];
        E[0] = m_digest[4];
        for(int i = 0; i < 16; i++)
            m_eData[i] = m_data[i];

        SubRound1(A, B, C, D, E, m_eData[0]);
        SubRound1(E, A, B, C, D, m_eData[1]);
        SubRound1(D, E, A, B, C, m_eData[2]);
        SubRound1(C, D, E, A, B, m_eData[3]);
        SubRound1(B, C, D, E, A, m_eData[4]);
        SubRound1(A, B, C, D, E, m_eData[5]);
        SubRound1(E, A, B, C, D, m_eData[6]);
        SubRound1(D, E, A, B, C, m_eData[7]);
        SubRound1(C, D, E, A, B, m_eData[8]);
        SubRound1(B, C, D, E, A, m_eData[9]);
        SubRound1(A, B, C, D, E, m_eData[10]);
        SubRound1(E, A, B, C, D, m_eData[11]);
        SubRound1(D, E, A, B, C, m_eData[12]);
        SubRound1(C, D, E, A, B, m_eData[13]);
        SubRound1(B, C, D, E, A, m_eData[14]);
        SubRound1(A, B, C, D, E, m_eData[15]);
        SubRound1(E, A, B, C, D, Expand(m_eData, 16));
        SubRound1(D, E, A, B, C, Expand(m_eData, 17));
        SubRound1(C, D, E, A, B, Expand(m_eData, 18));
        SubRound1(B, C, D, E, A, Expand(m_eData, 19));
        SubRound2(A, B, C, D, E, Expand(m_eData, 20));
        SubRound2(E, A, B, C, D, Expand(m_eData, 21));
        SubRound2(D, E, A, B, C, Expand(m_eData, 22));
        SubRound2(C, D, E, A, B, Expand(m_eData, 23));
        SubRound2(B, C, D, E, A, Expand(m_eData, 24));
        SubRound2(A, B, C, D, E, Expand(m_eData, 25));
        SubRound2(E, A, B, C, D, Expand(m_eData, 26));
        SubRound2(D, E, A, B, C, Expand(m_eData, 27));
        SubRound2(C, D, E, A, B, Expand(m_eData, 28));
        SubRound2(B, C, D, E, A, Expand(m_eData, 29));
        SubRound2(A, B, C, D, E, Expand(m_eData, 30));
        SubRound2(E, A, B, C, D, Expand(m_eData, 31));
        SubRound2(D, E, A, B, C, Expand(m_eData, 32));
        SubRound2(C, D, E, A, B, Expand(m_eData, 33));
        SubRound2(B, C, D, E, A, Expand(m_eData, 34));
        SubRound2(A, B, C, D, E, Expand(m_eData, 35));
        SubRound2(E, A, B, C, D, Expand(m_eData, 36));
        SubRound2(D, E, A, B, C, Expand(m_eData, 37));
        SubRound2(C, D, E, A, B, Expand(m_eData, 38));
        SubRound2(B, C, D, E, A, Expand(m_eData, 39));
        SubRound3(A, B, C, D, E, Expand(m_eData, 40));
        SubRound3(E, A, B, C, D, Expand(m_eData, 41));
        SubRound3(D, E, A, B, C, Expand(m_eData, 42));
        SubRound3(C, D, E, A, B, Expand(m_eData, 43));
        SubRound3(B, C, D, E, A, Expand(m_eData, 44));
        SubRound3(A, B, C, D, E, Expand(m_eData, 45));
        SubRound3(E, A, B, C, D, Expand(m_eData, 46));
        SubRound3(D, E, A, B, C, Expand(m_eData, 47));
        SubRound3(C, D, E, A, B, Expand(m_eData, 48));
        SubRound3(B, C, D, E, A, Expand(m_eData, 49));
        SubRound3(A, B, C, D, E, Expand(m_eData, 50));
        SubRound3(E, A, B, C, D, Expand(m_eData, 51));
        SubRound3(D, E, A, B, C, Expand(m_eData, 52));
        SubRound3(C, D, E, A, B, Expand(m_eData, 53));
        SubRound3(B, C, D, E, A, Expand(m_eData, 54));
        SubRound3(A, B, C, D, E, Expand(m_eData, 55));
        SubRound3(E, A, B, C, D, Expand(m_eData, 56));
        SubRound3(D, E, A, B, C, Expand(m_eData, 57));
        SubRound3(C, D, E, A, B, Expand(m_eData, 58));
        SubRound3(B, C, D, E, A, Expand(m_eData, 59));
        SubRound4(A, B, C, D, E, Expand(m_eData, 60));
        SubRound4(E, A, B, C, D, Expand(m_eData, 61));
        SubRound4(D, E, A, B, C, Expand(m_eData, 62));
        SubRound4(C, D, E, A, B, Expand(m_eData, 63));
        SubRound4(B, C, D, E, A, Expand(m_eData, 64));
        SubRound4(A, B, C, D, E, Expand(m_eData, 65));
        SubRound4(E, A, B, C, D, Expand(m_eData, 66));
        SubRound4(D, E, A, B, C, Expand(m_eData, 67));
        SubRound4(C, D, E, A, B, Expand(m_eData, 68));
        SubRound4(B, C, D, E, A, Expand(m_eData, 69));
        SubRound4(A, B, C, D, E, Expand(m_eData, 70));
        SubRound4(E, A, B, C, D, Expand(m_eData, 71));
        SubRound4(D, E, A, B, C, Expand(m_eData, 72));
        SubRound4(C, D, E, A, B, Expand(m_eData, 73));
        SubRound4(B, C, D, E, A, Expand(m_eData, 74));
        SubRound4(A, B, C, D, E, Expand(m_eData, 75));
        SubRound4(E, A, B, C, D, Expand(m_eData, 76));
        SubRound4(D, E, A, B, C, Expand(m_eData, 77));
        SubRound4(C, D, E, A, B, Expand(m_eData, 78));
        SubRound4(B, C, D, E, A, Expand(m_eData, 79));
        m_digest[0] = m_digest[0] + A[0];
        m_digest[1] = m_digest[1] + B[0];
        m_digest[2] = m_digest[2] + C[0];
        m_digest[3] = m_digest[3] + D[0];
        m_digest[4] = m_digest[4] + E[0];
    }

    ScmCASHA1()
    {
        A = new int[1];
        B = new int[1];
        C = new int[1];
        D = new int[1];
        E = new int[1];
        m_finalAlreadyCalled = false;
        m_bitCount = 0L;
        m_digest = new int[5];
        m_data = new int[16];
        m_eData = new int[16];
        m_dataBytes = new byte[64];
        m_digest[0] = 0x67452301;
        m_digest[1] = 0xefcdab89;
        m_digest[2] = 0x98badcfe;
        m_digest[3] = 0x10325476;
        m_digest[4] = 0xc3d2e1f0;
    }

    void Update(byte abyte0[], int i, int j)
    {
        int l = 0;
        int i1 = i;
        if(i + j > abyte0.length)
            throw new ArrayIndexOutOfBoundsException();
        l = j;
        int k = (int)(m_bitCount >> 3 & 63L);
        m_bitCount += l * 8;
        if(k > 0)
        {
            int j1 = k;
            k = 64 - k;
            if(l < k)
            {
                System.arraycopy(abyte0, i1, m_dataBytes, j1, l);
                return;
            }
            System.arraycopy(abyte0, i1, m_dataBytes, j1, k);
            AdjustInputByteOrder(64);
            Transform();
            i1 += k;
            l -= k;
        }
        for(; l >= 64; l -= 64)
        {
            System.arraycopy(abyte0, i1, m_dataBytes, 0, 64);
            AdjustInputByteOrder(64);
            Transform();
            i1 += 64;
        }

        System.arraycopy(abyte0, i1, m_dataBytes, 0, l);
    }

    void Final(byte abyte0[], int i)
    {
        if(abyte0 == null || abyte0.length - i >= 20)
        {
            if(!m_finalAlreadyCalled)
            {
                int j = (int)(m_bitCount >> 3 & 63L);
                int l = 0;
                m_finalAlreadyCalled = true;
                l = j;
                m_dataBytes[l++] = -128;
                j = 63 - j;
                if(j < 8)
                {
                    ScmCAUtils.ClearByteArray(m_dataBytes, l, j);
                    AdjustInputByteOrder(64);
                    Transform();
                    ScmCAUtils.ClearByteArray(m_dataBytes, 0, 56);
                } else
                {
                    ScmCAUtils.ClearByteArray(m_dataBytes, l, j - 8);
                }
                ScmCAUtils.EncodeBig_int((int)(m_bitCount >> 32), m_dataBytes, 56);
                ScmCAUtils.EncodeBig_int((int)(m_bitCount & -1L), m_dataBytes, 60);
                AdjustInputByteOrder(64);
                Transform();
            }
            if(abyte0 != null)
            {
                for(int k = 0; k < 5; k++)
                    ScmCAUtils.EncodeLittle_int(m_digest[k], abyte0, i + k * 4);

            }
        }
    }

    void RawTransform(byte abyte0[], byte abyte1[])
    {
        int l = 0;
        for(int i = 0; i < 5; i++)
            m_digest[i] = ScmCAUtils.DecodeBig_int(abyte0, i * 4);

        for(int j = 0; j < 16; j++)
        {
            m_data[j] = abyte1[l++] & 0xff;
            m_data[j] = abyte1[j] << 8 | abyte1[l++] & 0xff;
            m_data[j] = abyte1[j] << 8 | abyte1[l++] & 0xff;
            m_data[j] = abyte1[j] << 8 | abyte1[l++] & 0xff;
        }

        Transform();
        for(int k = 0; k < 5; k++)
            ScmCAUtils.EncodeLittle_int(m_digest[k], abyte0, k * 4);

    }

    static final int SHA_DATASIZE = 64;
    static final int SHA_DIGESTSIZE = 20;
    static final int K1 = 0x5a827999;
    static final int K2 = 0x6ed9eba1;
    static final int K3 = 0x8f1bbcdc;
    static final int K4 = 0xca62c1d6;
    private int A[];
    private int B[];
    private int C[];
    private int D[];
    private int E[];
    private long m_bitCount;
    private int m_digest[];
    private int m_data[];
    private int m_eData[];
    private byte m_dataBytes[];
    private boolean m_finalAlreadyCalled;
}

