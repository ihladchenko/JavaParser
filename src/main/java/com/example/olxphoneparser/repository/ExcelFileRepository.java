package com.example.olxphoneparser.repository;

import com.example.olxphoneparser.model.ExcelFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcelFileRepository extends JpaRepository<ExcelFile, Long> {
}