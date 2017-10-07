package io.pivotal.workshop;

public class SnippetInfo {

    private final String id;
    private final String title;
    private final String code;
    private final String created;
    private final String modified;

    public SnippetInfo(String id, String title, String code, String created, String modified) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.created = created;
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }
}
