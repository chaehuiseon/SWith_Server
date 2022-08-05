<<<<<<< Updated upstream
//package com.example.demo.userControllerTest;
//
//import com.example.demo.common.BaseTest;
//import com.example.demo.src.dto.request.PostSignUpReq;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@RunWith(SpringRunner.class)
////@EnableConfigurationProperties
//@SpringBootTest
//@AutoConfigureMockMvc
//public class SignUpTest extends BaseTest {
//    @Autowired
//    public MockMvc mockMvc;
//
//
//    @Test
//    @DisplayName("회원 가입 테스트(성공)")
//    public void signUpTestSuccess() throws Exception {
//
//        PostSignUpReq postSignUpReq = new PostSignUpReq(email, password, nickname, interest1, interest2, introduction);
//
////        mockMvc.perform(post("/v1/signUp")
////                //json 형식으로 데이터를 보낸다고 명시
////                .contentType(MediaType.APPLICATION_JSON)
////                //Map으로 만든 input을 json형식의 String으로 만들기 위해 objectMapper를 사용
////                .content(objectMapper.writeValueAsString(signUpRequestDto)))
////                //Http 200을 기대
////                .andExpect(status().isOk())
////                //화면에 결과를 출력
////                .andDo(print());
//
//        ResultActions result = mockMvc.perform(post("/v1/signUp")
//                .content(objectMapper.writeValueAsString(postSignUpReq))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        result.andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("email").value(email))
//                .andExpect(jsonPath("password").value(password))
//                .andExpect(jsonPath("introduction").value(introduction));
//    }
//}
=======
package com.example.demo.userControllerTest;

import com.example.demo.common.BaseTest;
import com.example.demo.src.dto.request.SignUpRequestDto;
import com.example.demo.src.entity.Interest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
//@EnableConfigurationProperties
@SpringBootTest
@AutoConfigureMockMvc
public class SignUpTest extends BaseTest {
    @Autowired
    public MockMvc mockMvc;


    @Test
    @DisplayName("회원 가입 테스트(성공)")
    public void signUpTestSuccess() throws Exception {

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(email, password, nickname, 1, 2, introduction);

//        mockMvc.perform(post("/v1/signUp")
//                //json 형식으로 데이터를 보낸다고 명시
//                .contentType(MediaType.APPLICATION_JSON)
//                //Map으로 만든 input을 json형식의 String으로 만들기 위해 objectMapper를 사용
//                .content(objectMapper.writeValueAsString(signUpRequestDto)))
//                //Http 200을 기대
//                .andExpect(status().isOk())
//                //화면에 결과를 출력
//                .andDo(print());

        ResultActions result = mockMvc.perform(post("/v1/signUp")
                .content(objectMapper.writeValueAsString(signUpRequestDto))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("password").value(password))
                .andExpect(jsonPath("introduction").value(introduction));
    }
}
>>>>>>> Stashed changes
