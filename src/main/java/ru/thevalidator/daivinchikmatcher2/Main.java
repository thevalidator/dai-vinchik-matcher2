package ru.thevalidator.daivinchikmatcher2;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.thevalidator.daivinchikmatcher2.account.UserAccount;
import ru.thevalidator.daivinchikmatcher2.config.SpringJavaConfig;
import ru.thevalidator.daivinchikmatcher2.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.service.impl.CaseMatcherImpl;
import ru.thevalidator.daivinchikmatcher2.task.request.DaiVinchikDialogHandler;
import ru.thevalidator.daivinchikmatcher2.vk.custom.transport.HttpTransportClientWithCustomUserAgent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        LOG.info("DAI-VINCHIK-MATCHER 2 -> START");

        var context = new AnnotationConfigApplicationContext(SpringJavaConfig.class);
        //CaseMatcher b = context.getBean(CaseMatcherImpl.class);
        //b.detectCase()
        System.out.println();

//        TransportClient transportClient = new HttpTransportClientWithCustomUserAgent("Java VK SDK/1.0");
//        VkApiClient vk = new VkApiClient(transportClient);
//
//        Path path = Paths.get("data/config/token");
//        String token = Files.readString(path);
//        LOG.debug("Token found: {}", token);
//        UserAccount account = new UserAccount("user", token);
//
//
//        //UserLongPollSpectatorService service = new UserLongPollSpectatorService(vk, account);
//        DaiVinchikDialogHandler service = new DaiVinchikDialogHandler(vk, account);
//
//        //new thread
//        Thread thread = new Thread(service);
//        thread.start();
//        thread.join();

        LOG.info("DAI-VINCHIK-MATCHER 2 -> END");


        //new thread by executor service
//        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
//            executor.execute(service);
//            TimeUnit.SECONDS.sleep(20);
//            service.stop();
//            LOG.info("DEACTIVATE INVOKED");
//        }


//        UserAuthResponse authResponse;
//        try {
//            authResponse = vk.oAuth()
//                    .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
//                    .execute();
//        } catch (OAuthException e) {
//            e.getRedirectUri();
//        }


    }

}


//[4, 1647082, 1, 755036964, 1703880683, Уже😂, {emoji=1, title= ... }, {}]
//[4, 1647097, 1, 755036964, 1703881695, , {title= ... }, {attach1_product_id=279, attach1_type=sticker, attach1=9027, attachments=[{"type":"sticker","sticker":{"images":[{"height":64,"url":"https://vk.com/sticker/1-9027-64","width":64},{"height":128,"url":"https://vk.com/sticker/1-9027-128","width":128},{"height":256,"url":"https://vk.com/sticker/1-9027-256","width":256},{"height":352,"url":"https://vk.com/sticker/1-9027-352","width":352},{"height":512,"url":"https://vk.com/sticker/1-9027-512","width":512}],"images_with_background":[{"height":64,"url":"https://vk.com/sticker/1-9027-64b","width":64},{"height":128,"url":"https://vk.com/sticker/1-9027-128b","width":128},{"height":256,"url":"https://vk.com/sticker/1-9027-256b","width":256},{"height":352,"url":"https://vk.com/sticker/1-9027-352b","width":352},{"height":512,"url":"https://vk.com/sticker/1-9027-512b","width":512}],"product_id":279,"sticker_id":9027}}], attachments_count=1}]
//[4, 1647098, 19, 755036964, 1703882134, не звонишь<br>слился?, {title= ... }, {}]
// voice
//[5, 1647110, 0, 755036964, 1703883055, , {title= ... }, {attach1_type=doc, attach1=755036964_673762416, attach1_kind=audiomsg, attachments=[{"type":"audio_message","audio_message":{"id":673762416,"owner_id":755036964,"duration":10,"waveform":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,7,14,1,6,1,7,12,6,3,3,9,1,9,0,23,0,14,3,0,0,0,0,0,0,0,0,18,3,31,13,9,2,5,7,11,2,20,20,14,2,13,3,1,10,3,11,2,0,18,6,5,0,0,0,0,0,0,0,0,0,0,0,1,15,16,4,10,10,10,1,8,4,3,5,1,3,20,3,0,4,1,0,0,0,0,0,3,0,1,2,4,2,4,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"link_ogg":"https://psv4.userapi.com/s/v1/amsg/O6cYO9g6urkeNan7mImp8sA3L5v9pCFcBnmLSLUzdeXtK1BHKvdfwlzSg3LzDcWDwtft.ogg","link_mp3":"https://psv4.userapi.com/s/v1/amsg/Di1GZ48rEo2ZeqRrD8cmXzPJopQ8FbmSkjdNZLFJ-1KhasXzAXX5v33bZYiiPvavhWAM.mp3","locale":"","is_recognized":0,"access_key":"1Y3f6QaOPZBFYQcHsOoZws4g0OJbeZdqQlNBaeBuF3s","transcript":"Тебе сколько лет девочка? Не пойму трубку, то возьми на телефоне. Сидишь. Я не пойму телефон, то возьми, потерял там не потеряла.","transcript_state":"done"}}], attachments_count=1}]
//response from me
//[4, 1647114, 2097171, 755036964, 1703883194, сделай лизай ей и все пройдет, {title= ... }, {reply={"conversation_message_id":44}, fwd=0_0}]
