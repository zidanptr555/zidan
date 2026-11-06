/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.zidan;

/**
 *
 * @author user
 */
public class PurchaseOrder {

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_purchase_order")
@Audited 
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomor_po", unique = true, nullable = false)
    private String nomorPo;

    private String status; 
    private LocalDateTime tanggalDibuat = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User pembuatPo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "po_id") 
    private List<Laptop> daftarLaptop = new ArrayList<>();

    public PurchaseOrder() {}

    public void tambahLaptop(Laptop laptop) {
        this.daftarLaptop.add(laptop);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomorPo() { return nomorPo; }
    public void setNomorPo(String nomorPo) { this.nomorPo = nomorPo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getTanggalDibuat() { return tanggalDibuat; }
    public void setTanggalDibuat(LocalDateTime tanggalDibuat) { this.tanggalDibuat = tanggalDibuat; }
    public User getPembuatPo() { return pembuatPo; }
    public void setPembuatPo(User pembuatPo) { this.pembuatPo = pembuatPo; }
    public List<Laptop> getDaftarLaptop() { return daftarLaptop; }
    public void setDaftarLaptop(List<Laptop> daftarLaptop) { this.daftarLaptop = daftarLaptop; }
}
}
