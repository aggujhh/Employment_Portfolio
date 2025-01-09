package org.example.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.OrderCompletion;
import org.example.pojo.entity.QrCode;
import org.example.server.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/qrCode")
public class QrCodeController {
    private final QrCodeService qrCodeService;

    @Autowired
    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping
    public Result fetchAllQrCode() {
        log.info("すべてのQRコードフェッチ。");
        List<QrCode> results = qrCodeService.fetchAllQrCode();
        log.info("フェッチ成功:{}", results);
        return Result.success(results);
    }
}
