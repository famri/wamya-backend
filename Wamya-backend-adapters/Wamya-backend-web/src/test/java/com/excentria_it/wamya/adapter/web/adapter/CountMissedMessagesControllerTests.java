package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.CountMessagesUseCase;
import com.excentria_it.wamya.application.port.in.CountMessagesUseCase.CountMessagesCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.CountMissedMessagesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {CountMissedMessagesController.class, RestApiExceptionHandler.class, ValidationHelper.class})
@WebMvcTest(controllers = CountMissedMessagesController.class)
public class CountMissedMessagesControllerTests {

    @Autowired
    private WebApplicationContext context;
    private static MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @MockBean
    private CountMessagesUseCase counMessagesUseCase;

    @Test
    void testCountMissedMessages() throws Exception {
        // given
        CountMessagesCommand command = CountMessagesCommand.builder().read("false")
                .subject("user").build();

        given(counMessagesUseCase.countMessages(any(CountMessagesCommand.class))).willReturn(5L);

        CountMissedMessagesResult result = CountMissedMessagesResult.builder().count(5L).build();

        // when //then
        mvc.perform(get("/users/me/messages/count").param("read", "false").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, CountMissedMessagesResult.class));

        then(counMessagesUseCase).should(times(1)).countMessages(command);
    }

}
