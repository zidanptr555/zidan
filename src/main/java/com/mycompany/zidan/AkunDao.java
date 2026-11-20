/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.zidan;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AkunDao {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DatabaseConfig.getDataSource());

    private final RowMapper<Object[]> akunRowMapper = new RowMapper<Object[]>() {
        @Override
        public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Object[]{
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("nama_lengkap"),
                rs.getString("role"),
                rs.getBoolean("aktif")
            };
        }
    };

    public List<Object[]> getAllAkun() {
        return jdbcTemplate.query("SELECT id, username, nama_lengkap, role, aktif FROM tbl_user", akunRowMapper);
    }

    public List<Object[]> searchAkun(String keyword) {
        String query = "SELECT id, username, nama_lengkap, role, aktif FROM tbl_user WHERE username LIKE ? OR nama_lengkap LIKE ?";
        String searchPattern = "%" + keyword + "%";
        return jdbcTemplate.query(query, akunRowMapper, searchPattern, searchPattern);
    }

    public boolean insertAkun(String username, String password, String namaLengkap, String role, boolean aktif) {
        String query = "INSERT INTO tbl_user (username, password, nama_lengkap, role, aktif) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(query, username, password, namaLengkap, role, aktif) > 0;
    }

    public boolean updateAkun(long id, String username, String namaLengkap, String role, boolean aktif) {
        String query = "UPDATE tbl_user SET username = ?, nama_lengkap = ?, role = ?, aktif = ? WHERE id = ?";
        return jdbcTemplate.update(query, username, namaLengkap, role, aktif, id) > 0;
    }

    public boolean deleteAkun(long id) {
        String query = "DELETE FROM tbl_user WHERE id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }
}

