package Interface;


import Pojo.Login;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Nishant on 23/1/18.
 */

public interface Appinstall {

    @FormUrlEncoded
    @POST("/JindalApi/Install_Active.jsp")
    Call<Login> InstallUpload(@Field("Emp_Name") String username,@Field("dateTime") String datetime,@Field("Active") String active);

    @FormUrlEncoded
    @POST("/JindalApi/Unstall_App.jsp")
    Call<Login> UnInstallUpload(@Field("Emp_Name") String username,@Field("dateTime") String datetime,@Field("Unstall") String uninstall);

    @FormUrlEncoded
    @POST("/JindalApi/InternetOnOff.jsp")
    Call<Login> MobileoffUpload(@Field("Emp_Name") String username,@Field("dateTime") String datetime,@Field("ON_OFF") String onoff);

    @FormUrlEncoded
    @POST("/JindalApi/EmployeeNotified.jsp")
    Call<Login> UninstalluploadSetting(@Field("Emp_Name") String username,@Field("dateTime") String datetime,@Field("ON_OFF") String onoff);


}
///JindalApi/InternetOnOff.jsp

//http://35.194.196.229:8080/JindalApi/Unstall_App.jsp