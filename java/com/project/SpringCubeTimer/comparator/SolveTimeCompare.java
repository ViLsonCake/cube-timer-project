package com.project.SpringCubeTimer.comparator;

import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.service.TimerService;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class SolveTimeCompare implements Comparator<SolveEntity> {

    @Override
    public int compare(SolveEntity o1, SolveEntity o2) {
        if (o1.getPenalty().equals("DNF"))
            return 1;
        else if (o2.getPenalty().equals("DNF"))
            return -1;

        String object1 = !(o1.getPenalty().equals("None")) && !(o1.getPenalty().equals("DNF")) ? o1.getPenalty() : o1.getTime();
        String object2 = !(o2.getPenalty().equals("None")) && !(o2.getPenalty().equals("DNF")) ? o2.getPenalty() : o2.getTime();

        return Integer.compare(TimerService.toSeconds(object1), TimerService.toSeconds(object2));
    }
}
