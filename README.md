## catalogs-assets discription
- Android project for displaying images from UPYUN origin

## Features
- 登录的超级权限密码
- 每个随机密码只使用验证一次
- 分享、收藏夹功能

## 后期快速开发迭代
- 替换启动icon
- 替换背景图片
- 替换app显示文字

## JSON file
- categories.json：用于保存upyun上目录和缩略图的映射关系，由业务方提供
```
{
    "categories" : [
        {
        "name" : "Basin mixer",
        "directory" : "Basin mixer",
        "thumbnail" : "Basin mixer.jpg",
        "number": 20
        },
        {
        "name" : "Kitchen mixer",
        "directory" : "Kitchen mixer",
        "thumbnail" : "Kitchen mixer.jpg",
        "number": 16
        }
    ]
}
```
- contact.json：用户保存公司地址和业务员工联系方式，由业务方填写提供
```
{
    "website": "www.meiya.cn",
    "address": "No.568 HAIGONG ROAD, HAICHENG TOWN, LONGWAN, WENZHOU, ZHEJIANG, CHINA",
    "persons": [
        {
            "name": "Kobe",
            "contacts": [
                {
                    "paramKey": "Email",
                    "paramValue": "ruantihong@163.com"
                },
                {
                    "paramKey": "Phone",
                    "paramValue": "0577-86374678"
                },
                {
                    "paramKey": "Moblie",
                    "paramValue": "15158133123"
                }
            ]
        },
        {
            "name": "Allen",
            "contacts": [
                {
                    "paramKey": "Email",
                    "paramValue": "allen@gmail.com"
                },
                {
                    "paramKey": "Moblie",
                    "paramValue": "15158111123"
                },
                 {
                    "paramKey": "WeChat",
                    "paramValue": "210098775"
                }
            ]
        }
    ]
}
```
- login_codes.json 用户用户登录权限校验，每个密码只验证使用一次，json文件有Generator.jar生成
```
{
  "login_codes": [
    {
      "code": "jZbGGGOP",
      "valid": true
    },
    {
      "code": "b9zsOpBt",
      "valid": true
    }
  ]
}
```
## Login Code Generator
- usage: java -jar Generator.jar code_size code_amount
- default: java -jar Generator.jar 8 100

## icons-launcher
- https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html

## icon-resource
- http://www.iconpng.com/series/1117/
