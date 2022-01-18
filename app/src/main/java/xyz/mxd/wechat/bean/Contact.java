package xyz.mxd.wechat.bean;

public class Contact {

    private Integer id;
    private String img;
    private String name;
    private String friendid;
    private String number;


    public Contact() {
    }

    public Contact(Integer id, String img, String name, String friendid, String number) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.friendid = friendid;
        this.number = number;
    }

    public Contact(String img, String name, String friendid, String number) {
        this.img = img;
        this.name = name;
        this.friendid = friendid;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", friendid='" + friendid + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
