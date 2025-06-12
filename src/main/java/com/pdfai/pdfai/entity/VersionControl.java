package com.pdfai.pdfai.entity;

import jakarta.persistence.*;

@Entity
public class VersionControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "editorId", referencedColumnName = "editorId", nullable = false)
    private TextContent textContent;
    @Column(columnDefinition = "TEXT")
    private String deltaJson;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TextContent getTextContent() {
        return textContent;
    }

    public void setTextContent(TextContent textContent) {
        this.textContent = textContent;
    }

    public String getDeltaJson() {
        return deltaJson;
    }

    public void setDeltaJson(String deltaJson) {
        this.deltaJson = deltaJson;
    }
}
