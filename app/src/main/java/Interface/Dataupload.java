package Interface;


import Pojo.Login;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Nishant on 17/1/18.
 */

public interface Dataupload {


    @FormUrlEncoded
    @POST("/JindalApi/Camera_Off.jsp")
    Call<Login> dataUpload(@Field("dateTime") String datetime, @Field("Emp_Name") String username);
}

