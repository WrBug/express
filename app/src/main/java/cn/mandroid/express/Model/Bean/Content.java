package cn.mandroid.express.Model.Bean;

import cn.mandroid.express.Utils.CharacterParser;

public class Content {

    private String letter;
    private String username;
    private String avatarUrl;
    private String name;
    private String pinyinName;
    private boolean hide;
    public Content(UserBean user) {
        super();
        name = user.getName();
        avatarUrl = user.getAvatarUrl();
        username = user.getUsername();
        pinyinName = CharacterParser.getInstance().getSelling(name);
        letter = pinyinName.toUpperCase().substring(0, 1);
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
