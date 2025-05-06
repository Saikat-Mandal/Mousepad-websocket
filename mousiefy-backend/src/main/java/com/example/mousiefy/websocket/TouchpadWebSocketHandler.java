package com.example.mousiefy.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.awt.*;

public class TouchpadWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private Robot robot;

    public TouchpadWebSocketHandler() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("WebSocket connected: " + session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("Error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        System.out.println("WebSocket closed: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode node = mapper.readTree(message.getPayload().toString());

        String type = node.get("type").asText();

        if ("move".equals(type)) {
            double dx = node.get("dx").asDouble();
            double dy = node.get("dy").asDouble();

            double speedMultiplier = 0.5; // Tune this value to control speed
            double threshold = 0.5; //
            // Get current position and apply delta
            if (Math.abs(dx) > threshold || Math.abs(dy) > threshold) {
                PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                Point location = pointerInfo.getLocation();

                int newX = (int) (location.x + dx * speedMultiplier);
                int newY = (int) (location.y + dy * speedMultiplier);

                robot.mouseMove(newX, newY);
            }
        }

        if ("click".equals(type)) {
            robot.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
        }

    }
}
