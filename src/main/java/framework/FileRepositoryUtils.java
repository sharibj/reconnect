package framework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class FileRepositoryUtils {

    private FileRepositoryUtils() {
    }

    public static List<String> readLines(final String filePath, final String fileName) throws IOException {
        Files.createDirectories(Paths.get(filePath));
        File file = new File(filePath, fileName);

        if (!file.isFile() && !file.createNewFile()) {
            throw new IOException("Error creating new file: " + file.getAbsolutePath());
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().toList();
        }
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
        Files.createDirectories(Paths.get(filePath));
        File file = new File(filePath, fileName);
        if (!file.isFile() && !file.createNewFile()) {
            throw new IOException("Error creating new file: " + file.getAbsolutePath());
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        try (writer) {
            for (String line : lines) {
                writer.append(line).append("\n");
            }
        }
    }
}
