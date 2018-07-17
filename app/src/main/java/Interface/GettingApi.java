package Interface;


import Utility.ConfigItems;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GettingApi {

    @GET("/JindalApi/Download_Employee.jsp")
        //@POST("/Android/download1.php")
    Call<ConfigItems> load(@Query("Email") String Email);


}



