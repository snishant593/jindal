package Pojo;


/**
 * Created by Nishant on 16/1/18.
 */

public class Login {

    public String Login_by;
    public String Pass;
    public String success;
    public String Mobile;


    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    public String getLogin_by() {
        return Login_by;
    }

    public void setLogin_by(String login_by) {
        Login_by = login_by;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

}
