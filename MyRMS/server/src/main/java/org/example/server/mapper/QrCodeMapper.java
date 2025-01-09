package org.example.server.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.entity.QrCode;

@Mapper
public interface QrCodeMapper {
    @Select("SELECT * FROM qr_code WHERE deskId=#{deskId}")
    QrCode fetchQrCodeByDeskId(String deskId);

    @Insert("INSERT INTO qr_code VALUES (#{deskId},#{fileName},#{url})")
    void addQrCode(QrCode qrCode);

    @Update("UPDATE qr_code SET fileName=#{fileName},url=#{url} " +
            "WHERE deskId=#{deskId}")
    void updateQrCode(QrCode qrCode);
}
