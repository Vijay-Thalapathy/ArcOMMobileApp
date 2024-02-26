package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Apr 2023*/

public class Model_NotesList {

    private String Notes_id;
    private String Notes_name;
    private String Notes_comments;
    private String displayname;


    public Model_NotesList(String Notes_id, String Notes_name, String Notes_comments,String displayname) {
        this.Notes_id = Notes_id;
        this.Notes_name = Notes_name;
        this.Notes_comments = Notes_comments;
        this.displayname = displayname;

    }

    public String getNotes_id() {
        return Notes_id;
    }

    public void setNotes_id(String notes_id) {
        Notes_id = notes_id;
    }

    public String getNotes_name() {
        return Notes_name;
    }

    public void setNotes_name(String notes_name) {
        Notes_name = notes_name;
    }

    public String getNotes_comments() {
        return Notes_comments;
    }

    public void setNotes_comments(String notes_comments) {
        Notes_comments = notes_comments;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }



}