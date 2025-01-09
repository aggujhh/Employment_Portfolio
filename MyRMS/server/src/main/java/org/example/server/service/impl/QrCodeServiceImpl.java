package org.example.server.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.example.common.SaveFile;
import org.example.pojo.entity.QrCode;
import org.example.server.mapper.DeskMapper;
import org.example.server.mapper.QrCodeMapper;
import org.example.server.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeMapper qrCodeMapper;
    private final DeskMapper deskMapper;

    @Autowired
    public QrCodeServiceImpl(QrCodeMapper qrCodeMapper, DeskMapper deskMapper) {
        this.qrCodeMapper = qrCodeMapper;
        this.deskMapper = deskMapper;
    }

    @Override
    public List<QrCode> fetchAllQrCode() {
        List<QrCode> qrCodeList = new ArrayList<>();
        List<String> deskIds = deskMapper.fetchAllTableIds();
        deskIds.forEach(deskId -> {
            QrCode qrCode = qrCodeMapper.fetchQrCodeByDeskId(deskId);
            if (qrCode == null) {
                qrCode = QRCodeGenerator(deskId);
                qrCodeMapper.addQrCode(qrCode);
            } else if (qrCode.getUrl() == null) {
                qrCode = QRCodeGenerator(deskId);
                qrCodeMapper.updateQrCode(qrCode);
            }
            qrCodeList.add(qrCode);
        });
        return qrCodeList;
    }

    private QrCode QRCodeGenerator(String deskId) {
        String fileName = NanoIdUtils.randomNanoId();
        String qrContent = "https://www.cyg1995.xyz/order/" + deskId + "/" + fileName; // 二维码内容

        int width = 300;                         // 二维码宽度
        int height = 300;                        // 二维码高度

        try {
            // 生成二维码
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

            // 保存二维码到文件
            SaveFile saveFile = new SaveFile();
            String basePath = saveFile.getBasePath();
            String filePath = basePath + File.separator + "qrCodeImages" + File.separator + deskId + File.separator + "qr_" + fileName + ".png"; // 保存路径
            saveFile.PathCheckExample(filePath);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("二维码已生成：" + filePath);
            QrCode qrCode = new QrCode();
            qrCode.setDeskId(deskId);
            qrCode.setFileName(fileName);
            qrCode.setUrl(qrContent);
            return qrCode;
        } catch (WriterException |
                 IOException e) {
            System.err.println("生成二维码时发生错误：" + e.getMessage());
            return null;
        }

    }

}
