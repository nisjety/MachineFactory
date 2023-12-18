package com.example.maschinefactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class H2ConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testH2Connection() throws Exception {
        // Simply obtaining a connection is sufficient to test connectivity
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.isClosed()).isFalse();

            // Additional check: execute a simple query
            Integer count = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            assertThat(count).isEqualTo(1);
        }
    }
}
