package org.example.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.Emp;

@Mapper
public interface EmpMapper {
    // ユーザー名とパスワードに基づいて管理員を検索します
    @Select("SELECT * FROM employee WHERE id=#{id} AND password=#{password}")
    Emp has_employee(Emp emp);
}
