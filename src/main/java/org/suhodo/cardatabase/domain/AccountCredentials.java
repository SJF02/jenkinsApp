package org.suhodo.cardatabase.domain;

// 값을 처음에 안전하게 보관하고, 꺼내쓰도록 하는 Vo 클래스
public record AccountCredentials(String username, String password) {}

/*
public class AccountCredentials {
    private final String username;
    private final String password;

    public AccountCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountCredentials)) return false;
        AccountCredentials that = (AccountCredentials) o;
        return username.equals(that.username) && password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "AccountCredentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
 */