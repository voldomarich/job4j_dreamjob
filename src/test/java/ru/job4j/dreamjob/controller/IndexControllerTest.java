package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class IndexControllerTest {

    @Test
    void testGetIndex() {
        var model = mock(Model.class);
        var indexController = new IndexController();
        var viewName = indexController.getIndex();
        assertThat(viewName).isEqualTo("index");
    }
}
