package ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile;

public class DaiVinchikUserProfile {

    private int age;
    private Gender gender;
    private Gender genderToSearch;
    private String city;
    private String name;
    private String profileText;

//private <?> photo;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGenderToSearch() {
        return genderToSearch;
    }

    public void setGenderToSearch(Gender genderToSearch) {
        this.genderToSearch = genderToSearch;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileText() {
        return profileText;
    }

    public void setProfileText(String profileText) {
        this.profileText = profileText;
    }

    @Override
    public String toString() {
        return "DaiVinchikUserProfile{" +
                "age=" + age +
                ", gender=" + gender +
                ", genderToSearch=" + genderToSearch +
                ", city='" + city + '\'' +
                ", name='" + name + '\'' +
                ", profileText='" + profileText + '\'' +
                '}';
    }

}
