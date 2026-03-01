package game.alias.team;

import game.alias.auth.AuthUser;
import game.alias.common.ApiVersion;
import game.alias.team.domain.dto.TeamDto;
import game.alias.team.domain.request.CreateTeamRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(ApiVersion.V1Private + "/room/{roomId}/team")
@RequiredArgsConstructor
public class TeamController {

    @PostMapping("/leave/{teamId}")
    public ResponseEntity<Void>leave(@AuthenticationPrincipal AuthUser user, @PathVariable UUID teamId) {
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<TeamDto>create(@AuthenticationPrincipal AuthUser user, @PathVariable UUID roomId, @Valid @RequestBody CreateTeamRequest request){
        return null;
    }

    @DeleteMapping("/{teamId}")
    public  ResponseEntity<Void>deleteTeam(@PathVariable UUID teamId, @AuthenticationPrincipal AuthUser user){
        return null;
    }

    @PostMapping("/join/{teamId}")
    public ResponseEntity<TeamDto>joinTeam(@PathVariable UUID teamID, @AuthenticationPrincipal AuthUser user){
        return null;
    }
}
