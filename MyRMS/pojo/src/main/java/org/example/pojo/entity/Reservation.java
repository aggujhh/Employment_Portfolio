package org.example.pojo.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data// Lombokを使用して自動生成されたgetter、setter、toStringメソッドを持つ
@NoArgsConstructor// 引数なしのコンストラクタを自動生成
@AllArgsConstructor// 全フィールドの値を初期化するコンストラクタを自動生成
public class Reservation {
    private Integer id;

    @NotNull(message = "日付は必須です")
    private LocalDate date;

    @NotBlank(message = "時間範囲を入力してください")
    private String timeRange;

    @NotBlank(message = "名前を入力してください")
    private String name;

    @NotBlank(message = "カタカナを入力してください")
    private String katakana;

    @NotBlank(message = "内容を入力してください")
    private String content;

    @NotNull(message = "人数は必須です")
    private Integer people;

    @NotBlank(message = "電話番号を入力してください")
    private String tel;

    @NotBlank(message = "メールアドレスを入力してください")
    private String mail;

    private String specialNote;

    private List<String> reservedTables;

    private String guestState;
}
