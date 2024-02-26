package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 27 May 2023*/

public class Model_QuestHistory {

    private String Quest_ID;

    private String Quest_Tittle;
    private String Quest_Type;
    private String Radio_Status;
    private String Inspection_ID;
    private String DescriptionValue;
    private String YesOptionsID;
    private String OptionsIsDefault;
    private String OptionsQuestionId;
    private String NoOptionsID;

    public Model_QuestHistory(String Quest_ID,
                              String Quest_Tittle,
                              String Quest_Type,
                              String Radio_Status,
                              String Inspection_ID,
                              String DescriptionValue,
                              String YesOptionsID,
                              String OptionsIsDefault,
                              String OptionsQuestionId,
                              String NoOptionsID) {

        this.Quest_ID = Quest_ID;
        this.Quest_Tittle = Quest_Tittle;
        this.Quest_Type = Quest_Type;
        this.Radio_Status = Radio_Status;
        this.Inspection_ID = Inspection_ID;
        this.DescriptionValue = DescriptionValue;
        this.YesOptionsID = YesOptionsID;
        this.OptionsIsDefault = OptionsIsDefault;
        this.OptionsQuestionId = OptionsQuestionId;
        this.NoOptionsID = NoOptionsID;

    }


    public String getQuest_ID() {
        return Quest_ID;
    }

    public void setQuest_ID(String quest_ID) {
        Quest_ID = quest_ID;
    }

    public String getQuest_Tittle() {
        return Quest_Tittle;
    }

    public void setQuest_Tittle(String quest_Tittle) {
        Quest_Tittle = quest_Tittle;
    }

    public String getQuest_Type() {
        return Quest_Type;
    }

    public void setQuest_Type(String quest_Type) {
        Quest_Type = quest_Type;
    }

    public String getRadio_Status() {
        return Radio_Status;
    }

    public void setRadio_Status(String radio_Status) {
        Radio_Status = radio_Status;
    }

    public String getInspection_ID() {
        return Inspection_ID;
    }

    public void setInspection_ID(String inspection_ID) {
        Inspection_ID = inspection_ID;
    }

    public String getDescriptionValue() {
        return DescriptionValue;
    }

    public void setDescriptionValue(String descriptionValue) {
        DescriptionValue = descriptionValue;
    }

    public String getYesOptionsID() {
        return YesOptionsID;
    }

    public void setYesOptionsID(String yesOptionsID) {
        YesOptionsID = yesOptionsID;
    }

    public String getOptionsIsDefault() {
        return OptionsIsDefault;
    }

    public void setOptionsIsDefault(String optionsIsDefault) {
        OptionsIsDefault = optionsIsDefault;
    }

    public String getOptionsQuestionId() {
        return OptionsQuestionId;
    }

    public void setOptionsQuestionId(String optionsQuestionId) {
        OptionsQuestionId = optionsQuestionId;
    }

    public String getNoOptionsID() {
        return NoOptionsID;
    }

    public void setNoOptionsID(String noOptionsID) {
        NoOptionsID = noOptionsID;
    }

}