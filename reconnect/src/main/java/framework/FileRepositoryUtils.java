package framework;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class FileRepositoryUtils {

    private FileRepositoryUtils() {
    }

    public static List<String> readLines(final String filePath, final String fileName) throws IOException {
        List<String> lines = List.of();
        File file = new File(filePath, fileName);

        //TODO: Test file creation
        if (!file.isFile() && !file.createNewFile()) {
            throw new IOException("Error creating new file: " + file.getAbsolutePath());
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            lines = reader.lines().toList();
        } finally {
            reader.close();
        }
        return lines;
    }

    public static List<String> readTokens(final String line, final String delimiter) {
        StringTokenizer tokenizer = new StringTokenizer(line, delimiter);
        List<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken().trim());
        }
        return tokens;
    }

    public static void appendLines(List<String> lines, String filePath, String fileName) throws IOException {
        File file = new File(filePath, fileName);
        if (!file.isFile() && !file.createNewFile()) {
            throw new IOException("Error creating new file: " + file.getAbsolutePath());
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("");
        try {
            for (String line : lines) {
                writer.append(line + "\n");
            }
        } finally {
            writer.close();
        }
    }

    public static void dropFile(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        if (file.isFile()) {
            file.delete();
        }
    }
}
