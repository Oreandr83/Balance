package models;

import com.google.gson.annotations.SerializedName;

public class AuthResult {

    public String status;
    public int id;
    @SerializedName("auth-token")
    public String token;
}
