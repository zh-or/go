###用户
* 注册  
 ```
    req:   /user/login.do{phone, password, lat, lng, device_type}  
    res_success: {"state":0, "message":"", "data": {"token": xxx}}  
    res_error:   {"state":-3, "message":"错误信息", "data": null}  
 ```
* 登陆  
 ```
    req:   /user/login.do{phone, password, lat, lng}  
    res_success: {"state":0, "message":"", "data": {"token": xxx}}  
    res_error:   {"state":-3, "message":"错误信息", "data": null}  
 ```	    
* 修改信息
```
    req: /user/updateUserInfo.do {token, name, value}
    res_success: {}
    res_error:   {}
```