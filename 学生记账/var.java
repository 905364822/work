package anmav.boss.config;

public class var {
	// 这里以express某个路由文件说明问题，一级路由是/api
    var express = require('express');
    var router = express.Router();
    router.get('/getsth', function(req, res, next) {
        // 是否需要跨域
        // res.header('Access-Control-Allow-Origin', '*')
        var name = req.query.name
        var age = req.query.age
        res.json({
            status:0,
            msg:'success',
            data:{
                name : name,
                age:age
            }
        })
      // req对请求做一些事儿
      // res对响应做一些事儿
      // next，下一步回调事件
    });
}
