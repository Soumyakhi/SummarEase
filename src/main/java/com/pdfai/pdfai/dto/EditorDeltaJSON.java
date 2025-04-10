package com.pdfai.pdfai.dto;

public class EditorDeltaJSON {
    private String fullDoc;
    private String updateDoc;
    private String uuid;
    public String getFullDoc() {
        return fullDoc;
    }

    public void setFullDoc(String fullDoc) {
        this.fullDoc = fullDoc;
    }

    public String getUpdateDoc() {
        return updateDoc;
    }

    public void setUpdateDoc(String updateDoc) {
        this.updateDoc = updateDoc;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
