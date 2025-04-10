package com.pdfai.pdfai.entity;

import jakarta.persistence.*;

@Entity
public class TextContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String editorId;
    @Column(columnDefinition = "TEXT")
    private String deltaJson;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getDeltaJson() {
        return deltaJson;
    }

    public void setDeltaJson(String deltaJson) {
        this.deltaJson = deltaJson;
    }
}
