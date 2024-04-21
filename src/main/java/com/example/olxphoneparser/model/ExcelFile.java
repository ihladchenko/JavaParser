package com.example.olxphoneparser.model;

import javax.persistence.*;

@Entity
@Table(name = "excel_files")
public class ExcelFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    private byte[] data;

    // Конструктори, гетери та сетери

    public ExcelFile() {
    }

    public ExcelFile(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ExcelFile{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}