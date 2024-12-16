package org.example.pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * テーブル情報を表すエンティティクラス
 */
@Data// Lombokを使用して自動生成されたgetter、setter、toStringメソッドを持つ
@NoArgsConstructor// 引数なしのコンストラクタを自動生成
@AllArgsConstructor// 全フィールドの値を初期化するコンストラクタを自動生成
public class Desk {
    private String id;//ID
    private String deskState;//テーブルの状態
    private String orderState;//オーダーの状態
    private Integer seatCount;//席数
    private Integer guestCount;//客人数
}
