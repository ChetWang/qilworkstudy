package nci.gps.util;

/**
 * @���ܣ�java String����ֽ����ַ�֮���ת��
 * @ʱ�䣺2008-07-21
 * @author ZHOUHM
 */
public class CharCoding {

    /**
     * �ַ���ת����ʮ������ֵ
     * @param bin String �����ַ���
     * @return String ʮ�������ַ���
     */
    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    /**
     * ʮ������ת���ַ���
     * @param hex String ʮ�������ַ���
     * @return String �����ַ���
     */
    public static String hex2bin(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    /** 
     * java�ֽ���ת�ַ��� 
     * @param b byte[] �����ֽ���
     * @return String ʮ�������ַ���
     */
    public static String byte2hex(byte[] b) { //һ���ֽڵ�����
        // ת��16�����ַ���
        StringBuffer hs = new StringBuffer();
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            //����ת��ʮ�����Ʊ�ʾ
            tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                hs.append("0").append(tmp);
            } else {
                 hs.append(tmp);
            }
        }
        tmp = null;
        return hs.toString().toUpperCase(); //ת�ɴ�д
    }

    /**
     * �ַ���תjava�ֽ���
     * @param b
     * @return
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("���Ȳ���ż��");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            // ��λһ�飬��ʾһ���ֽ�,��������ʾ��16�����ַ�������ԭ��һ�������ֽ�
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        b = null;
        return b2;
    }
}