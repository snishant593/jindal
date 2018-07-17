package Interface;
import Pojo.Login;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Nishant on 16/1/18.
 */
public interface LoginInterface {
    @FormUrlEncoded
    @POST("/JindalApi/Login.jsp")
    Call<Login> registration(@Field("Email") String email, @Field("Pass") String password);
}