package com.github.taojintianxia.tool.encrypttransfer;

public class Tester {
    
    private static final String TEMPLATE = "            plain:\n" +
            "              name: PLAIN_NAME\n" +
            "            cipher: \n" +
            "              name: CIPHER_NAME\n" +
            "              encryptorName: aes_encryptor";
    
    private static String SAMPLE = "      CU_EMPLOYEE_EVENT:\n" + "        columns:\n" + "          BANKID1:\n" + "            cipherColumn: BANKID1\n" + "            encryptorName: name_encryptor\n" + "          BENEACCOUNT:\n" + "            cipherColumn: BENEACCOUNT\n" + "            encryptorName: name_encryptor\n" + "          ACCUACCOUNT:\n" + "            cipherColumn: ACCUACCOUNT\n" + "            encryptorName: name_encryptor";
    
    public static void main(String... args) {
        transferConfig(SAMPLE);
    }
    
    private static void transferConfig(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();
        StringBuilder columnConfigBuilder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (!isColumnLine(lines[i])) {
                result.append(lines[i]).append("\n");
                result.append(columnConfigBuilder);
                columnConfigBuilder = new StringBuilder();
            }
            else {
                columnConfigBuilder.append(lines[i]).append("\n");
                columnConfigBuilder.append(generateColumnContent(lines[i+1] + "\n" + lines[i + 2])).append("\n");
                if (i + 3 < lines.length) {
                    i += 4;
                }
                if (i + 4 >= lines.length) {
                    break;
                }
            }
        }
        System.out.print(result.append(columnConfigBuilder));
    }
    
    private static boolean isColumnLine(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                count++;
            } else {
                break;
            }
        }
        return count == 10;
    }
    
    private static String generateColumnContent(String content) {
        String[] lines = content.split("\n");
        String columnName = lines[0].substring(lines[0].indexOf(": ") + 2);
        String result = "";
        result = TEMPLATE.replace("CIPHER_NAME", columnName.trim());
        result = result.replace("PLAIN_NAME", columnName.trim() + "_CIPHER01");
        return result;
    }
}
