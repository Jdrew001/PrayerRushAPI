package com.dtatkison.prayerrush.rushapi.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Friend
{
    @JsonProperty("username")
    public String username;

    @JsonProperty("friend")
    public String friendUsername;
}