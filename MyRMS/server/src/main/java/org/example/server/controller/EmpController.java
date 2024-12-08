package org.example.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.example.common.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.pojo.entity.Emp;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class EmpController {
//    @Autowired
//    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        log.info("ログイン:{}", emp);
        return Result.success("good");
//        Administrator a = adminService.login(admin);
//        if (a != null) {
//            // 新しいHashMapを作成して、クレームを格納します。
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("id", a.getId());
//            claims.put("name", a.getName());
//            claims.put("username", a.getUsername());
//            //JWTを生成
//            String jwt = JwtUtils.generateJet(claims);
//            Map<String, Object> data = new HashMap<>();
//            data.put("token", jwt);
//            data.put("name", a.getName());
//            data.put("jobTitle", a.getJobTitle());
//            String resultData = JSON.toJSONString(data);
//            return Result.success(resultData);
//        }
//        return Result.error("ユーザー名またはパスワードが間違っています");
    }
}
