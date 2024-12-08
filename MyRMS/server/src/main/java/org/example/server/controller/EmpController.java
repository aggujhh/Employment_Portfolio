package org.example.server.controller;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.server.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.example.common.Result;
import org.example.server.service.EmpService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.pojo.entity.Emp;
import org.example.common.Jwt;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class EmpController {

    private final EmpService empService;

    @Autowired
    public EmpController(EmpService empService) {
        // フィールドにインターセプターを設定
        this.empService = empService;
    }

    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        log.info("ログイン:{}", emp);
        Emp e = empService.login(emp);
        if (e != null) {
            // 新しいHashMapを作成して、クレームを格納します。
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("name", e.getName());
            //JWTを生成
            String jwt = Jwt.generateJet(claims);
            Map<String, Object> data = new HashMap<>();
            data.put("token", jwt);
            data.put("id", e.getId());
            data.put("name", e.getName());
            data.put("type", e.getType());
            data.put("job", e.getJob());
            String resultData = JSON.toJSONString(data);
            return Result.success(resultData);
        }
        return Result.error("ユーザー名またはパスワードが間違っています");
    }
}
