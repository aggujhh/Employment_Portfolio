package org.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 料理のカテゴリー情報を表すエンティティクラス
 */
@Data// Lombokを使用して自動生成されたgetter、setter、toStringメソッドを持つ
@NoArgsConstructor// 引数なしのコンストラクタを自動生成
@AllArgsConstructor// 全フィールドの値を初期化するコンストラクタを自動生成
public class DishCategory {
    private Integer id;//ID
    private String name;//料理名
    private String creator;//作成者
    private String updater; //更新者
    private LocalDateTime createTime; //作成時間
    private LocalDateTime updateTime; //更新時間
}
