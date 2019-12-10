package edu.ucsb.cs56.ucsb_courses_search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.InsSearchSpecific;
import edu.ucsb.cs56.ucsb_courses_search.MySearchResult;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

@Controller
public class SearchByInstructorController {

    @Autowired   
    private CurriculumService curriculumService;

    // Hard code value for quarters
    private static final String[] quarters = {"20174", "20181", "20182", "20183", "20184", "20191", "20192", "20193", "20194", "20201"};

    @GetMapping("/search/byinstructor") 
    public String instructor(Model model) {
    	model.addAttribute("searchObject", new MySearchResult());
        return "search/byinstructor/search"; 
    }

	@GetMapping("/search/byinstructor/results")  
    public String singleQtrSearch( InsSearch insSearch, Model model) {
        model.addAttribute("insSearch", insSearch);
        
        // calls curriculumService method to get JSON from UCSB API
        String json = curriculumService.getJSON(insSearch.getInstructor(), insSearch.getQuarter());
        
        // maps json to a CoursePage object so values can be easily accessed
        CoursePage cp = CoursePage.fromJSON(json);
        
        // adds the json and CoursePage object as attributes so they can be accessed in the html, e.g. ${json} or ${cp.classes}
        model.addAttribute("json",json);
        model.addAttribute("cp",cp);
        
        return "search/byinstructor/results";
    } 

    @GetMapping("/search/byinstructor/specific")  // /search/instructor/specific
    public String specifc(Model model) {
        model.addAttribute("searchObject", new MySearchResult());
        return "search/byinstructor/specific/search"; 
    }  

    @GetMapping("/search/byinstructor/specific/results")
    public String singleQtrSearch(
        InsSearchSpecific insSearchSpecic,
        Model model
        ) {
        model.addAttribute("insSearchSpecic", insSearchSpecic);
        
        // calls curriculumService method to get JSON from UCSB API
        String json = curriculumService.getJSON(insSearchSpecic.getInstructor(), insSearchSpecic.getQuarter());
        
        // maps json to a CoursePage object so values can be easily accessed
        CoursePage cp = CoursePage.fromJSON(json);
        
        // adds the json and CoursePage object as attributes so they can be accessed in the html, e.g. ${json} or ${cp.classes}
        model.addAttribute("json",json);
        model.addAttribute("cp",cp);
        
        return "search/byinstructor/specific/results"; 
    }  

    @GetMapping("/search/byinstructor/multiquarter") // search/instructor/multiquarter
    public String multi(Model model) {
    	model.addAttribute("searchObject", new MySearchResult());
        return "search/byinstructor/multiquarter/search"; 
    }

    @GetMapping("/search/byinstructor/multiquarter/results")
    public String search(
        @RequestParam(name = "instructor", required = true) 
        String instructor,
        @RequestParam(name = "beginQuarter", required = true) 
        int beginQuarter,
        @RequestParam(name = "endQuarter", required = true) 
        int endQuarter,
        Model model
        ) {
        model.addAttribute("instructor", instructor);
        model.addAttribute("beginQuarter", beginQuarter);
        model.addAttribute("endQuarter", endQuarter);
        //model.addAttribute("quarter", quarter);
        
        // calls curriculumService method to get JSON from UCSB API
        // Note: the same curriculum service as the single quarter search above is used, so we will need to implement a function that takes in multiple quarters
	    String json = curriculumService.getJSON(instructor, quarters[beginQuarter]);
        
        // maps json to a CoursePage object so values can be easily accessed
        CoursePage cp = CoursePage.fromJSON(json);

        for(int i = beginQuarter + 1; i <= endQuarter; i++){
            cp.classes.addAll(CoursePage.fromJSON(curriculumService.getJSON(instructor, quarters[i])).classes);
        }
        
        // adds the json and CoursePage object as attributes so they can be accessed in the html, e.g. ${json} or ${cp.classes}
        model.addAttribute("json",json);
        model.addAttribute("cp",cp);
        

        return "search/byinstructor/multiquarter/results"; 
    }

}