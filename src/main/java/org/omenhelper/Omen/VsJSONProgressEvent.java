package org.omenhelper.Omen;

import org.apache.commons.codec.binary.Base64;
import org.omenhelper.Utils.SignatureUtils;

import java.nio.charset.StandardCharsets;

/**
 * @Author jiyec
 * @Date 2021/5/26 18:47
 * @Version 1.0
 **/
public class VsJSONProgressEvent {
    public String eventName;

    public String startedAt;

    public String endedAt;

    public Integer value;

    public VsJSONProgressEvent(String eventName, String startedAt, String endedAt, Integer value) {
        this.eventName = eventName;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.value = value;
    }

    private byte[] GetSignableText()
    {
        String text = this.eventName + this.startedAt + this.endedAt + this.value.toString();
        // char[] array = (text).toCharArray();
        // System.out.println("text --- " + HexUtil.byte2HexStr(text.getBytes(StandardCharsets.UTF_8)));
        // byte[] array2 = new byte[array.length];
        // for (int i = 0; i < array.length; i++)
        // {
        //     array2[i] = (byte)array[i];
        // }
        // return array2;
        return text.getBytes(StandardCharsets.UTF_8);
    }
    private static byte[] UUIDtoByteArray(String _str_uuid)
    {
        String text = _str_uuid.replace("-", "");
        int length = text.length();
        int num = text.length() / 2;
        byte[] array = new byte[num];
        for (int i = 0; i < num; i++)
        {
            // ToByte([包含要转换的数字的字符串]，[value 中数字的基数，它必须是 2、8、10 或 16])
            // 与 value 中数字等效的 8 位无符号整数，如果 value 为 null，则为 0（零）
            // array[i] = Convert.ToByte(text.substring(i * 2, 2), 16);
            String substring = text.substring(i * 2, i * 2 + 2);
            if(substring.length() == 0)
                array[i] = 0;
            else {
                int i1 = Integer.parseInt(substring, 16);
                array[i] = (byte) i1;
            }
        }
        return array;
    }

    public String GetSignature(String _applicationId, String _sessionId) throws Exception {
        byte[] array = VsJSONProgressEvent.UUIDtoByteArray(_applicationId);
        byte[] array2 = VsJSONProgressEvent.UUIDtoByteArray(_sessionId);

        int num = 16;
        byte[] array3 = new byte[num];
        for (int i = 0; i < num; i++)
        {
            if (i < 8)
            {
                array3[i] = array[i * 2 + 1];
            }
            else
            {
                array3[i] = array2[(i - 8) * 2];
            }
        }

        // System.out.println(new String(array3));
        byte[] text = this.GetSignableText();
        byte[] ret = SignatureUtils.sign(text, array3);

        // System.out.println(new String(text));
        // System.out.println(new String(ret));
        // System.out.println("text --- " + HexUtil.byte2HexStr(text));
        // System.out.println("array3 --- " + HexUtil.byte2HexStr(array3));
        // System.out.println("ret --- " + HexUtil.byte2HexStr(ret));

        return Base64.encodeBase64String(ret);
    }
}
