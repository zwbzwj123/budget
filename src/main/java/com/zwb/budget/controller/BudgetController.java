package com.zwb.budget.controller;

import com.zwb.budget.dao.PriceRepository;
import com.zwb.budget.dao.PriceService;
import com.zwb.budget.entity.ItemPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
public class BudgetController {
    @Autowired
    PriceRepository priceRepository;
    @Autowired
    private PriceService priceService;

    @RequestMapping(value = "/getItemByIndex")
    public List<ItemPrice> getItemPrice(@RequestParam(value = "index",required = false) String index){
        return priceService.findByIndex(index);
    }

    @RequestMapping(value = "/getExcel", method = RequestMethod.POST)
    public ResponseEntity<byte[]> sendExcel(
            @RequestParam(value = "indexArray",required = true) String indexArray,
            @RequestParam(value = "username",required = true) String username
    ) {
        return priceService.sendExcel(indexArray, username);
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public void upload(MultipartFile file){
        priceService.upload(file);
    }

    @RequestMapping(value = "/setFactorDate", method = RequestMethod.POST)
    public void setFactorDate(
            @RequestParam(value = "factorDate",required = true) String factorDate,
            @RequestParam(value = "username",required = true) String username
    ){
        priceService.setFactorDate(factorDate, username);
    }

    @RequestMapping(value = "/removeFactorDate", method = RequestMethod.POST)
    public void removeFactorDate(
            @RequestParam(value = "factorDate",required = true) String factorDate
    ){
        priceService.removeFactorDate(factorDate);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(
            @RequestParam(value = "username",required = true) String username,
            @RequestParam(value = "password",required = true) String password
    ) {
        return priceService.login(username, password);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(
            @RequestParam(value = "username",required = true) String username,
            @RequestParam(value = "password",required = true) String password,
            @RequestParam(value = "code",required = true) String code
    ) {
        return priceService.register(username, password, code);
    }
}

