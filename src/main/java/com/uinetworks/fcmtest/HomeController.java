package com.uinetworks.fcmtest;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static final String FIREBASE_CONFIG_PATH = "eleccarfcmtest-firebase-adminsdk-42emz-01148a293c.json";

	@PostConstruct
	@RequestMapping(value = "/tokencheck", method = RequestMethod.GET)
	public void fcmTokenCheck() {
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(
							GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream()))
					.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				logger.info("Firebase application has been initialized");
				logger.info(options.getDatabaseUrl() + "\n" + options.getProjectId() + "\n"
						+ options.getServiceAccountId() + "\n" + options.getJsonFactory());
			}
			logger.info("tokencheck1");
		} catch (IOException e) {
			logger.info("tokencheck2");
			logger.error(e.getMessage());
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public boolean register(@RequestBody String token) {
	    Map<Integer, String> tokenMap = new HashMap<Integer, String>();
        tokenMap.put(1, token);
        return true;
	}

	@RequestMapping(value = "/fcmTest", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public void fcmTest() throws Exception {
		try {

			String path = "D:/workspace/fcmTester/src/main/webapp/resources/eleccarfcmtest-firebase-adminsdk-42emz-01148a293c.json";
			String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
			String[] SCOPES = { MESSAGING_SCOPE };

			GoogleCredential googleCredential = GoogleCredential.fromStream(new FileInputStream(path))
					.createScoped(Arrays.asList(SCOPES));
			googleCredential.refreshToken();

			HttpHeaders headers = new HttpHeaders();
			headers.add("content-type", MediaType.APPLICATION_JSON_VALUE);
			headers.add("Authorization", "Bearer " + googleCredential.getAccessToken());

			logger.info("GoogleCredential : " + googleCredential.getAccessToken());

			JSONObject notification = new JSONObject();
			notification.put("body", "FCM 테스트");
			notification.put("title", "완료");

			JSONObject message = new JSONObject();
			message.put("token",
					"erZxHPnpQGidAxjq-vuPRX:APA91bGyKdBDEnKgnJFetr_RJd4zyZd7TD8dDF7KXz3ARlND2W0PVaDBVqos5VIQZFAN08wFnnzzI2c4Sd_Bc0qMgqYgDcpIZoiBIEk6-VwGbfGfaQzZrI8nHnN4A8OJz_KlO4qND9bF");
			message.put("notification", notification);

			JSONObject jsonParams = new JSONObject();
			jsonParams.put("message", message);

			HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonParams, headers);
			RestTemplate rt = new RestTemplate();

			ResponseEntity<String> res = rt.exchange(
					"https://fcm.googleapis.com/v1/projects/eleccarfcmtest/messages:send", HttpMethod.POST, httpEntity,
					String.class);

			if (res.getStatusCode() != HttpStatus.OK) {
				logger.info("FCM-Exception");
				logger.info(res.getStatusCode().toString());
				logger.info(res.getHeaders().toString());
				logger.info(res.getBody().toString());

			} else {
				logger.info(res.getStatusCode().toString());
				logger.info(res.getHeaders().toString());
				logger.info(res.getBody().toLowerCase());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
