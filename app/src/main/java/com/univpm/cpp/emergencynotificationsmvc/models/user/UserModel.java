package com.univpm.cpp.emergencynotificationsmvc.models.user;

public interface UserModel {

    public User getUser(String username);

    public User getLastGuestUser();

    public boolean newUser(User user);

    public boolean newGuestUser(int index);

    public boolean updateUser(String name, String surname, int age, String mobilephone,
                              String username, String email, String password);

    public boolean deleteUser(String username);

}
