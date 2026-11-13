/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.zidan;

/**
 *
 * @author user
 */
public class Zidan {

    public static void main(String[] args) {
        // Mengubah baris ini agar langsung membuka LoginForm saat program dijalankan
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}