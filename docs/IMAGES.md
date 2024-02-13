# Загрузка фотографии в личное сообщение
Допустимые форматы: JPG, PNG, GIF.

Ограничения:
- Сумма высоты и ширины не более 14 000 пикселей.
- Файл объёмом не более 50 Мбайт.
- Соотношение сторон не менее 1:20.

## Получение адреса
Чтобы получить адрес для загрузки фотографии, вызовите метод [photos.getMessagesUploadServer](https://dev.vk.com/ru/method/photos.getMessagesUploadServer). Чтобы загрузить фотографию в сообщения сообщества, если вы используете не Standalone-приложение, передайте идентификатор сообщества в параметр `group_id`.

### Запрос
```Shell
curl 'https://api.vk.com/method/photos.getMessagesUploadServer' \
-F 'access_token=vk1.a.LBFl2Ygq_qRUimYp5BbGXT9XXUecYJv1ad-44FyA2Vb9KKHrlDPElLIbhpIgctLZ5VFuKJrUdMCFJkD7APtNzfUhDtK8ul59hYvMiRvgex2p14J8CIMQw2m5ZdGF644LMrM1sP0h0P0RVfh7dEA2iGB4zEXciFeHg9tYnbqa8Zyj8LlP12PjZHgiMCBdnGLO7ZWkjGFL-RzOPmRUaYlWeg' \
-F 'group_id=218252175' \
-F 'v=5.131'
```
### Ответ
```JSON
{
  "response": {
    "album_id": -64,
    "upload_url": "https:\/\/pu.vk.com\/c231331\/ss2269\/upload.php?act=do_add&mid=743784474&aid=-64&gid=218252175&hash=2e60ee02a7dd5aaaf0bce3e034b1dc30&rhash=eda561bac91afb0270fc9ca741ac626b&swfupload=1&api=1&mailphoto=1",
    "user_id": 0,
    "group_id": 218252175
  }
}
```

## Передача файла
### Запрос

Чтобы передать файлы, используйте:

- Адрес upload_url из раздела Получение адреса.
- HTTP-глагол POST.
- Поле photo.
- HTTP-формат multipart/form-data.
```Shell
curl -F 'photo=@<ПОЛНЫЙ_ПУТЬ_ДО_ФОТОГРАФИИ>' '<UPLOAD_URL>'
```
> Примечание. Чтобы отправить корректный запрос, удалите экранирование (символ \) из параметра upload_url.

Пример запроса:

```Shell
curl -F 'photo=@/Users/persik/Downloads/image1.png' \
'https://pu.vk.com/c231331/ss2269/upload.php?act=do_add&mid=743784474&aid=-64&gid=218252175&hash=2e60ee02a7dd5aaaf0bce3e034b1dc30&rhash=eda561bac91afb0270fc9ca741ac626b&swfupload=1&api=1&mailphoto=1'
```
### Ответ
После успешной загрузки сервер возвращает JSON-объект:

| Поле   | Тип     | Описание                                                |
|--------|---------|---------------------------------------------------------|
| server | integer | Идентификатор сервера, на который загружена фотография. |
| photo  | string  | Информация о загруженной фотографии.                    |                
| hash   | string  | Хеш фотографии.                                         |

Пример ответа:

```JSON
{
  "server": 231331,
  "photo": "[{\"markers_restarted\":true,\"photo\":\"f7cdd492d6:w\",\"sizes\":[],\"latitude\":0,\"longitude\":0,\"kid\":\"63339bce9aa7110b118709d208f4f605\",\"sizes2\":[[\"s\",\"2169b25ce7ab50db8883a22c2f01f10e3b14030beca59a61e5184e40\",\"-8077041814521722093\",75,50],[\"m\",\"532f75fa48381e7c22ac2208fd0dc09585fca2e7c7af8fe73e51ad10\",\"2481357305209911761\",130,87],[\"x\",\"04ec4c4702bcaed3a751c1be7cecdfac77707c1fb6baba086f75ba69\",\"7060873775304692609\",604,402],[\"y\",\"735b15dc188c8d9f7665e63d10d65f709d5c2c5fcc7c1cf4235887dc\",\"-247552755041223595\",807,537],[\"z\",\"69dd5cf636aed29fb13f019924fe256e4ea61465304f6a58ad687d2b\",\"-5038604692102399322\",1280,852],[\"w\",\"8772eaa43776efbc784d0ffa0459eb78f498958302859a574c815b7e\",\"22189486849580549\",2560,1704],[\"o\",\"532f75fa48381e7c22ac2208fd0dc09585fca2e7c7af8fe73e51ad10\",\"2481357305209911761\",130,87],[\"p\",\"b272a37f1d0e68a1041b1e530f021de86de255f0668c086fe8207c75\",\"-3609630020141000359\",200,133],[\"q\",\"61208ee4d94149963f6d7db4e0ae2e3a6bdde6fef60e5a81c7e76081\",\"-4859686797456740524\",320,213],[\"r\",\"e0a963afe97cb480055bdce5cfcb756c15a0e86af7b461eba0db05d0\",\"4955764010854965486\",510,340]],\"urls\":[],\"urls2\":[\"IWmyXOerUNuIg6IsLwHxDjsUAwvspZph5RhOQA/E8t0gUOV6I8.jpg\",\"Uy91-kg4HnwirCII_Q3AlYX8oufHr4_nPlGtEA/0XnbgVKNbyI.jpg\",\"BOxMRwK8rtOnUcG-fOzfrHdwfB-2uroIb3W6aQ/gVPo3ElD_WE.jpg\",\"c1sV3BiMjZ92ZeY9ENZfcJ1cLF_MfBz0I1iH3A/VTyZtBSEkPw.jpg\",\"ad1c9jau0p-xPwGZJP4lbk6mFGUwT2pYrWh9Kw/prqea7pHE7o.jpg\",\"h3LqpDd277x4TQ_6BFnrePSYlYMChZpXTIFbfg/Bd5qgTjVTgA.jpg\",\"Uy91-kg4HnwirCII_Q3AlYX8oufHr4_nPlGtEA/0XnbgVKNbyI.jpg\",\"snKjfx0OaKEEGx5TDwId6G3iVfBmjAhv6CB8dQ/WdnoDp8E6M0.jpg\",\"YSCO5NlBSZY_bX204K4uOmvd5v72DlqBx-dggQ/VG_v_Zrsjrw.jpg\",\"4Kljr-l8tIAFW9zlz8t1bBWg6Gr3tGHroNsF0A/7uCscRxpxkQ.jpg\"]}]",
  "hash": "4dc524efee95578c69883f897087bd77"
}
```
## Сохранение результата

Чтобы сохранить фотографию в личном сообщении, вызовите метод photos.saveMessagesPhoto с параметрами, полученными на предыдущем этапе.

> Примечание. На этом этапе фотография сохранится в системный альбом. Чтобы опубликовать её, выполните инструкции этапа Публикация фотографии.

### Запрос
```Shell
curl 'https://api.vk.com/method/photos.saveMessagesPhoto' \
-F 'access_token=vk1.a.LBFl2Ygq_qRUimYp5BbGXT9XXUecYJv1ad-44FyA2Vb9KKHrlDPElLIbhpIgctLZ5VFuKJrUdMCFJkD7APtNzfUhDtK8ul59hYvMiRvgex2p14J8CIMQw2m5ZdGF644LMrM1sP0h0P0RVfh7dEA2iGB4zEXciFeHg9tYnbqa8Zyj8LlP12PjZHgiMCBdnGLO7ZWkjGFL-RzOPmRUaYlWeg' \
-F 'photo=[{"markers_restarted":true,"photo":"f7cdd492d6:w","sizes":[],"latitude":0,"longitude":0,"kid":"63339bce9aa7110b118709d208f4f605","sizes2":[["s","2169b25ce7ab50db8883a22c2f01f10e3b14030beca59a61e5184e40","-8077041814521722093",75,50],["m","532f75fa48381e7c22ac2208fd0dc09585fca2e7c7af8fe73e51ad10","2481357305209911761",130,87],["x","04ec4c4702bcaed3a751c1be7cecdfac77707c1fb6baba086f75ba69","7060873775304692609",604,402],["y","735b15dc188c8d9f7665e63d10d65f709d5c2c5fcc7c1cf4235887dc","-247552755041223595",807,537],["z","69dd5cf636aed29fb13f019924fe256e4ea61465304f6a58ad687d2b","-5038604692102399322",1280,852],["w","8772eaa43776efbc784d0ffa0459eb78f498958302859a574c815b7e","22189486849580549",2560,1704],["o","532f75fa48381e7c22ac2208fd0dc09585fca2e7c7af8fe73e51ad10","2481357305209911761",130,87],["p","b272a37f1d0e68a1041b1e530f021de86de255f0668c086fe8207c75","-3609630020141000359",200,133],["q","61208ee4d94149963f6d7db4e0ae2e3a6bdde6fef60e5a81c7e76081","-4859686797456740524",320,213],["r","e0a963afe97cb480055bdce5cfcb756c15a0e86af7b461eba0db05d0","4955764010854965486",510,340]],"urls":[],"urls2":["IWmyXOerUNuIg6IsLwHxDjsUAwvspZph5RhOQA/E8t0gUOV6I8.jpg","Uy91-kg4HnwirCII_Q3AlYX8oufHr4_nPlGtEA/0XnbgVKNbyI.jpg","BOxMRwK8rtOnUcG-fOzfrHdwfB-2uroIb3W6aQ/gVPo3ElD_WE.jpg","c1sV3BiMjZ92ZeY9ENZfcJ1cLF_MfBz0I1iH3A/VTyZtBSEkPw.jpg","ad1c9jau0p-xPwGZJP4lbk6mFGUwT2pYrWh9Kw/prqea7pHE7o.jpg","h3LqpDd277x4TQ_6BFnrePSYlYMChZpXTIFbfg/Bd5qgTjVTgA.jpg","Uy91-kg4HnwirCII_Q3AlYX8oufHr4_nPlGtEA/0XnbgVKNbyI.jpg","snKjfx0OaKEEGx5TDwId6G3iVfBmjAhv6CB8dQ/WdnoDp8E6M0.jpg","YSCO5NlBSZY_bX204K4uOmvd5v72DlqBx-dggQ/VG_v_Zrsjrw.jpg","4Kljr-l8tIAFW9zlz8t1bBWg6Gr3tGHroNsF0A/7uCscRxpxkQ.jpg"]}]' \
-F 'server=231331' \
-F 'hash=4dc524efee95578c69883f897087bd77' \
-F 'v=5.131'
```
### Ответ
```JSON
{
  "response": [
    {
      "album_id": -64,
      "date": 1673862629,
      "id": 457239023,
      "owner_id": -218252175,
      "access_key": "698e738862a7917b5b",
      "sizes": [
        {
          "height": 50,
          "type": "s",
          "width": 75,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/Vus7E6r8jZjgv5E9bnuM6fbvL9U_NP4-goegNOaEy8t4Z1DnzofjER9exwblecB6Hxb3EUbWv7lQvxdRaErZGoT3.jpg?size=75x50&quality=96&type=album"
        },
        {
          "height": 87,
          "type": "m",
          "width": 130,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/JTtJ-M4Y1Md4nbNyY6QNKBjs9xleCGkDwGw-NuMvLV0DKfQrPb_xN7QcfazSTrBcZ-_JzsJ21pTuLI7Slr8m9HcB.jpg?size=130x87&quality=96&type=album"
        },
        {
          "height": 402,
          "type": "x",
          "width": 604,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/2DBzUBeOMpydPcypQFkirgj6g9mzsj8le0qsrWQ_lPX3zNQN1229bLivxf26ya-91HF9D57exLSnkSnJwUxJdUBN.jpg?size=604x402&quality=96&type=album"
        },
        {
          "height": 537,
          "type": "y",
          "width": 807,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/Biye5eNVG4UA_ymuN60MU6Qp26yO7rYp0WB-ch55oxkaATpXs4Kmqqznz1keCYHg_BHyvPhyrSGyK3zRK29LoVKH.jpg?size=807x537&quality=96&type=album"
        },
        {
          "height": 852,
          "type": "z",
          "width": 1280,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/O-BkqGyWMw2ZKcOyYz8sH543Ihkws7mAn6x76JYh0mVW2MCR9x9eig_AS6gT6OLeySlvewx5oyri1Ejj0uNhJuKo.jpg?size=1280x852&quality=96&type=album"
        },
        {
          "height": 1704,
          "type": "w",
          "width": 2560,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/o5klH0kpqicWBkDGQl_ch2j8VRpW69xrnq_PXw823wrMYc2qnXQLuDZeECtcKSaka1gfCpP9smoz7XwGAMDTk7vo.jpg?size=2560x1704&quality=96&type=album"
        },
        {
          "height": 87,
          "type": "o",
          "width": 130,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/JTtJ-M4Y1Md4nbNyY6QNKBjs9xleCGkDwGw-NuMvLV0DKfQrPb_xN7QcfazSTrBcZ-_JzsJ21pTuLI7Slr8m9HcB.jpg?size=130x87&quality=96&type=album"
        },
        {
          "height": 133,
          "type": "p",
          "width": 200,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/dFvcZ_sYZeMJtmvotINsevf_0x4KbDxo-jcrZojRQtebIKvM0juMU9U9NjybaidOukkrImr2CWcW8u6IHdlceWKD.jpg?size=200x133&quality=96&type=album"
        },
        {
          "height": 213,
          "type": "q",
          "width": 320,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/GglL_Kv0x1_rnPwXwtTPZUMFg9sT_JB9xUSUeNvNIRapPRhGvQbQAaCwD57WBhUKU8sPD6-BhyadPIXaALqERkS1.jpg?size=320x213&quality=96&type=album"
        },
        {
          "height": 340,
          "type": "r",
          "width": 510,
          "url": "https:\/\/sun9-west.userapi.com\/sun9-45\/s\/v1\/ig2\/LnQwirb-SUb689R2k90Q8MwuwHJ0tfO03a0IkCeXObaQERRE2-UUyLBCTTLme2qkLcxXAekHVbkLMEZhRq5E6Ggr.jpg?size=510x340&quality=96&crop=2,0,2556,1704&type=album"
        }
      ],
      "text": "",
      "user_id": 100,
      "has_tags": false
    }
  ]
}
```

### Публикация фотографии
Чтобы прикрепить фотографию к личному сообщению, вызовите метод messages.send. В поле attachment укажите идентификатор фотографии в формате photo{owner_id}_{photo_id}, где:

- owner_id — поле owner_id из ответа метода photos.saveMessagesPhoto.
- photo_id — поле id из ответа метода photos.saveMessagesPhoto.

> Примечание. Чтобы отправить личное сообщение, сообщество должно иметь права на отправку личных сообщений. Чтобы получить права, пользователь должен отправить сообщение сообществу (не в чат сообщества).

Запрос
```Shell
curl 'https://api.vk.com/method/messages.send' \
-F 'access_token=vk1.a.LBFl2Ygq_qRUimYp5BbGXT9XXUecYJv1ad-44FyA2Vb9KKHrlDPElLIbhpIgctLZ5VFuKJrUdMCFJkD7APtNzfUhDtK8ul59hYvMiRvgex2p14J8CIMQw2m5ZdGF644LMrM1sP0h0P0RVfh7dEA2iGB4zEXciFeHg9tYnbqa8Zyj8LlP12PjZHgiMCBdnGLO7ZWkjGFL-RzOPmRUaYlWeg' \
-F 'attachment=photo-218252175_457239023' \
-F 'user_id=743784474' \
-F 'random_id=12345678' \
-F 'v=5.131'
```

Ответ
```JSON
{
  "response": 5
}
```
