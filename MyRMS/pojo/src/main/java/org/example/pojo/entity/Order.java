package org.example.pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data// Lombokを使用して自動生成されたgetter、setter、toStringメソッドを持つ
@NoArgsConstructor// 引数なしのコンストラクタを自動生成
@AllArgsConstructor// 全フィールドの値を初期化するコンストラクタを自動生成
public class Order {
    private String id;
    private String deskId;
    private String state;
    private LocalDateTime orderTime;
    private LocalDateTime completionTime;
    private List<Dishes> dishes;

    // 嵌套的 Dish 类
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dishes {
        private Integer dishId;
        private Integer count;
    }
}
