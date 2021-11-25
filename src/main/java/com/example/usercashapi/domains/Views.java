package com.example.usercashapi.domains;

public class Views {
    public interface User extends UpdateAge, Email {
    }

    public interface Create extends User, AddCash, Phone {
    }

    public interface UpdateAge {
    }

    public interface Email extends UpdateAge {
    }

    public interface Phone extends UpdateAge {
    }

    public interface UpdatePassword extends UpdateAge {
    }

    public interface AddCash extends UpdateAge {
    }
}