package org.example.pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data// Lombokを使用して自動生成されたgetter、setter、toStringメソッドを持つ
@NoArgsConstructor// 引数なしのコンストラクタを自動生成
@AllArgsConstructor// 全フィールドの値を初期化するコンストラクタを自動生成
public class AccountingOrders {
    private String deskId;
    private Integer amount;
    private Integer guestCount;
    private String payMethod;
    private List<AccountingOrders.Dishes> dishes = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dishes {
        private Integer dishId;
        private String name;
        private Integer price;
        private Integer count;
    }
}
