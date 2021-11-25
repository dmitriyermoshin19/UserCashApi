package com.example.usercashapi.DTO;

import com.example.usercashapi.domains.Phone;
import com.example.usercashapi.domains.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PhoneDTO extends UserBase {
    @Size(min = 7, max = 10)
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    @JsonView(Views.Phone.class)
    private String value;

    public PhoneDTO(String value) {
        this.value = value;
    }

    public Phone toPhone() {
        var p = new Phone();
        p.setValue(value);
        return p;
    }
}
