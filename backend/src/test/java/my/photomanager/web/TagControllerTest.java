package my.photomanager.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import my.photomanager.core.tag.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TagService tagService;

    @Test
    void shouldGetCameraTags() throws Exception {
        mockMvc.perform(get("/tag/cameraTags"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetLocationTags() throws Exception {
        mockMvc.perform(get("/tag/locationTags"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetCreationYearTags() throws Exception {
        mockMvc.perform(get("/tag/creationYearTags"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOrientationTags() throws Exception {
        mockMvc.perform(get("/tag/orientationTags"))
                .andExpect(status().isOk());
    }
}
