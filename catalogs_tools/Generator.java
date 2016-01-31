import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

public class Generator {

    public static class CodeBean {
        private String code;
        private boolean valid;
        public CodeBean(String code, boolean valid) {
            this.code = code;
            this.valid = valid;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public boolean isValid() {
            return valid;
        }
        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }
    
    private static int CODE_AMOUNT = 100;
    private static int CODE_LENGTH = 8;
    private static String BASE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
    private static Random random = new Random();
    private static JSONObject json = new JSONObject();
    private static List<CodeBean> codes = new ArrayList<CodeBean>();
    
    public static void main(String[] args) throws Exception {
        FileWriter fw1 = new FileWriter("login_codes");
        FileWriter fw2 = new FileWriter("login_codes.json");
        
        int codesSize = CODE_LENGTH;
        int codeAmount = CODE_AMOUNT;
        try {
            if (args[0] != null) {
                codesSize = Integer.valueOf(args[0]);
            }
            if (args[1] != null) {
                codeAmount = Integer.valueOf(args[1]);
            }
        } catch (Exception e) {
        }

        int c = 0;
        while (c++ < codeAmount) {
            int index = 0;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < codesSize; i++) {
                index = random.nextInt(BASE.length());
                sb.append(BASE.charAt(index));
            }
            codes.add(new CodeBean(sb.toString(), true));
            json.put("login_codes",codes);
            fw1.write(sb.toString()+"\n");
        }
        fw2.write(json.toJSONString());
        fw1.close();
        fw2.close();
    }
}
