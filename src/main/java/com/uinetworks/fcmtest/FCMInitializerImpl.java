package com.uinetworks.fcmtest;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FCMInitializerImpl implements FCMInitializer{

	private static final Logger logger = LoggerFactory.getLogger(FCMInitializer.class);
    private static final String FIREBASE_CONFIG_PATH = "eleccarfcmtest-firebase-adminsdk-42emz-01148a293c.json";

	@Override
	@PostConstruct
	public void initialize() {
		try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application has been initialized");
                logger.info(options.getDatabaseUrl() + "\n" + options.getProjectId() + "\n" + options.getServiceAccountId() + "\n" + options.getJsonFactory());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
	}
}
