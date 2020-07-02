package com.oocl.workshop.intern.app.service;

import com.oocl.workshop.intern.app.service.impl.ProfileAppServiceImpl;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static com.oocl.workshop.intern.infrastructure.util.DateUtil.parseDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ProfileAppServiceTest {
    @Mock
    private ProfileDomService profileDomService;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void should_get_interns_from_domain_service() throws ParseException {
        ProfileAppServiceImpl service = new ProfileAppServiceImpl();
        service.setProfileDomService(profileDomService);
        Intern intern1 = new Intern();
        Intern intern2 = new Intern();
        when(profileDomService.findTeamInterns("team", parseDate("2020-06-01"), parseDate("2020-06-30"))).thenReturn(Arrays.asList(intern1, intern2));
        List<Intern> interns = service.getInterns("team", parseDate("2020-06-30"));
        assertEquals(2, interns.size());
    }
}
