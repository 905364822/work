package anmav.boss.config;

public class var {
	// ������expressĳ��·���ļ�˵�����⣬һ��·����/api
    var express = require('express');
    var router = express.Router();
    router.get('/getsth', function(req, res, next) {
        // �Ƿ���Ҫ����
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
      // req��������һЩ�¶�
      // res����Ӧ��һЩ�¶�
      // next����һ���ص��¼�
    });
}
