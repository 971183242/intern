package com.demo.workshop.intern.task;


import com.demo.workshop.intern.domain.profile.entity.Intern;
import com.demo.workshop.intern.domain.profile.entity.Team;
import com.demo.workshop.intern.domain.profile.service.ProfileDomService;
import com.demo.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.demo.workshop.intern.support.ActiveMQConfig;
import com.demo.workshop.intern.support.common.event.EventPublisher;
import com.demo.workshop.intern.support.common.event.NeedCheckInEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DailyCheckInReminderTask {
    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private ProfileDomService profileDomService;
    @Autowired
    private AttendanceDomService attendanceDomService;

    private final Logger logger = LoggerFactory.getLogger(DailyCheckInReminderTask.class);

//    @Scheduled(cron="0/5 * * * * MON-FRI")
    @Scheduled(cron="0 0 16 * * MON-FRI")
    public void execute() {
        List<Team> teamList = profileDomService.findAllTeams();
        Date today = Calendar.getInstance().getTime();
        Set<Intern> internNeedCheckIn = teamList.stream()
                .map(team -> profileDomService.findTeamInterns(team.getTeamId(), today, today))
                .flatMap(List::stream)
                .filter(this::noCheckInToday)
                .collect(Collectors.toSet());

        internNeedCheckIn.stream().forEach(this::publishNeedCheckInEvent);
    }

    private boolean noCheckInToday(Intern intern) {
        Date today = Calendar.getInstance().getTime();
        return ! attendanceDomService
                .findByInternIdAndWorkDay(intern.getDomainId(), today)
                .isPresent();
    }

    private void publishNeedCheckInEvent(Intern intern) {
        NeedCheckInEvent event = new NeedCheckInEvent();
        event.setDomainId(intern.getDomainId());
        event.setName(intern.getName());
        event.setEmail(intern.getEmail());
        eventPublisher.publish(event, ActiveMQConfig.INTERN_NEED_CHECKIN_QUEUE);
    }
}
