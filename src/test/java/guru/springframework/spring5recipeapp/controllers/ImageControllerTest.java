package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.services.ImageService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    @InjectMocks
    ImageController imageController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).setControllerAdvice(new ControllerExceptionHandler()).build();
    }

    @Test
    public void getImageForm() throws Exception{
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        //when
        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findCommandById(anyLong());
    }

    @Test
    public void handleImagePost() throws Exception{
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain", "Spring Framework Guru".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService).saveImageFile(anyLong(), any());
    }

    @Test
    public void renderImageFromDB() throws Exception{
       //given
       RecipeCommand recipeCommand = new RecipeCommand();
       recipeCommand.setId(1L);

       String s = "fake image text";
       Byte[] bytesBoxed = new Byte[s.getBytes().length];

       int i = 0;

       for(byte primByte : s.getBytes()){
           bytesBoxed[i++] = primByte;
       }

       recipeCommand.setImage(bytesBoxed);

       when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

       //when

       MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

       byte[] responseBytes = response.getContentAsByteArray();

       assertEquals(s.getBytes().length, responseBytes.length);
    }

    @Test
    public void testGetImageNumberFormatException() throws Exception{
        mockMvc.perform(get("/recipe/sadffas/recipeimage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}