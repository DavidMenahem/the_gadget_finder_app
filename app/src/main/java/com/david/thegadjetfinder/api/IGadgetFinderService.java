package com.david.thegadjetfinder.api;

import com.david.thegadjetfinder.model.Gadget;
import com.david.thegadjetfinder.model.Login;
import com.david.thegadjetfinder.model.Response;
import com.david.thegadjetfinder.model.Register;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IGadgetFinderService {
    String BASE_URL = "https://the-gadget-finder.ew.r.appspot.com/";

    @GET("/dummy/connection/{dummy}")
    Call<String> dummy(@Path("dummy") String dummy);
    @POST("/login")
    Call<Response> login(@Body Login login);

    @POST("/user/register")
    Call<Response> register(@Body Register register);

    @POST("/gadget/add")
    Call<Response> addGadget(@Body Gadget gadget);

    @GET("/gadget/all/{UserID}")
    Call<List<Gadget>> getALL(@Path("UserID") Long UserID);

    @DELETE("/gadget/{id}")
    Call<Void> delete(@Path("id") Long id);
}
