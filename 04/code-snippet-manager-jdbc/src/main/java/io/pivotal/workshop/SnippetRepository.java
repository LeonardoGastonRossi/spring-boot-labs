package io.pivotal.workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

import static java.util.UUID.randomUUID;

@Repository
public class SnippetRepository {


    private final String SQL_INSERT = "insert into snippet (id, title, code, created, modified)" +
            " values(?, ?, ?, now(), now())";
    private final String SQL_QUERY_ALL = "select * from snippet";
    private final String SQL_QUERY_BY_ID = "select * from snippet where id = ?";
    private final String SQL_QUERY_DATES = "select * from snippet WHERE created BETWEEN ? AND ?";


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<SnippetRecord> rowMapper =
            (ResultSet rs, int row) ->
                    new SnippetRecord(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("code"),
                        rs.getDate("created").toLocalDate(),
                        rs.getDate("modified").toLocalDate()
                    );

    @Autowired
    public SnippetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SnippetRecord save(NewSnippetFields newSnippetFields) {
        String newId = randomUUID().toString();
        jdbcTemplate.update(SQL_INSERT, newId, newSnippetFields.title, newSnippetFields.code);

        return findOne(newId);
    }

    public List<SnippetRecord> findBetween(String startDate, String finishDate) {
        return jdbcTemplate.query(SQL_QUERY_DATES, new Object[]{startDate, finishDate}, rowMapper);
    }

    public List<SnippetRecord> findAll() {

        return jdbcTemplate.query(SQL_QUERY_ALL, rowMapper);
    }

    public SnippetRecord findOne(String id) {
        return jdbcTemplate.queryForObject(SQL_QUERY_BY_ID, new Object[]{id}, rowMapper);
    }
}
