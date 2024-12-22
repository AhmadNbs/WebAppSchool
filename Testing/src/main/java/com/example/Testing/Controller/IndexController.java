package com.example.Testing.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller class for handling requests related to different pages.
 */
@Controller
public class IndexController {

    /**
     * Display the index page.
     *
     * @return ModelAndView for the index page.
     */
    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    /**
     * Display the users page.
     *
     * @return ModelAndView for the users page.
     */
    @GetMapping("/users")
    public ModelAndView showUsersPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        return modelAndView;
    }

    /**
     * Display the schedules page.
     *
     * @return ModelAndView for the schedules page.
     */
    @GetMapping("/schedules")
    public ModelAndView showSchedulesPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("schedules");
        return modelAndView;
    }

    /**
     * Display the subjects page.
     *
     * @return ModelAndView for the subjects page.
     */
    @GetMapping("/subjects")
    public ModelAndView showSubjectsPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("subjects");
        return modelAndView;
    }

    /**
     * Display the classes page.
     *
     * @return ModelAndView for the classes page.
     */

    @GetMapping("/classes")
    public ModelAndView showClassesPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("classes");
        modelAndView.setViewName("class");
        return modelAndView;
    }

    /**
     * Display the marks page.
     *
     * @return ModelAndView for the marks page.
     */

    @GetMapping("/marks")
    public ModelAndView showMarksPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("note");
        return modelAndView;
    }
}
