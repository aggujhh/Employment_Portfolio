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
    private String orderId;
    private String deskId;
    private String dishId;
    private String dishName;
    private Integer count;
    private LocalDateTime orderTime;
    private LocalDateTime completionTime;
}
