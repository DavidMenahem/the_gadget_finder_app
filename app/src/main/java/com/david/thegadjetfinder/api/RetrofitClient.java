package com.david.thegadjetfinder.api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private IGadgetFinderService iGadgetFinderService;
    private static RetrofitClient instance = null;

    private RetrofitClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IGadgetFinderService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iGadgetFinderService = retrofit.create(IGadgetFinderService.class);
    }
    public static synchronized RetrofitClient getInstance() {


        if(instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public IGadgetFinderService getIGadgetFinderService(){
        return iGadgetFinderService;
    }
}