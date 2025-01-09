package org.example.common;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;

import java.io.*;
import java.util.Base64;

@Slf4j
public class SaveFile {
    private final String base_path;

    /**
     * コンストラクタ
     * 現在の作業ディレクトリの親ディレクトリを初期化します。
     */
    public SaveFile() {
        String currentDir = System.getProperty("user.dir");
        File currentDirFile = new File(currentDir);
        this.base_path = currentDirFile.getParent();
    }

    /**
     * 指定されたパスが存在するか確認し、存在しない場合はディレクトリを作成します。
     *
     * @param savePath 保存先のパス
     * @throws RuntimeException ディレクトリの作成に失敗した場合
     */
    public void PathCheckExample(String savePath) {
        File saveDir = new File(savePath).getParentFile(); // 親ディレクトリを取得
        if (!saveDir.exists() && !saveDir.mkdirs()) { // ディレクトリが存在しない場合は作成
            log.error("ディレクトリの作成に失敗しました: {}", saveDir.getAbsolutePath());
            throw new RuntimeException("保存先ディレクトリを作成できません: " + saveDir.getAbsolutePath());
        }
    }


    /**
     * Base64形式の画像を指定のパスに保存します。
     * 指定された幅と高さにリサイズし、画像比率を保持します。
     *
     * @param image     Base64形式の画像データ
     * @param file_path 保存先のファイルパス（例: "dish.jpg"）
     * @param Maxsize   画像の幅
     */
    public void SaveImage(String image, String file_path, Integer Maxsize) {
        // 保存パスを組み立て
        String savePath = base_path + File.separator + "MyFront" + File.separator + "public" + File.separator + file_path;
        PathCheckExample(savePath); // パスのチェックとディレクトリ作成

        // Base64データを分割して画像データ部分を取得
        String[] parts = image.split(",");
        if (parts.length < 2) { // データが不足している場合は例外をスロー
            throw new IllegalArgumentException("無効なBase64画像データです");
        }
        String imageData = parts[1]; // 実際の画像データ部分

        // Base64データをデコードしてバイト配列に変換
        byte[] imageBytes = Base64.getDecoder().decode(imageData);

        // Thumbnailsを使用して画像を保存
        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            Thumbnails.of(inputStream)  // InputStreamを入力として使用
                    .width(Maxsize)     // 幅と高さを指定
                    .keepAspectRatio(true)  // 比率を保持
                    .outputFormat("jpg")//拡張子設定
                    .toFile(savePath);  // 保存先パスに出力
            log.info("画像保存成功しました。保存場所は>>>{}", savePath);
        } catch (Exception e) {
            log.error("画像保存中にエラーが発生しました: ファイルパス: {}, エラー: ", file_path, e);
        }
    }

    /**
     * 指定されたパスのファイルを削除します。
     *
     * @param file_path 削除対象のファイルパス（例: "dish.jpg"）
     * @return ファイルが正常に削除された場合は true、それ以外は false
     */
    public boolean deleteFile(String file_path) {
        // 保存パスを組み立て
        String targetPath = base_path + File.separator + "MyFront" + File.separator + "public" + File.separator + file_path;
        File file = new File(targetPath);

        if (file.exists()) { // ファイルが存在するか確認
            if (file.delete()) { // ファイル削除
                log.info("ファイル削除成功しました。削除対象: {}", targetPath);
                return true;
            } else {
                log.error("ファイル削除に失敗しました: {}", targetPath);
                return false;
            }
        } else {
            log.warn("削除対象のファイルが存在しません: {}", targetPath);
            return true;
        }
    }

    public String getBasePath() {
        return base_path + File.separator + "MyFront" + File.separator + "public";
    }

}
