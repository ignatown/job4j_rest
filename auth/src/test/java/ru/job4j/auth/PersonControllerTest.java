package ru.job4j.auth;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.domain.Person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.ArgumentCaptor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:schema.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private Gson gson;

    @Test
    public void whenFindAll() throws Exception {
        List<Person> list = List.of(Person.of("test", "test"));
        when(personRepository.findAll()).thenReturn(list);
        this.mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(personRepository).findAll();
        assertThat(personRepository.findAll(), is(list));
    }

    @Test
    public void whenFindById() throws Exception {
        Person somePerson = Person.of("test", "test");
        when(personRepository.findById(0)).thenReturn(Optional.of(somePerson));
        this.mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(somePerson)))
                .andDo(print());
        this.mockMvc.perform(get("/person/0"))
                .andDo(print())
                .andExpect(status().isOk());
        ArgumentCaptor<Person> personArgument = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(personArgument.capture());
        assertThat(personArgument.getValue().getLogin(), is("test"));
        assertThat(personArgument.getValue().getPassword(), is("test"));
    }

    @Test
    public void whenCreate() throws Exception {
        this.mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(Person.of("test", "test"))))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void whenUpdate() throws Exception {
        Person person = Person.of("test", "test");
        String json = gson.toJson(person);
        this.mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
        person.setPassword("new pass");
        String updatedJson = gson.toJson(person);
        this.mockMvc.perform(put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenDelete() throws Exception {
        String json = gson.toJson(Person.of("test", "test"));
        this.mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
        this.mockMvc.perform(delete("/person/0"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}