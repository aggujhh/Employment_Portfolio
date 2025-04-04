package org.example.pojo.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data// Lombokを使用して自動生成されたgetter、setter、toStringメソッドを持つ
@NoArgsConstructor// 引数なしのコンストラクタを自動生成
@AllArgsConstructor// 全フィールドの値を初期化するコンストラクタを自動生成
public class OrderHistory {
    private String orderHistoryId;
    private String deskId;
    private String payMethod;
    private Integer amount;
    private List<OrderHistory.Dishes> dishes = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dishes {
        private String orderHistoryId;
        private Integer dishId;
        private Integer count;
    }
}
