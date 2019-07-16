package com.marafiki.android.models;

public class AccountsModel implements Account {

    private int user_type;
    private String firstName;
    private String lastName;
    private float account_balance;

    private float right_field_input;
    private float left_field_input;

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public float getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(float account_balance) {
        this.account_balance = account_balance;
    }

    public float getRight_field_input() {
        return right_field_input;
    }

    public void setRight_field_input(float right_field_input) {
        this.right_field_input = right_field_input;
    }

    public float getLeft_field_input() {
        return left_field_input;
    }

    public void setLeft_field_input(float left_field_input) {
        this.left_field_input = left_field_input;
    }
}
