package com.github.taojintianxia.tool.encrypttransfer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Application {
    
    private static final String CONFIG_FILE = "config.properties";
    
    private static final Properties PROPERTIES = new Properties();
    
    private static final String TEMPLATE = "            plain:\n" + "              name: PLAIN_NAME\n" + "            cipher: \n" + "              name: CIPHER_NAME\n" + "              encryptorName: aes_encryptor";
    
    public static void main(String[] args) throws Exception {
        initConfig();
        transferCompatibleEncrypt();
    }
    
    private static void initConfig() throws IOException {
        InputStream input = Application.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
        PROPERTIES.load(input);
    }
    
    private static void transferCompatibleEncrypt() throws IOException {
        StringBuilder resultBuilder = new StringBuilder();
        StringBuilder columnContentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROPERTIES.getProperty("transfer.sourceFile")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (lineStartSpaceCount(line) == 6) {
                    resultBuilder.append(transferConfig(columnContentBuilder.toString()));
                    resultBuilder.append(line).append("\n");
                    columnContentBuilder = new StringBuilder();
                }
                if (lineStartSpaceCount(line) == 10 || lineStartSpaceCount(line) == 12) {
                    columnContentBuilder.append(line).append("\n");
                }
                if (lineStartSpaceCount(line) == 8) {
                    resultBuilder.append(line).append("\n");
                }
            }
        }
        resultBuilder.append(transferConfig(columnContentBuilder.toString()));
        System.out.println(resultBuilder.toString());
    }
    
    private static String transferConfig(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (isColumnConfigLine(lines[i])) {
                result.append(lines[i]).append("\n");
                result.append(generateColumnContent(lines[i + 1])).append("\n");
                i += 3;
            }
        }
        return result.toString();
    }
    
    private static int lineStartSpaceCount(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
    
    private static boolean isColumnConfigLine(String line) {
        return lineStartSpaceCount(line) == 10;
    }
    
    private static boolean isLogicColumnLabelLine(String line) {
        return lineStartSpaceCount(line) == 6;
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
