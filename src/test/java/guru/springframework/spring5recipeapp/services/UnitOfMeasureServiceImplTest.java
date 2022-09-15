package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.domain.UnitOfMeasure;
import guru.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    UnitOfMeasureServiceImpl unitOfMeasureServiceImpl;

    @BeforeEach
    void setUp() {
        unitOfMeasureServiceImpl = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void listAllUoms() throws Exception {
        //given

        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        uom1.setDescription("a");

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId(2L);
        uom2.setDescription("b");

        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>(){{add(uom1); add(uom2);}};

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);

        //when
        Set<UnitOfMeasureCommand> commands = unitOfMeasureServiceImpl.listAllUoms();

        //then
        assertEquals(2, commands.size());
        verify(unitOfMeasureRepository).findAll();
    }

    @Test
    void saveIngredientCommand() {
    }
}