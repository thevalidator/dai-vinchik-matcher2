package ru.thevalidator.daivinchikmatcher2.service.daivinchik.model;

public class DaiVinchikUserProfile {

private Gender gender;
private Gender genderToSearch;
private String city;
private String profileText;

//private <?> photo;


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

    public String getProfileText() {
        return profileText;
    }

    public void setProfileText(String profileText) {
        this.profileText = profileText;
    }

}
