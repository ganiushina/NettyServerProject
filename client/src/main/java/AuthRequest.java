public class AuthRequest extends AbstractMessage {

    private String login;
    private String password;
    private String nickName;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private boolean auth;

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public AuthRequest(String login, String password) {
        this.login = login;
        this.password = password;
        this.auth = false;
        this.nickName = "";
    }
}
