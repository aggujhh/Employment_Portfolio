package org.example.server.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Dish;
import org.example.server.mapper.DishMapper;
import org.example.server.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.common.SaveFile;

import java.util.UUID;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;
    private final HttpServletRequest request;


    @Autowired
    public DishServiceImpl(DishMapper dishMapper, HttpServletRequest request) {
        // フィールドにインターセプターを設定
        this.dishMapper = dishMapper;
        this.request = request;
    }


    @Override
    public List<Dish> getDishByCategoryId(Long dishCategoryId) {
        return dishMapper.getDishByCategoryId(dishCategoryId);
    }

    @Override
    public void addDish(Dish dish) {
        // 変数の初期化
        String base64Image = dish.getImage(); // Base64形式の画像データを取得
        String fileName = null; // 画像ファイル名
        String dishImagePath = null; // 大サイズ画像の保存パス
        String orderImagePath = null; // 小サイズ画像の保存パス

        // 画像が存在する場合、ファイル名と保存パスを生成
        if (base64Image != null && !base64Image.isEmpty()) {
            fileName = UUID.randomUUID() + ".jpg"; // 一意のファイル名を生成
            dishImagePath = "dish-img" + File.separator + dish.getDishCategoryId() + File.separator + fileName; // 大サイズ画像のパス
            orderImagePath = "order-img" + File.separator + dish.getDishCategoryId() + File.separator + fileName; // 小サイズ画像のパス
            dish.setImage(fileName); // 画像ファイル名をDishオブジェクトに設定
        } else {
            dish.setImage(null); // 画像がない場合はnullを設定
        }

        String userId = (String) request.getAttribute("userId"); // ユーザーIDをリクエスト属性から取得
        dish.setCreator(userId); // 作成者を設定
        dish.setCreateTime(LocalDateTime.now()); // 作成時間を現在時刻に設定

        // データベースに挿入
        dishMapper.addDish(dish); // Dishオブジェクトをデータベースに挿入

        // 画像が存在する場合は保存処理を実行
        if (fileName != null) {
            SaveFile saveFile = new SaveFile();
            saveFile.SaveImage(base64Image, dishImagePath, 800); // 大サイズ画像を保存
            saveFile.SaveImage(base64Image, orderImagePath, 400); // 小サイズ画像を保存
        }
    }

    @Override
    public void updateDishImage(Dish dish) {
        // 変数の初期化
        String base64Image = dish.getImage(); // Base64形式の画像データを取得
        String oldFileName = dish.getName();
        String fileName = null;
        ; // 画像ファイル名
        String dishImagePath = null; // 大サイズ画像の保存パス
        String orderImagePath = null; // 小サイズ画像の保存パス
        String oldDishImagePath = null; // 大サイズ画像の保存パス
        String oldorderImagePath = null; // 小サイズ画像の保存パス


        // 画像が存在する場合、ファイル名と保存パスを生成
        if (base64Image != null && !base64Image.isEmpty()) {
            fileName = UUID.randomUUID() + ".jpg"; // 一意のファイル名を生成
            dishImagePath = "dish-img" + File.separator + dish.getDishCategoryId() + File.separator + fileName; // 大サイズ画像のパス
            orderImagePath = "order-img" + File.separator + dish.getDishCategoryId() + File.separator + fileName; // 小サイズ画像のパス
            oldDishImagePath = "dish-img" + File.separator + dish.getDishCategoryId() + File.separator + oldFileName; // 大サイズ画像のパス
            oldorderImagePath = "order-img" + File.separator + dish.getDishCategoryId() + File.separator + oldFileName; // 小サイズ画像のパス
            dish.setImage(fileName); // 画像ファイル名をDishオブジェクトに設定

            // 作成者と作成時間を設定
            String userId = (String) request.getAttribute("userId"); // ユーザーIDをリクエスト属性から取得
            dish.setUpdater(userId); // 作成者を設定
            dish.setUpdateTime(LocalDateTime.now()); // 作成時間を現在時刻に設定

            // データベースに挿入
            dishMapper.updateDishImage(dish); // Dishオブジェクトをデータベースに挿入

            // 画像が存在する場合は保存処理を実行
            SaveFile saveFile = new SaveFile();
            if (saveFile.deleteFile(oldDishImagePath)&&saveFile.deleteFile(oldorderImagePath)) {
                saveFile.SaveImage(base64Image, dishImagePath, 800); // 大サイズ画像を保存
                saveFile.SaveImage(base64Image, orderImagePath, 400); // 小サイズ画像を保存
            }
        }
    }

    @Override
    public void updateDishInfo(Dish dish) {
        //  更新者と更新時間を設定
        String userId = (String) request.getAttribute("userId"); // ユーザーIDをリクエスト属性から取得
        dish.setUpdater(userId); // 作成者を設定
        dish.setUpdateTime(LocalDateTime.now()); // 作成時間を現在時刻に設定
        dishMapper.updateDishInfo(dish);
    }
}
