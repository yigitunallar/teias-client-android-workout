package com.appchup.teias;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pronious on 24/09/2017.
 */

public final class Helper {

    public static List<String> parseProjects(List<Project> dashboard){
        List<String> titles = new ArrayList<>();

        for(Project dash:dashboard){
            titles.add(dash.getTitle());
        }
        return titles;
    }
}
