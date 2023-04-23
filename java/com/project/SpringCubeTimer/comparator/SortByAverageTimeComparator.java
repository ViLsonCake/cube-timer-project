package com.project.SpringCubeTimer.comparator;

import com.project.SpringCubeTimer.service.TimerService;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class SortByAverageTimeComparator implements Comparator<String[]> {

    @Override
    public int compare(String[] o1, String[] o2) {
        return Integer.compare(TimerService.toSeconds(o1[2]), TimerService.toSeconds(o2[2]));
    }
}
