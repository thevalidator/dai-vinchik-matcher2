# Капча

### Пример обработки
```java
String captchaSid = null;
String captchaImg = null;

try {
    vk.wall().post(actor).message("Hello world").execute();
} catch (ApiCaptchaException e) {
    captchaSid = e.getCaptchaSid();
    captchaImg = e.getCaptchaImg();
}

//Showing captcha image...

if (captchaImg != null) {
    vk.wall().post(actor)
        .message("Hello world")
        .captchaSid(captchaSid)
        .captchaKey(captchaKey)
        .execute();
}
```

## Ошибка с Captcha
Если какое-либо действие (например, отправка сообщения) выполняется пользователем слишком часто, то запрос к API может возвращать ошибку "Captcha needed". При этом пользователю понадобится ввести код с изображения и отправить запрос повторно с передачей введенного кода Captcha в параметрах запроса.

```JSON
{
  "error_code": 14,
  "error_msg": "Captcha needed"
}
```

Если возникает данная ошибка, то в сообщении об ошибке передаются также следующие параметры:

- captcha_sid — идентификатор captcha.
- captcha_img — ссылка на изображение, которое нужно показать пользователю, чтобы он ввел текст с этого изображения.

В этом случае следует запросить пользователя ввести текст с изображения captcha_img и повторить запрос, добавив в него параметры:

- captcha_sid — полученный идентификатор
- captcha_key — текст, который ввел пользователь

### Пример ошибки
```JSON
{
  "error": {
    "error_code": 14,
    "error_msg": "Captcha needed",
    "request_params": [
      {
        "key": "oauth",
        "value": "1"
      },
      {
        "key": "method",
        "value": "captcha.force"
      },
      {
        "key": "uids",
        "value": "66748"
      },
      {
        "key": "access_token",
        "value": "b9b5151856dcc745d785a6b604295d30888a827a37763198888d8b7f5271a4d8a049fefbaeed791b2882"
      }
    ],
    "captcha_sid": "239633676097",
    "captcha_img": "http:\/\/api.vk.com\/captcha.php?sid=239633676097&s=1"
  }
}
```