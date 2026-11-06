/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.zidan;

/**
 *
 * @author user
 */
public class Laptop {
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "tbl_laptop")
@Audited
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String merk;
    private String tipe;
    private Double harga;

    public Laptop() {}

    public Laptop(String merk, String tipe, Double harga) {
        this.merk = merk;
        this.tipe = tipe;
        this.harga = harga;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMerk() { return merk; }
    public void setMerk(String merk) { this.merk = merk; }
    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }
    public Double getHarga() { return harga; }
    public void setHarga(Double harga) { this.harga = harga; }
}
}
