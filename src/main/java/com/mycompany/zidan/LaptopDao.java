package com.mycompany.zidan;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LaptopDao {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DatabaseConfig.getDataSource());

    private final RowMapper<Object[]> laptopRowMapper = new RowMapper<Object[]>() {
        @Override
        public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Object[]{
                rs.getInt("id"),
                rs.getString("merk"),
                rs.getString("tipe"),
                rs.getDouble("harga")
            };
        }
    };

    // Ambil semua data
    public List<Object[]> getAllLaptop() {
        return jdbcTemplate.query("SELECT * FROM tbl_laptop", laptopRowMapper);
    }

    // FITUR 1: Pencarian Real-time berdasarkan Merk atau Tipe
    public List<Object[]> searchLaptop(String keyword) {
        String query = "SELECT * FROM tbl_laptop WHERE merk LIKE ? OR tipe LIKE ?";
        String searchPattern = "%" + keyword + "%";
        return jdbcTemplate.query(query, laptopRowMapper, searchPattern, searchPattern);
    }

    // Simpan data baru
    public boolean insertLaptop(String merk, String tipe, double harga) {
        String query = "INSERT INTO tbl_laptop (merk, tipe, harga) VALUES (?, ?, ?)";
        return jdbcTemplate.update(query, merk, tipe, harga) > 0;
    }

    // FITUR 2: Update / Edit data berdasarkan ID
    public boolean updateLaptop(int id, String merk, String tipe, double harga) {
        String query = "UPDATE tbl_laptop SET merk = ?, tipe = ?, harga = ? WHERE id = ?";
        return jdbcTemplate.update(query, merk, tipe, harga, id) > 0;
    }

    // Hapus data berdasarkan ID
    public boolean deleteLaptop(int id) {
        String query = "DELETE FROM tbl_laptop WHERE id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }
}