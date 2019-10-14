package com.zwb.budget.dao;

import com.zwb.budget.entity.ItemPrice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by zwb
 * Time: 2019/10/6
 */
public interface PriceService {
    public List<ItemPrice> findByIndex(String index);
    public ResponseEntity<byte[]> sendExcel(String indexArray, String username);
    public void upload(MultipartFile file);
    public void setFactorDate(String factorDate, String username);
    public void removeFactorDate(String factorDate);
    public String login(String username, String password);
    public String register(String username, String password, String code);
}
