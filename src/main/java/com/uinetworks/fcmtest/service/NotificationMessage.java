package com.uinetworks.fcmtest.service;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

public interface NotificationMessage {
	HttpEntity<JSONObject> setHttpEntity(JSONObject jsonParamsm, HttpHeaders headers);
}
