package site.easy.to.build.crm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataHandler {
    private static final String RESET_DATA_SQL_FILEPATH = "sql/reset_data.sql";
    private static final String RESET_DATA_SQL_FILE_CONTENTS;

    private final JdbcTemplate jdbcTemplate;

    static {
        ClassPathResource resource = new ClassPathResource(RESET_DATA_SQL_FILEPATH);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            RESET_DATA_SQL_FILE_CONTENTS = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while initializing " +
                "the contents of file \"%s\"", RESET_DATA_SQL_FILEPATH), e);
        }
    }
    
    public boolean resetData() {
        try {
            jdbcTemplate.execute(RESET_DATA_SQL_FILE_CONTENTS);
            return true;
        } catch (Exception e) {
            log.error("Error while resetting data", e);

            return false;
        }
    }
}
