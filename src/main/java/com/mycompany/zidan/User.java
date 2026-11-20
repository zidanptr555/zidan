/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.zidan;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "tbl_user")
@Audited
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @NotAudited // Sesuai tabel tbl_user_aud yang tidak menyimpan kolom password
    @Column(nullable = false)
    private String password;

    @Column(name = "nama_lengkap", nullable = false)
    private String namaLengkap;

    @Column(nullable = false)
    private String role;

    private Boolean aktif = true;

    public User() {}

    // Getter dan Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getAktif() { return aktif; }
    public void setAktif(Boolean aktif) { this.aktif = aktif; }
}

