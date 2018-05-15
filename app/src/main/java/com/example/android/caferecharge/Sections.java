package com.example.android.caferecharge;

public class Sections {
    private String section_image;
    private String section_text;

    public Sections(){}

    public Sections(String a, String b){
        this.section_image=a;
        this.section_text=b;
    }

    public void setSection_image(String section_image) {
        this.section_image = section_image;
    }

    public void setSection_text(String section_text) {
        this.section_text = section_text;
    }

    public String getSection_image() {
        return section_image;
    }

    public String getSection_text() {
        return section_text;
    }
}
