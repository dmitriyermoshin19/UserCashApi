package com.example.usercashapi.servises;

import com.example.usercashapi.DTO.*;
import com.example.usercashapi.domains.Profile;
import com.example.usercashapi.domains.Role;
import com.example.usercashapi.domains.User;
import com.example.usercashapi.repositories.PhoneRepository;
import com.example.usercashapi.repositories.ProfileRepository;
import com.example.usercashapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PhoneRepository phoneRepository;
    private final BCryptPasswordEncoder encoder;

    public List<UserDTO> findAllUserDTO() {
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
    }
    public List<User> findAll() {
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<User> findByPhone(String number) {
        return findAll().stream()
                .filter(u -> u.getPhones().stream()
                        .anyMatch(p -> p.getValue().equals(number)))
                .findAny();
    }

    public void increaseCash(Profile profile) {
        var cash = profile.getCash();
        final var value = cash;
        var buf = 0.0;
        int i = 0;
        while (true) {
            log.info("Сколько раз была итерация: " + ++i);
            cash *= 1.1;
            if (cash < value * 2.07) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                buf = Math.round(cash * 100.0) / 100.0;
            } else {
                break;
            }
        }
        profile.setCash((float)buf);
        profileRepository.save(profile);
        float cashFromBD = profileRepository.findById(profile.getId()).get().getCash();
        log.info("cash finish: " + buf);
        log.info("cashFromBD: " + cashFromBD);
    }

    public UserDTO createUser(UserDTO user) {
        var userEntity = user.toUser();
        var password = user.getPassword();
        userEntity.setPassword(encoder.encode(password));
        userEntity.setRole(new Role(1, "user"));
//        userEntity.setRole(new Role(2, "admin"));
        userRepository.save(userEntity);
//        user.setPassword(password);
        log.info("User [id = " + userEntity.getId() + "] successfully created");
        new Thread(() -> increaseCash(userEntity.getProfile())).start();
        return UserDTO.fromCreateUser(userEntity);
    }

    public boolean userCanUpdate(UserBase user) {
        if (user.getName() != null) {
            String username = (String) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            log.info("getPrincipal : " + username + " --- userDTO: " + user.getName());
            return user.getName().equals(username);
        }
        log.warn("User " + user + " not updated, by reason: name "
                + user.getName() + " is null ");
        return false;
    }

    public UserBase update(UserBase user, Consumer<User> uo) {
        if (userCanUpdate(user)) {
            var userOptional = userRepository.findByName(user.getName());
            if (userOptional.isPresent()) {
                var userOld = userOptional.get();
                uo.accept(userOld);
                userRepository.save(userOld);
                return user;
            }
            return null;
        }
        log.warn("User " + user + " not updated, by reason: name "
                + user.getName() + " не соответствует контексту ");
        return null;
    }

    public UserBase updateAge(UserAgeDTO user) {
        return update(user, (userOld) -> {
            userOld.setAge(user.getAge());
        });
    }

    public UserBase updateEmail(UserEmailDTO user) {
        return update(user, (userOld) -> {
            userOld.setEmail(user.getEmail());
        });
    }

    public UserBase updatePassword(UserPasswordDTO user) {
        return update(user, (userOld) -> {
            var password = user.getPassword();
            userOld.setPassword(encoder.encode(password));
        });
    }

    public UserBase addCash(ProfileDTO user) {
        return update(user, (userOld) -> {
            var cash = user.getCash();
            var oldCash = userOld.getProfile().getCash();
            userOld.getProfile().setCash(oldCash + cash);
        });
    }

    public UserBase addPhone(PhoneDTO user) {
        var userByPhone = this.findByPhone(user.getValue());
        if (userByPhone.isEmpty()) {
            return update(user, (userOld) -> {
                userOld.userAddPhone(user.toPhone());
            });
        }
        log.warn("You can not add this number");
        return null;
    }

    public void delete(int id) {
        var userRepositoryById = userRepository.findById(id);
        if (userRepositoryById.isPresent()) {
            userRepository.delete(userRepositoryById.get());
        }

    }
}
