package com.pdfai.pdfai.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdfai.pdfai.dto.EditorDeltaJSON;
import com.pdfai.pdfai.entity.TextContent;
import com.pdfai.pdfai.repository.TextRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, Set<WebSocketSession>> groupSessions = new ConcurrentHashMap<>();
    private final Map<String,String> editorContent = new ConcurrentHashMap<>();
    private final Map<String,String> versionMap = new ConcurrentHashMap<>();
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        Long userId = (Long) attributes.get("userId");
        String uuid = (String) attributes.get("uuId");;
        if (isValidEditor(uuid)) {
            groupSessions.computeIfAbsent(uuid, k -> new HashSet<>()).add(session);
            System.out.println("User " + userId + " connected to room " + uuid);
            if(!editorContent.containsKey(uuid)){
                System.out.println("putting content to hashmap by ws");
                editorContent.put(uuid, textRepo.findByEditorId(uuid).getDeltaJson());
                versionMap.put(uuid, UUID.randomUUID().toString());
            }
        } else {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }
    @Autowired
    TextRepo textRepo;
    public boolean isValidEditor(String uuid) {
        return (textRepo.findByEditorId(uuid) != null);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String uuid = (String) attributes.get("uuId");;
        Set<WebSocketSession> groupSessionSet = groupSessions.getOrDefault(uuid, new HashSet<>());
        for (WebSocketSession s : groupSessionSet) {
            if (s.isOpen() && !s.equals(session)) {
                s.sendMessage(message);
            }
        }
    }
    public void sendUpdate(EditorDeltaJSON editorDeltaJSON) {
        Set<WebSocketSession> groupSessionSet = groupSessions.getOrDefault(editorDeltaJSON.getUuid(), new HashSet<>());
        if(editorDeltaJSON.getVersion().equals(this.versionMap.get(editorDeltaJSON.getUuid()))){
            editorContent.put(editorDeltaJSON.getUuid(), editorDeltaJSON.getFullDoc());
            String version=UUID.randomUUID().toString();
            System.out.println(this.editorContent.get(editorDeltaJSON.getUuid()));
            System.out.println(editorDeltaJSON.getFullDoc());
            editorDeltaJSON.setVersion(version);
            editorDeltaJSON.setFullDoc("");
            String jsonString;
            try {
                 jsonString=objectMapper.writeValueAsString(editorDeltaJSON);
            }
            catch (Exception e) {
                jsonString=editorDeltaJSON.getUpdateDoc();
            }
            versionMap.put(editorDeltaJSON.getUuid(), version);
            for (WebSocketSession session : groupSessionSet) {
                if (session.isOpen() ) {
                    try {
                        session.sendMessage(new TextMessage(jsonString));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String uuid = (String) attributes.get("uuId");;
        Long userId = (Long) attributes.get("userId");
        Set<WebSocketSession> groupSessionSet = groupSessions.getOrDefault(uuid, new HashSet<>());
        groupSessionSet.remove(session);
        if (groupSessionSet.isEmpty())
        {
            System.out.println("removing from Hashmap and putting the content into db");
            groupSessions.remove(uuid);
            String content = editorContent.get(uuid);
            editorContent.remove(uuid);
            TextContent textContent = textRepo.findByEditorId(uuid);
            textContent.setDeltaJson(content);
            textRepo.save(textContent);
        }
        System.out.println("User"+userId+" disconnected from group " + uuid);
    }
    public EditorDeltaJSON getEditorContent(String uuid) {
        EditorDeltaJSON editorDeltaJSON = new EditorDeltaJSON();
        if(editorContent.containsKey(uuid)) {
            System.out.println("fetched from hashmap");
            editorDeltaJSON.setFullDoc(editorContent.get(uuid));
            editorDeltaJSON.setVersion(versionMap.get(uuid));
        }
        try {
            String content =textRepo.findByEditorId(uuid).getDeltaJson();
            String versionId= UUID.randomUUID().toString();
            editorContent.put(uuid, content);
            versionMap.put(uuid, versionId);
            editorDeltaJSON.setFullDoc(content);
            editorDeltaJSON.setVersion(versionId);
        }

        catch (Exception e) {
            return null;
        }
        return editorDeltaJSON;
    }
    public void setEditorContent(EditorDeltaJSON editorDeltaJSON) {
        editorContent.put(editorDeltaJSON.getUuid(),editorDeltaJSON.getFullDoc());
    }
}
