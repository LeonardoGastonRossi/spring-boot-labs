package io.pivotal.workshop;

public class NewSnippetFields {

    public final String title;
    public final String code;

    public NewSnippetFields(String title, String code) {
        this.title = title;
        this.code = code;
    }

    private NewSnippetFields() {
        this(null, null);
    }
}
