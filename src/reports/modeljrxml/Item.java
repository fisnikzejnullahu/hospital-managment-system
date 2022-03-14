/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports.modeljrxml;

/**
 *
 * @author Fisnik Zejnullahu
 */
public class Item {
    private String data;
    private String emri;
    private String niveli;

    public Item(String data, String emri, String niveli) {
        this.data = data;
        this.emri = emri;
        this.niveli = niveli;
    }

    public String getData() {
        return data;
    }

    public String getEmri() {
        return emri;
    }

    public String getNiveli() {
        return niveli;
    }
}
