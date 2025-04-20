package com.pdfai.pdfai.dto;

public class EditorDeltaJSON {
    private String fullDoc;
    private String updateDoc;
    private String uuid;
    private String version;
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
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
