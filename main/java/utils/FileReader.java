package utils;

import java.io.*;

public class FileReader {

    public String loadContentOfFile(String filePath) throws IOException {

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("所选文件不存在或不可读或是目录");
            return "";
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
        String line = "";
        StringBuilder content = new StringBuilder();
        while ((line = br.readLine()) != null) {
            content.append(line+"\n");
        }
//        System.out.println(content.toString());
        return content.toString();
    }

}
