package org.example.server.service;

import org.example.pojo.entity.QrCode;

import java.util.List;

public interface QrCodeService {
    List<QrCode> fetchAllQrCode();
}
