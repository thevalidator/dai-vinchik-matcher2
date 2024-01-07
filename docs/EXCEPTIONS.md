## Обработка ошибок
### Общий случай

```java
try {
    vk.wall().post(actor)
        .message("Hello world")
        .execute();
} catch (ApiWallLinksForbiddenException e) {
    // Links posting is prohibited
} catch (ApiException e) {
    // Business logic error
} catch (ClientException e) {
    // Transport layer error
}
```

### Капча
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

### Обработка событий Callback API
Переопределите методы из класса CallbackApi, чтобы обрабатывать события:

```java
public class CallbackApiHandler extends CallbackApi {
    @Override
    public void messageNew(Integer groupId, Message message) {
        System.out.println(message.getText());
    }
}
```

### Обработка событий Callback API Long Poll
Включите Callback API Long Poll для нужного сообщества и выберите события, за которыми хотите следить.

```java
HttpTransportClient httpClient = HttpTransportClient.getInstance();
VkApiClient vk = new VkApiClient(httpClient);
vk.groups().setLongPollSettings(groupActor).enabled(true)
                                           .wallPostNew(true)
                                           .messageNew(true)
                                           .execute();
```

Переопределите методы из класса CallbackApiLongPoll, чтобы обрабатывать события, и создайте нужный конструктор:

```java
public class CallbackApiLongPollHandler extends CallbackApiLongPoll {
    public CallbackApiLongPollHandler(VkApiClient client, UserActor actor, Integer groupId) {
      super(client, actor, groupId);
    }

    public CallbackApiLongPollHandler(VkApiClient client, GroupActor actor) {
      super(client, actor);
    }

    @Override
    public void messageNew(Integer groupId, Message message) {
      System.out.println("messageNew: " + message.toString());
    }

    @Override
    public void wallPostNew(Integer groupId, WallPost wallPost) {
      System.out.println("wallPostNew: " + wallPost.toString());
    }
}
```

Чтобы использовать созданный CallbackApiLongPollHandler, который переопределяет методы из CallBackApiLongPoll, необходимо создать его экземпляр и вызвать метод run

```java
CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vk, groupActor);
handler.run();
```

Пример использования Callback API Long Poll можно найти в разделе examples как group-bot, который логирует все события.

### Обработка событий User Long Poll API

| Ошибка     | Описание                                                                                                                            |
|------------|-------------------------------------------------------------------------------------------------------------------------------------|
| "failed":1 | История событий устарела или была частично утеряна, приложение может получать события далее, используя новое значение ts из ответа. |
| "failed":2 | Истекло время действия ключа, нужно заново получить key методом messages.getLongPollServer.                                         |
| "failed":3 | Информация о пользователе утрачена, нужно запросить новые key и ts методом messages.getLongPollServer.                              |
| "failed":4 | Передан недопустимый номер версии в параметре version.                                                                              |

Обратите внимание, объекты в сообщении об ошибке могут содержать поля, не описанные в документации. Их необходимо игнорировать и не пытаться обработать.
