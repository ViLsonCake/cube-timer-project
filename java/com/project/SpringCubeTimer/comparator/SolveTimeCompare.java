package com.project.SpringCubeTimer.comparator;

import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.service.TimerService;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class SolveTimeCompare implements Comparator<SolveEntity> {

    @Override
    public int compare(SolveEntity o1, SolveEntity o2) {
        if (o1.getTime().equalsIgnoreCase("DNF"))
            return 1;
        else if (o2.getTime().equalsIgnoreCase("DNF"))
            return -1;

        return Integer.compare(TimerService.toSeconds(o1.getTime()), TimerService.toSeconds(o2.getTime()));
    }
}
