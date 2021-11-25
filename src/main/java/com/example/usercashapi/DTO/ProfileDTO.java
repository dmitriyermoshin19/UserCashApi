package com.example.usercashapi.DTO;

import com.example.usercashapi.domains.Profile;
import com.example.usercashapi.domains.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class ProfileDTO extends UserBase {
    @Min(value = 10)
    @JsonView(Views.Create.class)
    @Column(name = "cash")
    private float cash;

        public ProfileDTO(float cash) {
        this.cash = cash;
    }

    public Profile toProfile() {
        var p = new Profile();
        p.setCash(cash);
        return p;
    }
}
