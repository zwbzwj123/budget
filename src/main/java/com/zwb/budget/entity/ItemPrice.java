package com.zwb.budget.entity;

import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zwb
 * Time: 2019/10/5
 */
@Entity
@Table(name = "cost")
public class ItemPrice {

    @Id
    @GeneratedValue
    @Column(name = "title_index")
    private String titleIndex;
    @Column(name = "name")
    private String name;
    @Column(name = "measure")
    private String measure;
    @Column(name = "cost")
    private Integer cost;
    @Column(name = "factor")
    private String factor;
    @Column(name = "setup_cost")
    private Integer setupCost;
    @Column(name = "labor_cost")
    private Integer laborCost;
    @Column(name = "extra_cost")
    private Integer extraCost;
    @Column(name = "machinery_cost")
    private Integer machineryCost;

    public ItemPrice() {
    }

    public String getIndex() {
        return titleIndex;
    }

    public void setIndex(String titleIndex) {
        this.titleIndex = titleIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public Integer getSetupCost() {
        return setupCost;
    }

    public void setSetupCost(Integer setupCost) {
        this.setupCost = setupCost;
    }

    public Integer getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(Integer laborCost) {
        this.laborCost = laborCost;
    }

    public Integer getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(Integer extraCost) {
        this.extraCost = extraCost;
    }

    public Integer getMachineryCost() {
        return machineryCost;
    }

    public void setMachineryCost(Integer machineryCost) {
        this.machineryCost = machineryCost;
    }

}
