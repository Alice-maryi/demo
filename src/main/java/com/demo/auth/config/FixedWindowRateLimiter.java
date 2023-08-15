package com.demo.auth.config;

import com.alibaba.fastjson2.JSONObject;
import com.demo.auth.util.RSADigitalSignUtil;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class FixedWindowRateLimiter {

    private static final String serverPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC/ojlokTIxMUReV944ysAq6Njt9devlpEO5b0wp8Ou0v7y6OCoQWwNnBfvORfv+szUhkW1oEvQDXBp0FlPFsiG61JVvjWPYpi2ZOzdMW2b2in9VhBWc6fF9Amh5owdHGEYzY4JbYM9hWrHq8x612llq3x6oE/J1r161/OdGr8Avi3oeE2eacFfSUZMofv86l45V1JVF4akoNolyvjfVcfkkFFIE4wKEpM+I+ibLxiFa4yBnwMS5TtxqrefbhZYPVdJUKDmLTQ/TD0rSVAsmpGUQFRptst7uY5UEi+Mqm/42YedxowrNeiTNhLGVxRdHxK/Wyzr2sgj8CNnhan2v19/AgMBAAECggEAA1zXYQxbMx3IM1ebYKe+I4Kee956gGxF81r4rOfkuSaerqiDeLBdMwfwK5R25REjkv8gLgW4DtiNhm3Pg2HE9wkeEr8ZtYBnaZvvxBnf8IIthnKCKuIwqXqUyLCA+OnMkmDZAzVWvjefHg3Xsrgg81XFA2nYvJP2EzfC4JjdjSfBVpfbC8r7LjuEVT436zSFdmjC0E3KbDDfWLm8NMMfgTQYjXd4HWNT75r4Gg+9WQynfwU0QmqS4KCv+9SO8GBYlPeI5JEDDCBSOUHehPH7QNo+jeUoFVsXeinxnwtEiz8klxrfx8YWPV0HcFZhzv8PoXc3tJJgUmZozlD2w+xEsQKBgQD5STykzgRlZti3YUQwgqS7qShSqpO05ZrEK9wpRV/9RsJexGCRAGa93VhFVNkYgPa7ZUermCfUQGffoNfqoZjeCnDzAvALNMeOYGYahvBiVFkCGjvMvb9jiVAZgZZrqfdsrhDOmDHhXR9OUHMaIYbVv7McEYDCYB9dwA5lSX/kAwKBgQDEy30vywwZXOdOMZaGJusbdIfFGwU3QoUTZP1LtNKPVG65n740R641yFRifSIKhuePvHaq0dEmXlMuQFutEEpTjhiywzRXiRnAse9HuECftDeXu7uObumCOGkXKLW4867xB97y2yumRrmJ1TT5exEypDK2hUwFl5OCQ8nWgrjj1QKBgDsdL+FQ7V/ybI/H4/iw8kmCCorX4mDyVaoRUIsE5YdVIUCBxvh7pOF/PLBsN3MFGf1EPiOl2EQP5dH0/V6pZWu6BCDMYIn3wR61I2FFAHoA0H6cI/QgRuNeq3zw6u+rBX5jJJ8pHiL4BzV2BGV879F+PQX57JOrYaTEp2VDca1rAoGAL1I7LRXnN+YWHMBh0KplGHoB98ySiYIuW8aJ/f1iMGL+KRaXdxzRcE9Ws0QhvmRIJFEOO+8uGsrNt650HP5w0Py3Ra0Y1f+6uJJQ1m8g4wZD4/GZnRtgfRaxdocsg2fdZkodO0qmiVoyXUlubptAPGwtYaRV7nZPWyCP1Uwjx20CgYApmpA4ihEg5opugfhHGCPFX+wPS4rH5sVk1dm7VDIaKyw6jU+2DslPWZRnhXNinK6/C8rNPnCRUXpOwMOKpI28vV/6BRH/XDMBDgZk72R+tPiILeQJYuREL4WMwXI0vLFPkT0U+EeXDTA5z8UIj6FJxRm6kYlbMyyyKKzeIoUjmA==";
    // 服务器公钥
    private static final String serverPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv6I5aJEyMTFEXlfeO" +
            "MrAKujY7fXXr5aRDuW9MKfDrtL+8ujgqEFsDZwX7zkX7/rM1IZFtaBL0A1wadBZTxbIhutSVb41j2KYtmTs3TFtm" +
            "9op/VYQVnOnxfQJoeaMHRxhGM2OCW2DPYVqx6vMetdpZat8eqBPyda9etfznRq/AL4t6HhNnmnBX0lGTKH7/OpeOV" +
            "dSVReGpKDaJcr431XH5JBRSBOMChKTPiPomy8YhWuMgZ8DEuU7caq3n24WWD1XSVCg5i00P0w9K0lQLJqRlEBUabbL" +
            "e7mOVBIvjKpv+NmHncaMKzXokzYSxlcUXR8Sv1ss69rII/AjZ4Wp9r9ffwIDAQAB";

    private static final String clientPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnS3D0zdviB/WfB4c0OeLfXijKlLOiz1oceBz7pnG6uJ6/F4gRu5TUHvNh0Xn75UUH9a0RvqWFhJDqnaFOquGUPqK2EuukiTWtc+dLsnR6rLGyzx0JiqYMT0T46QwyaExBp4Zs5yvbbmKUWPOF4Tr4SJedHdsl+vRLLxnmhqqG5UzsLiQRjCDEc/k1eVCAK9hO9ost+twYqyeHgmNQEBN/60zu7r281vv48vMnR54AF/4+UnyITwsHXhgOTn4aZG0xBIxFkjtUpcCri5l/KTTQ+GcMpRPaATEkd6m+O/Txt1R6ROzZF7lzj6++C3PPUUjicdMRKst3F5LPq4ibKb8tQIDAQAB";


    public static void main(String[] args) throws Exception {
//        String json = "{\"documentUrlList\":[{\"url\":\"https://www.cnc.mvgx.com/api/fileSystem/file/download?fileId=1657359090325954561\"}],\"location\":\"111.000000,2222.000000\",\"orgName\":\"xu sun\",\"projectId\":100037,\"projectName\":\"Project2\"}";
//        String encrypted = RSADigitalSignUtil.encryptByPublicKey(json, clientPublicKey);
//        String sign = RSADigitalSignUtil.signByPrivateKey(encrypted, serverPrivateKey);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("encryptedData", encrypted);
//        jsonObject.put("signature", sign);
//        System.out.println(jsonObject.toJSONString());
        decrypt();

    }

    private static void decrypt() throws Exception {
        String sign = "AacFnodd550AcIPgRvAvu0Cq1VwaizBknLsPWn0oY7u4P5TBf8XJSLEzENev7NkAs7dC6FHHpctkMUXdM45oRCCGI/MnFyGompxQrmlvuNTj1i0buckGL8sVbont7q8ZZPLY1GDie38VpJ3cWZysUVh3PgJXVlnpoXmX7EzMaiqkjw5fp/SwUi/BpvuFJyDm3n76R1sHl/hw0HdwJfD2pOKSfGsHiFh5Izy1S+4xFYqeq185acHRIOu3ZzYbKkWdf8hVcIaCW+fZpD9HNR+WWKKJf57XpInaV0IX/WDDLOqyFCjESvaWjpxdVlGeWkP0gpt1E2h7RlL7biZFEFYpkQ==";
        String mes = "L8PKAUbMX85uC0aLupoSqkupGsVGKHEQDOrei+n/s8L0zgsRF3ZWvTWOLXSHtHsjmJB67QJXalVvdYafjvcF6qbeRbKPauIpeqyikvhinq5SKvlYCwtRq2PdOGppRWjUpUlRwJ3KVxXkP+etnIniSMZs84vh27ixap6nNtUN7vLvpH5U7S6Z0SDEhXJx6iKh7d0U9XHcGeX3NH2OS9gvpZ3RybdlN0HedXoek2hyjcDmEFWdu8s9hkz7vJkwvGDqw4QmFhutEo7dDPWW2LtCbdFksJwIa9Ffse6QPe2xCa3sKT0YM0ZK+QAJha6iTzmxiZhwXqWZY7MtxJ8EgHKlzA==";
        System.out.println(RSADigitalSignUtil.verify(mes.getBytes(StandardCharsets.UTF_8), clientPublicKey, sign));
        System.out.println(RSADigitalSignUtil.decryptByPrivateKey(mes, serverPrivateKey));
    }

}
