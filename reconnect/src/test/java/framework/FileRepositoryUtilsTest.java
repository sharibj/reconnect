package framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

class FileRepositoryUtilsTest {

    @Test
    void whenFilePathAndNameIsProvided_thenReturnLines() throws IOException {
        // given
        String filePath = "src/test/resources/";
        String fileName = "groups.csv";

        // when
        List<String> lines = FileRepositoryUtils.readLines(filePath, fileName);

        // then
        assertEquals(2, lines.size());
        assertEquals("id1, friends, 3", lines.get(0));
        assertEquals("id2, family, 6", lines.get(1));
    }

    @Test
    void whenLineIsProvided_thenReturnTokens() {
        //given
        String line = " token1 ,  token 2 ,  ,token3,,";
        String delimiter = ",";

        // when
        List<String> tokens = FileRepositoryUtils.readTokens(line, delimiter);

        // then
        assertEquals(4, tokens.size());
        assertTrue(tokens.containsAll(List.of("token1", "token 2", "", "token3")));
    }
}