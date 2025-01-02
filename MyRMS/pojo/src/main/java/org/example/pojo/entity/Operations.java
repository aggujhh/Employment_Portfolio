package org.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data// Lombokを使用して自動生成されたgetter、setter、toStringメソッドを持つ
@NoArgsConstructor// 引数なしのコンストラクタを自動生成
@AllArgsConstructor// 全フィールドの値を初期化するコンストラクタを自動生成
public class Operations {

    private LocalDate date;
    private Integer customerCount;
    private Integer sales;
    private Double turnoverRate;
    private LocalTime lunchTimeStart;
    private LocalTime lunchTimeEnd;
    private LocalTime dinnerTimeStart;
    private LocalTime dinnerTimeEnd;
 }
