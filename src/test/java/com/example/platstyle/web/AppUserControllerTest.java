package com.example.platstyle.web;

import com.example.platstyle.entities.Stylist;
import com.example.platstyle.repositories.StylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class AppUserControllerTest {

    Stylist stylist;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    StylistRepository stylistRepository;

    @Mock
    View mockView;

    @InjectMocks
    UserController userController;

    @InjectMocks
    StylistController stylistController;

    @BeforeEach
    void setUp() {
        stylist = new Stylist();
        stylist.setSid(Long.valueOf(1));
        stylist.setEmail("stylist@email.com");
        stylist.setName("Tom TheCat");
        stylist.setVerify(true);
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(userController).setSingleView(mockView).build();
    }

    @Test
    public void findAllVerifiedStylist() throws Exception {
        List<Stylist> list = new ArrayList<Stylist>();
        list.add(stylist);
        list.add(stylist);
        when(stylistRepository.findAllVerifiedStylist()).thenReturn(list);
        mockMvc.perform(get("/user/shop"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("stylistList", list))
                // .andExpect(view().name(""))
                .andExpect(model().attribute("stylistList", hasSize(2)));
        verify(stylistRepository, times(1)).findAllVerifiedStylist();
        verifyNoMoreInteractions(stylistRepository);
    }

//    User user;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    View mockView;
//
//    @InjectMocks
//    UserController userController;
//
//    @SneakyThrows
//    @BeforeEach
//    void setUp() {
//        user = new User();
//        user.setUid(Long.valueOf(1));
//        user.setFirstName("Tom");
//        user.setLastName("Thecat");
//        user.setEmail("user@email.com");
//        user.setPassword("1234");
//        String dateStr = "2022-08-03";
//        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
//        user.setRegisterDate(date);
//        user.setGender("M");
//        user.setRoles("ROLE_USER");
//        user.setPhone("6140001111");
//        MockitoAnnotations.openMocks(this);
//        mockMvc = standaloneSetup(userController).setSingleView(mockView).build();
//    }



//    @Test
//    public void findByEmail() throws Exception {
//        String email = "user@email.com";
//        Principal mockPrincipal = Mockito.mock(Principal.class);
//        Mockito.when(mockPrincipal.getName()).thenReturn(email);
//        Optional<User> userOptional = Optional.of(user);
//        when(userRepository.findByEmail(email)).thenReturn(userOptional);
//        mockMvc.perform(get("/user/account").principal(mockPrincipal)
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("user",user))
//                .andExpect(view().name("user"))
//                .andExpect(model().attribute("user", hasSize(1)));

//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/user/account")
//                .principal(mockPrincipal)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        int status = response.getStatus();
//        //Assert.assertEquals("response status is wrong", 200, status);

//        verify(userRepository, times(1)).findByEmail(anyString());
//        verifyNoMoreInteractions(userRepository);
//    }

}