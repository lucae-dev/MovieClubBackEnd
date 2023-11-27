package com.movies.movie.app.Notifications;

import com.google.firebase.messaging.*;
import com.movies.movie.app.FCMToken.DeviceToken;
import com.movies.movie.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {


    @Autowired
    private NotificationRepository notificationRepository;




    public String sendNotification(String token, String title, String body) throws Exception {



        Message message = Message.builder()
                .setToken(token)
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setTitle(title) // Android-specific title, if different
                                .setBody(body) // Android-specific body, if different
                                .build())
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setAlert(ApsAlert.builder()
                                        .setTitle(title) // iOS-specific title, if different
                                        .setBody(body) // iOS-specific body, if different
                                        .build())
                                .setContentAvailable(true)
                                //.setPriority(10) // APNs priority 10 for 'high'
                                .build())
                        .build())
                .build();
        return FirebaseMessaging.getInstance().send(message);
    }




        public void saveNotification(Notification notification) {
            List<Notification> existingNotifications = notificationRepository.findByRecipientIdOrderByTimestampDesc(notification.getRecipient().getId());

            if (existingNotifications.size() >= 30) {
                // Remove the oldest notification
                notificationRepository.delete(existingNotifications.get(existingNotifications.size() - 1));
            }

            notificationRepository.save(notification);
        }


        public void followNotification(User follower, User recipient){
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setTimestamp(LocalDateTime.now());
        notification.setAction("Followed");
        notification.setTriggerUserName(follower.getUsername());
        notification.setTriggerUserImage(follower.getPropic());
        notification.setAttachedImageUrl(recipient.getPropic());
        notificationRepository.save(notification);
        String body = follower.getUsername().toString() + " ha iniziato a seguirti";
        String title = "Hai un nuovo follower!";
        for (DeviceToken deviceToken : recipient.getDeviceTokens()){
            try {
                System.out.println("This is the token: " + deviceToken.getToken().toString());
                String resp = sendNotification(deviceToken.getToken(), title, body);
                System.out.println(resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        }

    public void likedCollectionNotification(User follower, User recipient, String collectionName){
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setTimestamp(LocalDateTime.now());
        notification.setAction("Liked Collection");
        notification.setTriggerUserName(follower.getUsername());
        notification.setTriggerUserImage(follower.getPropic());
        notification.setAttachedImageUrl(recipient.getPropic());
        notificationRepository.save(notification);

        String body = follower.getUsername().toString() + " ha salvato la tua tua collezione";
        String title = collectionName + " ha fatto colpo!";
        for (DeviceToken deviceToken : recipient.getDeviceTokens()){
            try {
                sendNotification(deviceToken.getToken(), title, body);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
