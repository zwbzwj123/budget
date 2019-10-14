package com.zwb.budget.dao;

import com.zwb.budget.entity.ItemPrice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zwb
 * Time: 2019/10/5
 */
public interface PriceRepository extends JpaRepository<ItemPrice, Integer> {
}
