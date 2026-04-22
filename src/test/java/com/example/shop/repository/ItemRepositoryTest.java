package com.example.shop.repository;

import com.example.shop.domain.SellStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("삼품 저장 테스트")
    public void createItemTest(){
        Item item  = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(25000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(SellStatus.SELL);
        item.setStockNumber(100);

        Item savedItem = itemRepository.save(item); //DB에 저장
        System.out.println("저장된 상품 정보 : " + savedItem.toString());

    }
}
