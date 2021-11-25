package com.example.usercashapi;


import com.example.usercashapi.DTO.*;
import com.example.usercashapi.domains.Role;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    private final static String BASE_URL = "http://localhost:8080";
    // private final static Header authHeader = Map.Entry("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaW1hQWRtaW4iLCJleHAiOjE2Mzk0ODI1NjF9.zgIMRBdjVY5bKSe0DNfdCUe3Klt2jLZ8rJm2pqLRgbFvErFrz2WRgHURBf4SPJMmBOwmLDfVBOWOA-xawPWWqA");
    private final static HttpClient httpClient = HttpClients.createDefault();
    private final static Gson GSON = new Gson();
    static int maxId;

    public static void main(String[] args) throws IOException {
        var userDTO = UserDTO.of(
                "dimaAdmin", "password", new Role(2, "admin"));
        List<UserDTO> users = getUsers(userDTO);
        maxId = nextMaxId(users);
        log.info(" ==== maxId = " + maxId);

        for(int i = 0; i < 1; i++) {
            UserDTO randomUser = createRandomUser();
            log.info("==== 4 в цикле = " + randomUser);
        }

    }

    static HttpGet getHttpGet(String uri, UserDTO userDTO) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader(getBearer(userDTO));
        return httpGet;
    }
    static Header getBearer(UserDTO userDTO) throws IOException {
        var httpPost = new HttpPost(BASE_URL + "/login");
        httpPost.addHeader("Content-Type", "application/json");
        String jsonBody = GSON.toJson(userDTO);
        StringEntity stringEntity = new StringEntity(jsonBody);
        httpPost.setEntity(stringEntity);

        HttpResponse response = httpClient.execute(httpPost);
        return Arrays.stream(response.getAllHeaders())
                .filter(h -> h.getName().equals("Authorization")).findAny().get();
    }
    static HttpPost getHttpPost(String uri, UserDTO userDTO) throws IOException {
        var httpPost = new HttpPost(uri);
        httpPost.addHeader("Content-Type", "application/json");
//        httpPost.addHeader(getBearer(userDTO));
        String jsonBody = GSON.toJson(userDTO);
        StringEntity stringEntity = new StringEntity(jsonBody);
        httpPost.setEntity(stringEntity);
        return httpPost;
    }

    static String getJson(HttpResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        new BufferedReader(new InputStreamReader((response
                .getEntity().getContent()))).lines().forEach(sb::append);
        return sb.toString();
    }
    static List<UserDTO> getUsers(UserDTO userDTO) throws IOException {
        HttpResponse response = httpClient.execute(getHttpGet(
                BASE_URL + "/user/", userDTO));
        String json = getJson(response);
        System.out.println(json);
        UserDTO[] users  = GSON.fromJson(json, UserDTO[].class );
        log.info(" ==== 1 getUsers ===");
        return Arrays.stream(users).collect(Collectors.toList());

    }

    static int nextMaxId(List<UserDTO> users) {
        var userDto = users.stream()
                .max(Comparator.comparingInt(UserDTO::getId)).get();
        return userDto.getId();
    }
    static UserDTO createRandomUser() throws IOException {
        int index = new Random().nextInt(5);
        var userDto = UserDTO.of(
                ++maxId, "Abc" + maxId, 20 + index,
                maxId + "email@y", "123" + maxId );
        userDto.setProfile(new ProfileDTO(20).toProfile());
        int n = (int) (4120000 + Math.random() * 10000);
        userDto.setPhones(Set.of(new PhoneDTO("+" + n).toPhone()));
        log.info(" ==== 2 createRandomUser ===");
        return createUserDB(userDto);
    }

    static UserDTO createUserDB(UserDTO userDTO) throws IOException {
        HttpResponse response = httpClient.execute(getHttpPost(
                BASE_URL + "/user/sign-up", userDTO));
        String json = getJson(response);
        log.info(" ==== 3 createUserDB ===");
        return GSON.fromJson(json, UserDTO.class );
    }


    static UserAgeDTO updateAge(UserAgeDTO user) throws IOException {
        var httpPut = new HttpPut(BASE_URL + "/user/age");
        httpPut.addHeader("Content-Type", "application/json");
        httpPut.addHeader("Authorization" , "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaW1hQWRtaW4iLCJleHAiOjE2Mzk0ODI1NjF9.zgIMRBdjVY5bKSe0DNfdCUe3Klt2jLZ8rJm2pqLRgbFvErFrz2WRgHURBf4SPJMmBOwmLDfVBOWOA-xawPWWqA");

        HttpResponse response = httpClient.execute(httpPut);
        String json = getJson(response);
        System.out.println(json);

        return GSON.fromJson(json, UserAgeDTO.class );

    }

    static UserAgeDTO updateAge1(UserAgeDTO userAge) throws IOException {
        var httpPut = new HttpPut(BASE_URL + "/user/age");
        httpPut.addHeader("Content-Type", "application/json");
        httpPut.addHeader("Authorization" , "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaW1hQWRtaW4iLCJleHAiOjE2Mzk0ODI1NjF9.zgIMRBdjVY5bKSe0DNfdCUe3Klt2jLZ8rJm2pqLRgbFvErFrz2WRgHURBf4SPJMmBOwmLDfVBOWOA-xawPWWqA");
        String jsonBody = GSON.toJson(userAge);
        StringEntity stringEntity = new StringEntity(jsonBody);
        httpPut.setEntity(stringEntity);

        HttpResponse response = httpClient.execute(httpPut);
        String json = getJson(response);
        System.out.println(json);

        return GSON.fromJson(json, UserAgeDTO.class );

    }

  /* System.out.println("-----------getUsers----updateAge---------------------------");
        int baseAge = 21;
        List<UserDTO> allUsers = getUsers();
        System.out.println(allUsers);
        System.out.println("------------------------------------------");
        for(UserDTO user : allUsers) {
            User updatedUser = updateAge(user.setAge(baseAge++));
            System.out.println("------------------------------------------");
            System.out.println(updatedUser);
            System.out.println("------------------------------------------");
        }
        System.out.println("------------------------------------------");
        List<UserBase> allUsers2 = getUsers();
        System.out.println("-----??-----------------------------------");
        System.out.println(allUsers2);
        System.out.println("------------------------------------------");

System.out.println("-------------createUserDB-----------------------------");
        var userDto = UserDTO.of(
                1, "Abc", 20, "email@y", "123");
        userDto.setProfile(new ProfileDTO(20).toProfile());
        var set = new HashSet<Phone>();
        var phoneDto = new PhoneDTO();
        phoneDto.setValue("+78888888");
        set.add(phoneDto.toPhone());
        userDto.setPhones(set);

        UserDTO userDTO = createUserDB(userDto);
        System.out.println(userDTO + " ==== в мейне");
        System.out.println("------------------------------------------");

        int baseAge = 21;*/

}
