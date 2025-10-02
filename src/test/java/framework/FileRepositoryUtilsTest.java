package framework;

import org.junit.jupiter.api.Test;

import adapter.secondary.persistence.file.FileRepositoryUtils;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileRepositoryUtilsTest {

    String filePath = "src/test/resources/";
    String fileName = "groups.csv";

    @Test
    void whenFilePathAndNameIsProvided_thenReturnLines() throws IOException {
        // given
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath, fileName)));
        writer.append("id1± friends± 3\nid2± family± 6");
        writer.close();

        // when
        List<String> lines = FileRepositoryUtils.readLines(filePath, fileName);

        // then
        assertEquals(2, lines.size());
        assertEquals("id1± friends± 3", lines.get(0));
        assertEquals("id2± family± 6", lines.get(1));
    }

    @Test
    void whenLineIsProvided_thenReturnTokens() {
        //given
        String line = " token1 ±  token 2 ±  ±token3±±";
        String delimiter = "±";

        // when
        List<String> tokens = FileRepositoryUtils.readTokens(line, delimiter);

        // then
        assertEquals(4, tokens.size());
        assertTrue(tokens.containsAll(List.of("token1", "token 2", "", "token3")));
    }

    @Test
    void whenLinesAreProvided_thenClearContentsAndAppendLines() throws IOException {
        // given
        FileRepositoryUtils.appendLines(List.of("testLine1", "testLine2"), filePath, fileName);
        // when
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath, fileName)));
        List<String> lines = reader.lines().toList();
        // then
        assertEquals(2, lines.size());
        assertEquals("testLine1", lines.get(0));
        assertEquals("testLine2", lines.get(1));
    }
}