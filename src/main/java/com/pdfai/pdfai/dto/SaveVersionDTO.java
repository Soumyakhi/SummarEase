package com.pdfai.pdfai.dto;

public class SaveVersionDTO {
    private String deltaJson;
    private String editorId;
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
