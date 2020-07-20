package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.app.service.ProfileAppService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.interfaces.assembler.ProfileAssembler;
import com.oocl.workshop.intern.interfaces.dto.profile.InternDTO;
import com.oocl.workshop.intern.interfaces.dto.profile.TeamDTO;
import com.oocl.workshop.intern.interfaces.dto.profile.UserDTO;
import com.oocl.workshop.intern.support.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileAppService profileAppService;

    @PostMapping(value = "/teams", produces = APPLICATION_JSON_VALUE)
    public List<TeamDTO> findAllTeams() {
        List<Team> allTeams = profileAppService.findAllTeams();
        return allTeams.stream().map(ProfileAssembler::toTeamDTO).collect(toList());
    }

    @GetMapping(value = "/user/{domainId}", produces = APPLICATION_JSON_VALUE)
    public UserDTO findUser(@PathVariable String domainId) {
        Optional<User> userOptional = profileAppService.findUserByDomainId(domainId);
        return userOptional.isPresent() ? ProfileAssembler.toDTO(userOptional.get()) : null;
    }

    @PostMapping(value = "/createIntern", produces = APPLICATION_JSON_VALUE)
    public UserDTO createIntern(@RequestBody InternDTO userDTO) throws ParseException {
        User user = profileAppService.createUser(ProfileAssembler.toDO(userDTO));
        return ProfileAssembler.toDTO(user);
    }

    @PostMapping(value = "/updateIntern", produces = APPLICATION_JSON_VALUE)
    public UserDTO updateIntern(@RequestBody InternDTO userDTO) throws ParseException {
        User user = profileAppService.updateUser(ProfileAssembler.toDO(userDTO));
        return ProfileAssembler.toDTO(user);
    }

    @GetMapping(value = "/getInterns", produces = APPLICATION_JSON_VALUE)
    public List<InternDTO> getInterns(@RequestParam("date") String dateStr) throws ParseException {
        List<Intern> interns = profileAppService.getInterns(DateUtil.parseDate(dateStr));
        return interns.stream().map(ProfileAssembler::toInternDTO).collect(Collectors.toList());
    }

    @GetMapping(value = "/roles", produces = APPLICATION_JSON_VALUE)
    public List<String> getRoles() throws ParseException {
        return profileAppService.getRoles();
    }

    @PostMapping(value = "/deleteUser", produces = APPLICATION_JSON_VALUE)
    public boolean deleteUser(@RequestParam("domainId") String domainId) {
        return profileAppService.deleteUser(domainId);
    }
}
