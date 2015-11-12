package com.sgh.swinburne.heartplus.PillEducation;

/**
 * Created by Saad on 11/12/2015.
 */
public class Interaction {
    int id;
    String interaction_name;
    String interaction_description;

    public Interaction() {

    }

    public Interaction(String interaction_name, String interaction_description) {
        this.interaction_name = interaction_name;
        this.interaction_description = interaction_description;
    }

    public Interaction(int id, String interaction_name, String interaction_description) {
        this.id = id;
        this.interaction_name = interaction_name;
        this.interaction_description = interaction_description;
    }

    public void setInteration_id(int id) {
        this.id = id;
    }

    public void setInteraction_name(String interaction_name) {
        this.interaction_name = interaction_name;
    }

    public void setInteraction_description(String interaction_description) {
        this.interaction_description = interaction_description;
    }

    public int getInteration_id() {
        return this.id;
    }

    public String getInteraction_name() {
        return this.interaction_name;
    }

    public String getInteraction_description() {
        return this.interaction_description;
    }
}
